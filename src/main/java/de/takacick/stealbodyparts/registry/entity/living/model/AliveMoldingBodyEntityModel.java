package de.takacick.stealbodyparts.registry.entity.living.model;

import de.takacick.stealbodyparts.registry.entity.living.AliveMoldedBossEntity;
import de.takacick.stealbodyparts.registry.entity.living.AliveMoldingBodyEntity;
import de.takacick.stealbodyparts.registry.entity.living.MoldingPart;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.animation.WardenAnimations;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

import java.util.HashMap;

@Environment(EnvType.CLIENT)
public class AliveMoldingBodyEntityModel<T extends AliveMoldingBodyEntity> extends SinglePartEntityModel<T> {

    public final ModelPart root;
    public final ModelPart bone;
    public final ModelPart body;
    public final ModelPart chest;
    public final ModelPart head;
    public final ModelPart head_1;
    public final ModelPart head_2;
    public final ModelPart leftLeg;
    public final ModelPart leftArm;
    public final ModelPart leftArmSlim;
    public final ModelPart leftArm1;
    public final ModelPart leftArm1Slim;
    public final ModelPart rightArm;
    public final ModelPart rightArmSlim;
    public final ModelPart rightLeg;

    public static final HashMap<MoldingPart, ModelPart> bodyParts = new HashMap<>();
    public static final HashMap<MoldingPart, ModelPart> slimBodyParts = new HashMap<>();

    public AliveMoldingBodyEntityModel(ModelPart root) {
        super(RenderLayer::getEntityCutoutNoCull);
        this.root = root;
        this.bone = root.getChild(EntityModelPartNames.BONE);
        this.body = this.bone.getChild(EntityModelPartNames.BODY);
        this.chest = this.body.getChild("chest");
        this.head = this.body.getChild("head");
        this.head_1 = this.body.getChild("head_1");
        this.head_2 = this.body.getChild("head_2");
        this.rightLeg = this.bone.getChild(EntityModelPartNames.RIGHT_LEG);
        this.leftLeg = this.bone.getChild(EntityModelPartNames.LEFT_LEG);
        this.rightArm = this.body.getChild(EntityModelPartNames.RIGHT_ARM);
        this.rightArmSlim = this.body.getChild("right_arm_slim");
        this.leftArm = this.body.getChild(EntityModelPartNames.LEFT_ARM);
        this.leftArmSlim = this.body.getChild("left_arm_slim");
        this.leftArm1 = this.body.getChild("left_arm_1");
        this.leftArm1Slim = this.body.getChild("left_arm_1_slim");

        bodyParts.put(MoldingPart.HEAD, this.head);
        bodyParts.put(MoldingPart.HEAD_1, this.head_1);
        bodyParts.put(MoldingPart.HEAD_2, this.head_2);
        bodyParts.put(MoldingPart.RIGHT_LEG, this.rightLeg);
        bodyParts.put(MoldingPart.RIGHT_ARM, this.rightArm);
        bodyParts.put(MoldingPart.LEFT_ARM, this.leftArm);
        bodyParts.put(MoldingPart.LEFT_ARM_1, this.leftArm1);
        bodyParts.put(MoldingPart.LEFT_LEG, this.leftLeg);

        slimBodyParts.put(MoldingPart.HEAD, this.head);
        slimBodyParts.put(MoldingPart.HEAD_1, this.head_1);
        slimBodyParts.put(MoldingPart.HEAD_2, this.head_2);
        slimBodyParts.put(MoldingPart.RIGHT_LEG, this.rightLeg);
        slimBodyParts.put(MoldingPart.RIGHT_ARM, this.rightArmSlim);
        slimBodyParts.put(MoldingPart.LEFT_ARM, this.leftArmSlim);
        slimBodyParts.put(MoldingPart.LEFT_ARM_1, this.leftArm1Slim);
        slimBodyParts.put(MoldingPart.LEFT_LEG, this.leftLeg);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData root = modelPartData.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData body = root.addChild("body", ModelPartBuilder.create(),
                ModelTransform.pivot(0.0F, -21.0F, 0.0F));

        ModelPartData chest = body.addChild("chest", ModelPartBuilder.create().uv(40, 0)
                        .cuboid(-4.0F, -3.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.0F)),
                ModelTransform.pivot(0.0F, -0, 0.0F));

