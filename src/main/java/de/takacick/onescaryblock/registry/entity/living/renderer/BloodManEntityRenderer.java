package de.takacick.onescaryblock.registry.entity.living.renderer;

import de.takacick.onescaryblock.registry.entity.living.BloodManEntity;
import de.takacick.onescaryblock.registry.entity.living.Entity303Entity;
import de.takacick.onescaryblock.registry.entity.living.feature.BloodManFeatureRenderer;
import de.takacick.onescaryblock.registry.entity.living.model.BloodManEntityModel;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.DrownedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.Nullable;

public class BloodManEntityRenderer extends MobEntityRenderer<BloodManEntity, BloodManEntityModel> {

    public BloodManEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new BloodManEntityModel(context.getModelLoader().getModelPart(EntityModelLayers.PLAYER), false), 0f);
        this.addFeature(new BloodManFeatureRenderer(this));
    }

    @Override
    protected void setupTransforms(BloodManEntity entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta) {
        super.setupTransforms(entity, matrices, animationProgress, bodyYaw, tickDelta);

        this.model.rightPants.visible = false;
        this.model.leftPants.visible = false;
        this.model.hat.visible = false;
        this.model.rightSleeve.visible = false;
        this.model.leftSleeve.visible = false;

        float i = entity.getLeaningPitch(tickDelta);
        if (i > 0.0f) {
            float j = -10.0f - entity.getPitch();
            float k = MathHelper.lerp(i, 0.0f, j);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(k), 0.0f, entity.getHeight() / 2.0f, 0.0f);
        }
    }

    @Nullable
    @Override
    public RenderLayer getRenderLayer(BloodManEntity bloodManEntity, boolean showBody, boolean translucent, boolean showOutline) {
        return null;
    }

    public Identifier getTexture(BloodManEntity bloodManEntity) {
        return BloodManFeatureRenderer.TEXTURE.getTextureId();
    }

    @Override
    protected void scale(BloodManEntity bloodManEntity, MatrixStack matrices, float amount) {
        matrices.scale(0.9375f, 0.9375f, 0.9375f);
    }
}
