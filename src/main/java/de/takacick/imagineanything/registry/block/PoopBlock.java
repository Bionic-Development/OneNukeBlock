package de.takacick.imagineanything.registry.block;

import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.concurrent.atomic.AtomicReference;

public class PoopBlock extends MudBlock {

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    private static final VoxelShape WEST_SHAPE = createWestShape();
    private static final VoxelShape NORTH_SHAPE = createNorthShape();
    private static final VoxelShape EAST_SHAPE = createEastShape();
    private static final VoxelShape SOUTH_SHAPE = createSouthShape();

    public PoopBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        world.syncWorldEvent(1853928903, pos, 0);
        super.onBreak(world, pos, state, player);
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
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case EAST -> EAST_SHAPE;
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            default -> WEST_SHAPE;
        };
    }

    public static VoxelShape createNorthShape() {
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.125, 0, 0.125, 0.875, 0.1875, 0.875));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.1875, 0.1875, 0.1875, 0.8125, 0.3125, 0.8125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.25, 0.3125, 0.25, 0.6875, 0.4375, 0.6875));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.375, 0.4375, 0.375, 0.625, 0.5625, 0.625));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.4375, 0.5625, 0.4375, 0.5625, 0.625, 0.5625));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.4375, 0.625, 0.4375, 0.5, 0.6875, 0.5));
        return shape;
    }

    public static VoxelShape createWestShape() {
        VoxelShape shape = VoxelShapes.empty();

        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.125, 0, 0.125, 0.875, 0.1875, 0.875));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.1875, 0.1875, 0.1875, 0.8125, 0.3125, 0.8125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.25, 0.3125, 0.3125, 0.6875, 0.4375, 0.75));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.375, 0.4375, 0.375, 0.625, 0.5625, 0.625));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.4375, 0.5625, 0.4375, 0.5625, 0.625, 0.5625));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.4375, 0.625, 0.5, 0.5, 0.6875, 0.5625));

        return shape;
    }

    public static VoxelShape createEastShape() {
        VoxelShape shape = VoxelShapes.empty();

        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.125, 0, 0.125, 0.875, 0.1875, 0.875));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.1875, 0.1875, 0.1875, 0.8125, 0.3125, 0.8125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.3125, 0.3125, 0.25, 0.75, 0.4375, 0.6875));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.375, 0.4375, 0.375, 0.625, 0.5625, 0.625));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.4375, 0.5625, 0.4375, 0.5625, 0.625, 0.5625));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.5, 0.625, 0.4375, 0.5625, 0.6875, 0.5));

        return shape;
    }

    public static VoxelShape createSouthShape() {
        AtomicReference<VoxelShape> shape = new AtomicReference<>(VoxelShapes.empty());

        createNorthShape().offset(-1, 0, -1).forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> {
            shape.set(VoxelShapes.union(shape.get(), VoxelShapes.cuboid(-maxX, minY, -maxZ, -minX, maxY, -minZ)));
        });

        return shape.get();
    }
}
