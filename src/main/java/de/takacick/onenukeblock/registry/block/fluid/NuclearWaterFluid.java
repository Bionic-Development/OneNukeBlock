package de.takacick.onenukeblock.registry.block.fluid;

import de.takacick.onenukeblock.OneNukeBlock;
import de.takacick.onenukeblock.client.helper.NukeParticleUtil;
import de.takacick.onenukeblock.registry.ItemRegistry;
import de.takacick.onenukeblock.registry.ParticleRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.joml.Vector3f;

public abstract class NuclearWaterFluid extends WaterFluid {
    public static final TagKey<Fluid> NUCLEAR_WATER = TagKey.of(RegistryKeys.FLUID, Identifier.of(OneNukeBlock.MOD_ID, "nuclear_water"));
    public static int HEX_COLOR = 0x4E8914;
    public static Vector3f COLOR = Vec3d.unpackRgb(HEX_COLOR).toVector3f();

    @Override
    public Fluid getStill() {
        return ItemRegistry.STILL_NUCLEAR_WATER;
    }

    @Override
    public Fluid getFlowing() {
        return ItemRegistry.FLOWING_NUCLEAR_WATER;
    }

    @Override
    public BlockState toBlockState(FluidState state) {
        return ItemRegistry.NUCLEAR_WATER.getDefaultState().with(FluidBlock.LEVEL, WaterFluid.getBlockStateLevel(state));
    }

    @Override
    public Item getBucketItem() {
        return ItemRegistry.NUCLEAR_WATER_BUCKET;
    }

    public boolean matchesType(Fluid fluid) {
        return fluid == getStill()
                || fluid == getFlowing()
                || fluid == Fluids.WATER
                || fluid == Fluids.FLOWING_WATER;
    }

    @Override
    public void randomDisplayTick(World world, BlockPos pos, FluidState state, Random random) {
        if (!state.isStill() && !state.get(FALLING).booleanValue()) {
            if (random.nextInt(64) == 0) {
                world.playSound((double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, SoundEvents.BLOCK_WATER_AMBIENT, SoundCategory.BLOCKS, random.nextFloat() * 0.25f + 0.75f, random.nextFloat() + 0.5f, false);
            }
        } else if (random.nextInt(10) == 0) {
            world.addParticle(ParticleRegistry.NUCLEAR_UNDERWATER, (double) pos.getX() + random.nextDouble(), (double) pos.getY() + random.nextDouble(), (double) pos.getZ() + random.nextDouble(), 0.0, 0.0, 0.0);
        }

        if (random.nextInt(15) == 0) {
            double d = (double) pos.getX() + random.nextDouble();
            double e = (double) pos.getY() + 0.25 + 0.5 * random.nextDouble();
            double f = (double) pos.getZ() + random.nextDouble();
            NukeParticleUtil.addPotion(new Vec3d(d, e, f), HEX_COLOR, 1);
        }
    }

    public static class Flowing
            extends NuclearWaterFluid {
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
            extends NuclearWaterFluid {
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