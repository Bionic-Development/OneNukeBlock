package de.takacick.onescaryblock.registry.entity.living.model;

import de.takacick.onescaryblock.registry.entity.living.BloodManEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.CrossbowPosing;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.util.math.MathHelper;

public class BloodManEntityModel extends PlayerEntityModel<BloodManEntity> {
    public BloodManEntityModel(ModelPart root, boolean thinArms) {
        super(root, thinArms);
    }

    @Override
    public void setAngles(BloodManEntity livingEntity, float f, float g, float h, float i, float j) {
        super.setAngles(livingEntity, f, g, h, i, j);
        if(livingEntity.isAttacking()) {
            CrossbowPosing.meleeAttack(this.leftArm, this.rightArm, livingEntity.isAttacking(), this.handSwingProgress, h);
        }

        if (this.leftArmPose == BipedEntityModel.ArmPose.THROW_SPEAR) {
            this.leftArm.pitch = this.leftArm.pitch * 0.5f - (float) Math.PI;
            this.leftArm.yaw = 0.0f;
        }
        if (this.rightArmPose == BipedEntityModel.ArmPose.THROW_SPEAR) {
            this.rightArm.pitch = this.rightArm.pitch * 0.5f - (float) Math.PI;
            this.rightArm.yaw = 0.0f;
        }
        if (this.leaningPitch > 0.0f) {
            this.rightArm.pitch = this.lerpAngle(this.leaningPitch, this.rightArm.pitch, -2.5132742f) + this.leaningPitch * 0.35f * MathHelper.sin(0.1f * h);
            this.leftArm.pitch = this.lerpAngle(this.leaningPitch, this.leftArm.pitch, -2.5132742f) - this.leaningPitch * 0.35f * MathHelper.sin(0.1f * h);
            this.rightArm.roll = this.lerpAngle(this.leaningPitch, this.rightArm.roll, -0.15f);
            this.leftArm.roll = this.lerpAngle(this.leaningPitch, this.leftArm.roll, 0.15f);
            this.leftLeg.pitch -= this.leaningPitch * 0.55f * MathHelper.sin(0.1f * h);
            this.rightLeg.pitch += this.leaningPitch * 0.55f * MathHelper.sin(0.1f * h);
            this.head.pitch = 0.0f;
        }
    }
}
