package de.takacick.raidbase;

import de.takacick.raidbase.access.PigProperties;
import de.takacick.raidbase.access.PlayerProperties;
import de.takacick.raidbase.client.renderer.BanLightningEntityRenderer;
import de.takacick.raidbase.client.shaders.RaidBaseLayers;
import de.takacick.raidbase.registry.EntityRegistry;
import de.takacick.raidbase.registry.ItemRegistry;
import de.takacick.raidbase.registry.ParticleRegistry;
import de.takacick.raidbase.registry.block.entity.renderer.BeaconDeathLaserBlockEntityRenderer;
import de.takacick.raidbase.registry.block.entity.renderer.PieLauncherBlockEntityRenderer;
import de.takacick.raidbase.registry.entity.projectiles.renderer.PieEntityRenderer;
import de.takacick.raidbase.registry.inventory.CopperHopperScreen;
import de.takacick.raidbase.registry.particles.*;
import de.takacick.raidbase.registry.particles.goop.GoopDropParticle;
import de.takacick.raidbase.registry.particles.goop.GoopDropParticleEffect;
import de.takacick.raidbase.registry.particles.goop.GoopParticle;
import de.takacick.raidbase.registry.particles.goop.GoopStringParticle;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;
import java.util.List;

public class RaidBaseClient implements ClientModInitializer {

