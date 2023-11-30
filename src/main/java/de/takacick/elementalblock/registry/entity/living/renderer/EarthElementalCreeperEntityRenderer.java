package de.takacick.elementalblock.registry.entity.living.renderer;

import de.takacick.elementalblock.OneElementalBlock;
import de.takacick.elementalblock.registry.entity.living.EarthElementalCreeperEntity;
import de.takacick.elementalblock.registry.entity.living.model.EarthElementalCreeperEntityModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class EarthElementalCreeperEntityRenderer extends MobEntityRenderer<EarthElementalCreeperEntity, EarthElementalCreeperEntityModel<EarthElementalCreeperEntity>> {

    private static final Identifier TEXTURE = new Identifier(OneElementalBlock.MOD_ID, "textures/entity/earth_elemental_creeper.png");

    public EarthElementalCreeperEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new EarthElementalCreeperEntityModel<>(EarthElementalCreeperEntityModel.getTexturedModelData().createModel()), 0.5f);
    }

    @Override
    protected void scale(EarthElementalCreeperEntity creeperEntity, MatrixStack matrixStack, float f) {
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
    protected float getAnimationCounter(EarthElementalCreeperEntity creeperEntity, float f) {
        float g = creeperEntity.getClientFuseTime(f);
        if ((int) (g * 10.0f) % 2 == 0) {
            return 0.0f;
        }
        return MathHelper.clamp(g, 0.5f, 1.0f);
    }

    @Override
    public Identifier getTexture(EarthElementalCreeperEntity creeperEntity) {
        return TEXTURE;
    }

}

