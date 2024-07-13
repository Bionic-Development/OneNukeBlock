package de.takacick.onegirlboyblock.client.entity.model;

import de.takacick.onegirlboyblock.client.entity.model.animation.BitCannonAnimations;
import de.takacick.onegirlboyblock.client.entity.model.animation.InfernoHairDryerAnimations;
import de.takacick.onegirlboyblock.client.utils.AdvancedAnimationHelper;
import de.takacick.onegirlboyblock.registry.ItemRegistry;
import de.takacick.onegirlboyblock.registry.entity.living.TurboBoardEntity;
import de.takacick.onegirlboyblock.registry.entity.living.model.animation.TurboBoardAnimations;
import de.takacick.onegirlboyblock.utils.ArmHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import org.joml.Vector3f;

public class AnimationBodyModel<T extends LivingEntity>
        extends SinglePartEntityModel<T> {

    private final ModelPart root;

    public final ModelPart bone;
    public final ModelPart head;
    public final ModelPart body;
    public final ModelPart butterflyWings;
    public final ModelPart rightArm;
    public final ModelPart rightItem;
    public final ModelPart leftItem;
    public final ModelPart leftArm;
    public final ModelPart rightLeg;
    public final ModelPart leftLeg;
    private boolean slim = false;

    public AnimationBodyModel() {
        this.root = getTexturedModelData().createModel();
        this.bone = this.root.getChild("bone");
        this.head = this.bone.getChild("head");
        this.body = this.bone.getChild("body");
        this.butterflyWings = this.body.getChild("butterfly_wings");
        this.rightArm = this.bone.getChild("right_arm");
        this.rightItem = this.rightArm.getChild("right_item");
        this.leftArm = this.bone.getChild("left_arm");
        this.leftItem = this.leftArm.getChild("left_item");
        this.rightLeg = this.bone.getChild("right_leg");
        this.leftLeg = this.bone.getChild("left_leg");
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
        ModelPartData projectile = bone.addChild("projectile", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData head = bone.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
                .uv(32, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.5F)), ModelTransform.pivot(0.0F, -0.0F, 0.0F));

        ModelPartData body = bone.addChild("body", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.0F))
                .uv(16, 32).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, -0.0F, 0.0F));

        ModelPartData butterfly_wings = body.addChild("butterfly_wings", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -6.0F, 2.0F));

        ModelPartData right_arm = bone.addChild("right_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
                .uv(40, 32).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(-5.0F, 2.0F, 0.0F));
        ModelPartData right_item = right_arm.addChild("right_item", ModelPartBuilder.create(), ModelTransform.pivot(-1.0F, 10.0F, 0.0F));

        ModelPartData left_arm = bone.addChild("left_arm", ModelPartBuilder.create().uv(32, 48).cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new Dilation(0.0F))
                .uv(48, 48).cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(5.0F, 2.0F, 0.0F));

        ModelPartData left_item = left_arm.addChild("left_item", ModelPartBuilder.create(), ModelTransform.pivot(1.0F, 10.0F, 0.0F));

        ModelPartData right_leg = bone.addChild("right_leg", ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(-1.9F, 10.0F, 0.0F));

        ModelPartData left_leg = bone.addChild("left_leg", ModelPartBuilder.create().uv(16, 48).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 48).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(1.9F, 10.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    public void copyFromBipedState(BipedEntityModel<T> model, boolean slim) {
        this.rightItem.resetTransform();
        this.leftItem.resetTransform();
        this.slim = slim;

        model.copyStateTo(this);

        this.head.copyTransform(model.head);
        this.body.copyTransform(model.body);
        this.rightArm.copyTransform(model.rightArm);
        this.leftArm.copyTransform(model.leftArm);
        this.rightLeg.copyTransform(model.rightLeg);
        this.leftLeg.copyTransform(model.leftLeg);
    }

    public void resetForRiding(BipedEntityModel<T> model) {
        this.rightItem.resetTransform();
        this.leftItem.resetTransform();

        this.bone.resetTransform();
        this.head.copyTransform(model.head);
        this.body.copyTransform(model.body);
        this.rightArm.copyTransform(model.rightArm);
        this.leftArm.copyTransform(model.leftArm);
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }

    public void setAngles(T playerEntity, BipedEntityModel<T> model, float f, float g, float h, float i, float j) {
        this.bone.resetTransform();
        float tickDelta = MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true);
        float pitch = playerEntity.getPitch(tickDelta) / 90f + 1f;
        long pitchDelta = (long) (pitch * 1000f);
        long animationDelta = (long) (((playerEntity.age + tickDelta) / 20f) * 1000f);

        boolean riding = false;
        if (playerEntity.getVehicle() instanceof TurboBoardEntity turboBoardEntity) {
            float speed = turboBoardEntity.limbAnimator.getSpeed(MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true));
            AdvancedAnimationHelper.animateLerp(this, TurboBoardAnimations.IDLE, TurboBoardAnimations.TURBO_BOARD, speed, (long) ((turboBoardEntity.age + tickDelta) / 20f * 1000f), 1.0f, new Vector3f(), true);
            riding = true;
        }

        for (Hand hand : Hand.values()) {
            boolean leftHand = ArmHelper.isArm(playerEntity, hand, Arm.LEFT);
            boolean using = playerEntity.isUsingItem() && playerEntity.getActiveHand().equals(hand);

            ItemStack itemStack = playerEntity.getStackInHand(hand);
            if (itemStack.isOf(ItemRegistry.INFERNO_HAIR_DRYER)) {
                if (using) {
                    if (riding) {
                        resetForRiding(model);
                    }
                    this.rightArm.resetTransform();
                    this.leftArm.resetTransform();
                    if (leftHand) {
                        this.leftItem.resetTransform();
                        this.rightItem.resetTransform();
                    } else {
                        this.rightItem.resetTransform();
                        this.leftItem.resetTransform();
                    }

                    if (playerEntity.isInSneakingPose()) {
                        this.leftArm.pivotY = 5.2f;
                        this.rightArm.pivotY = 5.2f;
                    }

                    AdvancedAnimationHelper.animate(this, InfernoHairDryerAnimations.getUsing(leftHand), pitchDelta, 1.0f, new Vector3f(), !leftHand);
                }
            } else if (itemStack.isOf(ItemRegistry.BIT_CANNON)) {
                if (using) {
                    if (riding) {
                        resetForRiding(model);
                    }

                    this.rightArm.resetTransform();
                    this.leftArm.resetTransform();
                    if (leftHand) {
                        this.leftItem.resetTransform();
                        this.rightItem.resetTransform();
                    } else {
                        this.rightItem.resetTransform();
                        this.leftItem.resetTransform();
                    }

                    if (playerEntity.isInSneakingPose()) {
                        this.leftArm.pivotY = 5.2f;
                        this.rightArm.pivotY = 5.2f;
                    }

                    AdvancedAnimationHelper.animate(this, BitCannonAnimations.getUsing(leftHand), pitchDelta, 1.0f, new Vector3f(), !leftHand);
                }
            }
        }
    }

    public void rotateItem(MatrixStack matrixStack, boolean rightItem) {
        if (rightItem) {
            this.rightItem.rotate(matrixStack);
        } else {
            this.leftItem.rotate(matrixStack);
        }
    }

    public void rotateButterflyWings(MatrixStack matrixStack) {
        this.butterflyWings.rotate(matrixStack);
    }
}