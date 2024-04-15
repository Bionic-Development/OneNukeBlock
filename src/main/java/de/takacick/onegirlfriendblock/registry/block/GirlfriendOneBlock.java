package de.takacick.onegirlfriendblock.registry.block;

import com.mojang.serialization.MapCodec;
import de.takacick.onegirlfriendblock.server.oneblock.OneBlockHandler;
import de.takacick.onegirlfriendblock.server.oneblock.OneBlockServerState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.explosion.Explosion;

import java.util.function.BiConsumer;

public class GirlfriendOneBlock extends HorizontalFacingBlock {

    public static final MapCodec<GirlfriendOneBlock> CODEC = GirlfriendOneBlock.createCodec(GirlfriendOneBlock::new);
    private static final VoxelShape NORTH = createNorth();
    private static final VoxelShape EAST = createEast();
    private static final VoxelShape SOUTH = createSouth();
    private static final VoxelShape WEST = createWest();

    public GirlfriendOneBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case SOUTH -> SOUTH;
            case EAST -> EAST;
            case WEST -> WEST;
            default -> NORTH;
        };
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public void onExploded(BlockState state, World world, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> stackMerger) {
        if (!world.isClient) {
            OneBlockServerState.getServerState(world.getServer()).ifPresent(serverState -> {
                OneBlockHandler.getInstance().drop(serverState, world, pos);
            });
        }

        world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, pos, Block.getRawIdFromState(state));
    }

    private static VoxelShape createNorth() {
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.25, -0.125, 1, 0.75, 0.375, 1.5));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.25, 0, 0.25, 0.75, 0.25, 1));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.75, 0, 0.25, 0.9375, 0.25, 1));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.0625, 0, 0.25, 0.25, 0.25, 1));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.5, 0, -0.5, 0.75, 0.25, 0.25));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.25, 0, -0.5, 0.5, 0.25, 0.25));

        return shape;
    }

    private static VoxelShape createEast() {
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(-0.5, -0.125, 0.25, 0, 0.375, 0.75));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0, 0, 0.25, 0.75, 0.25, 0.75));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0, 0, 0.75, 0.75, 0.25, 0.9375));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0, 0, 0.0625, 0.75, 0.25, 0.25));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.75, 0, 0.5, 1.5, 0.25, 0.75));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.75, 0, 0.25, 1.5, 0.25, 0.5));

        return shape;
    }

    private static VoxelShape createSouth() {
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.25, -0.125, -0.5, 0.75, 0.375, 0));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.25, 0, 0, 0.75, 0.25, 0.75));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.0625, 0, 0, 0.25, 0.25, 0.75));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.75, 0, 0, 0.9375, 0.25, 0.75));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.25, 0, 0.75, 0.5, 0.25, 1.5));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.5, 0, 0.75, 0.75, 0.25, 1.5));

        return shape;
    }

    private static VoxelShape createWest() {
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(1, -0.125, 0.25, 1.5, 0.375, 0.75));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.25, 0, 0.25, 1, 0.25, 0.75));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.25, 0, 0.0625, 1, 0.25, 0.25));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.25, 0, 0.75, 1, 0.25, 0.9375));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(-0.5, 0, 0.25, 0.25, 0.25, 0.5));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(-0.5, 0, 0.5, 0.25, 0.25, 0.75));

        return shape;
    }
}
