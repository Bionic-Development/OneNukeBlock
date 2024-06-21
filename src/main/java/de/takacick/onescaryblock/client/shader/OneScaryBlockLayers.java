package de.takacick.onescaryblock.client.shader;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import de.takacick.onescaryblock.OneScaryBlock;
import de.takacick.onescaryblock.access.ShaderProgramProperties;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.function.BiFunction;
import java.util.function.Function;

public class OneScaryBlockLayers {

    private static final VertexConsumerProvider.Immediate TRANSFORM_VERTEX_CONSUMER = VertexConsumerProvider.immediate(new BufferBuilder(786432));

    private static final Function<Float, RenderPhase.Transparency> TRANSLUCENT_TRANSPARENCY = (progress) ->
            new RenderPhase.Transparency("translucent_transparency", () -> {

                if (RenderSystem.getShader() instanceof ShaderProgramProperties shaderProgramProperties) {
                    shaderProgramProperties.setBloodOverlayProgress(progress);
                }

                RenderSystem.enableBlend();
                RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA,
                        GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
            }, () -> {
                if (RenderSystem.getShader() instanceof ShaderProgramProperties shaderProgramProperties) {
                    shaderProgramProperties.setBloodOverlayProgress(0f);
                }
                RenderSystem.disableBlend();
                RenderSystem.defaultBlendFunc();
            });

    public static final Identifier BLOOD_OVERLAY = new Identifier(OneScaryBlock.MOD_ID, "textures/misc/blood_overlay.png");

    public static final RenderPhase.ShaderProgram TRANSFORM_ENTITY_PROGRAM = new RenderPhase.ShaderProgram(OneScaryBlockShaders::getRenderTypeEntityOverlay);
    public static final RenderPhase.ShaderProgram SOUL_FLAME_PROGRAM = new RenderPhase.ShaderProgram(OneScaryBlockShaders::getRenderTypeSoulFlame);

    private static final BiFunction<Identifier, Float, RenderLayer> BLOOD_ENTITY = (texture, alpha) -> {
        RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder().program(TRANSFORM_ENTITY_PROGRAM)
                .texture(AdvancedTextures.create()
                        .add(texture, false, false)
                        .add(BLOOD_OVERLAY, false, false)
                        .build())
                .transparency(TRANSLUCENT_TRANSPARENCY.apply(alpha))
                .lightmap(RenderLayer.ENABLE_LIGHTMAP)
                .cull(RenderPhase.DISABLE_CULLING)
                .overlay(RenderPhase.Overlay.ENABLE_OVERLAY_COLOR).build(false);
        return RenderLayer.of("blood_entity", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                VertexFormat.DrawMode.QUADS, 1536, true, true, multiPhaseParameters);
    };

    private static final BiFunction<Identifier, Boolean, RenderLayer> SOUL_FLAME = Util.memoize((texture, affectsOutline) -> {
        RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder()
                .program(SOUL_FLAME_PROGRAM)
                .texture(new RenderPhase.Texture(texture, false, false))
                .transparency(RenderLayer.ADDITIVE_TRANSPARENCY)
                .cull(RenderLayer.DISABLE_CULLING)
                .lightmap(RenderLayer.ENABLE_LIGHTMAP)
                .overlay(RenderLayer.ENABLE_OVERLAY_COLOR)
                .build(affectsOutline);
        return RenderLayer.of("soul_flame", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 1536, true, false, multiPhaseParameters);
    });

    public static RenderLayer getBloodEntity(Identifier texture, float alpha) {
        return BLOOD_ENTITY.apply(texture, alpha);
    }

    public static RenderLayer getSoulFlame(Identifier texture) {
        return SOUL_FLAME.apply(texture, false);
    }

    public static VertexConsumerProvider.Immediate getTransformVertexConsumer() {
        return TRANSFORM_VERTEX_CONSUMER;
    }


}
