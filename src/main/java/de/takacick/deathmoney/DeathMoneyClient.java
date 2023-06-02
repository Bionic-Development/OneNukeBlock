package de.takacick.deathmoney;

import de.takacick.deathmoney.access.PlayerProperties;
import de.takacick.deathmoney.mixin.IngameHudAccessor;
import de.takacick.deathmoney.registry.EntityRegistry;
import de.takacick.deathmoney.registry.ItemRegistry;
import de.takacick.deathmoney.registry.ParticleRegistry;
import de.takacick.deathmoney.registry.block.entity.renderer.BlackMatterBlockEntityRenderer;
import de.takacick.deathmoney.registry.entity.custom.BlackHoleEntity;
import de.takacick.deathmoney.registry.entity.custom.renderer.*;
import de.takacick.deathmoney.registry.entity.living.renderer.CrazyExGirlfriendEntityRenderer;
import de.takacick.deathmoney.registry.entity.living.renderer.HungryTitanEntityRenderer;
import de.takacick.deathmoney.registry.entity.living.renderer.LittleWitherBullyEntityRenderer;
import de.takacick.deathmoney.registry.entity.projectiles.renderer.*;
import de.takacick.deathmoney.registry.particles.*;
import de.takacick.deathmoney.registry.particles.goop.GoopDropParticle;
import de.takacick.deathmoney.registry.particles.goop.GoopDropParticleEffect;
import de.takacick.deathmoney.registry.particles.goop.GoopParticle;
import de.takacick.deathmoney.registry.particles.goop.GoopStringParticle;
import de.takacick.deathmoney.utils.DeathCounter;
import de.takacick.deathmoney.utils.DeathToast;
import de.takacick.deathmoney.utils.MeteorExplosionHandler;
import de.takacick.deathmoney.utils.TntNukeExplosionHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.block.Blocks;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EmptyEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.List;

