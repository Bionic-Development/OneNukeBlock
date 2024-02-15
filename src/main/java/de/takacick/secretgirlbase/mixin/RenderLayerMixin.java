package de.takacick.secretgirlbase.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import de.takacick.secretgirlbase.client.shaders.SecretGirlBaseLayers;
import net.minecraft.client.render.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(RenderLayer.class)
public abstract class RenderLayerMixin {

    @ModifyReturnValue(method = "getBlockLayers", at = @At("RETURN"))
    private static List<RenderLayer> getBlockLayers(List<RenderLayer> original) {
        List<RenderLayer> renderLayers = new ArrayList<>(original);
        renderLayers.add(SecretGirlBaseLayers.getPoppyTranslucent());
        return renderLayers;
    }
}