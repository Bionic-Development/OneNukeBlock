package de.takacick.deathmoney.registry.block.fluid;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class CustomFluid extends FlowableFluid {

    @Override
    public boolean matchesType(Fluid fluid) {
        return fluid == getStill() || fluid == getFlowing();
    }

    @Override
    public Fluid getFlowing() {
        return null;
    }

    @Override
    public Fluid getStill() {
        return null;
    }

    @Override
    protected boolean isInfinite() {
        return false;
    }

    @Override
    protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {
        final BlockEntity blockEntity = world.getBlockEntity(pos);
        Block.dropStacks(state, world, pos, blockEntity);
    }

    @Override
    public Item getBucketItem() {
        return null;
    }

    @Override
    protected boolean canBeReplacedWith(FluidState fluidState, BlockView blockView, BlockPos blockPos, Fluid fluid, Direction direction) {
        return false;
    }

    @Override
    protected int getFlowSpeed(WorldView worldView) {
        return 4;
    }

    @Override
    protected int getLevelDecreasePerBlock(WorldView worldView) {
        return 1;
    }

    @Override
    public int getLevel(FluidState state) {
        return 0;
    }

    @Override
    public int getTickRate(WorldView worldView) {
        return 5;
    }

    @Override
    protected float getBlastResistance() {
        return 100.0F;
    }

    @Override
    protected BlockState toBlockState(FluidState state) {
        return null;
    }

    @Override
    public boolean isStill(FluidState state) {
        return false;
    }
}
