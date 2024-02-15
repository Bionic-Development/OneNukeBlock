package de.takacick.secretgirlbase.registry.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.WallMountedBlock;
import net.minecraft.block.enums.BlockFace;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AbstractButtonBlock extends WallMountedBlock {

    public static final BooleanProperty POWERED = BooleanProperty.of("powered");
    protected static final VoxelShape CEILING_X_SHAPE = Block.createCuboidShape(5.0 - 2.0, 14.0 - 4.0, 5.0 - 2.0, 11.0 + 2.0, 16.0, 11.0 + 2.0);
    protected static final VoxelShape CEILING_Z_SHAPE = Block.createCuboidShape(5.0 - 2.0, 14.0 - 4.0, 5.0 - 2.0, 11.0 + 2.0, 16.0, 11.0 + 2.0);
    protected static final VoxelShape FLOOR_X_SHAPE = Block.createCuboidShape(5.0 - 2.0, 0.0, 5.0 - 2.0, 11.0 + 2.0, 2.0 + 4.0, 11.0 + 2.0);
    protected static final VoxelShape FLOOR_Z_SHAPE = Block.createCuboidShape(5.0 - 2.0, 0.0, 5.0 - 2.0, 11.0 + 2.0, 2.0 + 4.0, 11.0 + 2.0);
    protected static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(5.0 - 2.0, 5.0 - 2.0, 14.0 - 4.0, 11.0 + 2.0, 11.0 + 2.0, 16.0);
    protected static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(5.0 - 2.0, 5.0 - 2.0, 0.0, 11.0 + 2.0, 11.0 + 2.0, 2.0 + 4.0);
    protected static final VoxelShape WEST_SHAPE = Block.createCuboidShape(14.0 - 4.0, 5.0 - 2.0, 5.0 - 2.0, 16.0, 11.0 + 2.0, 11.0 + 2.0);
    protected static final VoxelShape EAST_SHAPE = Block.createCuboidShape(0.0, 6.0 - 2.0, 5.0 - 2.0, 2.0 + 4.0, 11.0 + 2.0, 11.0 + 2.0);
    protected static final VoxelShape CEILING_X_PRESSED_SHAPE = Block.createCuboidShape(5.0 - 2.0, 14.0 - 1.0, 5.0 - 2.0, 11.0 + 2.0, 16.0, 11.0 + 2.0);
    protected static final VoxelShape CEILING_Z_PRESSED_SHAPE = Block.createCuboidShape(5.0 - 2.0, 14.0 - 1.0, 5.0 - 2.0, 11.0 + 2.0, 16.0, 11.0 + 2.0);
    protected static final VoxelShape FLOOR_X_PRESSED_SHAPE = Block.createCuboidShape(5.0 - 2.0, 0.0, 5.0 - 2.0, 11.0 + 2.0, 2.0 + 1.0, 11.0 + 2.0);
    protected static final VoxelShape FLOOR_Z_PRESSED_SHAPE = Block.createCuboidShape(5.0 - 2.0, 0.0, 5.0 - 2.0, 11.0 + 2.0, 2.0 + 1.0, 11.0 + 2.0);
    protected static final VoxelShape NORTH_PRESSED_SHAPE = Block.createCuboidShape(5.0 - 2.0, 5.0 - 2.0, 14.0 - 1.0, 11.0 + 2.0, 11.0 + 2.0, 16.0);
    protected static final VoxelShape SOUTH_PRESSED_SHAPE = Block.createCuboidShape(5.0 - 2.0, 5.0 - 2.0, 0.0, 11.0 + 2.0, 11.0 + 2.0, 2.0 + 1.0);
    protected static final VoxelShape WEST_PRESSED_SHAPE = Block.createCuboidShape(14.0 - 1.0, 5.0 - 2.0, 5.0 - 2.0, 16.0, 11.0 + 2.0, 11.0 + 2.0);
    protected static final VoxelShape EAST_PRESSED_SHAPE = Block.createCuboidShape(0.0, 6.0 - 2.0, 5.0 - 2.0, 2.0 + 1.0, 11.0 + 2.0, 11.0 + 2.0);
    private final boolean wooden;

    protected AbstractButtonBlock(boolean wooden, Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(POWERED, false).with(FACE, BlockFace.WALL));
        this.wooden = wooden;
    }

    private int getPressTicks() {
        return this.wooden ? 30 : 20;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = state.get(FACING);
        boolean bl = state.get(POWERED);
        switch (state.get(FACE)) {
            case FLOOR: {
                if (direction.getAxis() == Direction.Axis.X) {
                    return bl ? FLOOR_X_PRESSED_SHAPE : FLOOR_X_SHAPE;
                }
                return bl ? FLOOR_Z_PRESSED_SHAPE : FLOOR_Z_SHAPE;
            }
            case WALL: {
                switch (direction) {
                    case EAST: {
                        return bl ? EAST_PRESSED_SHAPE : EAST_SHAPE;
                    }
                    case WEST: {
                        return bl ? WEST_PRESSED_SHAPE : WEST_SHAPE;
                    }
                    case SOUTH: {
                        return bl ? SOUTH_PRESSED_SHAPE : SOUTH_SHAPE;
                    }
                }
                return bl ? NORTH_PRESSED_SHAPE : NORTH_SHAPE;
            }
        }
        if (direction.getAxis() == Direction.Axis.X) {
            return bl ? CEILING_X_PRESSED_SHAPE : CEILING_X_SHAPE;
        }
        return bl ? CEILING_Z_PRESSED_SHAPE : CEILING_Z_SHAPE;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (state.get(POWERED).booleanValue()) {
            return ActionResult.CONSUME;
        }
        this.powerOn(state, world, pos);
        this.playClickSound(player, world, pos, true);
        world.emitGameEvent((Entity) player, GameEvent.BLOCK_ACTIVATE, pos);
        return ActionResult.success(world.isClient);
    }

    public void powerOn(BlockState state, World world, BlockPos pos) {
        world.setBlockState(pos, (BlockState) state.with(POWERED, true), Block.NOTIFY_ALL);
        this.updateNeighbors(state, world, pos);
        world.scheduleBlockTick(pos, this, this.getPressTicks());
    }

    protected void playClickSound(@Nullable PlayerEntity player, WorldAccess world, BlockPos pos, boolean powered) {
        world.playSound(powered ? player : null, pos, this.getClickSound(powered), SoundCategory.BLOCKS, 0.3f, powered ? 0.6f : 0.5f);
    }

    protected abstract SoundEvent getClickSound(boolean var1);

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (moved || state.isOf(newState.getBlock())) {
            return;
        }
        if (state.get(POWERED).booleanValue()) {
            this.updateNeighbors(state, world, pos);
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, net.minecraft.util.math.random.Random random) {
        if (!state.get(POWERED).booleanValue()) {
            return;
        }
        if (this.wooden) {
            this.tryPowerWithProjectiles(state, world, pos);
        } else {
            world.setBlockState(pos, state.with(POWERED, false), Block.NOTIFY_ALL);
            this.updateNeighbors(state, world, pos);
            this.playClickSound(null, world, pos, false);
            world.emitGameEvent(null, GameEvent.BLOCK_DEACTIVATE, pos);
        }

        super.scheduledTick(state, world, pos, random);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (world.isClient || !this.wooden || state.get(POWERED).booleanValue()) {
            return;
        }
        this.tryPowerWithProjectiles(state, world, pos);
    }

    private void tryPowerWithProjectiles(BlockState state, World world, BlockPos pos) {
        boolean bl2;
        List<PersistentProjectileEntity> list = world.getNonSpectatingEntities(PersistentProjectileEntity.class, state.getOutlineShape(world, pos).getBoundingBox().offset(pos));
        boolean bl = !list.isEmpty();
        if (bl != (bl2 = state.get(POWERED).booleanValue())) {
            world.setBlockState(pos, state.with(POWERED, bl), Block.NOTIFY_ALL);
            this.updateNeighbors(state, world, pos);
            this.playClickSound(null, world, pos, bl);
            world.emitGameEvent(list.stream().findFirst().orElse(null), bl ? GameEvent.BLOCK_ACTIVATE : GameEvent.BLOCK_DEACTIVATE, pos);
        }
        if (bl) {
            world.scheduleBlockTick(new BlockPos(pos), this, this.getPressTicks());
        }
    }

    private void updateNeighbors(BlockState state, World world, BlockPos pos) {
        world.updateNeighborsAlways(pos, this);
        world.updateNeighborsAlways(pos.offset(AbstractButtonBlock.getDirection(state).getOpposite()), this);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED, FACE);
    }
}