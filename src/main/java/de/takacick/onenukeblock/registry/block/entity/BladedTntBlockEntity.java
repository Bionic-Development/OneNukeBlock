package de.takacick.onenukeblock.registry.block.entity;

import de.takacick.onenukeblock.registry.EntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class BladedTntBlockEntity extends BlockEntity {

    public BladedTntBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(EntityRegistry.BLADED_TNT_BLOCK_ENTITY, blockPos, blockState);
    }
}

