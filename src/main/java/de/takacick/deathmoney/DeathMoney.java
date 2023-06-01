package de.takacick.deathmoney;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import de.takacick.deathmoney.access.PlayerProperties;
import de.takacick.deathmoney.commands.DeathShopCommand;
import de.takacick.deathmoney.registry.EntityRegistry;
import de.takacick.deathmoney.registry.ItemRegistry;
import de.takacick.deathmoney.registry.ParticleRegistry;
import de.takacick.deathmoney.registry.entity.projectiles.TntNukeEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.FluidBlock;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DeathMoney implements ModInitializer {

    public static final String MOD_ID = "deathmoney";
    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "deathmoney"), () -> new ItemStack(ItemRegistry.DEATH_SHOP_PORTAL));
    public static final Identifier IDENTIFIER = new Identifier(MOD_ID, "deathmoney");

    @Override
    public void onInitialize() {
        ItemRegistry.register();
        EntityRegistry.register();
        ParticleRegistry.register();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            LiteralCommandNode<ServerCommandSource> rootNode = CommandManager.literal("setdeathmultiplier").requires((source) -> {
                return source.hasPermissionLevel(2);
            }).then((CommandManager.argument("player", EntityArgumentType.players())
                    .then(CommandManager.argument("multiplier", DoubleArgumentType.doubleArg()).executes((context) -> {
                        double multiplier = DoubleArgumentType.getDouble(context, "multiplier");
                        Collection<ServerPlayerEntity> playerEntities = EntityArgumentType.getPlayers(context, "target");

                        if (playerEntities != null) {
                            playerEntities.forEach(serverPlayerEntity -> {
                                context.getSource().sendFeedback(Text.of("Set " + serverPlayerEntity.getName().getString() + "'s Death multiplier to: §e" + multiplier), false);
                                ((PlayerProperties) serverPlayerEntity).setDeathMultiplier(multiplier);
                            });
                        } else {
                            context.getSource().sendFeedback(Text.of("§cUnknown players!"), false);
                        }
                        return 1;
                    })))).build();
            dispatcher.getRoot().addChild(rootNode);

            rootNode = CommandManager.literal("setdeaths").requires((source) -> {
                return source.hasPermissionLevel(2);
            }).then((CommandManager.argument("player", EntityArgumentType.players())
                    .then(CommandManager.argument("deaths", IntegerArgumentType.integer()).executes((context) -> {
                        int deaths = IntegerArgumentType.getInteger(context, "deaths");
                        Collection<ServerPlayerEntity> playerEntities = EntityArgumentType.getPlayers(context, "player");

                        if (playerEntities != null) {
                            playerEntities.forEach(serverPlayerEntity -> {
                                context.getSource().sendFeedback(Text.of("Set " + serverPlayerEntity.getName().getString() + "'s Deaths to: §e" + deaths), false);
                                ((PlayerProperties) serverPlayerEntity).setDeaths(deaths);
                            });
                        } else {
                            context.getSource().sendFeedback(Text.of("§cUnknown players!"), false);
                        }
                        return 1;
                    })))).build();
            dispatcher.getRoot().addChild(rootNode);


            DeathShopCommand.register(dispatcher);
        });

        ServerPlayNetworking.registerGlobalReceiver(new Identifier(MOD_ID, "deathnote"), (server, player, handler, buf, responseSender) -> {
            try {
                ItemStack itemStack = player.getMainHandStack().isOf(ItemRegistry.DEATH_NOTE) ? player.getMainHandStack() : player.getOffHandStack();

                if (!itemStack.isOf(ItemRegistry.DEATH_NOTE)) {
                    player.closeHandledScreen();
                } else {
                    ServerPlayerEntity target = server.getPlayerManager().getPlayer(buf.readString());

                    if (target instanceof PlayerProperties playerProperties) {
                        switch (target.getRandom().nextBetween(1, 3)) {
                            default -> playerProperties.setEarthFangsTicks(60);
                            case 2 -> {
                                target.getEntityWorld().playSound(null, target.getX(), target.getY() + 100, target.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 6f, 1f);
                                target.getEntityWorld().playSound(null, target.getX(), target.getY() + 10, target.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 6f, 1f);
                                World world = target.getEntityWorld();
                                TntNukeEntity tntNukeEntity = new TntNukeEntity(EntityRegistry.TNT_NUKE, world);
                                tntNukeEntity.setPos(target.getX(), target.getY() + 100, target.getZ());
                                world.spawnEntity(tntNukeEntity);
                            }
                            case 3 -> {
                                target.getWorld().playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.ENTITY_WARDEN_HEARTBEAT, SoundCategory.AMBIENT, 1f, 1f);
                                for (int y = -2; y < 0; y++) {
                                    for (BlockPos blockPos : generateCircle(target.getBlockPos(), 5, false)) {
                                        target.getWorld().setBlockState(blockPos.add(0, y, 0), ItemRegistry.BLOOD_BLOCK.getDefaultState().with(FluidBlock.LEVEL, 15));
                                    }
                                }
                            }
                        }

                        player.networkHandler.sendPacket(new PlaySoundS2CPacket(SoundEvents.ITEM_BOOK_PAGE_TURN, SoundCategory.PLAYERS, player.getX(), player.getBodyY(0.5), player.getZ(), 1f, 1f, Random.createThreadSafe().nextLong()));
                        player.closeHandledScreen();
                        itemStack.damage(1, player, en -> {
                        });
                    }
                }
            } catch (Exception exception) {

            }
        });
    }

    public static void updateEntityHealth(LivingEntity livingEntity, double maxHealth, boolean heal) {
        livingEntity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
        livingEntity.setHealth(heal ? livingEntity.getMaxHealth() : Math.min(livingEntity.getMaxHealth(), livingEntity.getHealth()));

        PacketByteBuf packetByteBuf = PacketByteBufs.create();
        packetByteBuf.writeDouble(maxHealth);
        packetByteBuf.writeInt((int) Math.ceil(maxHealth / 2d));
        packetByteBuf.writeFloat(livingEntity.getHealth());

        if (livingEntity instanceof ServerPlayerEntity serverPlayerEntity) {
            ServerPlayNetworking.send(serverPlayerEntity, new Identifier(DeathMoney.MOD_ID, "healthupdate"), packetByteBuf);
        }
    }

    public static List<BlockPos> generateSphere(BlockPos centerBlock, int radius, boolean hollow) {

        List<BlockPos> circleBlocks = new ArrayList<>();

        int bx = centerBlock.getX();
        int by = centerBlock.getY();
        int bz = centerBlock.getZ();

        for (int x = bx - radius; x <= bx + radius; x++) {
            for (int y = by - radius; y <= by + radius; y++) {
                for (int z = bz - radius; z <= bz + radius; z++) {
                    double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)) + ((by - y) * (by - y)));
                    if (distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {
                        circleBlocks.add(new BlockPos(x, y, z));
                    }
                }
            }
        }

        return circleBlocks;
    }

    public static List<BlockPos> generateCircle(BlockPos centerBlock, int radius, boolean hollow) {
        List<BlockPos> circleBlocks = new ArrayList<>();

        int bx = centerBlock.getX();
        int bz = centerBlock.getZ();

        for (int x = bx - radius; x <= bx + radius; x++) {
            for (int z = bz - radius; z <= bz + radius; z++) {
                double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)));
                if (distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {
                    circleBlocks.add(new BlockPos(x, centerBlock.getY(), z));
                }
            }
        }
        return circleBlocks;
    }
}
