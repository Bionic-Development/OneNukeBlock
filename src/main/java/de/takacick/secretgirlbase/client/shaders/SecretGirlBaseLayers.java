package de.takacick.secretgirlbase.client.shaders;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import de.takacick.secretgirlbase.access.ShaderProgramProperties;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

import java.util.function.BiFunction;
import java.util.function.Function;

public class SecretGirlBaseLayers {

    public static final RenderPhase.ShaderProgram BUBBLE_GUM_PROGRAM = new RenderPhase.ShaderProgram(SecretGirlBaseShaders::getRenderTypeBubbleGumProgram);
    public static final RenderPhase.ShaderProgram POPPY_TRANSLUCENT_PROGRAM = new RenderPhase.ShaderProgram(SecretGirlBaseShaders::getRenderTypePoppyTranslucentProgram);

    private static final Function<Float, RenderPhase.Transparency> TRANSLUCENT_TRANSPARENCY = (strength) ->
            new RenderPhase.Transparency("translucent_transparency", () -> {

                if (RenderSystem.getShader() instanceof ShaderProgramProperties shaderProgramProperties) {
                    shaderProgramProperties.setBubbleGumStrength(strength);
                }

                RenderSystem.enableBlend();
                RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA,
                        GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
            }, () -> {
                if (RenderSystem.getShader() instanceof ShaderProgramProperties shaderProgramProperties) {
                    shaderProgramProperties.setBubbleGumStrength(0f);
                }
                RenderSystem.disableBlend();
                RenderSystem.defaultBlendFunc();
            });

    private static final BiFunction<Identifier, Float, RenderLayer> BUBBLE_GUM = (texture, strength) ->
            RenderLayer.of("bubble_gum_entity_translucent_cull", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                    VertexFormat.DrawMode.QUADS, 256, true, true,
                    RenderLayer.MultiPhaseParameters.builder()
                            .program(BUBBLE_GUM_PROGRAM)
                            .texture(new RenderPhase.Texture(texture, false, false))
                            .transparency(TRANSLUCENT_TRANSPARENCY.apply(strength))
                            .lightmap(RenderLayer.ENABLE_LIGHTMAP)
                            .overlay(RenderLayer.ENABLE_OVERLAY_COLOR)
                            .build(true));

    private static final Function<Float, RenderLayer> POPPY_TRANSLUCENT = (strength) -> RenderLayer.of(
            "poppy_translucent",
            VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS,
            786432, true, true,
            RenderLayer.MultiPhaseParameters.builder()
                    .program(POPPY_TRANSLUCENT_PROGRAM)
                    .lightmap(RenderLayer.ENABLE_LIGHTMAP)
                    .texture(RenderLayer.MIPMAP_BLOCK_ATLAS_TEXTURE)
                    .transparency(TRANSLUCENT_TRANSPARENCY.apply(strength))
                    .build(true)
    );
    private static final RenderLayer POPPY_TRANSLUCENT_BLOCK = POPPY_TRANSLUCENT.apply(0f);

    public static RenderLayer getBubbleGum(Identifier texture, float bubbleGumStrength) {
        return BUBBLE_GUM.apply(texture, bubbleGumStrength);
    }

    public static RenderLayer getPoppyTranslucent() {
        return POPPY_TRANSLUCENT_BLOCK;
    }

    public static RenderLayer getPoppyTranslucent(float bubbleGumStrength) {
        return POPPY_TRANSLUCENT.apply(bubbleGumStrength);
    }

}
