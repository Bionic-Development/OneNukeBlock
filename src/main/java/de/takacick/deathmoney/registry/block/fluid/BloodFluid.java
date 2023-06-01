package de.takacick.deathmoney.registry.block.fluid;

import de.takacick.deathmoney.registry.ItemRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;

public class BloodFluid extends CustomFluid {

    @Override
    public Fluid getStill() {
        return ItemRegistry.BLOOD_STILL;
    }

    @Override
    public Fluid getFlowing() {
        return ItemRegistry.BLOOD_FLOW;
    }

    @Override
    public Item getBucketItem() {
        return null;
    }

    @Override
    protected boolean isInfinite() {
        return true;
    }

    @Override
    protected int getFlowSpeed(WorldView worldView) {
        return 0;
    }

    @Override
    protected boolean canFlow(BlockView world, BlockPos fluidPos, BlockState fluidBlockState, Direction flowDirection, BlockPos flowTo, BlockState flowToBlockState, FluidState fluidState, Fluid fluid) {
        return false;
    }

    @Override
    protected BlockState toBlockState(FluidState fluidState) {
        return ItemRegistry.BLOOD_BLOCK.getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(fluidState));
    }

    public static class Flowing extends BloodFluid {
        @Override
        protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getLevel(FluidState fluidState) {
            return fluidState.get(LEVEL);
        }

        @Override
        public boolean isStill(FluidState fluidState) {
            return false;
        }
    }

    public static class Still extends BloodFluid {
        @Override
        public int getLevel(FluidState fluidState) {
            return 8;
        }

        @Override
        public boolean isStill(FluidState fluidState) {
            return true;
        }
    }
}
