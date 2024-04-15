package de.takacick.onegirlfriendblock.mixin.onegirlfriendblock;

import de.takacick.onegirlfriendblock.access.PlayerProperties;
import de.takacick.onegirlfriendblock.client.renderer.HoldingOneBlockRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {


    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"), cancellable = true)
    private void renderFirstPersonItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo info) {
        if (player instanceof PlayerProperties playerProperties && playerProperties.hasOneGirlfriendBlock()) {
            if (hand.equals(Hand.OFF_HAND)) {
                info.cancel();
                return;
            }

            matrices.push();

            matrices.translate(0f, -0.5f, -1.8f);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-45f));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
            HoldingOneBlockRenderer.HOLDING_BLOCK.render(matrices, vertexConsumers.getBuffer(RenderLayer.getItemEntityTranslucentCull(HoldingOneBlockRenderer.TEXTURE)), light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
            matrices.pop();
            info.cancel();
        }
    }
}