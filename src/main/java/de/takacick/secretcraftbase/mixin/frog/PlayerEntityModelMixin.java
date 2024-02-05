package de.takacick.secretcraftbase.mixin.frog;

import de.takacick.secretcraftbase.registry.ItemRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
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
        if (livingEntity.getMainHandStack().isOf(ItemRegistry.FROG)) {
            float pitch = MathHelper.clamp(livingEntity.getPitch(MinecraftClient.getInstance().getTickDelta()), -80f, 80f);

            float progress = pitch / 90f;

            this.rightArm.pitch = (-75f + pitch) * ((float) Math.PI / 180);
            this.leftArm.pitch = (-75f + pitch) * ((float) Math.PI / 180);
            this.rightArm.yaw = -20f * ((float) Math.PI / 180);
            this.leftArm.yaw = 20f * ((float) Math.PI / 180);
            this.rightArm.roll = -20f * progress * ((float) Math.PI / 180);
            this.leftArm.roll = 20f * progress * ((float) Math.PI / 180);

            this.rightSleeve.copyTransform(this.rightArm);
            this.leftSleeve.copyTransform(this.leftArm);
        }
    }
}