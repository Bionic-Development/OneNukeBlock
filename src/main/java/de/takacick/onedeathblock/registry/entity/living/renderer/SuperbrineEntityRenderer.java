package de.takacick.onedeathblock.registry.entity.living.renderer;

import de.takacick.onedeathblock.OneDeathBlock;
import de.takacick.onedeathblock.registry.entity.living.SuperbrineEntity;
import de.takacick.onedeathblock.registry.entity.living.model.SuperbrineEntityModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

public class SuperbrineEntityRenderer extends MobEntityRenderer<SuperbrineEntity, PlayerEntityModel<SuperbrineEntity>> {
    private static final Identifier TEXTURE = new Identifier(OneDeathBlock.MOD_ID, "textures/entity/superbrine.png");

    public SuperbrineEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new SuperbrineEntityModel(context.getPart(EntityModelLayers.PLAYER), false), 0.5f);
        this.addFeature(new ArmorFeatureRenderer<>(this, new BipedEntityModel<>(context.getPart(EntityModelLayers.PLAYER_INNER_ARMOR)), new BipedEntityModel<>(context.getPart(EntityModelLayers.PLAYER_OUTER_ARMOR))));
        this.addFeature(new HeldItemFeatureRenderer<>(this, context.getHeldItemRenderer()));
        this.addFeature(new SuperbrineEyesFeatureRenderer<>(this));
        this.addFeature(new StuckArrowsFeatureRenderer<>(context, this));
        this.addFeature(new HeadFeatureRenderer<>(this, context.getModelLoader(), context.getHeldItemRenderer()));
        this.addFeature(new ElytraFeatureRenderer<>(this, context.getModelLoader()));
        this.addFeature(new TridentRiptideFeatureRenderer<>(this, context.getModelLoader()));
    }

    @Override
    public void render(SuperbrineEntity superbrineEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        setModelPose(superbrineEntity);
        super.render(superbrineEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    protected void scale(SuperbrineEntity superbrineEntity, MatrixStack matrixStack, float tickDelta) {
        if (superbrineEntity.isDead()) {
            float g = MathHelper.clamp(((float) superbrineEntity.deathTick + tickDelta) / 60.0f, 0f, 1f);
            matrixStack.scale(0.9375f + g * 1.35f, 0.9375f + g * 1.35f, 0.9375f + g * 1.35f);
        } else {
            matrixStack.scale(0.9375f, 0.9375f, 0.9375f);
        }
    }

    @Override
    protected float getAnimationCounter(SuperbrineEntity superbrineEntity, float tickDelta) {
        if (superbrineEntity.isDead()) {
            float g = MathHelper.clamp(((float) superbrineEntity.deathTick + tickDelta) / 60.0f, 0f, 1f);
            return g;
        }
        return 0f;
    }

    protected void setupTransforms(SuperbrineEntity entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta) {
        if (this.isShaking(entity)) {
            bodyYaw += (float) (Math.cos((double) entity.age * 3.25) * Math.PI * (double) 0.4f);
        }
        if (!entity.isInPose(EntityPose.SLEEPING)) {
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0f - bodyYaw));
        }

        if (entity.isUsingRiptide()) {
            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-90.0f - entity.getPitch()));
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(((float) entity.age + tickDelta) * -75.0f));
        } else if (entity.isInPose(EntityPose.SLEEPING)) {
            Direction direction = entity.getSleepingDirection();
            float g = direction != null ? getYaw(direction) : bodyYaw;
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(g));
            matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(this.getLyingAngle(entity)));
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(270.0f));
        } else if (LivingEntityRenderer.shouldFlipUpsideDown(entity)) {
            matrices.translate(0.0, entity.getHeight() + 0.1f, 0.0);
            matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0f));
        }
    }

    private void setModelPose(SuperbrineEntity superbrineEntity) {
        PlayerEntityModel<SuperbrineEntity> playerEntityModel = this.getModel();
        if (superbrineEntity
                .isSpectator()) {
            playerEntityModel.setVisible(false);
            playerEntityModel.head.visible = true;
            playerEntityModel.hat.visible = true;
        } else {
            playerEntityModel.setVisible(true);
            playerEntityModel.sneaking = superbrineEntity
                    .isInSneakingPose();
        }
    }

    public Identifier getTexture(SuperbrineEntity superbrineEntity) {
        return TEXTURE;
    }

    public static class HeldItemFeatureRenderer<T extends LivingEntity, M extends EntityModel<T> & ModelWithArms & ModelWithHead> extends net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer<T, M> {
        private final HeldItemRenderer heldItemRenderer;

        public HeldItemFeatureRenderer(FeatureRendererContext<T, M> featureRendererContext, HeldItemRenderer heldItemRenderer) {
            super(featureRendererContext, heldItemRenderer);
            this.heldItemRenderer = heldItemRenderer;
        }

        protected void renderItem(LivingEntity entity, ItemStack stack, ModelTransformation.Mode transformationMode, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
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
            heldItemRenderer.renderItem(entity, stack, ModelTransformation.Mode.HEAD, false, matrices, vertexConsumers, light);
            matrices.pop();
        }
    }

    @Environment(value = EnvType.CLIENT)
    public static class SuperbrineEyesFeatureRenderer<T extends LivingEntity>
            extends EyesFeatureRenderer<T, PlayerEntityModel<T>> {

        private static final RenderLayer SKIN = RenderLayer.getEyes(new Identifier(OneDeathBlock.MOD_ID, "textures/entity/superbrine_eyes.png"));

        public SuperbrineEyesFeatureRenderer(FeatureRendererContext<T, PlayerEntityModel<T>> featureRendererContext) {
            super(featureRendererContext);
        }

        @Override
        public RenderLayer getEyesTexture() {
            return SKIN;
        }
    }

    @Override
    protected boolean isShaking(SuperbrineEntity superbrineEntity) {
        return superbrineEntity.hasPassengers() || superbrineEntity.isDead() || super.isShaking(superbrineEntity);
    }

    private static float getYaw(Direction direction) {
        switch (direction) {
            case SOUTH: {
                return 90.0f;
            }
            case WEST: {
                return 0.0f;
            }
            case NORTH: {
                return 270.0f;
            }
            case EAST: {
                return 180.0f;
            }
        }
        return 0.0f;
    }
}
