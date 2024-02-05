package de.takacick.secretcraftbase.registry.block;

import de.takacick.secretcraftbase.registry.EntityRegistry;
import de.takacick.secretcraftbase.registry.ParticleRegistry;
import de.takacick.secretcraftbase.registry.block.entity.SecretMagicWellBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class SecretMagicWellWaterBlock extends FluidBlock implements SecretMagicWellPart, BlockEntityProvider {

    public SecretMagicWellWaterBlock(FlowableFluid fluid, Settings settings) {
        super(fluid, settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(SecretMagicWellBlock.OPEN, false));
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SecretMagicWellBlockEntity(pos, state);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient) {
            if (entity instanceof ItemEntity itemEntity) {
                if (itemEntity.getStack().isOf(Items.EMERALD)) {
                    if (world.getBlockEntity(pos) instanceof SecretMagicWellBlockEntity magicWellBlockEntity &&
                            world.getBlockEntity(magicWellBlockEntity.getOwnerBlockPos()) instanceof SecretMagicWellBlockEntity secretMagicWellBlockEntity) {
                        if (secretMagicWellBlockEntity.getAlpha(1f) >= 1f) {
                            itemEntity.discard();

                            world.playSound(null, secretMagicWellBlockEntity.getPos(), ParticleRegistry.MAGIC_VANISH, SoundCategory.BLOCKS, 1f, 1f);
                            secretMagicWellBlockEntity.setOpen(true);
                            world.updateListeners(secretMagicWellBlockEntity.getPos(),
                                    secretMagicWellBlockEntity.getCachedState(), secretMagicWellBlockEntity.getCachedState(), Block.NOTIFY_LISTENERS);
                        }
                    }
                }
            }
        }

        super.onEntityCollision(state, world, pos, entity);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        if (state.get(SecretMagicWellBlock.OPEN)) {
            return Fluids.EMPTY.getDefaultState();
        }

        return super.getFluidState(state);
    }

    public FluidState getRealFluidState(BlockState state) {
        return super.getFluidState(state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(SecretMagicWellBlock.OPEN)) {
            return VoxelShapes.empty();
        }

        if (context.isAbove(COLLISION_SHAPE, pos, true) && state.get(LEVEL) == 0 && context.canWalkOnFluid(world.getFluidState(pos.up()), state.getFluidState())) {
            return COLLISION_SHAPE;
        }
        return VoxelShapes.empty();
    }

    @Override
    public ItemStack tryDrainFluid(@Nullable PlayerEntity player, WorldAccess world, BlockPos pos, BlockState state) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canBucketPlace(BlockState state, Fluid fluid) {
        return false;
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, EntityRegistry.SECRET_MAGIC_WELL, SecretMagicWellBlockEntity::tick);
    }

    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> validateTicker(BlockEntityType<A> givenType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker) {
        return expectedType == givenType ? (BlockEntityTicker<A>) ticker : null;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(SecretMagicWellBlock.OPEN);
        super.appendProperties(builder);
    }

}
