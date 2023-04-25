package de.takacick.everythinghearts.mixin;

import de.takacick.everythinghearts.registry.entity.living.LoverWardenEntity;
import de.takacick.everythinghearts.registry.entity.living.brain.LoverWardenBrain;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.WardenAngerManager;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.event.listener.VibrationListener;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WardenEntity.class)
public abstract class WardenEntityMixin extends HostileEntity implements VibrationListener.Callback {

    @Shadow
    protected abstract void updateAnger();

    @Shadow
    private WardenAngerManager angerManager;

    @Shadow
    public abstract boolean isValidTarget(@Nullable Entity entity);

    protected WardenEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "mobTick*", at = @At(value = "HEAD"), cancellable = true)
    private void mobTick(CallbackInfo info) {
        if ((Object) this instanceof LoverWardenEntity loverWardenEntity) {

            ServerWorld serverWorld = (ServerWorld) this.world;
            serverWorld.getProfiler().push("wardenBrain");
            ((Brain<LoverWardenEntity>) this.getBrain()).tick(serverWorld, loverWardenEntity);
            this.world.getProfiler().pop();
            super.mobTick();

            if (this.age % 20 == 0) {
                this.angerManager.tick(serverWorld, this::isValidTarget);
                this.updateAnger();
            }
            LoverWardenBrain.updateActivities(loverWardenEntity);
            info.cancel();
        }
    }

    @Inject(method = "playListeningSound*", at = @At(value = "HEAD"), cancellable = true)
    private void playListeningSound(CallbackInfo info) {
        if ((Object) this instanceof LoverWardenEntity) {
            info.cancel();
        }
    }
}
