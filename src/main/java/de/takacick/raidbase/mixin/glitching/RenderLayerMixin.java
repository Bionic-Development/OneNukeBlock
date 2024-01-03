package de.takacick.raidbase.mixin.glitching;

import com.google.common.collect.ImmutableList;
import de.takacick.raidbase.client.shaders.RaidBaseLayers;
import net.minecraft.client.render.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = RenderLayer.class, priority = 999)
public abstract class RenderLayerMixin {

    @Inject(method = "getBlockLayers", at = @At("RETURN"), cancellable = true)
    private static void getBlockLayers(CallbackInfoReturnable<List<RenderLayer>> info) {
        List<RenderLayer> renderLayers = new ArrayList<>(info.getReturnValue());
        renderLayers.add(RaidBaseLayers.getSolid());

        info.setReturnValue(ImmutableList.copyOf(renderLayers));
    }

}