package de.takacick.everythinghearts.mixin;

import de.takacick.everythinghearts.EverythingHearts;
import de.takacick.everythinghearts.access.ServerWorldProperties;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.Heightmap;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(ServerWorld.class)
@Implements({@Interface(iface = ServerWorldProperties.class, prefix = "everythinghearts$")})
public abstract class ServerWorldMixin extends World {

    private int everythinghearts$heartLevel = 1;
    private boolean everythinghearts$heartRain = false;

    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, RegistryEntry<DimensionType> dimension, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates) {
        super(properties, registryRef, dimension, profiler, isClient, debugWorld, seed, maxChainedNeighborUpdates);
    }

    @Inject(method = "tickChunk", at = @At("HEAD"))
    public void tickChunk(WorldChunk chunk, int randomTickSpeed, CallbackInfo ci) {
        if (this.everythinghearts$heartRain) {
            Profiler profiler = this.getProfiler();
            profiler.swap("heartblocks");
            ChunkPos chunkPos = chunk.getPos();
            int i = chunkPos.getStartX();
            int j = chunkPos.getStartZ();

            if (this.random.nextInt(10) == 0) {
                BlockPos blockPos = this.getTopPosition(Heightmap.Type.MOTION_BLOCKING, this.getRandomPosInChunk(i, 0, j, 15));
                BlockPos blockPos2 = blockPos.down();

                BlockState blockState = this.getBlockState(blockPos2);
                EverythingHearts.replaceBlock(this, blockState, blockPos2, this.everythinghearts$heartLevel);
            }
        }
    }

    public void everythinghearts$setHeartLevel(int heartLevel) {
        this.everythinghearts$heartLevel = heartLevel;
    }

    public void everythinghearts$setHeartRaining(boolean heartRain) {
        this.everythinghearts$heartRain = heartRain;
    }
}
