package de.takacick.onegirlboyblock.registry.entity.living.renderer;

import de.takacick.onegirlboyblock.OneGirlBoyBlock;
import de.takacick.onegirlboyblock.registry.entity.living.TurboBoardEntity;
import de.takacick.onegirlboyblock.registry.entity.living.renderer.feature.TurboBoardBeamFeatureRenderer;
import de.takacick.onegirlboyblock.registry.entity.living.model.TurboBoardEntityModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class TurboBoardEntityRenderer extends MobEntityRenderer<TurboBoardEntity, TurboBoardEntityModel<TurboBoardEntity>> {
    public static final Identifier TEXTURE = Identifier.of(OneGirlBoyBlock.MOD_ID, "textures/entity/turbo_board.png");

    public TurboBoardEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new TurboBoardEntityModel<>(TurboBoardEntityModel.getTexturedModelData().createModel()), 0f);
        this.addFeature(new TurboBoardBeamFeatureRenderer(this));
    }

    @Override
    public Identifier getTexture(TurboBoardEntity ufoEntity) {
        return TEXTURE;
    }

}

