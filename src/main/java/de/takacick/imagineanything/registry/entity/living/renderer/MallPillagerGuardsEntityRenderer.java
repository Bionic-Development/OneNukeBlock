package de.takacick.imagineanything.registry.entity.living.renderer;

import de.takacick.imagineanything.ImagineAnything;
import de.takacick.imagineanything.registry.entity.living.MallPillagerGuardsEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.IllagerEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.IllagerEntityModel;
import net.minecraft.util.Identifier;

public class MallPillagerGuardsEntityRenderer
        extends IllagerEntityRenderer<MallPillagerGuardsEntity> {
    private static final Identifier TEXTURE = new Identifier(ImagineAnything.MOD_ID, "textures/entity/mall_pillager_guards.png");

    public MallPillagerGuardsEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new IllagerEntityModel(context.getPart(EntityModelLayers.PILLAGER)), 0.5f);
        this.addFeature(new HeldItemFeatureRenderer<>(this, context.getHeldItemRenderer()));
    }

    @Override
    public Identifier getTexture(MallPillagerGuardsEntity mallPillagerGuardsEntity) {
        return TEXTURE;
    }
}

