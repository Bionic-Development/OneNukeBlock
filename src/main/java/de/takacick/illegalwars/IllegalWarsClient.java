package de.takacick.illegalwars;

import com.google.common.collect.Lists;
import de.takacick.illegalwars.client.screen.CommandBlockPressurePlateScreen;
import de.takacick.illegalwars.registry.EntityRegistry;
import de.takacick.illegalwars.registry.ItemRegistry;
import de.takacick.illegalwars.registry.ParticleRegistry;
import de.takacick.illegalwars.registry.block.entity.CommandBlockPressurePlateBlockEntity;
import de.takacick.illegalwars.registry.block.entity.renderer.*;
import de.takacick.illegalwars.registry.entity.custom.renderer.MoneyBlockEntityRenderer;
import de.takacick.illegalwars.registry.entity.living.renderer.*;
import de.takacick.illegalwars.registry.entity.projectiles.CyberLaserEntity;
import de.takacick.illegalwars.registry.entity.projectiles.renderer.CyberLaserEntityRenderer;
import de.takacick.illegalwars.registry.entity.projectiles.renderer.GoldBlockEntityRenderer;
import de.takacick.illegalwars.registry.entity.projectiles.renderer.PoopEntityRenderer;
import de.takacick.illegalwars.registry.particles.*;
import de.takacick.illegalwars.registry.particles.goop.GoopDropParticle;
import de.takacick.illegalwars.registry.particles.goop.GoopDropParticleEffect;
import de.takacick.illegalwars.registry.particles.goop.GoopParticle;
import de.takacick.illegalwars.registry.particles.goop.GoopStringParticle;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.List;

