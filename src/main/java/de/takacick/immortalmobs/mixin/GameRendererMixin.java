package de.takacick.immortalmobs.mixin;

import com.mojang.datafixers.util.Pair;
import de.takacick.immortalmobs.client.CustomLayers;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Shader;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.resource.ResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow
    @Final
    private ResourceManager resourceManager;

    @Redirect(method = "loadShaders", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 1))
    <E> boolean loadShaders(List instance, E e) {
        try {
            ((ArrayList<Pair<Shader, Consumer<Shader>>>) instance).add(Pair.of(new Shader(resourceManager, "rendertype_immortal",
                    VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL), shader -> CustomLayers.IMMORTAL_SHADER = shader));
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return instance.add(e);
    }
}
