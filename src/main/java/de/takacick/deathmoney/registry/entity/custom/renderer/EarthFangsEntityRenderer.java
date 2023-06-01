package de.takacick.deathmoney.registry.entity.custom.renderer;

import de.takacick.deathmoney.DeathMoney;
import de.takacick.deathmoney.registry.entity.custom.EarthFangsEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EvokerFangsEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;

public class EarthFangsEntityRenderer extends EntityRenderer<EarthFangsEntity> {
    private static final Identifier TEXTURE = new Identifier(DeathMoney.MOD_ID, "textures/entity/earth_fangs.png");
    private final EvokerFangsEntityModel<EarthFangsEntity> model;

    public EarthFangsEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new EvokerFangsEntityModel<>(context.getPart(EntityModelLayers.EVOKER_FANGS));
    }

    @Override
    public void render(EarthFangsEntity earthFangsEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        float h = earthFangsEntity.getAnimationProgress(g);
        if (h == 0.0f) {
            return;
        }
        float j = 2.0f;
        if (h > 0.9f) {
            j *= (1.0f - h) / 0.1f;
        }
        matrixStack.push();
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90.0f - earthFangsEntity.getYaw()));
        matrixStack.scale(-j, -j, j);
        float k = 0.03125f;
        matrixStack.translate(0.0, -0.626, 0.0);
        matrixStack.scale(0.5f, 0.5f, 0.5f);
        this.model.setAngles(earthFangsEntity, h, 0.0f, 0.0f, earthFangsEntity.getYaw(), earthFangsEntity.getPitch());
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(this.model.getLayer(TEXTURE));
        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
        matrixStack.pop();
        super.render(earthFangsEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(EarthFangsEntity demoJawsEntity) {
        return TEXTURE;
    }
}

