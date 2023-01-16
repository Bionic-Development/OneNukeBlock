package de.takacick.imagineanything.mixin;

import de.takacick.imagineanything.access.PlayerProperties;
import de.takacick.imagineanything.registry.ItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    private Vec3d velocity;

    @Inject(method = "getTeamColorValue", at = @At(value = "HEAD"), cancellable = true)
    private void getTeamColorValue(CallbackInfoReturnable<Integer> info) {
        if ((Object) this instanceof ItemEntity itemEntity && itemEntity.getStack().isOf(ItemRegistry.IMAGINED_CAVE_EXTRACTOR)) {
            info.setReturnValue(0xFF0000);
        } else if (this instanceof PlayerProperties playerProperties) {
            if (playerProperties.hasTelekinesisFlight()) {
                info.setReturnValue(0x7600BC);
            } else if (playerProperties.hasIronManForcefield()) {
                info.setReturnValue(0x8CCFFF);
            }
        }
    }

    @Inject(method = "setVelocity(Lnet/minecraft/util/math/Vec3d;)V", at = @At(value = "HEAD"), cancellable = true)
    private void setVelocity(Vec3d velocity, CallbackInfo info) {
        if ((Object) this instanceof LivingEntity livingEntity) {
            if (livingEntity.getSleepingPosition().isPresent()) {
                if (livingEntity.getWorld().getBlockState(livingEntity.getSleepingPosition().get()).isOf(ItemRegistry.IMAGINED_INFINITY_TRAPPED_BED)) {
                    this.velocity = Vec3d.ZERO;
                    info.cancel();
                }
            }
        }
    }
}