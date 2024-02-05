package de.takacick.secretcraftbase;

import de.takacick.secretcraftbase.access.PlayerProperties;
import de.takacick.secretcraftbase.registry.EntityRegistry;
import de.takacick.secretcraftbase.registry.ItemRegistry;
import de.takacick.secretcraftbase.registry.ParticleRegistry;
import de.takacick.secretcraftbase.registry.item.Frog;
import de.takacick.secretcraftbase.server.commands.SecretCraftBaseCommand;
import de.takacick.secretcraftbase.server.datatracker.SecretCraftBaseTracker;
import de.takacick.secretcraftbase.server.utils.EntityNbtHelper;
import de.takacick.utils.BionicUtils;
import de.takacick.utils.BionicUtilsClient;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameMode;

public class SecretCraftBase implements ModInitializer {

    public static final String MOD_ID = "secretcraftbase";
    private static final RegistryKey<ItemGroup> ITEM_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(MOD_ID, "secretcraftbase"));
    public static final Identifier IDENTIFIER = new Identifier(MOD_ID, "secretcraftbase");

    @Override
    public void onInitialize() {
        SecretCraftBaseTracker.register();
        EntityRegistry.register();
        ItemRegistry.register();
        ParticleRegistry.register();

        Registry.register(Registries.ITEM_GROUP, ITEM_GROUP, FabricItemGroup.builder()
                .displayName(Text.translatable("itemGroup.secretcraftbase"))
                .icon(ItemRegistry.IRON_GOLEM_FARM::getDefaultStack)
                .entries((context, entries) -> {
                    entries.add(ItemRegistry.IRON_GOLEM_FARM);
                    entries.add(ItemRegistry.TREASURY_ROOM);
                    entries.add(ItemRegistry.ARMORY_ROOM);
                    entries.add(ItemRegistry.XP_FARM);
                    entries.add(ItemRegistry.SECRET_PIG_POWERED_PORTAL);
                    entries.add(ItemRegistry.PIG);
                    entries.add(ItemRegistry.NETHER_PORTAL_BLOCK);
                    entries.add(ItemRegistry.REDSTONE_ORE_CHUNKS);
                    entries.add(ItemRegistry.STONE_SECRET_REDSTONE_MIRROR_MELTER_ITEM);
                    entries.add(ItemRegistry.DEEPSLATE_SECRET_REDSTONE_MIRROR_MELTER_ITEM);
                    entries.add(ItemRegistry.SECRET_MAGIC_WELL);
                    entries.add(ItemRegistry.SECRET_GIANT_JUMPY_SLIME);
                    entries.add(ItemRegistry.IRON_HEART_CARVER);
                    entries.add(ItemRegistry.HEART);
                    entries.add(ItemRegistry.SECRET_FAKE_SUN_ITEM);
                    entries.add(ItemRegistry.BIG_WHITE_BLOCK_ITEM);
                    Registries.FROG_VARIANT.forEach(frogVariant -> {
                        ItemStack itemStack = ItemRegistry.FROG.getDefaultStack();
                        NbtCompound nbtCompound = new NbtCompound();
                        nbtCompound.putString("variant", Registries.FROG_VARIANT.getId(frogVariant).toString());
                        entries.add(EntityNbtHelper.setNbt(itemStack, nbtCompound));
                    });
                }).build());

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            SecretCraftBaseCommand.register(dispatcher);
        });

        ServerPlayNetworking.registerGlobalReceiver(new Identifier(MOD_ID, "frogpickup"), (server, player, handler, buf, responseSender) -> {
            int id = buf.readInt();

            ServerWorld world = player.getServerWorld();
            Entity entity = world.getEntityById(id);
            if (entity instanceof FrogEntity frogEntity) {
                server.execute(() -> {
                    player.swingHand(Hand.MAIN_HAND, true);

                    world.playSound(null, player.getX(), player.getBodyY(0.5), player.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 1f, 1f);
                    world.playSound(null, player.getX(), player.getBodyY(0.5), player.getZ(), SoundEvents.ENTITY_FROG_HURT, SoundCategory.PLAYERS, 1f, 1f);

                    ItemStack itemStack = EntityNbtHelper.fromEntity(ItemRegistry.FROG, frogEntity);

                    if (!player.getMainHandStack().isEmpty()) {
                        ItemStack copy = player.getMainHandStack().copy();
                        player.getMainHandStack().setCount(0);
                        player.equipStack(EquipmentSlot.MAINHAND, itemStack);
                        player.getInventory().offerOrDrop(copy);
                    } else {
                        player.equipStack(EquipmentSlot.MAINHAND, itemStack);
                    }

                    frogEntity.discard();
                });
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(new Identifier(MOD_ID, "basehuntround"), (server, player, handler, buf, responseSender) -> {
            if (!SecretCraftBaseCommand.running) {
                player.changeGameMode(GameMode.SPECTATOR);
                SecretCraftBaseCommand.running = true;
                SecretCraftBaseCommand.progressBar.setPercent(0f);
                SecretCraftBaseCommand.ticks = 0;
                SecretCraftBaseCommand.lastPlayer = player.getUuid();
                SecretCraftBaseCommand.progressBar.setVisible(true);
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(new Identifier(SecretCraftBase.MOD_ID, "frog"), (server, player, handler, buf, responseSender) -> {
            if (player.getMainHandStack().getItem() instanceof Frog
                    && player instanceof PlayerProperties playerProperties
                    && !playerProperties.isUsingFrogTongue()) {
                int type = buf.readInt();

                ItemStack itemStack = player.getMainHandStack();

                ServerWorld world = player.getServerWorld();

                if (type == 0) {
                    BlockPos blockPos = buf.readBlockPos();
                    server.execute(() -> {
                        Frog.eatBlock(player, itemStack, world, blockPos);
                    });
                } else if (type == 1) {
                    int id = buf.readInt();

                    Entity entity = world.getEntityById(id);
                    if (entity != null) {
                        server.execute(() -> {
                            Frog.eatEntity(player, itemStack, world, world.getEntityById(id));
                        });
                    }
                } else if (type == 2) {
                    playerProperties.setFrogTongueLength(15f);
                    BionicUtils.sendEntityStatus(world, player, SecretCraftBase.IDENTIFIER, 8);
                }
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
            ServerPlayNetworking.send(serverPlayerEntity, new Identifier(SecretCraftBase.MOD_ID, "healthupdate"), packetByteBuf);
        }
    }

    public static int getColor(float time) {
        float r = (float) ((time / 4.0f) % Math.PI);
        float s = (MathHelper.sin(r + 0.0f) + 1.0f) * 0.5f;
        float t = 1f;
        float u = (MathHelper.sin(r + 4.1887903f) + 1.0f) * 0.1f;
        return BionicUtilsClient.getRainbow().getIntFromColor((int) (s * 255), (int) (t * 255), (int) (u * 255));
    }
}
