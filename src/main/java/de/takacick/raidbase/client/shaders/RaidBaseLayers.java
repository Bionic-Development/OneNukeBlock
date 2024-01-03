package de.takacick.raidbase.client.shaders;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import de.takacick.raidbase.access.ShaderProgramProperties;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.function.BiFunction;
import java.util.function.Function;

public class RaidBaseLayers {

    public static final RenderPhase.ShaderProgram SOLID_PROGRAM = new RenderPhase.ShaderProgram(RaidBaseShaders::getRenderTypeSolidProgram);
    public static final RenderPhase.ShaderProgram ENTITY_ITEM_TRANSLUCENT_CULL_PROGRAM = new RenderPhase.ShaderProgram(RaidBaseShaders::getRenderTypeItemEntityTranslucentCullProgram);
    public static final RenderPhase.ShaderProgram ENTITY_GLITCHY_ITEM_TRANSLUCENT_CULL_PROGRAM = new RenderPhase.ShaderProgram(RaidBaseShaders::getRenderTypeGlitchyItemEntityTranslucentCullProgram);
    public static final RenderPhase.ShaderProgram ENTITY_TRANSLUCENT_CULL_PROGRAM = new RenderPhase.ShaderProgram(RaidBaseShaders::getRenderTypeEntityTranslucentCullProgram);

    private static final Function<Float, RenderPhase.Transparency> TRANSLUCENT_TRANSPARENCY = (strength) ->
            new RenderPhase.Transparency("translucent_transparency", () -> {

                if (RenderSystem.getShader() instanceof ShaderProgramProperties shaderProgramProperties) {
                    shaderProgramProperties.setGlitchStrength(strength);
                }

                RenderSystem.enableBlend();
                RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA,
                        GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
            }, () -> {
                if (RenderSystem.getShader() instanceof ShaderProgramProperties shaderProgramProperties) {
                    shaderProgramProperties.setGlitchStrength(0f);
                }

                RenderSystem.disableBlend();
                RenderSystem.defaultBlendFunc();
            });

    private static final RenderLayer SOLID = RenderLayer.of("solid",
            VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL,
            VertexFormat.DrawMode.QUADS, 0x200000, true, false,
            RenderLayer.MultiPhaseParameters.builder()
                    .lightmap(RenderLayer.ENABLE_LIGHTMAP)
                    .program(SOLID_PROGRAM)
                    .texture(RenderLayer.BLOCK_ATLAS_TEXTURE)
                    .build(true)
    );

    private static final BiFunction<Identifier, Float, RenderLayer> ENTITY_TRANSLUCENT_CULL = (texture, strength) ->
            RenderLayer.of("glitchy_entity_translucent_cull", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                    VertexFormat.DrawMode.QUADS, 256, true, true,
                    RenderLayer.MultiPhaseParameters.builder()
                            .program(ENTITY_TRANSLUCENT_CULL_PROGRAM)
                            .texture(new RenderPhase.Texture(texture, false, false))
                            .transparency(TRANSLUCENT_TRANSPARENCY.apply(strength))
                            .lightmap(RenderLayer.ENABLE_LIGHTMAP)
                            .overlay(RenderLayer.ENABLE_OVERLAY_COLOR)
                            .build(true));

    private static final Function<Identifier, RenderLayer> ITEM_ENTITY_TRANSLUCENT_CULL = Util.memoize((texture) -> {
        RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder()
                .program(ENTITY_ITEM_TRANSLUCENT_CULL_PROGRAM)
                .texture(new RenderPhase.Texture(texture, false, false))
                .transparency(RenderLayer.TRANSLUCENT_TRANSPARENCY)
                .target(RenderLayer.ITEM_ENTITY_TARGET)
                .lightmap(RenderLayer.ENABLE_LIGHTMAP)
                .overlay(RenderLayer.ENABLE_OVERLAY_COLOR)
                .writeMaskState(RenderPhase.ALL_MASK)
                .build(true);
        return RenderLayer.of("glitching_item_translucent_cull", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, true, true, multiPhaseParameters);
    });

    private static final Function<Identifier, RenderLayer> GLITCHY_ITEM_ENTITY_TRANSLUCENT_CULL = Util.memoize((texture) -> {
        RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder()
                .program(ENTITY_GLITCHY_ITEM_TRANSLUCENT_CULL_PROGRAM)
                .texture(new RenderPhase.Texture(texture, false, false))
                .transparency(RenderLayer.TRANSLUCENT_TRANSPARENCY)
                .target(RenderLayer.ITEM_ENTITY_TARGET)
                .lightmap(RenderLayer.ENABLE_LIGHTMAP)
                .overlay(RenderLayer.ENABLE_OVERLAY_COLOR)
                .writeMaskState(RenderPhase.ALL_MASK)
                .build(true);
        return RenderLayer.of("glitchy_item_translucent_cull", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, true, true, multiPhaseParameters);
    });

    private static final RenderLayer TEXTURED_ENTITY_TRANSLUCENT_CULL = getEntityTranslucentCull(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, 0f);
    private static final RenderLayer TEXTURED_ITEM_ENTITY_TRANSLUCENT_CULL = getItemEntityTranslucentCull(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
    private static final RenderLayer TEXTURED_GLITCHY_ITEM_ENTITY_TRANSLUCENT_CULL = getGlitchyItemEntityTranslucentCull(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);

    public static RenderLayer getSolid() {
        return SOLID;
    }

    public static RenderLayer getEntityTranslucentCull() {
        return TEXTURED_ENTITY_TRANSLUCENT_CULL;
    }

    public static RenderLayer getItemEntityTranslucentCull() {
        return TEXTURED_ITEM_ENTITY_TRANSLUCENT_CULL;
    }

    public static RenderLayer getGlitchyItemEntityTranslucentCull() {
        return TEXTURED_GLITCHY_ITEM_ENTITY_TRANSLUCENT_CULL;
    }

    public static RenderLayer getEntityTranslucentCull(Identifier texture, float glitchStrength) {
        return ENTITY_TRANSLUCENT_CULL.apply(texture, glitchStrength);
    }

    public static RenderLayer getItemEntityTranslucentCull(Identifier texture) {
        return ITEM_ENTITY_TRANSLUCENT_CULL.apply(texture);
    }

    public static RenderLayer getGlitchyItemEntityTranslucentCull(Identifier texture) {
        return GLITCHY_ITEM_ENTITY_TRANSLUCENT_CULL.apply(texture);
    }
}