public class IllegalWarsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityRegistry.MONEY_BLOCK, MoneyBlockEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.PROTO_PUPPY, ProtoPuppyEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.RAT, RatEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.POOP, PoopEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.GOLD_BLOCK, GoldBlockEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.SHARK, SharkEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.KING_RAT, KingRatEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.CYBER_WARDEN_SECURITY, CyberWardenSecurityEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.CYBER_LASER, CyberLaserEntityRenderer::new);
        BlockEntityRendererFactories.register(EntityRegistry.BASE_WARS_MONEY_WHEEL, BaseWarsMoneyWheelBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(EntityRegistry.POOP_LAUNCHER, PoopLauncherBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(EntityRegistry.KING_RAT_TRIAL_SPAWNER, KingRatTrialSpawnerBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(EntityRegistry.CYBER_WARDEN_SECURITY_TRIAL_SPAWNER, CyberWardenSecurityTrialSpawnerBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(EntityRegistry.PIGLIN_GOLD_TURRET, PiglinGoldTurretBlockEntityRenderer::new);

        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.COLORED_EXPLOSION, ColoredExplosionParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.COLORED_EXPLOSION_EMITTER, new ColoredExplosionEmitterParticle.Factory());
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP, GoopParticle.GoopParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP_DROP, GoopDropParticle.GoopDropParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP_STRING, GoopStringParticle.GoopStringParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.DUST, DustParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.COLORED_FLASH, ColoredFlashParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.LASER_DUST, LaserDustParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.FLYING_DUST, FlyingDustParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.FALLING_BLOOD, BloodLeakParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.BLOOD_SPLASH, BloodSplashParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.COLORED_FIREWORK, ColoredFireworksSparkParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.DOLLAR, DollarParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.COIN, DollarParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.SLUDGE_BUBBLE, SludgeBubbleParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.SLUDGE_BUBBLE_POP, SludgeBubblePopParticle.Factory::new);

        this.registerBlockLeakFactory(ParticleRegistry.DRIPPING_SLUDGE, SludgeBlockLeakParticle::createDrippingSludge);
        this.registerBlockLeakFactory(ParticleRegistry.FALLING_SLUDGE, SludgeBlockLeakParticle::createFallingSludge);
        this.registerBlockLeakFactory(ParticleRegistry.LANDING_SLUDGE, SludgeBlockLeakParticle::createLandingSludge);

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ItemRegistry.KING_RAT_TRIAL_SPAWNER, ItemRegistry.CYBER_WARDEN_SECURITY_TRIAL_SPAWNER);

        ClientPlayNetworking.registerGlobalReceiver(IllegalWars.IDENTIFIER, (client, handler, buf, responseSender) -> {
            if (client.player == null || client.world == null) {
                return;
            }

            ClientPlayerEntity playerEntity = client.player;
            ClientWorld world = client.world;

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

                        Vector3f color = Vec3d.unpackRgb(0x462A14).toVector3f();
                        world.addParticle(new GoopDropParticleEffect(color, 0.45f + (float) (random.nextDouble() * 0.5)),
                                true, g, entity.getY(), j, 0, 0, 0);

                        for (int i = 0; i < 7; ++i) {
                            double h = entity.getRandomBodyY();
                            double d = random.nextGaussian() * 0.4;
                            double e = random.nextDouble() * 1;
                            double f = random.nextGaussian() * 0.4;

                            world.addParticle(new GoopDropParticleEffect(color, 0.45f + (float) (random.nextDouble() * 0.5)),
                                    true, g + d, h, j + f, d * 0.5, e * 0.3, f * 0.5);
                        }

                        addPotion(entity.getPos().add(0, entity.getHeight() / 2, 0), 0x462A14, 4);
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.BLOCK_MUD_BREAK,
                                entity.getSoundCategory(), 1.3f, 1f, true);
                    } else if (status == 4) {
                        double g = entity.getX();
                        double j = entity.getZ();

                        Vector3f color = Vec3d.unpackRgb(0x462A14).toVector3f();

                        for (int i = 0; i < 6; ++i) {
                            double h = entity.getRandomBodyY();
                            double d = random.nextGaussian() * 0.4;
                            double e = random.nextDouble() * 1;
                            double f = random.nextGaussian() * 0.4;

                            world.addParticle(new GoopDropParticleEffect(color, 0.15f + (float) (random.nextDouble() * 0.5)),
                                    true, g + d, h, j + f, d * 0.1, e * 0.1, f * 0.1);
                        }

                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.BLOCK_MUD_BREAK,
                                entity.getSoundCategory(), 1.3f, 1f, true);
                    } else if (status == 5) {
                        double g = entity.getX();
                        double j = entity.getZ();

                        List<Vector3f> colors = Arrays.asList(0xF5CC27, 0xFFD83E, 0xFFFD90, 0xFEE048).stream().map(hex -> Vec3d.unpackRgb(hex).toVector3f()).toList();

                        world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_EXPLOSION, new Vector3f(colors.get(world.getRandom().nextInt(colors.size())))),
                                g, entity.getBodyY(0.5), j,
                                0.35, 0.01, 0.01);

                        for (int i = 0; i < 35; i++) {
                            ParticleEffect particleEffect = new ColoredParticleEffect(ParticleRegistry.DUST, new Vector3f(colors.get(world.getRandom().nextInt(colors.size()))));

                            double h = entity.getRandomBodyY();
                            double d = random.nextGaussian() * 1;
                            double e = random.nextDouble() * 1;
                            double f = random.nextGaussian() * 1;

                            world.addParticle(particleEffect,
                                    true, g + d * 0.4, h, j + f * 0.4, d * 1, e * 1, f * 1);
                        }

                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE,
                                entity.getSoundCategory(), 1.3f, 1f, false);
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.BLOCK_NETHER_GOLD_ORE_BREAK,
                                entity.getSoundCategory(), 1.3f, 1f, true);
                    } else if (status == 6) {
                        double g = entity.getX();
                        double j = entity.getZ();
                        List<Vector3f> colors = Arrays.asList(0x80E38E, 0x64C972, 0x45D65A).stream().map(hex -> Vec3d.unpackRgb(hex).toVector3f()).toList();

                        client.particleManager.addParticle(new ColoredFireworksSparkParticle.FireworkParticle(world, colors.get(random.nextInt(colors.size())),
                                entity.getX(), entity.getBodyY(0.5), entity.getZ(),
                                0, 0, 0, client.particleManager, getRocketNbt()));
                        client.particleManager.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_FLASH, Vec3d.unpackRgb(0x80E38E).toVector3f()),
                                entity.getX(), entity.getBodyY(0.5), entity.getZ(),
                                0, 0, 0);

                        for (int i = 0; i < 35; ++i) {
                            ParticleEffect particleEffect = new ColoredParticleEffect(ParticleRegistry.COLORED_FIREWORK, colors.get(random.nextInt(colors.size())));

                            double h = entity.getRandomBodyY();

                            double d = random.nextGaussian();
                            double e = random.nextGaussian();
                            double f = random.nextGaussian();

                            world.addParticle(particleEffect,
                                    true,
                                    g + d * 0.3, h + e * 0.3, j + f * 0.3,
                                    d * 1.2, e * 1.2, f * 1.2);
                        }
                        for (int i = 0; i < 85; ++i) {
                            ParticleEffect particleEffect = random.nextDouble() <= 0.7 ? ParticleRegistry.DOLLAR : ParticleRegistry.COIN;

                            double h = entity.getRandomBodyY();

                            double d = random.nextGaussian();
                            double e = random.nextGaussian();
                            double f = random.nextGaussian();

                            world.addParticle(particleEffect,
                                    true,
                                    g + d * 0.3, h + e * 0.3, j + f * 0.3,
                                    d * 1.2, e * 1.2, f * 1.2);
                        }
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, SoundCategory.BLOCKS, 8.0f, 1f, false);
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 8.0f, 1f, false);
                    } else if (status == 7) {
                        double g = entity.getX();
                        double j = entity.getZ();

                        for (int i = 0; i < 3; ++i) {
                            Vec3d vel = new Vec3d(random.nextGaussian(), random.nextDouble(), random.nextGaussian());

                            world.addParticle(ParticleTypes.SMOKE, true, entity.getX() + vel.getX() * entity.getWidth() * 0.8, entity.getRandomBodyY(), entity.getZ() + vel.getZ() * entity.getWidth() * 0.8,
                                    vel.getX() * 0.01, vel.getY() * 0.01, vel.getZ() * 0.01);
                        }

                        Vector3f vector3f = CyberLaserEntity.COLOR;

                        for (int i = 0; i < 5; ++i) {
                            Vec3d vel = new Vec3d(random.nextGaussian(), random.nextDouble(), random.nextGaussian());

                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.LASER_DUST, vector3f),
                                    true, entity.getX() + vel.getX() * entity.getWidth() * 0.8, entity.getRandomBodyY(), entity.getZ() + vel.getZ() * entity.getWidth() * 0.8,
                                    vel.getX() * 0.3, vel.getY() * 0.3, vel.getZ() * 0.3);
                        }

                        world.playSound(g, entity.getBodyY(0.5), j, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.AMBIENT, 1f, 1f, true);
                    }
                }
            });
        });

        FluidRenderHandlerRegistry.INSTANCE.register(ItemRegistry.STILL_SLUDGE_LIQUID, ItemRegistry.FLOWING_SLUDGE_LIQUID, new SimpleFluidRenderHandler(
                new Identifier(IllegalWars.MOD_ID, "block/sludge_liquid_still"),
                new Identifier(IllegalWars.MOD_ID, "block/sludge_liquid_flow")
        ));
    }

    public static void addPotion(Vec3d vec3d, int color, int count) {
        Random random = Random.create();
        float u = (float) (color >> 16 & 0xFF) / 255.0f;
        float v = (float) (color >> 8 & 0xFF) / 255.0f;
        float w = (float) (color >> 0 & 0xFF) / 255.0f;
        DefaultParticleType particleEffect = ParticleTypes.EFFECT;
        for (int x = 0; x < count; ++x) {
            double e = random.nextDouble() * 1.0;
            double f = random.nextDouble() * Math.PI * 2.0;
            double y = Math.cos(f) * e;
            double z = 0.01 + random.nextDouble() * 0.5;
            double aa = Math.sin(f) * e;
            Particle particle = MinecraftClient.getInstance().particleManager.addParticle(particleEffect, vec3d.x + y * 0.1, vec3d.y + 0.25 * random.nextGaussian(), vec3d.z + aa * 0.1, y * 0.6, z, aa * 0.6);
            if (particle == null) continue;
            float ab = 0.75f + random.nextFloat() * 0.25f;
            particle.setColor(u * ab, v * ab, w * ab);
            particle.move((float) e);
        }
    }

    public static void addSludge(Vec3d vec3d, int count) {
        Random random = Random.create();
        DefaultParticleType particleEffect = ParticleRegistry.SLUDGE_BUBBLE;
        for (int x = 0; x < count; ++x) {
            double e = random.nextDouble() * 1.0;
            double f = random.nextDouble() * Math.PI * 2.0;
            double y = Math.cos(f) * e;
            double z = 0.01 + random.nextDouble() * 0.5;
            double aa = Math.sin(f) * e;
            Particle particle = MinecraftClient.getInstance().particleManager.addParticle(particleEffect, vec3d.x + y * 0.1, vec3d.y + 0.25 * random.nextGaussian(), vec3d.z + aa * 0.1, y * 0.6, z, aa * 0.6);
            if (particle == null) continue;
            particle.move((float) e);
        }
    }

    public static void openCommandBlockScreen(CommandBlockPressurePlateBlockEntity commandBlock) {
        MinecraftClient.getInstance().setScreen(new CommandBlockPressurePlateScreen(commandBlock));
    }

    private static NbtCompound getRocketNbt() {
        NbtCompound nbtCompound = new NbtCompound();

        nbtCompound.putByte("Flight", (byte) 1);
        NbtCompound explosion = new NbtCompound();
        List<Integer> list = Lists.newArrayList();
        list.add(0xFFFFFF);

        explosion.putBoolean("Flicker", true);

        explosion.putIntArray("Colors", list);
        explosion.putIntArray("FadeColors", list);
        explosion.putByte("Type", (byte) 0);
        NbtList nbtList = new NbtList();
        nbtList.add(explosion);

        nbtCompound.put("Explosions", nbtList);

        return nbtCompound;
    }

    private <T extends ParticleEffect> void registerBlockLeakFactory(ParticleType<T> type, ParticleFactory.BlockLeakParticleFactory<T> factory) {
        ParticleFactoryRegistry.getInstance().register(type, (spriteBillboardParticle) -> (t, world, x, y, z, velocityX, velocityY, velocityZ) -> {
            SpriteBillboardParticle billboardParticle = factory.createParticle(t, world, x, y, z, velocityX, velocityY, velocityZ);
            if (billboardParticle != null) {
                billboardParticle.setSprite(spriteBillboardParticle);
            }
            return billboardParticle;
        });
    }

}
