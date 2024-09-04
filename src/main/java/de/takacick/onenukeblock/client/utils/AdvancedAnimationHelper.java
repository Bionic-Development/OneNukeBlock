package de.takacick.onenukeblock.client.utils;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Environment(value = EnvType.CLIENT)
public class AdvancedAnimationHelper {
    public static void animateLerpPitch(SinglePartEntityModel<?> model, Animation animation, Animation lerpAnimation, float lerpProgress, long runningTime, float animationScale, Vector3f tempVec, boolean mirror) {
        lerpProgress -= 1f;
        boolean lower = lerpProgress < 0;
        lerpProgress = lower ? -lerpProgress : lerpProgress;

        float f = AdvancedAnimationHelper.getRunningSeconds(animation, runningTime);
        for (Map.Entry<String, List<Transformation>> entry : animation.boneAnimations().entrySet()) {
            Optional<ModelPart> optional = model.getChild(entry.getKey());
            List<Transformation> list = entry.getValue();

            List<Transformation> animationList = lerpAnimation.boneAnimations().getOrDefault(entry.getKey(), List.of());

            float finalPitch = lerpProgress;
            optional.ifPresent(part -> {
                Vector3f translation = new Vector3f();
                Vector3f rotation = new Vector3f();
                Vector3f scale = new Vector3f();

                list.forEach(transformation -> {
                    Keyframe[] keyframes = transformation.keyframes();
                    int i = Math.max(0, MathHelper.binarySearch(0, keyframes.length, index -> f <= keyframes[index].timestamp()) - 1);
                    int j = Math.min(keyframes.length - 1, i + 1);
                    Keyframe keyframe = keyframes[i];
                    Keyframe keyframe2 = keyframes[j];
                    float h = f - keyframe.timestamp();
                    float k = j != i ? MathHelper.clamp(h / (keyframe2.timestamp() - keyframe.timestamp()), 0.0f, 1.0f) : 0.0f;
                    keyframe2.interpolation().apply(tempVec, k, keyframes, i, j, animationScale);

                    if (transformation.target().equals(Transformation.Targets.TRANSLATE)) {
                        translation.set(translation.x() + tempVec.x(), translation.y() + tempVec.y(), translation.z() + tempVec.z());
                    } else if (transformation.target().equals(Transformation.Targets.ROTATE)) {
                        rotation.set(rotation.x() + tempVec.x(), rotation.y() + tempVec.y(), rotation.z() + tempVec.z());
                    } else if (transformation.target().equals(Transformation.Targets.SCALE)) {
                        scale.set(scale.x() + tempVec.x(), scale.y() + tempVec.y(), scale.z() + tempVec.z());
                    }
                });

                Vector3f pitchTranslation = new Vector3f();
                Vector3f pitchRotation = new Vector3f();
                Vector3f pitchScale = new Vector3f();

                animationList.forEach(transformation -> {
                    Keyframe[] keyframes = transformation.keyframes();
                    int i = Math.max(0, MathHelper.binarySearch(0, keyframes.length, index -> f <= keyframes[index].timestamp()) - 1);
                    int j = Math.min(keyframes.length - 1, i + 1);
                    Keyframe keyframe = keyframes[i];
                    Keyframe keyframe2 = keyframes[j];
                    float h = f - keyframe.timestamp();
                    float k = j != i ? MathHelper.clamp(h / (keyframe2.timestamp() - keyframe.timestamp()), 0.0f, 1.0f) : 0.0f;
                    keyframe2.interpolation().apply(tempVec, k, keyframes, i, j, animationScale);

                    if (transformation.target().equals(Transformation.Targets.TRANSLATE)) {
                        pitchTranslation.set(pitchTranslation.x() + tempVec.x(), pitchTranslation.y() + tempVec.y(), pitchTranslation.z() + tempVec.z());
                    } else if (transformation.target().equals(Transformation.Targets.ROTATE)) {
                        pitchRotation.set(pitchRotation.x() + tempVec.x(), pitchRotation.y() + tempVec.y(), pitchRotation.z() + tempVec.z());
                    } else if (transformation.target().equals(Transformation.Targets.SCALE)) {
                        pitchScale.set(pitchScale.x() + tempVec.x(), pitchScale.y() + tempVec.y(), pitchScale.z() + tempVec.z());
                    }
                });

                if (!lower) {
                    Transformation.Targets.TRANSLATE.apply(part, translation.mul(1f - finalPitch)
                            .add(pitchTranslation.mul(finalPitch)).mul(mirror ? 1 : -1, 1, 1));
                    Transformation.Targets.ROTATE.apply(part, rotation.mul(1f - finalPitch)
                            .add(pitchRotation.mul(finalPitch)).mul(1, mirror ? 1 : -1, mirror ? 1 : -1));
                    Transformation.Targets.SCALE.apply(part, scale.mul(1f - finalPitch).add(pitchScale.mul(finalPitch)));
                } else {
                    Transformation.Targets.TRANSLATE.apply(part, translation.mul(1f - finalPitch)
                            .add(pitchTranslation.mul(finalPitch)).mul(mirror ? 1 : -1, 1, 1));
                    Transformation.Targets.ROTATE.apply(part, rotation.mul(1f - finalPitch)
                            .add(pitchRotation.mul(finalPitch)).mul(1, mirror ? 1 : -1, mirror ? 1 : -1));
                    Transformation.Targets.SCALE.apply(part, scale.mul(1f - finalPitch).add(pitchScale.mul(finalPitch)));
                }
            });
        }
    }

