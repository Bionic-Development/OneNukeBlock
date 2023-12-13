package de.takacick.upgradebody.registry.entity.projectiles.renderer;

import de.takacick.upgradebody.registry.entity.projectiles.EnergyBulletEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import org.joml.Matrix4f;

public class EnergyBulletEntityRenderer extends EntityRenderer<EnergyBulletEntity> {

    private static final float HALF_SQRT_3 = 1f;

    public EnergyBulletEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(EnergyBulletEntity energyBulletEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(45.0f));
        matrixStack.scale(0.55625f, 0.55625f, 0.55625f);

        float consumer = ((float) energyBulletEntity.age + g) / 25f;
        float vertexConsumer2 = 0f;
        Random random = Random.create(energyBulletEntity.getId());
        VertexConsumer vertexConsumer3 = vertexConsumerProvider.getBuffer(RenderLayer.getLightning());
        matrixStack.push();
        matrixStack.translate(0.0, 0, 0.0);
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees((energyBulletEntity.age + energyBulletEntity.getId()) * 5));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((energyBulletEntity.age + energyBulletEntity.getId()) * 5));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((energyBulletEntity.age + energyBulletEntity.getId()) * 5));

        int l = 0;
        while ((float) l < 4) {
            matrixStack.push();
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(random.nextFloat() * 360.0f));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(random.nextFloat() * 360.0f));
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(random.nextFloat() * 360.0f));
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(random.nextFloat() * 360.0f));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(random.nextFloat() * 360.0f));
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(random.nextFloat() * 360.0f + consumer * 90.0f));
            float m = 1;
            float n = 1;
            Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
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


        super.render(energyBulletEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(EnergyBulletEntity xpBulletEntity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
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
        vertices.vertex(matrix, 0.0f, y, z).color(1f, 0f, 0f, 0).next();
    }

    public float[] getColor() {
        return new float[]{1f, 0.1f, 0.1f};
    }
}

