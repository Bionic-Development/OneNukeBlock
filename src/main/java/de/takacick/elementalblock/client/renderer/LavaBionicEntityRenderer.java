package de.takacick.elementalblock.client.renderer;

import de.takacick.elementalblock.OneElementalBlock;
import de.takacick.elementalblock.client.feature.AnimatedModelFeatureRenderer;
import de.takacick.elementalblock.client.model.LavaBionicEntityModel;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.util.Identifier;

public class LavaBionicEntityRenderer extends PlayerEntityRenderer {
    public static final Identifier TEXTURE = new Identifier(OneElementalBlock.MOD_ID, "textures/entity/lava_bionic.png");

    public LavaBionicEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, true);

        this.model = new LavaBionicEntityModel<>(LavaBionicEntityModel.getTexturedModelData().createModel(), true);
        this.addFeature(new AnimatedModelFeatureRenderer(this));
    }

    public float getAnimationCounter(AbstractClientPlayerEntity entity, float tickDelta) {
        return super.getAnimationCounter(entity, tickDelta);
    }
}
