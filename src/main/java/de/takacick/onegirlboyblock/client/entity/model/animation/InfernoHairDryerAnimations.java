package de.takacick.onegirlboyblock.client.entity.model.animation;

import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;

public class InfernoHairDryerAnimations {

    public static Animation getUsing(boolean left) {
        String item = left ? "left_item" : "right_item";
        String arm = left ? "left_arm" : "right_arm";
        String offArm = left ? "right_arm" : "left_arm";
        return Animation.Builder.create(2f)
                .addBoneAnimation(arm,
                        new Transformation(Transformation.Targets.TRANSLATE,
                                new Keyframe(0f, AnimationHelper.createTranslationalVector(0.6f, 0f, -2.7f),
                                        Transformation.Interpolations.CUBIC),
                                new Keyframe(0.5f, AnimationHelper.createTranslationalVector(0.6f, 0f, -2.25f),
                                        Transformation.Interpolations.CUBIC),
                                new Keyframe(1f, AnimationHelper.createTranslationalVector(0.6f, 0f, -1.8f),
                                        Transformation.Interpolations.CUBIC),
                                new Keyframe(2f, AnimationHelper.createTranslationalVector(0.6f, 0f, -2.7f),
                                        Transformation.Interpolations.CUBIC)))
                .addBoneAnimation(arm,
                        new Transformation(Transformation.Targets.ROTATE,
                                new Keyframe(0f, AnimationHelper.createRotationalVector(-124.56f, -14.14f, 19.19f),
                                        Transformation.Interpolations.CUBIC),
                                new Keyframe(0.5f, AnimationHelper.createRotationalVector(-93.25f, -25.53f, 3.04f),
                                        Transformation.Interpolations.CUBIC),
                                new Keyframe(1f, AnimationHelper.createRotationalVector(-61.7f, -26.95f, -13.71f),
                                        Transformation.Interpolations.CUBIC),
                                new Keyframe(2f, AnimationHelper.createRotationalVector(-23.36f, -4.64f, -22.61f),
                                        Transformation.Interpolations.CUBIC)))
                .addBoneAnimation(offArm,
                        new Transformation(Transformation.Targets.TRANSLATE,
                                new Keyframe(0f, AnimationHelper.createTranslationalVector(-0.6f, 0f, -2.7f),
                                        Transformation.Interpolations.CUBIC),
                                new Keyframe(0.5f, AnimationHelper.createTranslationalVector(-0.6f, 0f, -2.25f),
                                        Transformation.Interpolations.CUBIC),
                                new Keyframe(1f, AnimationHelper.createTranslationalVector(-0.6f, 0f, -1.8f),
                                        Transformation.Interpolations.CUBIC),
                                new Keyframe(2f, AnimationHelper.createTranslationalVector(-0.6f, 0f, -2.7f),
                                        Transformation.Interpolations.CUBIC)))
                .addBoneAnimation(offArm,
                        new Transformation(Transformation.Targets.ROTATE,
                                new Keyframe(0f, AnimationHelper.createRotationalVector(-124.56f, 14.14f, -19.19f),
                                        Transformation.Interpolations.CUBIC),
                                new Keyframe(0.5f, AnimationHelper.createRotationalVector(-93.25f, 25.53f, -3.04f),
                                        Transformation.Interpolations.CUBIC),
                                new Keyframe(1f, AnimationHelper.createRotationalVector(-61.7f, 26.95f, 13.71f),
                                        Transformation.Interpolations.CUBIC),
                                new Keyframe(2f, AnimationHelper.createRotationalVector(-23.36f, 4.64f, 22.61f),
                                        Transformation.Interpolations.CUBIC)))
                .addBoneAnimation(item,
                        new Transformation(Transformation.Targets.TRANSLATE,
                                new Keyframe(0f, AnimationHelper.createTranslationalVector(2.9f, 3.2f, 2.3f),
                                        Transformation.Interpolations.CUBIC),
                                new Keyframe(0.5f, AnimationHelper.createTranslationalVector(2.8f, 4f, 1.8f),
                                        Transformation.Interpolations.CUBIC),
                                new Keyframe(1f, AnimationHelper.createTranslationalVector(2.3f, 3.8f, 1.3f),
                                        Transformation.Interpolations.CUBIC),
                                new Keyframe(1.5f, AnimationHelper.createTranslationalVector(3.28f, 4.1f, 1.3f),
                                        Transformation.Interpolations.CUBIC),
                                new Keyframe(2f, AnimationHelper.createTranslationalVector(3.2f, 4.2f, 1.3f),
                                        Transformation.Interpolations.CUBIC)))
                .addBoneAnimation(item,
                        new Transformation(Transformation.Targets.ROTATE,
                                new Keyframe(0f, AnimationHelper.createRotationalVector(50.75f, 6.53f, 24.06f),
                                        Transformation.Interpolations.CUBIC),
                                new Keyframe(0.5f, AnimationHelper.createRotationalVector(58.31f, 1.2f, 26.17f),
                                        Transformation.Interpolations.CUBIC),
                                new Keyframe(1f, AnimationHelper.createRotationalVector(66.23f, -0.21f, 29.18f),
                                        Transformation.Interpolations.CUBIC),
                                new Keyframe(1.5f, AnimationHelper.createRotationalVector(79.97f, -0.16f, 26.51f),
                                        Transformation.Interpolations.CUBIC),
                                new Keyframe(2f, AnimationHelper.createRotationalVector(93.57f, -3.45f, 23.28f),
                                        Transformation.Interpolations.CUBIC))).build();
    }

}
