package de.takacick.emeraldmoney.registry.entity.living.renderer;

import de.takacick.emeraldmoney.EmeraldMoney;
import de.takacick.emeraldmoney.registry.entity.living.CreepagerEntity;
import de.takacick.emeraldmoney.registry.entity.living.model.CreepagerEntityModel;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class CreepagerEntityRenderer extends MobEntityRenderer<CreepagerEntity, CreepagerEntityModel<CreepagerEntity>> {

    private static final Identifier TEXTURE = new Identifier(EmeraldMoney.MOD_ID, "textures/entity/creepager.png");

    public CreepagerEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new CreepagerEntityModel<>(CreepagerEntityModel.getTexturedModelData(Dilation.NONE).createModel()), 0.5f);
    }

    @Override
    protected void scale(CreepagerEntity creepagerEntity, MatrixStack matrixStack, float f) {
        float g = creepagerEntity.getClientFuseTime(f);
        float h = 1.0f + MathHelper.sin(g * 100.0f) * g * 0.01f;
        g = MathHelper.clamp(g, 0.0f, 1.0f);
        g *= g;
        g *= g;
        float i = (1.0f + g * 0.4f) * h;
        float j = (1.0f + g * 0.1f) / h;
        matrixStack.scale(i, j, i);
    }

    @Override
    protected float getAnimationCounter(CreepagerEntity creepagerEntity, float f) {
        float g = creepagerEntity.getClientFuseTime(f);
        if ((int) (g * 10.0f) % 2 == 0) {
            return 0.0f;
        }
        return MathHelper.clamp(g, 0.5f, 1.0f);
    }

    @Override
    public Identifier getTexture(CreepagerEntity creepagerEntity) {
        return TEXTURE;
    }
}

