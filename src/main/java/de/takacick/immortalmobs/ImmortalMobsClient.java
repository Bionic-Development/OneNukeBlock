package de.takacick.immortalmobs;

import com.mojang.blaze3d.systems.RenderSystem;
import de.takacick.immortalmobs.client.CustomLayers;
import de.takacick.immortalmobs.network.ImmortalExplosionHandler;
import de.takacick.immortalmobs.network.ImmortalFireworkExplosionHandler;
import de.takacick.immortalmobs.registry.EntityRegistry;
import de.takacick.immortalmobs.registry.ItemRegistry;
import de.takacick.immortalmobs.registry.ParticleRegistry;
import de.takacick.immortalmobs.registry.block.entity.ImmortalChainTrapBlockEntity;
import de.takacick.immortalmobs.registry.block.entity.ImmortalWoolBlockEntity;
import de.takacick.immortalmobs.registry.block.entity.renderer.ImmortalChainTrapBlockEntityRenderer;
import de.takacick.immortalmobs.registry.block.entity.renderer.ImmortalWoolBlockEntityRenderer;
import de.takacick.immortalmobs.registry.entity.custom.renderer.BlackHoleEntityRenderer;
import de.takacick.immortalmobs.registry.entity.custom.renderer.ImmortalEndCrystalEntityRenderer;
import de.takacick.immortalmobs.registry.entity.dragon.ImmortalEnderDragonEntity;
import de.takacick.immortalmobs.registry.entity.dragon.renderer.ImmortalEnderDragonEntityRenderer;
import de.takacick.immortalmobs.registry.entity.living.ImmortalCreeperEntity;
import de.takacick.immortalmobs.registry.entity.living.ImmortalEndermanEntity;
import de.takacick.immortalmobs.registry.entity.living.renderer.*;
import de.takacick.immortalmobs.registry.entity.projectiles.ImmortalFireworkEntity;
import de.takacick.immortalmobs.registry.entity.projectiles.renderer.*;
import de.takacick.immortalmobs.registry.particles.*;
import de.takacick.immortalmobs.registry.particles.goop.GoopDropParticle;
import de.takacick.immortalmobs.registry.particles.goop.GoopDropParticleEffect;
import de.takacick.immortalmobs.registry.particles.goop.GoopParticle;
import de.takacick.immortalmobs.registry.particles.goop.GoopStringParticle;
import draylar.identity.api.model.EntityUpdaters;
import draylar.identity.impl.PlayerDataProvider;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EmptyEntityRenderer;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;

public class ImmortalMobsClient implements ClientModInitializer {

