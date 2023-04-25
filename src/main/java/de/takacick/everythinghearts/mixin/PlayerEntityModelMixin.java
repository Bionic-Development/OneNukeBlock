package de.takacick.everythinghearts.mixin;

import de.takacick.everythinghearts.access.PlayerEntityModelProperties;
import de.takacick.everythinghearts.access.PlayerProperties;
import de.takacick.everythinghearts.registry.entity.custom.model.HeartHandsModel;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityModel.class)
@Implements({@Interface(iface = PlayerEntityModelProperties.class, prefix = "everythinghearts$")})
public class PlayerEntityModelMixin<T extends LivingEntity> extends BipedEntityModel<T> {

    @Shadow @Final public ModelPart leftSleeve;
    @Shadow @Final public ModelPart rightSleeve;
    private HeartHandsModel everythinghearts$heartHandsModel;
    private T everythinghearts$tempEntity;

    public PlayerEntityModelMixin(ModelPart root) {
        super(root);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(ModelPart root, boolean thinArms, CallbackInfo info) {
        this.everythinghearts$heartHandsModel = new HeartHandsModel(thinArms);
    }

    @Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    private void setAngles(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo info) {
        this.everythinghearts$tempEntity = livingEntity;
        this.everythinghearts$heartHandsModel.getRoot().visible = false;

        if (livingEntity instanceof PlayerProperties playerProperties) {

            if(playerProperties.getHeartTransformTicks() > 0) {
                this.rightArm.setAngles(0f, 0f, 0f);
                this.rightSleeve.setAngles(0f, 0f, 0f);
                this.leftArm.setAngles(0f, 0f, 0f);
                this.leftSleeve.setAngles(0f, 0f, 0f);
                this.rightArm.roll = 1.57079632679f;
                this.rightSleeve.roll = 1.57079632679f;
                this.leftArm.roll = -1.57079632679f;
                this.leftSleeve.roll = -1.57079632679f;
            }

            if (playerProperties.isHeart()) {
                this.everythinghearts$heartHandsModel.getRoot().visible = true;
                this.everythinghearts$heartHandsModel.getLeftArm().copyTransform(this.leftArm);
                this.everythinghearts$heartHandsModel.getRightArm().copyTransform(this.rightArm);
            }
        }
    }

    @Inject(method = "setArmAngle", at = @At("HEAD"))
    private void setArmAngle(Arm arm, MatrixStack matrices, CallbackInfo info) {
        if (this.everythinghearts$tempEntity instanceof PlayerProperties playerProperties) {
            if (playerProperties.isHeart()) {
                ModelPart modelPart = this.getArm(arm);
                modelPart.translate(new Vec3f(0, 0.3f, 0));
            }
        }
    }

    public HeartHandsModel everythinghearts$getHeartHandsModel() {
        return this.everythinghearts$heartHandsModel;
    }
}
