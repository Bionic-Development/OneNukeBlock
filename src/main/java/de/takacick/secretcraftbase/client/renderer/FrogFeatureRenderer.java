package de.takacick.secretcraftbase.client.renderer;

import de.takacick.secretcraftbase.access.PlayerProperties;
import de.takacick.secretcraftbase.registry.ItemRegistry;
import de.takacick.secretcraftbase.server.utils.EntityNbtHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.FrogEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FrogFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    private static final Vector3f TEMP = new Vector3f();
    public static final ModelPart FROG = FrogEntityModel.getTexturedModelData().createModel();
    private final ModelPart frog;

    public FrogFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext) {
        super(featureRendererContext);
        this.frog = getTexturedModelData().createModel();
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, AbstractClientPlayerEntity playerEntity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        ItemStack itemStack = playerEntity.getMainHandStack();
        if (!(itemStack.isOf(ItemRegistry.FROG) && playerEntity instanceof PlayerProperties playerProperties)) {
            return;
        }

        matrixStack.push();
        this.frog.traverse().forEach(ModelPart::resetTransform);

        if (playerEntity.isInSneakingPose()) {
            this.frog.pivotY += 3.2f;
        }

        float pitch = playerEntity.getPitch(tickDelta);

        this.frog.pitch = pitch * ((float) Math.PI / 180);
        Entity entity = EntityNbtHelper.getEntityType(EntityType.FROG, itemStack).create(MinecraftClient.getInstance().world);
        if (entity instanceof FrogEntity frogEntity) {
            entity.readNbt(EntityNbtHelper.getEntityNbt(itemStack));

            updateAnimation(this.frog, playerProperties.getFrogTongueState(), getUsingTongue(playerProperties.getFrogTongueLength()), animationProgress, 1f);

            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(frogEntity.getVariant().texture()));
            this.frog.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
        }


        matrixStack.pop();
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData root = modelPartData.addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 7.0F, 0.0F));

        ModelPartData body = root.addChild("body", ModelPartBuilder.create().uv(3, 1).cuboid(-3.5F, -2.0F, -8.0F, 7.0F, 3.0F, 9.0F, new Dilation(0.0F))
                .uv(23, 22).cuboid(-3.5F, -1.0F, -8.0F, 7.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -2.0F, -7.0F));

        ModelPartData head = body.addChild("head", ModelPartBuilder.create().uv(23, 13).cuboid(-3.5F, -1.0F, -7.0F, 7.0F, 0.0F, 9.0F, new Dilation(0.0F))
                .uv(0, 13).cuboid(-3.5F, -2.0F, -7.0F, 7.0F, 3.0F, 9.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -2.0F, -1.0F));

        ModelPartData eyes = head.addChild("eyes", ModelPartBuilder.create(), ModelTransform.pivot(-0.5F, 0.0F, 2.0F));

        ModelPartData right_eye = eyes.addChild("right_eye", ModelPartBuilder.create().uv(0, 0).cuboid(-1.5F, -1.0F, -1.5F, 3.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.5F, -3.0F, -6.5F));

        ModelPartData left_eye = eyes.addChild("left_eye", ModelPartBuilder.create().uv(0, 5).cuboid(-1.5F, -1.0F, -1.5F, 3.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(2.5F, -3.0F, -6.5F));

        ModelPartData croaking_body = body.addChild("croaking_body", ModelPartBuilder.create().uv(26, 5).cuboid(-3.5F, -0.1F, -2.9F, 7.0F, 2.0F, 3.0F, new Dilation(-0.1F)), ModelTransform.pivot(0.0F, -1.0F, -5.0F));

        ModelPartData tongue = body.addChild("tongue", ModelPartBuilder.create().uv(17, 13).cuboid(-2.0F, 0.0F, -7.1F, 4.0F, 0.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -1.1F, 1.0F));

        ModelPartData left_arm = body.addChild("left_arm", ModelPartBuilder.create().uv(0, 32).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 3.0F, new Dilation(0.0F))
                .uv(18, 40).cuboid(-4.0F, 3.01F, -5.0F, 8.0F, 0.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(4.0F, -1.0F, -6.5F));

        ModelPartData right_arm = body.addChild("right_arm", ModelPartBuilder.create().uv(0, 38).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 3.0F, new Dilation(0.0F))
                .uv(2, 40).cuboid(-4.0F, 3.01F, -5.0F, 8.0F, 0.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(-4.0F, -1.0F, -6.5F));

        ModelPartData left_leg = root.addChild("left_leg", ModelPartBuilder.create().uv(14, 25).cuboid(-1.0F, 0.0F, -2.0F, 3.0F, 3.0F, 4.0F, new Dilation(0.0F))
                .uv(2, 32).cuboid(-2.0F, 3.01F, -4.0F, 8.0F, 0.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(3.5F, -3.0F, -7.0F));

        ModelPartData right_leg = root.addChild("right_leg", ModelPartBuilder.create().uv(0, 25).cuboid(-2.0F, 0.0F, -2.0F, 3.0F, 3.0F, 4.0F, new Dilation(0.0F))
                .uv(18, 32).cuboid(-6.0F, 3.01F, -4.0F, 8.0F, 0.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.5F, -3.0F, -7.0F));
        return TexturedModelData.of(modelData, 48, 48);
    }

    public static void updateAnimation(ModelPart modelPart, AnimationState animationState, Animation animation, float animationProgress, float speedMultiplier) {
        animationState.update(animationProgress, speedMultiplier);
        animationState.run(state -> AnimationHelper.animate(modelPart, animation, state.getTimeRunning(), 1.0f, TEMP));
    }

    public static Optional<ModelPart> getChild(ModelPart modelPart, String name) {
        if (name.equals("root")) {
            return Optional.of(modelPart);
        }
        return modelPart.traverse().filter(part -> part.hasChild(name)).findFirst().map(part -> part.getChild(name));
    }

    @Environment(value = EnvType.CLIENT)
    public static class AnimationHelper {
        public static void animate(ModelPart model, Animation animation, long runningTime, float scale, Vector3f tempVec) {
            float f = getRunningSeconds(animation, runningTime);
            for (Map.Entry<String, List<Transformation>> entry : animation.boneAnimations().entrySet()) {
                Optional<ModelPart> optional = getChild(model, entry.getKey());
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

    public static Animation getUsingTongue(float tongueLength) {
        return Animation.Builder.create(0.5f).addBoneAnimation("head",
                        new Transformation(Transformation.Targets.ROTATE,
                                new Keyframe(0.0f, AnimationHelper
                                        .createRotationalVector(0.0f, 0.0f, 0.0f), Transformation.Interpolations.LINEAR),
                                new Keyframe(0.0833f, AnimationHelper
                                        .createRotationalVector(-60.0f, 0.0f, 0.0f), Transformation.Interpolations.LINEAR),
                                new Keyframe(0.4167f, AnimationHelper
                                        .createRotationalVector(-60.0f, 0.0f, 0.0f), Transformation.Interpolations.LINEAR),
                                new Keyframe(0.5f, AnimationHelper
                                        .createRotationalVector(0.0f, 0.0f, 0.0f), Transformation.Interpolations.LINEAR)))
                .addBoneAnimation("head", new Transformation(Transformation.Targets.SCALE,
                        new Keyframe(0.0f, AnimationHelper
                                .createRotationalVector(1.0f, 1.0f, 1.0f), Transformation.Interpolations.LINEAR),
                        new Keyframe(0.0833f, AnimationHelper
                                .createRotationalVector(0.998f, 1.0f, 1.0f), Transformation.Interpolations.LINEAR),
                        new Keyframe(0.4167f, AnimationHelper
                                .createRotationalVector(0.998f, 1.0f, 1.0f), Transformation.Interpolations.LINEAR),
                        new Keyframe(0.5f, AnimationHelper
                                .createRotationalVector(1.0f, 1.0f, 1.0f), Transformation.Interpolations.LINEAR)))
                .addBoneAnimation("tongue", new Transformation(Transformation.Targets.ROTATE,
                        new Keyframe(0.0f, AnimationHelper
                                .createRotationalVector(0.0f, 0.0f, 0.0f), Transformation.Interpolations.LINEAR),
                        new Keyframe(0.0833f, AnimationHelper.
                                createRotationalVector(0.0f, 0.0f, 0.0f), Transformation.Interpolations.LINEAR),
                        new Keyframe(0.4167f, AnimationHelper
                                .createRotationalVector(-18.0f, 0.0f, 0.0f), Transformation.Interpolations.LINEAR),
                        new Keyframe(0.5f, AnimationHelper.
                                createRotationalVector(0.0f, 0.0f, 0.0f), Transformation.Interpolations.LINEAR)))
                .addBoneAnimation("tongue", new Transformation(Transformation.Targets.SCALE,
                        new Keyframe(0.0833f, AnimationHelper
                                .createScalingVector(1.0, 1.0, 1.0), Transformation.Interpolations.LINEAR),
                        new Keyframe(0.1667f, AnimationHelper
                                .createScalingVector(0.5, 1.0, tongueLength), Transformation.Interpolations.LINEAR),
                        new Keyframe(0.4167f, AnimationHelper
                                .createScalingVector(1.0, 1.0, 1.0), Transformation.Interpolations.LINEAR))).build();
    }
}

