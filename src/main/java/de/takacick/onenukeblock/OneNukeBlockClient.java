package de.takacick.onenukeblock;

import de.takacick.onenukeblock.client.bossbar.renderer.NukeBossBarRenderer;
import de.takacick.onenukeblock.client.helper.NukeParticleUtil;
import de.takacick.onenukeblock.client.item.ItemParticleManager;
import de.takacick.onenukeblock.client.item.renderer.*;
import de.takacick.onenukeblock.registry.EntityRegistry;
import de.takacick.onenukeblock.registry.ItemRegistry;
import de.takacick.onenukeblock.registry.ParticleRegistry;
import de.takacick.onenukeblock.registry.ScreenHandlerRegistry;
import de.takacick.onenukeblock.registry.block.entity.renderer.BladedTntBlockEntityRenderer;
import de.takacick.onenukeblock.registry.block.entity.renderer.NukeOneBlockBlockEntityRenderer;
import de.takacick.onenukeblock.registry.block.entity.renderer.SkylandTntBlockEntityRenderer;
import de.takacick.onenukeblock.registry.entity.custom.renderer.BladedTntEntityRenderer;
import de.takacick.onenukeblock.registry.entity.custom.renderer.SkylandTntEntityRenderer;
import de.takacick.onenukeblock.registry.entity.living.renderer.CreeperScientistEntityRenderer;
import de.takacick.onenukeblock.registry.entity.living.renderer.HazmatVillagerEntityRenderer;
import de.takacick.onenukeblock.registry.entity.living.renderer.MutatedCreeperEntityRenderer;
import de.takacick.onenukeblock.registry.entity.living.renderer.ProtoEntityRenderer;
import de.takacick.onenukeblock.registry.entity.projectiles.renderer.AbstractBlockEntityRenderer;
import de.takacick.onenukeblock.registry.entity.projectiles.renderer.CustomBlockEntityRenderer;
import de.takacick.onenukeblock.registry.entity.projectiles.renderer.DiamondSwordEntityRenderer;
import de.takacick.onenukeblock.registry.inventory.NukeOneBlockScreen;
import de.takacick.onenukeblock.registry.particles.BloodLeakParticle;
import de.takacick.onenukeblock.registry.particles.BloodSplashParticle;
import de.takacick.onenukeblock.registry.particles.SmokeParticle;
import de.takacick.onenukeblock.registry.particles.goop.GoopDropParticle;
import de.takacick.onenukeblock.registry.particles.goop.GoopDropParticleEffect;
import de.takacick.onenukeblock.registry.particles.goop.GoopParticle;
import de.takacick.onenukeblock.registry.particles.goop.GoopStringParticle;
import de.takacick.onenukeblock.registry.particles.water.*;
import de.takacick.onenukeblock.utils.explosion.BangMaceBoostExplosion;
import de.takacick.onenukeblock.utils.explosion.BangMaceExplosion;
import de.takacick.onenukeblock.utils.network.BangMaceBoostExplosionPacket;
import de.takacick.onenukeblock.utils.network.BangMaceExplosionPacket;
import de.takacick.onenukeblock.utils.network.UseNukeBlockPacket;
import de.takacick.utils.bossbar.client.render.BossBarRenderers;
import de.takacick.utils.common.event.entity.client.ClientEntityEvent;
import de.takacick.utils.common.event.world.client.ClientWorldEvent;
import de.takacick.utils.item.client.ItemRenderers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.ParticleUtil;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;

public class OneNukeBlockClient implements ClientModInitializer {

