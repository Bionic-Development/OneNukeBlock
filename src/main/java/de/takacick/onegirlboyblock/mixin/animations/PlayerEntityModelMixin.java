
package de.takacick.onegirlboyblock.mixin.animations;

import de.takacick.onegirlboyblock.access.PlayerModelProperties;
import de.takacick.onegirlboyblock.client.entity.model.AnimationBodyModel;
import de.takacick.onegirlboyblock.registry.entity.living.TurboBoardEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityModel.class)
@Implements({@Interface(iface = PlayerModelProperties.class, prefix = "onegirlboyblock$")})
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

    @Unique
    private AnimationBodyModel<T> onegirlboyblock$playerEntityModel;

    @Unique
    private ModelPart onegirlboyblock$root;

    public PlayerEntityModelMixin(ModelPart root) {
        super(root);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(ModelPart root, boolean thinArms, CallbackInfo info) {
        this.onegirlboyblock$root = root;
        this.onegirlboyblock$playerEntityModel = new AnimationBodyModel<>();
    }

    @Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("HEAD"))
    private void resetAngles(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo info) {
        if (this.onegirlboyblock$root != null) {
            this.onegirlboyblock$root.traverse().forEach(ModelPart::resetTransform);
        }

        if (livingEntity.getVehicle() instanceof TurboBoardEntity) {
            this.riding = false;
        }
    }

    @Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    private void setAngles(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo info) {
        this.onegirlboyblock$playerEntityModel.copyFromBipedState(this, ((PlayerEntityModel<?>) (Object) this).thinArms);
        this.onegirlboyblock$playerEntityModel.setAngles(livingEntity, this, f, g, h, i, j);
        addTransforms(this.head, this.onegirlboyblock$playerEntityModel.head);
        this.hat.copyTransform(this.head);

        addTransforms(this.body, this.onegirlboyblock$playerEntityModel.body);
        this.jacket.copyTransform(this.body);

        addTransforms(this.rightArm, this.onegirlboyblock$playerEntityModel.rightArm);
        this.rightSleeve.copyTransform(this.rightArm);

        addTransforms(this.leftArm, this.onegirlboyblock$playerEntityModel.leftArm);
        this.leftSleeve.copyTransform(this.leftArm);

        addTransforms(this.leftLeg, this.onegirlboyblock$playerEntityModel.leftLeg);
        this.leftPants.copyTransform(this.leftLeg);

        addTransforms(this.rightLeg, this.onegirlboyblock$playerEntityModel.rightLeg);
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

    public AnimationBodyModel<T> onegirlboyblock$getAnimationBodyModel() {
        return this.onegirlboyblock$playerEntityModel;
    }
}
