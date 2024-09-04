package de.takacick.onenukeblock.registry.entity.living.renderer;

import de.takacick.onenukeblock.OneNukeBlock;
import de.takacick.onenukeblock.registry.entity.living.model.ProtoEntityModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.util.Identifier;

public class ProtoEntityRenderer extends MobEntityRenderer<WolfEntity, ProtoEntityModel<WolfEntity>> {
    private static final Identifier TEXTURE = Identifier.of(OneNukeBlock.MOD_ID, "textures/entity/proto.png");

    public ProtoEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new ProtoEntityModel<>(ProtoEntityModel.getTexturedModelData().createModel()), 0.5f);
    }

    @Override
    protected float getAnimationProgress(WolfEntity wolfEntity, float f) {
        return wolfEntity.getTailAngle();
    }

    @Override
    public Identifier getTexture(WolfEntity wolfEntity) {
        return TEXTURE;
    }
}

