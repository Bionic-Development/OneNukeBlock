package de.takacick.secretgirlbase.registry.block.entity.model;

import de.takacick.secretgirlbase.registry.block.entity.BubbleGumLauncherBlockEntity;
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

public class BubbleGumLauncherBlockEntityModel extends Model {

    private static final Vector3f TEMP = new Vector3f();

    private final ModelPart root;
    private final ModelPart bubbleGum;
    private final ModelPart launcher;
    private final ModelPart ground;
    private final ModelPart bone;
    private final ModelPart bottom;

    public BubbleGumLauncherBlockEntityModel(ModelPart root) {
        super(RenderLayer::getEntityTranslucent);

        this.root = root;
        this.bone = root.getChild("bone");
        this.ground = this.bone.getChild("ground");
        this.launcher = this.bone.getChild("launcher");
        this.bubbleGum = this.bone.getChild("bubble_gum");
        this.bottom = root.getChild("bottom");
    }

    public void setAngles(BubbleGumLauncherBlockEntity bubbleGumLauncherBlockEntity, float chargeProgress, float animationProgress) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);

        if (bubbleGumLauncherBlockEntity.getShootState().getTimeRunning() > 1650f) {
            bubbleGumLauncherBlockEntity.getShootState().stop();
        }

        this.updateAnimation(bubbleGumLauncherBlockEntity.getShootState(), SHOOT, animationProgress);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        matrices.push();
        this.root.rotate(matrices);
        this.bone.rotate(matrices);
        this.bubbleGum.visible = false;
        this.ground.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        this.launcher.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        this.bubbleGum.visible = true;
        matrices.pop();
    }

    public void renderBubbleGum(MatrixStack matrices, VertexConsumer vertexConsumer, float chargeProgress, int light, int overlay, float red, float green, float blue, float alpha) {
        matrices.push();

        this.root.rotate(matrices);
        this.bone.rotate(matrices);
        matrices.scale( chargeProgress,  chargeProgress,  chargeProgress);
        this.bubbleGum.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
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

    protected void updateAnimation(AnimationState animationState, Animation animation, float animationProgress, float speedMultiplier) {
        animationState.update(animationProgress, speedMultiplier);
        animationState.run(state -> AnimationHelper.animate(this, animation, state.getTimeRunning(), 1.0f, TEMP));
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

        ModelPartData bubble_gum = bone.addChild("bubble_gum", ModelPartBuilder.create().uv(91, 0).cuboid(-0.5F, -0.5F, -3.0F, 2.0F, 2.0F, 2.0F, new Dilation(1.0F)), ModelTransform.pivot(0.0F, -12.0F, -5.0F));

        ModelPartData ground = bone.addChild("ground", ModelPartBuilder.create().uv(86, 33).cuboid(-12.0F, -5.002F, 4.0F, 8.0F, 1.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(8.0F, 4.0F, -8.0F));

        ModelPartData launcher = bone.addChild("launcher", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -0.1667F, 0.0F));

        ModelPartData dispenser = launcher.addChild("dispenser", ModelPartBuilder.create().uv(0, 21).cuboid(-16.0F, -15.0F, 0.0F, 16.0F, 3.0F, 16.0F, new Dilation(0.0F))
                .uv(0, 41).cuboid(-15.0F, -19.0F, 1.0F, 14.0F, 4.0F, 14.0F, new Dilation(0.0F))
                .uv(5, 0).cuboid(-12.0F, -19.0F, 0.0F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-4.0F, -19.0F, 0.0F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 74).cuboid(-15.0F, -25.0F, 1.0F, 4.0F, 6.0F, 14.0F, new Dilation(0.0F))
                .uv(61, 69).cuboid(-5.0F, -25.0F, 1.0F, 4.0F, 6.0F, 14.0F, new Dilation(0.0F))
                .uv(43, 46).cuboid(-15.0F, -27.0F, 1.0F, 14.0F, 2.0F, 14.0F, new Dilation(0.0F))
                .uv(53, 9).cuboid(-14.0F, -38.0F, 2.0F, 12.0F, 11.0F, 12.0F, new Dilation(0.0F))
                .uv(84, 63).cuboid(-12.0F, -39.0F, 4.0F, 8.0F, 1.0F, 8.0F, new Dilation(0.0F))
                .uv(55, 33).cuboid(-13.0F, -41.0F, 3.0F, 10.0F, 2.0F, 10.0F, new Dilation(0.0F))
                .uv(49, 0).cuboid(-11.0F, -42.0F, 5.0F, 6.0F, 1.0F, 6.0F, new Dilation(0.0F))
                .uv(36, 63).cuboid(-11.0F, -25.0F, 2.0F, 6.0F, 6.0F, 13.0F, new Dilation(0.0F)), ModelTransform.pivot(8.0F, 4.1667F, -8.0F));

        ModelPartData handle = dispenser.addChild("handle", ModelPartBuilder.create().uv(69, 93).cuboid(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-8.0F, -23.0F, 1.5F));

        ModelPartData bubble_gums = dispenser.addChild("bubble_gums", ModelPartBuilder.create(), ModelTransform.pivot(-8.0746F, -29.8134F, 8.2302F));

        ModelPartData bubble_gum_r1 = bubble_gums.addChild("bubble_gum_r1", ModelPartBuilder.create().uv(82, 0).cuboid(-2.5F, -1.0F, 0.5F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0746F, 1.8134F, 1.7698F, 0.0F, 0.3054F, 0.0F));

        ModelPartData bubble_gum_r2 = bubble_gums.addChild("bubble_gum_r2", ModelPartBuilder.create().uv(91, 0).cuboid(-1.0F, -1.5F, 0.5F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-1.9254F, 1.8134F, -4.2302F, -0.4363F, 0.0F, 0.0F));

        ModelPartData bubble_gum_r3 = bubble_gums.addChild("bubble_gum_r3", ModelPartBuilder.create().uv(82, 5).cuboid(-1.0F, -1.0F, 1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(1.0746F, 1.8134F, -4.2302F, 0.0F, 0.0F, -0.3054F));

        ModelPartData bubble_gum_r4 = bubble_gums.addChild("bubble_gum_r4", ModelPartBuilder.create().uv(82, 5).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(4.0746F, 1.8134F, -1.2302F, 0.0F, 0.3054F, 0.0F));

        ModelPartData bubble_gum_r5 = bubble_gums.addChild("bubble_gum_r5", ModelPartBuilder.create().uv(91, 5).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-2.9254F, 1.8689F, 2.0311F, 0.0F, -0.6545F, 0.0F));

        ModelPartData bubble_gum_r6 = bubble_gums.addChild("bubble_gum_r6", ModelPartBuilder.create().uv(82, 5).cuboid(-0.5F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-4.9254F, 1.8689F, -2.9689F, -0.3491F, 0.0F, 0.0F));

        ModelPartData bubble_gum_r7 = bubble_gums.addChild("bubble_gum_r7", ModelPartBuilder.create().uv(100, 0).cuboid(-1.0F, -3.5F, 4.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-1.9254F, 1.8134F, -4.2302F, -0.0872F, 0.0038F, 0.0435F));

        ModelPartData bubble_gum_r8 = bubble_gums.addChild("bubble_gum_r8", ModelPartBuilder.create().uv(91, 0).cuboid(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-2.9254F, 1.8689F, 5.0311F, 0.3491F, 0.0F, 0.0F));

        ModelPartData bubble_gum_r9 = bubble_gums.addChild("bubble_gum_r9", ModelPartBuilder.create().uv(82, 0).cuboid(-5.063F, -1.9183F, -5.25F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.2354F, 0.0122F, 0.0198F, 0.0F, 0.0F, -0.1745F));

        ModelPartData bubble_gum_r10 = bubble_gums.addChild("bubble_gum_r10", ModelPartBuilder.create().uv(100, 0).cuboid(6.0F, -1.0F, -5.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-6.3146F, 1.8134F, 3.6483F, 0.0F, -0.48F, 0.0F));

        ModelPartData bubble_gum_r11 = bubble_gums.addChild("bubble_gum_r11", ModelPartBuilder.create().uv(82, 0).cuboid(-1.0F, -1.0F, 3.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-4.9254F, 1.8134F, -4.2302F, 0.0F, 0.3927F, 0.0F));

        ModelPartData bubble_gum_r12 = bubble_gums.addChild("bubble_gum_r12", ModelPartBuilder.create().uv(82, 5).cuboid(-3.1608F, -0.8137F, -2.2519F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.2354F, 0.0122F, 0.0198F, -0.3054F, 0.0F, 0.0F));

        ModelPartData bubble_gum_r13 = bubble_gums.addChild("bubble_gum_r13", ModelPartBuilder.create().uv(91, 5).cuboid(-2.6618F, -1.1988F, 1.7962F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.2354F, 0.0122F, 0.0198F, 0.0F, 0.3927F, 0.0F));

        ModelPartData bubble_gum_r14 = bubble_gums.addChild("bubble_gum_r14", ModelPartBuilder.create().uv(82, 5).cuboid(-3.355F, 0.0783F, 0.75F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.2354F, 0.0122F, 0.0198F, 0.0F, 0.0F, 0.4363F));

        ModelPartData bubble_gum_r15 = bubble_gums.addChild("bubble_gum_r15", ModelPartBuilder.create().uv(82, 5).cuboid(-0.8291F, -1.1988F, -5.4024F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(91, 0).cuboid(2.3293F, -1.1988F, 0.8146F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.2354F, 0.0122F, 0.0198F, 0.0F, 0.3054F, 0.0F));

        ModelPartData bubble_gum_r16 = bubble_gums.addChild("bubble_gum_r16", ModelPartBuilder.create().uv(82, 0).cuboid(0.0042F, 0.0311F, -5.25F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.2354F, 0.0122F, 0.0198F, 0.0F, 0.0F, -0.9163F));

        ModelPartData bubble_gum_r17 = bubble_gums.addChild("bubble_gum_r17", ModelPartBuilder.create().uv(100, 5).cuboid(1.1324F, -1.1988F, -4.2711F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.2354F, 0.0122F, 0.0198F, 0.0F, -0.48F, 0.0F));

        ModelPartData bubble_gum_r18 = bubble_gums.addChild("bubble_gum_r18", ModelPartBuilder.create().uv(91, 0).cuboid(2.3405F, -1.1432F, -0.097F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.2354F, 0.0122F, 0.0198F, 0.0F, -0.6545F, 0.0F));

        ModelPartData bubble_gum_r19 = bubble_gums.addChild("bubble_gum_r19", ModelPartBuilder.create().uv(100, 5).cuboid(2.8419F, 0.6099F, 1.75F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.2354F, 0.0122F, 0.0198F, 0.0F, 0.0F, -0.3491F));

        ModelPartData bubble_gum_r20 = bubble_gums.addChild("bubble_gum_r20", ModelPartBuilder.create().uv(91, 5).cuboid(-3.7737F, -0.1429F, 3.25F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.2354F, 0.0122F, 0.0198F, 0.0F, 0.0F, 0.3491F));

        ModelPartData bubble_gum_r21 = bubble_gums.addChild("bubble_gum_r21", ModelPartBuilder.create().uv(91, 5).cuboid(-1.9254F, -1.217F, -3.0632F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(100, 5).cuboid(-0.9254F, -1.217F, -0.5632F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.1754F, -1.514F, -0.0694F, 0.0435F, 0.0038F, 0.0872F));

        ModelPartData bubble_gum_r22 = bubble_gums.addChild("bubble_gum_r22", ModelPartBuilder.create().uv(100, 5).cuboid(-5.25F, -0.1183F, -3.7647F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.1754F, -2.014F, -0.0694F, 0.3491F, 0.0F, 0.0F));

        ModelPartData bubble_gum_r23 = bubble_gums.addChild("bubble_gum_r23", ModelPartBuilder.create().uv(100, 5).cuboid(-4.25F, 0.6345F, 2.8329F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.1754F, -2.014F, -0.0694F, -0.3491F, 0.0F, 0.0F));

        ModelPartData bubble_gum_r24 = bubble_gums.addChild("bubble_gum_r24", ModelPartBuilder.create().uv(91, 0).cuboid(-1.903F, -1.1171F, 2.3405F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.1754F, -1.514F, -0.0694F, 0.0F, -0.6545F, 0.0F));

        ModelPartData bubble_gum_r25 = bubble_gums.addChild("bubble_gum_r25", ModelPartBuilder.create().uv(82, 0).cuboid(2.2711F, -1.1726F, 1.1324F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.1754F, -1.514F, -0.0694F, 0.0F, -0.48F, 0.0F));

        ModelPartData bubble_gum_r26 = bubble_gums.addChild("bubble_gum_r26", ModelPartBuilder.create().uv(82, 0).cuboid(3.25F, 0.047F, -0.0166F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.1754F, -2.014F, -0.0694F, -0.9163F, 0.0F, 0.0F));

        ModelPartData bubble_gum_r27 = bubble_gums.addChild("bubble_gum_r27", ModelPartBuilder.create().uv(91, 5).cuboid(-2.8146F, -1.1726F, 2.3293F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(91, 5).cuboid(3.4024F, -1.6726F, -0.8291F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.1754F, -1.514F, -0.0694F, 0.0F, 0.3054F, 0.0F));

        ModelPartData bubble_gum_r28 = bubble_gums.addChild("bubble_gum_r28", ModelPartBuilder.create().uv(100, 0).cuboid(-3.7962F, -1.1726F, -2.6618F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.1754F, -1.514F, -0.0694F, 0.0F, 0.3927F, 0.0F));

        ModelPartData bubble_gum_r29 = bubble_gums.addChild("bubble_gum_r29", ModelPartBuilder.create().uv(91, 0).cuboid(-3.25F, 0.102F, -3.3439F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.1754F, -2.014F, -0.0694F, 0.4363F, 0.0F, 0.0F));

        ModelPartData bubble_gum_r30 = bubble_gums.addChild("bubble_gum_r30", ModelPartBuilder.create().uv(100, 0).cuboid(0.244F, -0.7887F, -3.1608F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.1754F, -2.014F, -0.0694F, 0.0F, 0.0F, 0.3054F));

        ModelPartData bubble_gum_r31 = bubble_gums.addChild("bubble_gum_r31", ModelPartBuilder.create().uv(82, 5).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(3.0746F, -2.1866F, 0.7698F, 0.3054F, 0.0F, 0.0F));

        ModelPartData bubble_gum_r32 = bubble_gums.addChild("bubble_gum_r32", ModelPartBuilder.create().uv(91, 0).cuboid(3.45F, 1.1075F, -5.0676F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.1754F, -1.514F, -0.0694F, -0.1745F, 0.0F, 0.0F));

        ModelPartData bubble_gum_r33 = bubble_gums.addChild("bubble_gum_r33", ModelPartBuilder.create().uv(91, 5).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(4.0746F, 1.8134F, -4.2302F, 0.1745F, 0.0F, 0.0F));

        ModelPartData bubble_gum_r34 = bubble_gums.addChild("bubble_gum_r34", ModelPartBuilder.create().uv(91, 0).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(4.0746F, 1.8431F, 1.3599F, 0.9163F, 0.0F, 0.0F));

        ModelPartData legs = launcher.addChild("legs", ModelPartBuilder.create().uv(37, 84).cuboid(-11.0F, -11.0F, 5.0F, 6.0F, 6.0F, 6.0F, new Dilation(0.0F))
                .uv(0, 60).cuboid(-14.0F, -12.0F, 2.0F, 12.0F, 1.0F, 12.0F, new Dilation(0.0F)), ModelTransform.pivot(8.0F, 4.1667F, -8.0F));

        ModelPartData bottom = modelPartData.addChild("bottom", ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -0.751F, -8.0F, 16.0F, 4.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 20.75F, 0.0F));
        return TexturedModelData.of(modelData, 128, 128);
    }

    @Environment(value = EnvType.CLIENT)
    public static class AnimationHelper {
        public static void animate(BubbleGumLauncherBlockEntityModel model, Animation animation, long runningTime, float scale, Vector3f tempVec) {
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

    public static final Animation SHOOT = Animation.Builder.create(1.625f)
            .addBoneAnimation("bone",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("launcher",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.25f, AnimationHelper.createTranslationalVector(0f, -0.15f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.41667f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.5f, AnimationHelper.createTranslationalVector(0f, -0.07f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.66667f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.75f, AnimationHelper.createTranslationalVector(0f, -0.07f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.91667f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1f, AnimationHelper.createTranslationalVector(0f, -0.04f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.16667f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC)))
            .addBoneAnimation("launcher",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.25f, AnimationHelper.createRotationalVector(-2.5f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.5f, AnimationHelper.createRotationalVector(1.26f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.75f, AnimationHelper.createRotationalVector(-1.25f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1f, AnimationHelper.createRotationalVector(0.6f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.16667f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.33333f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC))).build();

}