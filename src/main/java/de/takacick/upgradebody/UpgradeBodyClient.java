package de.takacick.upgradebody;

import de.takacick.upgradebody.access.PlayerProperties;
import de.takacick.upgradebody.mixin.InGameHudAccessor;
import de.takacick.upgradebody.registry.EntityRegistry;
import de.takacick.upgradebody.registry.ItemRegistry;
import de.takacick.upgradebody.registry.ParticleRegistry;
import de.takacick.upgradebody.registry.bodypart.BodyParts;
import de.takacick.upgradebody.registry.entity.custom.renderer.ShopItemEntityRenderer;
import de.takacick.upgradebody.registry.entity.custom.renderer.UpgradeShopPortalEntityRenderer;
import de.takacick.upgradebody.registry.entity.living.renderer.AllSeeingWardenEntityRenderer;
import de.takacick.upgradebody.registry.entity.living.renderer.SentientDesertPyramidEntityRenderer;
import de.takacick.upgradebody.registry.entity.living.renderer.TurretPillagerEntityRenderer;
import de.takacick.upgradebody.registry.entity.projectiles.renderer.CustomBlockEntityRenderer;
import de.takacick.upgradebody.registry.entity.projectiles.renderer.EnergyBulletEntityRenderer;
import de.takacick.upgradebody.registry.entity.projectiles.renderer.TntEntityRenderer;
import de.takacick.upgradebody.registry.particles.*;
import de.takacick.upgradebody.registry.particles.goop.GoopDropParticle;
import de.takacick.upgradebody.registry.particles.goop.GoopDropParticleEffect;
import de.takacick.upgradebody.registry.particles.goop.GoopParticle;
import de.takacick.upgradebody.registry.particles.goop.GoopStringParticle;
import de.takacick.upgradebody.server.explosion.EnergyExplosionHandler;
import de.takacick.utils.BionicUtilsClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EmptyEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

public class UpgradeBodyClient implements ClientModInitializer {

