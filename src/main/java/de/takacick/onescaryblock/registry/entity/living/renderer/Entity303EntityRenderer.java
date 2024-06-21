package de.takacick.onescaryblock.registry.entity.living.renderer;

import de.takacick.onescaryblock.OneScaryBlock;
import de.takacick.onescaryblock.registry.entity.living.Entity303Entity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class Entity303EntityRenderer extends MobEntityRenderer<Entity303Entity, PlayerEntityModel<Entity303Entity>> {
    private static final Identifier TEXTURE = new Identifier(OneScaryBlock.MOD_ID, "textures/entity/entity_303.png");

    public Entity303EntityRenderer(EntityRendererFactory.Context context) {
        super(context, new PlayerEntityModel<>(context.getModelLoader().getModelPart(EntityModelLayers.PLAYER), false), 0.5f);
        this.addFeature(new Entity303EyesFeatureRenderer<>(this));
    }

    @Override
    public void render(Entity303Entity entity303Entity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        entity303Entity.prevBodyYaw = entity303Entity.prevYaw;
        entity303Entity.setBodyYaw(entity303Entity.getYaw());
        entity303Entity.prevHeadYaw = entity303Entity.prevYaw;
        entity303Entity.setHeadYaw(entity303Entity.getYaw());

        super.render(entity303Entity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Identifier getTexture(Entity303Entity entity303Entity) {
        return TEXTURE;
    }

    @Override
    protected void scale(Entity303Entity entity303Entity, MatrixStack matrices, float amount) {
        matrices.scale(0.9375f, 0.9375f, 0.9375f);
    }

    @Environment(value = EnvType.CLIENT)
    public static class Entity303EyesFeatureRenderer<T extends Entity303Entity>
            extends EyesFeatureRenderer<T, PlayerEntityModel<T>> {
        private static final RenderLayer SKIN = RenderLayer.getEyes(new Identifier(OneScaryBlock.MOD_ID, "textures/entity/entity_303_eyes.png"));

        public Entity303EyesFeatureRenderer(FeatureRendererContext<T, PlayerEntityModel<T>> featureRendererContext) {
            super(featureRendererContext);
        }

        @Override
        public RenderLayer getEyesTexture() {
            return SKIN;
        }
    }
}
