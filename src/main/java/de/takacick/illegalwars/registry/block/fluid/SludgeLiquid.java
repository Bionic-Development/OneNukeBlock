package de.takacick.illegalwars.registry.block.fluid;

import de.takacick.illegalwars.IllegalWarsClient;
import de.takacick.illegalwars.registry.ItemRegistry;
import de.takacick.illegalwars.registry.ParticleRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class SludgeLiquid extends LavaFluid {

    @Override
    public Fluid getStill() {
        return ItemRegistry.STILL_SLUDGE_LIQUID;
    }

    @Override
    public Fluid getFlowing() {
        return ItemRegistry.FLOWING_SLUDGE_LIQUID;
    }

    @Override
    public BlockState toBlockState(FluidState state) {
        return ItemRegistry.SLUDGE_LIQUID_BLOCK.getDefaultState().with(FluidBlock.LEVEL, LavaFluid.getBlockStateLevel(state));
    }

    @Override
    public Item getBucketItem() {
        return ItemRegistry.SLUDGE_BUCKET;
    }

    @Override
    public void onScheduledTick(World world, BlockPos pos, FluidState state) {

        super.onScheduledTick(world, pos, state);
    }

    @Override
    public int getLevel(FluidState state) {
        return 0;
    }

    public boolean matchesType(Fluid fluid) {
        return fluid == getStill()
                || fluid == getFlowing()
                || fluid == Fluids.LAVA
                || fluid == Fluids.FLOWING_LAVA;
    }

    @Override
    public void randomDisplayTick(World world, BlockPos pos, FluidState state, Random random) {
        BlockPos blockPos = pos.up();
        if (world.getBlockState(blockPos).isAir() && !world.getBlockState(blockPos).isOpaqueFullCube(world, blockPos)) {
            if (random.nextInt(200) == 0) {
                world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_LAVA_AMBIENT, SoundCategory.BLOCKS, 0.2f + random.nextFloat() * 0.2f, 0.9f + random.nextFloat() * 0.15f, false);
            }
        }

        if (random.nextInt(15) == 0) {
            double d = (double) pos.getX() + random.nextDouble();
            double e = (double) pos.getY() + 0.25 + 0.5 * random.nextDouble();
            double f = (double) pos.getZ() + random.nextDouble();
            IllegalWarsClient.addSludge(new Vec3d(d, e, f), 1);
        }
    }

    @Override
    public void onRandomTick(World world, BlockPos pos, FluidState state, Random random) {

    }

    @Override
    @Nullable
    public ParticleEffect getParticle() {
        return ParticleRegistry.DRIPPING_SLUDGE;
    }

    public static class Flowing
            extends SludgeLiquid {
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
            extends SludgeLiquid {
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