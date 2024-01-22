package de.takacick.illegalwars.mixin;

import de.takacick.illegalwars.access.PiglinProperties;
import net.minecraft.client.render.entity.model.PiglinEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PiglinEntityModel.class)
public abstract class PiglinEntityModelMixin {

    @Inject(method = "setAngles(Lnet/minecraft/entity/mob/MobEntity;FFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;copyTransform(Lnet/minecraft/client/model/ModelPart;)V", shift = At.Shift.BEFORE, ordinal = 0))
    public <T extends MobEntity> void setAngles(T mobEntity, float f, float g, float h, float i, float j, CallbackInfo info) {
        if ((Object) this instanceof PlayerEntityModel playerEntityModel) {
            if (mobEntity instanceof PiglinProperties piglinProperties
                    && piglinProperties.isUsingPiglinGoldTurret()) {
                playerEntityModel.rightArm.yaw = -15f * ((float) Math.PI / 180);
                playerEntityModel.rightArm.pitch = -53f * ((float) Math.PI / 180);
                playerEntityModel.rightArm.roll = -17.5f * ((float) Math.PI / 180);

                playerEntityModel.leftArm.yaw = 15f * ((float) Math.PI / 180);
                playerEntityModel.leftArm.pitch = -53f * ((float) Math.PI / 180);
                playerEntityModel.leftArm.roll = 17.5f * ((float) Math.PI / 180);
            }
        }

    }
}
