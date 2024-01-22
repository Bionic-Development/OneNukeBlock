package de.takacick.illegalwars.mixin.warden;

import de.takacick.illegalwars.registry.entity.living.CyberWardenSecurityEntity;
import de.takacick.illegalwars.registry.entity.living.KingRatEntity;
import de.takacick.illegalwars.registry.entity.living.brain.CyberWardenSecurityBrain;
import de.takacick.illegalwars.registry.entity.living.brain.KingRatBrain;
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
        if ((Object) this instanceof KingRatEntity kingRatEntity) {
            ServerWorld serverWorld = (ServerWorld) this.getWorld();
            serverWorld.getProfiler().push("wardenBrain");
            ((Brain<KingRatEntity>) this.getBrain()).tick(serverWorld, kingRatEntity);
            this.getWorld().getProfiler().pop();
            super.mobTick();

            if (this.age % 20 == 0) {
                this.angerManager.tick(serverWorld, this::isValidTarget);
                this.updateAnger();
            }
            KingRatBrain.updateActivities(kingRatEntity);
            info.cancel();
        } else if ((Object) this instanceof CyberWardenSecurityEntity cyberWardenSecurityEntity) {
            ServerWorld serverWorld = (ServerWorld) this.getWorld();
            serverWorld.getProfiler().push("wardenBrain");
            ((Brain<CyberWardenSecurityEntity>) this.getBrain()).tick(serverWorld, cyberWardenSecurityEntity);
            this.getWorld().getProfiler().pop();
            super.mobTick();

            if (this.age % 20 == 0) {
                this.angerManager.tick(serverWorld, this::isValidTarget);
                this.updateAnger();
            }
            CyberWardenSecurityBrain.updateActivities(cyberWardenSecurityEntity);
            info.cancel();
        }
    }

    @Inject(method = "addDarknessToClosePlayers*", at = @At(value = "HEAD"), cancellable = true)
    private static void addDarknessToClosePlayers(ServerWorld world, Vec3d pos, Entity entity, int range, CallbackInfo info) {
        if (entity instanceof KingRatEntity || entity instanceof CyberWardenSecurityEntity) {
            info.cancel();
        }
    }

    @Inject(method = "playListeningSound*", at = @At(value = "HEAD"), cancellable = true)
    private void playListeningSound(CallbackInfo info) {
        if ((Object) this instanceof KingRatEntity || (Object) this instanceof CyberWardenSecurityEntity) {
            info.cancel();
        }
    }

    @Inject(method = "isValidTarget", at = @At(value = "HEAD"), cancellable = true)
    public void isValidTarget(Entity entity, CallbackInfoReturnable<Boolean> info) {
        if (entity instanceof KingRatEntity || entity instanceof CyberWardenSecurityEntity) {
            info.setReturnValue(false);
        }
    }

    @Inject(method = "increaseAngerAt(Lnet/minecraft/entity/Entity;)V", at = @At(value = "HEAD"), cancellable = true)
    public void increaseAngerAt(Entity entity, CallbackInfo info) {
        if (entity instanceof KingRatEntity || entity instanceof CyberWardenSecurityEntity) {
            info.cancel();
        }
    }
}
