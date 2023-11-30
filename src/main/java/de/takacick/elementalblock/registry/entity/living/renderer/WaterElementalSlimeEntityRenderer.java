package de.takacick.elementalblock.registry.entity.living.renderer;

import de.takacick.elementalblock.OneElementalBlock;
import de.takacick.elementalblock.registry.entity.living.WaterElementalSlimeEntity;
import de.takacick.elementalblock.registry.entity.living.model.WaterElementalSlimeEntityModel;
import de.takacick.elementalblock.registry.entity.living.renderer.feature.WaterElementalSlimeOverlayFeatureRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class WaterElementalSlimeEntityRenderer
        extends MobEntityRenderer<WaterElementalSlimeEntity, WaterElementalSlimeEntityModel<WaterElementalSlimeEntity>> {
    private static final Identifier TEXTURE = new Identifier(OneElementalBlock.MOD_ID, "textures/entity/water_elemental_slime.png");

    public WaterElementalSlimeEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new WaterElementalSlimeEntityModel<>(WaterElementalSlimeEntityModel.getInnerTexturedModelData().createModel()), 0.25f);
        this.addFeature(new WaterElementalSlimeOverlayFeatureRenderer<>(this));
    }

    @Override
    public void render(WaterElementalSlimeEntity waterElementalSlimeEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        this.shadowRadius = 0.25f * (float) waterElementalSlimeEntity.getSize();
        super.render(waterElementalSlimeEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    protected void scale(WaterElementalSlimeEntity waterElementalSlimeEntity, MatrixStack matrixStack, float f) {
        matrixStack.scale(0.7499f, 0.7499f, 0.7499f);
        matrixStack.translate(0.0f, 0.001f, 0.0f);
        float h = waterElementalSlimeEntity.getSize();
        float i = MathHelper.lerp(f, waterElementalSlimeEntity.lastStretch, waterElementalSlimeEntity.stretch) / (h * 0.5f + 1.0f);
        float j = 1.0f / (i + 1.0f);
        matrixStack.scale(j * h, 1.0f / j * h, j * h);
    }

    @Override
    public Identifier getTexture(WaterElementalSlimeEntity waterElementalSlimeEntity) {
        return TEXTURE;
    }
}

