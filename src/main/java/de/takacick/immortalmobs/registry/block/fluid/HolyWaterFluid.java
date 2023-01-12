package de.takacick.immortalmobs.registry.block.fluid;

import de.takacick.immortalmobs.registry.ItemRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class HolyWaterFluid extends CustomFluid {

    @Override
    public Fluid getStill() {
        return ItemRegistry.HOLY_WATER_STILL;
    }

    @Override
    public Fluid getFlowing() {
        return ItemRegistry.HOLY_WATER_FLOWING;
    }

    @Override
    public Item getBucketItem() {
        return ItemRegistry.HOLY_WATER_BUCKET;
    }

    @Override
    protected boolean isInfinite() {
        return true;
    }

    @Override
    protected int getFlowSpeed(WorldView worldView) {
        return 4;
    }

    @Override
    protected BlockState toBlockState(FluidState fluidState) {
        return ItemRegistry.HOLY_WATER_BLOCK.getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(fluidState));
    }
    @Override
    public void randomDisplayTick(World world, BlockPos pos, FluidState state, Random random) {
        BlockPos blockPos = pos.up();
        if (world.getBlockState(blockPos).isAir() && !world.getBlockState(blockPos).isOpaqueFullCube(world, blockPos)) {
            if (random.nextDouble() <= 0.3) {
                world.syncWorldEvent(831298212, pos, 0);
            }
        }
    }
    public static class Flowing extends HolyWaterFluid {
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

    public static class Still extends HolyWaterFluid {
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
