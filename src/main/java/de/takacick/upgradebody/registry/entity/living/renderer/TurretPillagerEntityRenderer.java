package de.takacick.upgradebody.registry.entity.living.renderer;

import de.takacick.upgradebody.UpgradeBody;
import de.takacick.upgradebody.registry.entity.living.TurretPillagerEntity;
import de.takacick.upgradebody.registry.entity.living.model.TurretPillagerEntityModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class TurretPillagerEntityRenderer
        extends MobEntityRenderer<TurretPillagerEntity, TurretPillagerEntityModel<TurretPillagerEntity>> {
    private static final Identifier TEXTURE = new Identifier(UpgradeBody.MOD_ID, "textures/entity/turret_pillager.png");

    public TurretPillagerEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new TurretPillagerEntityModel<>(TurretPillagerEntityModel.getTexturedModelData().createModel()), 0.5f);

        this.addFeature(new HeldItemFeatureRenderer<>(this, context.getHeldItemRenderer()));
    }

    @Override
    protected void scale(TurretPillagerEntity turretPillagerEntity, MatrixStack matrixStack, float f) {
        matrixStack.scale(0.9375f, 0.9375f, 0.9375f);
    }

    @Override
    public Identifier getTexture(TurretPillagerEntity turretPillagerEntity) {
        return TEXTURE;
    }
}

