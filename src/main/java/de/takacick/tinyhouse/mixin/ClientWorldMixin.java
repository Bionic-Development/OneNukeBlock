package de.takacick.tinyhouse.mixin;

import de.takacick.tinyhouse.access.EntityProperties;
import de.takacick.tinyhouse.access.PlayerProperties;
import de.takacick.tinyhouse.registry.block.entity.GiantCrusherTrapBlockEntity;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin extends World {

    @Shadow
    @Final
    private WorldRenderer worldRenderer;

    @Shadow
    public abstract @Nullable Entity getEntityById(int id);

    @Shadow
    protected abstract void tickPassenger(Entity entity, Entity passenger);

    protected ClientWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
    }

    @Inject(method = "setBlockBreakingInfo", at = @At("HEAD"), cancellable = true)
    private void setBlockBreakingInfo(int entityId, BlockPos pos, int progress, CallbackInfo info) {
        if (getBlockEntity(pos) instanceof GiantCrusherTrapBlockEntity giantCrusherTrapBlockEntity) {
            if (giantCrusherTrapBlockEntity.getOwnerBlockPos() != null
                    && getBlockState(giantCrusherTrapBlockEntity.getOwnerBlockPos()).isOf(giantCrusherTrapBlockEntity.getCachedState().getBlock())) {
                this.worldRenderer.setBlockBreakingInfo(entityId, giantCrusherTrapBlockEntity.getOwnerBlockPos(), progress);
                info.cancel();
            }
        }
    }

    @Inject(method = "tickEntity", at = @At("HEAD"), cancellable = true)
    private void stopEntityTick(Entity entity, CallbackInfo info) {
        if (entity instanceof EntityProperties entityProperties
                && entityProperties.getBlockMagnetOwner() > 0) {
            info.cancel();
        }
    }

    @Inject(method = "tickEntity", at = @At("TAIL"))
    private void tickEntity(Entity entity, CallbackInfo info) {
        if (entity instanceof PlayerProperties playerProperties
                && playerProperties.getBlockMagnetHolding() > 0) {
            Entity passenger = getEntityById(playerProperties.getBlockMagnetHolding());
            if (passenger != null) {
                tinyhouse$tickPassenger(entity, passenger);
            }
        }
    }

    private void tinyhouse$tickPassenger(Entity entity, Entity passenger) {
        passenger.resetPosition();
        ++passenger.age;
        passenger.tickRiding();
        for (Entity entity2 : passenger.getPassengerList()) {
            this.tickPassenger(passenger, entity2);
        }

        if (passenger instanceof PlayerProperties playerProperties
                && playerProperties.getBlockMagnetHolding() > -1) {
            Entity target = getEntityById(playerProperties.getBlockMagnetHolding());
            if (target != null) {
                tinyhouse$tickPassenger(passenger, target);
            }
        }
    }
}
