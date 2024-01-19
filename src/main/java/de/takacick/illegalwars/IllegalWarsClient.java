package de.takacick.illegalwars;

import de.takacick.illegalwars.registry.EntityRegistry;
import de.takacick.illegalwars.registry.ParticleRegistry;
import de.takacick.illegalwars.registry.block.entity.renderer.PieLauncherBlockEntityRenderer;
import de.takacick.illegalwars.registry.entity.projectiles.renderer.PieEntityRenderer;
import de.takacick.illegalwars.registry.particles.ColoredParticleEffect;
import de.takacick.illegalwars.registry.particles.DustParticle;
import de.takacick.illegalwars.registry.particles.goop.GoopDropParticle;
import de.takacick.illegalwars.registry.particles.goop.GoopDropParticleEffect;
import de.takacick.illegalwars.registry.particles.goop.GoopParticle;
import de.takacick.illegalwars.registry.particles.goop.GoopStringParticle;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.List;

public class IllegalWarsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityRegistry.PIE, PieEntityRenderer::new);
        BlockEntityRendererFactories.register(EntityRegistry.PIE_LAUNCHER, PieLauncherBlockEntityRenderer::new);

        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP, GoopParticle.GoopParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP_DROP, GoopDropParticle.GoopDropParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP_STRING, GoopStringParticle.GoopStringParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.DUST, DustParticle.Factory::new);

        ClientPlayNetworking.registerGlobalReceiver(IllegalWars.IDENTIFIER, (client, handler, buf, responseSender) -> {
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
                    if (status == 4) {
                        double g = entity.getX();
                        double j = entity.getZ();

                        List<Vector3f> color = Arrays.asList(Vec3d.unpackRgb(0xFAFEFE).toVector3f(),
                                Vec3d.unpackRgb(0xEDF7F9).toVector3f());

                        for (int i = 0; i < 7; ++i) {
                            double h = entity.getRandomBodyY();
                            double d = random.nextGaussian() * 0.4;
                            double e = random.nextDouble() * 1;
                            double f = random.nextGaussian() * 0.4;

                            world.addParticle(new GoopDropParticleEffect(color.get(random.nextInt(color.size())), 0.15f + (float) (random.nextDouble() * 0.5)),
                                    true, g + d, h, j + f, d * 0.5, e * 0.3, f * 0.5);
                        }

                        color = Arrays.asList(Vec3d.unpackRgb(0xEDBA6B).toVector3f(),
                                Vec3d.unpackRgb(0xD59F4B).toVector3f());
                        for (int i = 0; i < 35; ++i) {
                            double h = entity.getRandomBodyY();
                            double d = random.nextGaussian() * 0.4;
                            double e = random.nextDouble() * 1;
                            double f = random.nextGaussian() * 0.4;

                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.DUST, color.get(random.nextInt(color.size()))),
                                    true, g + d, h, j + f, d * 0.5, e * 0.3, f * 0.5);
                        }

                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.BLOCK_MUD_BREAK,
                                entity.getSoundCategory(), 1.3f, 1f, true);
                    } else if (status == 7) {
                        double g = entity.getX();
                        double j = entity.getZ();

                        List<Vector3f> color = Arrays.asList(Vec3d.unpackRgb(0xFAFEFE).toVector3f(),
                                Vec3d.unpackRgb(0xEDF7F9).toVector3f());

                        for (int i = 0; i < 25; ++i) {
                            double h = entity.getRandomBodyY();
                            double d = random.nextGaussian() * 0.4;
                            double e = random.nextDouble() * 1;
                            double f = random.nextGaussian() * 0.4;

                            world.addParticle(new GoopDropParticleEffect(color.get(random.nextInt(color.size())), 0.15f + (float) (random.nextDouble() * 0.5)),
                                    true, g + d, h, j + f, d * 0.5, e * 0.3, f * 0.5);
                        }

                        color = Arrays.asList(Vec3d.unpackRgb(0xEDBA6B).toVector3f(),
                                Vec3d.unpackRgb(0xD59F4B).toVector3f());
                        for (int i = 0; i < 15; ++i) {
                            double h = entity.getRandomBodyY();
                            double d = random.nextGaussian() * 0.4;
                            double e = random.nextDouble() * 1;
                            double f = random.nextGaussian() * 0.4;

                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.DUST, color.get(random.nextInt(color.size()))),
                                    true, g + d, h, j + f, d * 0.5, e * 0.3, f * 0.5);
                        }

                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.BLOCK_MUD_BREAK,
                                entity.getSoundCategory(), 1.3f, 1f, true);
                    }
                }
            });
        });
    }
}
