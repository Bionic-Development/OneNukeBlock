package de.takacick.everythinghearts.registry.entity.custom.renderer;

import de.takacick.everythinghearts.EverythingHearts;
import de.takacick.everythinghearts.access.PlayerEntityModelProperties;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class HeartHandsFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>>
        extends FeatureRenderer<T, M> {

    public static final Identifier TEXTURE = new Identifier(EverythingHearts.MOD_ID, "textures/entity/heart.png");

    public HeartHandsFeatureRenderer(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
        if (getContextModel() instanceof PlayerEntityModelProperties playerModelProperties) {
            if (playerModelProperties.getHeartHandsModel().getRoot().visible) {
                boolean bl = isVisible(livingEntity);
                boolean bl2 = !bl && !livingEntity.isInvisibleTo(MinecraftClient.getInstance().player);

                if (bl || bl2) {
                    matrixStack.push();
                    VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutout(TEXTURE));
                    playerModelProperties.getHeartHandsModel().getRoot().render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
                    matrixStack.pop();
                }
            }
        }
    }

    protected boolean isVisible(T entity) {
        return !entity.isInvisible();
    }
}

