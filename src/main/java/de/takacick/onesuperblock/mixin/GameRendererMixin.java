package de.takacick.onesuperblock.mixin;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import de.takacick.onesuperblock.client.render.SuperRenderLayers;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Shader;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.resource.ResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Consumer;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow
    @Final
    private ResourceManager resourceManager;

    @Shadow
    @Final
    private Map<String, Shader> shaders;

    @Inject(method = "loadShaders", at = @At("TAIL"))
    private void loadShaders(ResourceManager manager, CallbackInfo info) {
        ArrayList<Pair<Shader, Consumer<Shader>>> list2 = Lists.newArrayListWithCapacity(this.shaders.size());

        try {
            list2.add(Pair.of(new Shader(resourceManager, "onesuperblock/rendertype_solid",
                    VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL), shader -> SuperRenderLayers.SOLID_SHADER = shader));
            list2.add(Pair.of(new Shader(resourceManager, "onesuperblock/rendertype_rainbow_spotlight",
                    VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL), shader -> SuperRenderLayers.SPOTLIGHT_SHADER = shader));
            list2.add(Pair.of(new Shader(resourceManager, "onesuperblock/rendertype_rainbow_ore",
                    VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL), shader -> SuperRenderLayers.RAINBOW_ORE_SHADER = shader));
            list2.add(Pair.of(new Shader(resourceManager, "onesuperblock/rendertype_rainbow_entity",
                    VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL), shader -> SuperRenderLayers.RAINBOW_ENTITY_SHADER = shader));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        list2.forEach(pair -> {
            Shader shader = pair.getFirst();
            this.shaders.put(shader.getName(), shader);
            pair.getSecond().accept(shader);
        });
    }
}
