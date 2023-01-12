package de.takacick.immortalmobs.registry.block.entity;

import de.takacick.immortalmobs.registry.EntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class ImmortalChainTrapBlockEntity extends BlockEntity {

    public ImmortalChainTrapBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.IMMORTAL_CHAIN_TRAP, pos, state);
    }
}
