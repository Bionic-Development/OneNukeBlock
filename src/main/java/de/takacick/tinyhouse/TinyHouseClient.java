package de.takacick.tinyhouse;

import de.takacick.tinyhouse.registry.EntityRegistry;
import de.takacick.tinyhouse.registry.ItemRegistry;
import de.takacick.tinyhouse.registry.ParticleRegistry;
import de.takacick.tinyhouse.registry.block.entity.AerialChickenCannonBlockEntity;
import de.takacick.tinyhouse.registry.block.entity.SpinningPeepeeChoppaBlockEntity;
import de.takacick.tinyhouse.registry.block.entity.renderer.AerialChickenCannonBlockEntityRenderer;
import de.takacick.tinyhouse.registry.block.entity.renderer.GiantCrusherTrapBlockEntityRenderer;
import de.takacick.tinyhouse.registry.block.entity.renderer.SpinningPeepeeChoppaBlockEntityRenderer;
import de.takacick.tinyhouse.registry.entity.living.renderer.RatEntityRenderer;
import de.takacick.tinyhouse.registry.entity.projectile.renderer.ChickenProjectileEntityRenderer;
import de.takacick.tinyhouse.registry.particles.BloodLeakParticle;
import de.takacick.tinyhouse.registry.particles.BloodSplashParticle;
import de.takacick.tinyhouse.registry.particles.DustParticle;
import de.takacick.tinyhouse.registry.particles.FeatherParticle;
import de.takacick.tinyhouse.registry.particles.goop.GoopDropParticle;
import de.takacick.tinyhouse.registry.particles.goop.GoopDropParticleEffect;
import de.takacick.tinyhouse.registry.particles.goop.GoopParticle;
import de.takacick.tinyhouse.registry.particles.goop.GoopStringParticle;
import de.takacick.tinyhouse.server.explosion.ChickenExplosionHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.impl.blockrenderlayer.BlockRenderLayerMapImpl;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.entity.Entity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class TinyHouseClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityRegistry.RAT, RatEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.CHICKEN, ChickenProjectileEntityRenderer::new);
        BlockEntityRendererFactories.register(EntityRegistry.SPINNING_PEEPEE_CHOPPA, SpinningPeepeeChoppaBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(EntityRegistry.GIANT_CRUSHER_TRAP, GiantCrusherTrapBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(EntityRegistry.AERIAL_CHICKEN_CANNON, AerialChickenCannonBlockEntityRenderer::new);

        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP, GoopParticle.GoopParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP_DROP, GoopDropParticle.GoopDropParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP_STRING, GoopStringParticle.GoopStringParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.DUST, DustParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.FALLING_BLOOD, BloodLeakParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.BLOOD_SPLASH, BloodSplashParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.FEATHER, FeatherParticle.Factory::new);

        BlockRenderLayerMapImpl.INSTANCE.putBlock(ItemRegistry.CHICKEN, RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(ItemRegistry.BURNING_PLATE_TRAP, RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(ItemRegistry.TINY_BASE, RenderLayer.getTranslucent());

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            if (world == null || pos == null) {
                return GrassColors.getDefaultColor();
            }
            return BiomeColors.getGrassColor(world, pos);
        }, ItemRegistry.TINY_BASE);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> GrassColors.getColor(0.5, 1.0), ItemRegistry.TINY_BASE_ITEM);

        ClientPlayNetworking.registerGlobalReceiver(TinyHouse.IDENTIFIER, (client, handler, buf, responseSender) -> {
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
                        double g = entity.getX();
                        double h = entity.getBodyY(0.5);
                        double j = entity.getZ();

                        for (int i = 0; i < 3; ++i) {
                            double d = random.nextGaussian() * 0.2;
                            double e = random.nextDouble() * 0.3;
                            double f = random.nextGaussian() * 0.2;

                            world.addParticle(ParticleRegistry.FALLING_BLOOD,
                                    true, g + d, h + e, j + f, d * 0.1, e * 0.1, f * 0.1);
                        }
                    } else if (status == 2) {
                        double g = entity.getX();
                        double j = entity.getZ();

                        for (int i = 0; i < 6; ++i) {
                            double h = entity.getRandomBodyY();
                            double d = random.nextGaussian() * 0.4;
                            double e = random.nextDouble() * 1;
                            double f = random.nextGaussian() * 0.4;

                            world.addParticle(new GoopDropParticleEffect(Vec3d.unpackRgb(0x820A0A).toVector3f(), 0.1f + (float) (random.nextDouble() * 0.2)),
                                    true, g + d, h, j + f, d * 0.5, e * 0.3, f * 0.5);
                        }
                    } else if (status == 3) {
                        double g = entity.getX();
                        double j = entity.getZ();

                        for (int i = 0; i < 6; ++i) {
                            Vec3d vel = new Vec3d(random.nextGaussian(), random.nextDouble(), random.nextGaussian());

                            world.addParticle(ParticleTypes.SMOKE, true, entity.getX() + vel.getX() * entity.getWidth() * 0.8, entity.getBodyY(0.5 + random.nextDouble() * 0.6), entity.getZ() + vel.getZ() * entity.getWidth() * 0.8,
                                    vel.getX() * 0.01, vel.getY() * 0.01, vel.getZ() * 0.01);
                        }
                        world.playSound(g, entity.getBodyY(0.5), j, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.AMBIENT, 1f, 1f, true);
                    } else if (status == 4) {
                        double g = entity.getX();
                        double j = entity.getZ();

                        for (int i = 0; i < 10; ++i) {
                            Vec3d vel = new Vec3d(random.nextGaussian(), random.nextDouble(), random.nextGaussian());

                            world.addParticle(ParticleTypes.SNOWFLAKE, true, entity.getX() + vel.getX() * entity.getWidth() * 0.8,
                                    entity.getBodyY(0.0 + random.nextDouble() * 0.2), entity.getZ() + vel.getZ() * entity.getWidth() * 0.8,
                                    vel.getX() * 0.01, vel.getY() * 0.01, vel.getZ() * 0.01);
                        }
                        BlockStateParticleEffect particleEffect = new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.ICE.getDefaultState());
                        for (int i = 0; i < 20; ++i) {
                            Vec3d vel = new Vec3d(random.nextGaussian(), random.nextDouble(), random.nextGaussian());

                            world.addParticle(particleEffect, true, entity.getX() + vel.getX() * entity.getWidth() * 0.8,
                                    entity.getBodyY(0.0 + random.nextDouble() * 0.2), entity.getZ() + vel.getZ() * entity.getWidth() * 0.8,
                                    vel.getX() * 0.01, vel.getY() * 0.01, vel.getZ() * 0.01);
                        }
                        world.playSound(g, entity.getBodyY(0.5), j, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.AMBIENT, 1f, 1f, true);
                        world.playSound(g, entity.getBodyY(0.5), j, SoundEvents.ENTITY_PLAYER_HURT_FREEZE, SoundCategory.AMBIENT, 1f, 1f, true);
                    } else if (status == 5) {
                        double g = entity.getX();
                        double j = entity.getZ();

                        for (int i = 0; i < 10; ++i) {
                            Vec3d vel = new Vec3d(random.nextGaussian(), random.nextDouble(), random.nextGaussian());

                            world.addParticle(ParticleTypes.ELECTRIC_SPARK, true,
                                    entity.getX() + vel.getX() * entity.getWidth() * 0.8,
                                    entity.getBodyY(0.5 + random.nextDouble() * 0.6),
                                    entity.getZ() + vel.getZ() * entity.getWidth() * 0.8,
                                    vel.getX() * 0.01, vel.getY() * 0.01, vel.getZ() * 0.01);
                        }

                        world.playSound(g, entity.getBodyY(0.5), j, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.PLAYERS, 0.55f, 3f, true);
                        world.playSound(g, entity.getBodyY(0.5), j, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.PLAYERS, 0.55f, 3f, true);
                    }
                }
            });
        });

        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.SPINNING_PEEPEE_CHOPPA_ITEM, (stack, mode, matrices, vertexConsumers, light, overlay) ->
                MinecraftClient.getInstance().getBlockEntityRenderDispatcher().renderEntity(new SpinningPeepeeChoppaBlockEntity(BlockPos.ORIGIN,
                        ItemRegistry.SPINNING_PEEPEE_CHOPPA.getDefaultState()), matrices, vertexConsumers, light, overlay));

        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.AERIAL_CHICKEN_CANNON_ITEM, (stack, mode, matrices, vertexConsumers, light, overlay) -> {

            AerialChickenCannonBlockEntity aerialChickenCannonBlockEntity = new AerialChickenCannonBlockEntity(BlockPos.ORIGIN, ItemRegistry.AERIAL_CHICKEN_CANNON.getDefaultState());
            aerialChickenCannonBlockEntity.setYaw(180f);
            aerialChickenCannonBlockEntity.setLoading(0);
            aerialChickenCannonBlockEntity.setPrevLoading(0);
            MinecraftClient.getInstance().getBlockEntityRenderDispatcher().renderEntity(aerialChickenCannonBlockEntity, matrices, vertexConsumers, light, overlay);
        });

        ClientPlayNetworking.registerGlobalReceiver(ChickenExplosionHandler.IDENTIFIER, (client, handler, buf, responseSender) -> {
            try {
                ChickenExplosionHandler packet = new ChickenExplosionHandler(buf);
                client.execute(() -> {
                    ChickenExplosionHandler.Explosion explosion = new ChickenExplosionHandler.Explosion(client.world, null, packet.getX(), packet.getY(), packet.getZ(), packet.getRadius(), packet.getAffectedBlocks());
                    explosion.affectWorld(true);
                    client.player.setVelocity(client.player.getVelocity().add(packet.getPlayerVelocityX(), packet.getPlayerVelocityY(), packet.getPlayerVelocityZ()));
                });
            } catch (Exception exception) {

            }
        });
    }

    public static float getTickDelta() {
        return MinecraftClient.getInstance().getTickDelta();
    }
}
