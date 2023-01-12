package de.takacick.immortalmobs.registry.entity.projectiles.renderer;

import de.takacick.immortalmobs.ImmortalMobs;
import de.takacick.immortalmobs.client.CustomLayers;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

import java.util.Random;

public class ImmortalArrowEntityRenderer<T extends PersistentProjectileEntity> extends EntityRenderer<T> {
    public static final Identifier TEXTURE = new Identifier(ImmortalMobs.MOD_ID, "textures/entity/immortal_arrow.png");
    private static final float HALF_SQRT_3 = (float) 1f;
    private static final RenderLayer LIGHTNING = RenderLayer.of("lightning", VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.QUADS, 256, false, true, RenderLayer.MultiPhaseParameters.builder()
            .shader(RenderLayer.LIGHTNING_SHADER).transparency(RenderLayer.TRANSLUCENT_TRANSPARENCY).cull(RenderLayer.ENABLE_CULLING)
            .build(true));

    public ImmortalArrowEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(T persistentProjectileEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.lerp(g, persistentProjectileEntity.prevYaw, persistentProjectileEntity.getYaw()) - 90.0f));
        matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.lerp(g, persistentProjectileEntity.prevPitch, persistentProjectileEntity.getPitch())));
        float s = (float) persistentProjectileEntity.shake - g;
        if (s > 0.0f) {
            float t = -MathHelper.sin(s * 3.0f) * s;
            matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(t));
        }
        matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(45.0f));
        matrixStack.scale(0.05625f, 0.05625f, 0.05625f);
        matrixStack.translate(-4.0, 0.0, 0.0);
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(CustomLayers.IMMORTAL_CUTOUT.apply(this.getTexture(persistentProjectileEntity)));
        MatrixStack.Entry entry = matrixStack.peek();
        Matrix4f matrix4f = entry.getPositionMatrix();
        Matrix3f matrix3f = entry.getNormalMatrix();
        this.vertex(matrix4f, matrix3f, vertexConsumer, -7, -2, -2, 0.0f, 0.15625f, -1, 0, 0, i);
        this.vertex(matrix4f, matrix3f, vertexConsumer, -7, -2, 2, 0.15625f, 0.15625f, -1, 0, 0, i);
        this.vertex(matrix4f, matrix3f, vertexConsumer, -7, 2, 2, 0.15625f, 0.3125f, -1, 0, 0, i);
        this.vertex(matrix4f, matrix3f, vertexConsumer, -7, 2, -2, 0.0f, 0.3125f, -1, 0, 0, i);
        this.vertex(matrix4f, matrix3f, vertexConsumer, -7, 2, -2, 0.0f, 0.15625f, 1, 0, 0, i);
        this.vertex(matrix4f, matrix3f, vertexConsumer, -7, 2, 2, 0.15625f, 0.15625f, 1, 0, 0, i);
        this.vertex(matrix4f, matrix3f, vertexConsumer, -7, -2, 2, 0.15625f, 0.3125f, 1, 0, 0, i);
        this.vertex(matrix4f, matrix3f, vertexConsumer, -7, -2, -2, 0.0f, 0.3125f, 1, 0, 0, i);
        for (int u = 0; u < 4; ++u) {
            matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90.0f));
            this.vertex(matrix4f, matrix3f, vertexConsumer, -8, -2, 0, 0.0f, 0.0f, 0, 1, 0, i);
            this.vertex(matrix4f, matrix3f, vertexConsumer, 8, -2, 0, 0.5f, 0.0f, 0, 1, 0, i);
            this.vertex(matrix4f, matrix3f, vertexConsumer, 8, 2, 0, 0.5f, 0.15625f, 0, 1, 0, i);
            this.vertex(matrix4f, matrix3f, vertexConsumer, -8, 2, 0, 0.0f, 0.15625f, 0, 1, 0, i);
        }

        vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEyes(this.getTexture(persistentProjectileEntity)));
        for (int x = 0; x < 2; x++) {
            this.vertex(matrix4f, matrix3f, vertexConsumer, -7.05f, -2.2f, -2.2f, 0.0f, 0.15625f, -1, 0, 0, i);
            this.vertex(matrix4f, matrix3f, vertexConsumer, -7.05f, -2.2f, 2.2f, 0.15625f, 0.15625f, -1, 0, 0, i);
            this.vertex(matrix4f, matrix3f, vertexConsumer, -7.05f, 2.2f, 2.2f, 0.15625f, 0.3125f, -1, 0, 0, i);
            this.vertex(matrix4f, matrix3f, vertexConsumer, -7.05f, 2.2f, -2.2f, 0.0f, 0.3125f, -1, 0, 0, i);
            this.vertex(matrix4f, matrix3f, vertexConsumer, -7.05f, 2.2f, -2.2f, 0.0f, 0.15625f, 1, 0, 0, i);
            this.vertex(matrix4f, matrix3f, vertexConsumer, -7.05f, 2.2f, 2.2f, 0.15625f, 0.15625f, 1, 0, 0, i);
            this.vertex(matrix4f, matrix3f, vertexConsumer, -7.05f, -2.2f, 2.2f, 0.15625f, 0.3125f, 1, 0, 0, i);
            this.vertex(matrix4f, matrix3f, vertexConsumer, -7.05f, -2.2f, -2.2f, 0.0f, 0.3125f, 1, 0, 0, i);
            for (int u = 0; u < 4; ++u) {
                matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90.0f));
                this.vertex(matrix4f, matrix3f, vertexConsumer, -8.05f, -2.1f, 0.1f, 0.0f, 0.0f, 0, 1, 0, i);
                this.vertex(matrix4f, matrix3f, vertexConsumer, 8.05f, -2.1f, 0.1f, 0.5f, 0.0f, 0, 1, 0, i);
                this.vertex(matrix4f, matrix3f, vertexConsumer, 8.05f, 2.1f, 0.1f, 0.5f, 0.15625f, 0, 1, 0, i);
                this.vertex(matrix4f, matrix3f, vertexConsumer, -8.05f, 2.1f, 0.1f, 0.0f, 0.15625f, 0, 1, 0, i);
            }
        }
        matrixStack.pop();

        float consumer = ((float) persistentProjectileEntity.age + g) / 25f;
        float vertexConsumer2 = 0f;
        Random random = new Random(persistentProjectileEntity.getId());
        VertexConsumer vertexConsumer3 = vertexConsumerProvider.getBuffer(LIGHTNING);
        matrixStack.push();
        matrixStack.translate(0.0, 0, 0.0);
        int l = 0;
        while ((float) l < 4) {
            matrixStack.push();
            matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(random.nextFloat() * 360.0f));
            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(random.nextFloat() * 360.0f));
            matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(random.nextFloat() * 360.0f));
            matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(random.nextFloat() * 360.0f));
            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(random.nextFloat() * 360.0f));
            matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(random.nextFloat() * 360.0f + consumer * 90.0f));
            float m = 1;
            float n = 1;
            matrix4f = matrixStack.peek().getPositionMatrix();
            int o = (int) (255.0f * (1.0f - vertexConsumer2));

            method_23157(vertexConsumer3, matrix4f, getColor(), o);
            method_23156(vertexConsumer3, matrix4f, getColor(), m, n);
            method_23158(vertexConsumer3, matrix4f, getColor(), m, n);
            method_23157(vertexConsumer3, matrix4f, getColor(), o);
            method_23158(vertexConsumer3, matrix4f, getColor(), m, n);
            method_23159(vertexConsumer3, matrix4f, m, n);
            method_23157(vertexConsumer3, matrix4f, getColor(), o);
            method_23159(vertexConsumer3, matrix4f, m, n);
            method_23156(vertexConsumer3, matrix4f, getColor(), m, n);
            ++l;
            matrixStack.pop();
        }
        matrixStack.pop();


        super.render(persistentProjectileEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(T entity) {
        return TEXTURE;
    }

    public void vertex(Matrix4f positionMatrix, Matrix3f normalMatrix, VertexConsumer vertexConsumer, float x, float y, float z, float u, float v, int normalX, int normalZ, int normalY, int light) {
        vertexConsumer.vertex(positionMatrix, x, y, z).color(0.4029412f, 0.14705883f, 0.5411765f, 0.1f).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, normalX, normalY, normalZ).next();
    }

    private static void method_23157(VertexConsumer vertices, Matrix4f matrix, float[] rgb, int alpha) {
        vertices.vertex(matrix, 0.0f, 0.0f, 0.0f).color(rgb[0], rgb[1], rgb[2], alpha / 255f).next();
    }

    private static void method_23156(VertexConsumer vertices, Matrix4f matrix, float[] rgb, float y, float x) {
        vertices.vertex(matrix, -HALF_SQRT_3 * x, y, -0.5f * x).color(rgb[0], rgb[1], rgb[2], 0).next();
    }

    private static void method_23158(VertexConsumer vertices, Matrix4f matrix, float[] rgb, float y, float x) {
        vertices.vertex(matrix, HALF_SQRT_3 * x, y, -0.5f * x).color(rgb[0], rgb[1], rgb[2], 0).next();
    }

    private static void method_23159(VertexConsumer vertices, Matrix4f matrix, float y, float z) {
        vertices.vertex(matrix, 0.0f, y, z).color(120, 120, 120, 0).next();
    }

    public float[] getColor() {
        Vec3f vec3f = new Vec3f(0.4029412f, 0.14705883f, 0.5411765f);
        return new float[]{vec3f.getX(), vec3f.getY(), vec3f.getZ()};
    }
}

