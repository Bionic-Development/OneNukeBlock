package de.takacick.upgradebody;

import de.takacick.upgradebody.access.PlayerProperties;
import de.takacick.upgradebody.registry.EffectRegistry;
import de.takacick.upgradebody.registry.EntityRegistry;
import de.takacick.upgradebody.registry.ItemRegistry;
import de.takacick.upgradebody.registry.ParticleRegistry;
import de.takacick.upgradebody.registry.bodypart.BodyParts;
import de.takacick.upgradebody.registry.entity.custom.BlockBreakEntity;
import de.takacick.upgradebody.registry.entity.projectiles.EnergyBulletEntity;
import de.takacick.upgradebody.server.commands.UpgradeBodyCommand;
import de.takacick.upgradebody.server.commands.UpgradeBodyShopCommand;
import de.takacick.upgradebody.server.datatracker.BodyPartTracker;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemGroup;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class UpgradeBody implements ModInitializer {

    public static final String MOD_ID = "upgradebody";
    private static final RegistryKey<ItemGroup> ITEM_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(MOD_ID, "upgradebody"));
    public static final Identifier IDENTIFIER = new Identifier(MOD_ID, "upgradebody");

    @Override
    public void onInitialize() {
        BodyPartTracker.register();
        BodyParts.register();
        EntityRegistry.register();
        ItemRegistry.register();
        ParticleRegistry.register();
        EffectRegistry.register();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            UpgradeBodyCommand.register(dispatcher);
            UpgradeBodyShopCommand.register(dispatcher);
        });

        Registry.register(Registries.ITEM_GROUP, ITEM_GROUP, FabricItemGroup.builder()
                .displayName(Text.translatable("itemGroup.upgradebody"))
                .icon(ItemRegistry.UPGRADE_SHOP_PORTAL::getDefaultStack)
                .entries((context, entries) -> {
                    entries.add(ItemRegistry.UPGRADE_SHOP_PORTAL);
                    entries.add(ItemRegistry.TANK_TRACKS);
                    entries.add(ItemRegistry.ENERGY_BELLY_CANNON);
                    entries.add(ItemRegistry.KILLER_DRILLER);
                    entries.add(ItemRegistry.CYBER_CHAINSAWS);
                }).build());

        ServerPlayNetworking.registerGlobalReceiver(new Identifier(UpgradeBody.MOD_ID, "cyberslice"), (server, player, handler, buf, responseSender) -> {
            if (player instanceof PlayerProperties playerProperties
                    && playerProperties.isUpgrading()
                    && playerProperties.getBodyPartManager().hasBodyPart(BodyParts.CYBER_CHAINSAWS)
                    && player.getMainHandStack().isEmpty() && player.getOffHandStack().isEmpty()) {
                int ticks = buf.readInt();
                playerProperties.setCyberSlice(ticks > 0);
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(new Identifier(UpgradeBody.MOD_ID, "killerdrilling"), (server, player, handler, buf, responseSender) -> {
            if (player instanceof PlayerProperties playerProperties
                    && playerProperties.isUpgrading()
                    && playerProperties.getBodyPartManager().hasBodyPart(BodyParts.KILLER_DRILLER)) {
                int ticks = buf.readInt();
                playerProperties.setKillerDrilling(ticks > 0);
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(new Identifier(UpgradeBody.MOD_ID, "energybellycannon"), (server, player, handler, buf, responseSender) -> {
            if (player instanceof PlayerProperties playerProperties
                    && playerProperties.isUpgrading()
                    && playerProperties.getBodyPartManager().hasBodyPart(BodyParts.ENERGY_BELLY_CANNON)) {

                if (player.getHungerManager().getFoodLevel() <= 6) {
                    playerProperties.setEnergyBellyBlast(false);
                    return;
                }

                int ticks = buf.readInt();
                playerProperties.setEnergyBellyBlast(ticks > 0);

                if (playerProperties.getEnergyBellyBlastUsageTicks() > 15 && ticks > 0) {
                    player.getHungerManager().addExhaustion(1.0f);
                    Vec3d vec3d = player.getRotationVector();
                    World world = player.getWorld();
                    Vec3d pos = new Vec3d(player.getX(), player.getBodyY(0.71f), player.getZ()).add(vec3d);
                    EnergyBulletEntity energyBulletEntity = new EnergyBulletEntity(world, pos.getX(), pos.getY(), pos.getZ(), player);
                    energyBulletEntity.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, 2.6F, 1.0F);
                    world.spawnEntity(energyBulletEntity);
                    world.playSound(null, energyBulletEntity.getBlockPos(), SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE.value(), SoundCategory.PLAYERS, 1f, 3f);
                }
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(new Identifier(UpgradeBody.MOD_ID, "bionicheadbutt"), (server, player, handler, buf, responseSender) -> {
            if (player instanceof PlayerProperties playerProperties
                    && playerProperties.isUpgrading() && playerProperties.getBodyPartManager().isHeadOnly()) {
                server.execute(() -> {
                    if (player.isOnGround()) {
                        playerProperties.setHeadbutt(true);
                        player.setVelocity(player.getRotationVector(0f, player.getYaw()).multiply(1, 0, 1).normalize().add(0, 0.2, 0));
                        player.velocityDirty = true;
                        player.velocityModified = true;
                    }
                });
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(new Identifier(UpgradeBody.MOD_ID, "bionicheadbuttbreak"), (server, player, handler, buf, responseSender) -> {
            if (player instanceof PlayerProperties playerProperties
                    && playerProperties.isUpgrading() && playerProperties.getBodyPartManager().isHeadOnly()) {
                int i = buf.readInt();
                BlockPos blockPos = i == 1 ? buf.readBlockPos() : null;
                server.execute(() -> {
                    World world = player.getWorld();
                    playerProperties.setHeadbutt(false);

                    if (blockPos == null) {
                        return;
                    }

                    BlockState blockState = world.getBlockState(blockPos);

                    if (blockState.getHardness(world, blockPos) >= 0) {

                        if (!world.isClient) {
                            boolean bl = false;
                            for (Entity breakEntity : world.getOtherEntities(null, new Box(blockPos))) {
                                if (breakEntity instanceof BlockBreakEntity blockBreakEntity && breakEntity.isAlive()) {
                                    bl = true;

                                    blockBreakEntity.setBreakProgress(blockBreakEntity.getBreakProgress() + 0.5f);

                                    if (blockBreakEntity.getBreakProgress() >= blockBreakEntity.getMaxBreakProgress()) {
                                        world.breakBlock(blockPos, true, player);
                                        world.spawnEntity(new ExperienceOrbEntity(world, blockBreakEntity.getX(), blockBreakEntity.getY(), blockBreakEntity.getZ(), world.getRandom().nextInt(7) + 1));
                                    }
                                    break;
                                }
                            }

                            if (!bl && !blockState.isAir()) {
                                BlockBreakEntity blockBreakEntity = new BlockBreakEntity(world, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);
                                blockBreakEntity.setBreakProgress(0.5f);
                                world.spawnEntity(blockBreakEntity);
                            }
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
            ServerPlayNetworking.send(serverPlayerEntity, new Identifier(UpgradeBody.MOD_ID, "healthupdate"), packetByteBuf);
        }
    }

}
