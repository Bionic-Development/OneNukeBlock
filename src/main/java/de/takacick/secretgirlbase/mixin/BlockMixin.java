package de.takacick.secretgirlbase.mixin;

import de.takacick.secretgirlbase.registry.block.MagicFlowerDoorPart;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class BlockMixin {

    @Inject(method = "shouldDrawSide", at = @At(value = "HEAD"), cancellable = true)
    private static void shouldDrawSide(BlockState state, BlockView world, BlockPos pos, Direction side, BlockPos otherPos, CallbackInfoReturnable<Boolean> info) {
        BlockState blockState = world.getBlockState(otherPos);
        if (blockState.getBlock() instanceof MagicFlowerDoorPart) {
            if (!blockState.isFullCube(world, pos)) {
                info.setReturnValue(true);
            } else if (!(state.getBlock() instanceof MagicFlowerDoorPart)) {
                info.setReturnValue(true);
            } else {
                info.setReturnValue(false);
            }
        }
    }
}