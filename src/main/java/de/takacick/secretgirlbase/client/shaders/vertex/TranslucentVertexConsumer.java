package de.takacick.secretgirlbase.client.shaders.vertex;

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

public class TranslucentVertexConsumer
        implements VertexConsumer {
    private final VertexConsumer vertexConsumer;
    private float red = 1f;
    private float green = 1f;
    private float blue = 1f;
    private float alpha = 1f;

    public TranslucentVertexConsumer(VertexConsumer vertexConsumer) {
        this.vertexConsumer = vertexConsumer;
    }

    @Override
    public VertexConsumer vertex(double x, double y, double z) {
        this.vertexConsumer.vertex(x, y, z);

        return this;
    }

    @Override
    public VertexConsumer color(int red, int green, int blue, int alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;

        return this;
    }

    @Override
    public VertexConsumer texture(float u, float v) {
        this.vertexConsumer.texture(u, v);
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

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    @Override
    public void quad(MatrixStack.Entry matrixEntry, BakedQuad quad, float[] brightnesses, float red, float green, float blue, int[] lights, int overlay, boolean useQuadColorData) {
        float[] fs = new float[]{brightnesses[0], brightnesses[1], brightnesses[2], brightnesses[3]};
        int[] is = new int[]{lights[0], lights[1], lights[2], lights[3]};
        int[] js = quad.getVertexData();
        Vec3i vec3i = quad.getFace().getVector();
        Matrix4f matrix4f = matrixEntry.getPositionMatrix();
        Vector3f vector3f = matrixEntry.getNormalMatrix().transform(new Vector3f((float) vec3i.getX(), (float) vec3i.getY(), (float) vec3i.getZ()));
        int j = js.length / 8;
        MemoryStack memoryStack = MemoryStack.stackPush();

        try {
            ByteBuffer byteBuffer = memoryStack.malloc(VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL.getVertexSizeByte());
            IntBuffer intBuffer = byteBuffer.asIntBuffer();

            for (int k = 0; k < j; ++k) {
                intBuffer.clear();
                intBuffer.put(js, k * 8, 8);
                float f = byteBuffer.getFloat(0);
                float g = byteBuffer.getFloat(4);
                float h = byteBuffer.getFloat(8);
                float o;
                float p;
                float q;
                float m;
                float n;
                if (useQuadColorData) {
                    float l = (float) (byteBuffer.get(12) & 255) / 255.0F;
                    m = (float) (byteBuffer.get(13) & 255) / 255.0F;
                    n = (float) (byteBuffer.get(14) & 255) / 255.0F;
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
                Vector4f vector4f = matrix4f.transform(new Vector4f(f, g, h, 1.0F));

                this.vertexConsumer.vertex(vector4f.x(), vector4f.y(), vector4f.z(), o, p, q, this.alpha, m, n,
                        overlay, r, vector3f.x(), vector3f.y(), vector3f.z());
            }
        } catch (Throwable var33) {
            if (memoryStack != null) {
                try {
                    memoryStack.close();
                } catch (Throwable var32) {
                    var33.addSuppressed(var32);
                }
            }

            throw var33;
        }

        if (memoryStack != null) {
            memoryStack.close();
        }

    }
}