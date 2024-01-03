package de.takacick.raidbase.mixin.glitching;

import de.takacick.raidbase.client.shaders.RaidBaseLayers;
import de.takacick.raidbase.registry.ItemRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderLayers.class)
public abstract class RenderLayersMixin {

    @Inject(method = "getItemLayer", at = @At("HEAD"), cancellable = true)
    private static void getItemLayer(ItemStack stack, boolean direct, CallbackInfoReturnable<RenderLayer> info) {
        if (stack.isOf(ItemRegistry.GLITCHY_QUICKSAND_ITEM)) {
            info.setReturnValue(RaidBaseLayers.getGlitchyItemEntityTranslucentCull());
        }
    }
}