package de.takacick.immortalmobs.mixin;

import de.takacick.immortalmobs.ImmortalMobs;
import de.takacick.immortalmobs.access.PlayerEntityRendererAccessor;
import de.takacick.immortalmobs.client.CustomLayers;
import de.takacick.immortalmobs.registry.ItemRegistry;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>>
        extends FeatureRenderer<T, M> {

    @Shadow
    @Final
    private A bodyModel;
    private static final Identifier IMMORTAL_ARMOR = new Identifier(ImmortalMobs.MOD_ID, "textures/models/armor/immortal_layer_1.png");
    private A dilationBodyModel;
    private A dilationInnerBodyModel;

    public ArmorFeatureRendererMixin(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(FeatureRendererContext context, BipedEntityModel leggingsModel, BipedEntityModel bodyModel, CallbackInfo info) {
        if (context instanceof PlayerEntityRendererAccessor) {
            dilationBodyModel = (A) new BipedEntityModel<T>(TexturedModelData.of(BipedEntityModel.getModelData(new Dilation(1.1f), 0.0f), 64, 32).createModel());
            dilationInnerBodyModel = (A) new BipedEntityModel<T>(TexturedModelData.of(BipedEntityModel.getModelData(new Dilation(-0.9f), 0.0f), 64, 32).createModel());
        }
    }

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V", at = @At("TAIL"))
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo info) {
        if (livingEntity.getEquippedStack(EquipmentSlot.CHEST).isOf(ItemRegistry.IMMORTAL_SHIRT)) {
            this.getContextModel().setAttributes(bodyModel);
            VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumerProvider, CustomLayers.IMMORTAL_CUTOUT.apply(IMMORTAL_ARMOR), false, false);
            bodyModel.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
            vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumerProvider, RenderLayer.getEyes(IMMORTAL_ARMOR), false, false);
            if (dilationBodyModel != null) {
                this.getContextModel().setAttributes(dilationBodyModel);
                this.getContextModel().setAttributes(dilationInnerBodyModel);
            }
            (dilationBodyModel == null ? bodyModel : dilationBodyModel).render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 0.4029412f, 0.14705883f, 0.5411765f, 1.0f);
            (dilationBodyModel == null ? bodyModel : dilationBodyModel).render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 0.4029412f, 0.14705883f, 0.5411765f, 1.0f);
            if (dilationInnerBodyModel != null) {
                dilationInnerBodyModel.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 0.4029412f, 0.14705883f, 0.5411765f, 1.0f);
                dilationInnerBodyModel.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 0.4029412f, 0.14705883f, 0.5411765f, 1.0f);
            }
        }
    }
}
