package de.takacick.secretcraftbase.registry.block.fluid;

import com.mojang.datafixers.util.Pair;
import de.takacick.secretcraftbase.registry.ItemRegistry;
import it.unimi.dsi.fastutil.shorts.Short2BooleanMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public abstract class MagicWellWaterFluid extends WaterFluid {

    @Override
    public Fluid getStill() {
        return ItemRegistry.STILL_MAGIC_WELL_WATER;
    }

    @Override
    public Fluid getFlowing() {
        return ItemRegistry.FLOWING_MAGIC_WELL_WATER;
    }

    @Override
    public BlockState toBlockState(FluidState state) {
        return ItemRegistry.SECRET_MAGIC_WELL_WATER.getDefaultState().with(FluidBlock.LEVEL, WaterFluid.getBlockStateLevel(state));
    }

    @Override
    public boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos, Fluid fluid, Direction direction) {
        return false;
    }

    @Override
    public Item getBucketItem() {
        return ItemStack.EMPTY.getItem();
    }

    @Override
    protected void tryFlow(World world, BlockPos fluidPos, FluidState state) {

    }

    @Override
    protected int getFlowSpeedBetween(WorldView world, BlockPos pos, int i, Direction direction, BlockState state, BlockPos fromPos, Short2ObjectMap<Pair<BlockState, FluidState>> stateCache, Short2BooleanMap flowDownCache) {
        return 0;
    }

    @Override
    public int getLevel(FluidState state) {
        return 0;
    }

    public static class Flowing
            extends MagicWellWaterFluid {
        @Override
        protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getLevel(FluidState state) {
            return state.get(LEVEL);
        }

        @Override
        public boolean isStill(FluidState state) {
            return false;
        }
    }

    public static class Still
            extends MagicWellWaterFluid {
        @Override
        public int getLevel(FluidState state) {
            return 8;
        }

        @Override
        public boolean isStill(FluidState state) {
            return true;
        }
    }
}