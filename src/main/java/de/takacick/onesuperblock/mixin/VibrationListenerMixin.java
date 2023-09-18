package de.takacick.onesuperblock.mixin;

import de.takacick.onesuperblock.registry.entity.living.SuperFiedWardenEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.PositionSourceType;
import net.minecraft.world.event.listener.VibrationListener;
import org.jetbrains.annotations.Nullable;
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
    protected float distance;

    @Shadow
    @Nullable
    protected VibrationListener.@Nullable Vibration vibration;

    @Shadow
    protected int delay;

    @Shadow
    @Final
    protected VibrationListener.Callback callback;

    @Inject(method = "listen(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/event/GameEvent;Lnet/minecraft/world/event/GameEvent$Emitter;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)V", at = @At(value = "HEAD"), cancellable = true)
    private void listen(ServerWorld world, GameEvent gameEvent, GameEvent.Emitter emitter, Vec3d start, Vec3d end, CallbackInfo info) {
        if (positionSource.getType().equals(PositionSourceType.ENTITY)) {
            if (((EntityPositionSourceAccessor) positionSource).getSource().left().orElse(null) instanceof SuperFiedWardenEntity) {
                this.distance = (float) start.distanceTo(end);
                this.vibration = new VibrationListener.Vibration(gameEvent, this.distance, start, emitter.sourceEntity());
                this.delay = MathHelper.floor(this.distance);
                this.callback.onListen();

                info.cancel();
            }
        }
    }
}
