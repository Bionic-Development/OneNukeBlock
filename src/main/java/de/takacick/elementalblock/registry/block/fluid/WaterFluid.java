package de.takacick.elementalblock.registry.block.fluid;

import de.takacick.elementalblock.registry.ItemRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.Optional;

public class WaterFluid extends CustomFluid {

    @Override
    public Fluid getStill() {
        return ItemRegistry.WATER;
    }

    @Override
    public Fluid getFlowing() {
        return ItemRegistry.FLOWING_WATER;
    }

    @Override
    public Item getBucketItem() {
        return Items.WATER_BUCKET;
    }

    @Override
    protected boolean isInfinite(World world) {
        return true;
    }

    @Override
    protected int getFlowSpeed(WorldView worldView) {
        return 0;
    }

    public boolean matchesType(Fluid fluid) {
        return fluid == getStill() || fluid == getFlowing() || (fluid == Fluids.WATER) || (fluid == Fluids.FLOWING_WATER);
    }

    @Override
    protected BlockState toBlockState(FluidState fluidState) {
        return ItemRegistry.WATER_BLOCK.getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(fluidState));
    }

    @Override
    public Optional<SoundEvent> getBucketFillSound() {
        return Optional.of(SoundEvents.ITEM_BUCKET_FILL);
    }

    @Override
    protected boolean canFlow(BlockView world, BlockPos fluidPos, BlockState fluidBlockState, Direction flowDirection, BlockPos flowTo, BlockState flowToBlockState, FluidState fluidState, Fluid fluid) {
        if (!fluidState.isEmpty()) {
            return false;
        }

        return super.canFlow(world, fluidPos, fluidBlockState, flowDirection, flowTo, flowToBlockState, fluidState, fluid);
    }

    @Override
    protected void tryFlow(World world, BlockPos fluidPos, FluidState state) {

    }

    public static class Flowing extends WaterFluid {
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

    public static class Still extends WaterFluid {
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
