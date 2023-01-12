package de.takacick.immortalmobs.registry.block;

import de.takacick.immortalmobs.registry.entity.living.ImmortalEndermanEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HolyWaterBlock extends FluidBlock {

    public HolyWaterBlock(FlowableFluid fluid, Settings settings) {
        super(fluid, settings);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {


        if (!world.isClient && entity instanceof ImmortalEndermanEntity livingEntity) {
            livingEntity.damage(DamageSource.DROWN, 1.0f);

        }
        super.onEntityCollision(state, world, pos, entity);
    }
}