    public static void animateLerp(SinglePartEntityModel<?> model, Animation animation, Animation lerpAnimation, float lerpProgress, long runningTime, float animationScale, Vector3f tempVec, boolean mirror) {

        float f = AdvancedAnimationHelper.getRunningSeconds(animation, runningTime);
        for (Map.Entry<String, List<Transformation>> entry : animation.boneAnimations().entrySet()) {
            Optional<ModelPart> optional = model.getChild(entry.getKey());
            List<Transformation> list = entry.getValue();

            List<Transformation> animationList = lerpAnimation.boneAnimations().getOrDefault(entry.getKey(), List.of());

            optional.ifPresent(part -> {
                Vector3f translation = new Vector3f();
                Vector3f rotation = new Vector3f();
                Vector3f scale = new Vector3f();

                list.forEach(transformation -> {
                    Keyframe[] keyframes = transformation.keyframes();
                    int i = Math.max(0, MathHelper.binarySearch(0, keyframes.length, index -> f <= keyframes[index].timestamp()) - 1);
                    int j = Math.min(keyframes.length - 1, i + 1);
                    Keyframe keyframe = keyframes[i];
                    Keyframe keyframe2 = keyframes[j];
                    float h = f - keyframe.timestamp();
                    float k = j != i ? MathHelper.clamp(h / (keyframe2.timestamp() - keyframe.timestamp()), 0.0f, 1.0f) : 0.0f;
                    keyframe2.interpolation().apply(tempVec, k, keyframes, i, j, animationScale);

                    if (transformation.target().equals(Transformation.Targets.TRANSLATE)) {
                        translation.set(translation.x() + tempVec.x(), translation.y() + tempVec.y(), translation.z() + tempVec.z());
                    } else if (transformation.target().equals(Transformation.Targets.ROTATE)) {
                        rotation.set(rotation.x() + tempVec.x(), rotation.y() + tempVec.y(), rotation.z() + tempVec.z());
                    } else if (transformation.target().equals(Transformation.Targets.SCALE)) {
                        scale.set(scale.x() + tempVec.x(), scale.y() + tempVec.y(), scale.z() + tempVec.z());
                    }
                });

                Vector3f lerpTranslation = new Vector3f();
                Vector3f lerpRotation = new Vector3f();
                Vector3f lerpScale = new Vector3f();

                animationList.forEach(transformation -> {
                    Keyframe[] keyframes = transformation.keyframes();
                    int i = Math.max(0, MathHelper.binarySearch(0, keyframes.length, index -> f <= keyframes[index].timestamp()) - 1);
                    int j = Math.min(keyframes.length - 1, i + 1);
                    Keyframe keyframe = keyframes[i];
                    Keyframe keyframe2 = keyframes[j];
                    float h = f - keyframe.timestamp();
                    float k = j != i ? MathHelper.clamp(h / (keyframe2.timestamp() - keyframe.timestamp()), 0.0f, 1.0f) : 0.0f;
                    keyframe2.interpolation().apply(tempVec, k, keyframes, i, j, animationScale);

                    if (transformation.target().equals(Transformation.Targets.TRANSLATE)) {
                        lerpTranslation.set(lerpTranslation.x() + tempVec.x(), lerpTranslation.y() + tempVec.y(), lerpTranslation.z() + tempVec.z());
                    } else if (transformation.target().equals(Transformation.Targets.ROTATE)) {
                        lerpRotation.set(lerpRotation.x() + tempVec.x(), lerpRotation.y() + tempVec.y(), lerpRotation.z() + tempVec.z());
                    } else if (transformation.target().equals(Transformation.Targets.SCALE)) {
                        lerpScale.set(lerpScale.x() + tempVec.x(), lerpScale.y() + tempVec.y(), lerpScale.z() + tempVec.z());
                    }
                });

                Transformation.Targets.TRANSLATE.apply(part, applyLerp(translation, lerpTranslation, lerpProgress));
                Transformation.Targets.ROTATE.apply(part, applyLerp(rotation, lerpRotation, lerpProgress));
                Transformation.Targets.SCALE.apply(part, applyLerp(scale, lerpScale, lerpProgress));
            });
        }
    }

