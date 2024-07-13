package de.takacick.onegirlboyblock.client.utils;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class TurboBeamRenderer {

    public static final Identifier BEAM_TEXTURE = Identifier.of("textures/entity/beacon_beam.png");

    public static void renderBeam(MatrixStack matrices, VertexConsumerProvider vertexConsumers, float tickDelta, long worldTime, double yOffset, double maxY, float[] color, float radius, float alpha, float bottomAlpha) {
        renderBeam(matrices, vertexConsumers, BEAM_TEXTURE, tickDelta, 1.0F, worldTime, yOffset, maxY, color, radius, alpha, bottomAlpha);
    }

    private static void renderBeam(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Identifier textureId, float tickDelta, float heightScale, long worldTime, double yOffset, double maxY, float[] color, float outerRadius, float alpha, float bottomAlpha) {
        double i = yOffset + maxY;
        matrices.push();
        float f = (float) floorMod(worldTime, 40) + tickDelta;
        float g = maxY < 0 ? f : -f;
        float h = MathHelper.fractionalPart(g * 0.2F - (float) MathHelper.floor(g * 0.1F));
        float j = color[0];
        float k = color[1];
        float l = color[2];
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(0 * 2.25f - 45.0f));

        matrices.push();

        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(-90));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180));
        float y = 0.0F;
        float ab = 0.0F;
        float ac = -outerRadius;
        float r = 0.0F;
        float s = 0.0F;
        float t = -outerRadius;
        float ag = 0.0F;
        float ah = 1.0F;
        float ai = -1.0F + h;
        float aj = (float) maxY * heightScale * (0.5F / outerRadius) + ai;
        renderBeamLayer(matrices, vertexConsumers.getBuffer(RenderLayer.getBeaconBeam(textureId, alpha != 1f || bottomAlpha != 1f)), j, k, l, alpha, bottomAlpha, yOffset, i, 0.0F, outerRadius, outerRadius, 0.0F, ac, 0.0F, 0.0F, t, 0.0F, 1.0F, aj, ai);
        matrices.pop();
        matrices.pop();
    }

    private static void renderBeamLayer(MatrixStack matrices, VertexConsumer vertices, float red, float green, float blue, float alpha, float bottomAlpha, double yOffset, double height, float x1, float z1, float x2, float z2, float x3, float z3, float x4, float z4, float u1, float u2, float v1, float v2) {
        MatrixStack.Entry entry = matrices.peek();

        renderBeamFace(entry, vertices, red, green, blue, alpha, bottomAlpha, yOffset, height, x1, z1, x2, z2, u1, u2, v1, v2);
        renderBeamFace(entry, vertices, red, green, blue, alpha, bottomAlpha, yOffset, height, x4, z4, x3, z3, u1, u2, v1, v2);
        renderBeamFace(entry, vertices, red, green, blue, alpha, bottomAlpha, yOffset, height, x2, z2, x4, z4, u1, u2, v1, v2);
        renderBeamFace(entry, vertices, red, green, blue, alpha, bottomAlpha, yOffset, height, x3, z3, x1, z1, u1, u2, v1, v2);
    }

    private static void renderBeamFace(MatrixStack.Entry entry, VertexConsumer vertices, float red, float green, float blue, float alpha, float bottomAlpha, double yOffset, double height, float x1, float z1, float x2, float z2, float u1, float u2, float v1, float v2) {
        renderBeamVertex(entry, vertices, red, green, blue, bottomAlpha, height, x1, z1, u2, v1);
        renderBeamVertex(entry, vertices, red, green, blue, alpha, yOffset, x1, z1, u2, v2);
        renderBeamVertex(entry, vertices, red, green, blue, alpha, yOffset, x2, z2, u1, v2);
        renderBeamVertex(entry, vertices, red, green, blue, bottomAlpha, height, x2, z2, u1, v1);

        renderBeamVertex(entry, vertices, red, green, blue, bottomAlpha, height, x2, z2, u2, v1);
        renderBeamVertex(entry, vertices, red, green, blue, alpha, yOffset, x2, z2, u2, v2);
        renderBeamVertex(entry, vertices, red, green, blue, alpha, yOffset, x1, z1, u1, v2);
        renderBeamVertex(entry, vertices, red, green, blue, bottomAlpha, height, x1, z1, u1, v1);
    }

    private static void renderBeamVertex(MatrixStack.Entry entry, VertexConsumer vertices, float red, float green, float blue, float alpha, double y, float x, float z, float u, float v) {
        vertices.vertex(entry.getPositionMatrix(), x, (float) y, z).color(red, green, blue, alpha).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(15728880).normal(entry, 0.0F, 1.0F, 0.0F);
    }

    public static long floorMod(long x, long y) {
        long mod = x % y;

        if ((x ^ y) < 0 && mod != 0) {
            mod += y;
        }
        return mod;
    }

}
