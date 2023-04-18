package de.takacick.stealbodyparts.registry.entity.custom.renderer;

import de.takacick.stealbodyparts.registry.entity.living.AliveMoldingBodyEntity;
import de.takacick.stealbodyparts.registry.entity.living.model.AliveMoldingBodyEntityModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.random.Random;

import java.util.List;

@Environment(value = EnvType.CLIENT)
public class LightningFeatureRenderer<T extends AliveMoldingBodyEntity, M extends AliveMoldingBodyEntityModel<T>>
        extends FeatureRenderer<T, M> {

    public LightningFeatureRenderer(LivingEntityRenderer<T, M> entityRenderer) {
        super(entityRenderer);
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
        if (livingEntity.lightningTicks <= 0) {
            return;
        }

        livingEntity.lightningTicks--;

        int m = this.getObjectCount();
        Random random = Random.create();
        if (m <= 0) {
            return;
        }

        List<ModelPart> list = this.getContextModel().getPart().traverse().toList();

        if (!list.isEmpty()) {
            for (int n = 0; n < m; ++n) {
                matrixStack.push();
                ModelPart modelPart = list.get(random.nextInt(list.size()));
                if (modelPart.isEmpty()) {
                    matrixStack.pop();
                    continue;
                }
                ModelPart.Cuboid cuboid = modelPart.getRandomCuboid(random);
                modelPart.rotate(matrixStack);
                float o = random.nextFloat();
                float p = random.nextFloat();
                float q = random.nextFloat();
                float r = MathHelper.lerp(o, cuboid.minX, cuboid.maxX) / 16.0f;
                float s = MathHelper.lerp(p, cuboid.minY, cuboid.maxY) / 16.0f;
                float t = MathHelper.lerp(q, cuboid.minZ, cuboid.maxZ) / 16.0f;
                matrixStack.translate(r, s, t);
                matrixStack.scale(0.0175f, 0.0175f, 0.0175f);
                matrixStack.multiply(Quaternion.fromEulerXyz((float) Random.create().nextGaussian(), (float) Random.create().nextGaussian(), (float) Random.create().nextGaussian()));
                MinecraftClient.getInstance().getEntityRenderDispatcher().render(new LightningEntity(EntityType.LIGHTNING_BOLT, MinecraftClient.getInstance().world), BlockPos.ORIGIN.getX(), BlockPos.ORIGIN.getY(), BlockPos.ORIGIN.getZ(), 0, 0, matrixStack, vertexConsumerProvider, i);
                matrixStack.pop();
            }
        }
    }

    protected int getObjectCount() {
        return 10;
    }
}
