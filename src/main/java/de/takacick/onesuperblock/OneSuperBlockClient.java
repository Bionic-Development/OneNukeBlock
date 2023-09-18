package de.takacick.onesuperblock;

import de.takacick.onesuperblock.client.render.SuperRenderLayers;
import de.takacick.onesuperblock.registry.EntityRegistry;
import de.takacick.onesuperblock.registry.ItemRegistry;
import de.takacick.onesuperblock.registry.ParticleRegistry;
import de.takacick.onesuperblock.registry.block.entity.renderer.SuperBlockEntityRenderer;
import de.takacick.onesuperblock.registry.entity.living.renderer.SuperFiedWardenEntityRenderer;
import de.takacick.onesuperblock.registry.entity.living.renderer.SuperProtoEntityRenderer;
import de.takacick.onesuperblock.registry.entity.living.renderer.SuperVillagerEntityRenderer;
import de.takacick.onesuperblock.registry.entity.living.renderer.SuperWitherEntityRenderer;
import de.takacick.onesuperblock.registry.entity.projectiles.renderer.CustomBlockEntityRenderer;
import de.takacick.onesuperblock.registry.entity.projectiles.renderer.SuperWitherSkullEntityRenderer;
import de.takacick.onesuperblock.registry.particles.*;
import de.takacick.superitems.registry.particles.RainbowParticleEffect;
import de.takacick.superitems.registry.particles.ShockwaveParticleEffect;
import de.takacick.utils.BionicUtilsClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class OneSuperBlockClient implements ClientModInitializer {

    private int challengeCompleteTicks = 0;
    private int maxChallengeCompleteTicks = 100;

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityRegistry.FALLING_BLOCK, CustomBlockEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.SUPER_VILLAGER, SuperVillagerEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.SUPER_PROTO, SuperProtoEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.SUPER_BRIDGE_EGG, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.SUPER_ENDER_PEARL, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.SUPER_FIED_WARDEN, SuperFiedWardenEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.SUPER_WITHER, SuperWitherEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.SUPER_WITHER_SKULL, SuperWitherSkullEntityRenderer::new);
        BlockEntityRendererRegistry.register(EntityRegistry.SUPER_BLOCK, SuperBlockEntityRenderer::new);

        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.RAINBOW_DUST, RainbowDustParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.RAINBOW_BLOCK, RainbowBlockParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.RAINBOW_SONIC_BOOM, RainbowSonicBoomParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.RAINBOW_GLYPH, RainbowGlyphParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.RAINBOW_ITEM, RainbowItemParticle.Factory::new);

        BlockRenderLayerMap.INSTANCE.putBlock(ItemRegistry.SUPER_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ItemRegistry.SUPER_ORE, SuperRenderLayers.getOreOverlay());
        BlockRenderLayerMap.INSTANCE.putBlock(ItemRegistry.DEEPSLATE_SUPER_ORE, SuperRenderLayers.getOreOverlay());
        BlockRenderLayerMap.INSTANCE.putBlock(ItemRegistry.SUPER_WOOL, SuperRenderLayers.getOreOverlay());

        ClientPlayNetworking.registerGlobalReceiver(OneSuperBlock.IDENTIFIER, (client, handler, buf, responseSender) -> {
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

                        for (int i = 0; i < 5; ++i) {
                            double h = entity.getBodyY(0.45 + random.nextDouble() * 0.5);

                            double d = random.nextGaussian();
                            double e = random.nextGaussian();
                            double f = random.nextGaussian();

                            world.addParticle(new RainbowParticleEffect(de.takacick.superitems.registry.ParticleRegistry.RAINBOW_CONFETTI, world.getRandom().nextInt(24000), true), true,
                                    g + d * 0.2, h + e * 0.2, j + f * 0.2,
                                    d * 0.15, e * 0.15, f * 0.15);
                        }

                        for (int i = 0; i < 5; ++i) {
                            double h = entity.getBodyY(0.45 + random.nextDouble() * 0.5);

                            double d = random.nextGaussian();
                            double e = random.nextGaussian();
                            double f = random.nextGaussian();

                            world.addParticle(new RainbowParticleEffect(de.takacick.superitems.registry.ParticleRegistry.RAINBOW_SPARK, world.getRandom().nextInt(24000), false), true,
                                    g + d * 0.2, h + e * 0.2, j + f * 0.2,
                                    d * 0.15, e * 0.15, f * 0.15);
                        }
                        for (int i = 0; i < 15; ++i) {
                            double h = entity.getBodyY(0.45 + random.nextDouble() * 0.5);

                            double d = random.nextGaussian();
                            double e = random.nextGaussian();
                            double f = random.nextGaussian();

                            world.addParticle(new RainbowParticleEffect(de.takacick.superitems.registry.ParticleRegistry.RAINBOW_SMOKE, world.getRandom().nextInt(24000), false), true,
                                    g + d * 0.2, h + e * 0.2, j + f * 0.2,
                                    d * 0.05, e * 0.05, f * 0.05);
                        }
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.5f, 1.0f + world.getRandom().nextFloat() * 0.2f, true);
                    } else if (status == 2) {
                        double g = entity.getX();
                        double j = entity.getZ();

                        for (int i = 0; i < 5; ++i) {
                            double h = entity.getBodyY(0.45 + random.nextDouble() * 0.5);

                            double d = random.nextGaussian();
                            double e = random.nextGaussian();
                            double f = random.nextGaussian();

                            world.addParticle(new RainbowParticleEffect(de.takacick.superitems.registry.ParticleRegistry.RAINBOW_CONFETTI, world.getRandom().nextInt(24000), true), true,
                                    g + d * 0.2, h + e * 0.2, j + f * 0.2,
                                    d * 0.15, e * 0.15, f * 0.15);
                        }

                        for (int i = 0; i < 5; ++i) {
                            double h = entity.getBodyY(0.45 + random.nextDouble() * 0.5);

                            double d = random.nextGaussian();
                            double e = random.nextGaussian();
                            double f = random.nextGaussian();

                            world.addParticle(new RainbowParticleEffect(de.takacick.superitems.registry.ParticleRegistry.RAINBOW_SPARK, world.getRandom().nextInt(24000), false), true,
                                    g + d * 0.2, h + e * 0.2, j + f * 0.2,
                                    d * 0.15, e * 0.15, f * 0.15);
                        }
                        double h = entity.getBodyY(0.45 + random.nextDouble() * 0.5);
                        world.addParticle(new RainbowParticleEffect(de.takacick.superitems.registry.ParticleRegistry.RAINBOW_EXPLOSION,
                                        world.getRandom().nextInt(24000), false), true,
                                g, h, j,
                                0, 0, 0);

                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_FOX_BITE, SoundCategory.PLAYERS, 1.5f, 0.7f, true);
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1.5f, 2.5f, true);
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.5f, 1.0f + world.getRandom().nextFloat() * 0.2f, true);
                    } else if (status == 3) {
                        double g = entity.getX();
                        double j = entity.getZ();

                        for (int i = 0; i < 5; ++i) {
                            double h = entity.getBodyY(0.45 + random.nextDouble() * 0.5);

                            double d = random.nextGaussian();
                            double e = random.nextGaussian();
                            double f = random.nextGaussian();

                            world.addParticle(new RainbowParticleEffect(de.takacick.superitems.registry.ParticleRegistry.RAINBOW_CONFETTI, world.getRandom().nextInt(24000), true), true,
                                    g + d * 0.2, h + e * 0.2, j + f * 0.2,
                                    d * 0.15, e * 0.15, f * 0.15);
                        }

                        for (int i = 0; i < 5; ++i) {
                            double h = entity.getBodyY(0.45 + random.nextDouble() * 0.5);

                            double d = random.nextGaussian();
                            double e = random.nextGaussian();
                            double f = random.nextGaussian();

                            world.addParticle(new RainbowParticleEffect(de.takacick.superitems.registry.ParticleRegistry.RAINBOW_SPARK, world.getRandom().nextInt(24000), false), true,
                                    g + d * 0.2, h + e * 0.2, j + f * 0.2,
                                    d * 0.15, e * 0.15, f * 0.15);
                        }

                        for (int i = 0; i < 10; ++i) {
                            double h = entity.getBodyY(0.45 + random.nextDouble() * 0.5);

                            double d = random.nextGaussian();
                            double e = random.nextGaussian();
                            double f = random.nextGaussian();

                            world.addParticle(new RainbowParticleEffect(de.takacick.superitems.registry.ParticleRegistry.RAINBOW_SMOKE, world.getRandom().nextInt(24000), false), true,
                                    g + d * 0.2, h + e * 0.2, j + f * 0.2,
                                    d * 0.15, e * 0.15, f * 0.15);
                        }
                        double h = entity.getBodyY(0.45 + random.nextDouble() * 0.5);
                        world.addParticle(new RainbowParticleEffect(de.takacick.superitems.registry.ParticleRegistry.RAINBOW_EXPLOSION,
                                        world.getRandom().nextInt(24000), false), true,
                                g, h, j,
                                0, 0, 0);

                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f, false);
                    } else if (status == 4) {
                        double g = entity.getX();
                        double j = entity.getZ();

                        for (int i = 0; i < 5; ++i) {
                            double h = entity.getBodyY(0.45 + random.nextDouble() * 0.5);

                            double d = random.nextGaussian();
                            double e = random.nextGaussian();
                            double f = random.nextGaussian();

                            world.addParticle(new RainbowParticleEffect(de.takacick.superitems.registry.ParticleRegistry.RAINBOW_CONFETTI, world.getRandom().nextInt(24000), true), true,
                                    g + d * 0.2, h + e * 0.2, j + f * 0.2,
                                    d * 0.15, e * 0.15, f * 0.15);
                        }

                        for (int i = 0; i < 5; ++i) {
                            double h = entity.getBodyY(0.45 + random.nextDouble() * 0.5);

                            double d = random.nextGaussian();
                            double e = random.nextGaussian();
                            double f = random.nextGaussian();

                            world.addParticle(new RainbowParticleEffect(de.takacick.superitems.registry.ParticleRegistry.RAINBOW_SPARK, world.getRandom().nextInt(24000), false), true,
                                    g + d * 0.2, h + e * 0.2, j + f * 0.2,
                                    d * 0.15, e * 0.15, f * 0.15);
                        }

                        for (int i = 0; i < 10; ++i) {
                            double h = entity.getBodyY(0.45 + random.nextDouble() * 0.5);

                            double d = random.nextGaussian();
                            double e = random.nextGaussian();
                            double f = random.nextGaussian();

                            world.addParticle(new RainbowParticleEffect(de.takacick.superitems.registry.ParticleRegistry.RAINBOW_SMOKE, world.getRandom().nextInt(24000), false), true,
                                    g + d * 0.2, h + e * 0.2, j + f * 0.2,
                                    d * 0.15, e * 0.15, f * 0.15);
                        }
                        double h = entity.getBodyY(0.45 + random.nextDouble() * 0.5);
                        world.addParticle(new ShockwaveParticleEffect(de.takacick.superitems.registry.ParticleRegistry.RAINBOW_SHOCKWAVE, 6, 0.3f, 0.1f), true,
                                g, h, j,
                                0, 90, 0);

                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1f, 1.0f, false);
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.5f, 1.0f + world.getRandom().nextFloat() * 0.2f, true);
                    } else if (status == 5) {
                        double g = entity.getX();
                        double j = entity.getZ();

                        for (int i = 0; i < 15; ++i) {
                            double h = entity.getBodyY(0.45 + random.nextDouble() * 0.5);

                            double d = random.nextGaussian();
                            double e = random.nextGaussian();
                            double f = random.nextGaussian();

                            world.addParticle(new RainbowParticleEffect(de.takacick.superitems.registry.ParticleRegistry.RAINBOW_CONFETTI, world.getRandom().nextInt(24000), true), true,
                                    g + d * 0.2, h + e * 0.2, j + f * 0.2,
                                    d * 0.15, e * 0.15, f * 0.15);
                        }

                        for (int i = 0; i < 15; ++i) {
                            double h = entity.getBodyY(0.45 + random.nextDouble() * 0.5);

                            double d = random.nextGaussian();
                            double e = random.nextGaussian();
                            double f = random.nextGaussian();

                            world.addParticle(new RainbowParticleEffect(de.takacick.superitems.registry.ParticleRegistry.RAINBOW_SPARK, world.getRandom().nextInt(24000), false), true,
                                    g + d * 0.2, h + e * 0.2, j + f * 0.2,
                                    d * 0.15, e * 0.15, f * 0.15);
                        }

                        for (int i = 0; i < 15; ++i) {
                            double h = entity.getBodyY(0.45 + random.nextDouble() * 0.5);

                            double d = random.nextGaussian();
                            double e = random.nextGaussian();
                            double f = random.nextGaussian();

                            world.addParticle(new RainbowParticleEffect(de.takacick.superitems.registry.ParticleRegistry.RAINBOW_SMOKE, world.getRandom().nextInt(24000), false), true,
                                    g + d * 0.2, h + e * 0.2, j + f * 0.2,
                                    d * 0.15, e * 0.15, f * 0.15);
                        }
                        double h = entity.getBodyY(0.45 + random.nextDouble() * 0.5);
                        world.addParticle(new ShockwaveParticleEffect(de.takacick.superitems.registry.ParticleRegistry.RAINBOW_SHOCKWAVE, 6, 0.3f, 0.1f), true,
                                g, h, j,
                                0, 90, 0);

                        this.challengeCompleteTicks = this.maxChallengeCompleteTicks;

                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.5f, 1.0f + world.getRandom().nextFloat() * 0.2f, false);
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1f, 1.0f, false);
                    } else if (status == 6) {
                        double g = entity.getX();
                        double j = entity.getZ();

                        for (int i = 0; i < 15; ++i) {
                            double h = entity.getBodyY(0.45 + random.nextDouble() * 0.5);

                            double d = random.nextGaussian();
                            double e = random.nextGaussian();
                            double f = random.nextGaussian();

                            world.addParticle(new RainbowParticleEffect(ParticleRegistry.RAINBOW_GLYPH, world.getRandom().nextInt(24000), true), true,
                                    g + d * 0.2, h + e * 0.2, j + f * 0.2,
                                    d * 0.15, e * 0.15, f * 0.15);
                        }

                        for (int i = 0; i < 5; ++i) {
                            double h = entity.getBodyY(0.45 + random.nextDouble() * 0.5);

                            double d = random.nextGaussian();
                            double e = random.nextGaussian();
                            double f = random.nextGaussian();

                            world.addParticle(new RainbowParticleEffect(de.takacick.superitems.registry.ParticleRegistry.RAINBOW_SPARK, world.getRandom().nextInt(24000), false), true,
                                    g + d * 0.2, h + e * 0.2, j + f * 0.2,
                                    d * 0.15, e * 0.15, f * 0.15);
                        }
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.5f, 1.0f + world.getRandom().nextFloat() * 0.2f, true);
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 1f, 1.0f, true);
                    }
                }
            });
        });

        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.SUPER_PROTO, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
            matrices.translate(0.5, 0, 0.5);
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180));

            EntityType<?> entityType = EntityRegistry.SUPER_PROTO;
            Entity renderEntity = entityType.create(MinecraftClient.getInstance().world);
            if (renderEntity != null) {
                MinecraftClient.getInstance().getEntityRenderDispatcher().render(renderEntity,
                        BlockPos.ORIGIN.getX(), BlockPos.ORIGIN.getY(), BlockPos.ORIGIN.getZ(),
                        0, 0, matrices, vertexConsumers, light);
            }
        });

        ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).register((resourceManager, sprites) -> {
            sprites.register(new Identifier(OneSuperBlock.MOD_ID, "entity/super_wither"));
        });

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (this.challengeCompleteTicks > 0) {
                this.challengeCompleteTicks--;
                client.inGameHud.setTitleTicks(0, 3, 1);
                if (this.challengeCompleteTicks <= 0) {
                    client.inGameHud.setTitleTicks(0, 1, 1);
                }
                MutableText mutableText = Text.empty();

                String text = "Challenge Compelte!";

                int index = text.length();

                for (char c : text.toCharArray()) {
                    index--;
                    int animation = (index * 15) + this.challengeCompleteTicks;
                    mutableText = mutableText.append(Text.literal(c + "").setStyle(Style.EMPTY.withColor(BionicUtilsClient.getRainbow().getColorAsInt(animation))));
                }

                client.inGameHud.setTitle(mutableText);
                client.inGameHud.setSubtitle(Text.of(""));
            }
        });
    }
}
