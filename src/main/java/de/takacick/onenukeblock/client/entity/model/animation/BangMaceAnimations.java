package de.takacick.onenukeblock.client.entity.model.animation;

import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;

public class BangMaceAnimations {

    public static Animation getUsing(boolean right) {
        String arm = right ? "right_arm" : "left_arm";
        String offArm = right ? "left_arm" : "right_arm";
        String item = right ? "right_item" : "left_item";
        String rightLeg = right ? "right_leg" : "left_leg";
        String leftLeg = right ? "left_leg" : "right_leg";

        return Animation.Builder.create(0.5f)
                .addBoneAnimation(arm,
                        new Transformation(Transformation.Targets.TRANSLATE,
                                new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                        Transformation.Interpolations.LINEAR),
                                new Keyframe(0.5f, AnimationHelper.createTranslationalVector(0f, 0f, 0.6f),
                                        Transformation.Interpolations.LINEAR)))
                .addBoneAnimation(arm,
                        new Transformation(Transformation.Targets.ROTATE,
                                new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                        Transformation.Interpolations.LINEAR),
                                new Keyframe(0.5f, AnimationHelper.createRotationalVector(-67.69f, -25.72f, -10.1f),
                                        Transformation.Interpolations.LINEAR)))
                .addBoneAnimation(item,
                        new Transformation(Transformation.Targets.TRANSLATE,
                                new Keyframe(0.20834334f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                        Transformation.Interpolations.LINEAR),
                                new Keyframe(0.5f, AnimationHelper.createTranslationalVector(1f, 1.4f, 0.9f),
                                        Transformation.Interpolations.LINEAR)))
                .addBoneAnimation(item,
                        new Transformation(Transformation.Targets.ROTATE,
                                new Keyframe(0.20834334f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                        Transformation.Interpolations.LINEAR),
                                new Keyframe(0.5f, AnimationHelper.createRotationalVector(170.25f, -0.28f, 27.15f),
                                        Transformation.Interpolations.LINEAR)))
                .addBoneAnimation("bone",
                        new Transformation(Transformation.Targets.ROTATE,
                                new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                        Transformation.Interpolations.LINEAR),
                                new Keyframe(0.5f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                        Transformation.Interpolations.LINEAR)))
                .addBoneAnimation("head",
                        new Transformation(Transformation.Targets.TRANSLATE,
                                new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                        Transformation.Interpolations.LINEAR),
                                new Keyframe(0.2916767f, AnimationHelper.createTranslationalVector(0f, 0f, 2f),
                                        Transformation.Interpolations.LINEAR)))
                .addBoneAnimation("body",
                        new Transformation(Transformation.Targets.TRANSLATE,
                                new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                        Transformation.Interpolations.LINEAR),
                                new Keyframe(0.2916767f, AnimationHelper.createTranslationalVector(0f, 0f, 1.4f),
                                        Transformation.Interpolations.LINEAR)))
                .addBoneAnimation("body",
                        new Transformation(Transformation.Targets.ROTATE,
                                new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                        Transformation.Interpolations.LINEAR),
                                new Keyframe(0.2916767f, AnimationHelper.createRotationalVector(2.72f, 12.49f, 0.59f),
                                        Transformation.Interpolations.LINEAR)))
                .addBoneAnimation(offArm,
                        new Transformation(Transformation.Targets.TRANSLATE,
                                new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                        Transformation.Interpolations.LINEAR),
                                new Keyframe(0.5f, AnimationHelper.createTranslationalVector(0f, 0f, 0.6f),
                                        Transformation.Interpolations.LINEAR)))
                .addBoneAnimation(offArm,
                        new Transformation(Transformation.Targets.ROTATE,
                                new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                        Transformation.Interpolations.LINEAR),
                                new Keyframe(0.5f, AnimationHelper.createRotationalVector(-68.44f, 25.72f, 10.1f),
                                        Transformation.Interpolations.LINEAR)))
                .addBoneAnimation(leftLeg,
                        new Transformation(Transformation.Targets.TRANSLATE,
                                new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                        Transformation.Interpolations.LINEAR),
                                new Keyframe(0.2916767f, AnimationHelper.createTranslationalVector(0f, 1.5f, 0f),
                                        Transformation.Interpolations.LINEAR),
                                new Keyframe(0.4167667f, AnimationHelper.createTranslationalVector(0f, 0f, 2f),
                                        Transformation.Interpolations.LINEAR)))
                .addBoneAnimation(leftLeg,
                        new Transformation(Transformation.Targets.ROTATE,
                                new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                        Transformation.Interpolations.LINEAR),
                                new Keyframe(0.2916767f, AnimationHelper.createRotationalVector(3.52f, 0.1f, -9.66f),
                                        Transformation.Interpolations.LINEAR),
                                new Keyframe(0.4167667f, AnimationHelper.createRotationalVector(5.43f, 1.45f, -13.67f),
                                        Transformation.Interpolations.LINEAR)))
                .addBoneAnimation(rightLeg,
                        new Transformation(Transformation.Targets.TRANSLATE,
                                new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                        Transformation.Interpolations.LINEAR),
                                new Keyframe(0.125f, AnimationHelper.createTranslationalVector(0f, 1.5f, 0f),
                                        Transformation.Interpolations.LINEAR),
                                new Keyframe(0.25f, AnimationHelper.createTranslationalVector(0f, 0f, 2f),
                                        Transformation.Interpolations.LINEAR)))
                .addBoneAnimation(rightLeg,
                        new Transformation(Transformation.Targets.ROTATE,
                                new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                        Transformation.Interpolations.LINEAR),
                                new Keyframe(0.125f, AnimationHelper.createRotationalVector(3.43f, -0.81f, 5.31f),
                                        Transformation.Interpolations.LINEAR),
                                new Keyframe(0.25f, AnimationHelper.createRotationalVector(-4.71f, -2.43f, 15.93f),
                                        Transformation.Interpolations.LINEAR)))
                .build();
    }
}
