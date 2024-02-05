package de.takacick.secretcraftbase.registry.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class NetherPortal extends AliasedBlockItem {

    public NetherPortal(Block block, Settings settings) {
        super(block, settings);
    }

    @Nullable
    protected BlockState getPlacementState(ItemPlacementContext context) {
        BlockState blockState = super.getPlacementState(context);

        if (blockState == null) {
            return null;
        }

        Direction.Axis axis = context.getHorizontalPlayerFacing().rotateYClockwise().getAxis();
        if (!axis.equals(Direction.Axis.Y)) {
            blockState = blockState.with(NetherPortalBlock.AXIS, axis);
        }

        return blockState;
    }


}
