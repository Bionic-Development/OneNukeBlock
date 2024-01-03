package de.takacick.raidbase.registry.block.entity.model;

import de.takacick.raidbase.registry.block.entity.PieLauncherBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.AnimationState;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PieLauncherModel extends Model {

    private static final Vector3f TEMP = new Vector3f();

    private final ModelPart root;
    private final ModelPart pie;
    private final ModelPart launcher;
    private final ModelPart ground;
    private final ModelPart bone;
    private final ModelPart bottom;

    public PieLauncherModel(ModelPart root) {
        super(RenderLayer::getEntityCutoutNoCull);

        this.root = root;
        this.bone = root.getChild("bone");
        this.pie = this.bone.getChild("pie");
        this.ground = this.bone.getChild("ground");
        this.launcher = this.bone.getChild("launcher");
        this.bottom = root.getChild("bottom");
    }

    public void setAngles(PieLauncherBlockEntity pieLauncherBlockEntity, float chargeProgress, float animationProgress) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        // this.pie.scale(new Vector3f(progress, progress, progress));
        this.pie.translate(new Vector3f(0, 0, 3.5f * (1 - chargeProgress)));

        if (pieLauncherBlockEntity.getShootState().getTimeRunning() > 1650f) {
            pieLauncherBlockEntity.getShootState().stop();
        }

        this.updateAnimation(pieLauncherBlockEntity.getShootState(), SHOOT, animationProgress);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        matrices.push();
        this.root.rotate(matrices);
        this.bone.rotate(matrices);
        this.ground.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        this.launcher.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        matrices.pop();
    }

    public void renderPie(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        matrices.push();
        this.root.rotate(matrices);
        this.bone.rotate(matrices);
        this.pie.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        matrices.pop();
    }

    public void renderBottom(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        matrices.push();
        this.root.rotate(matrices);
        this.bottom.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        matrices.pop();
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

    public ModelPart getPart() {
        return root;
    }

    public Optional<ModelPart> getChild(String name) {
        if (name.equals("root")) {
            return Optional.of(this.getPart());
        }
        return this.getPart().traverse().filter(part -> part.hasChild(name)).findFirst().map(part -> part.getChild(name));
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 20.0F, 0.0F));

        ModelPartData ground = bone.addChild("ground", ModelPartBuilder.create().uv(49, 33).cuboid(-12.0F, -5.0F, 4.0F, 8.0F, 1.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(8.0F, 4.0F, -8.0F));

        ModelPartData pie = bone.addChild("pie", ModelPartBuilder.create(), ModelTransform.pivot(-8.5F, 1.0F, -0.7F));

        ModelPartData pie_bottom = pie.addChild("pie_bottom", ModelPartBuilder.create().uv(27, 54).cuboid(-6.0F, -25.25F, -9.25F, 12.0F, 12.0F, 1.0F, new Dilation(0.0F))
                .uv(65, 29).cuboid(-5.0F, -26.25F, -9.25F, 10.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 43).cuboid(-3.0F, -27.25F, -9.25F, 6.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 40).cuboid(-3.0F, -12.25F, -9.25F, 6.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(37, 68).cuboid(-8.0F, -22.25F, -9.25F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F))
                .uv(49, 33).cuboid(7.0F, -22.25F, -9.25F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F))
                .uv(65, 26).cuboid(-5.0F, -13.25F, -9.25F, 10.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(54, 0).cuboid(-7.0F, -24.25F, -9.25F, 1.0F, 10.0F, 1.0F, new Dilation(0.0F))
                .uv(49, 0).cuboid(6.0F, -24.25F, -9.25F, 1.0F, 10.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(8.5F, 2.5F, 1.0F));

        ModelPartData pie_center = pie.addChild("pie_center", ModelPartBuilder.create().uv(21, 68).cuboid(-8.5F, -22.25F, -11.25F, 1.0F, 6.0F, 2.0F, new Dilation(0.0F))
                .uv(52, 67).cuboid(-3.0F, -11.75F, -11.25F, 6.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(65, 47).cuboid(-3.0F, -27.75F, -11.25F, 6.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(7, 68).cuboid(-7.5F, -24.25F, -11.25F, 1.0F, 10.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 68).cuboid(6.5F, -24.25F, -11.25F, 1.0F, 10.0F, 2.0F, new Dilation(0.0F))
                .uv(65, 22).cuboid(-5.0F, -12.75F, -11.25F, 10.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(65, 18).cuboid(-5.0F, -26.75F, -11.25F, 10.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(65, 14).cuboid(-6.5F, -13.75F, -11.25F, 13.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(49, 43).cuboid(-6.5F, -25.75F, -11.25F, 13.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 54).cuboid(-5.5F, -24.75F, -10.75F, 11.0F, 11.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 33).cuboid(-2.0F, -21.25F, -11.25F, 4.0F, 4.0F, 2.0F, new Dilation(0.0F))
                .uv(28, 68).cuboid(2.5F, -22.75F, -11.25F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(67, 69).cuboid(0.0F, -15.75F, -11.25F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(42, 68).cuboid(-4.5F, -23.25F, -11.25F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(7, 0).cuboid(5.5F, -24.75F, -11.25F, 1.0F, 11.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-6.5F, -24.75F, -11.25F, 1.0F, 11.0F, 2.0F, new Dilation(0.0F))
                .uv(14, 68).cuboid(7.5F, -22.25F, -11.25F, 1.0F, 6.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(8.5F, 2.5F, 1.0F));

        ModelPartData pie_top = pie.addChild("pie_top", ModelPartBuilder.create().uv(46, 75).cuboid(14.0F, -11.25F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(41, 75).cuboid(16.0F, -19.75F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(48, 71).cuboid(16.0F, -17.75F, -10.75F, 1.0F, 2.0F, 1.0F, new Dilation(0.001F))
                .uv(32, 75).cuboid(16.0F, -14.75F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(57, 74).cuboid(15.0F, -13.75F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(52, 74).cuboid(15.0F, -20.75F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(74, 38).cuboid(1.0F, -20.75F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(74, 35).cuboid(0.0F, -19.75F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(54, 54).cuboid(0.0F, -17.75F, -10.75F, 1.0F, 2.0F, 1.0F, new Dilation(0.001F))
                .uv(74, 32).cuboid(0.0F, -14.75F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(73, 72).cuboid(1.0F, -13.75F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(68, 73).cuboid(11.5F, -24.25F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(73, 57).cuboid(10.5F, -25.25F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(7, 46).cuboid(7.5F, -25.25F, -10.75F, 2.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(73, 54).cuboid(5.5F, -25.25F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(28, 73).cuboid(4.5F, -24.25F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(72, 67).cuboid(11.5F, -10.25F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(63, 72).cuboid(10.5F, -9.25F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(0, 46).cuboid(7.5F, -9.25F, -10.75F, 2.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(42, 72).cuboid(5.5F, -9.25F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(58, 71).cuboid(4.5F, -10.25F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(53, 71).cuboid(14.0F, -23.25F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(70, 51).cuboid(2.0F, -23.25F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(65, 51).cuboid(2.0F, -11.25F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData launcher = bone.addChild("launcher", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -0.1667F, 0.0F));

        ModelPartData dispenser = launcher.addChild("dispenser", ModelPartBuilder.create().uv(0, 0).cuboid(-16.0F, -28.0F, 0.0F, 16.0F, 16.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(8.0F, 4.1667F, -8.0F));

        ModelPartData legs = launcher.addChild("legs", ModelPartBuilder.create().uv(54, 54).cuboid(-11.0F, -11.0F, 5.0F, 6.0F, 6.0F, 6.0F, new Dilation(0.0F))
                .uv(49, 0).cuboid(-14.0F, -12.0F, 2.0F, 12.0F, 1.0F, 12.0F, new Dilation(0.0F)), ModelTransform.pivot(8.0F, 4.1667F, -8.0F));

        ModelPartData bottom = modelPartData.addChild("bottom", ModelPartBuilder.create().uv(0, 33).cuboid(-8.0F, -0.75F, -8.0F, 16.0F, 4.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 20.75F, 0.0F));
        return TexturedModelData.of(modelData, 128, 128);
    }

    @Environment(value = EnvType.CLIENT)
    public static class AnimationHelper {
        public static void animate(PieLauncherModel model, Animation animation, long runningTime, float scale, Vector3f tempVec) {
            float f = getRunningSeconds(animation, runningTime);
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
                    transformation.target().apply((ModelPart) part, tempVec);
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


    public static final Animation SHOOT = Animation.Builder.create(1.625f)
            .addBoneAnimation("bone",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("launcher",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.25f, AnimationHelper.createTranslationalVector(0f, -1f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.41667f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.5f, AnimationHelper.createTranslationalVector(0f, -0.5f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.66667f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.75f, AnimationHelper.createTranslationalVector(0f, -0.5f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.91667f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1f, AnimationHelper.createTranslationalVector(0f, -0.25f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.16667f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC)))
            .addBoneAnimation("launcher",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.25f, AnimationHelper.createRotationalVector(-20f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.5f, AnimationHelper.createRotationalVector(7.5f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.75f, AnimationHelper.createRotationalVector(-5f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1f, AnimationHelper.createRotationalVector(3f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.16667f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.33333f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC))).build();

}