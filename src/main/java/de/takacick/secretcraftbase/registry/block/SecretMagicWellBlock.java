package de.takacick.secretcraftbase.registry.block;

import com.mojang.serialization.MapCodec;
import de.takacick.secretcraftbase.registry.EntityRegistry;
import de.takacick.secretcraftbase.registry.ItemRegistry;
import de.takacick.secretcraftbase.registry.block.entity.SecretMagicWellBlockEntity;
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
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class SecretMagicWellBlock extends BlockWithEntity implements SecretMagicWellPart {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final BooleanProperty OPEN = Properties.OPEN;

    public SecretMagicWellBlock(Settings settings) {
        super(settings.solidBlock((state, world, pos) -> !state.get(SecretMagicWellBlock.OPEN))
                .blockVision((state, world, pos) -> !state.get(SecretMagicWellBlock.OPEN))
                .suffocates((state, world, pos) -> !state.get(SecretMagicWellBlock.OPEN)));
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(OPEN, false));
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        if (state.get(SecretMagicWellBlock.OPEN)) {
            return true;
        }

        return super.canPathfindThrough(state, world, pos, type);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SecretMagicWellBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(OPEN)) {
            return VoxelShapes.empty();
        }

        if (world.getBlockEntity(pos)
                instanceof SecretMagicWellBlockEntity blockEntity) {
            return blockEntity.getBlockState()
                    .getOutlineShape(blockEntity.getWorld(), blockEntity.getOwnerBlockPos());
        }

        return super.getOutlineShape(state, world, pos, context);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(OPEN)) {
            return VoxelShapes.empty();
        }

        if (world.getBlockEntity(pos)
                instanceof SecretMagicWellBlockEntity blockEntity) {

            return blockEntity.getBlockState().getCollisionShape(blockEntity.getWorld(), blockEntity.getOwnerBlockPos());
        }

        return VoxelShapes.fullCube();
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient()) {
            if (world.getBlockEntity(pos)
                    instanceof SecretMagicWellBlockEntity blockEntity) {
                if (blockEntity.getBlockState().isOf(Blocks.TORCH)) {
                    super.onBreak(world, pos, state, player);
                    return state;
                }

                pos = blockEntity.getOwnerBlockPos();
                if (player != null && !player.isCreative()) {
                    ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                            ItemRegistry.SECRET_MAGIC_WELL.getDefaultStack());
                    world.spawnEntity(itemEntity);
                }
            }

            BlockPos owner = pos;
            forPositions(vec3d -> {
                BlockPos blockPos = owner.add(BlockPos.ofFloored(vec3d));
                BlockState blockState = world.getBlockState(blockPos);
                if (blockState.getBlock() instanceof SecretMagicWellPart) {
                    if (world.getBlockEntity(blockPos)
                            instanceof SecretMagicWellBlockEntity secretMagicWellBlockEntity
                            && secretMagicWellBlockEntity.getOwnerBlockPos().equals(owner)) {
                        world.breakBlock(blockPos, false);
                        world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
                        secretMagicWellBlockEntity.markRemoved();
                    }
                }
            });
        }
        super.onBreak(world, pos, state, player);
        return state;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (!world.isClient) {
            world.setBlockState(pos, state);

            BlockPos owner = pos;
            forPositions(vec3d -> {
                BlockPos blockPos = owner.add(BlockPos.ofFloored(vec3d));
                BlockState targetState = SecretMagicWellBlockEntity.BLOCKS.getOrDefault(vec3d, Blocks.AIR.getDefaultState());

                BlockState blockState = world.getBlockState(blockPos);
                if (!(blockState.getBlock() instanceof SecretMagicWellPart)) {
                    world.breakBlock(blockPos, true);
                }

                if (targetState.isOf(Blocks.COBBLESTONE)) {
                    if (world.getRandom().nextDouble() <= 0.2) {
                        targetState = Blocks.MOSSY_COBBLESTONE.getDefaultState();
                        world.setBlockState(blockPos, ItemRegistry.SECRET_MAGIC_WELL_MOSSY_COBBLESTONE.getStateWithProperties(state));
                    } else {
                        world.setBlockState(blockPos, ItemRegistry.SECRET_MAGIC_WELL_COBBLESTONE.getStateWithProperties(state));
                    }
                } else if (targetState.isOf(Blocks.MOSSY_COBBLESTONE)) {
                    world.setBlockState(blockPos, ItemRegistry.SECRET_MAGIC_WELL_MOSSY_COBBLESTONE.getStateWithProperties(state));
                } else if (targetState.isOf(Blocks.OAK_FENCE)) {
                    world.setBlockState(blockPos, ItemRegistry.SECRET_MAGIC_WELL_OAK_FENCE.getStateWithProperties(state));
                } else if (targetState.isOf(Blocks.TORCH)) {
                    targetState = ItemRegistry.SECRET_MAGIC_WELL_TORCH.getDefaultState();
                    world.setBlockState(blockPos, ItemRegistry.SECRET_MAGIC_WELL_TORCH.getStateWithProperties(state));
                } else if (targetState.isOf(Blocks.WATER)) {
                    world.setBlockState(blockPos, ItemRegistry.SECRET_MAGIC_WELL_WATER.getDefaultState());
                } else {
                    world.setBlockState(blockPos, state);
                }

                if (world.getBlockEntity(blockPos)
                        instanceof SecretMagicWellBlockEntity blockEntity) {
                    blockEntity.setBlockState(targetState);
                    blockEntity.setOwnerBlockPos(owner);
                    world.updateListeners(blockPos, state, state, Block.NOTIFY_LISTENERS);
                }
            });
        }
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, EntityRegistry.SECRET_MAGIC_WELL, SecretMagicWellBlockEntity::tick);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
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
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        if (world.getBlockEntity(pos) instanceof SecretMagicWellBlockEntity secretMagicWellBlockEntity) {
            if (!(secretMagicWellBlockEntity.getBlockState().getBlock() instanceof SecretMagicWellPart)) {
                return secretMagicWellBlockEntity.getBlockState().getBlock().getPickStack(world, pos, state);
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN);
        super.appendProperties(builder);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    public static void forPositions(Consumer<Vec3d> consumer) {
        SecretMagicWellBlockEntity.BLOCKS.forEach((vec3d, blockState) -> {
            consumer.accept(vec3d);
        });
    }
}
