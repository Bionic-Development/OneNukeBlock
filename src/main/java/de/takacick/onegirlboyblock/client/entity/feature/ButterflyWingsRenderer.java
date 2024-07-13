package de.takacick.onegirlboyblock.client.entity.feature;

import de.takacick.onegirlboyblock.OneGirlBoyBlock;
import de.takacick.onegirlboyblock.access.PlayerModelProperties;
import de.takacick.onegirlboyblock.client.entity.model.ButterflyWingsModel;
import de.takacick.onegirlboyblock.client.shader.OneGirlBoyBlockLayers;
import de.takacick.onegirlboyblock.registry.item.ButterflyWings;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class ButterflyWingsRenderer<T extends LivingEntity, M extends BipedEntityModel<T>> extends FeatureRenderer<T, M> {

    public static final Identifier TEXTURE = Identifier.of(OneGirlBoyBlock.MOD_ID, "textures/entity/butterfly_wings.png");
    private final ButterflyWingsModel<T> butterflyWings;

    public ButterflyWingsRenderer(FeatureRendererContext<T, M> featureRendererContext) {
        super(featureRendererContext);
        this.butterflyWings = new ButterflyWingsModel<>(ButterflyWingsModel.getTexturedModelData().createModel());
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {

        if (!(entity.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof ButterflyWings)) {
            return;
        }

        matrices.push();

        getContextModel().body.rotate(matrices);
        if (this.getContextModel() instanceof PlayerModelProperties playerModelProperties) {
            playerModelProperties.getAnimationBodyModel().rotateButterflyWings(matrices);
        }
        RenderLayer renderLayer = OneGirlBoyBlockLayers.getBitCannonGlow(TEXTURE);
        this.butterflyWings.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
        this.butterflyWings.render(matrices, vertexConsumers.getBuffer(renderLayer), light, OverlayTexture.DEFAULT_UV);
        matrices.pop();
    }
}

