package de.takacick.raidbase.registry.block;

import de.takacick.raidbase.registry.EntityRegistry;
import de.takacick.raidbase.registry.block.entity.BeaconDeathLaserBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BeaconDeathLaserBlock extends BlockWithEntity {

    protected static final VoxelShape SHAPE = Block.createCuboidShape(6.5, -1.0, 6.5, 9.5, 2.0, 9.5);

    public BeaconDeathLaserBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        if (player.isCreative()) {

            if (world.getBlockEntity(pos) instanceof BeaconDeathLaserBlockEntity beaconDeathLaserBlockEntity) {

                beaconDeathLaserBlockEntity.setMoving(!beaconDeathLaserBlockEntity.isMoving());
                beaconDeathLaserBlockEntity.markUpdate();
                return ActionResult.SUCCESS;
            }

        }

        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (world.getBlockEntity(pos) instanceof BeaconDeathLaserBlockEntity beaconDeathLaserBlockEntity) {
            Vec3d offset = beaconDeathLaserBlockEntity.getOffset().multiply(1);
            return SHAPE.offset(offset.getX(), offset.getY(), offset.getZ());
        }

        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BeaconDeathLaserBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, EntityRegistry.BEACON_DEATH_LASER, BeaconDeathLaserBlockEntity::tick);
    }
}
