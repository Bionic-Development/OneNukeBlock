package de.takacick.onenukeblock.registry.entity.living.renderer;

import de.takacick.onenukeblock.OneNukeBlock;
import de.takacick.onenukeblock.registry.entity.living.CreeperScientistEntity;
import de.takacick.onenukeblock.registry.entity.living.model.CreeperScientistEntityModel;
import de.takacick.onenukeblock.registry.entity.living.renderer.feature.CreeperScientistChargeFeatureRenderer;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class CreeperScientistEntityRenderer
        extends MobEntityRenderer<CreeperScientistEntity, CreeperScientistEntityModel<CreeperScientistEntity>> {
    private static final Identifier TEXTURE = Identifier.of(OneNukeBlock.MOD_ID, "textures/entity/creeper_scientist.png");

    public CreeperScientistEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new CreeperScientistEntityModel<>(CreeperScientistEntityModel.getTexturedModelData(Dilation.NONE).createModel()), 0.5f);
        this.addFeature(new CreeperScientistChargeFeatureRenderer(this, context.getModelLoader()));
    }

    @Override
    protected void scale(CreeperScientistEntity creeperEntity, MatrixStack matrixStack, float f) {
        float g = creeperEntity.getClientFuseTime(f);
        float h = 1.0f + MathHelper.sin(g * 100.0f) * g * 0.01f;
        g = MathHelper.clamp(g, 0.0f, 1.0f);
        g *= g;
        g *= g;
        float i = (1.0f + g * 0.4f) * h;
        float j = (1.0f + g * 0.1f) / h;
        matrixStack.scale(i, j, i);
    }

    @Override
    protected float getAnimationCounter(CreeperScientistEntity creeperEntity, float f) {
        float g = creeperEntity.getClientFuseTime(f);
        if ((int) (g * 10.0f) % 2 == 0) {
            return 0.0f;
        }
        return MathHelper.clamp(g, 0.5f, 1.0f);
    }

    @Override
    public Identifier getTexture(CreeperScientistEntity creeperEntity) {
        return TEXTURE;
    }

}

