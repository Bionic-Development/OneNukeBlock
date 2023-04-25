package de.takacick.everythinghearts;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import de.takacick.everythinghearts.access.LivingProperties;
import de.takacick.everythinghearts.access.PlayerProperties;
import de.takacick.everythinghearts.commands.ProgressBarCommand;
import de.takacick.everythinghearts.registry.EntityRegistry;
import de.takacick.everythinghearts.registry.ItemRegistry;
import de.takacick.everythinghearts.registry.ParticleRegistry;
import de.takacick.everythinghearts.registry.entity.living.ProtoEntity;
import de.takacick.utils.BionicUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static de.takacick.everythinghearts.commands.ProgressBarCommand.*;

public class EverythingHearts implements ModInitializer {

    public static final String MOD_ID = "everythinghearts";
    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "everythinghearts"), () -> new ItemStack(ItemRegistry.MYSTIC_HEART_TOUCH));
    public static final Identifier IDENTIFIER = new Identifier(MOD_ID, "everythinghearts");

    public static final List<Block> HEART_BLOCKS = new ArrayList<>();
    public static final HashMap<Block, Block> HEART_TOUCH_BLOCKS = new HashMap<>();
    public static final HashMap<EntityType<?>, EntityType<?>> HEART_TOUCH_ENTITIES = new HashMap<>();

    @Override
    public void onInitialize() {
        ItemRegistry.register();
        EntityRegistry.register();
        ParticleRegistry.register();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            ProgressBarCommand.register(dispatcher);

            LiteralCommandNode<ServerCommandSource> rootNode = CommandManager.literal("sethearttouch")
                    .requires(source -> {
                        return source.hasPermissionLevel(2);
                    }).then(CommandManager.argument("player", EntityArgumentType.players())
                            .then(CommandManager.argument("level", IntegerArgumentType.integer(1, 2)).executes(context -> {

                                        Collection<ServerPlayerEntity> playerEntities = EntityArgumentType.getPlayers(context, "player");
                                        int touchLevel = IntegerArgumentType.getInteger(context, "level");

                                        playerEntities.forEach(playerEntity -> {
                                            ((PlayerProperties) playerEntity).setHeartTouchLevel(touchLevel);
                                            context.getSource().sendFeedback(Text.literal("Set " + playerEntity.getName().getString() + "'s Heart Touch Level to " + touchLevel), false);
                                        });

                                        return 1;
                                    })
                            )).build();
            dispatcher.getRoot().addChild(rootNode);
        });

        ServerPlayNetworking.registerGlobalReceiver(new Identifier(MOD_ID, "hearttouch"), (server, player, handler, buf, responseSender) -> {
            if (player instanceof PlayerProperties playerProperties) {
                playerProperties.setHeartTouch(!playerProperties.hasHeartTouch());

                BionicUtils.sendEntityStatus(player.getWorld(), player, EverythingHearts.IDENTIFIER, playerProperties.hasHeartTouch() ? 5 : 6);
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(new Identifier(MOD_ID, "progressbartick"), (server, player, handler, buf, responseSender) -> {
            ticks = Math.min(ticks + tickStep, maxTicks);
            progressBar.setPercent((float) ticks / (float) maxTicks);
        });

        ServerPlayNetworking.registerGlobalReceiver(new Identifier(MOD_ID, "heartsonicscreech"), (server, player, handler, buf, responseSender) -> {
            if (player.getVehicle() instanceof ProtoEntity protoEntity) {
                server.execute(() -> {
                    World world = player.getEntityWorld();
                    float f = 1;
                    Vec3d vec3d = protoEntity.getEyePos().add(0, -0.25, 0);
                    Vec3d vec3d3 = protoEntity.getRotationVector();
                    Vec3d vec3d4;
                    world.playSound(null, protoEntity.getX(), protoEntity.getBodyY(0.5), protoEntity.getZ(), SoundEvents.ENTITY_WARDEN_SONIC_BOOM, SoundCategory.PLAYERS, 1.0f, 1.0f);
                    for (int h = 1; h < MathHelper.floor(f * 15) + f * 7; ++h) {
                        vec3d4 = vec3d.add(vec3d3.multiply(h));
                        if (h > 4) {
                            if (world.getBlockState(new BlockPos(vec3d4)).getMaterial().blocksMovement()) {
                                break;
                            }
                        }
                        ((ServerWorld) world).spawnParticles(ParticleRegistry.HEART_SONIC_BOOM, vec3d4.x, vec3d4.y, vec3d4.z,
                                1, 0.0, 0.0, 0.0, 0.0);
                        if (h > 3) {
                            world.getOtherEntities(null, Box.from(vec3d4).expand(6)).forEach(entity -> {
                                if (entity instanceof LivingEntity livingEntity
                                        && livingEntity instanceof LivingProperties livingProperties) {
                                    livingProperties.tryToHeartInfect(protoEntity);
                                }
                            });

                            generateSphere(new BlockPos(vec3d4), 5, false).forEach(blockPos -> {
                                BlockState blockState = world.getBlockState(blockPos);
                                EverythingHearts.replaceBlock(world, blockState, blockPos, ((PlayerProperties) player).getHeartTouchLevel());
                            });
                        }
                    }
                });
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
            ServerPlayNetworking.send(serverPlayerEntity, new Identifier(EverythingHearts.MOD_ID, "healthupdate"), packetByteBuf);
        }
    }


    public static List<BlockPos> generateSphere(BlockPos centerBlock, int radius, boolean hollow) {

        List<BlockPos> circleBlocks = new ArrayList<BlockPos>();

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

    public static boolean replaceBlock(World world, BlockState blockState, BlockPos blockPos, int heartTouchLevel) {
        if (!blockState.isAir() && !((heartTouchLevel == 1 && blockState.isOf(ItemRegistry.BASIC_HEART_BLOCK))
                || (heartTouchLevel == 2 && blockState.isOf(ItemRegistry.MULTI_HEART_BLOCK)))
                && !EverythingHearts.HEART_TOUCH_BLOCKS.values().contains(blockState.getBlock())) {

            world.setBlockState(blockPos, EverythingHearts.HEART_TOUCH_BLOCKS.getOrDefault(blockState.getBlock(),
                    heartTouchLevel == 1 ? ItemRegistry.BASIC_HEART_BLOCK
                            : ItemRegistry.MULTI_HEART_BLOCK).getStateWithProperties(blockState));
            if (world.getRandom().nextDouble() <= 0.2
                    || EverythingHearts.HEART_TOUCH_BLOCKS.keySet().contains(blockState.getBlock())) {
                world.syncWorldEvent(102391239, blockPos, 3);
            }
            return true;
        }

        return false;
    }

    static {
        HEART_BLOCKS.add(ItemRegistry.HEART_LOG);
        HEART_BLOCKS.add(ItemRegistry.HEART_PLANKS);
        HEART_BLOCKS.add(ItemRegistry.HEART_LEAVES);
        HEART_BLOCKS.add(ItemRegistry.HEART_CRAFTING_TABLE);
        HEART_BLOCKS.add(ItemRegistry.HEARTBALE);
        HEART_BLOCKS.add(ItemRegistry.HEART_CHEST);
        HEART_BLOCKS.add(ItemRegistry.BASIC_HEART_BLOCK);
        HEART_BLOCKS.add(ItemRegistry.MULTI_HEART_BLOCK);
        HEART_BLOCKS.add(ItemRegistry.WEATHER_HEART_BEACON);

        HEART_TOUCH_BLOCKS.put(Blocks.OAK_LOG, ItemRegistry.HEART_LOG);
        HEART_TOUCH_BLOCKS.put(Blocks.SPRUCE_LOG, ItemRegistry.HEART_LOG);
        HEART_TOUCH_BLOCKS.put(Blocks.BIRCH_LOG, ItemRegistry.HEART_LOG);
        HEART_TOUCH_BLOCKS.put(Blocks.JUNGLE_LOG, ItemRegistry.HEART_LOG);
        HEART_TOUCH_BLOCKS.put(Blocks.ACACIA_LOG, ItemRegistry.HEART_LOG);
        HEART_TOUCH_BLOCKS.put(Blocks.DARK_OAK_LOG, ItemRegistry.HEART_LOG);
        HEART_TOUCH_BLOCKS.put(Blocks.MANGROVE_LOG, ItemRegistry.HEART_LOG);
        HEART_TOUCH_BLOCKS.put(Blocks.STRIPPED_OAK_LOG, ItemRegistry.HEART_LOG);
        HEART_TOUCH_BLOCKS.put(Blocks.STRIPPED_SPRUCE_LOG, ItemRegistry.HEART_LOG);
        HEART_TOUCH_BLOCKS.put(Blocks.STRIPPED_BIRCH_LOG, ItemRegistry.HEART_LOG);
        HEART_TOUCH_BLOCKS.put(Blocks.STRIPPED_JUNGLE_LOG, ItemRegistry.HEART_LOG);
        HEART_TOUCH_BLOCKS.put(Blocks.STRIPPED_ACACIA_LOG, ItemRegistry.HEART_LOG);
        HEART_TOUCH_BLOCKS.put(Blocks.STRIPPED_DARK_OAK_LOG, ItemRegistry.HEART_LOG);
        HEART_TOUCH_BLOCKS.put(Blocks.STRIPPED_MANGROVE_LOG, ItemRegistry.HEART_LOG);

        HEART_TOUCH_BLOCKS.put(Blocks.OAK_LEAVES, ItemRegistry.HEART_LEAVES);
        HEART_TOUCH_BLOCKS.put(Blocks.SPRUCE_LEAVES, ItemRegistry.HEART_LEAVES);
        HEART_TOUCH_BLOCKS.put(Blocks.BIRCH_LEAVES, ItemRegistry.HEART_LEAVES);
        HEART_TOUCH_BLOCKS.put(Blocks.JUNGLE_LEAVES, ItemRegistry.HEART_LEAVES);
        HEART_TOUCH_BLOCKS.put(Blocks.ACACIA_LEAVES, ItemRegistry.HEART_LEAVES);
        HEART_TOUCH_BLOCKS.put(Blocks.DARK_OAK_LEAVES, ItemRegistry.HEART_LEAVES);
        HEART_TOUCH_BLOCKS.put(Blocks.MANGROVE_LEAVES, ItemRegistry.HEART_LEAVES);
        HEART_TOUCH_BLOCKS.put(Blocks.AZALEA_LEAVES, ItemRegistry.HEART_LEAVES);
        HEART_TOUCH_BLOCKS.put(Blocks.CRAFTING_TABLE, ItemRegistry.HEART_CRAFTING_TABLE);
        HEART_TOUCH_BLOCKS.put(Blocks.HAY_BLOCK, ItemRegistry.HEARTBALE);
        HEART_TOUCH_BLOCKS.put(Blocks.CHEST, ItemRegistry.HEART_CHEST);
        HEART_TOUCH_BLOCKS.put(Blocks.DIAMOND_ORE, ItemRegistry.HEARTMOND_ORE);
        HEART_TOUCH_BLOCKS.put(Blocks.DEEPSLATE_DIAMOND_ORE, ItemRegistry.DEEPSLATE_HEARTMOND_ORE);
        HEART_TOUCH_BLOCKS.put(ItemRegistry.WEATHER_HEART_BEACON, ItemRegistry.WEATHER_HEART_BEACON);

        HEART_TOUCH_ENTITIES.put(EntityType.SHEEP, EntityType.SHEEP);
        HEART_TOUCH_ENTITIES.put(EntityType.COW, EntityType.COW);
        HEART_TOUCH_ENTITIES.put(EntityType.MOOSHROOM, EntityType.MOOSHROOM);
        HEART_TOUCH_ENTITIES.put(EntityType.PIG, EntityType.PIG);
        HEART_TOUCH_ENTITIES.put(EntityType.CHICKEN, EntityType.CHICKEN);
        HEART_TOUCH_ENTITIES.put(EntityType.BEE, EntityType.BEE);
        HEART_TOUCH_ENTITIES.put(EntityType.VILLAGER, EntityType.VILLAGER);
        HEART_TOUCH_ENTITIES.put(EntityType.IRON_GOLEM, EntityType.IRON_GOLEM);
        HEART_TOUCH_ENTITIES.put(EntityType.PILLAGER, EntityType.PILLAGER);
        HEART_TOUCH_ENTITIES.put(EntityType.CREEPER, EntityType.CREEPER);
        HEART_TOUCH_ENTITIES.put(EntityType.ZOMBIE, EntityType.ZOMBIE);
        HEART_TOUCH_ENTITIES.put(EntityType.HUSK, EntityType.HUSK);
        HEART_TOUCH_ENTITIES.put(EntityType.SKELETON, EntityRegistry.REVIVED_PLAYER);
        HEART_TOUCH_ENTITIES.put(EntityType.WARDEN, EntityRegistry.LOVER_WARDEN);
    }
}
