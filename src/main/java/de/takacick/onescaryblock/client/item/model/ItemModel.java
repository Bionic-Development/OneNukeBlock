package de.takacick.onescaryblock.client.item.model;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public abstract class ItemModel extends Model {

    private static final Vector3f TEMP = new Vector3f();

    public ItemModel(Function<Identifier, RenderLayer> layerFactory) {
        super(layerFactory);
    }

    public abstract void setAngles(LivingEntity livingEntity,ItemStack itemStack, long time, float tickDelta);

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        this.getPart().render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }

    public void renderFeatures(@Nullable LivingEntity livingEntity, ModelTransformationMode modelTransformationMode, ItemStack itemStack, MatrixStack matrices, long time, float tickDelta, VertexConsumerProvider vertices, int light, int overlay, float red, float green, float blue, float alpha) {

    }

    public boolean shouldRender(@Nullable LivingEntity livingEntity, ItemStack itemStack, ModelTransformationMode modelTransformationMode) {
        return true;
    }

    public boolean shouldIgnoreItemTransforms(ItemStack itemStack) {
        return false;
    }

    public abstract ModelPart getPart();

    public Optional<ModelPart> getChild(String name) {
        if (name.equals("root")) {
            return Optional.of(this.getPart());
        }
        return this.getPart().traverse().filter(part -> part.hasChild(name)).findFirst().map(part -> part.getChild(name));
    }

    protected void updateAnimation(AnimationState animationState, Animation animation, float animationProgress) {
        this.updateAnimation(animationState, animation, animationProgress, 1.0f);
    }

    protected void animateMovement(Animation animation, float limbAngle, float limbDistance, float limbAngleScale, float limbDistanceScale) {
        long l = (long) (limbAngle * 50.0f * limbAngleScale);
        float f = Math.min(limbDistance * limbDistanceScale, 1.0f);
        AnimationHelper.animate(this, animation, l, f, TEMP);
    }

    protected void updateAnimation(AnimationState animationState, Animation animation, float animationProgress, float speedMultiplier) {
        animationState.update(animationProgress, speedMultiplier);
        animationState.run(state -> AnimationHelper.animate(this, animation, state.getTimeRunning(), 1.0f, TEMP));
    }

    protected void animate(Animation animation) {
        AnimationHelper.animate(this, animation, 0L, 1.0f, TEMP);
    }

    public abstract Identifier getTexture(ItemStack itemStack);

    protected static class AnimationHelper {
        public static void animate(ItemModel model, Animation animation, long runningTime, float scale, Vector3f tempVec) {
            float f = AnimationHelper.getRunningSeconds(animation, runningTime);
            for (Map.Entry<String, List<Transformation>> entry : animation.boneAnimations().entrySet()) {
                Optional<ModelPart> optional = model.getChild(entry.getKey());
                List<Transformation> list = entry.getValue();
                optional.ifPresent(part -> list.forEach(transformation -> {
                    Keyframe[] keyframes = transformation.keyframes();
                    int i = Math.max(0, MathHelper.binarySearch(0, keyframes.length, index -> f <= keyframes[index].timestamp()) - 1);
                    int j = Math.min(keyframes.length - 1, i + 1);
                    Keyframe keyframe = keyframes[i];
                    Keyframe keyframe2 = keyframes[j];
                    float h = f - keyframe.timestamp();
                    float k = j != i ? MathHelper.clamp(h / (keyframe2.timestamp() - keyframe.timestamp()), 0.0f, 1.0f) : 0.0f;
                    keyframe2.interpolation().apply(tempVec, k, keyframes, i, j, scale);
                    transformation.target().apply(part, tempVec);
                }));
            }
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
}
