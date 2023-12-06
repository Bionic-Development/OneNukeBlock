package de.takacick.emeraldmoney;

import de.takacick.emeraldmoney.access.PlayerProperties;
import de.takacick.emeraldmoney.client.gui.EmeraldCounter;
import de.takacick.emeraldmoney.client.gui.EmeraldToast;
import de.takacick.emeraldmoney.registry.EntityRegistry;
import de.takacick.emeraldmoney.registry.ItemRegistry;
import de.takacick.emeraldmoney.registry.ParticleRegistry;
import de.takacick.emeraldmoney.registry.entity.custom.EmeraldShopPortalEntity;
import de.takacick.emeraldmoney.registry.entity.custom.renderer.EmeraldShopPortalEntityRenderer;
import de.takacick.emeraldmoney.registry.entity.custom.renderer.ShopItemEntityRenderer;
import de.takacick.emeraldmoney.registry.entity.custom.renderer.VillagerNoseEntityRenderer;
import de.takacick.emeraldmoney.registry.entity.custom.renderer.VillagerSpikeEntityRenderer;
import de.takacick.emeraldmoney.registry.entity.living.CreepagerEntity;
import de.takacick.emeraldmoney.registry.entity.living.renderer.CreepagerEntityRenderer;
import de.takacick.emeraldmoney.registry.entity.projectile.renderer.CustomBlockEntityRenderer;
import de.takacick.emeraldmoney.registry.entity.projectile.renderer.PillagerEntityRenderer;
import de.takacick.emeraldmoney.registry.particles.*;
import de.takacick.emeraldmoney.server.explosion.CreepagerExplosionHandler;
import de.takacick.emeraldmoney.server.explosion.PillagerExplosionHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.entity.EmptyEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class EmeraldMoneyClient implements ClientModInitializer {
    private final static List<Integer> EMERALD_COLORS = new ArrayList<>();

    static {
        EMERALD_COLORS.add(0x007B18);
        EMERALD_COLORS.add(0x17DD62);
        EMERALD_COLORS.add(0x82F6AD);
    }

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityRegistry.BLOCK_BREAK, EmptyEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.EMERALD_SHOP_PORTAL, EmeraldShopPortalEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.SHOP_ITEM, ShopItemEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.PILLAGER, PillagerEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.VILLAGER_NOSE, VillagerNoseEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.CREEPAGER, CreepagerEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.FALLING_BLOCK, CustomBlockEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.VILLAGER_SPIKE, VillagerSpikeEntityRenderer::new);

        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.DUST, DustParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.TOTEM_DUST, TotemDustParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.EMERALD_POOF, EmeraldPoofParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.EMERALD_PORTAL, EmeraldPortalParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.EMERALD_TOTEM, EmeraldTotemParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.EMERALD_ITEMS, EmeraldItemsParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.EMERALD_EXPLOSION, EmeraldExplosionParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.EMERALD_EXPLOSION_EMITTER, new EmeraldExplosionEmitterParticle.Factory());
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.EMERALD_SPELL, EmeraldSpellParticle.Factory::new);

        ClientPlayNetworking.registerGlobalReceiver(EmeraldMoney.IDENTIFIER, (client, handler, buf, responseSender) -> {
            if (client.player == null || client.world == null) {
                return;
            }

            ClientPlayerEntity playerEntity = client.player;
            World world = client.world;

            int entityId = buf.readInt();
            int status = buf.readInt();
            Random random = Random.create();

            client.execute(() -> {
                Entity entity = world.getEntityById(entityId);
                if (entity != null) {
                    if (status == 1) {
                        world.playSoundFromEntity(playerEntity, entity, SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.PLAYERS, 0.8f, 1f);
                        client.particleManager.addEmitter(entity, ParticleRegistry.EMERALD_TOTEM, 2);

                        for (int i = 0; i < 15; ++i) {
                            double g = entity.getX();
                            double h = entity.getBodyY(0.45 + random.nextDouble() * 0.55);
                            double j = entity.getZ();

                            double d = random.nextGaussian() * 0.3;
                            double e = random.nextGaussian() * 0.3;
                            double f = random.nextGaussian() * 0.3;

                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.DUST, getEmeraldColor(random)),
                                    true, g + d, h + e, j + f, d, e, f);
                        }

                        if (entity == playerEntity) {
                            client.gameRenderer.showFloatingItem(ItemRegistry.EMERALD_WALLET.getDefaultStack());
                        }
                    } else if (status == 2) {
                        for (int i = 0; i < 12; ++i) {
                            double g = entity.getX();
                            double h = entity.getBodyY(0.45 + random.nextDouble() * 0.55);
                            double j = entity.getZ();

                            double d = random.nextGaussian() * 0.3;
                            double e = random.nextGaussian() * 0.3;
                            double f = random.nextGaussian() * 0.3;

                            world.addParticle(ParticleTypes.HAPPY_VILLAGER,
                                    true, g + d, h + e, j + f, d, e, f);
                        }

                        world.playSoundFromEntity(playerEntity, entity, SoundEvents.ENTITY_VILLAGER_YES, SoundCategory.PLAYERS, 0.8f, 1f);
                    } else if (status == 3) {
                        world.playSoundFromEntity(playerEntity, entity, SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS, 1f, 1f);
                        client.particleManager.addEmitter(entity, ParticleRegistry.EMERALD_TOTEM, 15);

                        for (int i = 0; i < 15; ++i) {
                            double g = entity.getX();
                            double h = entity.getBodyY(0.45 + random.nextDouble() * 0.55);
                            double j = entity.getZ();

                            double d = random.nextGaussian() * 0.3;
                            double e = random.nextGaussian() * 0.3;
                            double f = random.nextGaussian() * 0.3;

                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.DUST, getEmeraldColor(random)),
                                    true, g + d, h + e, j + f, d, e, f);
                        }
                        for (int i = 0; i < 15; ++i) {
                            double g = entity.getX();
                            double h = entity.getBodyY(0.45 + random.nextDouble() * 0.55);
                            double j = entity.getZ();

                            double d = random.nextGaussian() * 0.15;
                            double e = random.nextGaussian() * 0.30;
                            double f = random.nextGaussian() * 0.15;

                            world.addParticle(ParticleRegistry.EMERALD_ITEMS,
                                    true, g + d, h + e, j + f, d, e, f);
                        }
                        if (entity == playerEntity) {
                            client.gameRenderer.showFloatingItem(ItemRegistry.EMERALD_TOTEM.getDefaultStack());
                        }
                    } else if (status == 4) {
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.5f, 1f, true);
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.7f, 1f, true);

                        for (int i = 0; i < 15; ++i) {
                            double g = entity.getX();
                            double h = entity.getRandomBodyY();
                            double j = entity.getZ();

                            double d = random.nextGaussian() * 0.05;
                            double e = random.nextGaussian() * 0.15;
                            double f = random.nextGaussian() * 0.05;

                            world.addParticle(ParticleRegistry.EMERALD_ITEMS,
                                    true, g + d, h + e, j + f, d, e, f);
                        }
                    } else if (status == 5) {
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_IRON_GOLEM_DAMAGE, SoundCategory.PLAYERS, 0.5f, 1f, true);
                        double g = entity.getX();
                        double j = entity.getZ();
                        for (int i = 0; i < 10; ++i) {
                            double h = entity.getRandomBodyY();

                            double d = random.nextGaussian() * 0.20;
                            double e = random.nextGaussian() * 0.40;
                            double f = random.nextGaussian() * 0.20;

                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.DUST, getEmeraldColor(random)),
                                    true, g + d, h + e, j + f, d, e, f);
                        }
                    } else if (status == 6) {
                        double g = entity.getX();
                        double j = entity.getZ();
                        for (int i = 0; i < 10; ++i) {
                            double h = entity.getRandomBodyY();

                            double d = random.nextGaussian() * 0.30;
                            double e = random.nextGaussian() * 0.60;
                            double f = random.nextGaussian() * 0.40;

                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.DUST, getEmeraldColor(random)),
                                    true, g + d, h + e, j + f, d, e, f);
                        }

                        BlockStateParticleEffect blockStateParticleEffect = new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.EMERALD_BLOCK.getDefaultState());

                        for (int i = 0; i < 15; ++i) {
                            double h = entity.getRandomBodyY();

                            double d = random.nextGaussian() * 0.20;
                            double e = random.nextGaussian() * 0.40;
                            double f = random.nextGaussian() * 0.20;

                            world.addParticle(blockStateParticleEffect,
                                    true, g + d, h + e, j + f, d, e, f);
                        }
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.BLOCK_METAL_BREAK, SoundCategory.PLAYERS, 1.6f, 1f, true);
                    } else if (status == 7) {
                        client.particleManager.addEmitter(entity, ParticleRegistry.EMERALD_TOTEM, 10);
                        for (int i = 0; i < 15; ++i) {
                            double g = entity.getX();
                            double h = entity.getBodyY(0.45 + random.nextDouble() * 0.55);
                            double j = entity.getZ();

                            double d = random.nextGaussian() * 0.3;
                            double e = random.nextGaussian() * 0.3;
                            double f = random.nextGaussian() * 0.3;

                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.DUST, getEmeraldColor(random)),
                                    true, g + d, h + e, j + f, d, e, f);
                        }
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.4f, 1f, true);
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS, 1.4f, 1.4f, true);
                    } else if (status == 8) {
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.4f, 1f, true);
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS, 1.4f, 1.4f, true);
                    } else if (status == 9) {
                        for (int i = 0; i < 15; ++i) {
                            double g = entity.getX();
                            double h = entity.getBodyY(0.45 + random.nextDouble() * 0.55);
                            double j = entity.getZ();

                            double d = random.nextGaussian() * 0.3;
                            double e = random.nextGaussian() * 0.3;
                            double f = random.nextGaussian() * 0.3;

                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.DUST, getEmeraldColor(random)),
                                    true, g + d, h + e, j + f, d, e, f);
                        }

                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1f, 1f, true);
                    }
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(PillagerExplosionHandler.IDENTIFIER, (client, handler, buf, responseSender) -> {
            try {
                PillagerExplosionHandler packet = new PillagerExplosionHandler(buf);
                client.execute(() -> {
                    PillagerExplosionHandler.Explosion explosion = new PillagerExplosionHandler.Explosion(client.world, null, packet.getX(), packet.getY(), packet.getZ(), packet.getRadius(), packet.getAffectedBlocks());
                    explosion.affectWorld(true);
                    client.player.setVelocity(client.player.getVelocity().add(packet.getPlayerVelocityX(), packet.getPlayerVelocityY(), packet.getPlayerVelocityZ()));
                });
            } catch (Exception exception) {

            }
        });

        ClientPlayNetworking.registerGlobalReceiver(CreepagerExplosionHandler.IDENTIFIER, (client, handler, buf, responseSender) -> {
            try {
                CreepagerExplosionHandler packet = new CreepagerExplosionHandler(buf);
                client.execute(() -> {
                    CreepagerExplosionHandler.Explosion explosion = new CreepagerExplosionHandler.Explosion(client.world, null, packet.getX(), packet.getY(), packet.getZ(), packet.getRadius(), packet.getAffectedBlocks());
                    explosion.affectWorld(true);
                    client.player.setVelocity(client.player.getVelocity().add(packet.getPlayerVelocityX(), packet.getPlayerVelocityY(), packet.getPlayerVelocityZ()));
                });
            } catch (Exception exception) {

            }
        });

        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            Entity cameraEntity = client.cameraEntity;
            if (cameraEntity instanceof PlayerEntity playerEntity && !client.options.hudHidden && !client.options.debugEnabled) {
                EmeraldCounter.render(client, playerEntity, drawContext, tickDelta);
            }
        });

        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.CREEPAGER_PET, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
            matrices.translate(0.5, 0, 0.5);
            matrices.scale(1f, 1f, 1f);

            EntityType<CreepagerEntity> entityType = EntityRegistry.CREEPAGER;
            CreepagerEntity renderEntity = entityType.create(MinecraftClient.getInstance().world);
            if (renderEntity != null) {
                MinecraftClient.getInstance().getEntityRenderDispatcher().render(renderEntity, BlockPos.ORIGIN.getX(), BlockPos.ORIGIN.getY(), BlockPos.ORIGIN.getZ(), 0, 0, matrices, vertexConsumers, light);
            }
        });

        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.EMERALD_SHOP_PORTAL, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
            matrices.translate(0.5, 0, 0.5);
            matrices.scale(0.25f, 0.25f, 0.25f);

            EntityType<EmeraldShopPortalEntity> entityType = EntityRegistry.EMERALD_SHOP_PORTAL;
            EmeraldShopPortalEntity renderEntity = entityType.create(MinecraftClient.getInstance().world);
            if (renderEntity != null) {
                renderEntity.setAnimationProgress(renderEntity.getMaxAnimationProgress());
                renderEntity.age = MinecraftClient.getInstance().player != null ? MinecraftClient.getInstance().player.age : 0;
                MinecraftClient.getInstance().getEntityRenderDispatcher().render(renderEntity, BlockPos.ORIGIN.getX(), BlockPos.ORIGIN.getY(), BlockPos.ORIGIN.getZ(), 0, 0, matrices, vertexConsumers, light);
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(new Identifier(EmeraldMoney.MOD_ID, "addemeralds"), (client, handler, buf, responseSender) -> {
            try {
                int emeralds = buf.readInt();
                PlayerEntity playerEntity = client.player;
                if (playerEntity instanceof PlayerProperties playerProperties) {
                    client.execute(() -> {
                        List<EmeraldToast> minerToasts = playerProperties.getEmeraldToasts();

                        EmeraldToast prevMinerToast = minerToasts.isEmpty() ? null : minerToasts.get(minerToasts.size() - 1);
                        int y = prevMinerToast == null ? 0 : Math.min(prevMinerToast.getY() - 16, 0);

                        if (prevMinerToast != null && (minerToasts.size() >= 35 ||
                                (((prevMinerToast.getEmeralds() < 0 && emeralds < 0) || (prevMinerToast.getEmeralds() >= 0 && emeralds >= 0)) && prevMinerToast.getTick() <= 20))) {
                            prevMinerToast.setEmeralds(prevMinerToast.getEmeralds() + emeralds);
                        } else {
                            playerProperties.getEmeraldToasts().add(new EmeraldToast(emeralds, 0, y));
                        }
                    });
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            PlayerEntity playerEntity = client.player;
            if (playerEntity instanceof PlayerProperties playerProperties) {
                playerProperties.getEmeraldToasts().forEach(EmeraldToast::tick);
                playerProperties.getEmeraldToasts().removeIf(EmeraldToast::shouldRemove);
            }
        });
    }

    public static Vector3f getEmeraldColor(Random random) {
        return Vec3d.unpackRgb(EMERALD_COLORS.get(random.nextInt(EMERALD_COLORS.size())))
                .toVector3f();
    }
}
