package de.takacick.tinyhouse.registry.block;

import de.takacick.tinyhouse.server.VoxelShapeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
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
import org.jetbrains.annotations.Nullable;

public class ChickenBlock extends Block {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    private static final VoxelShape BASE = VoxelShapes.union(
            VoxelShapes.cuboid(0.3125, 0.3125, 0.3125, 0.6875, 0.6875, 0.8125),
            VoxelShapes.cuboid(0.375, 0.5625, 0.1875, 0.625, 0.9375, 0.375),
            VoxelShapes.cuboid(0.4375, 0.5625, 0.125, 0.5625, 0.6875, 0.25),
            VoxelShapes.cuboid(0.375, 0.6875, 0.0625, 0.625, 0.8125, 0.1875),
            VoxelShapes.cuboid(0.5, 0.0000625, 0.375, 0.6875, 0.0000625, 0.5625),
            VoxelShapes.cuboid(0.5, 0.0000625, 0.5625, 0.6875, 0.3125625, 0.5625),
            VoxelShapes.cuboid(0.3125, 0.0000625, 0.5625, 0.5, 0.3125625, 0.5625),
            VoxelShapes.cuboid(0.3125, 0.0000625, 0.375, 0.5, 0.0000625, 0.5625),
            VoxelShapes.cuboid(0.6875, 0.375, 0.375, 0.75, 0.625, 0.75),
            VoxelShapes.cuboid(0.25, 0.375, 0.375, 0.3125, 0.625, 0.75)
    );

    public ChickenBlock(Settings settings) {
        super(settings);

        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapeUtils.rotateShape(Direction.NORTH, state.get(FACING), BASE);
    }

    @Override
    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        if (mirror == BlockMirror.NONE) {
            return state;
        }
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }
}
