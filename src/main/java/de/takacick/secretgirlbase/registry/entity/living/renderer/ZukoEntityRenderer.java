package de.takacick.secretgirlbase.registry.entity.living.renderer;

import de.takacick.secretgirlbase.SecretGirlBase;
import de.takacick.secretgirlbase.registry.entity.living.ZukoEntity;
import de.takacick.secretgirlbase.registry.entity.living.model.ZukoEntityModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

import java.util.List;

public class ZukoEntityRenderer extends MobEntityRenderer<ZukoEntity, ZukoEntityModel<ZukoEntity>> {

    private static final Identifier TEXTURE = new Identifier(SecretGirlBase.MOD_ID, "textures/entity/zuko.png");

    public ZukoEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new ZukoEntityModel<>(ZukoEntityModel.getTexturedModelData().createModel()), 0.4f);
        this.addFeature(new ZukoCollarFeatureRenderer(this));
    }

    @Override
    public Identifier getTexture(ZukoEntity zukoEntity) {
        return TEXTURE;
    }

    @Override
    protected void scale(ZukoEntity zukoEntity, MatrixStack matrixStack, float f) {
        super.scale(zukoEntity, matrixStack, f);
        matrixStack.scale(0.8f, 0.8f, 0.8f);
    }

    @Override
    protected void setupTransforms(ZukoEntity zukoEntity, MatrixStack matrixStack, float f, float g, float h) {
        super.setupTransforms(zukoEntity, matrixStack, f, g, h);
        float i = zukoEntity.getSleepAnimation(h);
        if (i > 0.0f) {
            matrixStack.translate(0.4f * i, 0.15f * i, 0.1f * i);
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerpAngleDegrees(i, 0.0f, 90.0f)));
            BlockPos blockPos = zukoEntity.getBlockPos();
            List<PlayerEntity> list = zukoEntity.getWorld().getNonSpectatingEntities(PlayerEntity.class, new Box(blockPos).expand(2.0, 2.0, 2.0));
            for (PlayerEntity playerEntity : list) {
                if (!playerEntity.isSleeping()) continue;
                matrixStack.translate(0.15f * i, 0.0f, 0.0f);
                break;
            }
        }
    }
}

