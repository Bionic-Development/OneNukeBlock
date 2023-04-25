package de.takacick.everythinghearts;

import de.takacick.everythinghearts.mixin.IngameHudAccessor;
import de.takacick.everythinghearts.network.HeartmondExplosionHandler;
import de.takacick.everythinghearts.registry.EntityRegistry;
import de.takacick.everythinghearts.registry.ItemRegistry;
import de.takacick.everythinghearts.registry.ParticleRegistry;
import de.takacick.everythinghearts.registry.block.entity.HeartChestBlockEntity;
import de.takacick.everythinghearts.registry.block.entity.WeatherHeartBeaconBlockEntity;
import de.takacick.everythinghearts.registry.block.entity.renderer.HeartChestBlockEntityRenderer;
import de.takacick.everythinghearts.registry.block.entity.renderer.WeatherHeartBeaconBlockEntityRenderer;
import de.takacick.everythinghearts.registry.entity.living.ProtoEntity;
import de.takacick.everythinghearts.registry.entity.living.renderer.LoverWardenEntityRenderer;
import de.takacick.everythinghearts.registry.entity.living.renderer.ProtoEntityRenderer;
import de.takacick.everythinghearts.registry.entity.living.renderer.RevivedPlayerEntityRenderer;
import de.takacick.everythinghearts.registry.entity.projectiles.renderer.HeartEntityRenderer;
import de.takacick.everythinghearts.registry.entity.projectiles.renderer.HeartScytheEntityRenderer;
import de.takacick.everythinghearts.registry.entity.projectiles.renderer.HeartmondEntityRenderer;
import de.takacick.everythinghearts.registry.particles.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

public class EverythingHeartsClient implements ClientModInitializer {

    private KeyBinding heartTouch;
    private boolean heartTouchBoolean;
    private KeyBinding progressBarTick;
    private boolean progressBarTickBoolean;
    private KeyBinding heartSonicScreech;
    private boolean heartSonicScreechBoolean;

