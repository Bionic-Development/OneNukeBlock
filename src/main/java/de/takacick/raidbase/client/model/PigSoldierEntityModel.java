package de.takacick.raidbase.client.model;

import de.takacick.raidbase.access.PigProperties;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;

public class PigSoldierEntityModel<T extends Entity>
        extends SinglePartEntityModel<T> implements ModelWithArms {

    public final ModelPart root;
    public final ModelPart bone;
    public final ModelPart head;
    public final ModelPart helmet;
    public final ModelPart body;
    public final ModelPart rightFrontLeg;
    public final ModelPart leftFrontLeg;
    public final ModelPart rightHindLeg;
    public final ModelPart leftHindLeg;

    public PigSoldierEntityModel() {
        this.root = getTexturedModelData().createModel().getChild("root");
        this.bone = this.root.getChild("bone");
        this.head = this.bone.getChild("head");
        this.helmet = this.head.getChild("helmet");
        this.body = this.bone.getChild("body");
        this.rightFrontLeg = this.bone.getChild("right_front_leg");
        this.leftFrontLeg = this.bone.getChild("left_front_leg");
        this.rightHindLeg = this.bone.getChild("right_hind_leg");
        this.leftHindLeg = this.bone.getChild("left_hind_leg");
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        if (entity instanceof PigProperties pigProperties) {
            if (!pigProperties.getStandUpState().isRunning()) {
                entity.age = Math.max(10, entity.age);
                pigProperties.getStandUpState().startIfNotRunning(0);
            }

            this.updateAnimation(pigProperties.getStandUpState(), ANIMATION, animationProgress);
            if (pigProperties.getStandUpState().getTimeRunning() >= 240) {
                this.head.roll += headYaw * ((float) Math.PI / 180);
                this.head.pitch += headPitch * ((float) Math.PI / 180);
            }

            this.rightHindLeg.pitch += MathHelper.cos(limbAngle * 0.6662f) * 1.4f * limbDistance;
            this.leftHindLeg.pitch += MathHelper.cos(limbAngle * 0.6662f + (float) Math.PI) * 1.4f * limbDistance;

            this.rightFrontLeg.pitch += MathHelper.cos(limbAngle * 0.6662f) * 0.4f * limbDistance;
            this.leftFrontLeg.pitch += MathHelper.cos(limbAngle * 0.6662f + (float) Math.PI) * 0.4f * limbDistance;

            float f = this.handSwingProgress;
            f = 1.0f - this.handSwingProgress;
            f *= f;
            f *= f;
            f = 1.0f - f;
            float g = MathHelper.sin(f * (float)Math.PI);
            float h = MathHelper.sin(this.handSwingProgress * (float)Math.PI) * -(this.head.pitch - 0.7f) * 0.75f;
            this.rightFrontLeg.pitch -= g * 1.2f + h;
            this.rightFrontLeg.yaw += this.body.yaw * 2.0f;
            this.rightFrontLeg.roll += MathHelper.sin(this.handSwingProgress * (float)Math.PI) * -0.4f;
        }
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData root = modelPartData.addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData bone = root.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData right_hind_leg = bone.addChild("right_hind_leg", ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.0F, -6.0F, 7.0F));

        ModelPartData left_hind_leg = bone.addChild("left_hind_leg", ModelPartBuilder.create().uv(0, 16).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(3.0F, -6.0F, 7.0F));

        ModelPartData head = bone.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
                .uv(16, 16).cuboid(-2.0F, -4.0F, -5.0F, 4.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -8.0F, -10.0F));

        ModelPartData helmet = head.addChild("helmet", ModelPartBuilder.create().uv(0, 32).cuboid(-4.0F, -17.6F, -14.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.99F)), ModelTransform.pivot(0.0F, 8.7F, 10.0F));

        ModelPartData body = bone.addChild("body", ModelPartBuilder.create().uv(28, 8).cuboid(-5.0F, -10.0F, -7.0F, 10.0F, 16.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -13.0F, 2.0F, 1.5708F, 0.0F, 0.0F));

        ModelPartData right_front_leg = bone.addChild("right_front_leg", ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.0F, -6.0F, -5.0F));

        ModelPartData left_front_leg = bone.addChild("left_front_leg", ModelPartBuilder.create().uv(0, 16).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(3.0F, -6.0F, -5.0F));
        return TexturedModelData.of(modelData, 64, 32);
    }

    public static final Animation ANIMATION = Animation.Builder.create(0.8343334f)
            .addBoneAnimation("head",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.16766666f, AnimationHelper.createTranslationalVector(0f, 0f, 2f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.25f, AnimationHelper.createTranslationalVector(0f, 0f, 2f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("head",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.25f, AnimationHelper.createRotationalVector(90f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("right_hind_leg",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.16766666f, AnimationHelper.createTranslationalVector(0f, 4f, 1f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("right_hind_leg",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.16766666f, AnimationHelper.createRotationalVector(90f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("left_hind_leg",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.16766666f, AnimationHelper.createTranslationalVector(0f, 4f, 1f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("left_hind_leg",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.16766666f, AnimationHelper.createRotationalVector(90f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("bone",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.16766666f, AnimationHelper.createTranslationalVector(0f, 15f, -10f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.2916767f, AnimationHelper.createTranslationalVector(0f, 14f, -10f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("bone",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.25f, AnimationHelper.createRotationalVector(-90f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR))).build();

    @Override
    public void setArmAngle(Arm arm, MatrixStack matrices) {
        this.root.rotate(matrices);
        this.bone.rotate(matrices);

        ModelPart modelPart = this.getArm(arm);

        float f = arm == Arm.RIGHT ? 1f : -1f;
        modelPart.pivotX += f;
        this.getArm(arm).rotate(matrices);
        modelPart.pivotX -= f;

        matrices.translate(0, -0.25, 0);
    }

    protected ModelPart getArm(Arm arm) {
        if (arm == Arm.LEFT) {
            return this.leftFrontLeg;
        }
        return this.rightFrontLeg;
    }
}