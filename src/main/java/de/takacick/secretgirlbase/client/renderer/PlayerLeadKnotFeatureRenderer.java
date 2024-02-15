package de.takacick.secretgirlbase.client.renderer;

import de.takacick.secretgirlbase.SecretGirlBase;
import de.takacick.secretgirlbase.access.LeadCuffProperties;
import de.takacick.secretgirlbase.registry.entity.custom.FireworkTimeBombEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class PlayerLeadKnotFeatureRenderer<T extends PlayerEntity, M extends PlayerEntityModel<T>>
        extends FeatureRenderer<T, M> {

    public static final Identifier TEXTURE = new Identifier(SecretGirlBase.MOD_ID, "textures/entity/player_lead_knot.png");
    private final ModelPart leadKnot;

    public PlayerLeadKnotFeatureRenderer(FeatureRendererContext<T, M> context) {
        super(context);
        this.leadKnot = getTexturedModelData().createModel();
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
        if (!(livingEntity.getVehicle() instanceof FireworkTimeBombEntity)) {
            return;
        }
        matrixStack.push();

        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutout(TEXTURE));
        this.leadKnot.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV);

        matrixStack.pop();
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData rope = modelPartData.addChild("rope", ModelPartBuilder.create().uv(2, 2).cuboid(-9.0F, -18.0F, -4.0F, 18.0F, 7.0F, 9.0F, new Dilation(0.001F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }
}