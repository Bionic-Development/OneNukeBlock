package de.takacick.onesuperblock.mixin;

import de.takacick.onesuperblock.client.render.SuperRenderLayers;
import de.takacick.superitems.registry.ParticleRegistry;
import de.takacick.superitems.registry.particles.RainbowParticleEffect;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    @Shadow
    @Final
    private BufferBuilderStorage bufferBuilders;

    @Shadow
    protected abstract void renderLayer(RenderLayer renderLayer, MatrixStack matrices, double cameraX, double cameraY, double cameraZ, Matrix4f positionMatrix);

    @Shadow
    private @Nullable ClientWorld world;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;draw(Lnet/minecraft/client/render/RenderLayer;)V", ordinal = 5, shift = At.Shift.AFTER), cancellable = true)
    private void render(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix, CallbackInfo ci) {
        VertexConsumerProvider.Immediate immediate = this.bufferBuilders.getEntityVertexConsumers();
        immediate.draw(SuperRenderLayers.getBlockOverlay());
        immediate.draw(SuperRenderLayers.getOreOverlay());
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderLayer(Lnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/util/math/MatrixStack;DDDLnet/minecraft/util/math/Matrix4f;)V", ordinal = 2, shift = At.Shift.AFTER), cancellable = true)
    private void renderLayer(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix, CallbackInfo ci) {
        Vec3d vec3d = camera.getPos();
        double d = vec3d.getX();
        double e = vec3d.getY();
        double f = vec3d.getZ();
        this.renderLayer(SuperRenderLayers.getBlockOverlay(), matrices, d, e, f, positionMatrix);
        this.renderLayer(SuperRenderLayers.getOreOverlay(), matrices, d, e, f, positionMatrix);
    }

    @Inject(method = "processWorldEvent", at = @At("HEAD"), cancellable = true)
    public void processWorldEvent(int eventId, BlockPos blockPos, int data, CallbackInfo info) {
        if (this.world != null) {
            if (eventId == 521783128) {

                Vec3d pos = Vec3d.ofCenter(blockPos);
                if (data == 0) {
                    this.world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1.0f, 3.0f, true);
                    this.world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 1f, 1f, true);
                    this.world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.25f, 3f, true);

                    for (int i = 0; i < 16; ++i) {
                        double g = 0.6 * this.world.getRandom().nextGaussian();
                        double h = 0.6 * this.world.getRandom().nextDouble();
                        double j = 0.6 * this.world.getRandom().nextGaussian();
                        this.world.addParticle(new RainbowParticleEffect(ParticleRegistry.RAINBOW_CONFETTI,
                                        this.world.getRandom().nextInt(24000), false), true,
                                pos.getX() + g, pos.getY() + h, pos.getZ() + j,
                                g * 0.3, h * 0.3, j * 0.3);
                    }

                    for (int i = 0; i < 8; ++i) {
                        double g = 0.6 * this.world.getRandom().nextGaussian();
                        double h = 0.6 * this.world.getRandom().nextDouble();
                        double j = 0.6 * this.world.getRandom().nextGaussian();

                        this.world.addParticle(new RainbowParticleEffect(ParticleRegistry.RAINBOW_SPARK,
                                        this.world.getRandom().nextInt(24000), false), false,
                                pos.getX() + g, pos.getY() + h, pos.getZ() + j,
                                g * 0.1, h * 0.1, j * 0.1);
                    }

                    this.world.addParticle(new RainbowParticleEffect(ParticleRegistry.RAINBOW_EXPLOSION, world.getRandom().nextInt(24000), false), pos.getX(), pos.getY(), pos.getZ(), 0.25, 0, 0);
                } else if (data == 1) {
                    for (int i = 0; i < 2; ++i) {
                        double g = 0.6 * this.world.getRandom().nextGaussian();
                        double h = 0.6 * this.world.getRandom().nextGaussian();
                        double j = 0.6 * this.world.getRandom().nextGaussian();

                        this.world.addParticle(new RainbowParticleEffect(ParticleRegistry.RAINBOW_SPARK,
                                        this.world.getRandom().nextInt(24000), false), false,
                                pos.getX() + g, pos.getY() + h, pos.getZ() + j,
                                g * 0.1, h * 0.1, j * 0.1);
                    }
                }
                info.cancel();
            }
        }
    }
}