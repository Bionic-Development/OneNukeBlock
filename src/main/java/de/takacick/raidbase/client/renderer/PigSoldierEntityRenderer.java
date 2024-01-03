package de.takacick.raidbase.client.renderer;

import de.takacick.raidbase.client.model.PigSoldierEntityModel;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.util.Identifier;

public class PigSoldierEntityRenderer extends MobEntityRenderer<PigEntity, PigSoldierEntityModel<PigEntity>> {

    private static final Identifier TEXTURE = new Identifier("textures/entity/pig/pig.png");

    public PigSoldierEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new PigSoldierEntityModel<>(), 0.5f);
        this.addFeature(new PigSoldierFeatureEntityRenderer<>(this));
        this.addFeature(new HeldItemFeatureRenderer<>(this, context.getHeldItemRenderer()));
    }

    @Override
    public void render(PigEntity mobEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        this.model.helmet.visible = false;
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(PigEntity pigEntity) {
        return TEXTURE;
    }
}
