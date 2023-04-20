package de.takacick.stealbodyparts.registry.entity.custom.renderer;

import de.takacick.stealbodyparts.StealBodyParts;
import de.takacick.stealbodyparts.access.PlayerEntityModelProperties;
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

public class CarvedHeartFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>>
        extends FeatureRenderer<T, M> {

    private static final Identifier TEXTURE = new Identifier(StealBodyParts.MOD_ID, "textures/entity/carved_heart.png");

    public CarvedHeartFeatureRenderer(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
        if (getContextModel() instanceof PlayerEntityModelProperties playerModelProperties) {
            if (playerModelProperties.getBodyModel().inside.visible) {
                boolean bl = isVisible(livingEntity);
                boolean bl2 = !bl && !livingEntity.isInvisibleTo(MinecraftClient.getInstance().player);

                if (bl || bl2) {
                    VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutout(TEXTURE));
                    playerModelProperties.getBodyModel().getInside().render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
                }
            }
        }
    }

    protected boolean isVisible(T entity) {
        return !entity.isInvisible();
    }
}

