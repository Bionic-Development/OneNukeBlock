package de.takacick.onescaryblock.client.utils;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class LightningRenderer {

    public static void renderLightning(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int hexColor, long seed, float maxLength, float width) {
        renderLightning(matrixStack, vertexConsumerProvider, hexColor, seed, maxLength, width, 1, 1);
    }

    public static void renderLightning(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int hexColor, long seed, float maxLength, float width, int branches, float offset) {
        float[] fs = new float[8 * branches];
        float[] gs = new float[8 * branches];
        float h = 0.0f;
        float j = 0.0f;
        Random random = Random.create(seed);
        for (int k = 8 * branches; k > 0; --k) {
            fs[k - 1] = h;
            gs[k - 1] = j;
            h += (float) (random.nextInt(11) - 5);
            j += (float) (random.nextInt(11) - 5);
        }

        Vector3f color = Vec3d.unpackRgb(hexColor).toVector3f();

        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getLightning());
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        for (int l = 0; l < 4; ++l) {
            Random random2 = Random.create(seed);
            for (int m = 0; m < 1; ++m) {
                int n = 7 * branches;
                int o = 0;
                if (m > 0) {
                    n = 7 - m;
                }
                if (m > 0) {
                    o = n - 2;
                }
                float p = fs[n] - h;
                float q = gs[n] - j;
                for (int r = n; r >= o; --r) {
                    float s = p;
                    float t = q;
                    if (m == 0) {
                        p += (float) (random2.nextInt(11) - 5) * offset;
                        q += (float) (random2.nextInt(11) - 5) * offset;
                    } else {
                        p += (float) (random2.nextInt(31) - 15);
                        q += (float) (random2.nextInt(31) - 15);
                    }
                    float u = 0.5f;
                    float v = 0.45f;
                    float w = 0.45f;
                    float x = 0.5f;
                    float y = 0.1f + (float) l * 0.2f;
                    if (m == 0) {
                        y *= (float) r * 0.1f + 1.0f;
                    }
                    float z = 0.1f + (float) l * 0.2f;
                    if (m == 0) {
                        z *= ((float) r - 1.0f) * 0.1f + 1.0f;
                    }

                    drawBranch(matrix4f, vertexConsumer, maxLength, p, q, r, s, t, color.x(), color.y(), color.z(), y * width, z * width, false, false, true, false);
                    drawBranch(matrix4f, vertexConsumer, maxLength, p, q, r, s, t, color.x(), color.y(), color.z(), y * width, z * width, true, false, true, true);
                    drawBranch(matrix4f, vertexConsumer, maxLength, p, q, r, s, t, color.x(), color.y(), color.z(), y * width, z * width, true, true, false, true);
                    drawBranch(matrix4f, vertexConsumer, maxLength, p, q, r, s, t, color.x(), color.y(), color.z(), y * width, z * width, false, true, false, false);
                }
            }
        }
    }

    private static void drawBranch(Matrix4f matrix, VertexConsumer buffer, float maxLength, float x1, float z1, int y, float x2, float z2, float red, float green, float blue, float offset2, float offset1, boolean shiftEast1, boolean shiftSouth1, boolean shiftEast2, boolean shiftSouth2) {
        if (96.0f * maxLength < y * 16) {
            return;
        }

        buffer.vertex(matrix, x1 + (shiftEast1 ? offset1 : -offset1), Math.min((y * 16f), 96.0f * maxLength), z1 + (shiftSouth1 ? offset1 : -offset1)).color(red, green, blue, 0.3f).next();
        buffer.vertex(matrix, x2 + (shiftEast1 ? offset2 : -offset2), Math.min(((y + 1f) * 16f), 96.0f * maxLength), z2 + (shiftSouth1 ? offset2 : -offset2)).color(red, green, blue, 0.3f).next();
        buffer.vertex(matrix, x2 + (shiftEast2 ? offset2 : -offset2), Math.min(((y + 1f) * 16f), 96.0f * maxLength), z2 + (shiftSouth2 ? offset2 : -offset2)).color(red, green, blue, 0.3f).next();
        buffer.vertex(matrix, x1 + (shiftEast2 ? offset1 : -offset1), Math.min((y * 16f), 96.0f * maxLength), z1 + (shiftSouth2 ? offset1 : -offset1)).color(red, green, blue, 0.3f).next();
    }

}
