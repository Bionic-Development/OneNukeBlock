package de.takacick.raidbase.registry.block.fluid;

import de.takacick.raidbase.registry.ItemRegistry;
import de.takacick.raidbase.registry.ParticleRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public abstract class LightningWaterFluid extends WaterFluid {

    @Override
    public Fluid getStill() {
        return ItemRegistry.STILL_LIGHTNING_WATER;
    }

    @Override
    public Fluid getFlowing() {
        return ItemRegistry.FLOWING_LIGHTNING_WATER;
    }

    @Override
    public BlockState toBlockState(FluidState state) {
        return ItemRegistry.LIGHTNING_WATER.getDefaultState().with(FluidBlock.LEVEL, WaterFluid.getBlockStateLevel(state));
    }

    @Override
    public Item getBucketItem() {
        return ItemRegistry.BUCKET_OF_ELECTRO_WATER;
    }

    public boolean matchesType(Fluid fluid) {
        return fluid.isIn(FluidTags.WATER);
    }

    @Override
    public void randomDisplayTick(World world, BlockPos pos, FluidState state, Random random) {
        if (!state.isStill() && !state.get(FALLING).booleanValue()) {
            if (random.nextInt(64) == 0) {
                world.playSound((double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, SoundEvents.BLOCK_WATER_AMBIENT, SoundCategory.BLOCKS, random.nextFloat() * 0.25f + 0.75f, random.nextFloat() + 0.5f, false);
            }
        } else if (random.nextInt(10) == 0) {
            world.addParticle(ParticleTypes.UNDERWATER, (double) pos.getX() + random.nextDouble(), (double) pos.getY() + random.nextDouble(), (double) pos.getZ() + random.nextDouble(), 0.0, 0.0, 0.0);
        }

        world.addParticle(ParticleRegistry.LIGHTNING, (double) pos.getX() + random.nextDouble(), (double) pos.getY() + random.nextDouble() * (getHeight(state) + 0.1), (double) pos.getZ() + random.nextDouble(), 0.0, 0.0, 0.0);
        world.addParticle(ParticleRegistry.LIGHTNING, (double) pos.getX() + random.nextDouble(), (double) pos.getY() + random.nextDouble() * (getHeight(state) + 0.1), (double) pos.getZ() + random.nextDouble(), 0.0, 0.0, 0.0);
    }

    public static class Flowing
            extends LightningWaterFluid {
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
            extends LightningWaterFluid {
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