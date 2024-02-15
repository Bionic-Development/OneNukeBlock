package de.takacick.secretgirlbase.registry.block.entity.renderer;

import de.takacick.secretgirlbase.SecretGirlBase;
import de.takacick.secretgirlbase.registry.block.entity.BubbleGumLauncherBlockEntity;
import de.takacick.secretgirlbase.registry.block.entity.model.BubbleGumLauncherBlockEntityModel;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class BubbleGumLauncherBlockEntityRenderer implements BlockEntityRenderer<BubbleGumLauncherBlockEntity> {

    private static final Identifier BASE = new Identifier(SecretGirlBase.MOD_ID, "textures/entity/bubble_gum_launcher.png");
    private final BubbleGumLauncherBlockEntityModel bubbleGumLauncher;

    public BubbleGumLauncherBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.bubbleGumLauncher = new BubbleGumLauncherBlockEntityModel(BubbleGumLauncherBlockEntityModel.getTexturedModelData().createModel());
    }

    @Override
    public void render(BubbleGumLauncherBlockEntity bubbleGumLauncherBlockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        matrixStack.push();

        matrixStack.scale(-1.0f, -1.0f, 1.0f);
        matrixStack.translate(-0.5, -1.50f, 0.5);

        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(bubbleGumLauncher.getLayer(BASE));

        float chargeProgress = bubbleGumLauncherBlockEntity.getChargeProgress(tickDelta);

        this.bubbleGumLauncher.setAngles(bubbleGumLauncherBlockEntity, chargeProgress, bubbleGumLauncherBlockEntity.getAge() + tickDelta);
        this.bubbleGumLauncher.renderBottom(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);

        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerpAngleDegrees(tickDelta,
                bubbleGumLauncherBlockEntity.getPrevYaw(), bubbleGumLauncherBlockEntity.getYaw()) - 180f));

        this.bubbleGumLauncher.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);

        matrixStack.scale(chargeProgress, chargeProgress, chargeProgress);
        this.bubbleGumLauncher.renderBubbleGum(matrixStack, vertexConsumer, chargeProgress, i, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);

        matrixStack.pop();
    }
}

