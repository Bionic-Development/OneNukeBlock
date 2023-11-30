package de.takacick.elementalblock;

import com.google.common.collect.Lists;
import de.takacick.elementalblock.client.renderer.WaterArmorVesselRenderer;
import de.takacick.elementalblock.mixin.InGameHudAccessor;
import de.takacick.elementalblock.registry.EntityRegistry;
import de.takacick.elementalblock.registry.ItemRegistry;
import de.takacick.elementalblock.registry.ParticleRegistry;
import de.takacick.elementalblock.registry.entity.living.renderer.*;
import de.takacick.elementalblock.registry.entity.projectile.renderer.CobblestoneEntityRenderer;
import de.takacick.elementalblock.registry.entity.projectile.renderer.MagmaEntityRenderer;
import de.takacick.elementalblock.registry.entity.projectile.renderer.TsunamicTridentEntityRenderer;
import de.takacick.elementalblock.registry.entity.projectile.renderer.WhisperwindEntityRenderer;
import de.takacick.elementalblock.registry.particles.*;
import de.takacick.elementalblock.registry.particles.goop.GoopDropParticle;
import de.takacick.elementalblock.registry.particles.goop.GoopParticle;
import de.takacick.elementalblock.registry.particles.goop.GoopStringParticle;
import de.takacick.elementalblock.server.explosion.CobblestoneExplosionHandler;
import de.takacick.elementalblock.server.explosion.EarthExplosionHandler;
import de.takacick.elementalblock.server.explosion.MagmaExplosionHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EmptyEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.joml.Vector3f;

import java.util.List;

