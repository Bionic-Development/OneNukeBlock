package de.takacick.secretcraftbase.registry.entity.custom.renderer;

import de.takacick.secretcraftbase.registry.entity.custom.AbstractSchematicEntity;
import de.takacick.secretcraftbase.server.datatracker.SchematicBox;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public abstract class AbstractSchematicEntityRenderer<T extends AbstractSchematicEntity> extends EntityRenderer<T> {

    public AbstractSchematicEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(T abstractSchematicEntity, float f, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        float size = abstractSchematicEntity.getSize(tickDelta);
        if (size > 0) {
            SchematicBox schematicBox = abstractSchematicEntity.getSchematicBox();

            Vec3d min = schematicBox.getMin();
            Vec3d max = schematicBox.getMax();
            Vec3d center = schematicBox.getCenter();

            min = new Vec3d(center.getX() - min.getX(), center.getY() - min.getY(), center.getZ() - min.getZ());
            max = new Vec3d(center.getX() - max.getX(), center.getY() - max.getY(), center.getZ() - max.getZ());

            double diffX = 0;
            if (max.getX() == (int) max.getX() || min.getX() == (int) min.getX()) {
                diffX = 0.5;
            }
            double diffZ = 0;
            if (max.getZ() == (int) max.getZ() || min.getZ() == (int) min.getZ()) {
                diffZ = 0.5;
            }

            min = min.multiply(size);
            max = max.multiply(size);

            OutlineVertexConsumerProvider outlineVertexConsumers = MinecraftClient.getInstance().getBufferBuilders().getOutlineVertexConsumers();
            VertexConsumer vertexConsumer = outlineVertexConsumers.getBuffer(RenderLayer.getLines());

            matrixStack.translate(-diffX, center.getY() - abstractSchematicEntity.getY() + 0.5, -diffZ);

            Vector3f vector3f = Vec3d.unpackRgb(abstractSchematicEntity.getColor()).toVector3f();

            WorldRenderer.drawBox(matrixStack, vertexConsumer,
                    min.getX(), min.getY(), min.getZ(),
                    max.getX(), max.getY(), max.getZ(),
                    vector3f.x(), vector3f.y(), vector3f.z(), 1.0f);
        }
        matrixStack.pop();
        super.render(abstractSchematicEntity, f, tickDelta, matrixStack, vertexConsumerProvider, i);
    }

}

