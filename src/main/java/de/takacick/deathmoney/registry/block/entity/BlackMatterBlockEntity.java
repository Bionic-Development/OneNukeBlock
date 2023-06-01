package de.takacick.deathmoney.registry.block.entity;

import de.takacick.deathmoney.registry.EntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class BlackMatterBlockEntity extends BlockEntity {

    public BlackMatterBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.BLACK_MATTER, pos, state);
    }
}
