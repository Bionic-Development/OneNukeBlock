package de.takacick.onegirlboyblock.client.entity.model.animation;

import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;

public class BitCannonAnimations {

    public static Animation getUsing(boolean left) {
        String item = left ? "left_item" : "right_item";
        String arm = left ? "left_arm" : "right_arm";
        String offArm = left ? "right_arm" : "left_arm";
        return Animation.Builder.create(2f)
                .addBoneAnimation("body",
                        new Transformation(Transformation.Targets.TRANSLATE,
                                new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                        Transformation.Interpolations.LINEAR),
                                new Keyframe(1f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                        Transformation.Interpolations.LINEAR),
                                new Keyframe(2f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                        Transformation.Interpolations.LINEAR)))
                .addBoneAnimation("body",
                        new Transformation(Transformation.Targets.ROTATE,
                                new Keyframe(0f, AnimationHelper.createRotationalVector(0f, -15f, 0f),
                                        Transformation.Interpolations.LINEAR),
                                new Keyframe(1f, AnimationHelper.createRotationalVector(0f, -15f, 0f),
                                        Transformation.Interpolations.LINEAR),
                                new Keyframe(2f, AnimationHelper.createRotationalVector(0f, -20f, 0f),
                                        Transformation.Interpolations.LINEAR)))
                .addBoneAnimation(arm,
                        new Transformation(Transformation.Targets.TRANSLATE,
                                new Keyframe(0f, AnimationHelper.createTranslationalVector(0.6f, 0f, -2.7f),
                                        Transformation.Interpolations.LINEAR),
                                new Keyframe(1f, AnimationHelper.createTranslationalVector(0.6f, 0f, -2.8f),
                                        Transformation.Interpolations.LINEAR),
                                new Keyframe(2f, AnimationHelper.createTranslationalVector(0.6f, 0f, -2.7f),
                                        Transformation.Interpolations.LINEAR)))
                .addBoneAnimation(arm,
                        new Transformation(Transformation.Targets.ROTATE,
                                new Keyframe(0f, AnimationHelper.createRotationalVector(-125.06f, -13.67f, 11.08f),
                                        Transformation.Interpolations.LINEAR),
                                new Keyframe(1f, AnimationHelper.createRotationalVector(-63.94f, -15.81f, -7.59f),
                                        Transformation.Interpolations.LINEAR),
                                new Keyframe(2f, AnimationHelper.createRotationalVector(-23.65f, -2.64f, -18.02f),
                                        Transformation.Interpolations.LINEAR)))
                .addBoneAnimation(offArm,
                        new Transformation(Transformation.Targets.TRANSLATE,
                                new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                        Transformation.Interpolations.LINEAR),
                                new Keyframe(1f, AnimationHelper.createTranslationalVector(1f, 1f, 1f),
                                        Transformation.Interpolations.LINEAR),
                                new Keyframe(2f, AnimationHelper.createTranslationalVector(1f, 1f, 1f),
                                        Transformation.Interpolations.LINEAR)))
                .addBoneAnimation(offArm,
                        new Transformation(Transformation.Targets.ROTATE,
                                new Keyframe(0f, AnimationHelper.createRotationalVector(-68.4f, 27.79f, 33.76f),
                                        Transformation.Interpolations.LINEAR),
                                new Keyframe(1f, AnimationHelper.createRotationalVector(-36.55f, 20.44f, 32.15f),
                                        Transformation.Interpolations.LINEAR),
                                new Keyframe(2f, AnimationHelper.createRotationalVector(-41.07f, 31.21f, 39.87f),
                                        Transformation.Interpolations.LINEAR)))
                .addBoneAnimation(item,
                        new Transformation(Transformation.Targets.TRANSLATE,
                                new Keyframe(0f, AnimationHelper.createTranslationalVector(3.5f, 4.7f, 5.3f),
                                        Transformation.Interpolations.LINEAR),
                                new Keyframe(1f, AnimationHelper.createTranslationalVector(4.6f, 7f, 4.3f),
                                        Transformation.Interpolations.LINEAR),
                                new Keyframe(2f, AnimationHelper.createTranslationalVector(4.7f, 7.6f, 0.7f),
                                        Transformation.Interpolations.LINEAR)))
                .addBoneAnimation(item,
                        new Transformation(Transformation.Targets.ROTATE,
                                new Keyframe(0f, AnimationHelper.createRotationalVector(43.95f, 0.9f, 17.24f),
                                        Transformation.Interpolations.LINEAR),
                                new Keyframe(1f, AnimationHelper.createRotationalVector(63.33f, -0.2f, 18.01f),
                                        Transformation.Interpolations.LINEAR),
                                new Keyframe(2f, AnimationHelper.createRotationalVector(93.97f, -5.55f, 17.11f),
                                        Transformation.Interpolations.LINEAR))).build();
    }
}
