package de.takacick.elementalblock.mixin;

import de.takacick.elementalblock.registry.ItemRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderLayers.class)
public class RenderLayersMixin {

    @Inject(method = "getItemLayer", at = @At("HEAD"), cancellable = true)
    private static void getItemLayer(ItemStack stack, boolean direct, CallbackInfoReturnable<RenderLayer> info) {
        if (stack.isOf(ItemRegistry.WATER_BLOCK_ITEM)) {
            info.setReturnValue(RenderLayer.getTranslucent());
        }
    }
}
