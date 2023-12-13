package de.takacick.upgradebody.mixin;

import com.google.common.base.MoreObjects;
import de.takacick.upgradebody.access.PlayerProperties;
import de.takacick.upgradebody.registry.bodypart.BodyParts;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
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
    protected abstract void renderArmHoldingItem(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float equipProgress, float swingProgress, Arm arm);

    @Shadow
    protected abstract float getMapAngle(float tickDelta);

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    protected abstract void renderArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Arm arm);

    @Shadow
    private float prevEquipProgressMainHand;

    @Shadow
    private float equipProgressMainHand;

    @Shadow
    private float prevEquipProgressOffHand;

    @Shadow
    private ItemStack offHand;

    @Shadow
    private ItemStack mainHand;

    @Shadow
    private float equipProgressOffHand;

    @Shadow
    protected abstract void renderFirstPersonItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light);

    @Inject(method = "renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V", at = @At("HEAD"), cancellable = true)
    public void renderItem(float tickDelta, MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, ClientPlayerEntity player, int light, CallbackInfo info) {
        if (player instanceof PlayerProperties playerProperties
                && playerProperties.isUpgrading()
                && playerProperties.getBodyPartManager().hasBodyPart(BodyParts.CYBER_CHAINSAWS)) {

            HeldItemRenderer.HandRenderType handRenderType = HeldItemRenderer.getHandRenderType(player);
            if (handRenderType.renderMainHand && handRenderType.renderOffHand) {
                return;
            }

            float k;
            float j;
            float f = player.getHandSwingProgress(tickDelta);
            Hand hand = MoreObjects.firstNonNull(player.preferredHand, Hand.MAIN_HAND);
            float g = MathHelper.lerp(tickDelta, player.prevPitch, player.getPitch());
            float h = MathHelper.lerp(tickDelta, player.lastRenderPitch, player.renderPitch);
            float i = MathHelper.lerp(tickDelta, player.lastRenderYaw, player.renderYaw);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees((player.getPitch(tickDelta) - h) * 0.1f));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((player.getYaw(tickDelta) - i) * 0.1f));
            if (handRenderType.renderMainHand || player.getStackInHand(Hand.MAIN_HAND).isEmpty()) {
                j = hand == Hand.MAIN_HAND ? f : 0.0f;
                k = 1.0f - MathHelper.lerp(tickDelta, this.prevEquipProgressMainHand, this.equipProgressMainHand);
                this.renderFirstPersonItem(player, tickDelta, g, Hand.MAIN_HAND, j, this.mainHand, k, matrices, vertexConsumers, light);
            }
            if (handRenderType.renderOffHand || player.getStackInHand(Hand.OFF_HAND).isEmpty()) {
                j = hand == Hand.OFF_HAND ? f : 0.0f;
                k = 1.0f - MathHelper.lerp(tickDelta, this.prevEquipProgressOffHand, this.equipProgressOffHand);
                this.renderFirstPersonItem(player, tickDelta, g, Hand.OFF_HAND, j, this.offHand, k, matrices, vertexConsumers, light);
            }
            vertexConsumers.draw();
            info.cancel();
        }
    }

    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"), cancellable = true)
    public void renderFirstPersonItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo info) {
        if (player instanceof PlayerProperties playerProperties
                && playerProperties.isUpgrading()
                && playerProperties.getBodyPartManager().hasBodyPart(BodyParts.CYBER_CHAINSAWS)
                && player.getStackInHand(hand).isEmpty()) {
            matrices.push();
            boolean bl = hand == Hand.MAIN_HAND;
            Arm arm = bl ? player.getMainArm() : player.getMainArm().getOpposite();

            if (!player.isInvisible()) {
                this.renderArmHoldingItem(matrices, vertexConsumers, light, equipProgress, swingProgress, arm);
            }
            matrices.pop();
            info.cancel();
        }
    }

    @Unique
    private void upgradebody$renderCyberChainsawsInBothHands(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float pitch, float equipProgress, float swingProgress) {
        float g = -0.2f;
        float h = -0.4f;
        matrices.translate(0.0, -g / 2.0f, h);
        float i = this.getMapAngle(0f);
        matrices.translate(0.0f, 0.00f + equipProgress * -1.2f + i * -0.5f, -0.82f);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(i * -45.0f));
        if (!this.client.player.isInvisible()) {
            matrices.push();
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0f));
            matrices.push();
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(45.0f));
            matrices.translate(0, 1, 0);
            this.renderArm(matrices, vertexConsumers, light, Arm.RIGHT);
            matrices.pop();
            matrices.push();
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-45.0f));
            matrices.translate(0, 1, 0);
            this.renderArm(matrices, vertexConsumers, light, Arm.LEFT);
            matrices.pop();
            matrices.pop();
        }
    }
}
