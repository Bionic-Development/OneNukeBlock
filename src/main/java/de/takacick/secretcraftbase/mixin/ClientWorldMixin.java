package de.takacick.secretcraftbase.mixin;

import de.takacick.secretcraftbase.registry.block.entity.BigWhiteBlockEntity;
import de.takacick.secretcraftbase.registry.block.entity.SecretFakeSunBlockEntity;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
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

    protected ClientWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
    }

    @Inject(method = "setBlockBreakingInfo", at = @At("HEAD"), cancellable = true)
    private void setBlockBreakingInfo(int entityId, BlockPos pos, int progress, CallbackInfo info) {
        if (getBlockEntity(pos) instanceof BigWhiteBlockEntity bigWhiteBlockEntity) {
            if (bigWhiteBlockEntity.getOwnerBlockPos() != null
                    && getBlockState(bigWhiteBlockEntity.getOwnerBlockPos()).isOf(bigWhiteBlockEntity.getCachedState().getBlock())) {
                this.worldRenderer.setBlockBreakingInfo(entityId, bigWhiteBlockEntity.getOwnerBlockPos(), progress);
                info.cancel();
            }
        } else if (getBlockEntity(pos) instanceof SecretFakeSunBlockEntity secretFakeSunBlockEntity) {
            if (secretFakeSunBlockEntity.getOwnerBlockPos() != null
                    && getBlockState(secretFakeSunBlockEntity.getOwnerBlockPos()).isOf(secretFakeSunBlockEntity.getCachedState().getBlock())) {
                this.worldRenderer.setBlockBreakingInfo(entityId, secretFakeSunBlockEntity.getOwnerBlockPos(), progress);
                info.cancel();
            }
        }
    }
}