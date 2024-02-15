package de.takacick.secretgirlbase.mixin.leadcuffs;

import com.mojang.blaze3d.systems.RenderSystem;
import de.takacick.secretgirlbase.access.LeadCuffProperties;
import de.takacick.secretgirlbase.registry.entity.custom.FireworkTimeBombEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    @Final
    private EntityRenderDispatcher entityRenderDispatcher;

    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"), cancellable = true)
    private void renderFirstPersonItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo info) {
        if (((LeadCuffProperties) player).isLeadCuffed()  || player.getVehicle() instanceof FireworkTimeBombEntity) {
            info.cancel();
            if (hand != Hand.MAIN_HAND) {
                return;
            }
            matrices.push();
            this.secretgirlbase$renderLeadCuffedHands(matrices, vertexConsumers, light, pitch, equipProgress, 0);
            matrices.pop();
        }
    }

    private void secretgirlbase$renderLeadCuffedHands(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float pitch, float equipProgress, float swingProgress) {
        float g = -0.2f;
        float h = -0.4f;
        matrices.translate(0.0, -g / 2.0f, h);
        float i = 0;
        matrices.translate(0.0, 0.04f + i * -0.5f, -0.72f);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-25f));

        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0f));
        this.secretgirlbase$renderLeadCuffedArm(matrices, vertexConsumers, light, Arm.RIGHT);
        this.secretgirlbase$renderLeadCuffedArm(matrices, vertexConsumers, light, Arm.LEFT);
        matrices.pop();
    }

    private void secretgirlbase$renderLeadCuffedArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Arm arm) {
        RenderSystem.setShaderTexture(0, this.client.player.getSkinTextures().texture());
        PlayerEntityRenderer playerEntityRenderer = (PlayerEntityRenderer) this.entityRenderDispatcher.getRenderer(this.client.player);
        matrices.push();
        float f = arm == Arm.RIGHT ? 1.0f : -1.0f;
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0f));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(45.0f));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(f * -20.0f));
        matrices.translate(f * 0.3f, -1.1f, 0.45f);
        if (arm == Arm.RIGHT) {
            playerEntityRenderer.renderRightArm(matrices, vertexConsumers, light, this.client.player);
        } else {
            playerEntityRenderer.renderLeftArm(matrices, vertexConsumers, light, this.client.player);
        }
        matrices.pop();
    }
}