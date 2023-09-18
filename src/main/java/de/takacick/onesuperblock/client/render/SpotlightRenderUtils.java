package de.takacick.onesuperblock.client.render;

import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;

public class SpotlightRenderUtils {

    public static void renderSpotlight(MatrixStack matrices, VertexConsumer vertices, float red, float green, float blue, int yOffset, float height, float x1, float z1, float x2, float z2, float x3, float z3, float x4, float z4, float u1, float u2, float v1, float v2) {
        MatrixStack.Entry entry = matrices.peek();
        Matrix4f matrix4f = entry.getPositionMatrix();
        Matrix3f matrix3f = entry.getNormalMatrix();
        renderFace(matrix4f, matrix3f, vertices, red, green, blue, yOffset, height, x1, z1, x2, z2, u1, u2, v1, v2);
        renderFace(matrix4f, matrix3f, vertices, red, green, blue, yOffset, height, x4, z4, x3, z3, u1, u2, v1, v2);
        renderFace(matrix4f, matrix3f, vertices, red, green, blue, yOffset, height, x2, z2, x4, z4, u1, u2, v1, v2);
        renderFace(matrix4f, matrix3f, vertices, red, green, blue, yOffset, height, x3, z3, x1, z1, u1, u2, v1, v2);
    }

    private static void renderFace(Matrix4f positionMatrix, Matrix3f normalMatrix, VertexConsumer vertices, float red, float green, float blue, int yOffset, float height, float x1, float z1, float x2, float z2, float u1, float u2, float v1, float v2) {
        renderVertex(positionMatrix, normalMatrix, vertices, red, green, blue, 0.0f, height, x1, z1, u2, v1);
        renderVertex(positionMatrix, normalMatrix, vertices, red, green, blue, 1.0f, yOffset, x1, z1, u2, v2);
        renderVertex(positionMatrix, normalMatrix, vertices, red, green, blue, 1.0f, yOffset, x2, z2, u1, v2);
        renderVertex(positionMatrix, normalMatrix, vertices, red, green, blue, 0.0f, height, x2, z2, u1, v1);
    }

    private static void renderVertex(Matrix4f positionMatrix, Matrix3f normalMatrix, VertexConsumer vertices, float red, float green, float blue, float alpha, float y, float x, float z, float u, float v) {
        vertices.vertex(positionMatrix, x, y, z).color(red, green, blue, alpha).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(normalMatrix, 0.0f, 1.0f, 0.0f).next();
    }

}
