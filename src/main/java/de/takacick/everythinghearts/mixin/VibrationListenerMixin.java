package de.takacick.everythinghearts.mixin;

import de.takacick.everythinghearts.registry.entity.living.LoverWardenEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.PositionSourceType;
import net.minecraft.world.event.listener.VibrationListener;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VibrationListener.class)
public abstract class VibrationListenerMixin {

    @Shadow
    @Final
    protected PositionSource positionSource;

    @Shadow
    @Final
    protected VibrationListener.Callback callback;

    @Inject(method = "listen(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/event/GameEvent;Lnet/minecraft/world/event/GameEvent$Emitter;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)V", at = @At(value = "HEAD"), cancellable = true)
    private void listen(ServerWorld world, GameEvent gameEvent, GameEvent.Emitter emitter, Vec3d start, Vec3d end, CallbackInfo info) {
        if (positionSource.getType().equals(PositionSourceType.ENTITY)) {
            if (((EntityPositionSourceAccessor) positionSource).getSource().left().orElse(null) instanceof WardenEntity warden
                    && (warden.isRemoved() || warden instanceof LoverWardenEntity)) {
                info.cancel();
            }
        }
    }
}
