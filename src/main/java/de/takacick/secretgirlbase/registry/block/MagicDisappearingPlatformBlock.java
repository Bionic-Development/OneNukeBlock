package de.takacick.secretgirlbase.registry.block;

import com.mojang.serialization.MapCodec;
import de.takacick.secretgirlbase.registry.EntityRegistry;
import de.takacick.secretgirlbase.registry.ItemRegistry;
import de.takacick.secretgirlbase.registry.block.entity.MagicDisappearingPlatformBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MagicDisappearingPlatformBlock extends BlockWithEntity {

    public static final DirectionProperty PLACE = DirectionProperty.of("place", Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN);

    public MagicDisappearingPlatformBlock(Settings settings) {
        super(settings.solidBlock((state, world, pos) -> true)
                .blockVision((state, world, pos) -> true)
                .suffocates((state, world, pos) -> true));
        this.setDefaultState(this.stateManager.getDefaultState().with(PLACE, Direction.NORTH));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (world.getBlockEntity(pos)
                instanceof MagicDisappearingPlatformBlockEntity blockEntity) {
            if (blockEntity.getAlpha(0.5f) < 0.5f) {
                return VoxelShapes.empty();
            }
        }

        return super.getOutlineShape(state, world, pos, context);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (world.getBlockEntity(pos)
                instanceof MagicDisappearingPlatformBlockEntity blockEntity) {
            if (blockEntity.getAlpha(0.5f) < 0.5f) {
                return VoxelShapes.empty();
            }
        }

        return super.getCollisionShape(state, world, pos, context);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MagicDisappearingPlatformBlockEntity(pos, state);
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
                    instanceof MagicDisappearingPlatformBlockEntity giantCrusherTrapBlockEntity) {
                pos = giantCrusherTrapBlockEntity.getOwnerBlockPos();
                if (player != null && !player.isCreative()) {
                    ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                            ItemRegistry.MAGIC_DISAPPEARING_PLATFORM_ITEM.getDefaultStack());
                    world.spawnEntity(itemEntity);
                }
            }

            for (int x = -3; x <= 1; x++) {
                for (int z = -3; z <= 1; z++) {
                    BlockState blockState = world.getBlockState(pos.add(x, 0, z));
                    if (blockState.isOf(this)) {
                        if (world.getBlockEntity(pos.add(x, 0, z))
                                instanceof MagicDisappearingPlatformBlockEntity magicDisappearingPlatformBlockEntity
                                && magicDisappearingPlatformBlockEntity.getOwnerBlockPos().equals(pos)) {
                            world.breakBlock(pos.add(x, 0, z), false);
                        }
                    }
                }
            }
        }

        return super.onBreak(world, pos, state, player);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (!world.isClient) {
            Direction direction = state.get(PLACE);
            if (direction.getOffsetY() == 0) {
                pos = pos.add(direction.getVector());
            }

            BlockPos ownerPos = pos;
            world.setBlockState(ownerPos, state);
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    BlockState blockState = world.getBlockState(pos.add(x, 0, z));
                    if (!blockState.isOf(this)) {
                        world.breakBlock(pos.add(x, 0, z), true);
                        world.setBlockState(pos.add(x, 0, z), state);
                    }

                    if (world.getBlockEntity(pos.add(x, 0, z))
                            instanceof MagicDisappearingPlatformBlockEntity magicDisappearingPlatformBlockEntity) {
                        magicDisappearingPlatformBlockEntity.setOwnerBlockPos(ownerPos);
                        world.updateListeners(pos.add(x, 0, z), state, state, Block.NOTIFY_LISTENERS);
                    }
                }
            }
        }
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, EntityRegistry.MAGIC_DISAPPEARING_PLATFORM, MagicDisappearingPlatformBlockEntity::tick);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        return 1.0f;
    }

    @Override
    public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        return true;
    }

    @Override
    public boolean hasSidedTransparency(BlockState state) {
        return true;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(PLACE);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(PLACE, ctx.getSide());
    }
}