    private KeyBinding exitSlimeSuit;
    private boolean exitSlimeSuitBoolean;

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityRegistry.PIE, PieEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.BAN_LIGHTNING, BanLightningEntityRenderer::new);
        BlockEntityRendererFactories.register(EntityRegistry.BEACON_DEATH_LASER, BeaconDeathLaserBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(EntityRegistry.PIE_LAUNCHER, PieLauncherBlockEntityRenderer::new);

        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP, GoopParticle.GoopParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP_DROP, GoopDropParticle.GoopDropParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP_STRING, GoopStringParticle.GoopStringParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.LASER_DUST, LaserDustParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GLITCHED, GlitchedParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.BAN_SHOCKWAVE, BanShockwaveParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.LIGHTNING, LightningParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.QUICKSAND_DUST, QuicksandDustParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.HACK_TARGET, HackTargetParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.DUST, DustParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.TRANSPARENT_DUST, TransparentDustParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.SLIME, SlimeParticle.Factory::new);

        ScreenRegistry.register(RaidBase.COPPER_HOPPER, CopperHopperScreen::new);

        BlockRenderLayerMap.INSTANCE.putBlocks(RaidBaseLayers.getSolid(), ItemRegistry.GLITCHY_QUICKSAND);
        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), ItemRegistry.STILL_LIGHTNING_WATER, ItemRegistry.FLOWING_LIGHTNING_WATER);

        try {
            exitSlimeSuit = KeyBindingHelper.registerKeyBinding(
                    new KeyBinding("Exit Slime Suit",
                            InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X,
                            "RaidBase Abilities")
            );
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                PlayerEntity playerEntity = client.player;
                if (!(playerEntity instanceof PlayerProperties playerProperties)) {
                    return;
                }

                if (exitSlimeSuit.isPressed() && !exitSlimeSuitBoolean) {
                    exitSlimeSuitBoolean = exitSlimeSuit.isPressed();
                    if (playerProperties.hasSlimeSuit()) {
                        PacketByteBuf buf = PacketByteBufs.create();
                        ClientPlayNetworking.send(new Identifier(RaidBase.MOD_ID, "exitslimesuit"), buf);
                    }
                } else {
                    exitSlimeSuitBoolean = exitSlimeSuit.isPressed();
                }
            });
        } catch (RuntimeException ignored) {

        }

        ClientPlayNetworking.registerGlobalReceiver(RaidBase.IDENTIFIER, (client, handler, buf, responseSender) -> {
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
                        double j = entity.getZ();

                        for (int i = 0; i < 3; ++i) {
                            Vec3d vel = new Vec3d(random.nextGaussian(), random.nextDouble(), random.nextGaussian());

                            world.addParticle(ParticleTypes.SMOKE, true, entity.getX() + vel.getX() * entity.getWidth() * 0.8, entity.getRandomBodyY(), entity.getZ() + vel.getZ() * entity.getWidth() * 0.8,
                                    vel.getX() * 0.01, vel.getY() * 0.01, vel.getZ() * 0.01);
                        }


                        for (int i = 0; i < 5; ++i) {
                            Vec3d vel = new Vec3d(random.nextGaussian(), random.nextDouble(), random.nextGaussian());

                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.LASER_DUST, new Vector3f(1f, 0f, 0f)),
                                    true, entity.getX() + vel.getX() * entity.getWidth() * 0.8, entity.getRandomBodyY(), entity.getZ() + vel.getZ() * entity.getWidth() * 0.8,
                                    vel.getX() * 0.3, vel.getY() * 0.3, vel.getZ() * 0.3);
                        }

                        world.playSound(g, entity.getBodyY(0.5), j, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.AMBIENT, 1f, 1f, true);
                    } else if (status == 2) {
                        if (entity instanceof PigProperties pigProperties) {
                            pigProperties.getStandUpState().startIfNotRunning(entity.age);
                            pigProperties.setPigSoldier(true);
                        }

                        double g = entity.getX();
                        double j = entity.getZ();

                        for (int i = 0; i < 10; ++i) {
                            double h = entity.getRandomBodyY();
                            double x = world.getRandom().nextGaussian() * 0.3;
                            double y = world.getRandom().nextGaussian() * 0.3;
                            double z = world.getRandom().nextGaussian() * 0.3;

                            world.addParticle(ParticleTypes.HAPPY_VILLAGER, true, g + x, h + y, j + z, x * 0.4, y * 0.4, z * 0.4);
                        }

                        for (int i = 0; i < 6; ++i) {
                            double h = entity.getRandomBodyY();
                            double x = world.getRandom().nextGaussian() * 0.3;
                            double y = world.getRandom().nextGaussian() * 0.3;
                            double z = world.getRandom().nextGaussian() * 0.3;

                            world.addParticle(ParticleTypes.HEART, true, g + x, h + y, j + z, x * 0.4, y * 0.4, z * 0.4);
                        }

                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_PIGLIN_CELEBRATE,
                                entity.getSoundCategory(), 1f, 1.6f, true);
                    } else if (status == 3) {
                        world.addParticle(new ShockwaveParticleEffect(ParticleRegistry.BAN_SHOCKWAVE, 15, 2f, 2f), entity.getX(), entity.getBodyY(0.5f), entity.getZ(), 0, 90, entity.getYaw());
                        world.addParticle(new ShockwaveParticleEffect(ParticleRegistry.BAN_SHOCKWAVE, 15, 2f, 2f), entity.getX(), entity.getBodyY(0.5f), entity.getZ(), 0, 45, entity.getYaw());
                        world.addParticle(new ShockwaveParticleEffect(ParticleRegistry.BAN_SHOCKWAVE, 15, 2f, 2f), entity.getX(), entity.getBodyY(0.5f), entity.getZ(), 0, 135, entity.getYaw());
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1f, 1f, true);
                    } else if (status == 4) {
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
                    } else if (status == 5) {
                        double g = entity.getX();
                        double j = entity.getZ();
                        List<Vector3f> color = Arrays.asList(Vec3d.unpackRgb(0x73C262).toVector3f(),
                                Vec3d.unpackRgb(0x5AAA43).toVector3f());

                        for (int i = 0; i < 15; ++i) {
                            double h = entity.getRandomBodyY();
                            double d = random.nextGaussian() * 0.4;
                            double e = random.nextDouble() * 1;
                            double f = random.nextGaussian() * 0.4;

                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.TRANSPARENT_DUST, color.get(random.nextInt(color.size()))),
                                    true, g + d, h, j + f, d * 0.8, e * 0.5, f * 0.8);
                        }
                        for (int i = 0; i < 35; ++i) {
                            double h = entity.getRandomBodyY();
                            double d = random.nextGaussian() * 0.4;
                            double e = random.nextDouble() * 1;
                            double f = random.nextGaussian() * 0.4;

                            world.addParticle(ParticleRegistry.SLIME,
                                    true, g + d, h, j + f, d * 0.8, e * 0.5, f * 0.8);
                        }
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_SLIME_SQUISH,
                                entity.getSoundCategory(), 1f, 1f, true);
                    } else if (status == 6) {
                        double g = entity.getX();
                        double j = entity.getZ();
                        List<Vector3f> color = Arrays.asList(Vec3d.unpackRgb(0x73C262).toVector3f(),
                                Vec3d.unpackRgb(0x5AAA43).toVector3f());

                        for (int i = 0; i < 25; ++i) {
                            double h = entity.getRandomBodyY();
                            double d = random.nextGaussian() * 0.4;
                            double e = random.nextDouble() * 1;
                            double f = random.nextGaussian() * 0.4;

                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.TRANSPARENT_DUST, color.get(random.nextInt(color.size()))),
                                    true, g + d, h, j + f, d * 0.8, e * 0.5, f * 0.8);
                        }
                        for (int i = 0; i < 10; ++i) {
                            double h = entity.getRandomBodyY();
                            double d = random.nextGaussian() * 0.4;
                            double e = random.nextDouble() * 1;
                            double f = random.nextGaussian() * 0.4;

                            world.addParticle(ParticleRegistry.SLIME,
                                    true, g + d, h, j + f, d * 0.8, e * 0.5, f * 0.8);
                        }
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_SLIME_SQUISH,
                                entity.getSoundCategory(), 1f, 1f, true);
                    }else if (status == 7) {
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

        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.LIGHTNING, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
            matrices.translate(0.5, 0, 0.5);

            if (mode.equals(ModelTransformationMode.GUI)) {
                matrices.scale(0.02f, 0.02f, 0.02f);
            } else {
                matrices.scale(0.04f, 0.04f, 0.04f);
            }
            MinecraftClient.getInstance().getEntityRenderDispatcher().render(new LightningEntity(EntityType.LIGHTNING_BOLT, MinecraftClient.getInstance().world), BlockPos.ORIGIN.getX(), BlockPos.ORIGIN.getY(), BlockPos.ORIGIN.getZ(), 0, 0, matrices, vertexConsumers, light);
        });

        ClientPlayNetworking.registerGlobalReceiver(new Identifier(RaidBase.MOD_ID, "slimesuit"), (client, handler, buf, responseSender) -> {
            try {

                World world = client.world;
                if (world == null) {
                    return;
                }

                PlayerEntity playerEntity = world.getPlayerByUuid(buf.readUuid());

                if (playerEntity != null
                        && world.getEntityById(buf.readInt()) instanceof ProjectileEntity projectileEntity) {
                    Random random = Random.create();

                    Vec3d rotation = projectileEntity.getPos().subtract(playerEntity.getPos().add(0, playerEntity.getHeight() * 0.5, 0)).normalize().multiply(0.5f);

                    double g = playerEntity.getX() + rotation.getX() * 1.5;
                    double j = playerEntity.getZ() + rotation.getZ() * 1.5;
                    List<Vector3f> color = Arrays.asList(Vec3d.unpackRgb(0x73C262).toVector3f(),
                            Vec3d.unpackRgb(0x5AAA43).toVector3f());

                    client.execute(() -> {
                        for (int i = 0; i < 4; ++i) {
                            double h = projectileEntity.getRandomBodyY();
                            double d = random.nextGaussian() * 0.4;
                            double e = random.nextDouble() * 1;
                            double f = random.nextGaussian() * 0.4;

                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.TRANSPARENT_DUST, color.get(random.nextInt(color.size()))),
                                    true, g + d, h, j + f, rotation.getX() + d * 0.2, e * 0.5, rotation.getZ() + f * 0.2);
                        }
                        for (int i = 0; i < 6; ++i) {
                            double h = projectileEntity.getRandomBodyY();
                            double d = random.nextGaussian() * 0.4;
                            double e = random.nextDouble() * 1;
                            double f = random.nextGaussian() * 0.4;

                            world.addParticle(ParticleRegistry.SLIME,
                                    true, g + d, h, j + f, rotation.getX() + d * 0.2, e * 0.5, rotation.getZ() + f * 0.2);
                        }

                        world.playSound(projectileEntity.getX(), projectileEntity.getBodyY(0.5), projectileEntity.getZ(), SoundEvents.ENTITY_SLIME_SQUISH,
                                projectileEntity.getSoundCategory(), 1f, 1f, true);
                    });
                }
            } catch (Exception exception) {

            }
        });

    }
}
