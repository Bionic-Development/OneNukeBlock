package de.takacick.secretgirlbase.mixin.leadcuffs;

import de.takacick.secretgirlbase.access.LeadCuffProperties;
import de.takacick.secretgirlbase.registry.entity.custom.FireworkTimeBombEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityModel.class)
public class PlayerEntityModelMixin<T extends LivingEntity> extends BipedEntityModel<T> {

    @Shadow
    @Final
    public ModelPart rightSleeve;
    @Shadow
    @Final
    public ModelPart leftSleeve;

    @Shadow
    @Final
    private boolean thinArms;

    public PlayerEntityModelMixin(ModelPart root) {
        super(root);
    }
    @Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("HEAD"))
    private void setRiding(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo info) {
        if (livingEntity.getVehicle() instanceof FireworkTimeBombEntity fireworkTimeBombEntity) {
            this.riding = false;
            livingEntity.prevBodyYaw = fireworkTimeBombEntity.getRiderDirection().asRotation();
            livingEntity.bodyYaw = fireworkTimeBombEntity.getRiderDirection().asRotation();
        }
    }
    @Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    private void setAngles(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo info) {
        if (livingEntity.getVehicle() instanceof FireworkTimeBombEntity) {
            this.riding = false;
            this.rightArm.pitch = 0f;
            this.rightArm.yaw = 0f;
            this.rightArm.roll = 0f;
            this.leftArm.pitch = 0f;
            this.leftArm.yaw = 0f;
            this.leftArm.roll = 0f;
            this.rightSleeve.copyTransform(this.rightArm);
            this.leftSleeve.copyTransform(this.leftArm);
        } else if (livingEntity instanceof LeadCuffProperties leadCuffProperties && leadCuffProperties.isLeadCuffed()) {
            if (this.thinArms) {
                this.rightArm.pitch = -60f * ((float) Math.PI / 180);
                this.rightArm.yaw = -20f * ((float) Math.PI / 180);
                this.rightArm.roll = -0f * ((float) Math.PI / 180);
                this.leftArm.pitch = -60f * ((float) Math.PI / 180);
                this.leftArm.yaw = 20f * ((float) Math.PI / 180);
                this.leftArm.roll = 0f * ((float) Math.PI / 180);
                this.rightArm.translate(new Vector3f(0.4f, 0, 0));
                this.leftArm.translate(new Vector3f(-0.4f, 0, 0));
            } else {
                this.rightArm.pitch = -60f * ((float) Math.PI / 180);
                this.rightArm.yaw = -30f * ((float) Math.PI / 180);
                this.rightArm.roll = -0f * ((float) Math.PI / 180);
                this.leftArm.pitch = -60f * ((float) Math.PI / 180);
                this.leftArm.yaw = 30f * ((float) Math.PI / 180);
                this.leftArm.roll = 0f * ((float) Math.PI / 180);
                this.rightArm.translate(new Vector3f(0.5f, 0, 0));
                this.leftArm.translate(new Vector3f(-0.5f, 0, 0));
            }

            this.rightSleeve.copyTransform(this.rightArm);
            this.leftSleeve.copyTransform(this.leftArm);
        }
    }
}