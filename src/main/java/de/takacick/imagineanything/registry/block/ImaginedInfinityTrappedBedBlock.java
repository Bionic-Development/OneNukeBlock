package de.takacick.imagineanything.registry.block;

import de.takacick.imagineanything.registry.block.entity.TrappedBedBlockEntity;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.BedPart;
import net.minecraft.client.gui.screen.SleepingChatScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class ImaginedInfinityTrappedBedBlock extends BedBlock {

    public ImaginedInfinityTrappedBedBlock(DyeColor color, Settings settings) {
        super(color, settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TrappedBedBlockEntity(pos, state, getColor());
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.CONSUME;
        }
        if (state.get(PART) != BedPart.HEAD && !(state = world.getBlockState(pos = pos.offset(state.get(FACING)))).isOf(this)) {
            return ActionResult.CONSUME;
        }
        if (state.get(OCCUPIED).booleanValue()) {
            return ActionResult.SUCCESS;
        }
        player.sleep(pos);

        return ActionResult.SUCCESS;
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (state.get(PART) != BedPart.HEAD && !(state = world.getBlockState(pos = pos.offset(state.get(FACING)))).isOf(this)) {
            return;
        }

        if (entity instanceof LivingEntity livingEntity && !livingEntity.getPose().equals(EntityPose.SLEEPING) && !state.get(OCCUPIED).booleanValue()) {
            if (!world.isClient) {
                livingEntity.setVelocity(new Vec3d(0, 0, 0));
                livingEntity.velocityModified = true;
                livingEntity.sleep(pos);
                livingEntity.teleport((double)pos.getX() + 0.5, (double)pos.getY() + 0.6875, (double)pos.getZ() + 0.5);
            }
        } else {
            super.onSteppedOn(world, pos, state, entity);
        }
    }

    @Override
    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {

    }

    @Override
    public void onEntityLand(BlockView world, Entity entity) {

    }
}
