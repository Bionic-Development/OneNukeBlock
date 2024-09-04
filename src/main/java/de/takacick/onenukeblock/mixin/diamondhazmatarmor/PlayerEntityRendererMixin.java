package de.takacick.onenukeblock.mixin.diamondhazmatarmor;

import de.takacick.onenukeblock.client.entity.feature.DiamondHazmatArmorFeatureRenderer;
import de.takacick.onenukeblock.client.entity.model.DiamondHazmatArmorModel;
import de.takacick.onenukeblock.registry.item.DiamondHazmatArmorItem;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    @Unique
    private final DiamondHazmatArmorModel<AbstractClientPlayerEntity> onenukeblock$diamondHazmatArmorModel = new DiamondHazmatArmorModel<>(DiamondHazmatArmorModel.getTexturedModelData().createModel());

    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/PlayerEntityRenderer;addFeature(Lnet/minecraft/client/render/entity/feature/FeatureRenderer;)Z", ordinal = 0))
    public void init(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo info) {
        this.addFeature(new DiamondHazmatArmorFeatureRenderer<>(this, this.onenukeblock$diamondHazmatArmorModel));
    }

    @Inject(method = "setModelPose", at = @At(value = "TAIL"))
    public void setModelPose(AbstractClientPlayerEntity player, CallbackInfo info) {
        if (player.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof DiamondHazmatArmorItem) {
            getModel().jacket.visible = false;
            getModel().leftSleeve.visible = false;
            getModel().rightSleeve.visible = false;
        }

        if (player.getEquippedStack(EquipmentSlot.LEGS).getItem() instanceof DiamondHazmatArmorItem) {
            getModel().leftPants.visible = false;
            getModel().rightPants.visible = false;
        }
    }

    @Inject(method = "renderArm", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;II)V", ordinal = 1, shift = At.Shift.BEFORE))
    public void hideSleeve(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve, CallbackInfo info) {

        if (player.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof DiamondHazmatArmorItem) {
            sleeve.visible = false;
        }
    }

    @Inject(method = "renderArm", at = @At(value = "TAIL"))
    public void renderArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve, CallbackInfo ci) {

        if (player.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof DiamondHazmatArmorItem) {
            ModelPart heavyCoreArm = this.onenukeblock$diamondHazmatArmorModel.rightArm;

            if (!getModel().rightArm.equals(arm)) {
                heavyCoreArm = this.onenukeblock$diamondHazmatArmorModel.leftArm;
            }

            matrices.push();
            heavyCoreArm.resetTransform();
            heavyCoreArm.visible = true;
            heavyCoreArm.copyTransform(arm);
            heavyCoreArm.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(DiamondHazmatArmorFeatureRenderer.TEXTURE)), light, OverlayTexture.DEFAULT_UV);
            matrices.pop();
            sleeve.visible = true;
        }
    }
}