package de.takacick.illegalwars.mixin.warden;

import de.takacick.illegalwars.registry.ParticleRegistry;
import de.takacick.illegalwars.registry.entity.living.KingRatEntity;
import net.minecraft.entity.ai.brain.task.RoarTask;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RoarTask.class)
public abstract class RoarTaskMixin {

    @Inject(method = "keepRunning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/mob/WardenEntity;J)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/WardenEntity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V", shift = At.Shift.BEFORE), cancellable = true)
    private void keepRunning(ServerWorld serverWorld, WardenEntity wardenEntity, long l, CallbackInfo info) {
        if (wardenEntity instanceof KingRatEntity kingRatEntity) {
            kingRatEntity.playSound(ParticleRegistry.RAT_SCREAM, 5.0f, 0.6f);
            info.cancel();
        }
    }
}
