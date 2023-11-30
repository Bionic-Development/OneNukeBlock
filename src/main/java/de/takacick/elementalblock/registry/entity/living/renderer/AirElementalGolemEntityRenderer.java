package de.takacick.elementalblock.registry.entity.living.renderer;

import de.takacick.elementalblock.OneElementalBlock;
import de.takacick.elementalblock.registry.entity.living.model.AirElementalGolemEntityModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class AirElementalGolemEntityRenderer
        extends MobEntityRenderer<VexEntity, AirElementalGolemEntityModel> {
    private static final Identifier TEXTURE = new Identifier(OneElementalBlock.MOD_ID, "textures/entity/air_elemental_golem.png");

    public AirElementalGolemEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new AirElementalGolemEntityModel(AirElementalGolemEntityModel.getTexturedModelData().createModel()), 0.3f);
    }

    @Override
    protected int getBlockLight(VexEntity vexEntity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public Identifier getTexture(VexEntity vexEntity) {
        return TEXTURE;
    }
}

