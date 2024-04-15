package de.takacick.onegirlfriendblock.registry.entity.living.renderer;

import de.takacick.onegirlfriendblock.OneGirlfriendBlock;
import de.takacick.onegirlfriendblock.client.renderer.GirlfriendGlowRenderer;
import de.takacick.onegirlfriendblock.registry.entity.living.GirlfriendEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class GirlfriendEntityRenderer extends MobEntityRenderer<GirlfriendEntity, PlayerEntityModel<GirlfriendEntity>> {
    private static final Identifier TEXTURE = new Identifier(OneGirlfriendBlock.MOD_ID, "textures/entity/girlfriend.png");

    public GirlfriendEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new PlayerEntityModel<>(context.getModelLoader().getModelPart(EntityModelLayers.PLAYER_SLIM), true), 0.3F);
        this.addFeature(new ArmorFeatureRenderer<>(this, new ArmorEntityModel<>(context.getPart(EntityModelLayers.PLAYER_SLIM_INNER_ARMOR)), new ArmorEntityModel<>(context.getPart(EntityModelLayers.PLAYER_SLIM_OUTER_ARMOR)), context.getModelManager()));
        this.addFeature(new HeldItemFeatureRenderer<>(this, context.getHeldItemRenderer()));
        this.addFeature(new StuckArrowsFeatureRenderer<>(context, this));
        this.addFeature(new HeadFeatureRenderer<>(this, context.getModelLoader(), context.getHeldItemRenderer()));
        this.addFeature(new ElytraFeatureRenderer<>(this, context.getModelLoader()));
        this.addFeature(new TridentRiptideFeatureRenderer<>(this, context.getModelLoader()));
    }

    @Override
    public void render(GirlfriendEntity girlfriendEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {

        setModelPose(girlfriendEntity);
        super.render(girlfriendEntity, f, g, matrixStack, vertexConsumerProvider, i);
        float progress = 1f - ((girlfriendEntity.getClientFuseTime(g) / (float) GirlfriendEntity.DEFAULT_FUSE));

        if (progress <= 0f) {
            return;
        }

        matrixStack.push();

        float l = progress * 0.9f;
        float m1 = Math.min(l > 0.8f ? (l - 0.8f) / 0.2f : 0.0f, 1.0f) * 0.5f;
        Random random = Random.create(5);
        VertexConsumer vertexConsumer3 = vertexConsumerProvider.getBuffer(GirlfriendGlowRenderer.getShader());

        float scale = progress * 0.2f;
        matrixStack.translate(0f, girlfriendEntity.getHeight() * 0.5f, 0f);
        matrixStack.scale(scale, scale, scale);

        Vector3f vector3f = Vec3d.unpackRgb(random.nextDouble() <= 0.5 ? 0xCE8FC9 : 0xC377BD).toVector3f();
        float[] color = new float[]{vector3f.x(), vector3f.y(), vector3f.z()};

        int d = 0;
        while ((float) d < (l + l * l) / 2.0f * 60.0f) {
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(random.nextFloat() * 360.0f));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(random.nextFloat() * 360.0f));
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(random.nextFloat() * 360.0f));
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(random.nextFloat() * 360.0f));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(random.nextFloat() * 360.0f));
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(random.nextFloat() * 360.0f + ((girlfriendEntity.age + d) / 200f) * 90.0f));
            float m = random.nextFloat() * 20.0f + 5.0f + m1 * 10.0f;
            float n = random.nextFloat() * 2.0f + 1.0f + m1 * 2.0f;
            Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
            int o = (int) (255.0f * (1.0f - m1));

            GirlfriendGlowRenderer.method_23157(vertexConsumer3, matrix4f, color, o);
            GirlfriendGlowRenderer.method_23156(vertexConsumer3, matrix4f, color, m, n);
            GirlfriendGlowRenderer.method_23158(vertexConsumer3, matrix4f, color, m, n);
            GirlfriendGlowRenderer.method_23157(vertexConsumer3, matrix4f, color, o);
            GirlfriendGlowRenderer.method_23158(vertexConsumer3, matrix4f, color, m, n);
            GirlfriendGlowRenderer.method_23159(vertexConsumer3, matrix4f, m, n);
            GirlfriendGlowRenderer.method_23157(vertexConsumer3, matrix4f, color, o);
            GirlfriendGlowRenderer.method_23159(vertexConsumer3, matrix4f, m, n);
            GirlfriendGlowRenderer.method_23156(vertexConsumer3, matrix4f, color, m, n);

            ++d;
        }

        matrixStack.pop();
    }

    private void setModelPose(GirlfriendEntity girlfriendEntity) {
        PlayerEntityModel<GirlfriendEntity> playerEntityModel = this.getModel();
        if (girlfriendEntity.isSpectator()) {
            playerEntityModel.setVisible(false);
            playerEntityModel.head.visible = true;
            playerEntityModel.hat.visible = true;
        } else {
            playerEntityModel.setVisible(true);
            playerEntityModel.sneaking = girlfriendEntity.isInSneakingPose();
        }
    }

    public Identifier getTexture(GirlfriendEntity girlfriendEntity) {
        return TEXTURE;
    }

    @Override
    protected void scale(GirlfriendEntity girlfriendEntity, MatrixStack matrixStack, float f) {
        matrixStack.scale(0.9375f, 0.9375f, 0.9375f);
        float g = 1f - (girlfriendEntity.getClientFuseTime(f) / (float) GirlfriendEntity.DEFAULT_FUSE);

        if (g <= 0.8f) {
            return;
        }

        g = (g - 0.8f) * 5f;
        float h = 1.0f + MathHelper.sin(g * 100.0f) * g * 0.01f;
        g = MathHelper.clamp(g, 0.0f, 1.0f);
        g *= g;
        g *= g;
        float i = (1.0f + g * 0.4f) * h;
        float j = (1.0f + g * 0.1f) / h;
        matrixStack.scale(i, j, i);
    }

    @Override
    protected float getAnimationCounter(GirlfriendEntity girlfriendEntity, float f) {
        float g = 1f - (girlfriendEntity.getClientFuseTime(f) / (float) GirlfriendEntity.DEFAULT_FUSE);

        if (g <= 0.8f) {
            return super.getAnimationCounter(girlfriendEntity, f);
        }

        g = (g - 0.8f) * 5f;

        if ((int) (g * 10.0f) % 2 == 0) {
            return 0.0f;
        }
        return MathHelper.clamp(g, 0.5f, 1.0f);
    }

    public static class HeldItemFeatureRenderer<T extends LivingEntity, M extends EntityModel<T> & ModelWithArms & ModelWithHead> extends net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer<T, M> {
        private final HeldItemRenderer heldItemRenderer;

        public HeldItemFeatureRenderer(FeatureRendererContext<T, M> featureRendererContext, HeldItemRenderer heldItemRenderer) {
            super(featureRendererContext, heldItemRenderer);
            this.heldItemRenderer = heldItemRenderer;
        }

        protected void renderItem(LivingEntity entity, ItemStack stack, ModelTransformationMode transformationMode, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
            if (stack.isOf(Items.SPYGLASS) && entity.getActiveItem() == stack && entity.handSwingTicks == 0) {
                this.renderSpyglass(entity, stack, arm, matrices, vertexConsumers, light);
            } else {
                super.renderItem(entity, stack, transformationMode, arm, matrices, vertexConsumers, light);
            }
        }

        private void renderSpyglass(LivingEntity entity, ItemStack stack, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
            matrices.push();
            ModelPart modelPart = this.getContextModel().getHead();
            float f = modelPart.pitch;
            modelPart.pitch = MathHelper.clamp(modelPart.pitch, -0.5235988F, 1.5707964F);
            modelPart.rotate(matrices);
            modelPart.pitch = f;
            HeadFeatureRenderer.translate(matrices, false);
            boolean bl = arm == Arm.LEFT;
            matrices.translate((bl ? -2.5F : 2.5F) / 16.0F, -0.0625D, 0.0D);
            heldItemRenderer.renderItem(entity, stack, ModelTransformationMode.HEAD, false, matrices, vertexConsumers, light);
            matrices.pop();
        }
    }
}
