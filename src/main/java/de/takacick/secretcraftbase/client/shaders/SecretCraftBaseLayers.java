package de.takacick.secretcraftbase.client.shaders;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.function.BiFunction;
import java.util.function.Function;

public class SecretCraftBaseLayers {

    public static final RenderPhase.ShaderProgram ENTITY_NETHER_PORTAL_PROGRAM = new RenderPhase.ShaderProgram(SecretCraftBaseShaders::getRenderTypeEntityNetherPortalProgram);
    public static final RenderPhase.ShaderProgram XP_PROGRAM = new RenderPhase.ShaderProgram(SecretCraftBaseShaders::getRenderTypeXPProgram);

    private static final BiFunction<Identifier, Boolean, RenderLayer> ENTITY_NETHER_PORTAL = Util.memoize((texture, affectsOutline) -> {
        RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder()
                .program(ENTITY_NETHER_PORTAL_PROGRAM)
                .texture(new RenderPhase.Texture(texture, false, false))
                .transparency(RenderLayer.TRANSLUCENT_TRANSPARENCY).cull(RenderLayer.DISABLE_CULLING)
                .writeMaskState(RenderLayer.COLOR_MASK)
                .overlay(RenderLayer.ENABLE_OVERLAY_COLOR)
                .build(affectsOutline);
        return RenderLayer.of("entity_nether_portal", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 1536, true, true, multiPhaseParameters);
    });

    public static final Function<Identifier, RenderLayer> XP_CUTOUT = Util.memoize((texture) -> {
        RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder()
                .program(XP_PROGRAM)
                .texture(new RenderPhase.Texture(texture, false, false))
                .transparency(RenderLayer.TRANSLUCENT_TRANSPARENCY)
                .target(RenderLayer.ITEM_ENTITY_TARGET)
                .lightmap(RenderLayer.ENABLE_LIGHTMAP)
                .overlay(RenderLayer.ENABLE_OVERLAY_COLOR)
                .writeMaskState(RenderPhase.ALL_MASK)
                .build(true);
        return RenderLayer.of("xp_cutout", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                VertexFormat.DrawMode.QUADS, 256, true, true, multiPhaseParameters);
    });

    public static RenderLayer getXP(Identifier texture) {
        return XP_CUTOUT.apply(texture);
    }

    public static RenderLayer getEntityNetherPortal(Identifier texture) {
        return ENTITY_NETHER_PORTAL.apply(texture, false);
    }

    public static RenderLayer getEntityNetherPortal(Identifier texture, boolean affectOutline) {
        return ENTITY_NETHER_PORTAL.apply(texture, affectOutline);
    }
}
