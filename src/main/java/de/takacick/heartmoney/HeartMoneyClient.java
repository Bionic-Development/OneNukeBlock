package de.takacick.heartmoney;

import de.takacick.heartmoney.mixin.InGameHudAccessor;
import de.takacick.heartmoney.registry.EntityRegistry;
import de.takacick.heartmoney.registry.ItemRegistry;
import de.takacick.heartmoney.registry.ParticleRegistry;
import de.takacick.heartmoney.registry.block.entity.BloodBeaconTrapBlockEntity;
import de.takacick.heartmoney.registry.block.entity.renderer.BloodBeaconTrapBlockEntityRenderer;
import de.takacick.heartmoney.registry.block.entity.renderer.HeartwarmingNukeBlockEntityRenderer;
import de.takacick.heartmoney.registry.entity.custom.renderer.HeartShopPortalEntityRenderer;
import de.takacick.heartmoney.registry.entity.custom.renderer.LoveBarrierSuitRenderer;
import de.takacick.heartmoney.registry.entity.custom.renderer.ShopItemEntityRenderer;
import de.takacick.heartmoney.registry.entity.living.renderer.GirlfriendEntityRenderer;
import de.takacick.heartmoney.registry.entity.living.renderer.HeartAngelEntityRenderer;
import de.takacick.heartmoney.registry.entity.projectiles.renderer.HeartwarmingNukeEntityRenderer;
import de.takacick.heartmoney.registry.entity.projectiles.renderer.LifeStealScytheEntityRenderer;
import de.takacick.heartmoney.registry.particles.*;
import de.takacick.heartmoney.registry.particles.goop.GoopDropParticle;
import de.takacick.heartmoney.registry.particles.goop.GoopDropParticleEffect;
import de.takacick.heartmoney.registry.particles.goop.GoopParticle;
import de.takacick.heartmoney.registry.particles.goop.GoopStringParticle;
import de.takacick.utils.BionicUtilsClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EmptyEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

