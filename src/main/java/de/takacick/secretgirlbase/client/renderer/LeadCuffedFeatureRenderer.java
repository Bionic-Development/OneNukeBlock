package de.takacick.secretgirlbase.client.renderer;

import de.takacick.secretgirlbase.access.LeadCuffProperties;
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

public class LeadCuffedFeatureRenderer<T extends PlayerEntity, M extends PlayerEntityModel<T>>
        extends FeatureRenderer<T, M> {

    public static final Identifier TEXTURE = new Identifier("textures/entity/lead_knot.png");
    private final ModelPart rightKnot;
    private final ModelPart leftKnot;

    public LeadCuffedFeatureRenderer(FeatureRendererContext<T, M> context, ModelPart leadKnot) {
        super(context);
        this.rightKnot = leadKnot.getChild("right_knot");
        this.leftKnot = leadKnot.getChild("left_knot");
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
        if (!((LeadCuffProperties) livingEntity).isLeadCuffed()) {
            return;
        }
        matrixStack.push();

        this.rightKnot.copyTransform(getContextModel().rightArm);
        this.leftKnot.copyTransform(getContextModel().leftArm);

        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutout(TEXTURE));
        this.rightKnot.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV);
        this.leftKnot.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV);

        matrixStack.pop();
    }

    public static TexturedModelData getTexturedModelData(boolean slim) {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        if (slim) {
            modelPartData.addChild("right_knot", ModelPartBuilder.create().uv(0, 0).cuboid(-3.5F, 5.0F, -3.0F, 6.0F, 8.0F, 6.0F, new Dilation(-0.25F)), ModelTransform.of(-5.0F, 2.0F, 0.0F, -0.5236F, -0.0873F, -0.1745F));
            modelPartData.addChild("left_knot", ModelPartBuilder.create().uv(0, 0).cuboid(-2.5F, 5.0F, -3.0F, 6.0F, 8.0F, 6.0F, new Dilation(-0.25F)), ModelTransform.of(5.0F, 2.0F, 0.0F, -0.5236F, 0.0873F, 0.1745F));
        } else {
            modelPartData.addChild("right_knot", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, 5.0F, -3.0F, 6.0F, 8.0F, 6.0F, new Dilation(-0.25F)), ModelTransform.of(-5.0F, 2.0F, 0.0F, -1.0472F, -0.5236F, 0.0F));
            modelPartData.addChild("left_knot", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, 5.0F, -3.0F, 6.0F, 8.0F, 6.0F, new Dilation(-0.25F)), ModelTransform.of(5.0F, 2.0F, 0.0F, -1.0472F, 0.5236F, 0.0F));
        }
        return TexturedModelData.of(modelData, 32, 32);
    }
}