package de.takacick.everythinghearts.registry.entity.living.model;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.animation.WardenAnimations;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.util.math.MathHelper;

import java.util.List;

@Environment(EnvType.CLIENT)
public class LoverWardenEntityModel<T extends WardenEntity>
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

    public LoverWardenEntityModel(ModelPart root) {
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
        ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData body = bone.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-9.0F, -13.0F, -4.0F, 18.0F, 21.0F, 11.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -21.0F, 0.0F));

        ModelPartData heart = body.addChild("heart", ModelPartBuilder.create().uv(8, 112).cuboid(0.0F, -25.0F, -6.0F, 3.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(29, 110).cuboid(0.0F, -24.0F, -6.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(29, 110).cuboid(-1.0F, -25.0F, -6.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(29, 110).cuboid(-2.0F, -26.0F, -6.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(29, 110).cuboid(1.0F, -23.0F, -6.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(29, 110).cuboid(2.0F, -24.0F, -6.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(29, 110).cuboid(3.0F, -25.0F, -6.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(29, 110).cuboid(4.0F, -26.0F, -6.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(41, 110).cuboid(-1.0F, -28.0F, -6.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(35, 110).cuboid(1.0F, -24.0F, -6.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(35, 110).cuboid(-1.0F, -27.0F, -6.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(8, 112).cuboid(0.0F, -28.0F, -6.0F, 5.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(8, 112).cuboid(-2.0F, -28.0F, -6.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(8, 112).cuboid(-1.0F, -29.0F, -6.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(8, 112).cuboid(2.0F, -29.0F, -6.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(8, 112).cuboid(-1.0F, -26.0F, -6.0F, 5.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 21.0F, 1.0F));

        ModelPartData right_ribcage = body.addChild("right_ribcage", ModelPartBuilder.create().uv(90, 11).cuboid(-2.0F, -11.0F, -0.1F, 9.0F, 21.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(-7.0F, -2.0F, -4.0F));

        ModelPartData left_ribcage = body.addChild("left_ribcage", ModelPartBuilder.create().uv(90, 11).mirrored().cuboid(-7.0F, -11.0F, -0.1F, 9.0F, 21.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(7.0F, -2.0F, -4.0F));

        ModelPartData head = body.addChild("head", ModelPartBuilder.create().uv(0, 32).cuboid(-8.0F, -16.0F, -5.0F, 16.0F, 16.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -13.0F, 0.0F));

        ModelPartData right_tendril = head.addChild("right_tendril", ModelPartBuilder.create().uv(52, 32).cuboid(-16.0F, -13.0F, 0.0F, 16.0F, 16.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(-8.0F, -12.0F, 0.0F));

        ModelPartData left_tendril = head.addChild("left_tendril", ModelPartBuilder.create().uv(58, 0).cuboid(0.0F, -13.0F, 0.0F, 16.0F, 16.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(8.0F, -12.0F, 0.0F));

        ModelPartData right_arm = body.addChild("right_arm", ModelPartBuilder.create().uv(44, 50).cuboid(-4.0F, 0.0F, -4.0F, 8.0F, 28.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(-13.0F, -13.0F, 1.0F));

        ModelPartData left_arm = body.addChild("left_arm", ModelPartBuilder.create().uv(0, 58).cuboid(-4.0F, 0.0F, -4.0F, 8.0F, 28.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(13.0F, -13.0F, 1.0F));

        ModelPartData right_leg = bone.addChild("right_leg", ModelPartBuilder.create().uv(76, 48).cuboid(-3.1F, 0.0F, -3.0F, 6.0F, 13.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(-5.9F, -13.0F, 0.0F));

        ModelPartData left_leg = bone.addChild("left_leg", ModelPartBuilder.create().uv(76, 76).cuboid(-2.9F, 0.0F, -3.0F, 6.0F, 13.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(5.9F, -13.0F, 0.0F));
        return TexturedModelData.of(modelData, 128, 128);
    }

    @Override
    public void setAngles(T wardenEntity, float f, float g, float h, float i, float j) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        float k = h - (float)((WardenEntity)wardenEntity).age;
        this.setHeadAngle(i, j);
        this.setLimbAngles(f, g);
        this.setHeadAndBodyAngles(h);
        this.setTendrilPitches(wardenEntity, h, k);
        this.updateAnimation(((WardenEntity)wardenEntity).attackingAnimationState, WardenAnimations.ATTACKING, h);
        this.updateAnimation(((WardenEntity)wardenEntity).chargingSonicBoomAnimationState, WardenAnimations.CHARGING_SONIC_BOOM, h);
        this.updateAnimation(((WardenEntity)wardenEntity).diggingAnimationState, WardenAnimations.DIGGING, h);
        this.updateAnimation(((WardenEntity)wardenEntity).emergingAnimationState, WardenAnimations.EMERGING, h);
        this.updateAnimation(((WardenEntity)wardenEntity).roaringAnimationState, WardenAnimations.ROARING, h);
        this.updateAnimation(((WardenEntity)wardenEntity).sniffingAnimationState, WardenAnimations.SNIFFING, h);
    }

    private void setHeadAngle(float yaw, float pitch) {
        this.head.pitch = pitch * ((float)Math.PI / 180);
        this.head.yaw = yaw * ((float)Math.PI / 180);
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
        this.rightLeg.pitch = 1.0f * MathHelper.cos(g + (float)Math.PI) * f;
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
        this.leftTendril.pitch = f = ((WardenEntity)warden).getTendrilPitch(tickDelta) * (float)(Math.cos((double)animationProgress * 2.25) * Math.PI * (double)0.1f);
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
