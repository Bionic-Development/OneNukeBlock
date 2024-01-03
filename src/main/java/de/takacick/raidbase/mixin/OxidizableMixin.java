package de.takacick.raidbase.mixin;

import de.takacick.raidbase.registry.block.CopperHopperBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Oxidizable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Oxidizable.class)
public interface OxidizableMixin {

    @Inject(method = "getDecreasedOxidationBlock", at = @At("HEAD"), cancellable = true)
    private static void getDecreasedOxidationBlock(Block block, CallbackInfoReturnable<Optional<Block>> info) {
        if(block instanceof CopperHopperBlock) {
            info.setReturnValue(Optional.ofNullable(CopperHopperBlock.OXIDATION_LEVEL_DECREASES.get().get(block)));
        }
    }

    @Inject(method = "getUnaffectedOxidationBlock", at = @At("HEAD"), cancellable = true)
    private static void getUnaffectedOxidationBlock(Block block, CallbackInfoReturnable<Block> info) {
        if(block instanceof CopperHopperBlock) {
            Block block2 = block;
            Block block3 = CopperHopperBlock.OXIDATION_LEVEL_DECREASES.get().get(block2);
            while (block3 != null) {
                block2 = block3;
                block3 = CopperHopperBlock.OXIDATION_LEVEL_DECREASES.get().get(block2);
            }

            info.setReturnValue(block2);
        }
    }

    @Inject(method = "getIncreasedOxidationBlock", at = @At("HEAD"), cancellable = true)
    private static void getIncreasedOxidationBlock(Block block, CallbackInfoReturnable<Optional<Block>> info) {
        if(block instanceof CopperHopperBlock) {
            info.setReturnValue(Optional.ofNullable(CopperHopperBlock.OXIDATION_LEVEL_INCREASES.get().get(block)));
        }
    }

}