    public static void animate(SinglePartEntityModel<?> model, Animation animation, long runningTime, float animationScale, Vector3f tempVec, boolean rightHand) {
        float f = AdvancedAnimationHelper.getRunningSeconds(animation, runningTime);
        for (Map.Entry<String, List<Transformation>> entry : animation.boneAnimations().entrySet()) {
            Optional<ModelPart> optional = model.getChild(entry.getKey());
            List<Transformation> list = entry.getValue();

            optional.ifPresent(part -> {
                Vector3f translation = new Vector3f();
                Vector3f rotation = new Vector3f();
                Vector3f scale = new Vector3f();

                list.forEach(transformation -> {
                    Keyframe[] keyframes = transformation.keyframes();
                    int i = Math.max(0, MathHelper.binarySearch(0, keyframes.length, index -> f <= keyframes[index].timestamp()) - 1);
                    int j = Math.min(keyframes.length - 1, i + 1);
                    Keyframe keyframe = keyframes[i];
                    Keyframe keyframe2 = keyframes[j];
                    float h = f - keyframe.timestamp();
                    float k = j != i ? MathHelper.clamp(h / (keyframe2.timestamp() - keyframe.timestamp()), 0.0f, 1.0f) : 0.0f;
                    keyframe2.interpolation().apply(tempVec, k, keyframes, i, j, animationScale);

                    if (transformation.target().equals(Transformation.Targets.TRANSLATE)) {
                        translation.set(translation.x() + tempVec.x() * (rightHand ? 1 : -1), translation.y() + tempVec.y(), translation.z() + tempVec.z());
                    } else if (transformation.target().equals(Transformation.Targets.ROTATE)) {
                        rotation.set(rotation.x() + tempVec.x(), rotation.y() + tempVec.y() * (rightHand ? 1 : -1), rotation.z() + tempVec.z() * (rightHand ? 1 : -1));
                    } else if (transformation.target().equals(Transformation.Targets.SCALE)) {
                        scale.set(scale.x() + tempVec.x(), scale.y() + tempVec.y(), scale.z() + tempVec.z());
                    }
                });

                Transformation.Targets.TRANSLATE.apply(part, translation);
                Transformation.Targets.ROTATE.apply(part, rotation);
                Transformation.Targets.SCALE.apply(part, scale);
            });
        }
    }

    private static float applyLerp(float start, float target, float speed) {
        return start - ((start - target) * speed);
    }

    private static Vector3f applyLerp(Vector3f pos1, Vector3f pos2, float speed) {
        return new Vector3f(applyLerp(pos1.x(), pos2.x(), speed), applyLerp(pos1.y(), pos2.y(), speed), applyLerp(pos1.z(), pos2.z(), speed));
    }

    private static float getRunningSeconds(Animation animation, long runningTime) {
        float f = (float) runningTime / 1000.0f;
        return animation.looping() ? f % animation.lengthInSeconds() : f;
    }

    public static Vector3f createTranslationalVector(float x, float y, float z) {
        return new Vector3f(x, -y, z);
    }

    public static Vector3f createRotationalVector(float x, float y, float z) {
        return new Vector3f(x * ((float) Math.PI / 180), y * ((float) Math.PI / 180), z * ((float) Math.PI / 180));
    }

    public static Vector3f createScalingVector(double x, double y, double z) {
        return new Vector3f((float) (x - 1.0), (float) (y - 1.0), (float) (z - 1.0));
    }
}

