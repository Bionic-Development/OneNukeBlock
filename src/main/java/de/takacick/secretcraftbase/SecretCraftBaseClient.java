package de.takacick.secretcraftbase;

import com.google.common.collect.Lists;
import de.takacick.secretcraftbase.access.PlayerProperties;
import de.takacick.secretcraftbase.mixin.IngameHudAccessor;
import de.takacick.secretcraftbase.registry.EntityRegistry;
import de.takacick.secretcraftbase.registry.ItemRegistry;
import de.takacick.secretcraftbase.registry.ParticleRegistry;
import de.takacick.secretcraftbase.registry.block.entity.renderer.*;
import de.takacick.secretcraftbase.registry.entity.custom.AbstractSchematicEntity;
import de.takacick.secretcraftbase.registry.entity.custom.renderer.*;
import de.takacick.secretcraftbase.registry.entity.living.SecretPigPoweredPortalEntity;
import de.takacick.secretcraftbase.registry.entity.living.renderer.SecretPigPoweredPortalEntityRenderer;
import de.takacick.secretcraftbase.registry.particles.*;
import de.takacick.secretcraftbase.server.utils.EntityNbtHelper;
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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.SlimeEntityRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class SecretCraftBaseClient implements ClientModInitializer {

    private KeyBinding frogPickup;
    private Boolean frogPickupBoolean = false;
    private KeyBinding baseHuntRound;
    private Boolean baseHuntRoundBoolean = false;

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityRegistry.IRON_GOLEM_FARM, IronGolemFarmEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.TREASURY_ROOM, TreasuryRoomEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.ARMORY_ROOM, ArmoryRoomEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.SECRET_PIG_POWERED_PORTAL, SecretPigPoweredPortalEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.SECRET_GIANT_JUMPY_SLIME, SlimeEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.PLACING_BLOCK, AbstractBlockEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.BREAKING_BLOCK, AbstractBlockEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.XP_FARM, XPFarmEntityRenderer::new);
        BlockEntityRendererFactories.register(EntityRegistry.SECRET_MAGIC_WELL, SecretMagicWellBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(EntityRegistry.SECRET_REDSTONE_MIRROR_MELTER_ORE, SecretRedstoneMirrorMelterOreBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(EntityRegistry.SECRET_FAKE_SUN, SecretFakeSunBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(EntityRegistry.BIG_WHITE_BLOCK, BigWhiteBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(EntityRegistry.MELTING_BLOCK, MeltingBlockEntityRenderer::new);

        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.COLORED_FLASH, ColoredFlashParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.LASER_DUST, LaserDustParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.FALLING_BLOOD, BloodLeakParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.BLOOD_SPLASH, BloodSplashParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.COLORED_FIREWORK, ColoredFireworksSparkParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.MAGIC, MagicParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.FOLLOW_DUST, FollowDustParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.SPIT, SpitParticle.Factory::new);

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getTranslucent(), ItemRegistry.STONE_SECRET_REDSTONE_MIRROR_MELTER, ItemRegistry.DEEPSLATE_SECRET_REDSTONE_MIRROR_MELTER);
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ItemRegistry.DIAMOND_ORE_CHUNKS);

        try {
            frogPickup = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                    "Frog Pickup",
                    InputUtil.Type.KEYSYM,
                    GLFW.GLFW_KEY_X,
                    "SecretCraftBase Abilities"
            ));
            baseHuntRound = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                    "Base Hunt Round",
                    InputUtil.Type.KEYSYM,
                    GLFW.GLFW_KEY_X,
                    "SecretCraftBase Abilities"
            ));
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                PlayerEntity playerEntity = client.player;
                if (playerEntity == null) {
                    return;
                }

                if (frogPickup.isPressed() && !frogPickupBoolean) {
                    if (client.crosshairTarget instanceof EntityHitResult entityHitResult
                            && entityHitResult.getEntity() instanceof FrogEntity frogEntity) {
                        frogPickupBoolean = true;
                        PacketByteBuf buf = PacketByteBufs.create();
                        buf.writeInt(frogEntity.getId());
                        ClientPlayNetworking.send(new Identifier(SecretCraftBase.MOD_ID, "frogpickup"), buf);
                    }

                } else {
                    frogPickupBoolean = frogPickup.isPressed();
                }

                if (baseHuntRound.isPressed() && !baseHuntRoundBoolean) {
                    baseHuntRoundBoolean = baseHuntRound.isPressed();
                    PacketByteBuf buf = PacketByteBufs.create();
                    ClientPlayNetworking.send(new Identifier(SecretCraftBase.MOD_ID, "basehuntround"), buf);
                } else {
                    baseHuntRoundBoolean = baseHuntRound.isPressed();
                }
            });
        } catch (RuntimeException exception) {

        }

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            return world == null ? 4159204 : BiomeColors.getWaterColor(world, pos);
        }, ItemRegistry.SECRET_MAGIC_WELL_WATER);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            return 4159204;
        }, ItemRegistry.SECRET_MAGIC_WELL);

        ClientPlayNetworking.registerGlobalReceiver(SecretCraftBase.IDENTIFIER, (client, handler, buf, responseSender) -> {
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
                    if (status == 1 || status == 2) {
                        if (status == 1) {
                            if (entity instanceof PlayerProperties playerProperties) {
                                playerProperties.getHeartRemovalState().startIfNotRunning(entity.age);
                            }
                        }

                        double g = entity.getX();
                        double h = entity.getBodyY(0.7);
                        double j = entity.getZ();

                        for (int i = 0; i < 3; ++i) {
                            double d = random.nextGaussian() * 0.2;
                            double e = random.nextDouble() * 0.15;
                            double f = random.nextGaussian() * 0.2;

                            world.addParticle(ParticleRegistry.FALLING_BLOOD,
                                    true, g + d, h + e, j + f, d * 0.1, e * 0.1, f * 0.1);
                        }

                        if (status == 1 || world.getRandom().nextDouble() <= 0.3) {
                            world.playSound(g, h, j, SoundEvents.BLOCK_HONEY_BLOCK_SLIDE, playerEntity.getSoundCategory(), 1f, 1f, false);
                        }
                    } else if (status == 3 || status == 4) {
                        if (entity instanceof SecretPigPoweredPortalEntity secretPigPoweredPortalEntity) {
                            float f = status == 3 ? 0.6f : 0.5f;

                            Vec3d vec3d = secretPigPoweredPortalEntity.getSidewaysRotationVector().multiply(0.525);
                            Vec3d pos = secretPigPoweredPortalEntity.getPos().add(0, 0.625 * secretPigPoweredPortalEntity.getHeight() / 0.9f, 0).add(vec3d);

                            secretPigPoweredPortalEntity.getWorld().playSound(pos.getX(), pos.getY(), pos.getZ(),
                                    SoundEvents.BLOCK_LEVER_CLICK, secretPigPoweredPortalEntity.getSoundCategory(), 0.3f, f, true);

                            secretPigPoweredPortalEntity.getWorld().playSound(pos.getX(), pos.getY(), pos.getZ(),
                                    SoundEvents.ENTITY_ENDERMAN_TELEPORT, secretPigPoweredPortalEntity.getSoundCategory(), 0.35f, 0.7f + f, true);


                            if (status == 3) {
                                world.addParticle(new ColoredParticleEffect(ParticleRegistry.FOLLOW_DUST, DustParticleEffect.RED, 1f, secretPigPoweredPortalEntity.getId()), pos.getX(), pos.getY(), pos.getZ(),
                                        0.0, 0.0, 0.0);
                            }
                        }
                    } else if (status == 5) {
                        world.playSound(entity.getX(), entity.getBodyY(1.2), entity.getZ(), SoundEvents.ENTITY_SLIME_JUMP, SoundCategory.AMBIENT, 2f, 1f, true);
                        for (int j = 0; j < 8 * 8; ++j) {
                            float f = random.nextFloat() * ((float) Math.PI * 2);
                            float g = random.nextFloat();
                            float h = MathHelper.sin(f) * 1.5f * g;
                            float k = MathHelper.cos(f) * 1.5f * g;
                            world.addParticle(ParticleTypes.ITEM_SLIME, entity.getX() + (double) h, entity.getBodyY(1.2), entity.getZ() + (double) k, 0.0, 0.0, 0.0);
                        }
                    } else if (status == 6) {
                        world.playSound(entity.getX(), entity.getBodyY(1.2), entity.getZ(), SoundEvents.BLOCK_NOTE_BLOCK_PLING.value(), SoundCategory.AMBIENT, 2f, 1f, false);
                        world.addFireworkParticle(entity.getX(), entity.getBodyY(0.5), entity.getZ(), 0, 0, 0, getRocketNbt());
                    } else if (status == 7) {
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
                    } else if (status == 8) {
                        if (entity instanceof PlayerProperties playerProperties) {
                            playerProperties.getFrogTongueState().start(playerEntity.age);
                        }

                        Vec3d vec3d = entity.getRotationVector();
                        Vec3d pos = new Vec3d(entity.getX(), entity.getY() + 1.46875 * (entity.getHeight() / 1.8f), entity.getZ()).add(vec3d.multiply(0.5));

                        world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_FROG_TONGUE, SoundCategory.PLAYERS, 1f, 1f, true);
                        for (int i = 0; i < 5; ++i) {
                            double g = world.getRandom().nextGaussian() * 0.1;
                            double h = world.getRandom().nextGaussian() * 0.1;
                            double j = world.getRandom().nextGaussian() * 0.1;
                            world.addParticle(ParticleRegistry.SPIT, false,
                                    pos.getX() + g, pos.getY() + h, pos.getZ() + j,
                                    vec3d.getX() * 0.6 + g,
                                    vec3d.getY() * 0.6 + h,
                                    vec3d.getZ() * 0.6 + j);
                        }
                    } else if (status == 9) {
                        entity.noClip = true;
                    } else if (status == 10) {
                        world.playSound(entity.getX(),
                                entity.getY(), entity.getZ(),
                                SoundEvents.BLOCK_DECORATED_POT_INSERT, SoundCategory.BLOCKS, 3f,
                                0.6f + random.nextFloat() * 0.6f, false);
                    } else if (status == 11) {
                        Vector3f color = new Vector3f(1f, 1f, 1f);

                        if (entity instanceof AbstractSchematicEntity abstractSchematicEntity) {
                            color = Vec3d.unpackRgb(abstractSchematicEntity.getColor()).toVector3f();
                        }

                        client.particleManager.addParticle(new ColoredFireworksSparkParticle.FireworkParticle(world, color,
                                entity.getX(), entity.getBodyY(0.5), entity.getZ(),
                                0, 0, 0, client.particleManager, getRocketNbt()));
                        client.particleManager.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_FLASH, color),
                                entity.getX(), entity.getBodyY(0.5), entity.getZ(),
                                0, 0, 0);

                        double g = entity.getX();
                        double j = entity.getZ();
                        for (int i = 0; i < 100; ++i) {
                            ParticleEffect particleEffect = new ColoredParticleEffect(ParticleRegistry.COLORED_FIREWORK, color);

                            double h = entity.getRandomBodyY();

                            double d = random.nextGaussian();
                            double e = random.nextGaussian();
                            double f = random.nextGaussian();

                            world.addParticle(particleEffect,
                                    true,
                                    g + d * 0.3, h + e * 0.3, j + f * 0.3,
                                    d * 2.2, e * 2.2, f * 2.2);
                        }
                    }
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(new Identifier(SecretCraftBase.MOD_ID, "healthupdate"), (client, handler, buf, responseSender) -> {
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

        ModelPredicateProviderRegistry.register(ItemRegistry.IRON_HEART_CARVER, new Identifier("using"), (stack, world, entity, seed) ->
                entity instanceof PlayerProperties playerProperties && playerProperties.getHeartRemovalState().isRunning() ? 1.0F : 0.0F
        );

        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.PIG, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
            matrices.translate(0.5, 0, 0.5);

            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));

            Entity entity = EntityNbtHelper.getEntityType(stack).create(MinecraftClient.getInstance().world);
            if (entity != null) {
                entity.readNbt(EntityNbtHelper.getEntityNbt(stack));
                MinecraftClient.getInstance().getEntityRenderDispatcher().render(entity, BlockPos.ORIGIN.getX(), BlockPos.ORIGIN.getY(), BlockPos.ORIGIN.getZ(), 0, 0, matrices, vertexConsumers, light);
            }
        });

        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.SECRET_PIG_POWERED_PORTAL, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
            matrices.translate(0.5, 0, 0.5);

            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));

            Entity entity = EntityRegistry.SECRET_PIG_POWERED_PORTAL.create(MinecraftClient.getInstance().world);
            if (entity != null) {
                MinecraftClient.getInstance().getEntityRenderDispatcher().render(entity, BlockPos.ORIGIN.getX(), BlockPos.ORIGIN.getY(), BlockPos.ORIGIN.getZ(), 0, 0, matrices, vertexConsumers, light);
            }
        });

        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.SECRET_GIANT_JUMPY_SLIME, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
            matrices.translate(0.5, 0, 0.5);
            matrices.scale(0.5f, 0.5f, 0.5f);

            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));

            Entity entity = EntityRegistry.SECRET_GIANT_JUMPY_SLIME.create(MinecraftClient.getInstance().world);
            if (entity != null) {
                MinecraftClient.getInstance().getEntityRenderDispatcher().render(entity, BlockPos.ORIGIN.getX(), BlockPos.ORIGIN.getY(), BlockPos.ORIGIN.getZ(), 0, 0, matrices, vertexConsumers, light);
            }
        });

        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.IRON_GOLEM_FARM, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
            matrices.translate(0.5, 0, 0.5);

            Entity entity = EntityRegistry.IRON_GOLEM_FARM.create(MinecraftClient.getInstance().world);
            if (entity != null) {
                MinecraftClient.getInstance().getEntityRenderDispatcher().render(entity, BlockPos.ORIGIN.getX(), BlockPos.ORIGIN.getY(), BlockPos.ORIGIN.getZ(), 0, 0, matrices, vertexConsumers, light);
            }
        });

        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.TREASURY_ROOM, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
            matrices.translate(0.5, 0, 0.5);

            Entity entity = EntityRegistry.TREASURY_ROOM.create(MinecraftClient.getInstance().world);
            if (entity != null) {
                MinecraftClient.getInstance().getEntityRenderDispatcher().render(entity, BlockPos.ORIGIN.getX(), BlockPos.ORIGIN.getY(), BlockPos.ORIGIN.getZ(), 0, 0, matrices, vertexConsumers, light);
            }
        });

        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.ARMORY_ROOM, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
            matrices.translate(0.5, 0, 0.5);

            Entity entity = EntityRegistry.ARMORY_ROOM.create(MinecraftClient.getInstance().world);
            if (entity != null) {
                MinecraftClient.getInstance().getEntityRenderDispatcher().render(entity, BlockPos.ORIGIN.getX(), BlockPos.ORIGIN.getY(), BlockPos.ORIGIN.getZ(), 0, 0, matrices, vertexConsumers, light);
            }
        });

        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.XP_FARM, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
            matrices.translate(0.5, 0, 0.5);

            Entity entity = EntityRegistry.XP_FARM.create(MinecraftClient.getInstance().world);
            if (entity != null) {

                float tickDelta = MinecraftClient.getInstance().player == null && !MinecraftClient.getInstance().isPaused() ? MinecraftClient.getInstance().getTickDelta() : 0;

                entity.age = MinecraftClient.getInstance().player == null ? 0 : MinecraftClient.getInstance().player.age;

                MinecraftClient.getInstance().getEntityRenderDispatcher().render(entity, BlockPos.ORIGIN.getX(), BlockPos.ORIGIN.getY(), BlockPos.ORIGIN.getZ(), 0, tickDelta, matrices, vertexConsumers, light);
            }
        });
    }

    public NbtCompound getRocketNbt() {
        NbtCompound nbtCompound = new NbtCompound();

        nbtCompound.putByte("Flight", (byte) 1);
        NbtCompound explosion = new NbtCompound();
        List<Integer> list = Lists.newArrayList();

        explosion.putBoolean("Flicker", true);
        explosion.putBoolean("Trail", true);
        list.add(0x0078ff);

        explosion.putIntArray("Colors", list);
        explosion.putIntArray("FadeColors", list);
        explosion.putByte("Type", (byte) 1);
        NbtList nbtList = new NbtList();
        nbtList.add(explosion);

        nbtCompound.put("Explosions", nbtList);

        return nbtCompound;
    }

    public static void useFrog(PlayerEntity player) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (player.equals(client.player)) {
            Vec3d vec3d = player.getCameraPosVec(client.getTickDelta());

            double prevDistance = client.crosshairTarget.getPos().squaredDistanceTo(vec3d);
            double d = client.crosshairTarget.getType().equals(HitResult.Type.MISS)
                    ? client.interactionManager.getReachDistance() * 5 : Math.min(client.interactionManager.getReachDistance() * 2, prevDistance);
            HitResult hitResult = player.raycast(d, client.getTickDelta(), false);

            PacketByteBuf buf = PacketByteBufs.create();
            if (hitResult.getType().equals(HitResult.Type.MISS)) {
                buf.writeInt(2);
            } else if (hitResult instanceof BlockHitResult blockHitResult) {
                buf.writeInt(0);
                buf.writeBlockPos(blockHitResult.getBlockPos());
            } else if (hitResult instanceof EntityHitResult entityHitResult) {
                Entity target = entityHitResult.getEntity();
                if (target instanceof EnderDragonPart dragonPart) {
                    target = dragonPart.owner;
                }

                buf.writeInt(1);
                buf.writeInt(target.getId());
            }

            ClientPlayNetworking.send(new Identifier(SecretCraftBase.MOD_ID, "frog"), buf);
        }
    }

    public static int getColor(float time) {
        float r = (float) (((time + (!MinecraftClient.getInstance().isPaused() ? MinecraftClient.getInstance().getTickDelta() : 0)) / 4.0f) % Math.PI);
        float s = (MathHelper.sin(r + 0.0f) + 1.0f) * 0.5f;
        float t = 1f;
        float u = (MathHelper.sin(r + 4.1887903f) + 1.0f) * 0.1f;
        return BionicUtilsClient.getRainbow().getIntFromColor((int) (s * 255), (int) (t * 255), (int) (u * 255));
    }
}
