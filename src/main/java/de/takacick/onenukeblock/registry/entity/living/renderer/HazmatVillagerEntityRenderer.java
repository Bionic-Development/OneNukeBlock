package de.takacick.onenukeblock.registry.entity.living.renderer;

import de.takacick.onenukeblock.OneNukeBlock;
import de.takacick.onenukeblock.registry.entity.living.HazmatVillagerEntity;
import de.takacick.onenukeblock.registry.entity.living.model.HazmatVillagerEntityModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class HazmatVillagerEntityRenderer extends MobEntityRenderer<HazmatVillagerEntity, HazmatVillagerEntityModel<HazmatVillagerEntity>> {
    public static final Identifier TEXTURE = Identifier.of(OneNukeBlock.MOD_ID, "textures/entity/hazmat_villager.png");

    public HazmatVillagerEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new HazmatVillagerEntityModel<>(HazmatVillagerEntityModel.getTexturedModelData().createModel()), 0.5f);
    }

    @Override
    public Identifier getTexture(HazmatVillagerEntity hazmatVillagerEntity) {
        return TEXTURE;
    }

    @Override
    protected void scale(HazmatVillagerEntity hazmatVillagerEntity, MatrixStack matrixStack, float f) {
        float g = 0.9375f;
        if (hazmatVillagerEntity.isBaby()) {
            g *= 0.5f;
            this.shadowRadius = 0.25f;
        } else {
            this.shadowRadius = 0.5f;
        }
        matrixStack.scale(g, g, g);
    }
}

