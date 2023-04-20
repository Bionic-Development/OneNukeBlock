package de.takacick.stealbodyparts.mixin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import de.takacick.stealbodyparts.access.PlayerEntityModelProperties;
import de.takacick.stealbodyparts.access.PlayerProperties;
import de.takacick.stealbodyparts.registry.entity.custom.model.AnimationBodyModel;
import de.takacick.stealbodyparts.registry.entity.custom.model.BodyModel;
import de.takacick.stealbodyparts.utils.BodyPart;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntityModel.class)
@Implements({@Interface(iface = PlayerEntityModelProperties.class, prefix = "stealbodyparts$")})
public class PlayerEntityModelMixin<T extends LivingEntity> extends BipedEntityModel<T> {

    @Shadow
    @Final
    public ModelPart rightSleeve;

    private AnimationBodyModel<T> stealbodyparts$animationModel;
    private BodyModel<T> stealbodyparts$bodyModel;
    private ModelPart stealbodyparts$root;
    private T stealbodyparts$tempEntity;

    public PlayerEntityModelMixin(ModelPart root) {
        super(root);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(ModelPart root, boolean thinArms, CallbackInfo info) {
        this.stealbodyparts$animationModel = new AnimationBodyModel<>();
        this.stealbodyparts$bodyModel = new BodyModel<>();
        this.stealbodyparts$root = root;
    }

    @Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    private void setAngles(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo info) {
        this.stealbodyparts$tempEntity = livingEntity;
        this.stealbodyparts$bodyModel.getBody().visible = false;

        if (livingEntity instanceof PlayerProperties playerProperties) {
            if (playerProperties.getHeartRemovalState().isRunning()) {
                this.stealbodyparts$animationModel.setAngles(livingEntity, f, g, h, i, j);

                this.rightArm.copyTransform(this.stealbodyparts$animationModel.rightArm);
                this.rightSleeve.copyTransform(this.stealbodyparts$animationModel.rightArm);

                if (playerProperties.getHeartRemovalState().getTimeRunning() >= 2000L) {
                    playerProperties.getHeartRemovalState().stop();
                }
            } else if (playerProperties.removedHeart()) {
                this.body.visible = false;
                this.stealbodyparts$bodyModel.getBody().copyTransform(this.body);
                this.stealbodyparts$bodyModel.getInside().copyTransform(this.body);
                this.stealbodyparts$bodyModel.getBody().visible = true;
                this.stealbodyparts$bodyModel.getInside().visible = true;
            }

            for (BodyPart bodyPart : BodyPart.values()) {
                if (!playerProperties.hasBodyPart(bodyPart.getIndex())) {
                    bodyPart.getParts().forEach(part -> {
                        if (this.stealbodyparts$root.hasChild(part)) {
                            this.stealbodyparts$root.getChild(part).visible = false;
                        }
                    });
                } else {
                    bodyPart.getParts().forEach(part -> {
                        if (this.stealbodyparts$root.hasChild(part)) {
                            this.stealbodyparts$root.getChild(part).visible = true;
                        }
                    });
                }
            }
        }
    }

    @Inject(method = "getBodyParts", at = @At("RETURN"), cancellable = true)
    private void getBodyParts(CallbackInfoReturnable<Iterable<ModelPart>> info) {
        info.setReturnValue(Iterables.concat(info.getReturnValue(), ImmutableList.of(this.stealbodyparts$bodyModel.getBody())));
    }

    @Inject(method = "setArmAngle", at = @At("HEAD"), cancellable = true)
    public void setArmAngle(Arm arm, MatrixStack matrices, CallbackInfo info) {
        BodyPart bodyPart = BodyPart.RIGHT_ARM;
        if (arm.equals(Arm.LEFT)) {
            bodyPart = BodyPart.LEFT_ARM;
        }

        if (this.stealbodyparts$tempEntity instanceof PlayerProperties playerProperties) {
            if (!playerProperties.hasBodyPart(bodyPart.getIndex())) {
                ModelPart modelPart = this.getArm(arm);
                ModelTransform modelTransform = modelPart.getTransform();
                float pivotX = modelTransform.pivotX;
                float pivotY = modelTransform.pivotY;
                float pivotZ = modelTransform.pivotZ;

                modelPart.setPivot(pivotX > 0 ? 4f : -4f, -6f, pivotZ * -4f);
                modelPart.pitch *= 0.1f;
                modelPart.yaw *= 0.1f;
                modelPart.rotate(matrices);
                modelPart.setPivot(pivotX, pivotY, pivotZ);

                info.cancel();
            }
        }
    }

    public BodyModel<T> stealbodyparts$getBodyModel() {
        return this.stealbodyparts$bodyModel;
    }
}
