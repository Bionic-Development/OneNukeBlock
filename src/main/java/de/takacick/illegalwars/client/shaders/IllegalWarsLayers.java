package de.takacick.illegalwars.client.shaders;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import de.takacick.illegalwars.access.ShaderProgramProperties;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

import java.util.function.BiFunction;
import java.util.function.Function;

public class IllegalWarsLayers {

    public static final RenderPhase.ShaderProgram ENTITY_TRANSLUCENT_CULL_PROGRAM = new RenderPhase.ShaderProgram(IllegalWarsShaders::getRenderTypeEntityTranslucentCullProgram);

    private static final Function<Float, RenderPhase.Transparency> TRANSLUCENT_TRANSPARENCY = (strength) ->
            new RenderPhase.Transparency("translucent_transparency", () -> {

                if (RenderSystem.getShader() instanceof ShaderProgramProperties shaderProgramProperties) {
                    shaderProgramProperties.setSludgeStrength(strength);
                }

                RenderSystem.enableBlend();
                RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA,
                        GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
            }, () -> {
                if (RenderSystem.getShader() instanceof ShaderProgramProperties shaderProgramProperties) {
                    shaderProgramProperties.setSludgeStrength(0f);
                }

                RenderSystem.disableBlend();
                RenderSystem.defaultBlendFunc();
            });

    private static final BiFunction<Identifier, Float, RenderLayer> ENTITY_TRANSLUCENT_CULL = (texture, strength) ->
            RenderLayer.of("sludge_entity_translucent_cull", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                    VertexFormat.DrawMode.QUADS, 256, true, true,
                    RenderLayer.MultiPhaseParameters.builder()
                            .program(ENTITY_TRANSLUCENT_CULL_PROGRAM)
                            .texture(new RenderPhase.Texture(texture, false, false))
                            .transparency(TRANSLUCENT_TRANSPARENCY.apply(strength))
                            .lightmap(RenderLayer.ENABLE_LIGHTMAP)
                            .overlay(RenderLayer.ENABLE_OVERLAY_COLOR)
                            .build(true));

    public static RenderLayer getEntityTranslucentCull(Identifier texture, float glitchStrength) {
        return ENTITY_TRANSLUCENT_CULL.apply(texture, glitchStrength);
    }

}
