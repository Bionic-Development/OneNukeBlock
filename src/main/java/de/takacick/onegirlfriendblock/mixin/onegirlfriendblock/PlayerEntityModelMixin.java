package de.takacick.onegirlfriendblock.mixin.onegirlfriendblock;

import de.takacick.onegirlfriendblock.access.PlayerProperties;
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
    @Shadow
    @Final
    public ModelPart leftSleeve;

    public PlayerEntityModelMixin(ModelPart root) {
        super(root);
    }

    @Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    private void setAngles(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo info) {
        if (livingEntity instanceof PlayerProperties playerProperties && playerProperties.hasOneGirlfriendBlock()) {

            this.rightArm.pitch = -37.5f * ((float) Math.PI / 180);
            this.leftArm.pitch = -37.5f * ((float) Math.PI / 180);

            this.rightArm.yaw = -4.5f * ((float) Math.PI / 180);
            this.leftArm.yaw = 4.5f * ((float) Math.PI / 180);
            this.rightArm.roll = 0f;
            this.leftArm.roll = 0f;

            this.rightSleeve.copyTransform(this.rightArm);
            this.leftSleeve.copyTransform(this.leftArm);
        }
    }
}
