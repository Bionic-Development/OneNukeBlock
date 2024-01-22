package de.takacick.illegalwars.registry.entity.living.renderer;

import de.takacick.illegalwars.IllegalWars;
import de.takacick.illegalwars.registry.entity.living.KingRatEntity;
import de.takacick.illegalwars.registry.entity.living.model.KingRatEntityModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class KingRatEntityRenderer
        extends MobEntityRenderer<KingRatEntity, KingRatEntityModel<KingRatEntity>> {

    private static final Identifier TEXTURE = new Identifier(IllegalWars.MOD_ID, "textures/entity/king_rat.png");

    public KingRatEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new KingRatEntityModel<>(KingRatEntityModel.getTexturedModelData().createModel()), 0.9f * 0.4f);
    }

    @Override
    public Identifier getTexture(KingRatEntity kingRatEntity) {
        return TEXTURE;
    }

    @Override
    protected void scale(KingRatEntity entity, MatrixStack matrices, float amount) {
        matrices.scale(0.4f, 0.4f, 0.4f);

        super.scale(entity, matrices, amount);
    }
}

