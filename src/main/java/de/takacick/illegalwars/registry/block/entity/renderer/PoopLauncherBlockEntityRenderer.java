package de.takacick.illegalwars.registry.block.entity.renderer;

import de.takacick.illegalwars.IllegalWars;
import de.takacick.illegalwars.registry.block.entity.PoopLauncherBlockEntity;
import de.takacick.illegalwars.registry.block.entity.model.PoopLauncherModel;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class PoopLauncherBlockEntityRenderer implements BlockEntityRenderer<PoopLauncherBlockEntity> {

    private static final Identifier BASE = new Identifier(IllegalWars.MOD_ID, "textures/entity/poop_launcher.png");
    private final PoopLauncherModel poopLauncherModel;

    public PoopLauncherBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.poopLauncherModel = new PoopLauncherModel(PoopLauncherModel.getTexturedModelData().createModel());
    }

    @Override
    public void render(PoopLauncherBlockEntity poopLauncherBlockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        matrixStack.push();

        matrixStack.scale(-1.0f, -1.0f, 1.0f);
        matrixStack.translate(-0.5, -1.50f, 0.5);

        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(poopLauncherModel.getLayer(BASE));

        float chargeProgress = poopLauncherBlockEntity.getChargeProgress(tickDelta);

        this.poopLauncherModel.setAngles(poopLauncherBlockEntity, chargeProgress, poopLauncherBlockEntity.getAge() + tickDelta);
        this.poopLauncherModel.renderBottom(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);

        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerpAngleDegrees(tickDelta,
                poopLauncherBlockEntity.getPrevYaw(), poopLauncherBlockEntity.getYaw()) - 180f));

        this.poopLauncherModel.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);

        matrixStack.scale(chargeProgress, chargeProgress, chargeProgress);
        this.poopLauncherModel.renderPie(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);

        matrixStack.pop();
    }
}

