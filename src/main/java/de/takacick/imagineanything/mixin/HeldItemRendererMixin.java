package de.takacick.imagineanything.mixin;

import de.takacick.imagineanything.ImagineAnything;
import de.takacick.imagineanything.registry.item.HeadItem;
import de.takacick.imagineanything.registry.item.Imagined;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    protected abstract void renderArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Arm arm);

    @Shadow
    private ItemStack offHand;

    @Shadow
    protected abstract void renderArmHoldingItem(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float equipProgress, float swingProgress, Arm arm);

    @Shadow
    protected abstract float getMapAngle(float tickDelta);

    @Shadow
    private ItemStack mainHand;

    @Shadow
    @Final
    private static RenderLayer MAP_BACKGROUND;

    @Inject(method = "getUsingItemHandRenderType", at = @At("HEAD"), cancellable = true)
    private static void getUsingItemHandRenderType(ClientPlayerEntity player, CallbackInfoReturnable<HeldItemRenderer.HandRenderType> info) {

    }

    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"), cancellable = true)
    private void renderFirstPersonItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo info) {
        if (player.getStackInHand(hand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND).getItem() instanceof HeadItem) {
            info.cancel();
            return;
        }

        if (!(item.getItem() instanceof HeadItem)) {
            if (item.getItem() instanceof Imagined) {
                if (hand.equals(Hand.MAIN_HAND) && this.offHand.isEmpty()) {
                    this.renderThoughtInBothHands(matrices, vertexConsumers, light, pitch, equipProgress, swingProgress);
                    info.cancel();
                }
            }
            return;
        }

        matrices.push();
        this.renderHeadInBothHands(matrices, vertexConsumers, light, pitch, equipProgress, 0);
        matrices.pop();
    }

    private void renderHeadInBothHands(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float pitch, float equipProgress, float swingProgress) {
        float f = MathHelper.sqrt(swingProgress);
        float g = -0.2f;
        float h = -0.4f;
        matrices.translate(0.0, -g / 2.0f, h);
        float i = 0;
        matrices.translate(0.0, 0.04f + equipProgress * -1.2f + i * -0.5f, -0.72f);
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(i * -85.0f));
        if (!this.client.player.isInvisible()) {
            matrices.push();
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90.0f));
            this.renderArm(matrices, vertexConsumers, light, Arm.RIGHT);
            this.renderArm(matrices, vertexConsumers, light, Arm.LEFT);
            matrices.pop();
        }
    }

    private void renderThoughtInBothHands(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float pitch, float equipProgress, float swingProgress) {
        float f = MathHelper.sqrt(swingProgress);
        float g = -0.2f * MathHelper.sin(swingProgress * (float) Math.PI);
        float h = -0.4f * MathHelper.sin(f * (float) Math.PI);
        matrices.translate(0.0, -g / 2.0f, h);
        float i = this.getMapAngle(90);
        matrices.translate(0.0, 0.04f + equipProgress * -1.2f + i * -0.5f, -0.72f);
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(i * -85.0f));
        if (!this.client.player.isInvisible()) {
            matrices.push();
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90.0f));
            this.renderArm(matrices, vertexConsumers, light, Arm.RIGHT);
            this.renderArm(matrices, vertexConsumers, light, Arm.LEFT);
            matrices.pop();
        }
        float j = MathHelper.sin(f * (float) Math.PI);
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(j * 20.0f));
        matrices.scale(2.0f, 2.0f, 2.0f);
        this.renderFirstPersonThought(matrices, vertexConsumers, light, this.mainHand);
    }

    private void renderFirstPersonThought(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int swingProgress, ItemStack stack) {
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0f));
        matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0f));
        matrices.scale(0.38f, 0.38f, 0.38f);
        matrices.translate(-0.5, -0.25, 0.0);
        matrices.scale(0.0078125f, 0.0078125f, 0.0078125f);

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getText(stack.getItem() instanceof Imagined imagined ? imagined.getTexture() : new Identifier(ImagineAnything.MOD_ID, "textures/item/imagined_alfred_the_pickaxe.png")));
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        vertexConsumer.vertex(matrix4f, -7.0f, 135.0f, 0.0f).color(255, 255, 255, 255).texture(0.0f, 1.0f).light(swingProgress).next();
        vertexConsumer.vertex(matrix4f, 135.0f, 135.0f, 0.0f).color(255, 255, 255, 255).texture(1.0f, 1.0f).light(swingProgress).next();
        vertexConsumer.vertex(matrix4f, 135.0f, -7.0f, 0.0f).color(255, 255, 255, 255).texture(1.0f, 0.0f).light(swingProgress).next();
        vertexConsumer.vertex(matrix4f, -7.0f, -7.0f, 0.0f).color(255, 255, 255, 255).texture(0.0f, 0.0f).light(swingProgress).next();
    }
}