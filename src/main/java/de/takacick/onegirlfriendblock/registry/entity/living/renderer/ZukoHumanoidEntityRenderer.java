package de.takacick.onegirlfriendblock.registry.entity.living.renderer;

import de.takacick.onegirlfriendblock.OneGirlfriendBlock;
import de.takacick.onegirlfriendblock.registry.entity.living.ZukoHumanoidEntity;
import de.takacick.onegirlfriendblock.registry.entity.living.model.ZukoHumanoidEntityModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityPose;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class ZukoHumanoidEntityRenderer
        extends MobEntityRenderer<ZukoHumanoidEntity, ZukoHumanoidEntityModel<ZukoHumanoidEntity>> {

    private static final Identifier TEXTURE = new Identifier(OneGirlfriendBlock.MOD_ID, "textures/entity/zuko_humanoid.png");

    public ZukoHumanoidEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new ZukoHumanoidEntityModel<>(ZukoHumanoidEntityModel.getTexturedModelData().createModel()), 0.9f);
    }

    @Override
    public Identifier getTexture(ZukoHumanoidEntity zukoHumanoidEntity) {
        return TEXTURE;
    }

    @Override
    protected void setupTransforms(ZukoHumanoidEntity entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta) {
        if (this.isShaking(entity)) {
            bodyYaw += (float) (Math.cos((double) entity.age * 3.25) * Math.PI * (double) 0.4f);
        }
        if (!entity.isInPose(EntityPose.SLEEPING)) {
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f - bodyYaw));
        }
        if (entity.isUsingRiptide()) {
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0f - entity.getPitch()));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(((float) entity.age + tickDelta) * -75.0f));
        } else if (LivingEntityRenderer.shouldFlipUpsideDown(entity)) {
            matrices.translate(0.0f, entity.getHeight() + 0.1f, 0.0f);
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0f));
        }
    }

    @Override
    protected void scale(ZukoHumanoidEntity zukoHumanoidEntity, MatrixStack matrixStack, float f) {
        float g = zukoHumanoidEntity.isDead() ? (zukoHumanoidEntity.deathTime + f) / 20f : 0f;
        float h = 1.0f + MathHelper.sin(g * 100.0f) * g * 0.01f;
        g = MathHelper.clamp(g, 0.0f, 1.0f);
        g *= g;
        g *= g;
        float i = (1.0f + g * 0.4f) * h;
        float j = (1.0f + g * 0.1f) / h;
        matrixStack.scale(i, j, i);
    }

    @Override
    protected float getAnimationCounter(ZukoHumanoidEntity zukoHumanoidEntity, float f) {
        float g = zukoHumanoidEntity.isDead() ? (zukoHumanoidEntity.deathTime + f) / 20f : 0f;
        if ((int) (g * 10.0f) % 2 == 0) {
            return 0.0f;
        }
        return MathHelper.clamp(g, 0.5f, 1.0f);
    }
}

