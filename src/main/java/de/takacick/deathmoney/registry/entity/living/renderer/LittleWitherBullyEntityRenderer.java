package de.takacick.deathmoney.registry.entity.living.renderer;

import de.takacick.deathmoney.DeathMoney;
import de.takacick.deathmoney.registry.entity.living.LittleWitherBullyEntity;
import de.takacick.deathmoney.registry.entity.living.model.LittleWitherBullyEntityModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class LittleWitherBullyEntityRenderer extends MobEntityRenderer<LittleWitherBullyEntity, LittleWitherBullyEntityModel> {
    private static final Identifier TEXTURE = new Identifier(DeathMoney.MOD_ID, "textures/entity/little_wither_bully.png");

    public LittleWitherBullyEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new LittleWitherBullyEntityModel(LittleWitherBullyEntityModel.getTexturedModelData().createModel()), 0.4f);
    }

    @Override
    public Identifier getTexture(LittleWitherBullyEntity littleWitherBullyEntity) {
        return TEXTURE;
    }

    @Override
    protected int getBlockLight(LittleWitherBullyEntity littleWitherBullyEntity, BlockPos blockPos) {
        return 15;
    }
}

