package de.takacick.onegirlfriendblock.mixin.maidsuit;

import de.takacick.onegirlfriendblock.access.LivingProperties;
import de.takacick.onegirlfriendblock.access.PlayerProperties;
import de.takacick.onegirlfriendblock.client.model.MaidSuitModel;
import de.takacick.onegirlfriendblock.client.renderer.HoldingOneBlockRenderer;
import de.takacick.onegirlfriendblock.client.renderer.MaidSuitRenderer;
import de.takacick.onegirlfriendblock.client.shaders.OneGirlfriendBlockLayers;
import de.takacick.onegirlfriendblock.registry.item.MaidSuit;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    @Unique
    private MaidSuitModel onegirlfriendblock$maidSuit = new MaidSuitModel(MaidSuitModel.getTexturedModelData().createModel());

    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "setModelPose", at = @At(value = "TAIL"))
    public void setModelPose(AbstractClientPlayerEntity player, CallbackInfo info) {
        if (player.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof MaidSuit) {
            PlayerEntityModel<AbstractClientPlayerEntity> playerEntityModel = this.getModel();
            playerEntityModel.jacket.visible = false;
            playerEntityModel.leftPants.visible = false;
            playerEntityModel.rightPants.visible = false;
            playerEntityModel.leftSleeve.visible = false;
            playerEntityModel.rightSleeve.visible = false;
        }
    }

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    public void init(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo info) {
        this.onegirlfriendblock$maidSuit = new MaidSuitModel(MaidSuitModel.getTexturedModelData().createModel());
        this.addFeature(new MaidSuitRenderer(this, this.onegirlfriendblock$maidSuit));
        this.addFeature(new HoldingOneBlockRenderer(this));
    }

    @Inject(method = "renderArm", at = @At(value = "HEAD"), cancellable = true)
    private void head(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve, CallbackInfo info) {
        if (player instanceof PlayerProperties playerProperties && playerProperties.hasOneGirlfriendBlock()) {
            info.cancel();
        }
    }

    @Inject(method = "renderArm", at = @At(value = "TAIL"))
    private void renderRightArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve, CallbackInfo info) {
        ItemStack itemStack = player.getEquippedStack(EquipmentSlot.CHEST);
        if (itemStack.getItem() instanceof MaidSuit) {
            ModelPart modelPart = arm.equals(this.getModel().rightArm) ? this.onegirlfriendblock$maidSuit.rightArm : this.onegirlfriendblock$maidSuit.leftArm;
            modelPart.copyTransform(arm);

            RenderLayer renderLayer = RenderLayer.getEntitySolid(MaidSuitRenderer.TEXTURE);

            if (player instanceof LivingProperties livingProperties && livingProperties.getLipstickStrength() > 0) {
                renderLayer = OneGirlfriendBlockLayers.getLipstick(MaidSuitRenderer.TEXTURE,
                        livingProperties.getLipstickStrength());
            }

            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);
            modelPart.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
        }
    }
}