public class OneElementalBlockClient implements ClientModInitializer {


    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityRegistry.BLOCK_BREAK, EmptyEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.COBBLESTONE, CobblestoneEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.TSUNAMIC_TRIDENT, TsunamicTridentEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.MAGIC_CLOUD_BUDDY, MagicCloudBuddyEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.EARTH_ELEMENTAL_CREEPER, EarthElementalCreeperEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.AIR_ELEMENTAL_GOLEM, AirElementalGolemEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.WATER_ELEMENTAL_SLIME, WaterElementalSlimeEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.FIRE_ELEMENTAL_WARDEN, FireElementalWardenEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.MAGMA, MagmaEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.WHISPERWIND, WhisperwindEntityRenderer::new);

        BlockRenderLayerMap.INSTANCE.putBlock(ItemRegistry.EARTH_ELEMENTAL_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ItemRegistry.AIR_ELEMENTAL_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ItemRegistry.WATER_ELEMENTAL_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ItemRegistry.FIRE_ELEMENTAL_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ItemRegistry.CLOUD_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ItemRegistry.CLOUD_LOG, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ItemRegistry.LAVA_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ItemRegistry.WATER_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), ItemRegistry.WATER, ItemRegistry.FLOWING_WATER);
        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), ItemRegistry.TSUNAMIC_WATER, ItemRegistry.TSUNAMIC_FLOWING_WATER);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> 4159204, ItemRegistry.WATER_BLOCK_ITEM);

        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP, GoopParticle.GoopParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP_DROP, GoopDropParticle.GoopDropParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP_STRING, GoopStringParticle.GoopStringParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.DUST, DustParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.FALLING_WATER, WaterLeakParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.WATER_SPLASH, WaterSplashParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.WATER_SLIME, new WaterSlimeParticle.Factory());
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.WATER_POOF, WaterPoofParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.CLOUD, CloudParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.COLORED_GLOW_SPARK, ColoredGlowSparkParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.MAGMA, MagmaParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.LAVA_TOTEM, LavaTotemParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.MAGIC_LAVA, MagicLavaParticle.Factory::new);

        ClientPlayNetworking.registerGlobalReceiver(OneElementalBlock.IDENTIFIER, (client, handler, buf, responseSender) -> {
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

                        Vector3f color = Vec3d.unpackRgb(4159204).toVector3f();

                        for (int i = 0; i < 25; ++i) {
                            double h = entity.getRandomBodyY();
                            double d = random.nextGaussian() * 0.2;
                            double e = random.nextDouble() * 0.3;
                            double f = random.nextGaussian() * 0.2;

                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.FALLING_WATER, color),
                                    true, g + d, h + e, j + f, d * 0.3, e * 0.2, f * 0.3);
                        }
                        double h = entity.getBodyY(0.5);

                        world.playSound(g, h, j, SoundEvents.ENTITY_AXOLOTL_SPLASH, SoundCategory.AMBIENT, 1f, 1f + world.getRandom().nextFloat() * 0.2f, true);
                    } else if (status == 2) {
                        double g = entity.getX();
                        double j = entity.getZ();
                        for (int i = 0; i < 15; ++i) {
                            double h = entity.getY() + world.getRandom().nextDouble() * entity.getHeight() * 0.5;
                            double x = world.getRandom().nextGaussian() * 0.3;
                            double y = world.getRandom().nextGaussian() * 0.3;
                            double z = world.getRandom().nextGaussian() * 0.3;

                            world.addParticle(ParticleRegistry.CLOUD, true, g, h, j, x, y, z);
                        }
                    } else if (status == 3) {
                        double g = entity.getX();
                        double j = entity.getZ();
                        for (int i = 0; i < 25; ++i) {
                            double h = entity.getRandomBodyY();
                            world.addParticle(ParticleRegistry.MAGIC_LAVA, true, g, h, j, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3);
                        }

                        for (int i = 0; i < 20; ++i) {
                            double h = entity.getRandomBodyY();
                            double x = world.getRandom().nextGaussian() * 0.3;
                            double y = world.getRandom().nextGaussian() * 0.3;
                            double z = world.getRandom().nextGaussian() * 0.3;

                            world.addParticle(ParticleTypes.FLAME, true, g, h, j, x, y, z);
                            world.addParticle(ParticleTypes.SMOKE, true, g, h, j, x, y, z);
                        }

                        client.particleManager.addEmitter(entity, ParticleRegistry.MAGMA, 10);
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ITEM_FIRECHARGE_USE, entity.getSoundCategory(), 0.3f, 1.2f, true);
                        world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, entity.getSoundCategory(), 1.0f, 1.0f, false);
                        world.playSound(g, entity.getBodyY(0.5), j, SoundEvents.BLOCK_LAVA_POP, entity.getSoundCategory(), 0.65f, 1.0f, false);
                    } else if (status == 4) {
                        double g = entity.getX();
                        double j = entity.getZ();
                        for (int i = 0; i < 25; ++i) {
                            double h = entity.getRandomBodyY();
                            world.addParticle(ParticleRegistry.MAGIC_LAVA, true, g, h, j, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3);
                        }

                        for (int i = 0; i < 20; ++i) {
                            double h = entity.getRandomBodyY();
                            double x = world.getRandom().nextGaussian() * 0.3;
                            double y = world.getRandom().nextGaussian() * 0.3;
                            double z = world.getRandom().nextGaussian() * 0.3;

                            world.addParticle(ParticleTypes.FLAME, true, g, h, j, x, y, z);
                        }

                        world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BLOCK_FIRE_EXTINGUISH, entity.getSoundCategory(), 1.0f, 1.0f, false);
                        world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, entity.getSoundCategory(), 1.0f, 1.0f, false);
                        world.playSound(g, entity.getBodyY(0.5), j, SoundEvents.BLOCK_LAVA_POP, entity.getSoundCategory(), 0.65f, 1.0f, false);
                    } else if (status == 5) {
                        Vec3d vec3d = entity.getRotationVector();
                        double g = entity.getX();
                        double j = entity.getZ();

                        for (int i = 0; i < 30; ++i) {
                            double h = entity.getBodyY(0.5) + world.getRandom().nextGaussian() * 0.15;
                            double x = world.getRandom().nextGaussian() * 0.15;
                            double y = world.getRandom().nextGaussian() * 0.15;
                            double z = world.getRandom().nextGaussian() * 0.15;

                            world.addImportantParticle(ParticleTypes.FLAME, true, g + x, h + y, j + z, vec3d.getX() + x, vec3d.getY() + y, vec3d.getZ() + z);
                        }
                    }
                }
            });
        });

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> FoliageColors.getDefaultColor(), ItemRegistry.TREE);

        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.WATER_ARMOR_VESSEL, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
            matrices.translate(0.5, 0, 0.5);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0F));
            matrices.translate(0, -1.5, 0);

            VertexConsumer vertexConsumer = WaterArmorVesselRenderer.SKIN.getSprite().getTextureSpecificVertexConsumer(vertexConsumers.getBuffer(
                    RenderLayer.getEntityTranslucent(WaterArmorVesselRenderer.SKIN.getAtlasId())));
            Vector3f vec3f = Vec3d.unpackRgb(4159204).multiply(1.1f).toVector3f();
            TexturedModelData.of(BipedEntityModel.getModelData(new Dilation(0.5f), 0.0f), 64, 32)
                    .createModel().render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, vec3f.x(), vec3f.y(), vec3f.z(), 0.7f);
        });

        ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> {
            out.accept(new ModelIdentifier(OneElementalBlock.MOD_ID, "water_slime_ball", "inventory"));
            out.accept(new ModelIdentifier(OneElementalBlock.MOD_ID, "tsunamic_trident_in_hand", "inventory"));
            out.accept(new ModelIdentifier(OneElementalBlock.MOD_ID, "tsunamic_trident_throwing", "inventory"));
        });

        ModelPredicateProviderRegistry.register(ItemRegistry.TSUNAMIC_TRIDENT, new Identifier("throwing"),
                (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0f : 0.0f);
        ModelPredicateProviderRegistry.register(ItemRegistry.WHISPERWIND_BOW, new Identifier("pull"), (stack, world, entity, seed) -> {
            if (entity == null) {
                return 0.0f;
            }
            if (entity.getActiveItem() != stack) {
                return 0.0f;
            }
            return (float) (stack.getMaxUseTime() - entity.getItemUseTimeLeft()) / 10.0f;
        });
        ModelPredicateProviderRegistry.register(ItemRegistry.WHISPERWIND_BOW, new Identifier("pulling"), (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0f : 0.0f);


        ClientPlayNetworking.registerGlobalReceiver(new Identifier(OneElementalBlock.MOD_ID, "healthupdate"), (client, handler, buf, responseSender) -> {
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

        ClientPlayNetworking.registerGlobalReceiver(CobblestoneExplosionHandler.IDENTIFIER, (client, handler, buf, responseSender) -> {
            try {
                CobblestoneExplosionHandler packet = new CobblestoneExplosionHandler(buf);
                client.execute(() -> {
                    CobblestoneExplosionHandler.Explosion explosion = new CobblestoneExplosionHandler.Explosion(client.world, null, packet.getX(), packet.getY(), packet.getZ(), packet.getRadius(), packet.getAffectedBlocks());
                    explosion.affectWorld(true);
                    client.player.setVelocity(client.player.getVelocity().add(packet.getPlayerVelocityX(), packet.getPlayerVelocityY(), packet.getPlayerVelocityZ()));
                });
            } catch (Exception exception) {

            }
        });

        ClientPlayNetworking.registerGlobalReceiver(EarthExplosionHandler.IDENTIFIER, (client, handler, buf, responseSender) -> {
            try {
                EarthExplosionHandler packet = new EarthExplosionHandler(buf);
                client.execute(() -> {
                    EarthExplosionHandler.Explosion explosion = new EarthExplosionHandler.Explosion(client.world, null, packet.getX(), packet.getY(), packet.getZ(), packet.getRadius(), packet.getAffectedBlocks());
                    explosion.affectWorld(true);
                    client.player.setVelocity(client.player.getVelocity().add(packet.getPlayerVelocityX(), packet.getPlayerVelocityY(), packet.getPlayerVelocityZ()));
                });
            } catch (Exception exception) {

            }
        });

        ClientPlayNetworking.registerGlobalReceiver(MagmaExplosionHandler.IDENTIFIER, (client, handler, buf, responseSender) -> {
            try {
                MagmaExplosionHandler packet = new MagmaExplosionHandler(buf);
                client.execute(() -> {
                    MagmaExplosionHandler.Explosion explosion = new MagmaExplosionHandler.Explosion(client.world, null, packet.getX(), packet.getY(), packet.getZ(), packet.getRadius(), packet.getAffectedBlocks());
                    explosion.affectWorld(true);
                    client.player.setVelocity(client.player.getVelocity().add(packet.getPlayerVelocityX(), packet.getPlayerVelocityY(), packet.getPlayerVelocityZ()));
                });
            } catch (Exception exception) {

            }
        });
    }


    public static ItemStack getRocketItem(int data) {
        ItemStack itemStack = new ItemStack(Items.FIREWORK_ROCKET);
        NbtCompound nbtCompound = itemStack.getOrCreateSubNbt("Fireworks");

        nbtCompound.putByte("Flight", (byte) 1);
        NbtCompound explosion = new NbtCompound();
        List<Integer> list = Lists.newArrayList();

        explosion.putBoolean("Flicker", true);
        explosion.putBoolean("Trail", true);

        if (data == 0) {
            list.add(0x79553A);
            list.add(0x966C4A);
            list.add(0x79553A);
            list.add(0xB9855C);
            list.add(0xA6A6A6);
            list.add(0x616161);
        } else if (data == 1) {
            list.add(0xFFFFFF);
        } else if (data == 2) {
            list.add(0x2D40F4);
            list.add(0x3046F4);
        } else if (data == 3) {
            list.add(0xE03300);
            list.add(0xFFEB75);
            list.add(0xFFA02D);
        }

        explosion.putIntArray("Colors", list);
        explosion.putIntArray("FadeColors", list);
        explosion.putByte("Type", (byte) 0);
        NbtList nbtList = new NbtList();
        nbtList.add(explosion);

        nbtCompound.put("Explosions", nbtList);

        return itemStack;
    }
}
