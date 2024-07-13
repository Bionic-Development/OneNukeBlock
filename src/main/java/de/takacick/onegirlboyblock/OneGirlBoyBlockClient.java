package de.takacick.onegirlboyblock;

import de.takacick.onegirlboyblock.client.item.ItemParticleManager;
import de.takacick.onegirlboyblock.client.item.renderer.*;
import de.takacick.onegirlboyblock.registry.EntityRegistry;
import de.takacick.onegirlboyblock.registry.ItemRegistry;
import de.takacick.onegirlboyblock.registry.ParticleRegistry;
import de.takacick.onegirlboyblock.registry.entity.living.renderer.TurboBoardEntityRenderer;
import de.takacick.onegirlboyblock.registry.entity.projectiles.TetrisEntity;
import de.takacick.onegirlboyblock.registry.entity.projectiles.renderer.TetrisEntityRenderer;
import de.takacick.onegirlboyblock.registry.particles.*;
import de.takacick.onegirlboyblock.utils.data.ItemDataComponents;
import de.takacick.onegirlboyblock.utils.data.item.BitCannonItemHelper;
import de.takacick.utils.common.event.entity.client.ClientEntityEvent;
import de.takacick.utils.common.event.world.client.ClientWorldEvent;
import de.takacick.utils.item.client.ItemRenderers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EmptyEntityRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

import java.util.HashMap;

