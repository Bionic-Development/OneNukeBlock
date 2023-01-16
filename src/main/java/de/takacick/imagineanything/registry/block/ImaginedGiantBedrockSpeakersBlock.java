package de.takacick.imagineanything.registry.block;

import de.takacick.imagineanything.registry.EntityRegistry;
import de.takacick.imagineanything.registry.block.entity.ImaginedGiantBedrockSpeakersBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ImaginedGiantBedrockSpeakersBlock extends BlockWithEntity {

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    private static final VoxelShape NORTH_SHAPE = createNorthShape();
    private static final VoxelShape WEST_SHAPE = createWestShape();

    public ImaginedGiantBedrockSpeakersBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }

        if (world.getBlockEntity(pos) instanceof ImaginedGiantBedrockSpeakersBlockEntity imaginedGiantBedrockSpeakersBlockEntity) {
            imaginedGiantBedrockSpeakersBlockEntity.setActivated(!imaginedGiantBedrockSpeakersBlockEntity.isActivated());
            world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
            return ActionResult.SUCCESS;
        }

        return ActionResult.CONSUME;
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
        return state.get(FACING).getOffsetZ() == 0 ? WEST_SHAPE : NORTH_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(FACING).getOffsetZ() == 0 ? WEST_SHAPE : NORTH_SHAPE;
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? checkType(type, EntityRegistry.IMAGINED_GIANT_BEDROCK_SPEAKERS_ENTITY, ImaginedGiantBedrockSpeakersBlockEntity::clientTick) : checkType(type, EntityRegistry.IMAGINED_GIANT_BEDROCK_SPEAKERS_ENTITY, ImaginedGiantBedrockSpeakersBlockEntity::serverTick);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ImaginedGiantBedrockSpeakersBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public static VoxelShape createNorthShape() {
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.15625, 0, 0.1875, 0.84375, 0.875, 0.875));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.25, 0.875, 0.3125, 0.75, 1.375, 0.75));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.3125, 1.375, 0.375, 0.6875, 1.75, 0.6875));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.71875, 0, 0.125, 0.84375, 0.875, 0.1875));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.15625, 0, 0.125, 0.28125, 0.875, 0.1875));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.28125, 0.6875, 0.125, 0.71875, 0.875, 0.1875));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.28125, 0, 0.125, 0.71875, 0.125, 0.1875));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.28125, 0.25, 0.125, 0.71875, 0.3125, 0.1875));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.6875, 0.875, 0.25, 0.75, 1.375, 0.3125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.25, 0.875, 0.25, 0.3125, 1.375, 0.3125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.5, 0.875, 0.25, 0.5625, 1.375, 0.3125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.3125, 1.25, 0.25, 0.5, 1.375, 0.3125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.3125, 0.875, 0.25, 0.5, 1, 0.3125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.5625, 0.875, 0.25, 0.6875, 0.9375, 0.3125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.5625, 1.25, 0.25, 0.6875, 1.375, 0.3125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.5625, 1.0625, 0.25, 0.6875, 1.125, 0.3125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.3125, 1.375, 0.3125, 0.4375, 1.75, 0.375));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.5625, 1.375, 0.3125, 0.6875, 1.75, 0.375));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.4375, 1.625, 0.3125, 0.5625, 1.75, 0.375));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.4375, 1.375, 0.3125, 0.5625, 1.5, 0.375));
        return shape;
    }

    public static VoxelShape createWestShape() {
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.18750000000000003, 0, 0.15624999999999997, 0.8749999999999999, 0.875, 0.8437500000000001));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.25, 0.875, 0.3125, 0.75, 1.375, 0.75));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.3125, 1.375, 0.375, 0.6875, 1.75, 0.6875));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.12500000000000003, 0, 0.15625000000000003, 0.18750000000000003, 0.875, 0.28124999999999994));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.6875, 0.875, 0.25, 0.75, 1.375, 0.3125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.25, 0.875, 0.25, 0.3125, 1.375, 0.3125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.5, 0.875, 0.25, 0.5625, 1.375, 0.3125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.3125, 1.25, 0.25, 0.5, 1.375, 0.3125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.3125, 0.875, 0.25, 0.5, 1, 0.3125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.5625, 0.875, 0.25, 0.6875, 0.9375, 0.3125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.5625, 1.25, 0.25, 0.6875, 1.375, 0.3125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.5625, 1.0625, 0.25, 0.6875, 1.125, 0.3125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.3125, 1.375, 0.3125, 0.4375, 1.75, 0.375));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.5625, 1.375, 0.3125, 0.6875, 1.75, 0.375));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.4375, 1.625, 0.3125, 0.5625, 1.75, 0.375));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.4375, 1.375, 0.3125, 0.5625, 1.5, 0.375));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.12500000000000003, 0, 0.7187499999999999, 0.18750000000000003, 0.875, 0.8437499999999999));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.12500000000000003, 0.6875, 0.28124999999999994, 0.18750000000000003, 0.875, 0.7187499999999999));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.12500000000000003, 0, 0.28124999999999994, 0.18750000000000003, 0.125, 0.7187499999999999));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.12500000000000003, 0.25, 0.28124999999999994, 0.18750000000000003, 0.3125, 0.7812499999999999));
        return shape;
    }
}
