package de.takacick.secretgirlbase.client.renderer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;

public class ShadowRenderer {

    public static void renderShadow(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Entity entity, float opacity, float radius) {
        float f = radius;
        if (entity instanceof MobEntity && ((MobEntity) entity).isBaby()) {
            f *= 0.5f;
        }
        double d = 0;
        double e = 0;
        double g = 0;

        MatrixStack.Entry entry = matrices.peek();
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(EntityRenderDispatcher.SHADOW_LAYER);
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        float r = opacity - (float) (e - (double) mutable.getY()) * 0.5f;
        renderShadowPart(entry, vertexConsumer, mutable, d, e, g, f, r);
    }

    private static void renderShadowPart(MatrixStack.Entry entry, VertexConsumer vertices, BlockPos pos, double x, double y, double z, float radius, float opacity) {
        float f = 1.0F;
        float g = opacity * 0.5F * f;
        if (g >= 0.0F) {
            if (g > 1.0F) {
                g = 1.0F;
            }
            double d = (double) pos.getX() + -1;
            double e = (double) pos.getX() + 1;
            double h = (double) pos.getY() + 0;
            double i = (double) pos.getZ() + -1;
            double j = (double) pos.getZ() + 1;
            float k = (float) (d - x);
            float l = (float) (e - x);
            float m = (float) (h - y);
            float n = (float) (i - z);
            float o = (float) (j - z);
            float p = -k / 2.0F / radius + 0.5F;
            float q = -l / 2.0F / radius + 0.5F;
            float r = -n / 2.0F / radius + 0.5F;
            float s = -o / 2.0F / radius + 0.5F;
            EntityRenderDispatcher.drawShadowVertex(entry, vertices, g, k, m, n, p, r);
            EntityRenderDispatcher.drawShadowVertex(entry, vertices, g, k, m, o, p, s);
            EntityRenderDispatcher.drawShadowVertex(entry, vertices, g, l, m, o, q, s);
            EntityRenderDispatcher.drawShadowVertex(entry, vertices, g, l, m, n, q, r);
        }
    }

    public static <E extends Entity> void renderEntity(E entity, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        EntityRenderer<? super Entity> entityRenderer = MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(entity);

        entityRenderer.render(entity, 0, 0, matrices, vertexConsumers, light);

        if (mode.equals(ModelTransformationMode.GUI)) {
            renderShadow(matrices, vertexConsumers, entity, entityRenderer.shadowOpacity,
                    Math.min(entityRenderer.shadowRadius, 32.0f));
        }
    }

}
