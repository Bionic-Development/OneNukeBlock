package de.takacick.onegirlboyblock.mixin.animations;

import de.takacick.onegirlboyblock.access.PlayerModelProperties;
import de.takacick.onegirlboyblock.client.item.renderer.BitCannonItemRenderer;
import de.takacick.onegirlboyblock.client.item.renderer.InfernoHairDryerItemRenderer;
import de.takacick.onegirlboyblock.registry.ItemRegistry;
import de.takacick.onegirlboyblock.registry.item.BitCannon;
import de.takacick.onegirlboyblock.registry.item.HandheldItem;
import de.takacick.onegirlboyblock.utils.ArmHelper;
import de.takacick.utils.item.client.render.ItemRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
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

import java.util.HashMap;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {

    @Shadow
    @Final
    private EntityRenderDispatcher entityRenderDispatcher;

    @Shadow
    @Final
    private MinecraftClient client;

    @Unique
    private final HashMap<Item, ItemRenderer> onegirlboyblock$itemModels = new HashMap<>();

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(MinecraftClient client, EntityRenderDispatcher entityRenderDispatcher, net.minecraft.client.render.item.ItemRenderer itemRenderer, CallbackInfo info) {
        this.onegirlboyblock$itemModels.clear();
        this.onegirlboyblock$itemModels.put(ItemRegistry.INFERNO_HAIR_DRYER, new InfernoHairDryerItemRenderer(true));
        this.onegirlboyblock$itemModels.put(ItemRegistry.BIT_CANNON, new BitCannonItemRenderer(true));
    }

    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"), cancellable = true)
    private void renderFirstPersonItem(AbstractClientPlayerEntity player, float delta, float pitch, Hand currentHand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo info) {
        if (currentHand.equals(Hand.OFF_HAND)) {
            ItemStack stack = player.getStackInHand(currentHand);
            Arm arm = ArmHelper.getArm(player, currentHand);
            if (stack.getItem() instanceof HandheldItem handheldItem && handheldItem.shouldRender(player, stack, arm)) {

                ItemRenderer itemRenderer = this.onegirlboyblock$itemModels.get(stack.getItem());
                if (itemRenderer != null) {
                    info.cancel();
                }
            }
            if(!info.isCancelled()) {
                arm = arm.getOpposite();
                stack = ArmHelper.getArmStack(player, arm);
                if (stack.getItem() instanceof HandheldItem handheldItem && handheldItem.shouldRender(player, stack, arm)) {

                    ItemRenderer itemRenderer = this.onegirlboyblock$itemModels.get(stack.getItem());
                    if (itemRenderer != null) {
                        info.cancel();
                    }
                }
            }
            return;
        }

        for (Hand hand : Hand.values()) {
            ItemStack stack = player.getStackInHand(hand);
            Arm arm = ArmHelper.getArm(player, hand);
            if (stack.getItem() instanceof HandheldItem handheldItem && handheldItem.shouldRender(player, stack, arm)) {

                ItemRenderer itemRenderer = this.onegirlboyblock$itemModels.get(stack.getItem());
                if (itemRenderer == null) {
                    return;
                }

                boolean rightHand = ArmHelper.isArm(player, hand, Arm.RIGHT);
                ModelTransformationMode renderMode = rightHand ? ModelTransformationMode.FIRST_PERSON_RIGHT_HAND : ModelTransformationMode.FIRST_PERSON_LEFT_HAND;
                matrices.push();
                PlayerEntityRenderer playerEntityRenderer = (PlayerEntityRenderer) this.entityRenderDispatcher.getRenderer(player);
                this.renderArmHoldingItem(matrices, player, stack, vertexConsumers, light, equipProgress, swingProgress, Arm.RIGHT, handheldItem.showRightArm(player, stack, arm));

                matrices.push();
                if (handheldItem.allowHandTransformation(player, stack, arm)) {
                    ModelPart mainHand = !rightHand ? playerEntityRenderer.getModel().leftArm : playerEntityRenderer.getModel().rightArm;
                    mainHand.rotate(matrices);
                } else {
                    ModelPart mainHand = !rightHand ? playerEntityRenderer.getModel().leftArm : playerEntityRenderer.getModel().rightArm;

                    float prevYaw = mainHand.yaw;
                    float prevPitch = mainHand.pitch;
                    mainHand.yaw = 0f;
                    mainHand.pitch = 0f;
                    matrices.translate(0, -12 / 16f, 0);

                    mainHand.rotate(matrices);

                    mainHand.yaw = prevYaw;
                    mainHand.pitch = prevPitch;
                }

                if (playerEntityRenderer.getModel() instanceof PlayerModelProperties playerModelProperties) {
                    playerModelProperties.getAnimationBodyModel().rotateItem(matrices, rightHand);
                }

                if (itemRenderer.shouldRender(stack, player, renderMode)) {
                    matrices.push();
                    float tickDelta = MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true);
                    itemRenderer.render(stack, player, tickDelta, matrices, vertexConsumers, renderMode, light, OverlayTexture.DEFAULT_UV);
                    matrices.pop();
                }

                matrices.pop();

                matrices.pop();
                matrices.push();
                if (handheldItem.showLeftArm(player, stack, arm)) {
                    this.renderArmHoldingItem(matrices, player, stack, vertexConsumers, light, equipProgress, swingProgress, Arm.LEFT, true);
                }
                matrices.pop();
                info.cancel();
            }
        }
    }

    @Unique
    private void renderArmHoldingItem(MatrixStack matrices, LivingEntity player, ItemStack itemStack, VertexConsumerProvider vertexConsumers, int light, float equipProgress, float swingProgress, Arm arm, boolean renderArm) {
        boolean bl = arm != Arm.LEFT;
        float f = bl ? 1.0f : -1.0f;
        float g = MathHelper.sqrt(swingProgress);
        float h = -0.3f * MathHelper.sin((g * (float) Math.PI));
        float i = 0.4f * MathHelper.sin((g * ((float) Math.PI * 2)));
        float j = -0.4f * MathHelper.sin((swingProgress * (float) Math.PI));
        if (itemStack.getItem() instanceof BitCannon) {
            matrices.translate(0, 0.25, 0.3);
        }
        matrices.translate(f * h, i + -0.5f, j + (-0.71999997f));
        matrices.scale(-1, -1, 1);
        ClientPlayerEntity abstractClientPlayerEntity = this.client.player;
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-10f));
        PlayerEntityRenderer playerEntityRenderer = (PlayerEntityRenderer) this.entityRenderDispatcher.getRenderer(abstractClientPlayerEntity);
        float pitch = abstractClientPlayerEntity.getPitch();
        float prevPitch = abstractClientPlayerEntity.prevPitch;
        abstractClientPlayerEntity.setPitch(0f);
        abstractClientPlayerEntity.prevPitch = 0f;
        boolean sneaking = abstractClientPlayerEntity.isInSneakingPose();
        abstractClientPlayerEntity.inSneakingPose = false;

        if (renderArm) {
            if (bl) {
                playerEntityRenderer.renderRightArm(matrices, vertexConsumers, light, abstractClientPlayerEntity);
            } else {
                playerEntityRenderer.renderLeftArm(matrices, vertexConsumers, light, abstractClientPlayerEntity);
            }
        } else {
            playerEntityRenderer.getModel().setAngles(abstractClientPlayerEntity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        }

        abstractClientPlayerEntity.setPitch(pitch);
        abstractClientPlayerEntity.prevPitch = prevPitch;
        abstractClientPlayerEntity.inSneakingPose = sneaking;
    }
}