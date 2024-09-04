package de.takacick.onenukeblock.mixin.warden;

import de.takacick.onenukeblock.registry.entity.living.MutatedCreeperEntity;
import net.minecraft.registry.entry.RegistryEntry;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Vibrations.VibrationListener.class)
public abstract class VibrationListenerMixin {

    @Shadow
    public abstract PositionSource getPositionSource();

    @Inject(method = "listen(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/event/Vibrations$ListenerData;Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/world/event/GameEvent$Emitter;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)V", at = @At(value = "HEAD"), cancellable = true)
    private void listen(ServerWorld world, Vibrations.ListenerData listenerData, RegistryEntry<GameEvent> event, GameEvent.Emitter emitter, Vec3d emitterPos, Vec3d listenerPos, CallbackInfo info) {
        if (getPositionSource().getType().equals(PositionSourceType.ENTITY)) {
            if (((EntityPositionSourceAccessor) getPositionSource()).getSource().left().orElse(null) instanceof MutatedCreeperEntity) {
                info.cancel();
            }
        }
    }
}
