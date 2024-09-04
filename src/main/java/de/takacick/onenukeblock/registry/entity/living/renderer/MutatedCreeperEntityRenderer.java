package de.takacick.onenukeblock.registry.entity.living.renderer;

import de.takacick.onenukeblock.OneNukeBlock;
import de.takacick.onenukeblock.registry.entity.living.MutatedCreeperEntity;
import de.takacick.onenukeblock.registry.entity.living.model.MutatedCreeperEntityModel;
import net.minecraft.client.render.entity.CreeperEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class MutatedCreeperEntityRenderer
        extends MobEntityRenderer<MutatedCreeperEntity, MutatedCreeperEntityModel<MutatedCreeperEntity>> {

    private static final Identifier TEXTURE = Identifier.of(OneNukeBlock.MOD_ID, "textures/entity/mutated_creeper.png");

    public MutatedCreeperEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new MutatedCreeperEntityModel<>(MutatedCreeperEntityModel.getTexturedModelData().createModel()), 0.9f);
    }

    @Override
    protected void scale(MutatedCreeperEntity mutatedCreeperEntity, MatrixStack matrixStack, float f) {
        float g = mutatedCreeperEntity.getClientFuseTime(f);
        float h = 1.0f + MathHelper.sin(g * 100.0f) * g * 0.01f;
        g = MathHelper.clamp(g, 0.0f, 1.0f);
        g *= g;
        g *= g;
        float i = (1.0f + g * 0.4f) * h;
        float j = (1.0f + g * 0.1f) / h;
        matrixStack.scale(i, j, i);
    }

    @Override
    protected float getAnimationCounter(MutatedCreeperEntity mutatedCreeperEntity, float f) {
        float g = mutatedCreeperEntity.getClientFuseTime(f);
        if ((int)(g * 10.0f) % 2 == 0) {
            return 0.0f;
        }
        return MathHelper.clamp(g, 0.5f, 1.0f);
    }

    @Override
    public Identifier getTexture(MutatedCreeperEntity mutatedCreeperEntity) {
        return TEXTURE;
    }


}

