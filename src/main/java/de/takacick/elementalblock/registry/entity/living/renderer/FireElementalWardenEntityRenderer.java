package de.takacick.elementalblock.registry.entity.living.renderer;

import de.takacick.elementalblock.OneElementalBlock;
import de.takacick.elementalblock.registry.entity.living.FireElementalWardenEntity;
import de.takacick.elementalblock.registry.entity.living.model.FireElementalWardenEntityModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class FireElementalWardenEntityRenderer
        extends MobEntityRenderer<FireElementalWardenEntity, FireElementalWardenEntityModel<FireElementalWardenEntity>> {
    private static final Identifier TEXTURE = new Identifier(OneElementalBlock.MOD_ID, "textures/entity/fire_elemental_warden.png");

    public FireElementalWardenEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new FireElementalWardenEntityModel<>(FireElementalWardenEntityModel.getTexturedModelData().createModel()), 0.9f);
    }

    @Override
    public Identifier getTexture(FireElementalWardenEntity fireElementalWardenEntity) {
        return TEXTURE;
    }
}

