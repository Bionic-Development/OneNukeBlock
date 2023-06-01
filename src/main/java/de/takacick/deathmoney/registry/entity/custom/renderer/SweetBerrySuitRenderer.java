package de.takacick.deathmoney.registry.entity.custom.renderer;

import de.takacick.deathmoney.DeathMoney;
import de.takacick.deathmoney.registry.ItemRegistry;
import net.minecraft.client.model.*;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;

public class SweetBerrySuitRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    private static final Identifier TEXTURE = new Identifier(DeathMoney.MOD_ID, "textures/entity/sweet_berry_suit.png");
    private final ModelPart model = getTexturedModelData().createModel();

    public SweetBerrySuitRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext) {
        super(featureRendererContext);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (!entity.getEquippedStack(EquipmentSlot.CHEST).isOf(ItemRegistry.SWEET_BERRY_SUIT)) {
            return;
        }

        model.getChild("head").copyTransform(getContextModel().head);
        model.getChild("body").copyTransform(getContextModel().body);

        model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(TEXTURE)), light, OverlayTexture.DEFAULT_UV);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(31, 34).cuboid(-5.0F, -9.0F, -5.0F, 10.0F, 9.0F, 10.0F, new Dilation(0.0f))
                .uv(31, 12).cuboid(-5.0F, -9.0F, -5.0F, 10.0F, 9.0F, 10.0F, new Dilation(0.15F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 22).cuboid(-5.0F, 0.3F, -5.0F, 10.0F, 11.0F, 10.0F, new Dilation(0.15F))
                .uv(0, 0).cuboid(-5.0F, 0.3F, -5.0F, 10.0F, 11.0F, 10.0F, new Dilation(0.0f)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 128, 128);
    }
}

