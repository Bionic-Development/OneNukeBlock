package de.takacick.tinyhouse.registry.block;

import de.takacick.tinyhouse.TinyHouseClient;
import de.takacick.tinyhouse.registry.EntityRegistry;
import de.takacick.tinyhouse.registry.ItemRegistry;
import de.takacick.tinyhouse.registry.block.entity.GiantCrusherTrapBlockEntity;
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
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class GiantCrusherTrapBlock extends BlockWithEntity {

    public static final DirectionProperty PLACE = DirectionProperty.of("place", Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN);
    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16, 12, 16);

    public GiantCrusherTrapBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(PLACE, Direction.NORTH));
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new GiantCrusherTrapBlockEntity(pos, state);
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
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient()) {
            if (world.getBlockEntity(pos)
                    instanceof GiantCrusherTrapBlockEntity giantCrusherTrapBlockEntity) {
                pos = giantCrusherTrapBlockEntity.getOwnerBlockPos();
                if (player != null && !player.isCreative()) {
                    ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                            ItemRegistry.GIANT_CRUSHER_TRAP_ITEM.getDefaultStack());
                    world.spawnEntity(itemEntity);
                }
            }

            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    for (int y = -1; y <= 1; y++) {

                        BlockState blockState = world.getBlockState(pos.add(x, y, z));
                        if (blockState.isOf(this)) {
                            if (world.getBlockEntity(pos.add(x, y, z))
                                    instanceof GiantCrusherTrapBlockEntity giantCrusherTrapBlockEntity
                                    && giantCrusherTrapBlockEntity.getOwnerBlockPos().equals(pos)) {
                                world.breakBlock(pos.add(x, y, z), false);
                            }
                        }
                    }
                }
            }
        }
        super.onBreak(world, pos, state, player);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (!world.isClient) {
            pos = pos.add(state.get(PLACE).getVector());
            BlockPos ownerPos = pos;
            world.setBlockState(ownerPos, state);
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    for (int y = -1; y <= 1; y++) {
                        BlockState blockState = world.getBlockState(pos.add(x, y, z));
                        if (!blockState.isOf(this)) {
                            world.breakBlock(pos.add(x, y, z), true);
                            world.setBlockState(pos.add(x, y, z), state);
                        }

                        if (world.getBlockEntity(pos.add(x, y, z))
                                instanceof GiantCrusherTrapBlockEntity giantCrusherTrapBlockEntity) {
                            giantCrusherTrapBlockEntity.setOwnerBlockPos(ownerPos);
                            world.updateListeners(pos.add(x, y, z), state, state, Block.NOTIFY_LISTENERS);
                        }
                    }
                }
            }
        }
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, EntityRegistry.GIANT_CRUSHER_TRAP, GiantCrusherTrapBlockEntity::tick);
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

    @Override
    public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        boolean bl = world.isReceivingRedstonePower(pos);

        if (world.getBlockEntity(pos)
                instanceof GiantCrusherTrapBlockEntity child && child.getPos().getY() > child.getOwnerBlockPos().getY()) {

            if (world.getBlockEntity(child.getOwnerBlockPos())
                    instanceof GiantCrusherTrapBlockEntity owner) {
                boolean old = owner.isPowered();

                if (bl) {
                    if (!owner.poweredBlocks.contains(pos)) {
                        owner.poweredBlocks.add(pos);
                    }
                } else {
                    owner.poweredBlocks.remove(pos);
                }

                if (old != owner.isPowered()) {

                    if (owner.isOwner()) {
                        world.playSound(null, owner.getPos().getX(), owner.getPos().getY() - 2, owner.getPos().getZ(),
                                owner.isPowered() ? SoundEvents.BLOCK_PISTON_EXTEND : SoundEvents.BLOCK_PISTON_CONTRACT, SoundCategory.BLOCKS, 3f, 1f);
                    }

                    world.updateListeners(owner.getPos(), owner.getCachedState(), owner.getCachedState(), Block.NOTIFY_ALL);
                }
            }
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (world.getBlockEntity(pos) instanceof GiantCrusherTrapBlockEntity crusher) {

            if (world.getBlockEntity(crusher.getOwnerBlockPos()) instanceof GiantCrusherTrapBlockEntity owner) {
                float extensionLevel = owner.getExtensionLevel(crusher.getWorld().isClient ? TinyHouseClient.getTickDelta() : 1f);
                float offset = pos.getY() - (owner.getPos().getY() - 2);
                if (offset <= 0) {
                    boolean center = pos.getX() == owner.getPos().getX() && pos.getZ() == owner.getPos().getZ();
                    float height = offset + extensionLevel;
                    double min = Math.min(Math.max(1 - height, 0), 1);

                    if (center) {
                        if (height > 1.75) {
                            return VoxelShapes.cuboid(0.125, 0, 0.125, 0.875, 1, 0.875);
                        }

                        return VoxelShapes.union(
                                VoxelShapes.cuboid(0, min, 0, 1, Math.min(min + 0.75, 1), 1),
                                VoxelShapes.cuboid(0.125, min, 0.125, 0.875, 1, 0.875)
                        );
                    }

                    if (height > 1.75) {
                        return VoxelShapes.empty();
                    }

                    return VoxelShapes.cuboid(0, min, 0, 1, Math.min(min + 0.75, 1), 1);
                } else if (offset == 1 && extensionLevel != 0) {
                    boolean center = pos.getX() == owner.getPos().getX() && pos.getZ() == owner.getPos().getZ();
                    float height = offset + extensionLevel;

                    double min = Math.min(Math.max(1 - height, 0.75), 1);
                    VoxelShape voxelShape = VoxelShapes.cuboid(0, min, 0, 1, 1, 1);
                    if (center) {
                        return VoxelShapes.union(
                                voxelShape,
                                VoxelShapes.cuboid(0.125, Math.min(Math.max(1 - height, 0), 1), 0.125, 0.875, 1, 0.875)
                        );
                    }

                    return voxelShape;
                }
            }
        }

        return VoxelShapes.fullCube();
    }


    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return getOutlineShape(state, world, pos, ShapeContext.absent()).isEmpty();
    }
}
