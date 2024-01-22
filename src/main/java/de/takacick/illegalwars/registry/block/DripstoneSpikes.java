package de.takacick.illegalwars.registry.block;

import com.mojang.serialization.MapCodec;
import de.takacick.illegalwars.registry.EffectRegistry;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class DripstoneSpikes
        extends FacingBlock {
    public static final DirectionProperty HORIZONTAL = DirectionProperty.of("horizontal", Direction.Type.HORIZONTAL);

    protected static final VoxelShape POSITIVE_X_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 6.0, 16.0, 16.0);
    protected static final VoxelShape NEGATIVE_X_SHAPE = Block.createCuboidShape(10.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape POSITIVE_Y_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 6.0, 16.0);
    protected static final VoxelShape NEGATIVE_Y_SHAPE = Block.createCuboidShape(0.0, 10.0, 0.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape POSITIVE_Z_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 6.0);
    protected static final VoxelShape NEGATIVE_Z_SHAPE = Block.createCuboidShape(0.0, 0.0, 10.0, 16.0, 16.0, 16.0);

    public DripstoneSpikes(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.UP).with(HORIZONTAL, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends FacingBlock> getCodec() {
        return null;
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {

        if (!state.getOutlineShape(world, pos).getBoundingBox().offset(pos).intersects(entity.getBoundingBox())) {
            return;
        }

        entity.slowMovement(state, new Vec3d(0.8f, 0.75, 0.8f));
        if (!(world.isClient || entity.lastRenderX == entity.getX() && entity.lastRenderZ == entity.getZ())) {
            double d = Math.abs(entity.getX() - entity.lastRenderX);
            double e = Math.abs(entity.getZ() - entity.lastRenderZ);
            if (d >= (double) 0.003f || e >= (double) 0.003f) {
                entity.damage(world.getDamageSources().generic(), 2.0f);
            }
        }

        if (!world.isClient) {
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.addStatusEffect(new StatusEffectInstance(EffectRegistry.BLEEDING, 60, 0, false, false, true));
            }
        }

        super.onEntityCollision(state, world, pos, entity);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getSide();
        BlockState blockState = ctx.getWorld().getBlockState(ctx.getBlockPos().offset(direction.getOpposite()));

        BlockState result = this.getDefaultState();

        if (direction.getOffsetY() != 0) {
            result = result.with(HORIZONTAL, ctx.getHorizontalPlayerFacing());
        }

        if (blockState.isOf(this) && blockState.get(FACING) == direction) {
            return result.with(FACING, direction.getOpposite());
        }
        return result.with(FACING, direction);
    }

    public static boolean canPlaceAt(WorldView world, BlockPos pos, Direction direction) {
        BlockPos blockPos = pos.offset(direction);
        return world.getBlockState(blockPos).isSideSolidFullSquare(world, blockPos, direction.getOpposite());
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos.offset(state.get(FACING).getOpposite()));
        return canPlaceAt(world, pos, state.get(FACING).getOpposite())
                || blockState.isFullCube(world, pos)
                || blockState.isOf(Blocks.MOVING_PISTON)
                || blockState.isOf(Blocks.PISTON_HEAD)
                || blockState.isOf(Blocks.STICKY_PISTON);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(FACING).getOpposite() == direction) {
            if (!state.canPlaceAt(world, pos)) {
                return Blocks.AIR.getDefaultState();
            }
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        boolean negative = state.get(FACING).getDirection().offset() < 0;

        return switch (state.get(FACING).getAxis()) {
            case X -> negative ? NEGATIVE_X_SHAPE : POSITIVE_X_SHAPE;
            case Y -> negative ? NEGATIVE_Y_SHAPE : POSITIVE_Y_SHAPE;
            default -> negative ? NEGATIVE_Z_SHAPE : POSITIVE_Z_SHAPE;
        };
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.with(FACING, mirror.apply(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, HORIZONTAL);
    }
}
