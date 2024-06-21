package de.takacick.onescaryblock.registry.entity.living.renderer;

import de.takacick.onescaryblock.OneScaryBlock;
import de.takacick.onescaryblock.registry.entity.living.HerobrineEntity;
import de.takacick.onescaryblock.registry.entity.living.model.HerobrineEntityModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class HerobrineEntityRenderer extends MobEntityRenderer<HerobrineEntity, HerobrineEntityModel<HerobrineEntity>> {
    private static final Identifier TEXTURE = new Identifier(OneScaryBlock.MOD_ID, "textures/entity/herobrine.png");

    public HerobrineEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new HerobrineEntityModel<>(context.getModelLoader().getModelPart(EntityModelLayers.PLAYER), false), 0.5f);
        this.addFeature(new HerobrineEyesFeatureRenderer<>(this));
    }

    public Identifier getTexture(HerobrineEntity herobrineEntity) {
        return TEXTURE;
    }

    @Override
    protected void scale(HerobrineEntity herobrineEntity, MatrixStack matrices, float amount) {
        matrices.scale(0.9375f, 0.9375f, 0.9375f);
    }

    @Environment(value = EnvType.CLIENT)
    public static class HerobrineEyesFeatureRenderer<T extends HerobrineEntity>
            extends EyesFeatureRenderer<T, HerobrineEntityModel<T>> {
        private static final RenderLayer SKIN = RenderLayer.getEyes(new Identifier(OneScaryBlock.MOD_ID, "textures/entity/herobrine_eyes.png"));

        public HerobrineEyesFeatureRenderer(FeatureRendererContext<T, HerobrineEntityModel<T>> featureRendererContext) {
            super(featureRendererContext);
        }

        @Override
        public RenderLayer getEyesTexture() {
            return SKIN;
        }
    }
}
