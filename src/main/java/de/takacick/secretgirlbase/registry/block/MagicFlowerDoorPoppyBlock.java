package de.takacick.secretgirlbase.registry.block;

import de.takacick.secretgirlbase.registry.block.entity.MagicFlowerDoorBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class MagicFlowerDoorPoppyBlock extends MagicFlowerDoorBlock {

    protected static final VoxelShape SHAPE = Block.createCuboidShape(5.0, 0.0, 5.0, 11.0, 10.0, 11.0);

    public MagicFlowerDoorPoppyBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);

        if (itemStack.isOf(Items.BONE_MEAL)) {
            if (world.getBlockEntity(pos) instanceof MagicFlowerDoorBlockEntity flowerDoorBlockEntity
                    && world.getBlockEntity(flowerDoorBlockEntity.getOwnerBlockPos()) instanceof MagicFlowerDoorBlockEntity magicFlowerDoorBlockEntity) {
                if (!magicFlowerDoorBlockEntity.isOpening()) {
                    if (!player.isCreative()) {
                        itemStack.decrement(1);
                    }
                    world.syncWorldEvent(9412934, pos, 0);
                    magicFlowerDoorBlockEntity.setOpen(true);
                    world.updateListeners(magicFlowerDoorBlockEntity.getPos(),
                            magicFlowerDoorBlockEntity.getCachedState(), magicFlowerDoorBlockEntity.getCachedState(), Block.NOTIFY_LISTENERS);
                    return ActionResult.SUCCESS;
                }
            }
        }

        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        return Items.POPPY.getDefaultStack();
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(OPEN)) {
            return VoxelShapes.empty();
        }

        if (world.getBlockEntity(pos)
                instanceof MagicFlowerDoorBlockEntity blockEntity) {
            if (blockEntity.isOpening()) {
                return VoxelShapes.empty();
            }
        }

        return SHAPE;
    }
}
