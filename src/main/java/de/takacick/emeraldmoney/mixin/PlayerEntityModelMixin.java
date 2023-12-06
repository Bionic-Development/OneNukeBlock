package de.takacick.emeraldmoney.mixin;

import de.takacick.emeraldmoney.registry.ItemRegistry;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
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
    public ModelPart leftPants;

    @Shadow
    @Final
    public ModelPart rightPants;

    public PlayerEntityModelMixin(ModelPart root) {
        super(root);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(ModelPart root, boolean thinArms, CallbackInfo ci) {
    }

    @Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("HEAD"))
    private void setBodyYaw(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo info) {
        if (livingEntity.getMainHandStack().isOf(ItemRegistry.VILLAGER_DRILLER)) {

            livingEntity.prevBodyYaw = livingEntity.prevHeadYaw;
            livingEntity.setBodyYaw(livingEntity.getHeadYaw());
        }
    }

    @Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    private void setAngles(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo info) {
        if (livingEntity.getMainHandStack().isOf(ItemRegistry.VILLAGER_DRILLER)) {

            if (livingEntity.isInSneakingPose()) {
                this.rightArm.pitch = -70.5f * ((float) Math.PI / 180);
                this.leftArm.pitch = -70.5f * ((float) Math.PI / 180);
            } else {
                this.rightArm.pitch = -55.5f * ((float) Math.PI / 180);
                this.leftArm.pitch = -55.5f * ((float) Math.PI / 180);
            }

            this.rightArm.yaw = -4.5f * ((float) Math.PI / 180);
            this.leftArm.yaw = 4.5f * ((float) Math.PI / 180);
            this.rightArm.roll = 0f;
            this.leftArm.roll = 0f;

            this.rightSleeve.copyTransform(this.rightArm);
            this.leftSleeve.copyTransform(this.leftArm);
        }

        if (livingEntity.isUsingItem() && livingEntity.getActiveItem().isOf(ItemRegistry.CURSED_EMERALD_STAFF)) {

            Hand hand = livingEntity.getActiveHand();
            if(hand.equals(Hand.MAIN_HAND)) {
                this.rightArm.pitch = -70.5f * ((float) Math.PI / 180);
                this.rightSleeve.copyTransform(this.rightArm);
            } else {
                this.leftArm.pitch = -70.5f * ((float) Math.PI / 180);
                this.leftSleeve.copyTransform(this.leftArm);
            }
        }

        if (livingEntity.getEquippedStack(EquipmentSlot.CHEST).isOf(ItemRegistry.VILLAGER_ROBE)) {
            this.rightLeg.pitch *= 0.5f;
            this.leftLeg.pitch *= 0.5f;
            this.rightPants.pitch *= 0.5f;
            this.leftPants.pitch *= 0.5f;
        }
    }
}
