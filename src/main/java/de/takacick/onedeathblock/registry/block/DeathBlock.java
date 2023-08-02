package de.takacick.onedeathblock.registry.block;

import de.takacick.onedeathblock.damage.DeathDamageSources;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class DeathBlock extends Block {

    public DeathBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (context instanceof EntityShapeContext entityShapeContext
                && entityShapeContext.getEntity() instanceof LivingEntity livingEntity
                && !livingEntity.isSneaking()
                && livingEntity.isPartOfGame()
                && livingEntity.isAlive()
                && !livingEntity.getWorld().isClient) {

            if (state.getOutlineShape(world, pos)
                    .getBoundingBoxes().stream()
                    .anyMatch(box -> box.offset(pos).expand(0.001, 0.001, 0.001).intersects(livingEntity.getBoundingBox()))) {
                livingEntity.damage(DeathDamageSources.DEATH_BLOCK, 2.5f);

                if (!livingEntity.getWorld().getBlockTickScheduler().isQueued(pos, state.getBlock())) {
                    livingEntity.getWorld().createAndScheduleBlockTick(pos, state.getBlock(), 2);
                    livingEntity.getWorld().playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundCategory.BLOCKS, 1f, 1f + (float) livingEntity.getRandom().nextDouble() * 0.2f);
                }
            }
        }

        return super.getCollisionShape(state, world, pos, context);
    }
}
