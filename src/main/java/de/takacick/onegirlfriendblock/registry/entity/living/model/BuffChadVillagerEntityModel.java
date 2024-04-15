package de.takacick.onegirlfriendblock.registry.entity.living.model;

import de.takacick.onegirlfriendblock.registry.entity.living.BuffChadVillagerEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.math.MathHelper;

public class BuffChadVillagerEntityModel<T extends BuffChadVillagerEntity> extends SinglePartEntityModel<T> {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public BuffChadVillagerEntityModel(ModelPart root) {
        this.root = root.getChild("body");
        this.head = this.root.getChild(EntityModelPartNames.HEAD);
        this.rightArm = this.root.getChild(EntityModelPartNames.RIGHT_ARM);
        this.leftArm = this.root.getChild(EntityModelPartNames.LEFT_ARM);
        this.rightLeg = this.root.getChild(EntityModelPartNames.RIGHT_LEG);
        this.leftLeg = this.root.getChild(EntityModelPartNames.LEFT_LEG);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-9.0F, -2.0F, -4.0F, 18.0F, 12.0F, 9.0F, new Dilation(0.0F))
                .uv(55, 12).cuboid(-3.0F, -5.0F, -2.0F, 6.0F, 3.0F, 5.0F, new Dilation(0.0F))
                .uv(55, 0).cuboid(-4.5F, 10.0F, -3.0F, 9.0F, 5.0F, 6.0F, new Dilation(0.5F)), ModelTransform.pivot(0.0F, -7.0F, 0.0F));

        ModelPartData chest = body.addChild("chest", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 31.0F, 0.0F));

        ModelPartData right_chest = chest.addChild("right_chest", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData right_chest_r1 = right_chest.addChild("right_chest_r1", ModelPartBuilder.create().uv(33, 22).cuboid(-8.0F, -31.0F, -11.0F, 8.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

        ModelPartData left_chest = chest.addChild("left_chest", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData left_chest_r1 = left_chest.addChild("left_chest_r1", ModelPartBuilder.create().uv(31, 70).mirrored().cuboid(0.0F, -31.0F, -11.0F, 8.0F, 6.0F, 3.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

        ModelPartData right_shoulder_muscle = body.addChild("right_shoulder_muscle", ModelPartBuilder.create(), ModelTransform.pivot(-3.8298F, -2.2051F, 0.5F));

        ModelPartData right_shoulder_muscle_r1 = right_shoulder_muscle.addChild("right_shoulder_muscle_r1", ModelPartBuilder.create().uv(23, 55).cuboid(0.0F, 0.0F, -2.0F, 3.0F, 5.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.8298F, -2.7949F, -0.5F, 0.0F, 0.0F, 0.829F));

        ModelPartData left_shoulder_muscle = body.addChild("left_shoulder_muscle", ModelPartBuilder.create(), ModelTransform.pivot(3.8298F, -2.2051F, 0.5F));

        ModelPartData left_shoulder_muscle_r1 = left_shoulder_muscle.addChild("left_shoulder_muscle_r1", ModelPartBuilder.create().uv(23, 55).mirrored().cuboid(-3.0F, 0.0F, -2.0F, 3.0F, 5.0F, 5.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-0.8298F, -2.7949F, -0.5F, 0.0F, 0.0F, -0.829F));

        ModelPartData head = body.addChild("head", ModelPartBuilder.create().uv(0, 22).cuboid(-4.0F, -12.0F, -5.5F, 8.0F, 10.0F, 8.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-1.0F, -5.0F, -7.5F, 2.0F, 4.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -2.0F, 1.0F));

        ModelPartData right_arm = body.addChild("right_arm", ModelPartBuilder.create().uv(33, 32).cuboid(-6.0F, 2.5F, -3.0F, 6.0F, 8.0F, 6.0F, new Dilation(0.0F))
                .uv(51, 48).cuboid(-6.5F, -2.5F, -3.5F, 8.0F, 5.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(-9.0F, 0.0F, 0.0F));

        ModelPartData right_arm_muscle = right_arm.addChild("right_arm_muscle", ModelPartBuilder.create().uv(3, 70).cuboid(-8.0F, 3.5F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(-0.001F)), ModelTransform.pivot(3.8F, -1.0F, 0.0F));

        ModelPartData right_under_arm = right_arm.addChild("right_under_arm", ModelPartBuilder.create().uv(33, 40).cuboid(-3.0F, 0.0F, -3.0F, 6.0F, 8.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.0F, 10.5F, 0.0F));

        ModelPartData left_arm = body.addChild("left_arm", ModelPartBuilder.create().uv(51, 48).mirrored().cuboid(-1.5F, -2.5F, -3.5F, 8.0F, 5.0F, 7.0F, new Dilation(0.0F)).mirrored(false)
                .uv(33, 32).mirrored().cuboid(0.0F, 2.5F, -3.0F, 6.0F, 8.0F, 6.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(9.0F, 0.0F, 0.0F));

        ModelPartData left_arm_muscle = left_arm.addChild("left_arm_muscle", ModelPartBuilder.create().uv(3, 70).mirrored().cuboid(-8.0F, 3.5F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(-0.001F)).mirrored(false), ModelTransform.pivot(8.1F, -1.0F, 0.0F));

        ModelPartData left_under_arm = left_arm.addChild("left_under_arm", ModelPartBuilder.create().uv(33, 40).mirrored().cuboid(-3.0F, 0.0F, -3.0F, 6.0F, 8.0F, 6.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(3.0F, 10.5F, 0.0F));

        ModelPartData right_leg = body.addChild("right_leg", ModelPartBuilder.create().uv(0, 41).cuboid(-1.5F, -3.0F, -3.0F, 5.0F, 16.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(-4.0F, 18.0F, 0.0F));

        ModelPartData left_leg = body.addChild("left_leg", ModelPartBuilder.create().uv(0, 41).mirrored().cuboid(-4.5F, -3.0F, -3.0F, 5.0F, 16.0F, 6.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(5.0F, 18.0F, 0.0F));
        return TexturedModelData.of(modelData, 96, 96);
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    @Override
    public void setAngles(T buffChadVillagerEntity, float f, float g, float h, float i, float j) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);

        this.head.yaw = i * ((float) Math.PI / 180);
        this.head.pitch = j * ((float) Math.PI / 180);
        this.rightLeg.pitch = -1.5f * MathHelper.wrap(f, 13.0f) * g;
        this.leftLeg.pitch = 1.5f * MathHelper.wrap(f, 13.0f) * g;
        this.rightLeg.yaw = 0.0f;
        this.leftLeg.yaw = 0.0f;

        this.updateAnimation(buffChadVillagerEntity.getRightArmFlexingState(), RIGHT_ARM_FLEXING, h);
        this.updateAnimation(buffChadVillagerEntity.getLeftArmFlexingState(), LEFT_ARM_FLEXING, h);
        this.updateAnimation(buffChadVillagerEntity.getChestFlexingState(), CHEST_FLEXING, h);
        this.updateAnimation(buffChadVillagerEntity.getBodyFlexingState(), BODY_FLEXING, h);

        float tickDelta = MinecraftClient.getInstance().isPaused() ? 0f : MinecraftClient.getInstance().getTickDelta();
        int x = buffChadVillagerEntity.getAttackTicksLeft();
        if (x > 0) {
            if (this.rightArm.pitch == 0) {
                this.rightArm.pitch = -2.0f + 1.5f * MathHelper.wrap(x - tickDelta, 10.0f);
            }
            if (this.leftArm.pitch == 0) {
                this.leftArm.pitch = -2.0f + 1.5f * MathHelper.wrap(x - tickDelta, 10.0f);
            }
        } else {
            if (this.rightArm.pitch == 0) {
                this.rightArm.pitch = (-0.2f + 1.5f * MathHelper.wrap(f, 13.0f)) * g;
            }
            if (this.leftArm.pitch == 0) {
                this.leftArm.pitch = (-0.2f - 1.5f * MathHelper.wrap(f, 13.0f)) * g;
            }
        }

    }

    @Override
    public void animateModel(T buffChadVillagerEntity, float f, float g, float h) {
        int i = buffChadVillagerEntity.getAttackTicksLeft();
        if (i > 0) {
            this.rightArm.pitch = -2.0f + 1.5f * MathHelper.wrap((float) i - h, 10.0f);
            this.leftArm.pitch = -2.0f + 1.5f * MathHelper.wrap((float) i - h, 10.0f);
        } else {
            this.rightArm.pitch = (-0.2f + 1.5f * MathHelper.wrap(f, 13.0f)) * g;
            this.leftArm.pitch = (-0.2f - 1.5f * MathHelper.wrap(f, 13.0f)) * g;
        }
    }


    public static final Animation RIGHT_ARM_FLEXING = Animation.Builder.create(1.0834333f)
            .addBoneAnimation("right_arm",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.3433333f, AnimationHelper.createRotationalVector(-6.36f, 90f, 74.41f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.875f, AnimationHelper.createRotationalVector(-6.36f, 90f, 74.41f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1.0834333f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("right_under_arm",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0.25f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.3433333f, AnimationHelper.createRotationalVector(-40f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.875f, AnimationHelper.createRotationalVector(-40f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1.0834333f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("right_arm_muscle",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0.25f, AnimationHelper.createTranslationalVector(0f, 1f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.3433333f, AnimationHelper.createTranslationalVector(0f, 1f, -3f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.5f, AnimationHelper.createTranslationalVector(0f, 1f, -1.5f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.625f, AnimationHelper.createTranslationalVector(0f, 1f, -3f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.75f, AnimationHelper.createTranslationalVector(0f, 1f, -1.5f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.875f, AnimationHelper.createTranslationalVector(0f, 1f, -3f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.9583434f, AnimationHelper.createTranslationalVector(0f, 1f, 0f),
                                    Transformation.Interpolations.CUBIC))).build();
    public static final Animation CHEST_FLEXING = Animation.Builder.create(0.5f)
            .addBoneAnimation("right_chest",
                    new Transformation(Transformation.Targets.SCALE,
                            new Keyframe(0f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.125f, AnimationHelper.createScalingVector(1.2f, 1f, 1.3f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.20834334f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.2916767f, AnimationHelper.createScalingVector(1.2f, 1f, 1.3f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.375f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.4583433f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.CUBIC)))
            .addBoneAnimation("left_chest",
                    new Transformation(Transformation.Targets.SCALE,
                            new Keyframe(0f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.125f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.20834334f, AnimationHelper.createScalingVector(1.2f, 1f, 1.3f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.2916767f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.375f, AnimationHelper.createScalingVector(1.2f, 1f, 1.3f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.4583433f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.CUBIC))).build();
    public static final Animation LEFT_ARM_FLEXING = Animation.Builder.create(1.0834333f)
            .addBoneAnimation("left_arm",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.3433333f, AnimationHelper.createRotationalVector(6.36f, -90f, -89.4495f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.875f, AnimationHelper.createRotationalVector(6.36f, -90f, -89.4495f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1.0834333f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("left_under_arm",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0.25f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.3433333f, AnimationHelper.createRotationalVector(-40f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.875f, AnimationHelper.createRotationalVector(-40f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1.0834333f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("left_arm_muscle",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0.25f, AnimationHelper.createTranslationalVector(0f, 1f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.3433333f, AnimationHelper.createTranslationalVector(0f, 1f, -3f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.5f, AnimationHelper.createTranslationalVector(0f, 1f, -1.5f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.625f, AnimationHelper.createTranslationalVector(0f, 1f, -3f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.75f, AnimationHelper.createTranslationalVector(0f, 1f, -1.5f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.875f, AnimationHelper.createTranslationalVector(0f, 1f, -3f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.9583434f, AnimationHelper.createTranslationalVector(0f, 1f, 0f),
                                    Transformation.Interpolations.CUBIC))).build();
    public static final Animation BODY_FLEXING = Animation.Builder.create(1.0834333f)
            .addBoneAnimation("right_arm",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.3433333f, AnimationHelper.createRotationalVector(-6.36f, 90f, 74.41f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.875f, AnimationHelper.createRotationalVector(-6.36f, 90f, 74.41f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1.0834333f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("right_under_arm",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0.25f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.3433333f, AnimationHelper.createRotationalVector(-40f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.875f, AnimationHelper.createRotationalVector(-40f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1.0834333f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("right_arm_muscle",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0.25f, AnimationHelper.createTranslationalVector(0f, 1f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.3433333f, AnimationHelper.createTranslationalVector(0f, 1f, -3f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.5f, AnimationHelper.createTranslationalVector(0f, 1f, -1.5f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.625f, AnimationHelper.createTranslationalVector(0f, 1f, -3f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.75f, AnimationHelper.createTranslationalVector(0f, 1f, -1.5f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.875f, AnimationHelper.createTranslationalVector(0f, 1f, -3f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.9583434f, AnimationHelper.createTranslationalVector(0f, 1f, 0f),
                                    Transformation.Interpolations.CUBIC)))
            .addBoneAnimation("left_arm",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.3433333f, AnimationHelper.createRotationalVector(6.36f, -90f, -89.4495f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.875f, AnimationHelper.createRotationalVector(6.36f, -90f, -89.4495f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1.0834333f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("left_under_arm",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0.25f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.3433333f, AnimationHelper.createRotationalVector(-40f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.875f, AnimationHelper.createRotationalVector(-40f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1.0834333f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("left_arm_muscle",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0.25f, AnimationHelper.createTranslationalVector(0f, 1f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.3433333f, AnimationHelper.createTranslationalVector(0f, 1f, -3f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.5f, AnimationHelper.createTranslationalVector(0f, 1f, -1.5f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.625f, AnimationHelper.createTranslationalVector(0f, 1f, -3f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.75f, AnimationHelper.createTranslationalVector(0f, 1f, -1.5f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.875f, AnimationHelper.createTranslationalVector(0f, 1f, -3f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.9583434f, AnimationHelper.createTranslationalVector(0f, 1f, 0f),
                                    Transformation.Interpolations.CUBIC)))
            .addBoneAnimation("right_chest",
                    new Transformation(Transformation.Targets.SCALE,
                            new Keyframe(0f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.2916767f, AnimationHelper.createScalingVector(1.2f, 1f, 1.3f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.375f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.4583433f, AnimationHelper.createScalingVector(1.2f, 1f, 1.3f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.5416766f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.625f, AnimationHelper.createScalingVector(1.2f, 1f, 1.3f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.7083434f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.7916766f, AnimationHelper.createScalingVector(1.2f, 1f, 1.3f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.875f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.9583434f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.CUBIC)))
            .addBoneAnimation("left_chest",
                    new Transformation(Transformation.Targets.SCALE,
                            new Keyframe(0f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.2916767f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.375f, AnimationHelper.createScalingVector(1.2f, 1f, 1.3f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.4583433f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.5416766f, AnimationHelper.createScalingVector(1.2f, 1f, 1.3f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.625f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.7083434f, AnimationHelper.createScalingVector(1.2f, 1f, 1.3f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.7916766f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.875f, AnimationHelper.createScalingVector(1.2f, 1f, 1.3f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.9583434f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.CUBIC)))
            .addBoneAnimation("left_shoulder_muscle",
                    new Transformation(Transformation.Targets.SCALE,
                            new Keyframe(0.20834334f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.2916767f, AnimationHelper.createScalingVector(1.4f, 1.5f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.375f, AnimationHelper.createScalingVector(1.2f, 1.3f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.4583433f, AnimationHelper.createScalingVector(1.4f, 1.5f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.5416766f, AnimationHelper.createScalingVector(1.2f, 1.3f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.625f, AnimationHelper.createScalingVector(1.4f, 1.5f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.7083434f, AnimationHelper.createScalingVector(1.2f, 1.3f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.7916766f, AnimationHelper.createScalingVector(1.4f, 1.5f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.875f, AnimationHelper.createScalingVector(1.2f, 1.3f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("right_shoulder_muscle",
                    new Transformation(Transformation.Targets.SCALE,
                            new Keyframe(0.20834334f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.2916767f, AnimationHelper.createScalingVector(1.4f, 1.5f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.375f, AnimationHelper.createScalingVector(1.2f, 1.3f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.4583433f, AnimationHelper.createScalingVector(1.4f, 1.5f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.5416766f, AnimationHelper.createScalingVector(1.2f, 1.3f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.625f, AnimationHelper.createScalingVector(1.4f, 1.5f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.7083434f, AnimationHelper.createScalingVector(1.2f, 1.3f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.7916766f, AnimationHelper.createScalingVector(1.4f, 1.5f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.875f, AnimationHelper.createScalingVector(1.2f, 1.3f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.LINEAR))).build();
}

