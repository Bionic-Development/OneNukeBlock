package de.takacick.deathmoney.registry.block;

import de.takacick.deathmoney.DeathMoney;
import de.takacick.deathmoney.damage.DeathDamageSources;
import de.takacick.utils.BionicUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BloodFluidBlock extends FluidBlock {

    public BloodFluidBlock(FlowableFluid fluid, Settings settings) {
        super(fluid, settings);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient) {
            if(entity.damage(DeathDamageSources.BLOOD_FLOOD, 10)) {
                BionicUtils.sendEntityStatus((ServerWorld) world, entity, DeathMoney.IDENTIFIER, 11);
            }
        }
        super.onEntityCollision(state, world, pos, entity);
    }
}
