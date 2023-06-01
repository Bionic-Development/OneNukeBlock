package de.takacick.deathmoney.registry.entity.living.renderer;

import de.takacick.deathmoney.DeathMoney;
import de.takacick.deathmoney.registry.entity.living.model.HungryTitanEntityModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class HungryTitanEntityRenderer extends MobEntityRenderer<WardenEntity, HungryTitanEntityModel<WardenEntity>> {

    private static final Identifier TEXTURE = new Identifier(DeathMoney.MOD_ID, "textures/entity/hungry_titan.png");

    public HungryTitanEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new HungryTitanEntityModel<>(HungryTitanEntityModel.getTexturedModelData().createModel()), 0.5f);
    }

    @Override
    protected void scale(WardenEntity wardenEntity, MatrixStack matrices, float amount) {
        matrices.scale(3.8f, 3.8f, 3.8f);
        super.scale(wardenEntity, matrices, amount);
    }

    @Override
    public Identifier getTexture(WardenEntity wardenEntity) {
        return TEXTURE;
    }
}

