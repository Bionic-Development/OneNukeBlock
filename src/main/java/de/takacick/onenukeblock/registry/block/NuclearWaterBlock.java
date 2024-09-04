package de.takacick.onenukeblock.registry.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NuclearWaterBlock extends FluidBlock {

    public NuclearWaterBlock(FlowableFluid fluid, Settings settings) {
        super(fluid, settings);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient
                && entity instanceof LivingEntity livingEntity) {
            if (!livingEntity.hasStatusEffect(StatusEffects.POISON)
                    || livingEntity.getStatusEffect(StatusEffects.POISON).getDuration() <= 175) {
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 200, 2));
            }
        }

        super.onEntityCollision(state, world, pos, entity);
    }
}