    public static float heartRainGradient = 0f;
    public static float prevHeartRainGradient = 0f;

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityRegistry.PROTO, ProtoEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.REVIVED_PLAYER, RevivedPlayerEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.LOVER_WARDEN, LoverWardenEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.HEART, HeartEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.HEART_SCYTHE, HeartScytheEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.HEARTMOND, HeartmondEntityRenderer::new);
        BlockEntityRendererRegistry.register(EntityRegistry.HEART_CHEST, HeartChestBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(EntityRegistry.WEATHER_HEART_BEACON, WeatherHeartBeaconBlockEntityRenderer::new);

        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.HEART, HeartParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.COLORED_GLOW_SPARK, ColoredGlowSparkParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.COLORED_FLASH, ColoredFlashParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.HEART_SHOCKWAVE, HeartShockwaveParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.HEART_SWEEP_ATTACK, HeartSweepAttackParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.HEART_EXPLOSION_EMITTER, new HeartExplosionEmitterParticle.Factory());
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.HEART_EXPLOSION, HeartExplosionParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.HEART_UPGRADE, HeartUpgradeParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.HEART_TOTEM, HeartTotemParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.HEART_POOF, HeartPoofParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.HEART_SONIC_BOOM, HeartSonicBoomParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.HEART_SPLASH, HeartSplashParticle.Factory::new);

        BlockRenderLayerMap.INSTANCE.putBlock(ItemRegistry.HEART_LEAVES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ItemRegistry.WEATHER_HEART_BEACON, RenderLayer.getCutoutMipped());

        try {
            heartTouch = KeyBindingHelper.registerKeyBinding(
                    new KeyBinding("Heart Touch",
                            InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X,
                            "EverythingHearts Abilities")
            );
            progressBarTick = KeyBindingHelper.registerKeyBinding(
                    new KeyBinding("Progress Bar Tick",
                            InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X,
                            "EverythingHearts Abilities")
            );
            heartSonicScreech = KeyBindingHelper.registerKeyBinding(
                    new KeyBinding("Heart Sonic Screech",
                            InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X,
                            "EverythingHearts Abilities")
            );
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                PlayerEntity playerEntity = client.player;
                if (playerEntity == null) {
                    return;
                }

                if (heartTouch.isPressed() && !heartTouchBoolean) {
                    heartTouchBoolean = heartTouch.isPressed();
                    PacketByteBuf buf = PacketByteBufs.create();
                    ClientPlayNetworking.send(new Identifier(EverythingHearts.MOD_ID, "hearttouch"), buf);
                } else {
                    heartTouchBoolean = heartTouch.isPressed();
                }

                if (progressBarTick.isPressed() && !progressBarTickBoolean) {
                    progressBarTickBoolean = progressBarTick.isPressed();
                    PacketByteBuf buf = PacketByteBufs.create();
                    ClientPlayNetworking.send(new Identifier(EverythingHearts.MOD_ID, "progressbartick"), buf);
                } else {
                    progressBarTickBoolean = progressBarTick.isPressed();
                }

                if (heartSonicScreech.isPressed() && !heartSonicScreechBoolean) {
                    heartSonicScreechBoolean = heartSonicScreech.isPressed();
                    if (playerEntity.getVehicle() instanceof ProtoEntity) {
                        PacketByteBuf buf = PacketByteBufs.create();
                        ClientPlayNetworking.send(new Identifier(EverythingHearts.MOD_ID, "heartsonicscreech"), buf);
                    }
                } else {
                    heartSonicScreechBoolean = heartSonicScreech.isPressed();
                }
            });
        } catch (RuntimeException ignored) {

        }

        ClientPlayNetworking.registerGlobalReceiver(EverythingHearts.IDENTIFIER, (client, handler, buf, responseSender) -> {
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
                        client.gameRenderer.showFloatingItem(ItemRegistry.MYSTIC_HEART_TOUCH.getDefaultStack());

                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ITEM_TOTEM_USE, entity.getSoundCategory(), 0.5f, 1f, true);
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_PLAYER_LEVELUP, entity.getSoundCategory(), 1.0f, 1f, true);
                    } else if (status == 2) {
                        for (int i = 0; i < 50; ++i) {
                            double g = entity.getX();
                            double h = entity.getBodyY(0.5);
                            double j = entity.getZ();
                            world.addParticle(ParticleRegistry.HEART, true, g, h, j, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3);
                        }

                        for (int i = 0; i < 50; ++i) {
                            double g = entity.getX();
                            double h = entity.getBodyY(0.5);
                            double j = entity.getZ();
                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_GLOW_SPARK, new Vec3f(Vec3d.unpackRgb(0xFF1313))), true, g, h, j, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3);
                        }

                        client.gameRenderer.showFloatingItem(null);

                        world.addParticle(new ShockwaveParticleEffect(ParticleRegistry.HEART_SHOCKWAVE, 6, 0.6f, 0.4f), entity.getX(), entity.getBodyY(0.5f), entity.getZ(), 0, 90, entity.getYaw());

                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_WARDEN_HEARTBEAT, entity.getSoundCategory(), 0.5f, 3f, true);
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_PLAYER_LEVELUP, entity.getSoundCategory(), 0.4f, 1f, true);
                    } else if (status == 3) {
                        for (int i = 0; i < 50; ++i) {
                            double g = entity.getX();
                            double h = entity.getBodyY(0.5);
                            double j = entity.getZ();
                            world.addParticle(ParticleRegistry.HEART, true, g, h, j, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3);
                        }

                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.BLOCK_AMETHYST_CLUSTER_HIT, entity.getSoundCategory(), 1.5f, 1f, true);
                    } else if (status == 4) {
                        for (int i = 0; i < 10; ++i) {
                            double g = entity.getX();
                            double h = entity.getBodyY(0.5);
                            double j = entity.getZ();
                            world.addParticle(ParticleRegistry.HEART, true, g, h, j, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3);
                        }

                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.BLOCK_AMETHYST_CLUSTER_HIT, entity.getSoundCategory(), 0.5f, 1f, true);
                    } else if (status == 5) {
                        for (int i = 0; i < 25; ++i) {
                            double g = entity.getX();
                            double h = entity.getBodyY(0.5);
                            double j = entity.getZ();
                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_GLOW_SPARK, new Vec3f(Vec3d.unpackRgb(0xFF1313))), true, g, h, j, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3);
                        }

                        client.particleManager.addEmitter(entity, ParticleRegistry.HEART_TOTEM, 3);
                        world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_WARDEN_HEARTBEAT, entity.getSoundCategory(), 1.0f, 1.0f, false);
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, entity.getSoundCategory(), 1.0f, 1.0f, false);
                        world.addParticle(new HeartUpgradeParticleEffect(entity.getHeight(), entity.getWidth() + 0.5f, entityId), entity.getX(), entity.getY(), entity.getZ(), 0, 0, 0);
                        world.addParticle(new HeartUpgradeParticleEffect(entity.getHeight() + 0.2f, entity.getWidth() + 0.5f, entityId), entity.getX(), entity.getY() - 0.2f, entity.getZ(), 0, 0, 0);
                        world.addParticle(new HeartUpgradeParticleEffect(entity.getHeight() + 0.4f, entity.getWidth() + 0.5f, entityId), entity.getX(), entity.getY() - 0.4f, entity.getZ(), 0, 0, 0);
                    } else if (status == 6) {
                        for (int i = 0; i < 25; ++i) {
                            double g = entity.getX();
                            double h = entity.getBodyY(0.5);
                            double j = entity.getZ();
                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_GLOW_SPARK, new Vec3f(Vec3d.unpackRgb(0xFF1313))), true, g, h, j, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3);
                        }

                        client.particleManager.addEmitter(entity, ParticleRegistry.HEART_TOTEM, 3);
                        world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_WARDEN_HEARTBEAT, entity.getSoundCategory(), 1.0f, 1.0f, false);
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, entity.getSoundCategory(), 1.0f, 1.0f, false);
                        world.addParticle(new HeartUpgradeParticleEffect(-entity.getHeight(), entity.getWidth() + 0.5f, entityId), entity.getX(), entity.getBodyY(1.0), entity.getZ(), 0, 0, 0);
                        world.addParticle(new HeartUpgradeParticleEffect(-entity.getHeight() - 0.2f, entity.getWidth() + 0.5f, entityId), entity.getX(), entity.getBodyY(1.0) + 0.2f, entity.getZ(), 0, 0, 0);
                        world.addParticle(new HeartUpgradeParticleEffect(-entity.getHeight() - 0.4f, entity.getWidth() + 0.5f, entityId), entity.getX(), entity.getBodyY(1.0) + 0.4f, entity.getZ(), 0, 0, 0);
                    } else if (status == 7) {
                        for (int i = 0; i < 15; ++i) {
                            double g = entity.getX();
                            double h = entity.getBodyY(0.5);
                            double j = entity.getZ();
                            world.addParticle(ParticleRegistry.HEART, true, g, h, j, world.getRandom().nextGaussian() * 0.2, world.getRandom().nextGaussian() * 0.2, world.getRandom().nextGaussian() * 0.2);
                        }

                        for (int i = 0; i < 10; ++i) {
                            double g = entity.getX();
                            double h = entity.getBodyY(0.5);
                            double j = entity.getZ();
                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_GLOW_SPARK, new Vec3f(Vec3d.unpackRgb(0xFF1313))), true, g, h, j, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3);
                        }
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, entity.getSoundCategory(), 0.5f, 5f, true);
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_WARDEN_HEARTBEAT, entity.getSoundCategory(), 0.3f, 5f, true);
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, entity.getSoundCategory(), 0.2f, 2f, true);
                    }
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(new Identifier(EverythingHearts.MOD_ID, "healthupdate"), (client, handler, buf, responseSender) -> {
            try {
                double maxHealthValue = buf.readDouble();
                int healthValue = buf.readInt();
                float health = buf.readFloat();

                client.execute(() -> {
                    if (client.player != null) {
                        client.player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(maxHealthValue);
                        client.player.setHealth(health);
                    }
                    ((IngameHudAccessor) client.inGameHud).setLastHealthValue(healthValue);
                    ((IngameHudAccessor) client.inGameHud).setRenderHealthValue(healthValue);
                });
            } catch (Exception exception) {

            }
        });

        ClientPlayNetworking.registerGlobalReceiver(new Identifier(EverythingHearts.MOD_ID, "heartrain"), (client, handler, buf, responseSender) -> {
            try {
                float heartRain = buf.readFloat();
                setHeartRainGradient(heartRain);
            } catch (Exception exception) {

            }
        });

        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.HEART_CHEST, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
            MinecraftClient.getInstance().getBlockEntityRenderDispatcher().renderEntity(new HeartChestBlockEntity(BlockPos.ORIGIN, ItemRegistry.HEART_CHEST.getDefaultState()), matrices, vertexConsumers, light, overlay);
        });

        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.WEATHER_HEART_BEACON, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
            MinecraftClient.getInstance().getBlockEntityRenderDispatcher().renderEntity(new WeatherHeartBeaconBlockEntity(BlockPos.ORIGIN, ItemRegistry.WEATHER_HEART_BEACON.getDefaultState()), matrices, vertexConsumers, light, overlay);
        });

        ClientPlayNetworking.registerGlobalReceiver(HeartmondExplosionHandler.IDENTIFIER, (client, handler, buf, responseSender) -> {
            try {
                HeartmondExplosionHandler packet = new HeartmondExplosionHandler(buf);
                client.execute(() -> {
                    HeartmondExplosionHandler.Explosion explosion = new HeartmondExplosionHandler.Explosion(client.world, null, packet.getX(), packet.getY(), packet.getZ(), packet.getRadius(), packet.getAffectedBlocks());
                    explosion.affectWorld(true);
                    client.player.setVelocity(client.player.getVelocity().add(packet.getPlayerVelocityX(), packet.getPlayerVelocityY(), packet.getPlayerVelocityZ()));
                });
            } catch (Exception exception) {

            }
        });

    }

    public static float getHeartRainGradient(float delta) {
        return MathHelper.lerp(delta, prevHeartRainGradient, heartRainGradient);
    }

    public static void setHeartRainGradient(float rainGradient) {
        float f;
        prevHeartRainGradient = f = MathHelper.clamp(rainGradient, 0.0f, 1.0f);
        heartRainGradient = f;
    }
}