    private KeyBinding bionicHeadbutt;
    private boolean bionicHeadbuttBoolean;
    private KeyBinding energyBellyBlast;
    private boolean energyBellyBlastBoolean;
    private KeyBinding killerDrilling;
    private boolean killerDrillingBoolean;
    private KeyBinding cyberSlice;
    private boolean cyberSliceBoolean;

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityRegistry.UPGRADE_SHOP_PORTAL, UpgradeShopPortalEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.SHOP_ITEM, ShopItemEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.BLOCK_BREAK, EmptyEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.TURRET_PILLAGER, TurretPillagerEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.ALL_SEEING_WARDEN, AllSeeingWardenEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.SENTIENT_DESERT_PYRAMID, SentientDesertPyramidEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.TNT, TntEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.ENERGY_BULLET, EnergyBulletEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.FALLING_BLOCK, CustomBlockEntityRenderer::new);

        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP, GoopParticle.GoopParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP_DROP, GoopDropParticle.GoopDropParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP_STRING, GoopStringParticle.GoopStringParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.DUST, DustParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.UPGRADE_PORTAL, UpgradePortalParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.XP_TOTEM, XPTotemParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.XP_EXPLOSION, XPExplosionParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.XP_EXPLOSION_EMITTER, new XPExplosionEmitterParticle.Factory());
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.PLAYER_SLIME, new PlayerSlimeParticle.Factory());
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.XP_FLASH, XPFlashParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.FALLING_BLOOD, BloodLeakParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.BLOOD_SPLASH, BloodSplashParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.ENERGY_PORTAL, EnergyPortalParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.ENERGY_EXPLOSION, EnergyExplosionParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.ENERGY_EXPLOSION_EMITTER, new EnergyExplosionEmitterParticle.Factory());
        try {
            bionicHeadbutt = KeyBindingHelper.registerKeyBinding(
                    new KeyBinding("Bionic Headbutt",
                            InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X,
                            "UpgradeBody Abilities")
            );
            energyBellyBlast = KeyBindingHelper.registerKeyBinding(
                    new KeyBinding("Energy Belly Blast",
                            InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X,
                            "UpgradeBody Abilities")
            );
            killerDrilling = KeyBindingHelper.registerKeyBinding(
                    new KeyBinding("Killer Drilling",
                            InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X,
                            "UpgradeBody Abilities")
            );
            cyberSlice = KeyBindingHelper.registerKeyBinding(
                    new KeyBinding("Cyber Slice",
                            InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X,
                            "UpgradeBody Abilities")
            );
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                PlayerEntity playerEntity = client.player;
                if (!(playerEntity instanceof PlayerProperties playerProperties)) {
                    return;
                }

                if (bionicHeadbutt.isPressed() && !bionicHeadbuttBoolean) {
                    bionicHeadbuttBoolean = bionicHeadbutt.isPressed();
                    if (playerProperties.isUpgrading()
                            && playerProperties.getBodyPartManager().isHeadOnly()) {
                        PacketByteBuf buf = PacketByteBufs.create();
                        ClientPlayNetworking.send(new Identifier(UpgradeBody.MOD_ID, "bionicheadbutt"), buf);
                    }
                } else {
                    bionicHeadbuttBoolean = bionicHeadbutt.isPressed();
                }

                if (energyBellyBlast.isPressed()
                        && playerProperties.isUpgrading()
                        && playerProperties.getBodyPartManager().hasBodyPart(BodyParts.ENERGY_BELLY_CANNON)) {
                    energyBellyBlastBoolean = energyBellyBlast.isPressed();
                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeInt(1);
                    ClientPlayNetworking.send(new Identifier(UpgradeBody.MOD_ID, "energybellycannon"), buf);
                } else {
                    if (energyBellyBlastBoolean) {
                        PacketByteBuf buf = PacketByteBufs.create();
                        buf.writeInt(0);
                        ClientPlayNetworking.send(new Identifier(UpgradeBody.MOD_ID, "energybellycannon"), buf);
                    }

                    energyBellyBlastBoolean = energyBellyBlast.isPressed();
                }

                if (killerDrilling.isPressed()
                        && playerProperties.isUpgrading()
                        && playerProperties.getBodyPartManager().hasBodyPart(BodyParts.KILLER_DRILLER)) {
                    killerDrillingBoolean = killerDrilling.isPressed();
                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeInt(1);
                    ClientPlayNetworking.send(new Identifier(UpgradeBody.MOD_ID, "killerdrilling"), buf);
                } else {
                    if (killerDrillingBoolean) {
                        PacketByteBuf buf = PacketByteBufs.create();
                        buf.writeInt(0);
                        ClientPlayNetworking.send(new Identifier(UpgradeBody.MOD_ID, "killerdrilling"), buf);
                    }

                    killerDrillingBoolean = killerDrilling.isPressed();
                }

                if (cyberSlice.isPressed()
                        && playerProperties.isUpgrading()
                        && playerProperties.getBodyPartManager().hasBodyPart(BodyParts.CYBER_CHAINSAWS)
                        && playerEntity.getMainHandStack().isEmpty() && playerEntity.getOffHandStack().isEmpty()) {
                    cyberSliceBoolean = cyberSlice.isPressed();
                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeInt(1);
                    ClientPlayNetworking.send(new Identifier(UpgradeBody.MOD_ID, "cyberslice"), buf);
                } else {
                    if (cyberSliceBoolean) {
                        PacketByteBuf buf = PacketByteBufs.create();
                        buf.writeInt(0);
                        ClientPlayNetworking.send(new Identifier(UpgradeBody.MOD_ID, "cyberslice"), buf);
                    }

                    cyberSliceBoolean = cyberSlice.isPressed();
                }
            });
        } catch (RuntimeException ignored) {

        }

        ClientPlayNetworking.registerGlobalReceiver(UpgradeBody.IDENTIFIER, (client, handler, buf, responseSender) -> {
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
                        world.playSoundFromEntity(playerEntity, entity, SoundEvents.BLOCK_AMETHYST_CLUSTER_HIT, entity.getSoundCategory(), 0.8f, 1f);
                        client.particleManager.addEmitter(entity, ParticleRegistry.XP_TOTEM, 2);
                        world.addParticle(ParticleRegistry.XP_FLASH, entity.getX(), entity.getBodyY(0.5f), entity.getZ(), 0.25f, 0, 0);
                    } else if (status == 2) {
                        client.particleManager.addEmitter(entity, ParticleRegistry.XP_TOTEM, 0);
                        world.addParticle(ParticleRegistry.XP_FLASH, entity.getX(), entity.getBodyY(0.5), entity.getZ(), 0.1, 0, 0);
                    } else if (status == 3) {
                        world.addParticle(ParticleTypes.EXPLOSION, entity.getX(), entity.getBodyY(0.5), entity.getZ(), 0.1, 0, 0);
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_IRON_GOLEM_DAMAGE, entity.getSoundCategory(), 0.8f, 1f, true);
                    } else if (status == 4) {
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
                    } else if (status == 5) {
                        double g = entity.getX();
                        double j = entity.getZ();

                        for (int i = 0; i < 15; ++i) {
                            double h = entity.getBodyY(0.5);
                            double d = random.nextGaussian() * 0.4;
                            double e = random.nextDouble() * 0.5;
                            double f = random.nextGaussian() * 0.4;

                            world.addParticle(ParticleRegistry.FALLING_BLOOD,
                                    true, g + d, h + e, j + f, d * 0.3, e * 0.3, f * 0.3);
                        }

                        for (int i = 0; i < 3; ++i) {
                            double h = entity.getRandomBodyY();
                            double d = random.nextGaussian() * 0.4;
                            double e = random.nextDouble() * 1;
                            double f = random.nextGaussian() * 0.4;

                            world.addParticle(new GoopDropParticleEffect(Vec3d.unpackRgb(0x820A0A).toVector3f(), 0.1f + (float) (random.nextDouble() * 3)),
                                    true, g + d, h, j + f, d * 0.5, e * 0.3, f * 0.5);
                        }
                    } else if (status == 6) {
                        world.addParticle(ParticleRegistry.XP_FLASH, entity.getX(), entity.getBodyY(0.5), entity.getZ(), 0.2, 0, 0);
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, entity.getSoundCategory(), 0.8f, 1f, true);

                        double g = entity.getX();
                        double j = entity.getZ();

                        for (int i = 0; i < 6; ++i) {
                            double h = entity.getRandomBodyY();
                            double d = random.nextGaussian() * 0.2;
                            double e = random.nextDouble() * 0.3;
                            double f = random.nextGaussian() * 0.2;

                            world.addParticle(ParticleRegistry.XP_TOTEM,
                                    true, g + d, h + e, j + f, d * 1.5, e * 1.5, f * 1.5);
                        }
                    } else if (status == 7) {
                        world.addParticle(ParticleRegistry.XP_FLASH, entity.getX(), entity.getBodyY(0.5), entity.getZ(), 0.2, 0, 0);
                        world.addParticle(ParticleRegistry.XP_EXPLOSION, entity.getX(), entity.getBodyY(0.5), entity.getZ(), 0.2, 0, 0);
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, entity.getSoundCategory(), 0.8f, 1f, true);

                        double g = entity.getX();
                        double j = entity.getZ();

                        for (int i = 0; i < 14; ++i) {
                            double h = entity.getRandomBodyY();
                            double d = random.nextGaussian() * 0.2;
                            double e = random.nextDouble() * 0.3;
                            double f = random.nextGaussian() * 0.2;

                            world.addParticle(ParticleRegistry.XP_TOTEM,
                                    true, g + d, h + e, j + f, d * 1.5, e * 1.5, f * 1.5);
                        }
                    } else if (status == 8) {
                        world.addParticle(ParticleRegistry.XP_FLASH, entity.getX(), entity.getBodyY(0.5), entity.getZ(), 0.2, 0, 0);
                        world.addParticle(ParticleRegistry.XP_EXPLOSION_EMITTER, entity.getX(), entity.getBodyY(0.5), entity.getZ(), 0.2, 0, 0);
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, entity.getSoundCategory(), 6f, 1f, true);
                        for (int i = 0; i < 25; ++i) {
                            double g = entity.getParticleX(entity.getWidth() * 0.5);
                            double j = entity.getParticleZ(entity.getWidth() * 0.5);

                            double h = entity.getRandomBodyY();
                            double d = random.nextGaussian() * 0.2;
                            double e = random.nextDouble() * 0.3;
                            double f = random.nextGaussian() * 0.2;

                            world.addImportantParticle(ParticleRegistry.XP_EXPLOSION_EMITTER, true, g + d, h + e, j + f, 0.2, 0, 0);
                        }

                        for (int i = 0; i < 20; ++i) {
                            double g = entity.getParticleX(entity.getWidth());
                            double j = entity.getParticleZ(entity.getWidth());

                            double h = entity.getRandomBodyY();
                            double d = random.nextGaussian() * 0.2;
                            double e = random.nextDouble() * 0.3;
                            double f = random.nextGaussian() * 0.2;

                            world.addParticle(ParticleRegistry.XP_TOTEM,
                                    true, g + d, h + e, j + f, d * 1.5, e * 1.5, f * 1.5);
                        }
                    } else if (status == 9) {
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.BLOCK_MUD_BREAK, entity.getSoundCategory(), 0.8f, 1f, true);
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_IRON_GOLEM_DAMAGE, entity.getSoundCategory(), 0.8f, 1.4f, true);
                        double g = entity.getX();
                        double j = entity.getZ();

                        for (int i = 0; i < 15; ++i) {
                            double h = entity.getBodyY(0.5);
                            double d = random.nextGaussian() * 0.4;
                            double e = random.nextDouble() * 0.5;
                            double f = random.nextGaussian() * 0.4;

                            world.addParticle(ParticleRegistry.FALLING_BLOOD,
                                    true, g + d, h + e, j + f, d * 0.3, e * 0.3, f * 0.3);
                        }

                        for (int i = 0; i < 3; ++i) {
                            double h = entity.getRandomBodyY();
                            double d = random.nextGaussian() * 0.4;
                            double e = random.nextDouble() * 1;
                            double f = random.nextGaussian() * 0.4;

                            world.addParticle(new GoopDropParticleEffect(Vec3d.unpackRgb(0x820A0A).toVector3f(), 0.1f + (float) (random.nextDouble() * 3)),
                                    true, g + d, h, j + f, d * 0.5, e * 0.3, f * 0.5);
                        }

                        for (int i = 0; i < 6; ++i) {
                            double h = entity.getRandomBodyY();
                            double d = random.nextGaussian() * 0.2;
                            double e = random.nextDouble() * 0.3;
                            double f = random.nextGaussian() * 0.2;

                            world.addParticle(ParticleRegistry.XP_TOTEM,
                                    true, g + d, h + e, j + f, d * 1.5, e * 1.5, f * 1.5);
                        }
                    } else if (status >= 10 && status <= 13) {
                        Item item = switch (status) {
                            case 11 -> ItemRegistry.ENERGY_BELLY_CANNON;
                            case 12 -> ItemRegistry.KILLER_DRILLER;
                            case 13 -> ItemRegistry.CYBER_CHAINSAWS;
                            default -> ItemRegistry.TANK_TRACKS;
                        };

                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, entity.getSoundCategory(), 1f, 1f, false);
                        double g = entity.getX();
                        double j = entity.getZ();
                        world.addParticle(ParticleRegistry.XP_EXPLOSION, entity.getX(), entity.getBodyY(0.5), entity.getZ(), 0.2, 0, 0);

                        for (int i = 0; i < 15; ++i) {
                            double h = entity.getRandomBodyY();
                            double d = random.nextGaussian() * 0.2;
                            double e = random.nextDouble() * 0.3;
                            double f = random.nextGaussian() * 0.2;

                            world.addParticle(ParticleRegistry.XP_TOTEM,
                                    true, g + d, h + e, j + f, d * 1.5, e * 1.5, f * 1.5);
                        }

                        if (entity == playerEntity) {
                            client.gameRenderer.showFloatingItem(item.getDefaultStack());
                        }
                    }
                }
            });
        });

        ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> {
            out.accept(new ModelIdentifier(UpgradeBody.MOD_ID, "player_slime_ball", "inventory"));
            out.accept(new ModelIdentifier(UpgradeBody.MOD_ID, "upgrade_shop_portal_border", "inventory"));
            out.accept(new ModelIdentifier(UpgradeBody.MOD_ID, "upgrade_shop_portal_inside", "inventory"));
        });

        ClientPlayNetworking.registerGlobalReceiver(new Identifier(UpgradeBody.MOD_ID, "healthupdate"), (client, handler, buf, responseSender) -> {
            try {
                double maxHealthValue = buf.readDouble();
                int healthValue = buf.readInt();
                float health = buf.readFloat();

                client.execute(() -> {
                    if (client.player != null) {
                        client.player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(maxHealthValue);
                        client.player.setHealth(health);
                    }
                    ((InGameHudAccessor) client.inGameHud).setLastHealthValue(healthValue);
                    ((InGameHudAccessor) client.inGameHud).setRenderHealthValue(healthValue);
                });
            } catch (Exception exception) {

            }
        });

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            return UpgradeBodyClient.getColor();
        }, ItemRegistry.UPGRADE_SHOP_PORTAL);

        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.UPGRADE_SHOP_PORTAL, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
            matrices.push();
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getTranslucent());

            BakedModel bakedModel = MinecraftClient.getInstance().getItemRenderer()
                    .getModels().getModelManager().getModel(new ModelIdentifier(UpgradeBody.MOD_ID, "upgrade_shop_portal_border", "inventory"));
            UpgradeShopPortalEntityRenderer.renderBakedItemQuads(matrices, vertexConsumer, bakedModel.getQuads(null, null,
                    Random.create()), light, OverlayTexture.DEFAULT_UV);

            bakedModel = MinecraftClient.getInstance().getItemRenderer()
                    .getModels().getModelManager().getModel(new ModelIdentifier(UpgradeBody.MOD_ID, "upgrade_shop_portal_inside", "inventory"));
            UpgradeShopPortalEntityRenderer.renderBakedItemQuads(matrices, vertexConsumer, bakedModel.getQuads(null, null,
                    Random.create()), light, OverlayTexture.DEFAULT_UV);
            matrices.pop();
        });

        ClientPlayNetworking.registerGlobalReceiver(EnergyExplosionHandler.IDENTIFIER, (client, handler, buf, responseSender) -> {
            try {
                EnergyExplosionHandler packet = new EnergyExplosionHandler(buf);
                client.execute(() -> {
                    EnergyExplosionHandler.Explosion explosion = new EnergyExplosionHandler.Explosion(client.world, null, packet.getX(), packet.getY(), packet.getZ(), packet.getRadius(), packet.getAffectedBlocks());
                    explosion.affectWorld(true);
                    client.player.setVelocity(client.player.getVelocity().add(packet.getPlayerVelocityX(), packet.getPlayerVelocityY(), packet.getPlayerVelocityZ()));
                });
            } catch (Exception exception) {

            }
        });

    }

    public static int getColor(float time) {
        float r = (float) (((time + (MinecraftClient.getInstance().isPaused() ? MinecraftClient.getInstance().getTickDelta() : 0)) / 4.0f) % Math.PI);
        float s = (MathHelper.sin(r + 0.0f) + 1.0f) * 0.5f;
        float t = 1f;
        float u = (MathHelper.sin(r + 4.1887903f) + 1.0f) * 0.1f;
        return BionicUtilsClient.getRainbow().getIntFromColor((int) (s * 255), (int) (t * 255), (int) (u * 255));
    }

    public static int getColor() {
        return getColor(MinecraftClient.getInstance().world == null ? 0 : MinecraftClient.getInstance().world.getTime());
    }

    public static void sendHeadBlockBreaking(Entity entity, BlockPos blockPos) {
        PlayerEntity playerEntity = MinecraftClient.getInstance().player;
        if (playerEntity == null || !playerEntity.equals(entity)) {
            return;
        }

        PacketByteBuf buf = PacketByteBufs.create();
        if (blockPos != null) {
            buf.writeInt(1);
            buf.writeBlockPos(blockPos);
        } else {
            buf.writeInt(0);
        }
        ClientPlayNetworking.send(new Identifier(UpgradeBody.MOD_ID, "bionicheadbuttbreak"), buf);
    }

}
