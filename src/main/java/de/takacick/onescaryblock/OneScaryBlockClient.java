package de.takacick.onescaryblock;

import de.takacick.onescaryblock.access.PlayerProperties;
import de.takacick.onescaryblock.registry.EntityRegistry;
import de.takacick.onescaryblock.registry.ItemRegistry;
import de.takacick.onescaryblock.registry.ParticleRegistry;
import de.takacick.onescaryblock.registry.ScreenHandlerRegistry;
import de.takacick.onescaryblock.registry.block.entity.renderer.Item303BlockEntityRenderer;
import de.takacick.onescaryblock.registry.block.entity.renderer.PhantomBlockEntityRenderer;
import de.takacick.onescaryblock.registry.block.entity.renderer.ScaryOneBlockBlockEntityRenderer;
import de.takacick.onescaryblock.registry.entity.custom.renderer.HerobrineLightningEntityRenderer;
import de.takacick.onescaryblock.registry.entity.custom.renderer.ScaryOneBlockEntityRenderer;
import de.takacick.onescaryblock.registry.entity.living.renderer.BloodManEntityRenderer;
import de.takacick.onescaryblock.registry.entity.living.renderer.Entity303EntityRenderer;
import de.takacick.onescaryblock.registry.entity.living.renderer.HerobrineEntityRenderer;
import de.takacick.onescaryblock.registry.entity.projectile.renderer.HerobrineLightningProjectileEntityRenderer;
import de.takacick.onescaryblock.registry.inventory.ScaryOneBlockScreen;
import de.takacick.onescaryblock.registry.particles.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.EmptyEntityRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

public class OneScaryBlockClient implements ClientModInitializer {

