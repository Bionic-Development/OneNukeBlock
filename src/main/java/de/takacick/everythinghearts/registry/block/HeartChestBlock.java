package de.takacick.everythinghearts.registry.block;

import de.takacick.everythinghearts.registry.EntityRegistry;
import de.takacick.everythinghearts.registry.block.entity.HeartChestBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class HeartChestBlock extends ChestBlock {

    public HeartChestBlock(Settings settings) {
        super(settings, () -> EntityRegistry.HEART_CHEST);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new HeartChestBlockEntity(pos, state);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        ChestType chestType = ChestType.SINGLE;
        Direction direction = ctx.getPlayerFacing().getOpposite();
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());

        return this.getDefaultState().with(FACING, direction).with(CHEST_TYPE, chestType).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
    }
}
