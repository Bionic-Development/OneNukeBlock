package de.takacick.secretcraftbase.registry.block;

import com.mojang.serialization.MapCodec;
import de.takacick.secretcraftbase.registry.EntityRegistry;
import de.takacick.secretcraftbase.registry.ItemRegistry;
import de.takacick.secretcraftbase.registry.block.entity.SecretFakeSunBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SecretFakeSunBlock extends BlockWithEntity {

    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 15.9999, 0.0, 16.0, 16.0, 16.0);

    public SecretFakeSunBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState());
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SecretFakeSunBlockEntity(pos, state);
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
                    instanceof SecretFakeSunBlockEntity secretFakeSunBlock) {
                pos = secretFakeSunBlock.getOwnerBlockPos();
                if (player != null && !player.isCreative()) {
                    ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.1, pos.getZ() + 0.5,
                            ItemRegistry.BIG_WHITE_BLOCK_ITEM.getDefaultStack());
                    world.spawnEntity(itemEntity);
                }
            }

            for (int x = -4; x <= 4; x++) {
                for (int z = -4; z <= 4; z++) {
                    BlockState blockState = world.getBlockState(pos.add(x, 0, z));
                    if (blockState.isOf(this)) {
                        if (world.getBlockEntity(pos.add(x, 0, z))
                                instanceof SecretFakeSunBlockEntity secretFakeSunBlockEntity
                                && secretFakeSunBlockEntity.getOwnerBlockPos().equals(pos)) {
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
            BlockPos ownerPos = pos;
            world.setBlockState(ownerPos, state);

            for (int x = -4; x <= 4; x++) {
                for (int z = -4; z <= 4; z++) {
                    BlockState blockState = world.getBlockState(pos.add(x, 0, z));
                    if (!blockState.isOf(this)) {
                        world.breakBlock(pos.add(x, 0, z), true);
                        world.setBlockState(pos.add(x, 0, z), state);
                    }

                    if (world.getBlockEntity(pos.add(x, 0, z))
                            instanceof SecretFakeSunBlockEntity secretFakeSunBlockEntity) {
                        secretFakeSunBlockEntity.setOwnerBlockPos(ownerPos);
                        world.updateListeners(pos.add(x, 0, z), state, state, Block.NOTIFY_LISTENERS);
                    }
                }
            }
        }
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, EntityRegistry.SECRET_FAKE_SUN, SecretFakeSunBlockEntity::tick);
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
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }
}
