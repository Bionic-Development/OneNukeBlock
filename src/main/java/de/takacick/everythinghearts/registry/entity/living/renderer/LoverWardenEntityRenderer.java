package de.takacick.everythinghearts.registry.entity.living.renderer;

import de.takacick.everythinghearts.EverythingHearts;
import de.takacick.everythinghearts.registry.entity.living.LoverWardenEntity;
import de.takacick.everythinghearts.registry.entity.living.model.LoverWardenEntityModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

import java.util.Random;

@Environment(EnvType.CLIENT)
public class LoverWardenEntityRenderer extends MobEntityRenderer<LoverWardenEntity, LoverWardenEntityModel<LoverWardenEntity>> {

    private static final Identifier TEXTURE = new Identifier(EverythingHearts.MOD_ID, "textures/entity/lover_warden.png");
    private static final float HALF_SQRT_3 = (float) (Math.sqrt(3.0) / 2.0);

    public LoverWardenEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new LoverWardenEntityModel<>(LoverWardenEntityModel.getTexturedModelData().createModel()), 0.9f);
    }

    @Override
    public void render(LoverWardenEntity loverWardenEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {

        if (loverWardenEntity.isExploding()) {
            matrixStack.push();

            float vertexConsumer = ((float) (loverWardenEntity.fuseTicks) + g) / 240;
            float vertexConsumer2 = Math.min(vertexConsumer > 0.8f ? (vertexConsumer - 0.8f) / 0.2f : 0.0f, 1.0f);
            Random random = new Random(432L);
            VertexConsumer vertexConsumer3 = vertexConsumerProvider.getBuffer(RenderLayer.getLightning());
            matrixStack.push();
            matrixStack.translate(0.0, loverWardenEntity.getHeight() / 2, 0.0);
            int l = 0;
            while ((float) l < (vertexConsumer + vertexConsumer * vertexConsumer) / 2.0f * 60.0f) {
                matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(random.nextFloat() * 360.0f));
                matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(random.nextFloat() * 360.0f));
                matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(random.nextFloat() * 360.0f));
                matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(random.nextFloat() * 360.0f));
                matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(random.nextFloat() * 360.0f));
                matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(random.nextFloat() * 360.0f + vertexConsumer * 90.0f));
                float m = random.nextFloat() * 20.0f + 5.0f + vertexConsumer2 * 10.0f;
                float n = random.nextFloat() * 2.0f + 1.0f + vertexConsumer2 * 2.0f;
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
            }
            matrixStack.pop();
            matrixStack.pop();
        }

        super.render(loverWardenEntity, f, g, matrixStack, vertexConsumerProvider, i);
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
        vertices.vertex(matrix, 0.0f, y, 1.0f * z).color(120, 120, 120, 0).next();
    }

    @Override
    public Identifier getTexture(LoverWardenEntity loverWardenEntity) {
        return TEXTURE;
    }
}

