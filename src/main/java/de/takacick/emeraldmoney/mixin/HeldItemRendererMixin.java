package de.takacick.emeraldmoney.mixin;

import de.takacick.emeraldmoney.registry.ItemRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
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

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    protected abstract void renderArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Arm arm);

    @Shadow
    protected abstract float getMapAngle(float tickDelta);

    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"), cancellable = true)
    private void renderFirstPersonItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo info) {
        if (player.getMainHandStack().isOf(ItemRegistry.VILLAGER_DRILLER)) {
            if(!hand.equals(Hand.MAIN_HAND)) {
                info.cancel();
            } else {
                float y = (pitch / 90f);

                matrices.translate(0.0f, Math.min(-0.5f * y, 0), 0.25f);
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(y * 85.0f));

                matrices.push();
                this.emeraldmoney$renderTerraDrillInBothHands(matrices, vertexConsumers, light, pitch, equipProgress, 0);
                matrices.pop();
            }
        }
    }

    @Unique
    private void emeraldmoney$renderTerraDrillInBothHands(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float pitch, float equipProgress, float swingProgress) {
        float g = -0.2f;
        float h = -0.4f;
        matrices.translate(0.0, -g / 2.0f, h);
        float i = this.getMapAngle(0f);
        matrices.translate(0.0f, 0.00f + equipProgress * -1.2f + i * -0.5f, -0.82f);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(i * -85.0f ));
        if (!this.client.player.isInvisible()) {
            matrices.push();
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0f));
            this.renderArm(matrices, vertexConsumers, light, Arm.RIGHT);
            this.renderArm(matrices, vertexConsumers, light, Arm.LEFT);
            matrices.pop();
        }
    }
}