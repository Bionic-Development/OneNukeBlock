package de.takacick.onenukeblock.client.entity.model;

import de.takacick.onenukeblock.client.entity.model.animation.BangMaceAnimations;
import de.takacick.onenukeblock.client.entity.model.animation.ExplosiveGummyBearAnimations;
import de.takacick.onenukeblock.client.utils.AdvancedAnimationHelper;
import de.takacick.onenukeblock.registry.item.BangMace;
import de.takacick.onenukeblock.utils.ArmHelper;
import de.takacick.onenukeblock.utils.data.AttachmentTypes;
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

    public AnimationBodyModel(boolean slim) {
        this.root = getTexturedModelData(slim).createModel();
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

    public static TexturedModelData getTexturedModelData(boolean slim) {
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

        ModelPartData left_item = left_arm.addChild("left_item", ModelPartBuilder.create(), ModelTransform.pivot(-1.0f, 10.0F, 0.0F));

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

        var explosiveGummyBear = playerEntity.getAttached(AttachmentTypes.EXPLOSIVE_GUMMY_BEAR);
        if (explosiveGummyBear != null) {
            this.rightArm.resetTransform();
            this.leftArm.resetTransform();

            if (playerEntity.isInSneakingPose()) {
                this.leftArm.pivotY = 5.2f;
                this.rightArm.pivotY = 5.2f;
                this.leftArm.setAngles(0f, 40f * ((float) Math.PI / 180), 5f * ((float) Math.PI / 180f));
                this.rightArm.setAngles(0f, -40f * ((float) Math.PI / 180), -5f * ((float) Math.PI / 180f));
            }

            float time = (explosiveGummyBear.getProgress(tickDelta) * explosiveGummyBear.getMaxTicks());

            AdvancedAnimationHelper.animate(this, ExplosiveGummyBearAnimations.EXPLODE, (long) (time / 20f * 1000f), 1.0f, new Vector3f(), true);
        }

        for (Hand hand : Hand.values()) {
            boolean rightHand = ArmHelper.isArm(playerEntity, hand, Arm.RIGHT);
            boolean using = playerEntity.isUsingItem() && playerEntity.getActiveHand().equals(hand);

            ItemStack itemStack = playerEntity.getStackInHand(hand);
            if (itemStack.getItem() instanceof BangMace && using) {
                this.rightArm.resetTransform();
                this.leftArm.resetTransform();
                this.leftItem.resetTransform();
                this.rightItem.resetTransform();

                if (playerEntity.isInSneakingPose()) {
                    this.leftArm.pivotY = 5.2f;
                    this.rightArm.pivotY = 5.2f;
                }

                var animation = playerEntity.getAttached(AttachmentTypes.BANG_MACE);
                if (animation != null) {
                    AdvancedAnimationHelper.animate(this, BangMaceAnimations.getUsing(rightHand), (long) (animation.getTick(tickDelta) / 20f * 1000f), 1.0f, new Vector3f(), rightHand);
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