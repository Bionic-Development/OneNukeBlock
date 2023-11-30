package de.takacick.elementalblock.mixin.warden;

import de.takacick.elementalblock.registry.entity.living.FireElementalWardenEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.PositionSourceType;
import net.minecraft.world.event.Vibrations;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Vibrations.VibrationListener.class)
public abstract class VibrationListenerMixin {

    @Shadow
    public abstract PositionSource getPositionSource();

    @Inject(method = "listen(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/event/GameEvent;Lnet/minecraft/world/event/GameEvent$Emitter;Lnet/minecraft/util/math/Vec3d;)Z", at = @At(value = "HEAD"), cancellable = true)
    private void listen(ServerWorld world, GameEvent event, GameEvent.Emitter emitter, Vec3d emitterPos, CallbackInfoReturnable<Boolean> info) {
        if (getPositionSource().getType().equals(PositionSourceType.ENTITY)) {
            if (((EntityPositionSourceAccessor) getPositionSource()).getSource().left().orElse(null) instanceof FireElementalWardenEntity) {
                info.cancel();
            }
        }
    }
}
