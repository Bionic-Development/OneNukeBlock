package de.takacick.secretgirlbase.registry.entity.living.model;

import de.takacick.secretgirlbase.registry.entity.living.ZukoEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.CatEntityModel;
import net.minecraft.client.render.entity.model.OcelotEntityModel;

public class ZukoEntityModel<T extends ZukoEntity> extends OcelotEntityModel<T> {

    private final ModelPart root;
    private float sleepAnimation;
    private float tailCurlAnimation;
    private float headDownAnimation;

    public ZukoEntityModel(ModelPart modelPart) {
        super(modelPart);
        this.root = modelPart;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 21).cuboid(-3.5F, -3.0F, -2.0F, 7.0F, 6.0F, 5.0F, new Dilation(0.0F))
                .uv(34, 7).cuboid(-1.5F, 3.0F, 1.5F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-5.5F, 0.0F, -2.0F, 2.0F, 3.0F, 0.0F, new Dilation(0.0F))
                .uv(0, 0).mirrored().cuboid(3.5F, 0.0F, -2.0F, 2.0F, 3.0F, 0.0F, new Dilation(0.0F)).mirrored(false)
                .uv(34, 2).cuboid(-3.5F, -5.0F, 1.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(34, 2).mirrored().cuboid(2.5F, -5.0F, 1.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)).mirrored(false)
                .uv(23, 0).cuboid(-2.5F, 1.0F, -3.0F, 5.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 15.0F, -9.0F));

        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -3.0F, 8.0F, 11.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 17.0F, 1.0F, 1.5708F, 0.0F, 0.0F));

        ModelPartData tail1 = modelPartData.addChild("tail1", ModelPartBuilder.create().uv(17, 6).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 12.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 15.0F, 4.0F));

        ModelPartData tail2 = modelPartData.addChild("tail2", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 1.0F, 1.0F));

        ModelPartData left_hind_leg = modelPartData.addChild("left_hind_leg", ModelPartBuilder.create().uv(22, 31).cuboid(-1.5F, -1.0F, -2.0F, 3.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(3.1F, 19.0F, 4.0F));

        ModelPartData right_hind_leg = modelPartData.addChild("right_hind_leg", ModelPartBuilder.create().uv(25, 21).cuboid(-1.5F, -1.0F, -2.0F, 3.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.1F, 19.0F, 4.0F));

        ModelPartData right_front_leg = modelPartData.addChild("right_front_leg", ModelPartBuilder.create().uv(11, 33).cuboid(-2.0F, 0.0F, -1.0F, 3.0F, 6.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(3.2F, 18.0F, -4.0F));

        ModelPartData left_front_leg = modelPartData.addChild("left_front_leg", ModelPartBuilder.create().uv(0, 33).cuboid(-1.0F, 0.0F, -1.0F, 3.0F, 6.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.2F, 18.0F, -4.0F));
        return TexturedModelData.of(modelData, 48, 48);
    }

    @Override
    public void animateModel(T zukoEntity, float f, float g, float h) {
        this.root.traverse().forEach(ModelPart::resetTransform);

        this.sleepAnimation = zukoEntity.getSleepAnimation(h);
        this.tailCurlAnimation = zukoEntity.getTailCurlAnimation(h);
        this.headDownAnimation = zukoEntity.getHeadDownAnimation(h);
        if (this.sleepAnimation <= 0.0f) {
            this.head.pitch = 0.0f;
            this.head.roll = 0.0f;
            this.leftFrontLeg.pitch = 0.0f;
            this.leftFrontLeg.roll = 0.0f;
            this.rightFrontLeg.pitch = 0.0f;
            this.rightFrontLeg.roll = 0.0f;
            this.rightFrontLeg.pivotX = 3.2F + -1.2f;
            this.leftHindLeg.pitch = 0.0f;
            this.rightHindLeg.pitch = 0.0f;
            this.rightHindLeg.roll = 0.0f;
            this.rightHindLeg.pivotX = -3.1F + -1.1f;
        }
        this.body.pivotY = 17.0F;
        this.body.pivotZ = 1.0f;
        this.head.pivotY = 15.0f;
        this.head.pivotZ = -9.0f;
        this.upperTail.pivotY = 15.0f;
        this.upperTail.pivotZ = 4.0F;
        this.lowerTail.pivotY = 20.0f;
        this.lowerTail.pivotZ = 14.0f;

        this.leftFrontLeg.pivotY = 18f;
        this.leftFrontLeg.pivotZ = -4.0f;
        this.rightFrontLeg.pivotY = 18f;
        this.rightFrontLeg.pivotZ = -4.0f;

        this.leftHindLeg.pivotY = 19.0F;
        this.leftHindLeg.pivotZ = 4.0F;
        this.rightHindLeg.pivotY = 19.0F;
        this.rightHindLeg.pivotZ = 4.0F;

        this.upperTail.pitch = 0.9f;

        if (zukoEntity.isInSneakingPose()) {
            this.body.pivotY += 1.0f;
            this.head.pivotY += 2.0f;
            this.upperTail.pivotY += 1.0f;
            this.lowerTail.pivotY += -4.0f;
            this.lowerTail.pivotZ += 2.0f;
            this.upperTail.pitch = 1.5707964f;
            this.lowerTail.pitch = 1.5707964f;
            this.animationState = 0;
        } else if (zukoEntity.isSprinting()) {
            this.lowerTail.pivotY = this.upperTail.pivotY;
            this.lowerTail.pivotZ += 2.0f;
            this.upperTail.pitch = 1.5707964f;
            this.lowerTail.pitch = 1.5707964f;
            this.animationState = 2;
        } else {
            this.animationState = 1;
        }

        if (zukoEntity.isInSittingPose()) {
            modifyPart(this.body, 0f, -3f, 0f, -35f, 0f, 0f);
            modifyPart(this.head, 0f, 1f, 0.5f, -10f, 0f, 0f);
            modifyPart(this.upperTail, 0f, -4.2f, 0f, 0f, 0f, 0f);
            modifyPart(this.leftHindLeg, 0f, -4f, -2f, -87.5f, 0f, 0f);
            modifyPart(this.rightHindLeg, 0f, -4f, -2f, -87.5f, 0f, 0f);
            modifyPart(this.rightFrontLeg, 0f, 0f, -1f, -20f, 0f, 0f);
            modifyPart(this.leftFrontLeg, 0f, 0f, -1f, -20f, 0f, 0f);

            this.animationState = 3;
        }
    }

    @Override
    public void setAngles(T zukoEntity, float f, float g, float h, float i, float j) {
        super.setAngles(zukoEntity, f, g, h, i, j);
        if (this.sleepAnimation > 0.0f) {
            this.head.roll = ModelUtil.interpolateAngle(this.head.roll, -1.2707963f, this.sleepAnimation);
            this.head.yaw = ModelUtil.interpolateAngle(this.head.yaw, 1.2707963f, this.sleepAnimation);
            this.leftFrontLeg.pitch = -1.2707963f;
            this.rightFrontLeg.pitch = -0.47079635f;
            this.rightFrontLeg.roll = -0.2f;
            this.rightFrontLeg.pivotX = -0.2f;
            this.leftHindLeg.pitch = -0.4f;
            this.rightHindLeg.pitch = 0.5f;
            this.rightHindLeg.roll = -0.5f;
            this.rightHindLeg.pivotX = -0.3f;
            this.rightHindLeg.pivotY = 20.0f;
            this.upperTail.pitch = ModelUtil.interpolateAngle(this.upperTail.pitch, 0.8f, this.tailCurlAnimation);
            this.lowerTail.pitch = ModelUtil.interpolateAngle(this.lowerTail.pitch, -0.4f, this.tailCurlAnimation);
        }
        if (this.headDownAnimation > 0.0f) {
            this.head.pitch = ModelUtil.interpolateAngle(this.head.pitch, -0.58177644f, this.headDownAnimation);
        }
    }

    private void modifyPart(ModelPart modelPart, float x, float y, float z, float pitch, float yaw, float roll) {
        yaw = yaw * ((float) Math.PI / 180f);
        pitch = pitch * ((float) Math.PI / 180f);
        roll = roll * ((float) Math.PI / 180f);

        y = -y;

        modelPart.yaw += yaw;
        modelPart.pitch += pitch;
        modelPart.roll += roll;
        modelPart.pivotX += x;
        modelPart.pivotY += y;
        modelPart.pivotZ += z;
    }
}

