package de.takacick.upgradebody.client.renderer;

import de.takacick.upgradebody.access.ClientPlayerProperties;
import de.takacick.upgradebody.access.PlayerProperties;
import de.takacick.upgradebody.client.model.BodyEntityModel;
import de.takacick.upgradebody.client.model.parts.BodyPartModel;
import de.takacick.upgradebody.client.model.parts.CyberChainsawsModel;
import de.takacick.upgradebody.registry.bodypart.BodyPart;
import de.takacick.upgradebody.registry.bodypart.BodyParts;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

public class BodyEntityRenderer extends PlayerEntityRenderer {

    private final BodyEntityModel baseModel;

    public BodyEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, true);
        this.features.clear();

        this.baseModel = new BodyEntityModel(BodyEntityModel.getTexturedModelData().createModel());
        this.model = this.baseModel;
        //   this.addFeature(new HeadHeldItemFeatureRenderer<>(this, ctx.getHeldItemRenderer()));
        this.addFeature(new HeldItemFeatureRenderer<>(this, ctx.getHeldItemRenderer()));
    }

    @Override
    public void render(AbstractClientPlayerEntity abstractClientPlayerEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        if (abstractClientPlayerEntity instanceof ClientPlayerProperties clientPlayerProperties) {
            this.model = clientPlayerProperties.getUpgradeModel();
        }

        if (this.model == null) {
            this.model = this.baseModel;
        }

        boolean hasArms = false;

        if (abstractClientPlayerEntity instanceof PlayerProperties playerProperties
                && playerProperties.isUpgrading()) {

            if (playerProperties.isUsingEnergyBellyBlast() || playerProperties.isUsingCyberSlice()) {
                abstractClientPlayerEntity.prevBodyYaw = abstractClientPlayerEntity.prevHeadYaw;
                abstractClientPlayerEntity.bodyYaw = abstractClientPlayerEntity.headYaw;
            }

            hasArms = playerProperties.getBodyPartManager().hasBodyPart(BodyParts.CYBER_CHAINSAWS);
            this.shadowRadius = (float) playerProperties.getBodyPartManager().getWidth() / 1.2f;
        }

        matrixStack.push();
        this.model.handSwingProgress = this.getHandSwingProgress(abstractClientPlayerEntity, g);
        this.model.riding = abstractClientPlayerEntity.hasVehicle();
        this.model.child = abstractClientPlayerEntity.isBaby();
        float j = MathHelper.lerpAngleDegrees(g, abstractClientPlayerEntity.prevHeadYaw, abstractClientPlayerEntity.headYaw);
        float h = hasArms ? MathHelper.lerpAngleDegrees(g, abstractClientPlayerEntity.prevBodyYaw, abstractClientPlayerEntity.bodyYaw) : j;
        float k = j - h;
        float l;
        if (abstractClientPlayerEntity.hasVehicle() && abstractClientPlayerEntity.getVehicle() instanceof LivingEntity abstractClientPlayerEntity2) {
            h = MathHelper.lerpAngleDegrees(g, abstractClientPlayerEntity2.prevBodyYaw, abstractClientPlayerEntity2.bodyYaw);
            k = j - h;
            l = MathHelper.wrapDegrees(k);
            if (l < -85.0F) {
                l = -85.0F;
            }

            if (l >= 85.0F) {
                l = 85.0F;
            }

            h = j - l;
            if (l * l > 2500.0F) {
                h += l * 0.2F;
            }

            k = j - h;
        }

        float m = MathHelper.lerp(g, abstractClientPlayerEntity.prevPitch, abstractClientPlayerEntity.getPitch());
        if (shouldFlipUpsideDown(abstractClientPlayerEntity)) {
            m *= -1.0F;
            k *= -1.0F;
        }

        float n;
        if (abstractClientPlayerEntity.isInPose(EntityPose.SLEEPING)) {
            Direction direction = abstractClientPlayerEntity.getSleepingDirection();
            if (direction != null) {
                n = abstractClientPlayerEntity.getEyeHeight(EntityPose.STANDING) - 0.1F;
                matrixStack.translate((float) (-direction.getOffsetX()) * n, 0.0F, (float) (-direction.getOffsetZ()) * n);
            }
        }

        l = this.getAnimationProgress(abstractClientPlayerEntity, g);
        this.setupTransforms(abstractClientPlayerEntity, matrixStack, l, h, g);
        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        this.scale(abstractClientPlayerEntity, matrixStack, g);
        matrixStack.translate(0.0F, -1.501F, 0.0F);
        n = 0.0F;
        float o = 0.0F;
        if (!abstractClientPlayerEntity.hasVehicle() && abstractClientPlayerEntity.isAlive()) {
            n = abstractClientPlayerEntity.limbAnimator.getSpeed(g);
            o = abstractClientPlayerEntity.limbAnimator.getPos(g);
            if (abstractClientPlayerEntity.isBaby()) {
                o *= 3.0F;
            }

            if (n > 1.0F) {
                n = 1.0F;
            }
        }

        this.model.animateModel(abstractClientPlayerEntity, o, n, g);
        this.model.setAngles(abstractClientPlayerEntity, o, n, l, k, m);
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        boolean bl = this.isVisible(abstractClientPlayerEntity);
        boolean bl2 = !bl && !abstractClientPlayerEntity.isInvisibleTo(minecraftClient.player);
        boolean bl3 = minecraftClient.hasOutline(abstractClientPlayerEntity);
        int p = getOverlay(abstractClientPlayerEntity, this.getAnimationCounter(abstractClientPlayerEntity, g));
        if (this.model instanceof BodyEntityModel bodyEntityModel) {
            bodyEntityModel.render(abstractClientPlayerEntity, matrixStack, vertexConsumerProvider, bl, bl2, bl3, i, p, 1.0F, 1.0F, 1.0F, bl2 ? 0.15F : 1.0F);
        }

        if (!abstractClientPlayerEntity.isSpectator()) {
            var var23 = this.features.iterator();

            while (var23.hasNext()) {
                var featureRenderer = var23.next();
                featureRenderer.render(matrixStack, vertexConsumerProvider, i, abstractClientPlayerEntity, o, n, g, l, k, m);
            }
        }

        matrixStack.pop();
        super.render(abstractClientPlayerEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    protected void scale(AbstractClientPlayerEntity abstractClientPlayerEntity, MatrixStack matrixStack, float f) {
        super.scale(abstractClientPlayerEntity, matrixStack, f);

        if (abstractClientPlayerEntity instanceof PlayerProperties playerProperties) {

            float g = 0.999f;
            matrixStack.scale(0.999f, 0.999f, 0.999f);
            matrixStack.translate(0.0f, 0.001f, 0.0f);
            float h = 1;
            float i = MathHelper.lerp(f, playerProperties.getLastStretch(), playerProperties.getStretch()) / (h * 0.5f + 1.0f);
            float j = 1.0f / (i + 1.0f);
            matrixStack.scale(j * h, 1.0f / j * h, j * h);
        }
    }

    @Override
    public void renderRightArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player) {
        this.renderArm(matrices, vertexConsumers, light, player, BodyParts.CYBER_CHAINSAWS, false);
    }

    @Override
    public void renderLeftArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player) {
        this.renderArm(matrices, vertexConsumers, light, player, BodyParts.CYBER_CHAINSAWS, true);
    }

    private void renderArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, BodyPart bodyPart, boolean leftArm) {
        if (player instanceof ClientPlayerProperties clientPlayerProperties
                && clientPlayerProperties.getUpgradeModel() != null) {
            BodyEntityModel bodyEntityModel = clientPlayerProperties.getUpgradeModel();
            this.model = bodyEntityModel;
            this.setModelPose(player);
            bodyEntityModel.handSwingProgress = 0.0f;
            bodyEntityModel.sneaking = false;
            bodyEntityModel.leaningPitch = 0.0f;
            bodyEntityModel.setAngles(player, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
            BodyPartModel bodyPartModel = bodyEntityModel.getUpgradedParts().getOrDefault(bodyPart, null);

            if (bodyPartModel instanceof CyberChainsawsModel model) {
                RenderLayer renderLayer = bodyEntityModel.getRenderLayer(player, bodyPartModel, true, false, false);

                ModelPart arm = leftArm ? model.getLeftArm() : model.getRightArm();
                arm.pitch = 0.0f;
                arm.render(matrices, vertexConsumers.getBuffer(renderLayer), light, OverlayTexture.DEFAULT_UV);
            }

        }
    }

}
