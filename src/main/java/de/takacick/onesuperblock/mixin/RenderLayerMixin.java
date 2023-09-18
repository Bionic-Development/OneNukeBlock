package de.takacick.onesuperblock.mixin;

import com.google.common.collect.ImmutableList;
import de.takacick.onesuperblock.client.render.SuperRenderLayers;
import net.minecraft.client.render.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(RenderLayer.class)
public class RenderLayerMixin {

    @Inject(method = "getBlockLayers", at = @At("RETURN"), cancellable = true)
    private static void getBlockLayers(CallbackInfoReturnable<List<RenderLayer>> info) {
        List<RenderLayer> renderLayers = new ArrayList<>(info.getReturnValue());
        renderLayers.add(SuperRenderLayers.getBlockOverlay());
        renderLayers.add(SuperRenderLayers.getOreOverlay());

        info.setReturnValue(ImmutableList.copyOf(renderLayers));
    }

}
