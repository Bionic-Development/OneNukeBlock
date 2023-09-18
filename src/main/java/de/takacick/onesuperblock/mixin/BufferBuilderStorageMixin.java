package de.takacick.onesuperblock.mixin;

import de.takacick.onesuperblock.client.render.SuperRenderLayers;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BufferBuilderStorage.class)
public class BufferBuilderStorageMixin {

    @Inject(method = "assignBufferBuilder", at = @At("HEAD"))
    private static void assignBufferBuilder(Object2ObjectLinkedOpenHashMap<RenderLayer, BufferBuilder> builderStorage, RenderLayer layer, CallbackInfo info) {
        if (layer.equals(RenderLayer.getTranslucentNoCrumbling())) {
            builderStorage.put(SuperRenderLayers.getBlockOverlay(),
                    new BufferBuilder(SuperRenderLayers.getBlockOverlay().getExpectedBufferSize()));
            builderStorage.put(SuperRenderLayers.getOreOverlay(),
                    new BufferBuilder(SuperRenderLayers.getOreOverlay().getExpectedBufferSize()));
        }
    }
}
