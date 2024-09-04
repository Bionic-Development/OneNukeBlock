package de.takacick.onenukeblock.registry.block.entity;

import de.takacick.onenukeblock.registry.EntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class SkylandTntBlockEntity extends BlockEntity {

    public SkylandTntBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(EntityRegistry.SKYLAND_TNT_BLOCK_ENTITY, blockPos, blockState);
    }
}

