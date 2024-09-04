package de.takacick.onenukeblock.utils.worldedit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.chunk.light.ChunkLightProvider;
import org.jetbrains.annotations.Nullable;

public class WorldChunkUtils {

    @Nullable
    public static BlockState setBlockState(World world, BlockPos pos, BlockState state, boolean moved) {
        if (!(world instanceof ServerWorld serverWorld)) {
            return null;
        }

        if (pos.getY() > world.getHeight() || pos.getY() < -64) {
            return null;
        }

        WorldChunk worldChunk = world.getWorldChunk(pos);

        int l;
        int k;
        int i = pos.getY();

        if (worldChunk.getSectionArray().length <= worldChunk.getSectionIndex(i)) {
            return null;
        }

        ChunkSection chunkSection = worldChunk.getSection(worldChunk.getSectionIndex(i));
        boolean bl = chunkSection.isEmpty();
        if (bl && state.isAir()) {
            return null;
        }
        int j = pos.getX() & 0xF;
        BlockState blockState = chunkSection.setBlockState(j, k = i & 0xF, l = pos.getZ() & 0xF, state);
        if (blockState == state) {
            return null;
        }
        Block block = state.getBlock();
        worldChunk.heightmaps.get(Heightmap.Type.MOTION_BLOCKING).trackUpdate(j, i, l, state);
        worldChunk.heightmaps.get(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES).trackUpdate(j, i, l, state);
        worldChunk.heightmaps.get(Heightmap.Type.OCEAN_FLOOR).trackUpdate(j, i, l, state);
        worldChunk.heightmaps.get(Heightmap.Type.WORLD_SURFACE).trackUpdate(j, i, l, state);
        boolean bl2 = chunkSection.isEmpty();
        if (bl != bl2) {
            worldChunk.getWorld().getChunkManager().getLightingProvider().setSectionStatus(pos, bl2);
        }
        if (ChunkLightProvider.needsLightUpdate(worldChunk, pos, blockState, state)) {
            Profiler profiler = worldChunk.getWorld().getProfiler();
            profiler.push("updateSkyLightSources");
            worldChunk.getChunkSkyLight().isSkyLightAccessible(worldChunk, j, i, l);
            profiler.swap("queueCheckLight");
            worldChunk.getWorld().getChunkManager().getLightingProvider().checkBlock(pos);
            profiler.pop();
        }
        boolean bl3 = blockState.hasBlockEntity();
        if (!worldChunk.getWorld().isClient) {
            blockState.onStateReplaced(worldChunk.getWorld(), pos, state, moved);
        } else if (!blockState.isOf(block) && bl3) {
            worldChunk.removeBlockEntity(pos);
        }
        if (!chunkSection.getBlockState(j, k, l).isOf(block)) {
            return null;
        }

        if (state.hasBlockEntity()) {
            BlockEntity blockEntity = worldChunk.getBlockEntity(pos, WorldChunk.CreationType.CHECK);
            if (blockEntity == null) {
                blockEntity = ((BlockEntityProvider) block).createBlockEntity(pos, state);
                if (blockEntity != null) {
                    worldChunk.addBlockEntity(blockEntity);
                }
            } else {
                blockEntity.setCachedState(state);
                worldChunk.updateTicker(blockEntity);
            }
        }
        worldChunk.setNeedsSaving(true);
        serverWorld.getChunkManager().markForUpdate(pos);
        return blockState;
    }


}
