package de.takacick.onescaryblock.client.utils;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import org.joml.Matrix4f;

public class GlowRenderer {

    private static final float HALF_SQRT_3 = 1f;
    private static final RenderLayer LIGHTNING = RenderLayer.of("lightning", VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.QUADS, 256, false, true, RenderLayer.MultiPhaseParameters.builder()
            .program(RenderLayer.LIGHTNING_PROGRAM).transparency(RenderLayer.TRANSLUCENT_TRANSPARENCY).cull(RenderLayer.ENABLE_CULLING)
            .build(true));

    public static void method_23157(VertexConsumer vertices, Matrix4f matrix, float[] rgb, int alpha) {
        vertices.vertex(matrix, 0.0f, 0.0f, 0.0f).color(rgb[0], rgb[1], rgb[2], alpha / 255f).next();
    }

    public static void method_23156(VertexConsumer vertices, Matrix4f matrix, float[] rgb, float y, float x) {
        vertices.vertex(matrix, -HALF_SQRT_3 * x, y, -0.5f * x).color(rgb[0], rgb[1], rgb[2], 0).next();
    }

    public static void method_23158(VertexConsumer vertices, Matrix4f matrix, float[] rgb, float y, float x) {
        vertices.vertex(matrix, HALF_SQRT_3 * x, y, -0.5f * x).color(rgb[0], rgb[1], rgb[2], 0).next();
    }

    public static void method_23159(VertexConsumer vertices, Matrix4f matrix, float y, float z) {
        vertices.vertex(matrix, 0.0f, y, z).color(255, 120, 120, 0).next();
    }

    public static RenderLayer getShader() {
        return LIGHTNING;
    }
}
