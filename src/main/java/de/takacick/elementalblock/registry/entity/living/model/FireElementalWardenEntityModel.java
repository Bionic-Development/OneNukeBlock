package de.takacick.elementalblock.registry.entity.living.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.animation.WardenAnimations;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class FireElementalWardenEntityModel<T extends WardenEntity>
        extends SinglePartEntityModel<T> {

    private final ModelPart root;
    protected final ModelPart bone;
    protected final ModelPart body;
    protected final ModelPart head;
    protected final ModelPart rightTendril;
    protected final ModelPart leftTendril;
    protected final ModelPart leftLeg;
    protected final ModelPart leftArm;
    protected final ModelPart rightArm;
    protected final ModelPart rightLeg;
    private final List<ModelPart> tendrils;
    private final List<ModelPart> justBody;
    private final List<ModelPart> headAndLimbs;
    private final List<ModelPart> bodyHeadAndLimbs;

    public FireElementalWardenEntityModel(ModelPart root) {
        super(RenderLayer::getEntityCutoutNoCull);
        this.root = root;
        this.bone = root.getChild(EntityModelPartNames.BONE);
        this.body = this.bone.getChild(EntityModelPartNames.BODY);
        this.head = this.body.getChild(EntityModelPartNames.HEAD);
        this.rightLeg = this.bone.getChild(EntityModelPartNames.RIGHT_LEG);
        this.leftLeg = this.bone.getChild(EntityModelPartNames.LEFT_LEG);
        this.rightArm = this.body.getChild(EntityModelPartNames.RIGHT_ARM);
        this.leftArm = this.body.getChild(EntityModelPartNames.LEFT_ARM);
        this.rightTendril = this.head.getChild(EntityModelPartNames.RIGHT_TENDRIL);
        this.leftTendril = this.head.getChild(EntityModelPartNames.LEFT_TENDRIL);
        this.tendrils = ImmutableList.of(this.leftTendril, this.rightTendril);
        this.justBody = ImmutableList.of(this.body);
        this.headAndLimbs = ImmutableList.of(this.head, this.leftArm, this.rightArm, this.leftLeg, this.rightLeg);
        this.bodyHeadAndLimbs = ImmutableList.of(this.body, this.head, this.leftArm, this.rightArm, this.leftLeg, this.rightLeg);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData root = modelPartData.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData body = root.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-9.0F, -13.0F, -4.0F, 18.0F, 21.0F, 11.0F, new Dilation(0.0F))
                .uv(24, 86).cuboid(-2.5F, -4.5F, -6.0F, 5.0F, 6.0F, 2.0F, new Dilation(0.0F))
                .uv(15, 86).cuboid(-1.5F, -1.0F, -7.0F, 3.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -21.0F, 0.0F));

        ModelPartData body_r1 = body.addChild("body_r1", ModelPartBuilder.create().uv(0, 86).mirrored().cuboid(-1.1F, 0.0F, 1.0F, 4.0F, 6.0F, 3.0F, new Dilation(0.0F)).mirrored(false)
                .uv(39, 86).mirrored().cuboid(-4.0F, 0.0F, 1.0F, 2.0F, 4.0F, 3.0F, new Dilation(0.0F)).mirrored(false)
                .uv(39, 86).cuboid(-10.0F, 0.0F, 1.0F, 2.0F, 4.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 86).cuboid(-14.9F, 0.0F, 1.0F, 4.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(6.0F, -11.0F, -5.0F, -0.829F, 0.0F, 0.0F));

        ModelPartData head = body.addChild("head", ModelPartBuilder.create().uv(59, 0).cuboid(-8.0F, -12.0F, -5.0F, 16.0F, 12.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -13.0F, 0.0F));

        ModelPartData right_tendril = head.addChild("right_tendril", ModelPartBuilder.create().uv(25, 66).cuboid(-9.0F, -1.0F, -3.0F, 9.0F, 5.0F, 6.0F, new Dilation(0.0F))
                .uv(56, 66).cuboid(-9.0F, -6.0F, -3.0F, 5.0F, 5.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(-8.0F, -8.0F, 0.0F));

        ModelPartData left_tendril = head.addChild("left_tendril", ModelPartBuilder.create().uv(25, 66).mirrored().cuboid(0.0F, -1.0F, -3.0F, 9.0F, 5.0F, 6.0F, new Dilation(0.0F)).mirrored(false)
                .uv(56, 66).mirrored().cuboid(4.0F, -6.0F, -3.0F, 5.0F, 5.0F, 6.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(8.0F, -8.0F, 0.0F));

        ModelPartData right_arm = body.addChild("right_arm", ModelPartBuilder.create().uv(0, 33).cuboid(-6.0F, 0.0F, -4.0F, 10.0F, 24.0F, 8.0F, new Dilation(0.0F))
                .uv(37, 33).cuboid(-7.0F, -1.0F, -5.5F, 12.0F, 7.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(-13.0F, -13.0F, 1.0F));

        ModelPartData left_arm = body.addChild("left_arm", ModelPartBuilder.create().uv(0, 33).mirrored().cuboid(-4.0F, 0.0F, -4.0F, 10.0F, 24.0F, 8.0F, new Dilation(0.0F)).mirrored(false)
                .uv(37, 33).mirrored().cuboid(-5.0F, -1.0F, -5.5F, 12.0F, 7.0F, 10.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(13.0F, -13.0F, 1.0F));

        ModelPartData right_leg = root.addChild("right_leg", ModelPartBuilder.create().uv(0, 66).cuboid(-3.1F, 0.0F, -3.0F, 6.0F, 13.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(-5.9F, -13.0F, 0.0F));

        ModelPartData left_leg = root.addChild("left_leg", ModelPartBuilder.create().uv(0, 66).mirrored().cuboid(-2.9F, 0.0F, -3.0F, 6.0F, 13.0F, 6.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(5.9F, -13.0F, 0.0F));
        return TexturedModelData.of(modelData, 112, 112);
    }

    @Override
    public void setAngles(T wardenEntity, float f, float g, float h, float i, float j) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        float k = h - (float) wardenEntity.age;
        this.setHeadAngle(i, j);
        this.setLimbAngles(f, g);
        this.setHeadAndBodyAngles(h);
        this.setTendrilPitches(wardenEntity, h, k);
        this.updateAnimation(wardenEntity.attackingAnimationState, WardenAnimations.ATTACKING, h);
        this.updateAnimation(wardenEntity.chargingSonicBoomAnimationState, WardenAnimations.CHARGING_SONIC_BOOM, h);
        this.updateAnimation(wardenEntity.diggingAnimationState, WardenAnimations.DIGGING, h);
        this.updateAnimation(wardenEntity.emergingAnimationState, WardenAnimations.EMERGING, h);
        this.updateAnimation(wardenEntity.roaringAnimationState, WardenAnimations.ROARING, h);
        this.updateAnimation(wardenEntity.sniffingAnimationState, WardenAnimations.SNIFFING, h);
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
        this.setArmPivots();
    }

    private void setArmPivots() {
        this.leftArm.yaw = 0.0f;
        this.leftArm.pivotZ = 1.0f;
        this.leftArm.pivotX = 13.0f;
        this.leftArm.pivotY = -13.0f;
        this.rightArm.yaw = 0.0f;
        this.rightArm.pivotZ = 1.0f;
        this.rightArm.pivotX = -13.0f;
        this.rightArm.pivotY = -13.0f;
    }

    private void setTendrilPitches(T warden, float animationProgress, float tickDelta) {
        float f;
        this.leftTendril.pitch = f = warden.getTendrilPitch(tickDelta) * (float) (Math.cos((double) animationProgress * 2.25) * Math.PI * (double) 0.1f);
        this.rightTendril.pitch = -f;
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    public List<ModelPart> getTendrils() {
        return this.tendrils;
    }

    public List<ModelPart> getBody() {
        return this.justBody;
    }

    public List<ModelPart> getHeadAndLimbs() {
        return this.headAndLimbs;
    }

    public List<ModelPart> getBodyHeadAndLimbs() {
        return this.bodyHeadAndLimbs;
    }
}
