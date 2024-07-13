package de.takacick.onegirlboyblock.client.shader;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.function.BiFunction;

public class OneGirlBoyBlockLayers {

    public static final RenderPhase.ShaderProgram BIT_CANNON_GLOW_PROGRAM = new RenderPhase.ShaderProgram(OneGirlBoyBlockShaders::getRenderTypeBitCannonGlow);

    private static final BiFunction<Identifier, Boolean, RenderLayer> BIT_CANNON_GLOW = Util.memoize((texture, affectsOutline) -> {
        RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder()
                .program(BIT_CANNON_GLOW_PROGRAM)
                .texture(new RenderPhase.Texture(texture, false, false))
                .transparency(RenderLayer.TRANSLUCENT_TRANSPARENCY)
                .cull(RenderLayer.DISABLE_CULLING)
                .lightmap(RenderLayer.ENABLE_LIGHTMAP)
                .overlay(RenderLayer.ENABLE_OVERLAY_COLOR)
                .build(affectsOutline);
        return RenderLayer.of("bit_cannon_glow", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 1536, true, false, multiPhaseParameters);
    });

    public static RenderLayer getBitCannonGlow(Identifier texture) {
        return BIT_CANNON_GLOW.apply(texture, false);
    }

}
