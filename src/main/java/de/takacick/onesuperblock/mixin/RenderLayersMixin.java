package de.takacick.onesuperblock.mixin;

import de.takacick.onesuperblock.client.render.SuperRenderLayers;
import de.takacick.onesuperblock.registry.ItemRegistry;
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
        if (stack.isOf(ItemRegistry.SUPER_ORE_ITEM) || stack.isOf(ItemRegistry.DEEPSLATE_SUPER_ORE_ITEM) || stack.isOf(ItemRegistry.SUPER_WOOL_ITEM)) {
            info.setReturnValue(SuperRenderLayers.getOreOverlay());
        }
    }
}
