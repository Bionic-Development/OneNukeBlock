package de.takacick.onedeathblock.mixin;

import de.takacick.onedeathblock.access.PlayerProperties;
import de.takacick.onedeathblock.client.model.AnimationBodyModel;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
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

    private AnimationBodyModel<T> onedeathblock$animationModel;

    public PlayerEntityModelMixin(ModelPart root) {
        super(root);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(ModelPart root, boolean thinArms, CallbackInfo info) {
        this.onedeathblock$animationModel = new AnimationBodyModel<>();
    }

    @Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    private void setAngles(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo info) {
        if (livingEntity instanceof PlayerProperties playerProperties) {
            if (playerProperties.getHeartRemovalState().isRunning()) {
                this.onedeathblock$animationModel.setAngles(livingEntity, f, g, h, i, j);

                this.rightArm.copyTransform(this.onedeathblock$animationModel.rightArm);
                this.rightSleeve.copyTransform(this.onedeathblock$animationModel.rightArm);

                if (playerProperties.getHeartRemovalState().getTimeRunning() >= 2000L) {
                    playerProperties.getHeartRemovalState().stop();
                }
            }
        }
    }
}