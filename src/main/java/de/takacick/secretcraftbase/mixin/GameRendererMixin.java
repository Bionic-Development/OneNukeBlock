package de.takacick.secretcraftbase.mixin;

import com.mojang.datafixers.util.Pair;
import de.takacick.secretcraftbase.client.shaders.SecretCraftBaseShaders;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.resource.ResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Consumer;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow
    @Final
    private ResourceManager resourceManager;

    @Shadow
    @Final
    private Map<String, ShaderProgram> programs;

    @Inject(method = "loadPrograms", at = @At("TAIL"))
    private void loadPrograms(ResourceFactory factory, CallbackInfo info) {
        ArrayList<Pair<ShaderProgram, Consumer<ShaderProgram>>> list2 = new ArrayList<>();

        try {
            list2.add(Pair.of(new ShaderProgram(resourceManager, "secretcraftbase/rendertype_entity_nether_portal",
                    VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL), shader -> SecretCraftBaseShaders.renderTypeEntityNetherPortalProgram = shader));
            list2.add(Pair.of(new ShaderProgram(resourceManager, "secretcraftbase/rendertype_xp",
                    VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL), shader -> SecretCraftBaseShaders.renderTypeXPProgram = shader));
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        list2.forEach(pair -> {
            ShaderProgram shader = pair.getFirst();
            this.programs.put(shader.getName(), shader);
            pair.getSecond().accept(shader);
        });
    }
}