package de.takacick.onegirlfriendblock;

import com.google.common.collect.Lists;
import de.takacick.onegirlfriendblock.access.PlayerProperties;
import de.takacick.onegirlfriendblock.registry.EntityRegistry;
import de.takacick.onegirlfriendblock.registry.ItemRegistry;
import de.takacick.onegirlfriendblock.registry.ParticleRegistry;
import de.takacick.onegirlfriendblock.registry.entity.living.BuffChadVillagerEntity;
import de.takacick.onegirlfriendblock.registry.entity.living.SimpEntity;
import de.takacick.onegirlfriendblock.registry.entity.living.ZukoEntity;
import de.takacick.onegirlfriendblock.registry.entity.living.renderer.*;
import de.takacick.onegirlfriendblock.registry.particles.*;
import de.takacick.onegirlfriendblock.registry.particles.goop.GoopDropParticle;
import de.takacick.onegirlfriendblock.registry.particles.goop.GoopParticle;
import de.takacick.onegirlfriendblock.registry.particles.goop.GoopStringParticle;
import de.takacick.utils.BionicUtilsClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class OneGirlfriendBlockClient implements ClientModInitializer {

    private KeyBinding girlfriendBlockYoink;
    private Boolean girlfriendBlockYoinkBoolean = false;
    private KeyBinding girlfriendScaryYoink;
    private Boolean girlfriendScaryYoinkBoolean = false;

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityRegistry.GIRLFRIEND, GirlfriendEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.BUFF_CHAD_VILLAGER, BuffChadVillagerEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.SIMP, SimpEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.ZUKO_HUMANOID, ZukoHumanoidEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.ZUKO, ZukoEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.SIMP_YOINK, SimpYoinkEntityRenderer::new);

        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP, GoopParticle.GoopParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP_DROP, GoopDropParticle.GoopDropParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP_STRING, GoopStringParticle.GoopStringParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.FALLING_BLOOD, BloodLeakParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.BLOOD_SPLASH, BloodSplashParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.COLORED_DUST, ColoredDustParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.COLORED_FIREWORK, ColoredFireworksSparkParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.COLORED_BUBBLE_DUST, ColoredBubbleDustParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.HEART, HeartParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.LIPSTICK_SWEEP, LipstickSweepParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.COLORED_POOF, ColoredPoofParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.LIPSTICK, new LipstickParticle.Factory());
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.FEATHER, FeatherParticle.Factory::new);

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ItemRegistry.GIRLFRIEND_ONE_BLOCK);

        try {
            this.girlfriendBlockYoink = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                    "key." + OneGirlfriendBlock.MOD_ID + ".girlfriend_block_yoink",
                    InputUtil.Type.KEYSYM,
                    GLFW.GLFW_KEY_X,
                    "category." + OneGirlfriendBlock.MOD_ID
            ));
            this.girlfriendScaryYoink = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                    "key." + OneGirlfriendBlock.MOD_ID + ".girlfriend_scary_yoink",
                    InputUtil.Type.KEYSYM,
                    GLFW.GLFW_KEY_X,
                    "category." + OneGirlfriendBlock.MOD_ID
            ));
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                PlayerEntity playerEntity = client.player;
                if (!(playerEntity instanceof PlayerProperties playerProperties)) {
                    return;
                }

                if (this.girlfriendBlockYoink.isPressed() && !this.girlfriendBlockYoinkBoolean) {
                    this.girlfriendBlockYoinkBoolean = true;
                    if (client.crosshairTarget instanceof BlockHitResult blockHitResult && !blockHitResult.getType().equals(HitResult.Type.MISS)) {

                        if (playerProperties.hasOneGirlfriendBlock()) {
                            PacketByteBuf buf = PacketByteBufs.create();
                            buf.writeBlockPos(blockHitResult.getBlockPos());
                            buf.writeInt(blockHitResult.getSide().getId());
                            ClientPlayNetworking.send(new Identifier(OneGirlfriendBlock.MOD_ID, "girlfriendblockyoink"), buf);
                        } else if (playerEntity.getWorld().getBlockState(blockHitResult.getBlockPos()).isOf(ItemRegistry.GIRLFRIEND_ONE_BLOCK)) {
                            PacketByteBuf buf = PacketByteBufs.create();
                            buf.writeBlockPos(blockHitResult.getBlockPos());
                            buf.writeInt(blockHitResult.getSide().getId());
                            ClientPlayNetworking.send(new Identifier(OneGirlfriendBlock.MOD_ID, "girlfriendblockyoink"), buf);
                        }
                    }
                } else {
                    this.girlfriendBlockYoinkBoolean = this.girlfriendBlockYoink.isPressed();
                }

                if (this.girlfriendScaryYoink.isPressed() && !this.girlfriendScaryYoinkBoolean) {
                    this.girlfriendScaryYoinkBoolean = true;
                    PacketByteBuf buf = PacketByteBufs.create();
                    ClientPlayNetworking.send(new Identifier(OneGirlfriendBlock.MOD_ID, "girlfriendscaryyoink"), buf);
                } else {
                    this.girlfriendScaryYoinkBoolean = this.girlfriendScaryYoink.isPressed();
                }
            });
        } catch (RuntimeException exception) {

        }

        ClientPlayNetworking.registerGlobalReceiver(OneGirlfriendBlock.IDENTIFIER, (client, handler, buf, responseSender) -> {
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
                        for (int i = 0; i < 8; ++i) {
                            Vec3d vel = new Vec3d(random.nextGaussian(), random.nextDouble(), random.nextGaussian());

                            world.addParticle(ParticleRegistry.HEART, true,
                                    entity.getX() + vel.getX() * 0.4, entity.getRandomBodyY(), entity.getZ() + vel.getZ() * 0.4,
                                    vel.getX() * 0.01, Math.abs(vel.getY()) * 0.03, vel.getZ() * 0.01);
                        }
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, entity.getSoundCategory(), 0.5f, 1f + world.getRandom().nextFloat() * 0.2f, true);
                    } else if (status == 2) {
                        Vec3d rot = entity.getRotationVector();
                        double d = -MathHelper.sin(entity.getYaw() * ((float) Math.PI / 180));
                        double e = MathHelper.cos(entity.getYaw() * ((float) Math.PI / 180));
                        world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, entity.getSoundCategory(), 1.0f, 1.0f, true);
                        world.addParticle(ParticleRegistry.LIPSTICK_SWEEP,
                                true, entity.getX() + d, entity.getBodyY(0.5), entity.getZ() + e, d, rot.getX(), rot.getZ());
                    } else if (status == 3) {
                        double g = entity.getX();
                        double j = entity.getZ();

                        for (int i = 0; i < 3; ++i) {
                            double d = random.nextGaussian() * 0.2;
                            double e = random.nextDouble() * 0.4;
                            double f = random.nextGaussian() * 0.2;

                            world.addParticle(ParticleRegistry.FALLING_BLOOD,
                                    true, g + d, entity.getBodyY(0.5 + e), j + f, d * 0.1, e * 0.1, f * 0.1);
                        }
                    } else if (status == 4) {
                        double d = -MathHelper.sin(entity.getYaw() * ((float) Math.PI / 180));
                        world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, entity.getSoundCategory(), 1.0f, 1.0f, true);
                        world.addParticle(ParticleRegistry.LIPSTICK_SWEEP,
                                true, entity.getX(), entity.getBodyY(0.5), entity.getZ(), d, 0, 0);
                    } else if (status == 5) {
                        double g = entity.getX();
                        double j = entity.getZ();

                        Vector3f color = Vec3d.unpackRgb(0xC61117).toVector3f();

                        for (int i = 0; i < 5; ++i) {
                            double h = entity.getRandomBodyY();
                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_DUST, color), true, g, h, j, world.getRandom().nextGaussian() * 0.7, world.getRandom().nextGaussian() * 0.7, world.getRandom().nextGaussian() * 0.7);
                        }

                        for (int i = 0; i < 12; ++i) {
                            double h = entity.getRandomBodyY();
                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_POOF, color), true, g, h, j, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3);
                        }

                        for (int i = 0; i < 3; ++i) {
                            double h = entity.getRandomBodyY();
                            world.addParticle(ParticleRegistry.LIPSTICK_SWEEP, true, g + random.nextGaussian() * 0.75, h + random.nextGaussian() * 0.75, j + random.nextGaussian() * 0.75, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3);
                        }
                    } else if (status == 6) {
                        double g = entity.getX();
                        double j = entity.getZ();

                        for (int i = 0; i < 8; ++i) {
                            double d = random.nextGaussian() * 0.2;
                            double e = random.nextDouble() * 0.4;
                            double f = random.nextGaussian() * 0.2;

                            world.addParticle(ParticleRegistry.FALLING_BLOOD,
                                    true, g + d, entity.getBodyY(0.5 + e), j + f, d * 0.1, e * 0.1, f * 0.1);
                        }

                        double d = -MathHelper.sin(entity.getYaw() * ((float) Math.PI / 180));
                        world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, entity.getSoundCategory(), 1.0f, 1.0f, true);
                        world.addParticle(ParticleRegistry.LIPSTICK_SWEEP,
                                true, entity.getX(), entity.getBodyY(0.5), entity.getZ(), d, 0, 0);
                    } else if (status == 7) {
                        double g = entity.getX();
                        double j = entity.getZ();

                        ParticleEffect particleEffect = ParticleRegistry.LIPSTICK;

                        for (int i = 0; i < 20; ++i) {
                            double h = entity.getRandomBodyY();

                            double d = random.nextGaussian();
                            double e = random.nextGaussian();
                            double f = random.nextGaussian();

                            world.addParticle(particleEffect,
                                    true,
                                    g + d * 0.3, h + e * 0.3, j + f * 0.3,
                                    d * 0.05, e * 0.05, f * 0.05);
                        }

                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.BLOCK_MUD_PLACE, SoundCategory.PLAYERS, 0.5f, 1.0f + world.getRandom().nextFloat() * 0.2f, true);
                    } else if (status == 8) {
                        double g = entity.getX();
                        double j = entity.getZ();

                        for (int i = 0; i < 7 * entity.getHeight(); ++i) {
                            double h = entity.getRandomBodyY();

                            double d = random.nextGaussian();
                            double e = random.nextGaussian();
                            double f = random.nextGaussian();

                            world.addParticle(ParticleRegistry.FEATHER,
                                    true,
                                    g + d * 0.3, h + e * 0.3, j + f * 0.3,
                                    d * 0.05, e * 0.05, f * 0.05);
                        }

                        for (int i = 0; i < 5; ++i) {
                            double h = entity.getRandomBodyY();

                            double d = random.nextGaussian();
                            double e = random.nextGaussian();
                            double f = random.nextGaussian();

                            world.addParticle(ParticleTypes.POOF,
                                    true,
                                    g + d * 0.5, h + e * 0.5, j + f * 0.5,
                                    0, 0, 0);
                        }

                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_CAT_AMBIENT, SoundCategory.PLAYERS, 0.5f, 1.0f + world.getRandom().nextFloat() * 0.2f, true);
                    } else if (status == 9) {
                        if (entity instanceof SimpEntity simpEntity) {
                            simpEntity.getSimpPleaseState().startIfNotRunning(simpEntity.age);
                        }
                    } else if (status == 10) {
                        if (random.nextDouble() <= 0.4) {
                            for (int i = 0; i < 1; ++i) {
                                Vec3d vel = new Vec3d(random.nextGaussian(), random.nextDouble(), random.nextGaussian());

                                world.addParticle(ParticleRegistry.HEART, true,
                                        entity.getX() + vel.getX() * 0.4, entity.getRandomBodyY(), entity.getZ() + vel.getZ() * 0.4,
                                        vel.getX() * 0.01, Math.abs(vel.getY()) * 0.03, vel.getZ() * 0.01);
                            }
                        } else {
                            for (int i = 0; i < 1; ++i) {
                                Vec3d vel = new Vec3d(random.nextGaussian(), random.nextDouble(), random.nextGaussian());

                                world.addParticle(ParticleTypes.HEART, true,
                                        entity.getX() + vel.getX() * 0.4, entity.getRandomBodyY(), entity.getZ() + vel.getZ() * 0.4,
                                        vel.getX() * 0.01, Math.abs(vel.getY()) * 0.03, vel.getZ() * 0.01);
                            }
                        }
                    } else if (status == 11) {
                        world.playSoundFromEntity(entity, SoundEvents.ENTITY_VILLAGER_WORK_TOOLSMITH, SoundCategory.PLAYERS, 1f, 0.7f + world.getRandom().nextFloat() * 0.2f);
                        world.playSoundFromEntity(entity, SoundEvents.ENTITY_VILLAGER_YES, SoundCategory.PLAYERS, 1f, 0.7f + world.getRandom().nextFloat() * 0.2f);
                        for (int i = 0; i < 5; ++i) {
                            Vec3d vel = new Vec3d(random.nextGaussian(), random.nextDouble(), random.nextGaussian());

                            world.addParticle(ParticleTypes.ELECTRIC_SPARK, true,
                                    entity.getX() + vel.getX() * 0.4, entity.getRandomBodyY(), entity.getZ() + vel.getZ() * 0.4,
                                    vel.getX() * 0.01, Math.abs(vel.getY()) * 0.03, vel.getZ() * 0.01);
                        }
                        for (int i = 0; i < 10; ++i) {
                            Vec3d vel = new Vec3d(random.nextGaussian(), random.nextDouble(), random.nextGaussian());

                            world.addParticle(ParticleTypes.HEART, true,
                                    entity.getX() + vel.getX() * 0.4, entity.getRandomBodyY(), entity.getZ() + vel.getZ() * 0.4,
                                    vel.getX() * 0.01, Math.abs(vel.getY()) * 0.03, vel.getZ() * 0.01);
                        }
                        for (int i = 0; i < 15; ++i) {
                            Vec3d vel = new Vec3d(random.nextGaussian(), random.nextDouble(), random.nextGaussian());

                            world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.IRON_BLOCK.getDefaultState()), true,
                                    entity.getX() + vel.getX() * 0.4, entity.getRandomBodyY(), entity.getZ() + vel.getZ() * 0.4,
                                    vel.getX() * 0.01, Math.abs(vel.getY()) * 0.03, vel.getZ() * 0.01);
                        }
                    } else if (status >= 12 && status <= 15) {
                        world.playSoundFromEntity(entity, SoundEvents.ENTITY_VILLAGER_YES, SoundCategory.PLAYERS, 1f, 0.5f + world.getRandom().nextFloat() * 0.2f);
                        if (entity instanceof BuffChadVillagerEntity buffChadVillagerEntity) {
                            AnimationState animationState = switch (status - 12) {
                                case 0 -> buffChadVillagerEntity.getChestFlexingState();
                                case 1 -> buffChadVillagerEntity.getLeftArmFlexingState();
                                case 2 -> buffChadVillagerEntity.getRightArmFlexingState();
                                default -> buffChadVillagerEntity.getBodyFlexingState();
                            };
                            animationState.start(buffChadVillagerEntity.age);
                        }
                    } else if (status == 16) {
                        if (entity instanceof BuffChadVillagerEntity buffChadVillagerEntity) {
                            AnimationState animationState = buffChadVillagerEntity.getBodyFlexingState();
                            animationState.start(buffChadVillagerEntity.age);
                        }

                        world.playSoundFromEntity(entity, SoundEvents.ENTITY_VILLAGER_WORK_TOOLSMITH, SoundCategory.PLAYERS, 1f, 0.7f + world.getRandom().nextFloat() * 0.2f);
                        world.playSoundFromEntity(entity, SoundEvents.ENTITY_VILLAGER_NO, SoundCategory.PLAYERS, 1f, 0.5f + world.getRandom().nextFloat() * 0.2f);
                        for (int i = 0; i < 20; ++i) {
                            Vec3d vel = new Vec3d(random.nextGaussian(), random.nextDouble(), random.nextGaussian());

                            world.addParticle(ParticleTypes.ANGRY_VILLAGER, true,
                                    entity.getX() + vel.getX() * 0.4, entity.getRandomBodyY(), entity.getZ() + vel.getZ() * 0.4,
                                    vel.getX() * 0.01, Math.abs(vel.getY()) * 0.03, vel.getZ() * 0.01);
                        }
                        for (int i = 0; i < 10; ++i) {
                            Vec3d vel = new Vec3d(random.nextGaussian(), random.nextDouble(), random.nextGaussian());

                            world.addParticle(ParticleTypes.CRIT, true,
                                    entity.getX() + vel.getX() * 0.4, entity.getRandomBodyY(), entity.getZ() + vel.getZ() * 0.4,
                                    vel.getX() * 0.01, Math.abs(vel.getY()) * 0.03, vel.getZ() * 0.01);
                        }
                    } else if (status == 17) {
                        if (random.nextDouble() <= 0.4) {
                            for (int i = 0; i < 1; ++i) {
                                Vec3d vel = new Vec3d(random.nextGaussian(), random.nextDouble(), random.nextGaussian());

                                world.addParticle(ParticleTypes.ANGRY_VILLAGER, true,
                                        entity.getX() + vel.getX() * 0.4, entity.getRandomBodyY(), entity.getZ() + vel.getZ() * 0.4,
                                        vel.getX() * 0.01, Math.abs(vel.getY()) * 0.03, vel.getZ() * 0.01);
                            }
                        }
                    } else if (status == 18) {
                        double d = -MathHelper.sin(entity.getYaw() * ((float) Math.PI / 180));
                        world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, entity.getSoundCategory(), 1.0f, 1.0f, true);
                        world.addParticle(ParticleTypes.SWEEP_ATTACK,
                                true, entity.getX(), entity.getBodyY(0.5), entity.getZ(), d, 0, 0);
                        for (int i = 0; i < 6; ++i) {
                            Vec3d vel = new Vec3d(random.nextGaussian(), random.nextDouble(), random.nextGaussian());

                            world.addParticle(ParticleTypes.CRIT, true,
                                    entity.getX() + vel.getX() * 0.4, entity.getRandomBodyY(), entity.getZ() + vel.getZ() * 0.4,
                                    vel.getX() * 0.01, Math.abs(vel.getY()) * 0.03, vel.getZ() * 0.01);
                        }
                    } else if (status == 19) {
                        world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_CAT_BEG_FOR_FOOD, entity.getSoundCategory(), 2.0f, 1.2f, true);
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
                        for (int i = 0; i < 5; ++i) {
                            Vec3d vel = new Vec3d(random.nextGaussian(), random.nextDouble(), random.nextGaussian());

                            world.addParticle(ParticleRegistry.HEART, true,
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
                    } else if (status == 20) {
                        world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, entity.getSoundCategory(), 2.0f, 1.2f, true);
                        for (int i = 0; i < 4; ++i) {
                            Vec3d vel = new Vec3d(random.nextGaussian(), random.nextDouble(), random.nextGaussian());

                            world.addParticle(ParticleTypes.HEART, true,
                                    entity.getX() + vel.getX() * 1.5, entity.getRandomBodyY(), entity.getZ() + vel.getZ() * 1.5,
                                    vel.getX() * 0.01, Math.abs(vel.getY()) * 0.03, vel.getZ() * 0.01);
                        }
                        for (int i = 0; i < 10; ++i) {
                            Vec3d vel = new Vec3d(random.nextGaussian(), random.nextDouble(), random.nextGaussian());

                            world.addParticle(ParticleRegistry.HEART, true,
                                    entity.getX() + vel.getX() * 1.5, entity.getRandomBodyY(), entity.getZ() + vel.getZ() * 1.5,
                                    vel.getX() * 0.01, Math.abs(vel.getY()) * 0.03, vel.getZ() * 0.01);
                        }

                        for (int i = 0; i < 15; ++i) {
                            Vec3d vel = new Vec3d(random.nextGaussian(), random.nextDouble(), random.nextGaussian());

                            world.addParticle(ParticleTypes.GUST, true,
                                    entity.getX() + vel.getX() * 1.5, entity.getRandomBodyY(), entity.getZ() + vel.getZ() * 1.5,
                                    vel.getX() * 0.01, Math.abs(vel.getY()) * 0.03, vel.getZ() * 0.01);
                        }
                        for (int i = 0; i < 20; ++i) {
                            Vec3d vel = new Vec3d(random.nextGaussian(), random.nextDouble(), random.nextGaussian());

                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_FIREWORK, new Vector3f(DyeColor.CYAN.getColorComponents())), true,
                                    entity.getX() + vel.getX() * 1.5, entity.getRandomBodyY(), entity.getZ() + vel.getZ() * 1.5,
                                    vel.getX() * 0.01, vel.getY() * 0.01, vel.getZ() * 0.01);
                        }
                    } else if (status == 21) {
                        world.addFireworkParticle(entity.getX(), entity.getBodyY(0.5), entity.getZ(),
                                0, 0, 0, getRocketNbt());
                    }
                }
            });
        });

        ModelLoadingPlugin.register(plugin -> {
            plugin.addModels(LipstickParticle.LIPSTICK_BALL);
        });


        ClientPlayNetworking.registerGlobalReceiver(new Identifier(OneGirlfriendBlock.MOD_ID, "girlfriendteleport"), (client, handler, buf, responseSender) -> {
            if (client.player == null || client.world == null) {
                return;
            }

            try {
                Vec3d pos = buf.readVec3d();
                int entityId = buf.readInt();
                ClientWorld world = client.world;

                client.execute(() -> {
                    Entity entity = world.getEntityById(entityId);
                    if (entity != null) {
                        entity.prevX = pos.getX();
                        entity.prevY = pos.getY();
                        entity.prevZ = pos.getZ();
                        entity.setPos(pos.getX(), pos.getY(), pos.getZ());
                    }
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
        list.add(0xC377BD);
        list.add(0xCE8FC9);

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
