package de.takacick.imagineanything.mixin;

import de.takacick.imagineanything.access.PlayerProperties;
import de.takacick.imagineanything.registry.ItemRegistry;
import de.takacick.imagineanything.registry.entity.custom.model.HeadModel;
import de.takacick.imagineanything.registry.item.HeadItem;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
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
    private HeadModel<T> imagineanything$playerEntityModel;

    public PlayerEntityModelMixin(ModelPart root) {
        super(root);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(ModelPart root, boolean thinArms, CallbackInfo ci) {
        this.imagineanything$playerEntityModel = new HeadModel<>();
    }

    @Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("HEAD"))
    private void resetAngles(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo info) {
        this.head.resetTransform();
    }

    @Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    private void setAngles(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo info) {
        if (livingEntity.getMainHandStack().getItem() instanceof HeadItem || livingEntity.getOffHandStack().getItem() instanceof HeadItem) {
            this.leftArm.pitch = -55f * ((float) Math.PI / 180);
            this.rightArm.pitch = -55f * ((float) Math.PI / 180);
            this.rightArm.yaw = -15f * ((float) Math.PI / 180);
            this.leftArm.yaw = 15f * ((float) Math.PI / 180);

            this.rightSleeve.copyTransform(this.rightArm);
            this.leftSleeve.copyTransform(this.leftArm);
        }

        if (livingEntity instanceof PlayerProperties playerProperties) {
            if (playerProperties.removedHead()) {
                this.head.visible = false;
                this.hat.visible = false;
            }
            if (playerProperties.getHeadRemovalState().isRunning()) {
                this.head.visible = true;
                this.hat.visible = true;
                this.head.resetTransform();
                this.rightArm.resetTransform();
                this.leftArm.resetTransform();
                imagineanything$playerEntityModel.setAngles(livingEntity, f, g, h, i, j);

                this.head.copyTransform(imagineanything$playerEntityModel.head);
                this.hat.copyTransform(imagineanything$playerEntityModel.head);
                this.rightArm.copyTransform(imagineanything$playerEntityModel.rightArm);
                this.rightSleeve.copyTransform(imagineanything$playerEntityModel.rightArm);
                this.leftArm.copyTransform(imagineanything$playerEntityModel.leftArm);
                this.leftSleeve.copyTransform(imagineanything$playerEntityModel.leftArm);

                if (playerProperties.getHeadRemovalState().getTimeRunning() >= 1.52f * 1000L) {
                    playerProperties.getHeadRemovalState().stop();
                }
            }

            if (livingEntity.getEquippedStack(EquipmentSlot.CHEST).isOf(ItemRegistry.IRON_MAN_SUIT)) {
                this.head.visible = true;
                this.hat.visible = true;
            }
        }

        if (livingEntity instanceof PlayerProperties playerProperties &&
                (playerProperties.hasIronManLaser()) &&
                (livingEntity.isInSwimmingPose() || livingEntity.isFallFlying() || playerProperties.getFallFlying())) {
            this.head.pitch = -1.6507964f;
        }
    }
}