    private KeyBinding immortalDragonBall;
    private boolean immortalDragonBallBoolean;
    private KeyBinding immortalDragonBreath;
    private boolean immortalDragonBreathBoolean;
    public static boolean TELEPORTED = false;
    public static boolean MOVED = false;

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityRegistry.FALLING_BLOCK, CustomBlockEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.IMMORTAL_SHEEP, ImmortalSheepEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.IMMORTAL_PIG, ImmortalPigEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.IMMORTAL_WOLF, ImmortalWolfEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.IMMORTAL_IRON_GOLEM, ImmortalIronGolemEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.IMMORTAL_SKELETON, ImmortalSkeletonEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.IMMORTAL_CREEPER, ImmortalCreeperEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.IMMORTAL_ENDERMAN, ImmortalEndermanEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.IMMORTAL_ENDER_DRAGON, ImmortalEnderDragonEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.IMMORTAL_FIREWORK, ImmortalFireworkEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.IMMORTAL_FIREWORK_EXPLOSION, EmptyEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.IMMORTAL_END_CRYSTAL, ImmortalEndCrystalEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.IMMORTAL_ITEM, ItemEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.IMMORTAL_PICKAXE, ImmortalPickaxeEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.IMMORTAL_DRAGON_BREATH, EmptyEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.IMMORTAL_DRAGON_BALL, ImmortalDragonBallEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.IMMORTAL_ARROW, ImmortalArrowEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.BLACK_HOLE, BlackHoleEntityRenderer::new);
        BlockEntityRendererRegistry.register(EntityRegistry.IMMORTAL_WOOL, ImmortalWoolBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(EntityRegistry.IMMORTAL_CHAIN_TRAP, ImmortalChainTrapBlockEntityRenderer::new);

        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP, GoopParticle.GoopParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP_DROP, GoopDropParticle.GoopDropParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP_STRING, GoopStringParticle.GoopStringParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.IMMORTAL_EXPLOSION, ImmortalExplosionParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.IMMORTAL_EXPLOSION_EMITTER, new ImmortalExplosionEmitterParticle.Factory());
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.IMMORTAL_SWEEP_ATTACK, ImmortalSweepAttackParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.IMMORTAL_WOLF, ImmortalWolfParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.IMMORTAL_TOTEM, ImmortalTotemParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.IMMORTAL_GLOW_TOTEM, ImmortalGlowTotemParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.IMMORTAL_POOF, ImmortalPoofParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.IMMORTAL_PORTAL, ImmortalPortalParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.IMMORTAL_FIREWORK, ImmortalFireworksSparkParticle.ExplosionFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.IMMORTAL_FLAME, ImmortalFlameParticle.Factory::new);

        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), ItemRegistry.HOLY_WATER_STILL, ItemRegistry.HOLY_WATER_FLOWING);

        try {
            immortalDragonBall = KeyBindingHelper.registerKeyBinding(
                    new KeyBinding("Immortal Dragon Ball",
                            InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X,
                            "ImmortalMobs Abilities")
            );
            immortalDragonBreath = KeyBindingHelper.registerKeyBinding(
                    new KeyBinding("Immortal Dragon Breath",
                            InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X,
                            "ImmortalMobs Abilities")
            );

            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                PlayerEntity playerEntity = client.player;
                if (playerEntity == null) {
                    return;
                }

                if (immortalDragonBall.isPressed() && !immortalDragonBallBoolean) {
                    immortalDragonBallBoolean = immortalDragonBall.isPressed();
                    if (((PlayerDataProvider) playerEntity).getIdentity() instanceof ImmortalEnderDragonEntity) {
                        PacketByteBuf buf = PacketByteBufs.create();
                        ClientPlayNetworking.send(new Identifier(ImmortalMobs.MOD_ID, "immortaldragonball"), buf);
                    }
                } else {
                    immortalDragonBallBoolean = immortalDragonBall.isPressed();
                }

                if (immortalDragonBreath.isPressed()) {
                    if (((PlayerDataProvider) playerEntity).getIdentity() instanceof ImmortalEnderDragonEntity) {
                        PacketByteBuf buf = PacketByteBufs.create();
                        buf.writeInt(immortalDragonBreathBoolean ? 0 : 1);
                        ClientPlayNetworking.send(new Identifier(ImmortalMobs.MOD_ID, "immortaldragonbreath"), buf);
                        immortalDragonBreathBoolean = true;
                    }
                } else {
                    immortalDragonBreathBoolean = false;
                }
            });
        } catch (RuntimeException ignored) {

        }

        ClientPlayNetworking.registerGlobalReceiver(ImmortalMobs.IDENTIFIER, (client, handler, buf, responseSender) -> {
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
                        client.particleManager.addEmitter(entity, ParticleRegistry.IMMORTAL_TOTEM, 10);
                        world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ITEM_TOTEM_USE, entity.getSoundCategory(), 1.0f, 1.0f, false);
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, entity.getSoundCategory(), 1.0f, 1.0f, false);
                    } else if (status == 2) {
                        for (int i = 0; i < 20; i++)
                            world.addParticle(new GoopDropParticleEffect(new Vec3f(0.80f, 0.80f, 0.80f), (float) (world.getRandom().nextDouble() * 3)),
                                    entity.getX(), entity.getBodyY(0.5), entity.getZ(), world.getRandom().nextGaussian() * 0.3, world.getRandom().nextDouble() * 0.15, world.getRandom().nextGaussian() * 0.3);
                        world.addParticle(new GoopDropParticleEffect(new Vec3f(0.80f, 0.80f, 0.80f), (float) (world.getRandom().nextDouble() * 3)),
                                entity.getX(), entity.getBodyY(0.5), entity.getZ(), 0, -1, 0);
                    } else if (status == 3) {
                        Vec3f color = new Vec3f(0.4029412f, 0.14705883f, 0.5411765f);
                        world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BLOCK_END_PORTAL_SPAWN, entity.getSoundCategory(), 1.0f, 1.0f, false);

                        client.particleManager.addEmitter(entity, ParticleRegistry.IMMORTAL_TOTEM, 10);
                        world.addParticle(new ImmortalWolfParticleEffect(color, entity.getHeight()), entity.getX(), entity.getY(), entity.getZ(), 0, -1, 0);
                        world.addParticle(new ImmortalWolfParticleEffect(color, entity.getHeight() + 0.2f), entity.getX(), entity.getY() - 0.2f, entity.getZ(), 0, -1, 0);
                        world.addParticle(new ImmortalWolfParticleEffect(color, entity.getHeight() + 0.4f), entity.getX(), entity.getY() - 0.4f, entity.getZ(), 0, -1, 0);
                    } else if (status == 4) {
                        if (!entity.equals(playerEntity)) {
                            Vec3f color = new Vec3f(0.4029412f, 0.14705883f, 0.5411765f);
                            world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BLOCK_END_PORTAL_SPAWN, entity.getSoundCategory(), 1.0f, 1.0f, false);

                            client.particleManager.addEmitter(entity, ParticleRegistry.IMMORTAL_TOTEM, 10);
                            world.addParticle(new ImmortalWolfParticleEffect(color, -entity.getHeight()), entity.getX(), entity.getY() + entity.getHeight(), entity.getZ(), 0, -1, 0);
                            world.addParticle(new ImmortalWolfParticleEffect(color, -(entity.getHeight() + 0.2f)), entity.getX(), entity.getY() + entity.getHeight() + 0.2f, entity.getZ(), 0, -1, 0);
                            world.addParticle(new ImmortalWolfParticleEffect(color, -(entity.getHeight() + 0.4f)), entity.getX(), entity.getY() + entity.getHeight() + 0.4f, entity.getZ(), 0, -1, 0);
                        }
                    } else if (status == 5) {
                        client.particleManager.addEmitter(entity, ParticleRegistry.IMMORTAL_GLOW_TOTEM, 5);
                    } else if (status == 6) {
                        client.particleManager.addEmitter(entity, ParticleRegistry.IMMORTAL_GLOW_TOTEM, 2);
                        world.addParticle(ParticleTypes.FLASH, entity.getX(), entity.getBodyY(0.5), entity.getZ(), 0, -1, 0);
                    } else if (status == 7) {
                        if (entity instanceof ImmortalCreeperEntity immortalCreeperEntity) {
                            immortalCreeperEntity.currentFuseTime = 0;
                            immortalCreeperEntity.lastFuseTime = 0;
                        }
                        client.particleManager.addEmitter(entity, ParticleRegistry.IMMORTAL_GLOW_TOTEM, 2);
                        world.addParticle(ParticleTypes.FLASH, entity.getX(), entity.getBodyY(0.5), entity.getZ(), 0, -1, 0);
                    } else if (status == 8) {
                        client.particleManager.addEmitter(entity, ParticleRegistry.IMMORTAL_GLOW_TOTEM, 2);
                        world.addParticle(ParticleTypes.FLASH, entity.getX(), entity.getBodyY(0.5), entity.getZ(), 0, -1, 0);
                    } else if (status == 9) {
                        Vec3f color = new Vec3f(0.4029412f, 0.14705883f, 0.5411765f);
                        world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, entity.getSoundCategory(), 1.0f, 1.0f, false);
                        world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA, entity.getSoundCategory(), 1.0f, 1.0f, false);

                        world.addParticle(new ImmortalWolfParticleEffect(color, -entity.getHeight()), entity.getX(), entity.getY() + entity.getHeight(), entity.getZ(), 0, -1, 0);
                        world.addParticle(new ImmortalWolfParticleEffect(color, -(entity.getHeight() + 0.2f)), entity.getX(), entity.getY() + entity.getHeight() + 0.2f, entity.getZ(), 0, -1, 0);
                        world.addParticle(new ImmortalWolfParticleEffect(color, -(entity.getHeight() + 0.4f)), entity.getX(), entity.getY() + entity.getHeight() + 0.4f, entity.getZ(), 0, -1, 0);

                        client.particleManager.addEmitter(entity, ParticleRegistry.IMMORTAL_GLOW_TOTEM, 10);
                        world.addParticle(ParticleTypes.FLASH, entity.getX(), entity.getBodyY(0.5), entity.getZ(), 0, -1, 0);
                    } else if (status == 10) {
                        world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, entity.getSoundCategory(), 1.0f, 1.0f, false);
                        client.particleManager.addEmitter(entity, ParticleRegistry.IMMORTAL_GLOW_TOTEM, 1);
                        world.addParticle(ParticleTypes.FLASH, entity.getX(), entity.getBodyY(0.5), entity.getZ(), 0, -1, 0);
                    } else if (status == 11) {
                        ItemStack itemStack = ItemRegistry.IMMORTAL_FIREWORK.getDefaultStack();
                        ImmortalFireworkEntity.getFireworkNBT(itemStack);
                        NbtCompound nbtCompound = itemStack.isEmpty() ? null : itemStack.getSubNbt("Fireworks");
                        Vec3d vec3d = entity.getVelocity();
                        ImmortalMobsClient.addFireworkParticle(world, entity.getX(), entity.getY() - 2, entity.getZ(), vec3d.x, vec3d.y, vec3d.z, nbtCompound);

                        world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, entity.getSoundCategory(), 1.0f, 1.0f, false);
                        client.particleManager.addEmitter(entity, ParticleRegistry.IMMORTAL_GLOW_TOTEM, 1);
                        world.addParticle(ParticleTypes.FLASH, entity.getX(), entity.getBodyY(0.5), entity.getZ(), 0, -1, 0);
                    } else if (status == 12) {
                        Vec3f color = new Vec3f(0.4029412f, 0.14705883f, 0.5411765f);
                        if (entity instanceof ImmortalEndermanEntity) {
                            world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_ENDERMAN_SCREAM, entity.getSoundCategory(), 1.0f, 1.0f, false);
                        }
                        world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BLOCK_CHAIN_PLACE, entity.getSoundCategory(), 1.0f, 1.0f, false);
                        client.particleManager.addEmitter(entity, ParticleRegistry.IMMORTAL_GLOW_TOTEM, 1);
                        world.addParticle(ParticleTypes.FLASH, entity.getX(), entity.getBodyY(0.5), entity.getZ(), 0, -1, 0);
                        world.addParticle(new ImmortalWolfParticleEffect(color, 0.3f), entity.getX(), entity.getY() - 0.1f, entity.getZ(), 0, -1, 0);
                    } else if (status == 13) {
                        if (Random.create().nextDouble() <= 0.03) {
                            world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BLOCK_CHAIN_PLACE, entity.getSoundCategory(), 1.0f, 1.0f, false);
                            if (entity instanceof ImmortalEndermanEntity && Random.create().nextDouble() <= 0.2) {
                                world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_ENDERMAN_SCREAM, entity.getSoundCategory(), 1.0f, 1.0f, false);
                            }
                        }

                        Vec3f color = new Vec3f(0.4029412f, 0.14705883f, 0.5411765f);
                        world.addParticle(new ImmortalWolfParticleEffect(color, 0.2f), entity.getX(), entity.getY() - 0.1f, entity.getZ(), 0, -1, 0);
                    } else if (status == 14) {
                        Vec3f color = new Vec3f(0.4029412f, 0.14705883f, 0.5411765f);
                        world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BLOCK_CHAIN_BREAK, entity.getSoundCategory(), 1.0f, 1.0f, false);


                        client.particleManager.addEmitter(entity, ParticleRegistry.IMMORTAL_GLOW_TOTEM, 1);
                        world.addParticle(ParticleTypes.FLASH, entity.getX(), entity.getBodyY(0.5), entity.getZ(), 0, -1, 0);
                        world.addParticle(new ImmortalWolfParticleEffect(color, -0.3f), entity.getX(), entity.getY() + 0.3f, entity.getZ(), 0, -1, 0);
                    }
                }
            });
        });

        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.IMMORTAL_WOOL_ITEM, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
            MinecraftClient.getInstance().getBlockEntityRenderDispatcher().renderEntity(
                    new ImmortalWoolBlockEntity(BlockPos.ORIGIN, ItemRegistry.IMMORTAL_WOOL.getDefaultState()),
                    matrices, vertexConsumers, light, overlay);
        });
        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.IMMORTAL_CHAIN_TRAP_ITEM, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
            MinecraftClient.getInstance().getBlockEntityRenderDispatcher().renderEntity(
                    new ImmortalChainTrapBlockEntity(BlockPos.ORIGIN, ItemRegistry.IMMORTAL_CHAIN_TRAP.getDefaultState()),
                    matrices, vertexConsumers, light, overlay);
        });

        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.IMMORTAL_ARROW, this::render);
        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.IMMORTAL_CANNON, this::renderCannon);
        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.IMMORTAL_FIREWORK, this::render);
        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.IMMORTAL_GUNPOWDER, this::render);
        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.IMMORTAL_INGOT, this::render);
        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.IMMORTAL_PICKAXE, this::render);
        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.IMMORTAL_PORKCHOP, this::render);
        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.IMMORTAL_SWORD, this::render);
        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.IMMORTAL_ORB, this::render);
        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.IMMORTAL_SHIRT, this::render);
        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.IMMORTAL_DIAMOND, this::render);
        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.IMMORTAL_ELYTRA, this::render);
        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.IMMORTAL_END_CRYSTAL, this::render);
        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.IMMORTAL_STRING, this::render);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.getGame().getCurrentSession() == null) {
                TELEPORTED = false;
                MOVED = false;
            }
            if (!(client.currentScreen instanceof DownloadingTerrainScreen)) {
                if (TELEPORTED && !MOVED) {
                    PlayerEntity entity = client.player;
                    if (entity != null) {
                        World world = entity.getWorld();
                        Vec3f color = new Vec3f(0.4029412f, 0.14705883f, 0.5411765f);
                        world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BLOCK_END_PORTAL_SPAWN, entity.getSoundCategory(), 1.0f, 1.0f, false);

                        client.particleManager.addEmitter(entity, ParticleRegistry.IMMORTAL_TOTEM, 10);
                        world.addParticle(new ImmortalWolfParticleEffect(color, -entity.getHeight()), entity.getX(), entity.getY() + entity.getHeight(), entity.getZ(), 0, -1, 0);
                        world.addParticle(new ImmortalWolfParticleEffect(color, -(entity.getHeight() + 0.2f)), entity.getX(), entity.getY() + entity.getHeight() + 0.2f, entity.getZ(), 0, -1, 0);
                        world.addParticle(new ImmortalWolfParticleEffect(color, -(entity.getHeight() + 0.4f)), entity.getX(), entity.getY() + entity.getHeight() + 0.4f, entity.getZ(), 0, -1, 0);
                    }
                    MOVED = false;
                    TELEPORTED = false;
                }
            } else if (MOVED) {
                MOVED = false;
            }
        });

        EntityUpdaters.register(EntityRegistry.IMMORTAL_ENDER_DRAGON, (player, dragon) -> {
            dragon.prevX = player.prevX;
            dragon.prevY = player.prevY;
            dragon.prevZ = player.prevZ;
            dragon.wingPosition += 0.01F;
            dragon.prevWingPosition = dragon.wingPosition;

            if (dragon.latestSegment < 0) {
                for (int l = 0; l < dragon.segmentCircularBuffer.length; ++l) {
                    dragon.segmentCircularBuffer[l][0] = (double) player.getYaw() + 180;
                    dragon.segmentCircularBuffer[l][1] = player.getY();
                }
            }

            if (++(dragon).latestSegment == (dragon).segmentCircularBuffer.length) {
                (dragon).latestSegment = 0;
            }

            dragon.segmentCircularBuffer[dragon.latestSegment][0] = (double) player.getYaw() + 180;
            dragon.segmentCircularBuffer[dragon.latestSegment][1] = player.getY();
        });

        ClientPlayNetworking.registerGlobalReceiver(ImmortalFireworkExplosionHandler.IDENTIFIER, (client, handler, buf, responseSender) -> {
            try {
                ImmortalFireworkExplosionHandler packet = new ImmortalFireworkExplosionHandler(buf);
                client.execute(() -> {
                    ImmortalFireworkExplosionHandler.Explosion explosion = new ImmortalFireworkExplosionHandler.Explosion(client.world, null, packet.getX(), packet.getY(), packet.getZ(), packet.getRadius(), packet.getAffectedBlocks());
                    explosion.affectWorld(true);
                    client.player.setVelocity(client.player.getVelocity().add(packet.getPlayerVelocityX(), packet.getPlayerVelocityY(), packet.getPlayerVelocityZ()));
                });
            } catch (Exception exception) {

            }
        });

        ClientPlayNetworking.registerGlobalReceiver(ImmortalExplosionHandler.IDENTIFIER, (client, handler, buf, responseSender) -> {
            try {
                ImmortalExplosionHandler packet = new ImmortalExplosionHandler(buf);
                client.execute(() -> {
                    ImmortalExplosionHandler.Explosion explosion = new ImmortalExplosionHandler.Explosion(client.world, null, packet.getX(), packet.getY(), packet.getZ(), packet.getRadius(), packet.getAffectedBlocks());
                    explosion.affectWorld(true);
                    client.player.setVelocity(client.player.getVelocity().add(packet.getPlayerVelocityX(), packet.getPlayerVelocityY(), packet.getPlayerVelocityZ()));
                });
            } catch (Exception exception) {

            }
        });

        ClientTickEvents.START_WORLD_TICK.register(world -> {
            world.getPlayers().forEach(player -> {
                if (((PlayerDataProvider) player).getIdentity() instanceof ImmortalEnderDragonEntity immortalEnderDragonEntity) {
                    if (immortalEnderDragonEntity.hurtTime > 0) {
                        immortalEnderDragonEntity.hurtTime--;
                    }

                    immortalEnderDragonEntity.age = player.age;
                    immortalEnderDragonEntity.deathTime = player.deathTime;
                    immortalEnderDragonEntity.setPlayerEntity(player);

                    immortalEnderDragonEntity.tickWithEndCrystals();
                }
            });
        });

        ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> {
            out.accept(new ModelIdentifier(ImmortalMobs.MOD_ID + ":immortal_cannon_glow#inventory"));
            out.accept(new ModelIdentifier(ImmortalMobs.MOD_ID + ":bow#inventory"));
            out.accept(new ModelIdentifier(ImmortalMobs.MOD_ID + ":bow_arrow#inventory"));
            out.accept(new ModelIdentifier(ImmortalMobs.MOD_ID + ":bow_pulling_0#inventory"));
            out.accept(new ModelIdentifier(ImmortalMobs.MOD_ID + ":bow_pulling_0_arrow#inventory"));
            out.accept(new ModelIdentifier(ImmortalMobs.MOD_ID + ":bow_pulling_1#inventory"));
            out.accept(new ModelIdentifier(ImmortalMobs.MOD_ID + ":bow_pulling_1_arrow#inventory"));
            out.accept(new ModelIdentifier(ImmortalMobs.MOD_ID + ":bow_pulling_2#inventory"));
            out.accept(new ModelIdentifier(ImmortalMobs.MOD_ID + ":bow_pulling_2_arrow#inventory"));
        });

        ModelPredicateProviderRegistry.register(ItemRegistry.SUPER_SHEAR_SAW, new Identifier("running"), (stack, clientWorld, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F);

        ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register((atlasTexture, registry) -> {
            registry.register(new Identifier(ImmortalMobs.MOD_ID, "block/holy_water_still"));
            registry.register(new Identifier(ImmortalMobs.MOD_ID, "block/holy_water_flowing"));
        });

        FluidRenderHandlerRegistry.INSTANCE.register(ItemRegistry.HOLY_WATER_STILL, ItemRegistry.HOLY_WATER_FLOWING, new SimpleFluidRenderHandler(
                new Identifier(ImmortalMobs.MOD_ID, "block/holy_water_still"),
                new Identifier(ImmortalMobs.MOD_ID, "block/holy_water_flowing")
        ));
    }

    public static void renderBakedItemQuads(MatrixStack matrices, VertexConsumer vertices, List<BakedQuad> quads, ItemStack stack, int light, int overlay) {
        MatrixStack.Entry entry = matrices.peek();
        for (BakedQuad bakedQuad : quads) {
            float f = 0.4029412f;
            float g = 0.14705883f;
            float h = 0.5411765f;
            vertices.quad(entry, bakedQuad, f, g, h, light, overlay);
        }
    }

    private void renderBakedItemQuadsCannon(MatrixStack matrices, VertexConsumer vertices, List<BakedQuad> quads, ItemStack stack, int light, int overlay) {
        MatrixStack.Entry entry = matrices.peek();
        for (BakedQuad bakedQuad : quads) {

            if (bakedQuad.hasColor()) {

            }

            float f = 0.4029412f;
            float g = 0.14705883f;
            float h = 0.5411765f;
            vertices.quad(entry, bakedQuad, f, g, h, light, overlay);
        }
    }

    public void quad(VertexConsumer vertexConsumer, MatrixStack.Entry matrixEntry, BakedQuad quad, float red, float green, float blue, int light, int overlay) {
        this.quad(vertexConsumer, matrixEntry, quad, new float[]{1.0f, 1.0f, 1.0f, 1.0f}, red, green, blue, new int[]{light, light, light, light}, overlay, false);
    }

    public void quad(VertexConsumer vertexConsumer, MatrixStack.Entry matrixEntry, BakedQuad quad, float[] brightnesses, float red, float green, float blue, int[] lights, int overlay, boolean useQuadColorData) {
        float[] fs = new float[]{brightnesses[0], brightnesses[1], brightnesses[2], brightnesses[3]};
        int[] is = new int[]{lights[0], lights[1], lights[2], lights[3]};
        int[] js = quad.getVertexData();
        Vec3i vec3i = quad.getFace().getVector();
        Vec3f vec3f = new Vec3f(vec3i.getX(), vec3i.getY(), vec3i.getZ());
        Matrix4f matrix4f = matrixEntry.getPositionMatrix();
        vec3f.transform(matrixEntry.getNormalMatrix());
        float extraSize = 0.002f;
        int i = 8;
        int x = 0;

        RenderSystem.setShaderTexture(0, new Identifier(ImmortalMobs.MOD_ID, "/textures/item/background.png"));

        int j = js.length / 8;
        try (MemoryStack memoryStack = MemoryStack.stackPush();) {
            ByteBuffer byteBuffer = memoryStack.malloc(VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL.getVertexSizeByte());
            IntBuffer intBuffer = byteBuffer.asIntBuffer();
            for (int k = 0; k < j; ++k) {
                float q;
                float p;
                float o;
                float n;
                float m;
                intBuffer.clear();
                intBuffer.put(js, k * 8, 8);
                float f = byteBuffer.getFloat(0);
                float g = byteBuffer.getFloat(4);
                float h = byteBuffer.getFloat(8);
                if (useQuadColorData) {
                    float l = (float) (byteBuffer.get(12) & 0xFF) / 255.0f;
                    m = (float) (byteBuffer.get(13) & 0xFF) / 255.0f;
                    n = (float) (byteBuffer.get(14) & 0xFF) / 255.0f;
                    o = l * fs[k] * red;
                    p = m * fs[k] * green;
                    q = n * fs[k] * blue;
                } else {
                    o = fs[k] * red;
                    p = fs[k] * green;
                    q = fs[k] * blue;
                }

                //            x -= extraX;
                //            y -= extraY;
                //            z -= extraZ;
                //            f += extraX;
                //            g += extraY;
                //            h += extraZ;
                //            if (mirror) {
                //                float i = f;
                //                f = x;
                //                x = i;
                //            }
                //
                //            Vertex vertex = new Vertex(x, y, z, 0.0f, 0.0f);
                //            Vertex vertex2 = new Vertex(f, y, z, 0.0f, 8.0f);
                //            Vertex vertex3 = new Vertex(f, g, z, 8.0f, 8.0f);
                //            Vertex vertex4 = new Vertex(x, g, z, 8.0f, 0.0f);
                //            Vertex vertex5 = new Vertex(x, y, h, 0.0f, 0.0f);
                //            Vertex vertex6 = new Vertex(f, y, h, 0.0f, 8.0f);
                //            Vertex vertex7 = new Vertex(f, g, h, 8.0f, 8.0f);
                //            Vertex vertex8 = new Vertex(x, g, h, 8.0f, 0.0f);
                //            this.sides[4] = new Quad(new Vertex[]{vertex2, vertex, vertex4, vertex3}, k, q, l, r, textureWidth, textureHeight, mirror, Direction.NORTH);
                //            this.sides[5] = new Quad(new Vertex[]{vertex5, vertex6, vertex7, vertex8}, n, q, o, r, textureWidth, textureHeight, mirror, Direction.SOUTH);
                int r = is[k];
                m = byteBuffer.getFloat(16);
                n = byteBuffer.getFloat(20);
                Vector4f vector4f = new Vector4f(f, g, h, 1.0f);

                if (quad.getFace() == Direction.SOUTH) {
                    switch (x) {
                        case 3 -> vector4f.add(extraSize, extraSize, extraSize, 0);
                        case 0 -> vector4f.add(-extraSize, extraSize, extraSize, 0);
                        case 1 -> vector4f.add(-extraSize, -extraSize, extraSize, 0);
                        case 2 -> vector4f.add(extraSize, -extraSize, extraSize, 0);
                    }
                } else if (quad.getFace() == Direction.NORTH) {
                    switch (x) {
                        case 3 -> vector4f.add(-extraSize, extraSize, -extraSize, 0);
                        case 0 -> vector4f.add(extraSize, extraSize, -extraSize, 0);
                        case 1 -> vector4f.add(extraSize, -extraSize, -extraSize, 0);
                        case 2 -> vector4f.add(-extraSize, -extraSize, -extraSize, 0);
                    }
                } else if (quad.getFace() == Direction.EAST) {
                    switch (x) {
                        case 3 -> vector4f.add(extraSize, extraSize, -extraSize, 0);
                        case 0 -> vector4f.add(extraSize, extraSize, extraSize, 0);
                        case 1 -> vector4f.add(extraSize, -extraSize, extraSize, 0);
                        case 2 -> vector4f.add(extraSize, -extraSize, -extraSize, 0);
                    }
                } else if (quad.getFace() == Direction.WEST) {
                    switch (x) {
                        case 3 -> vector4f.add(-extraSize, extraSize, extraSize, 0);
                        case 0 -> vector4f.add(-extraSize, extraSize, -extraSize, 0);
                        case 1 -> vector4f.add(-extraSize, -extraSize, -extraSize, 0);
                        case 2 -> vector4f.add(-extraSize, -extraSize, extraSize, 0);
                    }
                } else if (quad.getFace() == Direction.DOWN) {
                    switch (x) {
                        case 0 -> vector4f.add(-extraSize, -extraSize, extraSize, 0);
                        case 1 -> vector4f.add(-extraSize, -extraSize, -extraSize, 0);
                        case 2 -> vector4f.add(extraSize, -extraSize, -extraSize, 0);
                        case 3 -> vector4f.add(extraSize, -extraSize, extraSize, 0);
                    }
                } else if (quad.getFace() == Direction.UP) {
                    switch (x) {
                        case 3 -> vector4f.add(extraSize, extraSize, -extraSize, 0);
                        case 0 -> vector4f.add(-extraSize, extraSize, -extraSize, 0);
                        case 1 -> vector4f.add(-extraSize, extraSize, extraSize, 0);
                        case 2 -> vector4f.add(extraSize, extraSize, extraSize, 0);
                    }
                }

                x++;
                vector4f.transform(matrix4f);
                vertexConsumer.vertex(vector4f.getX(), vector4f.getY(), vector4f.getZ(), o, p, q, 1.0f, m, n, overlay, r, vec3f.getX(), vec3f.getY(), vec3f.getZ());
            }
        }
    }

    public void render(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        BakedModel bakedModel = MinecraftClient.getInstance().getItemRenderer().getModels().getModel(stack);
        renderBakedItemQuads(matrices,
                vertexConsumers.getBuffer(RenderLayer.getEyes(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE)), bakedModel.getQuads(null, null, Random.create()),
                stack, LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE, overlay);
        renderBakedItemQuads(matrices,
                vertexConsumers.getBuffer(RenderLayer.getEyes(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE)), bakedModel.getQuads(null, null, Random.create()),
                stack, LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE, overlay);
    }

    public void renderCannon(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        BakedModel bakedModel = MinecraftClient.getInstance().getItemRenderer().getModels().getModelManager().getModel(new ModelIdentifier(ImmortalMobs.MOD_ID + ":immortal_cannon_glow#inventory"));
        renderBakedItemQuads(matrices,
                vertexConsumers.getBuffer(RenderLayer.getEyes(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE)), bakedModel.getQuads(null, null, Random.create()),
                stack, LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE, overlay);
        renderBakedItemQuads(matrices,
                vertexConsumers.getBuffer(RenderLayer.getEyes(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE)), bakedModel.getQuads(null, null, Random.create()),
                stack, LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE, overlay);
    }

    public static void renderBow(LivingEntity livingEntity, World world, int seed, ItemStack stack, boolean leftHanded, ModelTransformation.Mode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        BakedModel bakedModel = MinecraftClient.getInstance().getItemRenderer().getModels().getModelManager().getModel(new ModelIdentifier(ImmortalMobs.MOD_ID + ":bow_arrow#inventory"));
        ClientWorld clientWorld = world instanceof ClientWorld ? (ClientWorld) world : null;
        BakedModel bakedModel2 = bakedModel.getOverrides().apply(bakedModel, stack, clientWorld, livingEntity, seed);

        bakedModel2.getTransformation().getTransformation(mode).apply(leftHanded, matrices);
        matrices.translate(-0.5, -0.5, -0.5);

        renderBakedItemQuads(matrices,
                vertexConsumers.getBuffer(CustomLayers.IMMORTAL_CUTOUT.apply(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE)), bakedModel2.getQuads(null, null, Random.create()),
                stack, LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE, overlay);
        renderBakedItemQuads(matrices,
                vertexConsumers.getBuffer(RenderLayer.getEyes(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE)), bakedModel2.getQuads(null, null, Random.create()),
                stack, LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE, overlay);
        renderBakedItemQuads(matrices,
                vertexConsumers.getBuffer(RenderLayer.getEyes(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE)), bakedModel2.getQuads(null, null, Random.create()),
                stack, LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE, overlay);
    }

    public static void addFireworkParticle(World world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, @Nullable NbtCompound nbt) {
        MinecraftClient.getInstance().particleManager.addParticle(new ImmortalFireworksSparkParticle.FireworkParticle((ClientWorld) world, x, y, z, velocityX, velocityY, velocityZ, MinecraftClient.getInstance().particleManager, nbt));
    }

}