    private KeyBinding nukeOneBlockOpen;
    private Boolean nukeOneBlockOpenBoolean = false;

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityRegistry.SKYLAND_TNT, SkylandTntEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.PLACING_BLOCK, AbstractBlockEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.BLADED_TNT, BladedTntEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.DIAMOND_SWORD, DiamondSwordEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.FALLING_BLOCK, CustomBlockEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.PROTO, ProtoEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.HAZMAT_VILLAGER, HazmatVillagerEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.CREEPER_SCIENTIST, CreeperScientistEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.MUTATED_CREEPER, MutatedCreeperEntityRenderer::new);
        BlockEntityRendererFactories.register(EntityRegistry.NUKE_ONE_BLOCK, NukeOneBlockBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(EntityRegistry.SKYLAND_TNT_BLOCK_ENTITY, SkylandTntBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(EntityRegistry.BLADED_TNT_BLOCK_ENTITY, BladedTntBlockEntityRenderer::new);

        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP, GoopParticle.GoopParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP_DROP, GoopDropParticle.GoopDropParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP_STRING, GoopStringParticle.GoopStringParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.FALLING_BLOOD, BloodLeakParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.BLOOD_SPLASH, BloodSplashParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.SMOKE, SmokeParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.NUCLEAR_BUBBLE, NuclearWaterBubbleParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.NUCLEAR_BUBBLE_COLUMN_UP, NuclearBubbleColumnUpParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.NUCLEAR_BUBBLE_POP, NuclearBubblePopParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.NUCLEAR_SPLASH, NuclearWaterSplashParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.NUCLEAR_CURRENT_DOWN, NuclearCurrentDownParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.NUCLEAR_UNDERWATER, NuclearWaterSuspendParticle.Factory::new);

        ItemRenderers.register(ItemRegistry.ONE_NUKE_BLOCK_ITEM, OneNukeBlockItemRenderer::new);
        ItemRenderers.register(ItemRegistry.KABOOM_MINER, KaboomMinerItemRenderer::new);
        ItemRenderers.register(ItemRegistry.SKYLAND_TNT_ITEM, SkylandTntBlockItemRenderer::new);
        ItemRenderers.register(ItemRegistry.BLADED_TNT_ITEM, BladedTntBlockItemRenderer::new);
        ItemRenderers.register(ItemRegistry.EXPLOSIVE_GUMMY_BEAR, ExplosiveGummyBearItemRenderer::new);
        ItemRenderers.register(ItemRegistry.BANG_MACE, BangMaceItemRenderer::new);
        ItemRenderers.register(ItemRegistry.DIAMOND_HAZMAT_HELMET, DiamondHazmatArmorItemRenderer::createHelmet);
        ItemRenderers.register(ItemRegistry.DIAMOND_HAZMAT_CHESTPLATE, DiamondHazmatArmorItemRenderer::createChestplate);
        ItemRenderers.register(ItemRegistry.DIAMOND_HAZMAT_LEGGINGS, DiamondHazmatArmorItemRenderer::createLeggings);
        ItemRenderers.register(ItemRegistry.DIAMOND_HAZMAT_BOOTS, DiamondHazmatArmorItemRenderer::createBoots);

        BossBarRenderers.register(ParticleRegistry.NUKE_BOSSBAR, NukeBossBarRenderer::new);
        HandledScreens.register(ScreenHandlerRegistry.NUKE_ONE_BLOCK, NukeOneBlockScreen::new);

        try {
            this.nukeOneBlockOpen = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                    "key." + OneNukeBlock.MOD_ID + ".nuke_one_block_open",
                    InputUtil.Type.KEYSYM,
                    GLFW.GLFW_KEY_X,
                    "category." + OneNukeBlock.MOD_ID
            ));
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                PlayerEntity playerEntity = client.player;

                if (this.nukeOneBlockOpen.isPressed() && !this.nukeOneBlockOpenBoolean) {
                    this.nukeOneBlockOpenBoolean = true;
                    if (client.crosshairTarget instanceof BlockHitResult blockHitResult && !blockHitResult.getType().equals(HitResult.Type.MISS)) {
                        if (playerEntity.getWorld().getBlockState(blockHitResult.getBlockPos()).isOf(ItemRegistry.ONE_NUKE_BLOCK)) {
                            ClientPlayNetworking.send(new UseNukeBlockPacket(blockHitResult.getBlockPos()));
                        }
                    }
                } else {
                    this.nukeOneBlockOpenBoolean = this.nukeOneBlockOpen.isPressed();
                }
            });
        } catch (RuntimeException exception) {

        }

        ClientEntityEvent.register(OneNukeBlock.IDENTIFIER, (client, event, context) -> {
            if (client.player == null || client.world == null) {
                return;
            }

            ClientPlayerEntity playerEntity = client.player;
            ClientWorld world = client.world;

            int entityId = event.entityId();
            int status = event.event();
            int data = event.data();
            Entity entity = world.getEntityById(entityId);

            Random random = Random.create();
            client.execute(() -> {
                if (entity != null) {
                    if (status == 1) {
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.BLOCKS,
                                4.0f, (1.0f + (random.nextFloat() - random.nextFloat()) * 0.2f) * 0.7f, true);

                        NukeParticleUtil.spawnSmashAttackParticles(world, entity.getSteppingPos(), 15, 0.75);
                        NukeParticleUtil.spawnSmashAttackParticles(world, entity.getSteppingPos(), 15, 1.5);
                        NukeParticleUtil.spawnSmashAttackParticles(world, entity.getSteppingPos(), 15, 2.25);
                        NukeParticleUtil.spawnSmashAttackParticles(world, entity.getSteppingPos(), 15, 3);
                        world.addParticle(ParticleTypes.EXPLOSION_EMITTER, entity.getX(), entity.getBodyY(0.5), entity.getZ(), 1.0, 0.0, 0.0);
                    } else if (status == 2) {
                        Vec3d rot = entity.getRotationVector();
                        Vec3d pos = entity.getPos()
                                .add(0, entity.getHeight() * 0.25, 0)
                                .add(rot.multiply(0.375 * 1.66666666667).multiply(entity.getWidth()));

                        world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_MACE_SMASH_AIR, SoundCategory.BLOCKS,
                                2.0f, 1f, true);

                        NukeParticleUtil.spawnSmashAttackParticles(world, entity.getSteppingPos(), 25, 0.75);
                        NukeParticleUtil.spawnSmashAttackParticles(world, entity.getSteppingPos(), 25, 1.5);
                        NukeParticleUtil.spawnSmashAttackParticles(world, entity.getSteppingPos(), 25, 2.25);
                        NukeParticleUtil.spawnSmashAttackParticles(world, entity.getSteppingPos(), 25, 3);
                    } else if (status == 3) {
                        double g = entity.getX();
                        double j = entity.getZ();

                        for (int i = 0; i < 10; ++i) {
                            double d = random.nextGaussian() * 0.2;
                            double e = random.nextDouble() * 0.1;
                            double f = random.nextGaussian() * 0.2;
                            double h = entity.getBodyY(0.5 + e);

                            world.addParticle(ParticleRegistry.BLOOD_SPLASH,
                                    true, g + d, h + e, j + f, d * 3, e + 0.3, f * 3);
                        }
                        for (int i = 0; i < 3; ++i) {
                            double d = random.nextGaussian() * 0.4;
                            double e = random.nextDouble() * 0.6;
                            double f = random.nextGaussian() * 0.4;
                            double h = entity.getBodyY(0.6 + e);

                            world.addParticle(new GoopDropParticleEffect(Vec3d.unpackRgb(0x820A0A).toVector3f(), (0.6f + (float) (random.nextDouble() * 0.4)) * 0.25f),
                                    true, g + d, h, j + f,
                                    d * 0.3, e * 0.3, f * 0.3
                            );
                        }
                        world.playSound(g, entity.getBodyY(0.5), j, ParticleRegistry.BLOOD_DROP, entity.getSoundCategory(), 0.6f, 1f, true);
                    } else if (status == 4) {
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.BLOCKS,
                                6.0f, (1.0f + (random.nextFloat() - random.nextFloat()) * 0.2f) * 0.7f, true);

                        NukeParticleUtil.spawnSmashParticles(world, entity.getPos().add(0, entity.getHeight() * 0.5, 0), 50, 0.75, Blocks.DIRT.getDefaultState());
                        world.addParticle(ParticleTypes.EXPLOSION_EMITTER, entity.getX(), entity.getBodyY(0.5), entity.getZ(), 1.0, 0.0, 0.0);
                    } else if (status == 5) {
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.AMBIENT, 2.0f, 1f, true);
                    } else if (status == 6) {
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.BLOCKS,
                                0.8f, (1.0f + (random.nextFloat() - random.nextFloat()) * 0.2f) * 0.7f, true);

                        NukeParticleUtil.spawnSmashAttackParticles(world, entity.getSteppingPos(), 50, 0.75);
                        double g = entity.getX();
                        double j = entity.getZ();

                        for (int i = 0; i < 2; ++i) {
                            double d = random.nextGaussian() * 0.4;
                            double e = random.nextGaussian() * 0.4;
                            double f = random.nextGaussian() * 0.4;

                            world.addImportantParticle(ParticleTypes.EXPLOSION,
                                    true, g + d, entity.getBodyY(0.5) + e, j + f, 0.5, 0, 0);
                        }
                    } else if (status == 7) {
                        Vec3d rotation = entity.getRotationVector(0, entity.getYaw());

                        Vec3d pos = entity.getPos().add(0, entity.getHeight() * 0.5, 0).add(rotation.multiply(-entity.getWidth()));
                        world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.BLOCKS,
                                0.1f, (1.2f + random.nextFloat() * 0.5f), true);

                        NukeParticleUtil.spawnSmashAttackParticles(world, entity.getSteppingPos(), 50, 0.75);

                        world.addImportantParticle(ParticleTypes.EXPLOSION,
                                true, pos.getX(), pos.getY(), pos.getZ(), 0.1, 0, 0);
                    } else if (status == 8) {
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.BLOCKS,
                                6.0f, (1.0f + (random.nextFloat() - random.nextFloat()) * 0.2f) * 0.7f, true);

                        NukeParticleUtil.spawnSmashAttackParticles(world, entity.getSteppingPos(), 25, 0.75);
                        NukeParticleUtil.spawnSmashAttackParticles(world, entity.getSteppingPos(), 25, 1.5);
                        NukeParticleUtil.spawnSmashAttackParticles(world, entity.getSteppingPos(), 25, 2.25);
                        NukeParticleUtil.spawnSmashAttackParticles(world, entity.getSteppingPos(), 25, 3);

                        double g = entity.getX();
                        double j = entity.getZ();
                        for (int i = 0; i < 5; ++i) {
                            double d = random.nextGaussian() * 0.8;
                            double e = random.nextGaussian() * 0.8;
                            double f = random.nextGaussian() * 0.8;

                            world.addImportantParticle(ParticleTypes.EXPLOSION,
                                    true, g + d, entity.getBodyY(0.5) + e, j + f, 0.5, 0, 0);
                        }
                    } else if (status == 9) {
                        BlockState blockState = Block.getStateFromRawId(data);
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE.value(),
                                SoundCategory.BLOCKS,
                                8.0f, (1.0f + (random.nextFloat() - random.nextFloat()) * 0.2f) * 0.7f, false);
                        for (int i = 0; i < 6; i++) {
                            NukeParticleUtil.spawnSmashAttackParticles(world, entity.getPos(), blockState, 25, 1 + i);
                        }
                    }
                }
            });
        });

        ClientWorldEvent.register(OneNukeBlock.IDENTIFIER, (client, event, context) -> {
            if (client.player == null || client.world == null) {
                return;
            }

            ClientPlayerEntity playerEntity = client.player;
            ClientWorld world = client.world;

            Vec3d pos = event.pos();
            BlockPos blockPos = BlockPos.ofFloored(pos);
            int status = event.event();
            int data = event.data();

            Random random = Random.create();

            client.execute(() -> {
                if (status == 1) {
                    ParticleUtil.spawnSmashAttackParticles(world, blockPos, data);
                } else if (status == 2) {
                    double g = pos.getX();
                    double h = pos.getY();
                    double j = pos.getZ();

                    for (int i = 0; i < 3; ++i) {
                        double d = random.nextGaussian() * 0.4;
                        double e = random.nextGaussian() * 0.4;
                        double f = random.nextGaussian() * 0.4;

                        world.addImportantParticle(ParticleTypes.EXPLOSION,
                                true, g + d, h + e, j + f, 0.5, 0, 0);
                    }

                    world.playSound(g, h, j, SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.AMBIENT, 1f, 1f, false);

                    BlockState blockState = Block.getStateFromRawId(data);

                    NukeParticleUtil.addBlockBreakParticles(blockPos, blockState);
                }
            });
        });

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            return 0xFF52DC;
        }, ItemRegistry.ONE_NUKE_BLOCK);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            return 0xFF52DC;
        }, ItemRegistry.ONE_NUKE_BLOCK_ITEM);

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            var map = new HashMap<>(ItemParticleManager.getItemParticleHelperList());
            map.forEach((itemStack, itemParticleHelper) -> {
                if (itemParticleHelper.shouldRemove()) {
                    ItemParticleManager.getItemParticleHelperList().remove(itemStack);
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(BangMaceBoostExplosionPacket.PACKET_ID, (payload, context) -> {
            MinecraftClient client = context.client();
            client.execute(() -> {
                BangMaceBoostExplosion explosion = new BangMaceBoostExplosion(client.world, null, payload.getX(), payload.getY(), payload.getZ(), payload.getRadius(), payload.getAffectedBlocks(), payload.getDestructionType(), payload.getParticle(), payload.getEmitterParticle(), payload.getSoundEvent());
                explosion.affectWorld(true);
                client.player.setVelocity(client.player.getVelocity().add(payload.getPlayerVelocityX(), payload.getPlayerVelocityY(), payload.getPlayerVelocityZ()));
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(BangMaceExplosionPacket.PACKET_ID, (payload, context) -> {
            MinecraftClient client = context.client();
            client.execute(() -> {
                BangMaceExplosion explosion = new BangMaceExplosion(client.world, null, payload.getX(), payload.getY(), payload.getZ(), payload.getRadius(), payload.getAffectedBlocks(), payload.getDestructionType(), payload.getParticle(), payload.getEmitterParticle(), payload.getSoundEvent());
                explosion.affectWorld(true);
                client.player.setVelocity(client.player.getVelocity().add(payload.getPlayerVelocityX(), payload.getPlayerVelocityY(), payload.getPlayerVelocityZ()));
            });
        });

        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), ItemRegistry.STILL_NUCLEAR_WATER, ItemRegistry.FLOWING_NUCLEAR_WATER);

        FluidRenderHandlerRegistry.INSTANCE.register(ItemRegistry.STILL_NUCLEAR_WATER, ItemRegistry.FLOWING_NUCLEAR_WATER, new SimpleFluidRenderHandler(
                Identifier.of(OneNukeBlock.MOD_ID, "block/nuclear_water_still"),
                Identifier.of(OneNukeBlock.MOD_ID, "block/nuclear_water_flow")
        ));
    }
}
