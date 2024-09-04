
package de.takacick.onenukeblock.mixin.animations;

import de.takacick.onenukeblock.access.PlayerModelProperties;
import de.takacick.onenukeblock.client.entity.model.AnimationBodyModel;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityModel.class)
@Implements({@Interface(iface = PlayerModelProperties.class, prefix = "onenukeblock$")})
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
    @Shadow
    @Final
    public ModelPart jacket;

    @Shadow @Final public boolean thinArms;

    @Unique
    private AnimationBodyModel<T> onenukeblock$playerEntityModel;

    @Unique
    private ModelPart onenukeblock$root;

    public PlayerEntityModelMixin(ModelPart root) {
        super(root);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(ModelPart root, boolean thinArms, CallbackInfo info) {
        this.onenukeblock$root = root;
        this.onenukeblock$playerEntityModel = new AnimationBodyModel<>(thinArms);
    }

    @Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("HEAD"))
    private void resetAngles(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo info) {
        if (this.onenukeblock$root != null) {
            this.onenukeblock$root.traverse().forEach(ModelPart::resetTransform);
        }
    }

    @Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    private void setAngles(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo info) {
        this.onenukeblock$playerEntityModel.copyFromBipedState(this, ((PlayerEntityModel<?>) (Object) this).thinArms);
        this.onenukeblock$playerEntityModel.setAngles(livingEntity, this, f, g, h, i, j);
        addTransforms(this.head, this.onenukeblock$playerEntityModel.head);
        this.hat.copyTransform(this.head);

        addTransforms(this.body, this.onenukeblock$playerEntityModel.body);
        this.jacket.copyTransform(this.body);

        addTransforms(this.rightArm, this.onenukeblock$playerEntityModel.rightArm);
        this.rightSleeve.copyTransform(this.rightArm);

        addTransforms(this.leftArm, this.onenukeblock$playerEntityModel.leftArm);
        this.leftSleeve.copyTransform(this.leftArm);

        addTransforms(this.leftLeg, this.onenukeblock$playerEntityModel.leftLeg);
        this.leftPants.copyTransform(this.leftLeg);

        addTransforms(this.rightLeg, this.onenukeblock$playerEntityModel.rightLeg);
        this.rightPants.copyTransform(this.rightLeg);
    }

    @Unique
    private void addTransforms(ModelPart modelPart, ModelPart modelPart2) {
        modelPart.xScale = modelPart2.xScale;
        modelPart.yScale = modelPart2.yScale;
        modelPart.zScale = modelPart2.zScale;
        modelPart.pitch = modelPart2.pitch;
        modelPart.yaw = modelPart2.yaw;
        modelPart.roll = modelPart2.roll;
        modelPart.pivotX = modelPart2.pivotX;
        modelPart.pivotY = modelPart2.pivotY;
        modelPart.pivotZ = modelPart2.pivotZ;
    }

    public AnimationBodyModel<T> onenukeblock$getAnimationBodyModel() {
        return this.onenukeblock$playerEntityModel;
    }
}
