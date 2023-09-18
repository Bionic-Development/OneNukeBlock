package de.takacick.onesuperblock.registry.block.entity;

import de.takacick.onesuperblock.registry.EntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class SuperBlockEntity extends BlockEntity {

    public SuperBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.SUPER_BLOCK, pos, state);
    }
}
