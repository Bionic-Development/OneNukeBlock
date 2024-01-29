package de.takacick.illegalwars.registry.block;

import com.mojang.serialization.MapCodec;
import de.takacick.illegalwars.registry.EntityRegistry;
import de.takacick.illegalwars.registry.ItemRegistry;
import de.takacick.illegalwars.registry.block.entity.BaseWarsMoneyWheelBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class BaseWarsMoneyWheelBlock extends BlockWithEntity {

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    public BaseWarsMoneyWheelBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BaseWarsMoneyWheelBlockEntity(pos, state);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!newState.isAir()) {
            onBreak(world, pos, state, null);
            return;
        }

        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient()) {
            if (world.getBlockEntity(pos)
                    instanceof BaseWarsMoneyWheelBlockEntity baseWarsMoneyWheelBlockEntity) {
                pos = baseWarsMoneyWheelBlockEntity.getOwnerBlockPos();
                if (player != null && !player.isCreative()) {
                    ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                            ItemRegistry.BASE_WARS_MONEY_WHEEL_ITEM.getDefaultStack());
                    world.spawnEntity(itemEntity);
                }
            }

            BlockPos ownerPos = pos;

            forPositions(ownerPos, state.get(FACING), blockPos -> {
                BlockState blockState = world.getBlockState(blockPos);
                if (blockState.isOf(this)) {
                    if (world.getBlockEntity(blockPos)
                            instanceof BaseWarsMoneyWheelBlockEntity baseWarsMoneyWheelBlockEntity
                            && baseWarsMoneyWheelBlockEntity.getOwnerBlockPos().equals(ownerPos)) {
                        world.breakBlock(blockPos, false);
                        baseWarsMoneyWheelBlockEntity.markRemoved();
                    }
                }
            });
        }
        return super.onBreak(world, pos, state, player);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (!world.isClient) {
            world.setBlockState(pos, state);

            BlockPos owner = pos.add(0, 1, 0);
            forPositions(owner, state.get(FACING), blockPos -> {
                BlockState blockState = world.getBlockState(blockPos);
                if (!blockState.isOf(this)) {
                    world.breakBlock(blockPos, true);
                    world.setBlockState(blockPos, state);
                }

                if (world.getBlockEntity(blockPos)
                        instanceof BaseWarsMoneyWheelBlockEntity baseWarsMoneyWheelBlockEntity) {
                    baseWarsMoneyWheelBlockEntity.setOwnerBlockPos(owner);
                    world.updateListeners(blockPos, state, state, Block.NOTIFY_LISTENERS);
                }
            });
        }
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return getOutlineShape(state, world, pos, ShapeContext.absent()).isEmpty();
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            if (world.getBlockEntity(pos) instanceof BaseWarsMoneyWheelBlockEntity wheelBlockEntity) {
                if(world.getBlockEntity(wheelBlockEntity.getOwnerBlockPos()) instanceof BaseWarsMoneyWheelBlockEntity baseWarsMoneyWheelBlockEntity) {
                    baseWarsMoneyWheelBlockEntity.setSpin(Math.min(baseWarsMoneyWheelBlockEntity.getSpin() + 180f + world.getRandom().nextFloat() * 360f, 680f));
                    world.updateListeners(baseWarsMoneyWheelBlockEntity.getPos(),
                            baseWarsMoneyWheelBlockEntity.getCachedState(), baseWarsMoneyWheelBlockEntity.getCachedState(), Block.NOTIFY_ALL);
                }
            }
        }
        return ActionResult.SUCCESS;
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, EntityRegistry.BASE_WARS_MONEY_WHEEL, BaseWarsMoneyWheelBlockEntity::tick);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    public static void forPositions(BlockPos center, Direction direction, Consumer<BlockPos> consumer) {
        Direction nextDirection = direction.rotateYClockwise();

        int range = 3;
        int offsetX = direction.getOffsetX() == 0 ? nextDirection.getOffsetX() : 0;
        int offsetZ = direction.getOffsetZ() == 0 ? nextDirection.getOffsetZ() : 0;

        for (int y = 2; y > -range + 1; y--) {
            for (int x = 1; x > -range + 1; x--) {
                BlockPos pos = center.add(x * offsetX, y, x * offsetZ);
                if (!(y <= -range + 2 && x != 0)) {
                    consumer.accept(pos);
                }
            }
        }
    }
}