public class DeathMoneyClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityRegistry.DEATH_SHOP_PORTAL, DeathShopPortalEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.SHOP_ITEM, ShopItemEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.METEOR, MeteorEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.LITTLE_WITHER_BULLY, LittleWitherBullyEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.LITTLE_WITHER_SKULL, LittleWitherSkullEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.CRAZY_EX_GIRLFRIEND, CrazyExGirlfriendEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.DEATH_MINER, EmptyEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.DANGEROUS_BLOCK, DangerousBlockEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.BLOCK_BREAK, EmptyEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.EARTH_FANGS, EarthFangsEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.TNT_NUKE, TntNukeEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.TNT_NUKE_EXPLOSION, EmptyEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.FALLING_BLOCK, CustomBlockEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.BLACK_HOLE, BlackHoleEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.BLACK_MATTER_SHOCKWAVE, EmptyEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.HUNGRY_TITAN, HungryTitanEntityRenderer::new);
        BlockEntityRendererRegistry.register(EntityRegistry.BLACK_MATTER, BlackMatterBlockEntityRenderer::new);

        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP, GoopParticle.GoopParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP_DROP, GoopDropParticle.GoopDropParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP_STRING, GoopStringParticle.GoopStringParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.SMOKE, SmokeParticle.Factory::new);

        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.HEART, HeartParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.COLORED_GLOW_SPARK, ColoredGlowSparkParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.COLORED_FLASH, ColoredFlashParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.COLORED_POOF, ColoredPoofParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.DEATH_PORTAL, DeathPortalParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.DEATH_SOUL, DeathSoulParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.BLACK_MATTER_TOTEM, BlackMatterTotemParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.BLACK_MATTER_PORTAL, BlackMatterPortalParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.BLACK_MATTER_EXPLOSION, BlackMatterExplosionParticle.Factory::new);

        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), ItemRegistry.BLOOD_STILL, ItemRegistry.BLOOD_FLOW);

        ClientPlayNetworking.registerGlobalReceiver(DeathMoney.IDENTIFIER, (client, handler, buf, responseSender) -> {
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
                        Random random = Random.create();

                        ParticleEffect particleEffect = new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.SWEET_BERRY_BUSH.getDefaultState().with(SweetBerryBushBlock.AGE, 3));
                        for (int i = 0; i < 8; ++i) {
                            double g = entity.getX();
                            double h = entity.getBodyY(0.45 + random.nextDouble() * 0.55);
                            double j = entity.getZ();

                            double d = random.nextGaussian() * 0.1;
                            double e = random.nextGaussian() * 0.1;
                            double f = random.nextGaussian() * 0.1;

                            world.addParticle(particleEffect, true, g + d, h + e, j + f, d, e, f);
                        }
                    } else if (status >= 4 && status <= 6) {

                        Random random = Random.create();

                        ParticleEffect particleEffect = switch (status) {
                            default ->
                                    new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.SWEET_BERRY_BUSH.getDefaultState().with(SweetBerryBushBlock.AGE, 3));
                            case 6 ->
                                    new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.CACTUS.getDefaultState());
                        };

                        if (status == 4) {
                            world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, SoundCategory.PLAYERS, 0.8f, 1f, false);
                        } else if (status == 5) {
                            world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_PLAYER_HURT_ON_FIRE, SoundCategory.PLAYERS, 0.8f, 1f, false);
                            for (int i = 0; i < 5; ++i) {
                                double g = entity.getX();
                                double h = entity.getBodyY(0.45 + random.nextDouble() * 0.55);
                                double j = entity.getZ();

                                double d = random.nextGaussian() * 0.1;
                                double e = random.nextGaussian() * 0.1;
                                double f = random.nextGaussian() * 0.1;

                                world.addParticle(ParticleTypes.FLAME, true, g + d, h + e, j + f, d, e, f);
                                world.addParticle(ParticleTypes.SMOKE, true, g + d, h + e, j + f, d, e, f);
                            }

                            for (int i = 0; i < 7; ++i) {
                                double g = entity.getX();
                                double h = entity.getBodyY(0.45 + random.nextDouble() * 0.55);
                                double j = entity.getZ();

                                double d = random.nextGaussian() * 0.1;
                                double e = random.nextGaussian() * 0.1;
                                double f = random.nextGaussian() * 0.1;

                                world.addParticle(ParticleTypes.LAVA, true, g + d, h + e, j + f, d, e, f);
                            }
                            return;
                        } else {
                            world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.BLOCK_WOOL_PLACE, SoundCategory.PLAYERS, 0.8f, 1f, false);
                            world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_BEE_STING, SoundCategory.PLAYERS, 0.8f, 1f, false);

                            for (int i = 0; i < 4; ++i) {
                                double g = entity.getX();
                                double h = entity.getBodyY(0.45 + random.nextDouble() * 0.55);
                                double j = entity.getZ();

                                double d = random.nextGaussian() * 0.1;
                                double e = random.nextGaussian() * 0.1;
                                double f = random.nextGaussian() * 0.1;

                                world.addParticle(new GoopDropParticleEffect(new Vec3f(Vec3d.unpackRgb(0x820A0A)), (float) (world.getRandom().nextDouble() * 0.25)),
                                        true, g + d, h + e, j + f, d, e, f);
                            }
                        }

                        for (int i = 0; i < 8; ++i) {
                            double g = entity.getX();
                            double h = entity.getBodyY(0.45 + random.nextDouble() * 0.55);
                            double j = entity.getZ();

                            double d = random.nextGaussian() * 0.1;
                            double e = random.nextGaussian() * 0.1;
                            double f = random.nextGaussian() * 0.1;

                            world.addParticle(particleEffect, true, g + d, h + e, j + f, d, e, f);
                        }
                    } else if (status == 7) {
                        Random random = Random.create();

                        for (int i = 0; i < 5; ++i) {
                            double g = entity.getX();
                            double h = entity.getBodyY(0.45 + random.nextDouble() * 0.55);
                            double j = entity.getZ();

                            double d = random.nextGaussian() * 0.1;
                            double e = random.nextGaussian() * 0.1;
                            double f = random.nextGaussian() * 0.1;

                            world.addParticle(ParticleTypes.FLAME, true, g + d, h + e, j + f, d, e, f);
                            world.addParticle(ParticleTypes.SMOKE, true, g + d, h + e, j + f, d, e, f);
                        }

                        for (int i = 0; i < 7; ++i) {
                            double g = entity.getX();
                            double h = entity.getBodyY(0.45 + random.nextDouble() * 0.55);
                            double j = entity.getZ();

                            double d = random.nextGaussian() * 0.1;
                            double e = random.nextGaussian() * 0.1;
                            double f = random.nextGaussian() * 0.1;

                            world.addParticle(ParticleTypes.LAVA, true, g + d, h + e, j + f, d, e, f);
                        }
                    } else if (status == 8) {
                        ((PlayerProperties) entity).setMeteorShakeTicks(25);
                    } else if (status == 9) {
                        double g = entity.getX();
                        double j = entity.getZ();
                        world.playSound(g, entity.getBodyY(0.5), j, SoundEvents.BLOCK_AMETHYST_CLUSTER_HIT, entity.getSoundCategory(), 0.5f, 1f, true);
                        for (int i = 0; i < 10; ++i) {
                            double h = entity.getRandomBodyY();
                            world.addParticle(ParticleRegistry.HEART, true, g, h, j, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3);
                        }

                        for (int i = 0; i < 8; ++i) {
                            double h = entity.getRandomBodyY();
                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_GLOW_SPARK, new Vec3f(Vec3d.unpackRgb(0xFF1313))), true, g, h, j, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3);
                        }

                        for (int i = 0; i < 6; ++i) {
                            double d = world.getRandom().nextGaussian() * 0.02;
                            double e = world.getRandom().nextGaussian() * 0.02;
                            double f = world.getRandom().nextGaussian() * 0.02;
                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_POOF, new Vec3f(Vec3d.unpackRgb(0xFF1313))), entity.getParticleX(1.0), entity.getRandomBodyY(), entity.getParticleZ(1.0), d, e, f);
                        }
                    } else if (status == 10) {
                        world.playSound(entity.getX(), entity.getY(), entity.getZ(), ParticleRegistry.DEATH_SHOP_EMERGE, SoundCategory.BLOCKS, 1f, 1f, true);
                    } else if (status == 11) {

                        Random random = Random.create();

                        for (int i = 0; i < 4; ++i) {
                            double g = entity.getX();
                            double h = entity.getBodyY(0.45 + random.nextDouble() * 0.55);
                            double j = entity.getZ();

                            double d = random.nextGaussian() * 0.1;
                            double e = random.nextGaussian() * 0.1;
                            double f = random.nextGaussian() * 0.1;

                            world.addParticle(new GoopDropParticleEffect(new Vec3f(Vec3d.unpackRgb(0x820A0A)), (float) (world.getRandom().nextDouble() * 2.25)),
                                    true, g + d, h + e, j + f, d, e, f);
                        }
                    } else if (status == 12) {
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), ParticleRegistry.DEATH_SHOP_EMERGE, SoundCategory.BLOCKS, 1f, 1f, true);
                        client.particleManager.addEmitter(entity, ParticleRegistry.BLACK_MATTER_TOTEM, 0);
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

        ClientPlayNetworking.registerGlobalReceiver(new Identifier(DeathMoney.MOD_ID, "adddeaths"), (client, handler, buf, responseSender) -> {
            try {
                int deaths = buf.readInt();
                PlayerEntity playerEntity = client.player;
                if (playerEntity instanceof PlayerProperties playerProperties) {
                    client.execute(() -> {
                        List<DeathToast> deathToasts = playerProperties.getDeathToasts();

                        DeathToast prevDeathToast = deathToasts.isEmpty() ? null : deathToasts.get(deathToasts.size() - 1);
                        int y = prevDeathToast == null ? 0 : Math.min(prevDeathToast.getY() - 10, 0);

                        if (prevDeathToast != null && deathToasts.size() >= 35) {
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

        ClientPlayNetworking.registerGlobalReceiver(new Identifier(DeathMoney.MOD_ID, "healthupdate"), (client, handler, buf, responseSender) -> {
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

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> GrassColors.getColor(0.5, 1.0), ItemRegistry.GAMER_ALLERGY_INJECTION);
        ModelPredicateProviderRegistry.register(ItemRegistry.HEART_CARVER, new Identifier("using"), (stack, world, entity, seed) ->
                entity instanceof PlayerProperties playerProperties && playerProperties.getHeartRemovalState().isRunning() ? 1.0F : 0.0F
        );

        ModelPredicateProviderRegistry.register(ItemRegistry.GAMER_ALLERGY_INJECTION, new Identifier("usages"), (stack, world, entity, seed) ->
                switch (stack.getOrCreateNbt().getInt("usages")) {
                    default -> 1.0f;
                    case 1 -> 0.9f;
                    case 2 -> 0.8f;
                    case 3 -> 0.7f;
                }
        );

        ClientPlayNetworking.registerGlobalReceiver(MeteorExplosionHandler.IDENTIFIER, (client, handler, buf, responseSender) -> {
            try {
                MeteorExplosionHandler packet = new MeteorExplosionHandler(buf);
                client.execute(() -> {
                    MeteorExplosionHandler.Explosion explosion = new MeteorExplosionHandler.Explosion(client.world, null, packet.getX(), packet.getY(), packet.getZ(), packet.getRadius(), packet.getAffectedBlocks());
                    explosion.affectWorld(true);
                    client.player.setVelocity(client.player.getVelocity().add(packet.getPlayerVelocityX(), packet.getPlayerVelocityY(), packet.getPlayerVelocityZ()));
                });
            } catch (Exception exception) {

            }
        });

        ClientPlayNetworking.registerGlobalReceiver(TntNukeExplosionHandler.IDENTIFIER, (client, handler, buf, responseSender) -> {
            try {
                TntNukeExplosionHandler packet = new TntNukeExplosionHandler(buf);
                client.execute(() -> {
                    TntNukeExplosionHandler.Explosion explosion = new TntNukeExplosionHandler.Explosion(client.world, null, packet.getX(), packet.getY(), packet.getZ(), packet.getRadius(), packet.getAffectedBlocks());
                    explosion.affectWorld(true);
                    client.player.setVelocity(client.player.getVelocity().add(packet.getPlayerVelocityX(), packet.getPlayerVelocityY(), packet.getPlayerVelocityZ()));
                });
            } catch (Exception exception) {

            }
        });

        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.LITTLE_WITHER_BULLY, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
            matrices.translate(0.5, 0, 0.5);
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180));

            EntityType<?> entityType = EntityRegistry.LITTLE_WITHER_BULLY;
            Entity renderEntity = entityType.create(MinecraftClient.getInstance().world);
            if (renderEntity != null) {
                renderEntity.age = MinecraftClient.getInstance().player != null ? MinecraftClient.getInstance().player.age : 0;
                MinecraftClient.getInstance().getEntityRenderDispatcher().render(renderEntity,
                        BlockPos.ORIGIN.getX(), BlockPos.ORIGIN.getY(), BlockPos.ORIGIN.getZ(),
                        0, 0, matrices, vertexConsumers, light);
            }
        });

        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.CRAZY_EX_GIRLFRIENDS, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
            matrices.translate(0.5, 0, 0.5);
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180));

            EntityType<?> entityType = EntityRegistry.CRAZY_EX_GIRLFRIEND;
            Entity renderEntity = entityType.create(MinecraftClient.getInstance().world);
            if (renderEntity != null) {
                MinecraftClient.getInstance().getEntityRenderDispatcher().render(renderEntity,
                        BlockPos.ORIGIN.getX(), BlockPos.ORIGIN.getY(), BlockPos.ORIGIN.getZ(),
                        0, 0, matrices, vertexConsumers, light);
            }
        });

        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.HUNGRY_TITAN, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
            matrices.translate(0.5, 0, 0.5);
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180));

            matrices.scale(0.25f, 0.25f, 0.25f);

            EntityType<?> entityType = EntityRegistry.HUNGRY_TITAN;
            Entity renderEntity = entityType.create(MinecraftClient.getInstance().world);
            if (renderEntity != null) {
                MinecraftClient.getInstance().getEntityRenderDispatcher().render(renderEntity,
                        BlockPos.ORIGIN.getX(), BlockPos.ORIGIN.getY(), BlockPos.ORIGIN.getZ(),
                        0, 0, matrices, vertexConsumers, light);
            }
        });

        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.BLACK_HOLE, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
            matrices.translate(0.5, 0.0, 0.5);

            EntityType<?> entityType = EntityRegistry.BLACK_HOLE;
            Entity renderEntity = entityType.create(MinecraftClient.getInstance().world);
            if (renderEntity instanceof BlackHoleEntity blackHoleEntity) {
                blackHoleEntity.setPreview(true);
                renderEntity.age = MinecraftClient.getInstance().player != null ? MinecraftClient.getInstance().player.age : 0;
                MinecraftClient.getInstance().getEntityRenderDispatcher().render(renderEntity,
                        BlockPos.ORIGIN.getX(), BlockPos.ORIGIN.getY(), BlockPos.ORIGIN.getZ(),
                        0, 0, matrices, vertexConsumers, light);
            }
        });

        ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register((atlasTexture, registry) -> {
            registry.register(new Identifier("deathmoney:block/blood_still"));
            registry.register(new Identifier("deathmoney:block/blood_flow"));
        });
        FluidRenderHandlerRegistry.INSTANCE.register(ItemRegistry.BLOOD_STILL, ItemRegistry.BLOOD_FLOW, new SimpleFluidRenderHandler(new Identifier("deathmoney:block/blood_still"), new Identifier("deathmoney:block/blood_flow")));

    }
}
