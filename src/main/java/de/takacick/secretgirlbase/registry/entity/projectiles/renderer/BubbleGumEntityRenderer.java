package de.takacick.secretgirlbase.registry.entity.projectiles.renderer;

import de.takacick.secretgirlbase.SecretGirlBase;
import de.takacick.secretgirlbase.registry.entity.projectiles.BubbleGumEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class BubbleGumEntityRenderer extends EntityRenderer<BubbleGumEntity> {

    private static final Identifier TEXTURE = new Identifier(SecretGirlBase.MOD_ID, "textures/entity/bubble_gum_launcher.png");

    private final ModelPart modelPart;

    public BubbleGumEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.modelPart = getTexturedModelData().createModel();
    }

    public void render(BubbleGumEntity bubbleGumEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.translate(0, 0.125, 0);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerpAngleDegrees(g, bubbleGumEntity.prevYaw, bubbleGumEntity.getYaw()) + 180));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(MathHelper.lerp(g, bubbleGumEntity.prevPitch, bubbleGumEntity.getPitch())));

        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(getTexture(bubbleGumEntity)));
        this.modelPart.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);

        matrixStack.pop();

        super.render(bubbleGumEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Identifier getTexture(BubbleGumEntity bubbleGumEntity) {
        return TEXTURE;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bubble_gum = modelPartData.addChild("bubble_gum", ModelPartBuilder.create().uv(91, 0).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(1.0F)), ModelTransform.pivot(0.0F, 0f, 0.0F));
        return TexturedModelData.of(modelData, 128, 128);
    }
}
