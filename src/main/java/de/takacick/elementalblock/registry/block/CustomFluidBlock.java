package de.takacick.elementalblock.registry.block;

import de.takacick.elementalblock.registry.ItemRegistry;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class CustomFluidBlock extends FluidBlock {
    private static final VoxelShape SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);

    public CustomFluidBlock(FlowableFluid fluid, Settings settings) {
        super(fluid, settings);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        world.scheduleFluidTick(pos, state.getFluidState().getFluid(), this.fluid.getTickRate(world));
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        world.scheduleFluidTick(pos, state.getFluidState().getFluid(), this.fluid.getTickRate(world));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        if (state.getProperties().contains(LEVEL)) {
            return super.getFluidState(state);
        }

        return fluid.getFlowing(8, true);
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        this.spawnBreakParticles(world, player, pos, getDefaultState());
        world.emitGameEvent(player, GameEvent.BLOCK_DESTROY, pos);
        world.setBlockState(pos, Blocks.AIR.getDefaultState());

        if (!player.isCreative()) {
            if (state.isOf(ItemRegistry.LAVA_BLOCK)) {
                Block.dropStack(world, pos, ItemRegistry.LAVA_BLOCK_ITEM.getDefaultStack());
            } else {
                Block.dropStack(world, pos, ItemRegistry.WATER_BLOCK_ITEM.getDefaultStack());
            }
        }
    }
}
