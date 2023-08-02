package de.takacick.onedeathblock.registry.entity.living.renderer;

import de.takacick.onedeathblock.OneDeathBlock;
import de.takacick.onedeathblock.registry.entity.living.SkullagerEntity;
import de.takacick.onedeathblock.registry.entity.living.model.SkullagerEntityModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.util.Identifier;

public class SkullagerEntityRenderer<T extends SkullagerEntity> extends MobEntityRenderer<T, SkullagerEntityModel<T>> {
    private static final Identifier TEXTURE = new Identifier(OneDeathBlock.MOD_ID, "textures/entity/skullager.png");

    public SkullagerEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new SkullagerEntityModel<>(SkullagerEntityModel.getTexturedModelData().createModel()), 0.5f);
        this.addFeature(new HeldItemFeatureRenderer<>(this, context.getHeldItemRenderer()));
        this.addFeature(new HeadFeatureRenderer<>(this, context.getModelLoader(), context.getHeldItemRenderer()));
    }

    @Override
    public Identifier getTexture(SkullagerEntity skullagerEntity) {
        return TEXTURE;
    }
}