    private KeyBinding scaryOneBlockOpen;
    private Boolean scaryOneBlockOpenBoolean = false;
    private KeyBinding entity303RandomTeleport;
    private Boolean entity303RandomTeleportBoolean = false;

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityRegistry.SCARY_ONE_BLOCK, ScaryOneBlockEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.HEROBRINE, HerobrineEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.BLOOD_MAN, BloodManEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.ENTITY_303, Entity303EntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.HEROBRINE_LIGHTNING_BOLT, HerobrineLightningEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.HEROBRINE_LIGHTNING_PROJECTILE, HerobrineLightningProjectileEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.HEROBRINE_LIGHTNING_EFFECT, EmptyEntityRenderer::new);
        BlockEntityRendererFactories.register(EntityRegistry.SCARY_ONE_BLOCK_BLOCK_ENTITY, ScaryOneBlockBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(EntityRegistry.ITEM_303, Item303BlockEntityRenderer::new);
        BlockEntityRendererFactories.register(EntityRegistry.PHANTOM_BLOCK, PhantomBlockEntityRenderer::new);

        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.FALLING_BLOOD, BloodLeakParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.BLOOD_SPLASH, BloodSplashParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.ITEM_303, Item303Particle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.SOUL, SoulParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.BARRIER, BarrierParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.HEROBRINE_LIGHTNING, HerobrineLightningParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.SMOKE, SmokeParticle.Factory::new);

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ItemRegistry.SCARY_ONE_BLOCK);
        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), ItemRegistry.STILL_BLOOD, ItemRegistry.FLOWING_BLOOD);
        ScreenRegistry.register(ScreenHandlerRegistry.SCARY_ONE_BLOCk, ScaryOneBlockScreen::new);

        try {
            this.scaryOneBlockOpen = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                    "key." + OneScaryBlock.MOD_ID + ".scary_one_block_open",
                    InputUtil.Type.KEYSYM,
                    GLFW.GLFW_KEY_X,
                    "category." + OneScaryBlock.MOD_ID
            ));
            this.entity303RandomTeleport = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                    "key." + OneScaryBlock.MOD_ID + ".entity_303_random_teleport",
                    InputUtil.Type.KEYSYM,
                    GLFW.GLFW_KEY_X,
                    "category." + OneScaryBlock.MOD_ID
            ));
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                PlayerEntity playerEntity = client.player;
                if (!(playerEntity instanceof PlayerProperties playerProperties)) {
                    return;
                }

                if (this.scaryOneBlockOpen.isPressed() && !this.scaryOneBlockOpenBoolean) {
                    this.scaryOneBlockOpenBoolean = true;
                    if (client.crosshairTarget instanceof BlockHitResult blockHitResult && !blockHitResult.getType().equals(HitResult.Type.MISS)) {
                        if (playerEntity.getWorld().getBlockState(blockHitResult.getBlockPos()).isOf(ItemRegistry.SCARY_ONE_BLOCK)) {
                            PacketByteBuf buf = PacketByteBufs.create();
                            buf.writeBlockPos(blockHitResult.getBlockPos());
                            buf.writeInt(blockHitResult.getSide().getId());
                            ClientPlayNetworking.send(new Identifier(OneScaryBlock.MOD_ID, "scaryoneblockopen"), buf);
                        }
                    }
                } else {
                    this.scaryOneBlockOpenBoolean = this.scaryOneBlockOpen.isPressed();
                }

                if (this.entity303RandomTeleport.isPressed() && !this.entity303RandomTeleportBoolean) {
                    this.entity303RandomTeleportBoolean = true;
                    PacketByteBuf buf = PacketByteBufs.create();
                    ClientPlayNetworking.send(new Identifier(OneScaryBlock.MOD_ID, "entity303randomteleport"), buf);
                } else {
                    this.entity303RandomTeleportBoolean = this.entity303RandomTeleport.isPressed();
                }
            });
        } catch (RuntimeException exception) {

        }

        ClientPlayNetworking.registerGlobalReceiver(OneScaryBlock.IDENTIFIER, (client, handler, buf, responseSender) -> {
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
                        double g = entity.getX();
                        double j = entity.getZ();

                        for (int i = 0; i < 3; ++i) {
                            double d = random.nextGaussian() * 0.2;
                            double e = random.nextDouble() * 0.4;
                            double f = random.nextGaussian() * 0.2;

                            world.addParticle(ParticleRegistry.FALLING_BLOOD,
                                    true, g + d, entity.getBodyY(0.5 + e), j + f, d * 0.1, e * 0.1, f * 0.1);
                        }
                    } else if (status == 2) {
                        double g = entity.getX();
                        double j = entity.getZ();

                        for (int i = 0; i < 35; ++i) {
                            double d = random.nextGaussian() * 0.5;
                            double e = random.nextGaussian() * 0.35;
                            double f = random.nextGaussian() * 0.5;

                            world.addParticle(ParticleRegistry.BARRIER,
                                    true, g + d, entity.getBodyY(0.5 + e), j + f, d * 0.15, e * 0.25, f * 0.15);
                        }

                        world.playSound(entity.getX(), entity.getBodyY(0.5f), entity.getZ(), SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE.value(), SoundCategory.AMBIENT, 1f, 3f, true);
                    } else if (status == 3) {
                        double g = entity.getX();
                        double j = entity.getZ();

                        for (int i = 0; i < 5; ++i) {
                            double d = random.nextGaussian() * 0.5;
                            double e = random.nextGaussian() * 0.35;
                            double f = random.nextGaussian() * 0.5;

                            world.addParticle(ParticleRegistry.SOUL,
                                    true, g + d, entity.getBodyY(0.5 + e), j + f, d * 0.15, e * 0.25, f * 0.15);
                        }

                        world.playSound(entity.getX(), entity.getBodyY(0.5f), entity.getZ(), SoundEvents.PARTICLE_SOUL_ESCAPE, SoundCategory.PLAYERS, 1f, 3f, true);
                    }
                }
            });
        });


        FluidRenderHandlerRegistry.INSTANCE.register(ItemRegistry.STILL_BLOOD, ItemRegistry.FLOWING_BLOOD, new SimpleFluidRenderHandler(
                new Identifier(OneScaryBlock.MOD_ID, "block/blood_still"),
                new Identifier(OneScaryBlock.MOD_ID, "block/blood_flow")
        ));
    }

    public static PlayerEntity getClientPlayer() {
        return MinecraftClient.getInstance().player;
    }

    public static void addItemBreakParticles(World world, BlockPos blockPos, ParticleEffect particleEffect) {
        addItemBreakParticles(world, blockPos.toCenterPos(), particleEffect);
    }

    public static void addItemBreakParticles(World world, Vec3d pos, ParticleEffect particleEffect) {
        Random random = Random.create();

        for (int i = 0; i < 35; ++i) {
            Vec3d vel = new Vec3d(random.nextGaussian() * 0.45, 0, random.nextGaussian() * 0.45)
                    .add(0, 0.5 + world.getRandom().nextDouble() * 0.8, 0);

            world.addImportantParticle(particleEffect, true,
                    pos.getX() + vel.getX(), pos.getY() + vel.getY(), pos.getZ() + vel.getZ(),
                    vel.getX() * 0.1, vel.getY() * 0.3, vel.getZ() * 0.1);
        }
    }

    public static void setWorldTime(World world, long time) {
        if (world instanceof ClientWorld clientWorld) {
            clientWorld.setTimeOfDay(time);
        }
    }

    public static void setMojangOverlay(Entity entity) {
        if (!entity.equals(MinecraftClient.getInstance().player)) {
            return;
        }

        MinecraftClient.getInstance().options.useKey.setPressed(false);
        MinecraftClient.getInstance().reloadResources().thenAccept(unused -> {

        });
    }

}