public class OneGirlBoyBlockClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityRegistry.TURBO_BOARD, TurboBoardEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.TETRIS, TetrisEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.FLAME, EmptyEntityRenderer::new);

        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOLD_NUGGET, new GoldNuggetParticle.Factory());
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.SUGAR, new SugarParticle.Factory());
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.SHRINK, ShrinkParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GROW, GrowParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GIRL_SIZE, GirlSizeParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GLITTER, GlitterParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GLITTER_SWEEP, GlitterSweepParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GLITTER_BLADE, GlitterBladeParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GIRL_FLASH, GirlFlashParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.STAR_GLITTER, StarGlitterParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.STAR_MINER_GLITTER, StarMinerGlitterParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.TETRIS, new TetrisParticle.Factory());
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.TETRIS_FLASH, TetrisFlashParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.TETRIS_GLITTER, TetrisGlitterParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.BUTTERFLY_WINGS_GLITTER, ButterflyWingsGlitterParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.BUTTERFLY_GLITTER, ButterflyGlitterParticle.Factory::new);

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ItemRegistry.ONE_GIRL_BLOCK, ItemRegistry.ONE_BOY_BLOCK);

        ItemRenderers.register(ItemRegistry.GOLDEN_MEAT, GoldenMeatItemRenderer::new);
        ItemRenderers.register(ItemRegistry.BASEBALL_BAT, BaseballBatItemRenderer::new);
        ItemRenderers.register(ItemRegistry.FOOTBALL_GEAR, FootballGearItemRenderer::new);
        ItemRenderers.register(ItemRegistry.GLITTER_BLADE, GlitterBladeItemRenderer::new);
        ItemRenderers.register(ItemRegistry.TIARA, TiaraItemRenderer::new);
        ItemRenderers.register(ItemRegistry.STRAWBERRY_SHORTCAKE, StrawberryShortcakeItemRenderer::new);
        ItemRenderers.register(ItemRegistry.STAR_MINER, StarMinerItemRenderer::new);
        ItemRenderers.register(ItemRegistry.INFERNO_HAIR_DRYER, InfernoHairDryerItemRenderer::new);
        ItemRenderers.register(ItemRegistry.BIT_CANNON, BitCannonItemRenderer::new);
        ItemRenderers.register(ItemRegistry.TURBO_BOARD, TurboBoardItemRenderer::new);
        ItemRenderers.register(ItemRegistry.BUTTERFLY_WINGS, ButterflyWingsItemRenderer::new);

        ClientEntityEvent.register(OneGirlBoyBlock.IDENTIFIER, (client, event, context) -> {
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
                        double g = entity.getX();
                        double j = entity.getZ();

                        for (int i = 0; i < 10; ++i) {
                            double h = entity.getRandomBodyY();

                            double d = random.nextGaussian() * 0.3;
                            double e = random.nextGaussian() * 0.3;
                            double f = random.nextGaussian() * 0.3;

                            world.addImportantParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, ItemRegistry.BASEBALL_BAT.getDefaultStack()),
                                    true, g + d, h + e, j + f, d, e * 2, f);
                        }
                        for (int i = 0; i < 10; ++i) {
                            double h = entity.getRandomBodyY();

                            double d = random.nextGaussian() * 0.3;
                            double e = random.nextDouble() * 0.3;
                            double f = random.nextGaussian() * 0.3;

                            world.addImportantParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, ItemRegistry.BASEBALL_BAT.getDefaultStack()),
                                    true, g + d, h + e, j + f, d * 0.2, e + 0.5, f * 0.2);
                        }

                        world.playSound(g, entity.getBodyY(0.5), j, SoundEvents.BLOCK_WOOD_BREAK, SoundCategory.AMBIENT, 1f, 1.5f, true);
                    } else if (status == 2) {
                        double g = entity.getX();
                        double j = entity.getZ();

                        for (int i = 0; i < 16; ++i) {
                            double h = entity.getRandomBodyY();

                            double d = random.nextGaussian() * 0.3;
                            double e = random.nextGaussian() * 0.3;
                            double f = random.nextGaussian() * 0.3;

                            world.addImportantParticle(ParticleRegistry.GLITTER,
                                    true, g + d, h + e, j + f, d, e * 2, f);
                        }
                        for (int i = 0; i < 35; ++i) {
                            double h = entity.getRandomBodyY();

                            double d = random.nextGaussian() * 0.3;
                            double e = random.nextDouble() * 0.3;
                            double f = random.nextGaussian() * 0.3;

                            world.addImportantParticle(ParticleRegistry.GLITTER,
                                    true, g + d, h + e, j + f, d * 0.4, e + 0.8, f * 0.4);
                        }

                        world.addImportantParticle(ParticleRegistry.GIRL_FLASH,
                                true, g, entity.getBodyY(0.5), j, 0.4, 0, 0);
                        world.playSound(g, entity.getBodyY(0.5), j, SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK, SoundCategory.AMBIENT, 2f, 1.5f, true);
                    } else if (status == 3) {
                        Vec3d rot = entity.getRotationVector();

                        double d = -MathHelper.sin(entity.getYaw() * ((float) Math.PI / 180));
                        double e = MathHelper.cos(entity.getYaw() * ((float) Math.PI / 180));
                        world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, entity.getSoundCategory(), 1.0f, 1.0f, true);
                        world.addParticle(ParticleRegistry.GLITTER_SWEEP,
                                true, entity.getX() + d, entity.getBodyY(0.5), entity.getZ() + e, d, rot.getX(), rot.getZ());
                    } else if (status == 4) {
                        int handValue = (data >> 4) & 0x1;
                        int tetrisVariant = data & 0xF;

                        Hand hand = handValue == 0 ? Hand.MAIN_HAND : Hand.OFF_HAND;
                        if (entity instanceof LivingEntity livingEntity) {
                            ItemStack itemStack = livingEntity.getStackInHand(hand);
                            BitCannonItemHelper helper = itemStack.get(ItemDataComponents.BIT_CANNON_HELPER);
                            if (helper != null) {
                                helper.setVariant(TetrisEntity.Variant.byId(tetrisVariant));
                                helper.setTick(0);
                                helper.setPrevTick(0);
                            }
                        }
                    } else if (status == 5) {
                        double g = entity.getX();
                        double j = entity.getZ();

                        int tetrisVariant = -1;
                        if (entity instanceof TetrisEntity tetris) {
                            tetrisVariant = tetris.getVariant().getId();
                        }

                        for (int i = 0; i < 20; ++i) {
                            double h = entity.getBodyY(0.5);

                            double d = random.nextGaussian() * 0.3;
                            double e = random.nextDouble() * 0.3;
                            double f = random.nextGaussian() * 0.3;

                            world.addImportantParticle(new TetrisParticleEffect(ParticleRegistry.TETRIS, tetrisVariant),
                                    true, g + d, h + e, j + f, d * 2f, e * 2f, f * 2f);
                        }
                        for (int i = 0; i < 35; ++i) {
                            double h = entity.getBodyY(0.5);

                            double d = random.nextGaussian() * 0.3;
                            double e = random.nextDouble() * 0.3;
                            double f = random.nextGaussian() * 0.3;

                            world.addImportantParticle(new TetrisParticleEffect(ParticleRegistry.TETRIS_GLITTER, tetrisVariant),
                                    true, g + d, h + e, j + f, d * 0.4, e * 0.4, f * 0.4);
                        }

                        world.addImportantParticle(new TetrisParticleEffect(ParticleRegistry.TETRIS_FLASH, tetrisVariant),
                                true, g, entity.getBodyY(0.5), j, 0.5, 0, 0);
                        world.playSound(g, entity.getBodyY(0.5), j, ParticleRegistry.BIT_EXPLOSION, SoundCategory.AMBIENT, 4f, 1 + random.nextFloat() * 0.2f, false);
                    } else if (status == 6) {
                        double g = entity.getX();
                        double j = entity.getZ();

                        int tetrisVariant = -1;

                        for (int i = 0; i < 5; ++i) {
                            double e = random.nextGaussian();
                            double h = entity.getBodyY(0.5 + e * 0.5);

                            e *= 0.3;
                            double d = random.nextGaussian() * 0.3;
                            double f = random.nextGaussian() * 0.3;

                            world.addImportantParticle(new TetrisParticleEffect(ParticleRegistry.TETRIS, tetrisVariant),
                                    true, g + d, h, j + f, d * 2f, e * 2f, f * 2f);
                        }
                        for (int i = 0; i < 2; ++i) {
                            double e = random.nextGaussian();
                            double h = entity.getBodyY(0.5 + e * 0.5);

                            e *= 0.3;
                            double d = random.nextGaussian() * 0.3;
                            double f = random.nextGaussian() * 0.3;

                            world.addImportantParticle(new TetrisParticleEffect(ParticleRegistry.TETRIS_GLITTER, tetrisVariant),
                                    true, g + d, h, j + f, d * 0.4, e * 0.4, f * 0.4);
                        }
                    } else if (status == 7) {
                        double g = entity.getX();
                        double j = entity.getZ();

                        Vec3d rotation = entity.getRotationVector();

                        for (int i = 0; i < 35; ++i) {
                            double h = entity.getRandomBodyY();

                            double d = random.nextGaussian() * 0.3;
                            double e = random.nextGaussian() * 0.3;
                            double f = random.nextGaussian() * 0.3;

                            world.addImportantParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, ItemRegistry.TURBO_BOARD.getDefaultStack()),
                                    true, g + (rotation.getX() * random.nextGaussian()), h, j + (rotation.getZ() * random.nextGaussian()), d * 0.2, e * 0.2, f * 0.2);
                        }
                        world.playSound(g, entity.getBodyY(0.5), j, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.AMBIENT, 1f, 1 + random.nextFloat() * 0.2f, true);
                    }
                }
            });
        });

        ClientWorldEvent.register(OneGirlBoyBlock.IDENTIFIER, (client, event, context) -> {
            if (client.player == null || client.world == null) {
                return;
            }

            ClientPlayerEntity playerEntity = client.player;
            ClientWorld world = client.world;

            Vec3d pos = event.pos();
            int status = event.event();
            int data = event.data();

            Random random = Random.create();

            client.execute(() -> {
                if (status == 1) {
                    for (int i = 0; i < 1 + random.nextBetween(1, 2); ++i) {
                        Vec3d vel = new Vec3d(random.nextGaussian(), random.nextGaussian(), random.nextGaussian()).normalize().multiply(0.4 + world.getRandom().nextDouble() * 0.2);

                        world.addParticle(ParticleRegistry.STAR_GLITTER, true, pos.getX() + vel.getX(), pos.getY() + vel.getY(), pos.getZ() + vel.getZ(),
                                vel.getX() * 0.45, vel.getY() * 0.45, vel.getZ() * 0.45);
                    }
                } else if (status == 2) {
                    world.addBlockBreakParticles(BlockPos.ofFloored(pos), Blocks.ANVIL.getDefaultState());
                    world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_IRON_GOLEM_HURT, SoundCategory.BLOCKS, 1.0f, 1.0f, true);
                    world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.BLOCKS, 1.0f, 1.5f, true);
                }
            });
        });

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            return 0x2B9BFF;
        }, ItemRegistry.ONE_BOY_BLOCK);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            return 0x2B9BFF;
        }, ItemRegistry.ONE_BOY_BLOCK_ITEM);

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            return 0xFF52DC;
        }, ItemRegistry.ONE_GIRL_BLOCK);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            return 0xFF52DC;
        }, ItemRegistry.ONE_GIRL_BLOCK_ITEM);

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            var map = new HashMap<>(ItemParticleManager.getItemParticleHelperList());
            map.forEach((itemStack, itemParticleHelper) -> {
                if (itemParticleHelper.shouldRemove()) {
                    ItemParticleManager.getItemParticleHelperList().remove(itemStack);
                }
            });
        });
    }
}
