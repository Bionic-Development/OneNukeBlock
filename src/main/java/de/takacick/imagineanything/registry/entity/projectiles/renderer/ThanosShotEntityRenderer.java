package de.takacick.imagineanything.registry.entity.projectiles.renderer;

import de.takacick.imagineanything.registry.entity.projectiles.ThanosShotEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

import java.util.Random;

@Environment(EnvType.CLIENT)
public class ThanosShotEntityRenderer extends EntityRenderer<ThanosShotEntity> {

    private static final float HALF_SQRT_3 = 1f;
    private static final RenderLayer LIGHTNING = RenderLayer.of("lightning", VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.QUADS, 256, false, true, RenderLayer.MultiPhaseParameters.builder()
            .shader(RenderLayer.LIGHTNING_SHADER).transparency(RenderLayer.TRANSLUCENT_TRANSPARENCY).cull(RenderLayer.ENABLE_CULLING)
            .build(true));

    public ThanosShotEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    public void render(ThanosShotEntity thanosShotEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.scale(2.0f, 2.0f, 2.0f);
        matrixStack.translate(0.0, 0.25, 0.0);

        float vertexConsumer = ((float) thanosShotEntity.age + g) / 200f;
        float vertexConsumer2 = 0f;
        Random random = new Random(thanosShotEntity.getId());
        VertexConsumer vertexConsumer3 = vertexConsumerProvider.getBuffer(LIGHTNING);
        matrixStack.push();
        matrixStack.translate(0.0, -0.25, 0.0);
        matrixStack.translate(0.0, thanosShotEntity.getHeight() / 2, 0.0);
        matrixStack.scale(0.25f, 0.25f, 0.25f);
        int o = 0;
        while ((float) o < 15) {
            matrixStack.push();
            matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(random.nextFloat() * 360.0f));
            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(random.nextFloat() * 360.0f));
            matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(random.nextFloat() * 360.0f));
            matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(random.nextFloat() * 360.0f));
            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(random.nextFloat() * 360.0f));
            matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(random.nextFloat() * 360.0f + vertexConsumer * 90.0f));
            float m = 2;
            float n = 2;
            Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
            int u = (int) (255.0f * (1.0f - vertexConsumer2));

            method_23157(vertexConsumer3, matrix4f, new float[]{1, 1, 1}, u);
            method_23156(vertexConsumer3, matrix4f, new float[]{1, 0, 1}, m, n);
            method_23158(vertexConsumer3, matrix4f, new float[]{1, 0, 1}, m, n);
            method_23157(vertexConsumer3, matrix4f, new float[]{1, 1, 1}, u);
            method_23158(vertexConsumer3, matrix4f, new float[]{1, 0, 1}, m, n);
            method_23159(vertexConsumer3, matrix4f, m, n);
            method_23157(vertexConsumer3, matrix4f, new float[]{1, 1, 1}, u);
            method_23159(vertexConsumer3, matrix4f, m, n);
            method_23156(vertexConsumer3, matrix4f, new float[]{1, 0, 1}, m, n);
            ++o;
            matrixStack.pop();
        }
        matrixStack.pop();
        matrixStack.pop();

        super.render(thanosShotEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Identifier getTexture(ThanosShotEntity chaosCrystalEntity) {
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
        vertices.vertex(matrix, 0.0f, y, 1.0f * z).color(120, 120, 120, 0).next();
    }
}
