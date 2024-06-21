package de.takacick.onescaryblock.client.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Range;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

public class CircleRenderer {

    private static void setupRender() {
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(GL11.GL_LEQUAL);
    }

    private static void endRender() {
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    public static void renderCylinder(MatrixStack matrices, float radius, float height, int hexColor, float alpha, @Range(from = 4, to = 360) int segments) {
        segments = MathHelper.clamp(segments, 4, 360);

        Matrix4f matrix = matrices.peek().getPositionMatrix();
        Vector3f color = Vec3d.unpackRgb(hexColor).toVector3f();

        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        RenderSystem.depthMask(false);
        for (int i = 0; i < segments; i++) {
            float angle1 = (float) Math.toRadians(i * 360.0 / segments);
            float angle2 = (float) Math.toRadians((i + 1) * 360.0 / segments);

            float x1Outer = (MathHelper.cos(angle1) * radius);
            float z1Outer = (MathHelper.sin(angle1) * radius);

            float x2Outer = (MathHelper.cos(angle2) * radius);
            float z2Outer = MathHelper.sin(angle2) * radius;

            bufferBuilder.vertex(matrix, x1Outer, height, z1Outer).color(color.x(), color.y(), color.z(), 0.0F).next();
            bufferBuilder.vertex(matrix, x2Outer, height, z2Outer).color(color.x(), color.y(), color.z(), 0.0F).next();

            bufferBuilder.vertex(matrix, x2Outer, 0.0F, z2Outer).color(color.x(), color.y(), color.z(), alpha).next();
            bufferBuilder.vertex(matrix, x1Outer, 0.0F, z1Outer).color(color.x(), color.y(), color.z(), alpha).next();

            bufferBuilder.vertex(matrix, x1Outer, height, z1Outer).color(color.x(), color.y(), color.z(), 0.0F).next();
            bufferBuilder.vertex(matrix, x2Outer, height, z2Outer).color(color.x(), color.y(), color.z(), 0.0F).next();
            bufferBuilder.vertex(matrix, x2Outer, 0.0F, z2Outer).color(color.x(), color.y(), color.z(), alpha).next();
            bufferBuilder.vertex(matrix, x1Outer, 0.0F, z1Outer).color(color.x(), color.y(), color.z(), alpha).next();

            bufferBuilder.vertex(matrix, x1Outer, height, z1Outer).color(color.x(), color.y(), color.z(), 0.0F).next();
            bufferBuilder.vertex(matrix, x1Outer, 0.0F, z1Outer).color(color.x(), color.y(), color.z(), alpha).next();
            bufferBuilder.vertex(matrix, x2Outer, 0.0F, z2Outer).color(color.x(), color.y(), color.z(), alpha).next();
            bufferBuilder.vertex(matrix, x2Outer, height, z2Outer).color(color.x(), color.y(), color.z(), 0.0F).next();
        }

        setupRender();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        RenderSystem.depthMask(true);
        endRender();
    }

}