public class HeartMoneyClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityRegistry.HEART_SHOP_PORTAL, HeartShopPortalEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.SHOP_ITEM, ShopItemEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.HEART_ANGEL, HeartAngelEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.GIRLFRIEND, GirlfriendEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.BLOCK_BREAK, EmptyEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.LIFE_STEAL_SCYTHE, LifeStealScytheEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.HEARTWARMING_NUKE, HeartwarmingNukeEntityRenderer::new);
        BlockEntityRendererRegistry.register(EntityRegistry.BLOOD_BEACON_TRAP, BloodBeaconTrapBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(EntityRegistry.HEARTWARMING_NUKE_BLOCK, HeartwarmingNukeBlockEntityRenderer::new);

        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP, GoopParticle.GoopParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP_DROP, GoopDropParticle.GoopDropParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP_STRING, GoopStringParticle.GoopStringParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.HEART, HeartParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.COLORED_GLOW_SPARK, ColoredGlowSparkParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.COLORED_GLOW_HEART, ColoredGlowHeartParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.COLORED_FLASH, ColoredFlashParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.COLORED_POOF, ColoredPoofParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.HEART_SHOCKWAVE, HeartShockwaveParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.HEART_SWEEP_ATTACK, HeartSweepAttackParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.HEART_EXPLOSION_EMITTER, new HeartExplosionEmitterParticle.Factory());
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.HEART_EXPLOSION, HeartExplosionParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.HEART_TOTEM, HeartTotemParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.HEART_POOF, HeartPoofParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.HEART_SONIC_BOOM, HeartSonicBoomParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.HEART_PORTAL, HeartPortalParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.HEART_SOUL, HeartSoulParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.ANGEL_DUST, AngelDustParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.ANGEL_SHOCKWAVE, AngelShockwaveParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.BLOOD_SWEEP_ATTACK, BloodSweepAttackParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.BLOOD_PORTAL, BloodPortalParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.HEART_FIRE, HeartFireParticle.Factory::new);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> 0xFFEECC, ItemRegistry.HEART_ANGEL_HEART);

        ClientPlayNetworking.registerGlobalReceiver(HeartMoney.IDENTIFIER, (client, handler, buf, responseSender) -> {
            if (client.player == null || client.world == null) {
                return;
            }

            ClientPlayerEntity playerEntity = client.player;
            World world = client.world;

            int entityId = buf.readInt();
            int status = buf.readInt();

            client.execute(() -> {
                Entity entity = world.getEntityById(entityId);
                if (entity != null) {
                    if (status == 1) {
                        int count = 1;
                        boolean angel = false;
                        if (entity instanceof ItemEntity itemEntity) {
                            count = itemEntity.getStack().getCount();
                            if (itemEntity.getStack().isOf(ItemRegistry.HEART_ANGEL_HEART)) {
                                angel = true;
                            }
                        }

                        for (int i = 0; i < Math.min(count * 5, 60); ++i) {
                            double g = entity.getX();
                            double h = entity.getBodyY(0.5);
                            double j = entity.getZ();
                            world.addParticle(angel ? new ColoredParticleEffect(ParticleRegistry.COLORED_GLOW_HEART, new Vec3f(Vec3d.unpackRgb(0xFFE6A8))) : ParticleRegistry.HEART, true, g, h, j, world.getRandom().nextGaussian() * 0.15, world.getRandom().nextGaussian() * 0.15, world.getRandom().nextGaussian() * 0.15);
                        }

                        for (int i = 0; i < Math.min(count, 15); ++i) {
                            world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.BLOCK_AMETHYST_CLUSTER_HIT, entity.getSoundCategory(), 0.23f, 1f, true);
                        }
                    } else if (status == 2) {
                        world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_FIREWORK_ROCKET_BLAST, entity.getSoundCategory(), 1.0f, 4.0f, false);
                        world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_FIREWORK_ROCKET_TWINKLE, entity.getSoundCategory(), 1.0f, 3.0f, false);

                        client.particleManager.addEmitter(entity, ParticleRegistry.HEART_TOTEM);

                        for (int i = 0; i < 25; ++i) {
                            double g = entity.getParticleX(0.8);
                            double h = entity.getBodyY(world.getRandom().nextDouble() * 1.2);
                            double j = entity.getParticleZ(0.8);
                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_GLOW_SPARK, new Vec3f(Vec3d.unpackRgb(0xFF1313))), true, g, h, j, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3);
                        }

                        for (int i = 0; i < 1; ++i) {
                            double g = entity.getParticleX(entity.getWidth());
                            double h = entity.getBodyY(0.5);
                            double j = entity.getParticleZ(entity.getWidth());
                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_FLASH, new Vec3f(Vec3d.unpackRgb(0xFF1313))), true, g, h, j, 0.15f, 0, 0);
                        }

                    } else if (status == 3) {
                        double g = entity.getX();
                        double h = entity.getBodyY(0.5);
                        double j = entity.getZ();
                        for (int i = 0; i < 15; ++i) {
                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_GLOW_SPARK, new Vec3f(Vec3d.unpackRgb(0xFF0000))), true, g, h, j, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3);
                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_GLOW_SPARK, new Vec3f(Vec3d.unpackRgb(0xFF1313))), true, g, h, j, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3);
                        }

                        for (int i = 0; i < 20; ++i) {
                            double x = world.getRandom().nextGaussian() * 0.3;
                            double y = world.getRandom().nextGaussian() * 0.3;
                            double z = world.getRandom().nextGaussian() * 0.3;

                            world.addParticle(ParticleRegistry.HEART_POOF, true, g, h, j, x, y, z);
                        }

                        client.particleManager.addEmitter(entity, ParticleRegistry.HEART_TOTEM, 15);
                        world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ITEM_TOTEM_USE, entity.getSoundCategory(), 1.0f, 1.0f, false);
                        if (entity == client.player) {
                            client.gameRenderer.showFloatingItem(ItemRegistry.LOVER_TOTEM.getDefaultStack());
                        }
                    } else if (status == 4) {
                        double g = entity.getX();
                        double h = entity.getBodyY(0.5);
                        double j = entity.getZ();

                        for (int i = 0; i < 10; ++i) {
                            world.addParticle(ParticleRegistry.HEART, true, g, h, j, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3);
                        }

                        for (int i = 0; i < 15; ++i) {
                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_GLOW_SPARK, new Vec3f(Vec3d.unpackRgb(0xFF0000))), true, g, h, j, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3);
                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_GLOW_SPARK, new Vec3f(Vec3d.unpackRgb(0xFF1313))), true, g, h, j, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3);
                        }

                        world.playSound(g, h, j, SoundEvents.BLOCK_AMETHYST_CLUSTER_HIT, entity.getSoundCategory(), 0.5f, 1f, true);
                    } else if (status == 5 || status == 6) {
                        double g = entity.getX();
                        double j = entity.getZ();

                        for (int i = 0; i < 8; ++i) {
                            double h = entity.getRandomBodyY();
                            world.addParticle(ParticleRegistry.HEART, true, g, h, j, world.getRandom().nextGaussian() * 0.1, world.getRandom().nextGaussian() * 0.1, world.getRandom().nextGaussian() * 0.1);
                        }

                        for (int i = 0; i < 15; ++i) {
                            double h = entity.getRandomBodyY();
                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_GLOW_SPARK, new Vec3f(Vec3d.unpackRgb(0xFF1313))), true, g, h, j, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3);
                        }

                        if (status == 6) {
                            world.playSound(g, entity.getBodyY(0.5), j, SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE, entity.getSoundCategory(), 0.5f, 1f, true);
                        }
                        world.playSound(g, entity.getBodyY(0.5), j, SoundEvents.BLOCK_AMETHYST_CLUSTER_HIT, entity.getSoundCategory(), 0.5f, 1f, true);
                    } else if (status == 7) {
                        double g = entity.getX();
                        double j = entity.getZ();

                        for (int i = 0; i < 8; ++i) {
                            double h = entity.getRandomBodyY();
                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_GLOW_HEART, new Vec3f(Vec3d.unpackRgb(0xFFE6A8))), true, g, h, j, world.getRandom().nextGaussian() * 0.1, world.getRandom().nextGaussian() * 0.1, world.getRandom().nextGaussian() * 0.1);
                        }

                        for (int i = 0; i < 15; ++i) {
                            double h = entity.getRandomBodyY();
                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_GLOW_SPARK, new Vec3f(Vec3d.unpackRgb(0xFFE6A8))), true, g, h, j, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3);
                        }
                        world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_FLASH, new Vec3f(Vec3d.unpackRgb(0xFFE6A8))), true, g, entity.getBodyY(0.5), j, 0.25f, 0, 0);

                        world.playSound(g, entity.getBodyY(0.5), j, SoundEvents.BLOCK_AMETHYST_CLUSTER_HIT, entity.getSoundCategory(), 0.5f, 1f, true);
                    } else if (status == 8) {
                        double g = entity.getX();
                        double j = entity.getZ();

                        for (int i = 0; i < 8; ++i) {
                            double h = entity.getRandomBodyY();
                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_GLOW_HEART, new Vec3f(Vec3d.unpackRgb(0xFFE6A8))), true, g, h, j, world.getRandom().nextGaussian() * 0.1, world.getRandom().nextGaussian() * 0.1, world.getRandom().nextGaussian() * 0.1);
                        }

                        for (int i = 0; i < 15; ++i) {
                            double h = entity.getRandomBodyY();
                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_GLOW_SPARK, new Vec3f(Vec3d.unpackRgb(0xFFE6A8))), true, g, h, j, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3);
                        }
                        world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_FLASH, new Vec3f(Vec3d.unpackRgb(0xFFE6A8))), true, g, entity.getBodyY(0.5), j, 0.25f, 0, 0);
                        world.addParticle(new ShockwaveParticleEffect(ParticleRegistry.ANGEL_SHOCKWAVE, 6, 0.2f, 0.1f), entity.getX(), entity.getBodyY(0.5f), entity.getZ(), 0, 90, entity.getYaw());

                        world.playSound(g, entity.getBodyY(0.5), j, SoundEvents.BLOCK_AMETHYST_CLUSTER_HIT, entity.getSoundCategory(), 0.5f, 1f, true);
                    } else if (status == 9) {
                        double g = entity.getX();
                        double j = entity.getZ();
                        world.playSound(g, entity.getBodyY(0.5), j, ParticleRegistry.UWU, entity.getSoundCategory(), 0.5f, 1f, true);
                        world.playSound(g, entity.getBodyY(0.5), j, SoundEvents.BLOCK_AMETHYST_CLUSTER_HIT, entity.getSoundCategory(), 0.5f, 1f, true);
                        for (int i = 0; i < 18; ++i) {
                            double h = entity.getRandomBodyY();
                            int color = BionicUtilsClient.getRainbow().getColorAsInt(world.random.nextInt(601));
                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_GLOW_HEART, new Vec3f(Vec3d.unpackRgb(color))), true, g, h, j, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3);
                        }

                        for (int i = 0; i < 15; ++i) {
                            double h = entity.getRandomBodyY();
                            int color = BionicUtilsClient.getRainbow().getColorAsInt(world.random.nextInt(601));
                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_GLOW_SPARK, new Vec3f(Vec3d.unpackRgb(color))), true, g, h, j, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3);
                        }

                        for (int i = 0; i < 15; ++i) {
                            double d = world.getRandom().nextGaussian() * 0.02;
                            double e = world.getRandom().nextGaussian() * 0.02;
                            double f = world.getRandom().nextGaussian() * 0.02;
                            int color = BionicUtilsClient.getRainbow().getColorAsInt(world.random.nextInt(601));
                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_POOF, new Vec3f(Vec3d.unpackRgb(color))), entity.getParticleX(1.0), entity.getRandomBodyY(), entity.getParticleZ(1.0), d, e, f);
                        }
                    } else if (status == 10) {
                        for (int i = 0; i < 1; ++i) {
                            double g = entity.getX();
                            double h = entity.getBodyY(0.5);
                            double j = entity.getZ();
                            world.addParticle(ParticleRegistry.HEART, true, g, h, j, world.getRandom().nextGaussian() * 0.15, world.getRandom().nextGaussian() * 0.15, world.getRandom().nextGaussian() * 0.15);
                        }

                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.BLOCK_AMETHYST_CLUSTER_HIT, entity.getSoundCategory(), 0.23f, 1f, true);
                    } else if (status == 11) {
                        double g = entity.getX();
                        double h = entity.getBodyY(0.5);
                        double j = entity.getZ();
                        for (int i = 0; i < 4; ++i) {
                            world.addParticle(ParticleRegistry.HEART, true, g, h, j, world.getRandom().nextGaussian() * 0.15, world.getRandom().nextGaussian() * 0.15, world.getRandom().nextGaussian() * 0.15);
                        }

                        world.playSound(g, h, j, SoundEvents.PARTICLE_SOUL_ESCAPE, entity.getSoundCategory(), 0.43f, 1f, true);

                        world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_FLASH, new Vec3f(Vec3d.unpackRgb(0xFF0000))), true, g, entity.getBodyY(0.5), j, 0.25f, 0, 0);
                    } else if (status == 12) {
                        double g = entity.getX();
                        double j = entity.getZ();

                        for (int i = 0; i < 18; ++i) {
                            double h = entity.getRandomBodyY();
                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_GLOW_HEART, new Vec3f(Vec3d.unpackRgb(0x820A0A))), true, g, h, j, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3);
                        }

                        for (int i = 0; i < 15; ++i) {
                            double h = entity.getRandomBodyY();
                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_GLOW_SPARK, new Vec3f(Vec3d.unpackRgb(0x820A0A))), true, g, h, j, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3);
                        }

                        for (int i = 0; i < 15; ++i) {
                            double h = entity.getRandomBodyY();
                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_POOF, new Vec3f(Vec3d.unpackRgb(0x820A0A))), true, g, h, j, world.getRandom().nextGaussian() * 0.1, world.getRandom().nextGaussian() * 0.1, world.getRandom().nextGaussian() * 0.1);
                        }
                        world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_FLASH, new Vec3f(Vec3d.unpackRgb(0x820A0A))), true, g, entity.getBodyY(0.5), j, 0.25f, 0, 0);
                        world.playSound(g, entity.getBodyY(0.5), j, SoundEvents.ENTITY_GENERIC_EXPLODE, entity.getSoundCategory(), 0.43f, 4f, true);
                        for (int i = 0; i < 20; i++) {
                            world.addParticle(new GoopDropParticleEffect(new Vec3f(Vec3d.unpackRgb(0x820A0A)), (float) (world.getRandom().nextDouble() * 3)), true, entity.getX(), entity.getRandomBodyY(), entity.getZ(), world.getRandom().nextGaussian() * 0.25, world.getRandom().nextDouble() * 0.20, world.getRandom().nextGaussian() * 0.25);
                        }
                    } else if (status == 13) {
                        double g = entity.getX();
                        double j = entity.getZ();

                        for (int i = 0; i < 5; ++i) {
                            double h = entity.getRandomBodyY();
                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_GLOW_HEART, new Vec3f(Vec3d.unpackRgb(0x820A0A))), true, g, h, j, world.getRandom().nextGaussian() * 0.7, world.getRandom().nextGaussian() * 0.7, world.getRandom().nextGaussian() * 0.7);
                        }

                        for (int i = 0; i < 5; ++i) {
                            double h = entity.getRandomBodyY();
                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_GLOW_SPARK, new Vec3f(Vec3d.unpackRgb(0x820A0A))), true, g, h, j, world.getRandom().nextGaussian() * 0.7, world.getRandom().nextGaussian() * 0.7, world.getRandom().nextGaussian() * 0.7);
                        }

                        for (int i = 0; i < 12; ++i) {
                            double h = entity.getRandomBodyY();
                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_POOF, new Vec3f(Vec3d.unpackRgb(0x820A0B))), true, g, h, j, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3);
                        }
                    } else if (status == 14) {
                        double g = entity.getX();
                        double j = entity.getZ();

                        for (int i = 0; i < 5; ++i) {
                            double h = entity.getRandomBodyY();
                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_POOF, new Vec3f(Vec3d.unpackRgb(0x820A0A))), true, g, h, j, world.getRandom().nextGaussian() * 0.1, world.getRandom().nextGaussian() * 0.1, world.getRandom().nextGaussian() * 0.1);
                        }
                        world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_FLASH, new Vec3f(Vec3d.unpackRgb(0x820A0A))), true, g, entity.getBodyY(0.5), j, 0.25f, 0, 0);
                        for (int i = 0; i < 20; i++) {
                            world.addParticle(new GoopDropParticleEffect(new Vec3f(Vec3d.unpackRgb(0x820A0A)), (float) (world.getRandom().nextDouble() * 3)), true, entity.getX(), entity.getRandomBodyY(), entity.getZ(), world.getRandom().nextGaussian() * 0.25, world.getRandom().nextDouble() * 0.20, world.getRandom().nextGaussian() * 0.25);
                        }
                    } else if (status == 15) {
                        double g = entity.getX();
                        double j = entity.getZ();

                        for (int i = 0; i < 5; ++i) {
                            double h = entity.getRandomBodyY();
                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_POOF, new Vec3f(Vec3d.unpackRgb(0x820A0B))), true, g, h, j, world.getRandom().nextGaussian() * 0.1, world.getRandom().nextGaussian() * 0.1, world.getRandom().nextGaussian() * 0.1);
                            world.playSound(g, entity.getBodyY(0.5), j, SoundEvents.PARTICLE_SOUL_ESCAPE, entity.getSoundCategory(), 2f, 1f, true);
                        }

                        for (int i = 0; i < 15; ++i) {
                            double h = entity.getRandomBodyY();
                            world.addParticle(ParticleRegistry.HEART_SOUL, true, g, h, j, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3);
                        }

                    } else if (status == 16) {
                        double g = entity.getX();
                        double j = entity.getZ();

                        for (int i = 0; i < 5; ++i) {
                            double h = entity.getRandomBodyY();
                            world.addParticle(ParticleRegistry.HEART_SOUL, true, g, h, j, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3);
                        }

                        world.playSound(g, entity.getBodyY(0.5), j, SoundEvents.PARTICLE_SOUL_ESCAPE, entity.getSoundCategory(), 1f, 1f, true);
                    } else if (status == 17) {
                        for (int i = 0; i < 35; ++i) {
                            double g = entity.getParticleX(entity.getWidth());
                            double j = entity.getZ();
                            double h = entity.getParticleZ(entity.getWidth());
                            world.addImportantParticle(ParticleRegistry.HEART_EXPLOSION, true, g, h, j, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3);
                            world.playSound(g, entity.getBodyY(0.5), j, SoundEvents.ENTITY_GENERIC_EXPLODE, entity.getSoundCategory(), 1f, 1f, true);
                        }
                    }
                }
            });
        });

        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.BLOOD_BEACON_TRAP, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
            MinecraftClient.getInstance().getBlockEntityRenderDispatcher().renderEntity(new BloodBeaconTrapBlockEntity(BlockPos.ORIGIN, ItemRegistry.BLOOD_BEACON_TRAP.getDefaultState()), matrices, vertexConsumers, light, overlay);
        });

        ClientPlayNetworking.registerGlobalReceiver(new Identifier(HeartMoney.MOD_ID, "healthupdate"), (client, handler, buf, responseSender) -> {
            try {
                double maxHealthValue = buf.readDouble();
                int healthValue = buf.readInt();
                float health = buf.readFloat();

                client.execute(() -> {
                    if (client.player != null) {
                        client.player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(maxHealthValue);
                        client.player.setHealth(health);
                    }
                    ((InGameHudAccessor) client.inGameHud).setLastHealthValue(healthValue);
                    ((InGameHudAccessor) client.inGameHud).setRenderHealthValue(healthValue);
                });
            } catch (Exception exception) {

            }
        });

        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.LOVE_BARRIER_SUIT, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
            matrices.translate(0.5, 1.5, 0.5);
            matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));

            float tickDelta = MinecraftClient.getInstance().getTickDelta();

            float f = MinecraftClient.getInstance().player != null ? MinecraftClient.getInstance().player.age + tickDelta : 0f;
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEnergySwirl(LoveBarrierSuitRenderer.LOVE_BARRIER_SUIT, (f * 0.01f) % 1.0f, f * 0.01f % 1.0f));

            LoveBarrierSuitRenderer.LOVE_BARRIER_MODEL.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
        });

        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.HEART_ANGEL, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
            matrices.translate(0.5, 0, 0.5);
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180));

            EntityType<?> entityType = EntityRegistry.HEART_ANGEL;
            Entity renderEntity = entityType.create(MinecraftClient.getInstance().world);
            if (renderEntity != null) {
                renderEntity.age = MinecraftClient.getInstance().player != null ? MinecraftClient.getInstance().player.age : 0;
                MinecraftClient.getInstance().getEntityRenderDispatcher().render(renderEntity,
                        BlockPos.ORIGIN.getX(), BlockPos.ORIGIN.getY(), BlockPos.ORIGIN.getZ(),
                        0, 0, matrices, vertexConsumers, light);
            }
        });
    }
}
