package de.takacick.secretcraftbase.mixin.carver;

import de.takacick.secretcraftbase.access.PlayerProperties;
import de.takacick.secretcraftbase.client.model.AnimationBodyModel;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityModel.class)
public abstract class PlayerEntityModelMixin<T extends LivingEntity> {

    @Unique
    private AnimationBodyModel<T> secretcraftbase$animationModel;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(ModelPart root, boolean thinArms, CallbackInfo info) {
        this.secretcraftbase$animationModel = new AnimationBodyModel<>();
    }

    @Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    private void setAngles(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo info) {
        if ((Object) this instanceof PlayerEntityModel<?> playerEntityModel) {

            if (livingEntity instanceof PlayerProperties playerProperties) {
                if (playerProperties.getHeartRemovalState().isRunning()) {
                    this.secretcraftbase$animationModel.setAngles(livingEntity, f, g, h, i, j);

                    playerEntityModel.rightArm.copyTransform(this.secretcraftbase$animationModel.rightArm);
                    playerEntityModel.rightSleeve.copyTransform(this.secretcraftbase$animationModel.rightArm);

                    if (playerProperties.getHeartRemovalState().getTimeRunning() >= 2000L) {
                        playerProperties.getHeartRemovalState().stop();
                    }
                }
            }
        }
    }
}