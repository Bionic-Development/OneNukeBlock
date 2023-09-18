package de.takacick.onesuperblock.registry.block;

import de.takacick.onesuperblock.registry.item.Super;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SuperOreBlock extends OreBlock implements Super {

    public SuperOreBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {

        if (!world.isClient) {
            world.syncWorldEvent(521783128, pos, 0);
        }

        super.onBreak(world, pos, state, player);
    }

}
