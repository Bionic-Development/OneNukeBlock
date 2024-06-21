package de.takacick.onescaryblock.client.shader.vertex;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3i;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class UvShiftVertexConsumer implements VertexConsumer {

    private final VertexConsumer vertexConsumer;
    private float red = 1f;
    private float green = 1f;
    private float blue = 1f;
    private float xShift;
    private float yShift;

    public UvShiftVertexConsumer(VertexConsumer vertexConsumer, float xShift, float yShift) {
        this.vertexConsumer = vertexConsumer;
        this.xShift = xShift;
        this.yShift = yShift;
    }

    @Override
    public VertexConsumer vertex(double x, double y, double z) {
        this.vertexConsumer.vertex(x, y, z);

        return this;
    }

    @Override
    public VertexConsumer color(int red, int green, int blue, int alpha) {
        return this;
    }

    @Override
    public VertexConsumer texture(float u, float v) {
        this.vertexConsumer.texture(u + xShift, v + yShift);
        return this;
    }

    @Override
    public VertexConsumer overlay(int u, int v) {
        this.vertexConsumer.overlay(u, v);
        return this;
    }

    @Override
    public VertexConsumer light(int u, int v) {
        this.vertexConsumer.light(u, v);
        return this;
    }

    @Override
    public VertexConsumer normal(float x, float y, float z) {
        this.vertexConsumer.normal(x, y, z);
        return this;
    }

    @Override
    public void next() {
        vertexConsumer.next();
    }

    @Override
    public void fixedColor(int red, int green, int blue, int alpha) {
        vertexConsumer.fixedColor(red, green, blue, alpha);
    }

    @Override
    public void unfixColor() {
        vertexConsumer.unfixColor();
    }

    @Override
    public void quad(MatrixStack.Entry matrixEntry, BakedQuad quad, float red, float green, float blue, int light, int overlay) {
        VertexConsumer.super.quad(matrixEntry, quad, red, green, blue, light, overlay);
    }

    @Override
    public void quad(MatrixStack.Entry matrixEntry, BakedQuad quad, float[] brightnesses, float red, float green, float blue, int[] lights, int overlay, boolean useQuadColorData) {
        float[] fs = new float[]{brightnesses[0], brightnesses[1], brightnesses[2], brightnesses[3]};
        int[] is = new int[]{lights[0], lights[1], lights[2], lights[3]};
        int[] js = quad.getVertexData();
        Vec3i vec3i = quad.getFace().getVector();
        Matrix4f matrix4f = matrixEntry.getPositionMatrix();
        Vector3f vector3f = matrixEntry.getNormalMatrix().transform(new Vector3f(vec3i.getX(), vec3i.getY(), vec3i.getZ()));
        int i = 8;
        int j = js.length / 8;
        try (MemoryStack memoryStack = MemoryStack.stackPush();) {
            ByteBuffer byteBuffer = memoryStack.malloc(VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL.getVertexSizeByte());
            IntBuffer intBuffer = byteBuffer.asIntBuffer();
            for (int k = 0; k < j; ++k) {
                float q;
                float p;
                float o;
                float n;
                float m;
                intBuffer.clear();
                intBuffer.put(js, k * 8, 8);
                float f = byteBuffer.getFloat(0);
                float g = byteBuffer.getFloat(4);
                float h = byteBuffer.getFloat(8);
                if (useQuadColorData) {
                    float l = (float) (byteBuffer.get(12) & 0xFF) / 255.0f;
                    m = (float) (byteBuffer.get(13) & 0xFF) / 255.0f;
                    n = (float) (byteBuffer.get(14) & 0xFF) / 255.0f;
                    o = l * fs[k] * red;
                    p = m * fs[k] * green;
                    q = n * fs[k] * blue;
                } else {
                    o = fs[k] * red;
                    p = fs[k] * green;
                    q = fs[k] * blue;
                }
                int r = is[k];
                m = byteBuffer.getFloat(16);
                n = byteBuffer.getFloat(20);
                Vector4f vector4f = matrix4f.transform(new Vector4f(f, g, h, 1.0f));
                vertexConsumer.vertex(vector4f.x(), vector4f.y(), vector4f.z(), o, p, q, 1.0f, m + xShift, n + yShift, overlay, r, vector3f.x(), vector3f.y(), vector3f.z());
            }
        }
    }
}