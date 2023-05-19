package de.takacick.heartmoney.registry.entity.living.renderer;

import de.takacick.heartmoney.HeartMoney;
import de.takacick.heartmoney.registry.entity.living.HeartAngelEntity;
import de.takacick.heartmoney.registry.entity.living.model.HeartAngleEntityModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class HeartAngelEntityRenderer extends MobEntityRenderer<HeartAngelEntity, HeartAngleEntityModel> {
    private static final Identifier TEXTURE = new Identifier(HeartMoney.MOD_ID, "textures/entity/heart_angel.png");

    public HeartAngelEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new HeartAngleEntityModel(HeartAngleEntityModel.getTexturedModelData().createModel()), 0.4f);
        this.addFeature(new HeldItemFeatureRenderer<>(this, context.getHeldItemRenderer()));
    }

    @Override
    public Identifier getTexture(HeartAngelEntity heartAngelEntity) {
        return TEXTURE;
    }

    @Override
    protected int getBlockLight(HeartAngelEntity heartAngelEntity, BlockPos blockPos) {
        return 15;
    }
}

