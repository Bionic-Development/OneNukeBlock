package de.takacick.onenukeblock.mixin.warden;

import de.takacick.onenukeblock.registry.entity.living.MutatedCreeperEntity;
import de.takacick.onenukeblock.registry.entity.living.ai.MutatedCreeperBrain;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.WardenAngerManager;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.Vibrations;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WardenEntity.class)
public abstract class WardenEntityMixin extends HostileEntity implements Vibrations {

    @Shadow
    protected abstract void updateAnger();

    @Shadow
    WardenAngerManager angerManager;

    @Shadow
    public abstract boolean isValidTarget(@Nullable Entity entity);

    protected WardenEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "mobTick", at = @At(value = "HEAD"), cancellable = true)
    private void mobTick(CallbackInfo info) {
        if ((Object) this instanceof MutatedCreeperEntity mutatedCreeperEntity) {
            ServerWorld serverWorld = (ServerWorld) this.getWorld();
            serverWorld.getProfiler().push("wardenBrain");
            ((Brain<MutatedCreeperEntity>) this.getBrain()).tick(serverWorld, mutatedCreeperEntity);
            this.getWorld().getProfiler().pop();
            super.mobTick();

            if (this.age % 20 == 0) {
                this.angerManager.tick(serverWorld, this::isValidTarget);
                this.updateAnger();
            }
            MutatedCreeperBrain.updateActivities(mutatedCreeperEntity);
            info.cancel();
        }
    }

    @Inject(method = "addDarknessToClosePlayers*", at = @At(value = "HEAD"), cancellable = true)
    private static void addDarknessToClosePlayers(ServerWorld world, Vec3d pos, Entity entity, int range, CallbackInfo info) {
        if (entity instanceof MutatedCreeperEntity) {
            info.cancel();
        }
    }

    @Inject(method = "playListeningSound*", at = @At(value = "HEAD"), cancellable = true)
    private void playListeningSound(CallbackInfo info) {
        if ((Object) this instanceof MutatedCreeperEntity) {
            info.cancel();
        }
    }

    @Inject(method = "isValidTarget", at = @At(value = "HEAD"), cancellable = true)
    public void isValidTarget(Entity entity, CallbackInfoReturnable<Boolean> info) {
        if (entity instanceof MutatedCreeperEntity) {
            info.setReturnValue(false);
        }
    }

    @Inject(method = "increaseAngerAt(Lnet/minecraft/entity/Entity;)V", at = @At(value = "HEAD"), cancellable = true)
    public void increaseAngerAt(Entity entity, CallbackInfo info) {
        if (entity instanceof MutatedCreeperEntity) {
            info.cancel();
        }
    }
}
