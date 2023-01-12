package de.takacick.immortalmobs.registry.block.entity;

import de.takacick.immortalmobs.registry.EntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class ImmortalWoolBlockEntity extends BlockEntity {

    public ImmortalWoolBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.IMMORTAL_WOOL, pos, state);
    }
}
