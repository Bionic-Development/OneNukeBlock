package de.takacick.secretgirlbase.mixin;

import de.takacick.secretgirlbase.client.shaders.SecretGirlBaseLayers;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BufferBuilderStorage.class)
public abstract class BufferBuilderStorageMixin {

    @Inject(method = "assignBufferBuilder", at = @At("HEAD"))
    private static void assignBufferBuilder(Object2ObjectLinkedOpenHashMap<RenderLayer, BufferBuilder> builderStorage, RenderLayer layer, CallbackInfo info) {
        if (layer.equals(TexturedRenderLayers.getShieldPatterns())) {
            builderStorage.putAndMoveToFirst(SecretGirlBaseLayers.getPoppyTranslucent(), new BufferBuilder(RenderLayer.getTranslucent()
                    .getExpectedBufferSize()));
        }
    }
}
