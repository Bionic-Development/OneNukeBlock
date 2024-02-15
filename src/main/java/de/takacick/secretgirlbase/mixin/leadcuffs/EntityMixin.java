package de.takacick.secretgirlbase.mixin.leadcuffs;

import de.takacick.secretgirlbase.registry.entity.custom.FireworkTimeBombEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public abstract @Nullable Entity getVehicle();

    @Shadow
    public abstract float getYaw();

    @Shadow
    public abstract void setYaw(float yaw);

    @Shadow
    public float prevYaw;

    @Inject(method = "changeLookDirection", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;clamp(FFF)F", ordinal = 1, shift = At.Shift.AFTER))
    private void changeLookDirection(double cursorDeltaX, double cursorDeltaY, CallbackInfo info) {
        if (getVehicle() instanceof FireworkTimeBombEntity fireworkTimeBombEntity) {
            float centerYaw = fireworkTimeBombEntity.getRiderDirection().asRotation();
            setYaw(MathHelper.clamp(getYaw(), centerYaw - 45f, centerYaw + 45f));
            this.prevYaw = MathHelper.clamp(this.prevYaw, centerYaw - 45f, centerYaw + 45f);
        }
    }
}