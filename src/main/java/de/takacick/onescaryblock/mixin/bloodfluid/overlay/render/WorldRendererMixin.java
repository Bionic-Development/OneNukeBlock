package de.takacick.onescaryblock.mixin.bloodfluid.overlay.render;

import de.takacick.onescaryblock.client.shader.OneScaryBlockLayers;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.SynchronousResourceReloader;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin implements SynchronousResourceReloader, AutoCloseable {

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/BufferBuilderStorage;getOutlineVertexConsumers()Lnet/minecraft/client/render/OutlineVertexConsumerProvider;", ordinal = 1, shift = At.Shift.AFTER))
    private void render(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f projectionMatrix, CallbackInfo info) {
        OneScaryBlockLayers.getTransformVertexConsumer().draw();
    }
}
