package de.takacick.illegalwars.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import de.takacick.illegalwars.registry.block.entity.KingRatTrialSpawnerBlockEntity;
import de.takacick.illegalwars.registry.block.entity.spawner.TrialSpawnerStateHandler;
import net.minecraft.block.enums.TrialSpawnerState;
import net.minecraft.block.spawner.EntityDetector;
import net.minecraft.block.spawner.TrialSpawnerConfig;
import net.minecraft.block.spawner.TrialSpawnerData;
import net.minecraft.block.spawner.TrialSpawnerLogic;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(TrialSpawnerLogic.class)
public abstract class TrialSpawnerLogicMixin {

    @Shadow
    public abstract TrialSpawnerState getSpawnerState();

    @Shadow
    @Final
    private TrialSpawnerData data;

    @Shadow
    public abstract void setSpawnerState(World world, TrialSpawnerState spawnerState);

    @Shadow
    public abstract boolean canActivate(World world);

    @Shadow
    private static boolean shouldRemoveMobFromData(ServerWorld world, BlockPos pos, UUID uuid) {
        return false;
    }

    @Shadow
    @Final
    private TrialSpawnerConfig config;

    @Unique
    private boolean illegalwars$spawner;
    @Unique
    private boolean illegalwars$inactive;

    @Inject(method = "<init>(Lnet/minecraft/block/spawner/TrialSpawnerConfig;Lnet/minecraft/block/spawner/TrialSpawnerData;Lnet/minecraft/block/spawner/TrialSpawnerLogic$TrialSpawner;Lnet/minecraft/block/spawner/EntityDetector;)V", at = @At("TAIL"))
    private void init(TrialSpawnerConfig config, TrialSpawnerData data, TrialSpawnerLogic.TrialSpawner trialSpawner, EntityDetector entityDetector, CallbackInfo info) {
        if (trialSpawner instanceof KingRatTrialSpawnerBlockEntity) {
            this.illegalwars$spawner = true;
        }
    }

    @Inject(method = "tickServer", at = @At("HEAD"), cancellable = true)
    private void tickServer(ServerWorld world, BlockPos pos, CallbackInfo info) {
        if (this.illegalwars$spawner) {
            info.cancel();
            TrialSpawnerState trialSpawnerState2;
            TrialSpawnerState trialSpawnerState = this.getSpawnerState();
            if (!this.canActivate(world)) {
                if (trialSpawnerState.playsSound()) {
                    this.data.reset();
                    this.setSpawnerState(world, TrialSpawnerState.INACTIVE);
                }
                return;
            }
            if (this.data.spawnedMobsAlive.removeIf(uuid -> shouldRemoveMobFromData(world, pos, uuid))) {
                this.data.nextMobSpawnsAt = world.getTime() + (long) this.config.ticksBetweenSpawn();
            }

            if ((trialSpawnerState2 = TrialSpawnerStateHandler.tick(trialSpawnerState, pos, (TrialSpawnerLogic) (Object) this, world)) != trialSpawnerState) {
                if (trialSpawnerState2.equals(TrialSpawnerState.COOLDOWN)) {
                    this.illegalwars$inactive = true;
                    this.setSpawnerState(world, TrialSpawnerState.INACTIVE);
                } else {
                    this.setSpawnerState(world,trialSpawnerState2);
                }
            }
        }
    }

    @ModifyReturnValue(method = "canActivate", at = @At("RETURN"))
    private boolean canActivate(boolean original) {
        if (this.illegalwars$spawner && this.illegalwars$inactive) {
            return false;
        }

        return original;
    }
}