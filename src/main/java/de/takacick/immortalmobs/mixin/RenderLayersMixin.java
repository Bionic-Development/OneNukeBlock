package de.takacick.immortalmobs.mixin;

import de.takacick.immortalmobs.client.CustomLayers;
import de.takacick.immortalmobs.registry.item.ImmortalItem;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderLayers.class)
public class RenderLayersMixin {

    @Inject(method = "getItemLayer", at = @At("HEAD"), cancellable = true)
    private static void getItemLayer(ItemStack stack, boolean direct, CallbackInfoReturnable<RenderLayer> cir) {
        if (stack.getItem() instanceof ImmortalItem) {
            cir.setReturnValue(CustomLayers.IMMORTAL_CUTOUT.apply(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE));
        }
    }
}
