package de.takacick.secretgirlbase.registry.block;

import com.mojang.serialization.MapCodec;
import de.takacick.secretgirlbase.registry.EntityRegistry;
import de.takacick.secretgirlbase.registry.block.entity.BubbleGumLauncherBlockEntity;
import de.takacick.secretgirlbase.registry.entity.projectiles.BubbleGumEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
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
import org.jetbrains.annotations.Nullable;

public class BubbleGumLauncherBlock extends BlockWithEntity {

    private static final VoxelShape SHAPE = createShape();

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    public BubbleGumLauncherBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);

        if (placer != null) {
            if (world.getBlockEntity(pos) instanceof BubbleGumLauncherBlockEntity bubbleGumLauncherBlockEntity) {
                bubbleGumLauncherBlockEntity.setOwner(placer.getUuid());
                world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
            }
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
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
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BubbleGumLauncherBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, EntityRegistry.BUBBLE_GUM_LAUNCHER, BubbleGumLauncherBlockEntity::tick);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    public static VoxelShape createShape() {
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0, 0.75, 0, 1, 1.75, 1));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.125, 0.6875, 0.125, 0.875, 0.75, 0.875));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.3125, 0.3125, 0.3125, 0.6875, 0.6875, 0.6875));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.25, 0.25, 0.25, 0.75, 0.3125, 0.75));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.001, 0, 0.001, 0.999, 0.25, 0.999));

        return shape;
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }
}