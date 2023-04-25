package de.takacick.everythinghearts.registry.entity.projectiles.renderer;

import de.takacick.everythinghearts.registry.entity.projectiles.HeartmondEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

import java.util.Random;

public class HeartmondEntityRenderer extends EntityRenderer<HeartmondEntity> {

    private static final float HALF_SQRT_3 = 4f;

    public HeartmondEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(HeartmondEntity heartmondEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(45.0f));
        matrixStack.scale(0.25625f, 0.25625f, 0.25625f);

        float consumer = ((float) heartmondEntity.age + g) / 25f;
        float vertexConsumer2 = Math.min(consumer > 0.8f ? (consumer - 0.8f) / 0.2f : 0.0f, 1.0f);
        Random random = new Random(heartmondEntity.getId());
        VertexConsumer vertexConsumer3 = vertexConsumerProvider.getBuffer(RenderLayer.getLightning());
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
            Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
            int o = (int) (255.0f * (1.0f - vertexConsumer2));

            method_23157(vertexConsumer3, matrix4f, o);
            method_23156(vertexConsumer3, matrix4f, m, n);
            method_23158(vertexConsumer3, matrix4f, m, n);
            method_23157(vertexConsumer3, matrix4f, o);
            method_23158(vertexConsumer3, matrix4f, m, n);
            method_23159(vertexConsumer3, matrix4f, m, n);
            method_23157(vertexConsumer3, matrix4f, o);
            method_23159(vertexConsumer3, matrix4f, m, n);
            method_23156(vertexConsumer3, matrix4f, m, n);
            ++l;
            matrixStack.pop();
        }
        matrixStack.pop();


        super.render(heartmondEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(HeartmondEntity heartmondEntity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }

    private static void method_23157(VertexConsumer vertices, Matrix4f matrix, int alpha) {
        vertices.vertex(matrix, 0.0f, 0.0f, 0.0f).color(255, 19, 19, alpha).next();
    }

    private static void method_23156(VertexConsumer vertices, Matrix4f matrix, float y, float x) {
        vertices.vertex(matrix, -HALF_SQRT_3 * x, y, -0.5f * x).color(255, 19, 19, 0).next();
    }

    private static void method_23158(VertexConsumer vertices, Matrix4f matrix, float y, float x) {
        vertices.vertex(matrix, HALF_SQRT_3 * x, y, -0.5f * x).color(255, 19, 19, 0).next();
    }

    private static void method_23159(VertexConsumer vertices, Matrix4f matrix, float y, float z) {
        vertices.vertex(matrix, 0.0f, y, z).color(120, 120, 120, 0).next();
    }
}

