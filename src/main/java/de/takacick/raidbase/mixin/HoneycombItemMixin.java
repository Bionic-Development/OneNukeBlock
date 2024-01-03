package de.takacick.raidbase.mixin;

import de.takacick.raidbase.registry.block.CopperHopperBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Oxidizable;
import net.minecraft.item.HoneycombItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(HoneycombItem.class)
public abstract class HoneycombItemMixin {

    @Inject(method = "getWaxedState", at = @At("HEAD"), cancellable = true)
    private static void getDecreasedOxidationBlock(BlockState state, CallbackInfoReturnable<Optional<BlockState>> info) {
        if(state.getBlock() instanceof CopperHopperBlock) {
            info.setReturnValue(Optional.ofNullable(CopperHopperBlock.UNWAXED_TO_WAXED_BLOCKS.get()
                    .get(state.getBlock())).map(block -> block.getStateWithProperties(state)));
        }
    }
}