        ModelPartData head_1 = body.addChild("head",
                ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
                        .uv(32, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.5F)), ModelTransform.of(3.5445F, -2.3662F, 0.8F, 0.0F, 0.0F, 0.1745F));

        ModelPartData head_2 = body.addChild("head_1",
                ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
                        .uv(32, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.5F)), ModelTransform.of(-2.6923F, -2.5332F, 0.0F, 0.0F, 0.0F, -0.0873F));

        ModelPartData head_3 = body.addChild("head_2", ModelPartBuilder.create(),
                ModelTransform.of(-2.6923F, -2.5332F, 0.0F, 0.0F, 0.0F, -0.0873F));

        ModelPartData head_r1 = head_3.addChild("head_r1",
                ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-2.5F, -7.6F, -5.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
                        .uv(32, 0).cuboid(-2.5F, -7.6F, -5.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.5F)), ModelTransform.of(4.6923F, 9.5332F, 2.0F, 0.3054F, 3.1416F, -0.1745F));

        ModelPartData right_arm = body.addChild("right_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
                .uv(40, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(-6.0F, -3.0F, 0.0F));

        ModelPartData left_arm_1 = body.addChild("left_arm", ModelPartBuilder.create().uv(32, 48).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
                .uv(48, 48).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(6.0F, -3.0F, 0.0F));

        ModelPartData left_arm_2 = body.addChild("left_arm_1", ModelPartBuilder.create().uv(48, 48).cuboid(24.0F, -32.0F, 3.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F))
                .uv(32, 48).cuboid(24.0F, -32.0F, 3.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-30.0F, 35.8F, -3.0F));

        ModelPartData right_arm_slim = body.addChild("right_arm_slim", ModelPartBuilder.create().uv(40, 32).cuboid(-1.5F, 0.0F, -2.0F, 3.0F, 12.0F, 4.0F, new Dilation(0.25F))
                .uv(40, 16).cuboid(-1.5F, 0.0F, -2.0F, 3.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-5.5F, -3.0F, 0.0F));

        ModelPartData left_arm_1_slim = body.addChild("left_arm_slim", ModelPartBuilder.create().uv(32, 48).cuboid(-1.5F, 0.0F, -2.0F, 3.0F, 12.0F, 4.0F, new Dilation(0.0F))
                .uv(48, 48).cuboid(-1.5F, 0.0F, -2.0F, 3.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(5.5F, -3.0F, 0.0F));

        ModelPartData left_arm_2_slim = body.addChild("left_arm_1_slim", ModelPartBuilder.create().uv(32, 48).cuboid(25.0F, -32.0F, 3.0F, 3.0F, 12.0F, 4.0F, new Dilation(0.0F))
                .uv(48, 48).cuboid(25.0F, -32.0F, 3.0F, 3.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(-30.0F, 35.8F, -3.0F));

        ModelPartData right_leg = root.addChild("right_leg", ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 32).cuboid(-2.1F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(-1.9F, -12.0F, 0.0F));

        ModelPartData left_leg = root.addChild("left_leg", ModelPartBuilder.create().uv(0, 48).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F))
                .uv(16, 48).cuboid(-1.9F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(1.9F, -12.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(T wardenEntity, float f, float g, float h, float i, float j) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        if(wardenEntity instanceof AliveMoldedBossEntity) {
            this.setHeadAngle(i, j);
            this.setLimbAngles(f, g);
            this.setHeadAndBodyAngles(h);
            this.updateAnimation(wardenEntity.attackingAnimationState, WardenAnimations.ATTACKING, h);
            this.updateAnimation(wardenEntity.chargingSonicBoomAnimationState, WardenAnimations.CHARGING_SONIC_BOOM, h);
            this.updateAnimation(wardenEntity.diggingAnimationState, WardenAnimations.DIGGING, h);
            this.updateAnimation(wardenEntity.emergingAnimationState, WardenAnimations.EMERGING, h);
            this.updateAnimation(wardenEntity.roaringAnimationState, WardenAnimations.ROARING, h);
            this.updateAnimation(wardenEntity.sniffingAnimationState, WardenAnimations.SNIFFING, h);
            this.leftArmSlim.setAngles(this.leftArm.pitch, this.leftArm.yaw, this.leftArm.roll);
            this.rightArmSlim.setAngles(this.rightArm.pitch, this.rightArm.yaw, this.rightArm.roll);

            this.head_1.setAngles(this.head.pitch, this.head.yaw, this.head_1.roll);
        }
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        this.getPart().rotate(matrices);

        matrices.push();
        matrices.translate(0f, 24.0f / 16.0f, 0f);
        this.body.rotate(matrices);

        this.chest.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        matrices.pop();
        matrices.translate(0f, 3.0f / 16.0f, 0f);
    }

    private void setHeadAngle(float yaw, float pitch) {
        this.head.pitch = pitch * ((float) Math.PI / 180);
        this.head.yaw = yaw * ((float) Math.PI / 180);
    }

    private void setHeadAndBodyAngles(float animationProgress) {
        float f = animationProgress * 0.1f;
        float g = MathHelper.cos(f);
        float h = MathHelper.sin(f);
        this.head.roll += 0.06f * g;
        this.head.pitch += 0.06f * h;
        this.body.roll += 0.025f * h;
        this.body.pitch += 0.025f * g;
    }

    private void setLimbAngles(float angle, float distance) {
        float f = Math.min(0.5f, 3.0f * distance);
        float g = angle * 0.8662f;
        float h = MathHelper.cos(g);
        float i = MathHelper.sin(g);
        float j = Math.min(0.35f, f);
        this.head.roll += 0.3f * i * f;
        this.head.pitch += 1.2f * MathHelper.cos(g + 1.5707964f) * j;
        this.body.roll = 0.1f * i * f;
        this.body.pitch = 1.0f * h * j;
        this.leftLeg.pitch = 1.0f * h * f;
        this.rightLeg.pitch = 1.0f * MathHelper.cos(g + (float) Math.PI) * f;
        this.leftArm.pitch = -(0.8f * h * f);
        this.leftArm.roll = 0.0f;
        this.rightArm.pitch = -(0.8f * i * f);
        this.rightArm.roll = 0.0f;
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }
}
