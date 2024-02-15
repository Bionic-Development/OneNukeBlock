package de.takacick.secretgirlbase;

import com.google.common.collect.Lists;
import de.takacick.secretgirlbase.client.renderer.ShadowRenderer;
import de.takacick.secretgirlbase.mixin.IngameHudAccessor;
import de.takacick.secretgirlbase.registry.EntityRegistry;
import de.takacick.secretgirlbase.registry.ItemRegistry;
import de.takacick.secretgirlbase.registry.ParticleRegistry;
import de.takacick.secretgirlbase.registry.block.entity.renderer.BubbleGumLauncherBlockEntityRenderer;
import de.takacick.secretgirlbase.registry.block.entity.renderer.MagicDisappearingPlatformBlockEntityRenderer;
import de.takacick.secretgirlbase.registry.block.entity.renderer.MagicFlowerDoorBlockEntityRenderer;
import de.takacick.secretgirlbase.registry.entity.custom.renderer.FireworkTimeBombEntityRenderer;
import de.takacick.secretgirlbase.registry.entity.living.ZukoEntity;
import de.takacick.secretgirlbase.registry.entity.living.renderer.ZukoEntityRenderer;
import de.takacick.secretgirlbase.registry.entity.projectiles.renderer.BubbleGumEntityRenderer;
import de.takacick.secretgirlbase.registry.particles.*;
import de.takacick.secretgirlbase.registry.particles.goop.GoopDropParticle;
import de.takacick.secretgirlbase.registry.particles.goop.GoopDropParticleEffect;
import de.takacick.secretgirlbase.registry.particles.goop.GoopParticle;
import de.takacick.secretgirlbase.registry.particles.goop.GoopStringParticle;
import de.takacick.utils.BionicUtilsClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class SecretGirlBaseClient implements ClientModInitializer {

    private KeyBinding smokeDrop;
    private Boolean smokeDropBoolean = false;

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityRegistry.ZUKO, ZukoEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.BUBBLE_GUM, BubbleGumEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.FIREWORK_TIME_BOMB, FireworkTimeBombEntityRenderer::new);
        BlockEntityRendererFactories.register(EntityRegistry.MAGIC_FLOWER_DOOR, MagicFlowerDoorBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(EntityRegistry.MAGIC_DISAPPEARING_PLATFORM, MagicDisappearingPlatformBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(EntityRegistry.BUBBLE_GUM_LAUNCHER, BubbleGumLauncherBlockEntityRenderer::new);

        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP, GoopParticle.GoopParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP_DROP, GoopDropParticle.GoopDropParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP_STRING, GoopStringParticle.GoopStringParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.COLORED_DUST, ColoredDustParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.COLORED_FIREWORK, ColoredFireworksSparkParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.MAGIC_FLOWER, MagicFlowerParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.MAGIC_SPARK, MagicSparkParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.COLORED_BUBBLE_DUST, ColoredBubbleDustParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GROW, GrowParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.SHRINK, ShrinkParticle.Factory::new);

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ItemRegistry.TEENY_BERRY_BUSH, ItemRegistry.GREAT_VINES, ItemRegistry.GREAT_VINES_PLANT);

        try {
            smokeDrop = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                    "Smoke & Drop",
                    InputUtil.Type.KEYSYM,
                    GLFW.GLFW_KEY_X,
                    "SecretGirlBase Abilities"
            ));
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                PlayerEntity playerEntity = client.player;
                if (playerEntity == null) {
                    return;
                }

                if (smokeDrop.isPressed() && !smokeDropBoolean) {
                    smokeDropBoolean = true;
                    PacketByteBuf buf = PacketByteBufs.create();
                    ClientPlayNetworking.send(new Identifier(SecretGirlBase.MOD_ID, "smokedrop"), buf);
                } else {
                    smokeDropBoolean = smokeDrop.isPressed();
                }
            });
        } catch (RuntimeException exception) {

        }

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            BlockState blockState = Blocks.GRASS_BLOCK.getDefaultState();
            return ColorProviderRegistry.BLOCK.get(blockState.getBlock()).getColor(blockState, null, null, tintIndex);
        }, ItemRegistry.MAGIC_FLOWER_DOOR);

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            if (world == null || pos == null) {
                return GrassColors.getDefaultColor();
            }
            return BiomeColors.getGrassColor(world, pos);
        }, ItemRegistry.MAGIC_FLOWER_DOOR_GRASS_BLOCK);

        ClientPlayNetworking.registerGlobalReceiver(SecretGirlBase.IDENTIFIER, (client, handler, buf, responseSender) -> {
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
                        for (int i = 0; i < 15; ++i) {
                            Vec3d vel = new Vec3d(random.nextGaussian(), random.nextDouble(), random.nextGaussian());

                            world.addParticle(ParticleTypes.POOF, true,
                                    entity.getX() + vel.getX() * 0.4, entity.getRandomBodyY(), entity.getZ() + vel.getZ() * 0.4,
                                    vel.getX() * 0.01, vel.getY() * 0.01, vel.getZ() * 0.01);
                        }

                        for (int i = 0; i < 4; ++i) {
                            Vec3d vel = new Vec3d(random.nextGaussian(), random.nextDouble(), random.nextGaussian());

                            world.addParticle(ParticleTypes.HEART, true,
                                    entity.getX() + vel.getX() * 0.4, entity.getRandomBodyY(), entity.getZ() + vel.getZ() * 0.4,
                                    vel.getX() * 0.01, Math.abs(vel.getY()) * 0.03, vel.getZ() * 0.01);
                        }

                        if (entity instanceof ZukoEntity zukoEntity) {
                            for (int i = 0; i < 15; ++i) {
                                Vec3d vel = new Vec3d(random.nextGaussian(), random.nextDouble(), random.nextGaussian());

                                world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_FIREWORK, new Vector3f(zukoEntity.getCollarColor().getColorComponents())), true,
                                        entity.getX() + vel.getX() * 0.4, entity.getRandomBodyY(), entity.getZ() + vel.getZ() * 0.4,
                                        vel.getX() * 0.01, vel.getY() * 0.01, vel.getZ() * 0.01);
                            }
                        }

                    } else if (status == 2) {
                        for (int i = 0; i < 15; ++i) {
                            Vec3d vel = new Vec3d(random.nextGaussian(), random.nextDouble(), random.nextGaussian());

                            world.addParticle(ParticleTypes.POOF, true,
                                    entity.getX() + vel.getX() * 0.4, entity.getRandomBodyY(), entity.getZ() + vel.getZ() * 0.4,
                                    vel.getX() * 0.01, vel.getY() * 0.01, vel.getZ() * 0.01);
                        }

                        for (int i = 0; i < 4; ++i) {
                            Vec3d vel = new Vec3d(random.nextGaussian(), random.nextDouble(), random.nextGaussian());

                            world.addParticle(ParticleTypes.HEART, true,
                                    entity.getX() + vel.getX() * 0.4, entity.getRandomBodyY(), entity.getZ() + vel.getZ() * 0.4,
                                    vel.getX() * 0.01, Math.abs(vel.getY()) * 0.03, vel.getZ() * 0.01);
                        }
                        for (int i = 0; i < 15; ++i) {
                            Vec3d vel = new Vec3d(random.nextGaussian(), random.nextDouble(), random.nextGaussian());

                            world.addParticle(ParticleTypes.HAPPY_VILLAGER, true,
                                    entity.getX() + vel.getX() * 0.6, entity.getRandomBodyY(), entity.getZ() + vel.getZ() * 0.6,
                                    vel.getX() * 0.01, Math.abs(vel.getY()) * 0.03, vel.getZ() * 0.01);
                        }

                        world.playSound(entity.getX(), entity.getBodyY(0.5f), entity.getZ(), SoundEvents.ENTITY_PHANTOM_FLAP, SoundCategory.AMBIENT, 2f, 2f, true);
                    } else if (status == 5) {
                        world.playSound(entity.getX(), entity.getBodyY(0.5f), entity.getZ(), SoundEvents.BLOCK_HONEY_BLOCK_BREAK, SoundCategory.AMBIENT, 2f, 1f, true);

                        for (int i = 0; i < 5; ++i) {
                            Vec3d vel = new Vec3d(random.nextGaussian(), random.nextDouble(), random.nextGaussian());

                            world.addParticle(new GoopDropParticleEffect(Vec3d.unpackRgb(0xEA1CD0).toVector3f(), random.nextFloat() * 0.25f + 0.1f),
                                    entity.getX() + vel.getX() * entity.getWidth() * 0.8, entity.getRandomBodyY(), entity.getZ() + vel.getZ() * entity.getWidth() * 0.8,
                                    vel.getX() * 0.1, vel.getY() * 0.1, vel.getZ() * 0.1);
                        }

                        for (int i = 0; i < 15; ++i) {
                            Vec3d vel = new Vec3d(random.nextGaussian(), random.nextDouble(), random.nextGaussian());

                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_DUST, Vec3d.unpackRgb(0xEA1CD0).toVector3f()),
                                    true, entity.getX() + vel.getX() * entity.getWidth() * 0.8, entity.getRandomBodyY(), entity.getZ() + vel.getZ() * entity.getWidth() * 0.8,
                                    vel.getX() * 0.3, vel.getY() * 0.3, vel.getZ() * 0.3);
                        }
                    } else if (status == 6) {
                        for (int i = 0; i < 2; ++i) {
                            Vec3d vel = new Vec3d(random.nextGaussian(), random.nextDouble(), random.nextGaussian());

                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.GROW, Vec3d.unpackRgb(0x45F1EC).toVector3f(), entityId),
                                    true, entity.getX() + vel.getX() * entity.getWidth() * 0.6, entity.getRandomBodyY(), entity.getZ() + vel.getZ() * entity.getWidth() * 0.6,
                                    vel.getX() * 0.3, 1, vel.getZ() * 0.3);
                        }
                    } else if (status == 7) {
                        for (int i = 0; i < 2; ++i) {
                            Vec3d vel = new Vec3d(random.nextGaussian(), random.nextDouble(), random.nextGaussian());

                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.SHRINK, Vec3d.unpackRgb(0xBAFF36).toVector3f(), entityId),
                                    true, entity.getX() + vel.getX() * entity.getWidth() * 0.6, entity.getRandomBodyY(), entity.getZ() + vel.getZ() * entity.getWidth() * 0.6,
                                    vel.getX() * 0.3, 1, vel.getZ() * 0.3);
                        }
                    } else if (status == 8) {
                        NbtCompound nbtCompound = SecretGirlBaseClient.getRandomRocketNbt(random);
                        for (int i = 0; i < 25; i++) {
                            world.addFireworkParticle(entity.getX() + random.nextGaussian() * 15, entity.getBodyY(0.6) + random.nextGaussian() * 10, entity.getZ() + random.nextGaussian() * 15, 0, 0, 0, nbtCompound);
                        }
                        world.addFireworkParticle(entity.getX(), entity.getBodyY(0.6), entity.getZ(), 0, 0, 0, nbtCompound);
                    } else if (status == 9) {
                        for (int i = 0; i < 3; i++) {
                            world.addFireworkParticle(entity.getX() + random.nextGaussian() * 15, entity.getBodyY(0.6) + random.nextGaussian() * 10, entity.getZ() + random.nextGaussian() * 15, 0, 0, 0, SecretGirlBaseClient.getRandomRocketNbt(random));
                        }
                    }
                }
            });
        });

        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.ZUKO, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
            matrices.translate(0.5, 0, 0.5);

            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));

            Entity entity = EntityRegistry.ZUKO.create(MinecraftClient.getInstance().world);
            if (entity != null) {
                ShadowRenderer.renderEntity(entity, mode, matrices, vertexConsumers, light);
            }
        });

        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.FIREWORK_TIME_BOMB, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
            matrices.translate(0.5, 0, 0.5);

            Entity entity = EntityRegistry.FIREWORK_TIME_BOMB.create(MinecraftClient.getInstance().world);
            if (entity != null) {
                EntityRenderer<? super Entity> entityRenderer = MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(entity);

                entityRenderer.render(entity, 0, 0, matrices, vertexConsumers, light);
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(new Identifier(SecretGirlBase.MOD_ID, "healthupdate"), (client, handler, buf, responseSender) -> {
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
    }

    public static NbtCompound getRocketNbt() {
        NbtCompound nbtCompound = new NbtCompound();

        nbtCompound.putByte("Flight", (byte) 1);
        NbtCompound explosion = new NbtCompound();
        List<Integer> list = Lists.newArrayList();

        explosion.putBoolean("Flicker", true);
        explosion.putBoolean("Trail", true);
        list.add(0x8BCE29);

        explosion.putIntArray("Colors", list);
        explosion.putIntArray("FadeColors", list);
        explosion.putByte("Type", (byte) 1);
        NbtList nbtList = new NbtList();
        nbtList.add(explosion);

        nbtCompound.put("Explosions", nbtList);

        return nbtCompound;
    }

    public static NbtCompound getRandomRocketNbt(Random random) {
        NbtCompound nbtCompound = new NbtCompound();

        nbtCompound.putByte("Flight", (byte) 1);
        NbtCompound explosion = new NbtCompound();
        List<Integer> list = Lists.newArrayList();

        explosion.putBoolean("Flicker", random.nextBoolean());
        explosion.putBoolean("Trail", random.nextBoolean());
        list.add(BionicUtilsClient.getRainbow().getColorAsInt(random.nextInt(601)));

        explosion.putIntArray("Colors", list);
        explosion.putIntArray("FadeColors", list);
        explosion.putByte("Type", (byte) random.nextInt(5));
        NbtList nbtList = new NbtList();
        nbtList.add(explosion);

        nbtCompound.put("Explosions", nbtList);

        return nbtCompound;
    }
}
