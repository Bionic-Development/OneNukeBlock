package de.takacick.upgradebody.registry.entity.living.renderer;

import de.takacick.upgradebody.UpgradeBody;
import de.takacick.upgradebody.registry.entity.living.AllSeeingWardenEntity;
import de.takacick.upgradebody.registry.entity.living.model.AllSeeingWardenEntityModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class AllSeeingWardenEntityRenderer
        extends MobEntityRenderer<AllSeeingWardenEntity, AllSeeingWardenEntityModel<AllSeeingWardenEntity>> {
    private static final Identifier TEXTURE = new Identifier(UpgradeBody.MOD_ID, "textures/entity/all_seeing_warden.png");

    public AllSeeingWardenEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new AllSeeingWardenEntityModel<>(AllSeeingWardenEntityModel.getTexturedModelData().createModel()), 0.9f);
    }

    @Override
    public Identifier getTexture(AllSeeingWardenEntity allSeeingWardenEntity) {
        return TEXTURE;
    }
}

