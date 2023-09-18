package de.takacick.onesuperblock.client.render;

import de.takacick.superitems.client.CustomLayers;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.function.Function;

public class SuperRenderLayers {

    public static Shader SOLID_SHADER;
    private static final RenderPhase.Shader SOLID_RENDER_PHASE = new RenderPhase.Shader(() -> SOLID_SHADER);
    public static Shader RAINBOW_ORE_SHADER;
    private static final RenderPhase.Shader RAINBOW_ORE_RENDER_PHASE = new RenderPhase.Shader(() -> RAINBOW_ORE_SHADER);
    public static Shader SPOTLIGHT_SHADER;
    private static final RenderPhase.Shader SPOTLIGHT_RENDER_PHASE = new RenderPhase.Shader(() -> SPOTLIGHT_SHADER);
    public static Shader RAINBOW_ENTITY_SHADER;
    private static final RenderPhase.Shader RAINBOW_ENTITY_RENDER_PHASE = new RenderPhase.Shader(() -> RAINBOW_ENTITY_SHADER);

    public static final Function<Identifier, RenderLayer> RAINBOW_OVERLAY = Util.memoize((texture) -> {
        RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder()
                .shader(SOLID_RENDER_PHASE)
                .transparency(RenderLayer.ADDITIVE_TRANSPARENCY)
                .lightmap(RenderLayer.ENABLE_LIGHTMAP)
                .texture(RenderLayer.MIPMAP_BLOCK_ATLAS_TEXTURE)
                .build(false);
        return RenderLayer.of("rainbow_overlay", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL,
                VertexFormat.DrawMode.QUADS, 0x200000, true, false, multiPhaseParameters);
    });

    public static final RenderLayer RAINBOW_ORE = RenderLayer.of("rainbow_ore", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL,
            VertexFormat.DrawMode.QUADS, 0x200000, true, false,
            RenderLayer.MultiPhaseParameters.builder()
                    .shader(RAINBOW_ORE_RENDER_PHASE)
                    .transparency(RenderLayer.TRANSLUCENT_TRANSPARENCY)
                    .lightmap(RenderLayer.ENABLE_LIGHTMAP)
                    .texture(new RenderPhase.Texture(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, false, false))
                    .build(false));
    public static final Function<Identifier, RenderLayer> RAINBOW_BLOCK = Util.memoize((texture) -> {
        RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder()
                .shader(RAINBOW_ORE_RENDER_PHASE)
                .transparency(RenderLayer.TRANSLUCENT_TRANSPARENCY)
                .lightmap(RenderLayer.ENABLE_LIGHTMAP)
                .texture(new RenderPhase.Texture(texture, false, false))
                .build(false);
        return RenderLayer.of("rainbow_ore", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL,
                VertexFormat.DrawMode.QUADS, 0x200000, true, false, multiPhaseParameters);
    });
    public static final Function<Identifier, RenderLayer> RAINBOW_SPOTLIGHT = Util.memoize((texture) -> {
        RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder()
                .shader(SPOTLIGHT_RENDER_PHASE)
                .texture(new RenderPhase.Texture(texture, false, false))
                .transparency(RenderLayer.TRANSLUCENT_TRANSPARENCY)
                .cull(RenderLayer.DISABLE_CULLING)
                .writeMaskState(RenderLayer.COLOR_MASK)
                .build(false);
        return RenderLayer.of("rainbow_spotlight", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, false, true, multiPhaseParameters);
    });

    public static final Function<Identifier, RenderLayer> RAINBOW_ENTITY_GLOW = Util.memoize((texture) -> {
        RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder()
                .shader(RAINBOW_ENTITY_RENDER_PHASE)
                .texture(new RenderPhase.Texture(texture, false, false))
                .transparency(RenderLayer.ADDITIVE_TRANSPARENCY)
                .target(RenderLayer.ITEM_TARGET)
                .lightmap(RenderLayer.ENABLE_LIGHTMAP)
                .overlay(RenderLayer.ENABLE_OVERLAY_COLOR)
                .writeMaskState(RenderPhase.ALL_MASK)
                .build(false);
        return RenderLayer.of("rainbow_entity_glow", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL,
                VertexFormat.DrawMode.QUADS, 256, true, true, multiPhaseParameters);
    });
    public static final Function<Identifier, RenderLayer> RAINBOW_ENTITY = Util.memoize((texture) -> {
        RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder()
                .shader(RAINBOW_ENTITY_RENDER_PHASE)
                .texture(new RenderPhase.Texture(texture, false, false))
                .transparency(RenderLayer.TRANSLUCENT_TRANSPARENCY)
                .target(RenderLayer.ITEM_TARGET)
                .cull(RenderLayer.DISABLE_CULLING)
                .lightmap(RenderLayer.ENABLE_LIGHTMAP)
                .overlay(RenderLayer.ENABLE_OVERLAY_COLOR)
                .writeMaskState(RenderPhase.ALL_MASK)
                .build(false);
        return RenderLayer.of("rainbow_entity", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL,
                VertexFormat.DrawMode.QUADS, 256, true, true, multiPhaseParameters);
    });

    private static final RenderLayer BLOCK_OVERLAY = RAINBOW_OVERLAY.apply(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
    private static final RenderLayer ORE_BLOCK_OVERLAY = RAINBOW_ORE;

    public static RenderLayer getBlockOverlay() {
        return BLOCK_OVERLAY;
    }

    public static RenderLayer getOreOverlay() {
        return ORE_BLOCK_OVERLAY;
    }

}
