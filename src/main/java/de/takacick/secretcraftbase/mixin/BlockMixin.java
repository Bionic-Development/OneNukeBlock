package de.takacick.secretcraftbase.mixin;

import de.takacick.secretcraftbase.registry.block.SecretMagicWellPart;
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
        if (blockState.getBlock() instanceof SecretMagicWellPart && !(state.getBlock() instanceof SecretMagicWellPart)) {
            info.setReturnValue(true);
        }
    }
}