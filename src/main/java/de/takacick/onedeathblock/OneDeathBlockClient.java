package de.takacick.onedeathblock;

import de.takacick.onedeathblock.access.PlayerProperties;
import de.takacick.onedeathblock.client.DeathCounter;
import de.takacick.onedeathblock.client.DeathToast;
import de.takacick.onedeathblock.client.renderer.HeadSpawnerRenderer;
import de.takacick.onedeathblock.client.renderer.SpikyIronArmorSuitRenderer;
import de.takacick.onedeathblock.mixin.IngameHudAccessor;
import de.takacick.onedeathblock.registry.EntityRegistry;
import de.takacick.onedeathblock.registry.ItemRegistry;
import de.takacick.onedeathblock.registry.ParticleRegistry;
import de.takacick.onedeathblock.registry.block.entity.renderer.SpikedBedBlockEntityRenderer;
import de.takacick.onedeathblock.registry.entity.living.renderer.SkullagerEntityRenderer;
import de.takacick.onedeathblock.registry.entity.living.renderer.SuperbrineEntityRenderer;
import de.takacick.onedeathblock.registry.entity.projectiles.renderer.BuildMeteorEntityRenderer;
import de.takacick.onedeathblock.registry.entity.projectiles.renderer.CustomBlockEntityRenderer;
import de.takacick.onedeathblock.registry.particles.SmokeParticle;
import de.takacick.onedeathblock.registry.particles.goop.GoopDropParticle;
import de.takacick.onedeathblock.registry.particles.goop.GoopParticle;
import de.takacick.onedeathblock.registry.particles.goop.GoopStringParticle;
import de.takacick.onedeathblock.server.BuildMeteorExplosionHandler;
import de.takacick.onedeathblock.server.SuperbrineExplosionHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class OneDeathBlockClient implements ClientModInitializer {

    private KeyBinding explosivePlacing;
    private boolean explosivePlacingBoolean;
    private KeyBinding superbrineSummon;
    private boolean superbrineSummonBoolean;

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityRegistry.FALLING_BLOCK, CustomBlockEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.SKULLAGER, SkullagerEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.SUPERBRINE, SuperbrineEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.BUILD_METEOR, BuildMeteorEntityRenderer::new);
        BlockEntityRendererRegistry.register(EntityRegistry.SPIKED_BED, SpikedBedBlockEntityRenderer::new);

        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP, GoopParticle.GoopParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP_DROP, GoopDropParticle.GoopDropParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP_STRING, GoopStringParticle.GoopStringParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.SMOKE, SmokeParticle.Factory::new);

        BlockRenderLayerMap.INSTANCE.putBlock(ItemRegistry.DEATH_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ItemRegistry.ELECTRICUTIONER_DOOR, RenderLayer.getCutoutMipped());

        try {
            explosivePlacing = KeyBindingHelper.registerKeyBinding(
                    new KeyBinding("Explosing Placing",
                            InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X,
                            "OneDeathBlock Abilities")
            );
            superbrineSummon = KeyBindingHelper.registerKeyBinding(
                    new KeyBinding("Superbrine Summon",
                            InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X,
                            "OneDeathBlock Abilities")
            );
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                PlayerEntity playerEntity = client.player;
                if (playerEntity == null) {
                    return;
                }

                if (explosivePlacing.isPressed() && !explosivePlacingBoolean) {
                    explosivePlacingBoolean = explosivePlacing.isPressed();

                    PacketByteBuf buf = PacketByteBufs.create();
                    ClientPlayNetworking.send(new Identifier(OneDeathBlock.MOD_ID, "explosiveplacing"), buf);
                } else {
                    explosivePlacingBoolean = explosivePlacing.isPressed();
                }

                if (superbrineSummon.isPressed() && !superbrineSummonBoolean) {
                    superbrineSummonBoolean = superbrineSummon.isPressed();

                    if (playerEntity.hasPermissionLevel(2)) {
                        PacketByteBuf buf = PacketByteBufs.create();
                        ClientPlayNetworking.send(new Identifier(OneDeathBlock.MOD_ID, "superbrinesummon"), buf);
                    }
                } else {
                    superbrineSummonBoolean = superbrineSummon.isPressed();
                }
            });
        } catch (RuntimeException ignored) {

        }

        ClientPlayNetworking.registerGlobalReceiver(OneDeathBlock.IDENTIFIER, (client, handler, buf, responseSender) -> {
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
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS, 0.05f, 1f, false);
                        client.particleManager.addEmitter(entity, ParticleTypes.TOTEM_OF_UNDYING, 0);
                        if (entity instanceof PlayerProperties playerProperties) {
                            playerProperties.setDeathTicks(0);
                        }
                    } else if (status == 2) {
                        if (entity instanceof PlayerProperties playerProperties) {
                            playerProperties.getHeartRemovalState().startIfNotRunning(entity.age);
                        }
                    } else if (status == 3) {
                        entity.animateDamage();
                    } else if (status == 4) {
                        if (entity == playerEntity) {
                            world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS, 0.05f, 1f, false);
                            client.gameRenderer.showFloatingItem(ItemRegistry.EXPLOSIVE_PLACING.getDefaultStack());
                        }
                    } else if (status == 5) {
                        double g = entity.getX();
                        double j = entity.getZ();

                        for (int i = 0; i < 25; ++i) {
                            double h = entity.getBodyY(0.45 + random.nextDouble() * 0.5);

                            double d = random.nextGaussian();
                            double e = random.nextGaussian();
                            double f = random.nextGaussian();

                            world.addParticle(ParticleTypes.SMOKE, true,
                                    g + d * 0.2, h + e * 0.2, j + f * 0.2,
                                    d * 0.01, e * 0.1, f * 0.01);
                        }
                    }
                }
            });
        });

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            PlayerEntity playerEntity = client.player;
            if (playerEntity instanceof PlayerProperties playerProperties) {
                playerProperties.getDeathToasts().forEach(DeathToast::tick);
                playerProperties.getDeathToasts().removeIf(DeathToast::shouldRemove);
            }
        });

        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            Entity cameraEntity = client.cameraEntity;
            if (cameraEntity instanceof PlayerProperties playerProperties && !client.options.hudHidden && !client.options.debugEnabled) {
                DeathCounter.render(client, playerProperties, matrixStack, tickDelta);
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(new Identifier(OneDeathBlock.MOD_ID, "adddeaths"), (client, handler, buf, responseSender) -> {
            try {
                int deaths = buf.readInt();
                PlayerEntity playerEntity = client.player;
                if (playerEntity instanceof PlayerProperties playerProperties) {
                    client.execute(() -> {
                        List<DeathToast> deathToasts = playerProperties.getDeathToasts();

                        DeathToast prevDeathToast = deathToasts.isEmpty() ? null : deathToasts.get(deathToasts.size() - 1);
                        int y = prevDeathToast == null ? 0 : Math.min(prevDeathToast.getY() - 16, 0);

                        if (prevDeathToast != null && (deathToasts.size() >= 35 ||
                                (((prevDeathToast.getDeaths() < 0 && deaths < 0) || (prevDeathToast.getDeaths() >= 0 && deaths >= 0)) && prevDeathToast.getTick() <= 20))) {
                            prevDeathToast.setDeaths(prevDeathToast.getDeaths() + deaths);
                        } else {
                            playerProperties.getDeathToasts().add(new DeathToast(deaths, 0, y));
                        }
                    });
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(new Identifier(OneDeathBlock.MOD_ID, "healthupdate"), (client, handler, buf, responseSender) -> {
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

        ClientPlayNetworking.registerGlobalReceiver(SuperbrineExplosionHandler.IDENTIFIER, (client, handler, buf, responseSender) -> {
            try {
                SuperbrineExplosionHandler packet = new SuperbrineExplosionHandler(buf);
                client.execute(() -> {
                    SuperbrineExplosionHandler.Explosion explosion = new SuperbrineExplosionHandler.Explosion(client.world, null, packet.getX(), packet.getY(), packet.getZ(), packet.getRadius(), packet.getAffectedBlocks());
                    explosion.affectWorld(true);
                    client.player.setVelocity(client.player.getVelocity().add(packet.getPlayerVelocityX(), packet.getPlayerVelocityY(), packet.getPlayerVelocityZ()));
                });
            } catch (Exception exception) {

            }
        });

        ClientPlayNetworking.registerGlobalReceiver(BuildMeteorExplosionHandler.IDENTIFIER, (client, handler, buf, responseSender) -> {
            try {
                BuildMeteorExplosionHandler packet = new BuildMeteorExplosionHandler(buf);
                client.execute(() -> {
                    BuildMeteorExplosionHandler.Explosion explosion = new BuildMeteorExplosionHandler.Explosion(client.world, null, packet.getX(), packet.getY(), packet.getZ(), packet.getRadius(), packet.getAffectedBlocks());
                    explosion.affectWorld(true);
                    client.player.setVelocity(client.player.getVelocity().add(packet.getPlayerVelocityX(), packet.getPlayerVelocityY(), packet.getPlayerVelocityZ()));
                });
            } catch (Exception exception) {

            }
        });

        ModelPredicateProviderRegistry.register(ItemRegistry.HEART_CARVER, new Identifier("using"), (stack, world, entity, seed) ->
                entity instanceof PlayerProperties playerProperties && playerProperties.getHeartRemovalState().isRunning() ? 1.0F : 0.0F
        );

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            if (world == null || pos == null) {
                return GrassColors.getColor(0.5, 1.0);
            }
            return BiomeColors.getGrassColor(world, pos);
        }, ItemRegistry.DEATH_BLOCK);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            BlockState blockState = ((BlockItem) stack.getItem()).getBlock().getDefaultState();
            return ColorProviderRegistry.BLOCK.get(blockState.getBlock()).getColor(blockState, null, null, tintIndex);
        }, ItemRegistry.DEATH_BLOCK_ITEM);

        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.SPIKY_IRON_ARMOR_SUIT, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
            matrices.translate(0.5, 1.5, 0.5);
            matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));

            SpikyIronArmorSuitRenderer.MODEL_PART.resetTransform();
            SpikyIronArmorSuitRenderer.MODEL_PART.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(SpikyIronArmorSuitRenderer.TEXTURE)), light, OverlayTexture.DEFAULT_UV);
        });

        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.HEAD_SPAWNER, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
            matrices.translate(0.5, 0, 0.5);
            matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));

            HeadSpawnerRenderer.renderAsItem(matrices, vertexConsumers, light, MinecraftClient.getInstance().player, MinecraftClient.getInstance().getTickDelta());
        });
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
}
