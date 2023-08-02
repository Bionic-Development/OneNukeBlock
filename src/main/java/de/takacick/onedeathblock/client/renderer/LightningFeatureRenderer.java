package de.takacick.onedeathblock.client.renderer;

import de.takacick.onedeathblock.access.PlayerProperties;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.StuckObjectsFeatureRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.random.Random;

public class LightningFeatureRenderer<T extends LivingEntity, M extends PlayerEntityModel<T>>
        extends StuckObjectsFeatureRenderer<T, M> {
    private final EntityRenderDispatcher dispatcher;

    public LightningFeatureRenderer(EntityRendererFactory.Context context, LivingEntityRenderer<T, M> entityRenderer) {
        super(entityRenderer);
        this.dispatcher = context.getRenderDispatcher();
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
        if (livingEntity instanceof PlayerProperties playerProperties && playerProperties.getShockedTicks() > 0) {
            int m = this.getObjectCount(livingEntity);
            Random random = Random.create();
            if (m <= 0) {
                return;
            }
            for (int n = 0; n < m; ++n) {
                matrixStack.push();
                ModelPart modelPart = this.getContextModel().getRandomPart(random);
                ModelPart.Cuboid cuboid = modelPart.getRandomCuboid(random);
                modelPart.rotate(matrixStack);
                float o = random.nextFloat();
                float p = random.nextFloat();
                float q = random.nextFloat();
                float r = MathHelper.lerp(o, cuboid.minX, cuboid.maxX) / 16.0f;
                float s = MathHelper.lerp(p, cuboid.minY, cuboid.maxY) / 16.0f;
                float t = MathHelper.lerp(q, cuboid.minZ, cuboid.maxZ) / 16.0f;
                matrixStack.translate(r, s, t);
                o = -1.0f * (o * 2.0f - 1.0f);
                p = -1.0f * (p * 2.0f - 1.0f);
                q = -1.0f * (q * 2.0f - 1.0f);
                this.renderObject(matrixStack, vertexConsumerProvider, i, livingEntity, o, p, q, h);
                matrixStack.pop();
            }
        }
    }

    @Override
    protected int getObjectCount(T entity) {
        return entity.getRandom().nextBetween(5, 10);
    }

    @Override
    protected void renderObject(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Entity entity, float directionX, float directionY, float directionZ, float tickDelta) {
        Random random = Random.create();

        matrices.scale(0.00675f, 0.00675f, 0.00675f);
        matrices.multiply(Quaternion.fromEulerXyz((float) random.nextGaussian(), (float) random.nextGaussian(), (float) random.nextGaussian()));
        this.dispatcher.render(new LightningEntity(EntityType.LIGHTNING_BOLT, MinecraftClient.getInstance().world), BlockPos.ORIGIN.getX(), BlockPos.ORIGIN.getY(), BlockPos.ORIGIN.getZ(), 0, 0, matrices, vertexConsumers, light);
    }
}

