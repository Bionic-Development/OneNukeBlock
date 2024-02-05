package de.takacick.secretcraftbase.registry.block;

import com.mojang.serialization.MapCodec;
import de.takacick.secretcraftbase.registry.EntityRegistry;
import de.takacick.secretcraftbase.registry.ItemRegistry;
import de.takacick.secretcraftbase.registry.block.entity.BigWhiteBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
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
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BigWhiteBlock extends BlockWithEntity {

    public static final DirectionProperty PLACE = DirectionProperty.of("place", Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN);

    public BigWhiteBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(PLACE, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BigWhiteBlockEntity(pos, state);
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
                    instanceof BigWhiteBlockEntity giantCrusherTrapBlockEntity) {
                pos = giantCrusherTrapBlockEntity.getOwnerBlockPos();
                if (player != null && !player.isCreative()) {
                    ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                            ItemRegistry.BIG_WHITE_BLOCK_ITEM.getDefaultStack());
                    world.spawnEntity(itemEntity);
                }
            }

            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    for (int y = -1; y <= 1; y++) {

                        BlockState blockState = world.getBlockState(pos.add(x, y, z));
                        if (blockState.isOf(this)) {
                            if (world.getBlockEntity(pos.add(x, y, z))
                                    instanceof BigWhiteBlockEntity bigWhiteBlockEntity
                                    && bigWhiteBlockEntity.getOwnerBlockPos().equals(pos)) {
                                world.breakBlock(pos.add(x, y, z), false);
                            }
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
                                instanceof BigWhiteBlockEntity bigWhiteBlockEntity) {
                            bigWhiteBlockEntity.setOwnerBlockPos(ownerPos);
                            world.updateListeners(pos.add(x, y, z), state, state, Block.NOTIFY_LISTENERS);
                        }
                    }
                }
            }
        }
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, EntityRegistry.BIG_WHITE_BLOCK, BigWhiteBlockEntity::tick);
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
