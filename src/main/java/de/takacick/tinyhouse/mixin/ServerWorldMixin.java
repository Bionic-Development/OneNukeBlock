package de.takacick.tinyhouse.mixin;

import de.takacick.tinyhouse.access.EntityProperties;
import de.takacick.tinyhouse.access.PlayerProperties;
import net.minecraft.entity.Entity;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World {

    @Shadow
    public abstract @Nullable Entity getEntityById(int id);

    @Shadow
    protected abstract void tickPassenger(Entity vehicle, Entity passenger);

    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
    }

    @Inject(method = "tickEntity", at = @At("HEAD"), cancellable = true)
    private void stopEntityTick(Entity entity, CallbackInfo info) {
        if (entity instanceof EntityProperties entityProperties
                && entityProperties.getBlockMagnetOwner() > -1) {
            Entity holder = getEntityById(entityProperties.getBlockMagnetOwner());

            if (!(holder instanceof PlayerProperties playerProperties
                    && playerProperties.getBlockMagnetHolding() == entity.getId())) {
                entityProperties.setBlockMagnetOwner(null);
                return;
            }

            info.cancel();
        }
    }

    @Inject(method = "tickEntity", at = @At("TAIL"))
    private void tickEntity(Entity entity, CallbackInfo info) {
        if (entity instanceof PlayerProperties playerProperties
                && playerProperties.getBlockMagnetHolding() > -1) {
            Entity passenger = getEntityById(playerProperties.getBlockMagnetHolding());
            if (passenger != null) {
                tinyhouse$tickPassenger(entity, passenger);
            }
        }
    }

    private void tinyhouse$tickPassenger(Entity entity, Entity passenger) {
        passenger.resetPosition();
        ++passenger.age;
        Profiler profiler = this.getProfiler();
        profiler.push(() -> Registries.ENTITY_TYPE.getId(passenger.getType()).toString());
        profiler.visit("tickPassenger");
        passenger.tickRiding();
        profiler.pop();
        for (Entity target : passenger.getPassengerList()) {
            this.tickPassenger(passenger, target);
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
