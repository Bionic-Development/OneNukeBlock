package de.takacick.raidbase.registry.block.entity.renderer;

import de.takacick.raidbase.RaidBase;
import de.takacick.raidbase.registry.block.entity.PieLauncherBlockEntity;
import de.takacick.raidbase.registry.block.entity.model.PieLauncherModel;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class PieLauncherBlockEntityRenderer implements BlockEntityRenderer<PieLauncherBlockEntity> {

    private static final Identifier BASE = new Identifier(RaidBase.MOD_ID, "textures/entity/pie_launcher.png");
    private final PieLauncherModel pieLauncherModel;

    public PieLauncherBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.pieLauncherModel = new PieLauncherModel(PieLauncherModel.getTexturedModelData().createModel());
    }

    @Override
    public void render(PieLauncherBlockEntity pieLauncherBlockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        matrixStack.push();

        matrixStack.scale(-1.0f, -1.0f, 1.0f);
        matrixStack.translate(-0.5, -1.501f, 0.5);

        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(pieLauncherModel.getLayer(BASE));

        float chargeProgress = pieLauncherBlockEntity.getChargeProgress(tickDelta);

        this.pieLauncherModel.setAngles(pieLauncherBlockEntity, chargeProgress, pieLauncherBlockEntity.getAge() + tickDelta);
        this.pieLauncherModel.renderBottom(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);

        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerpAngleDegrees(tickDelta,
                pieLauncherBlockEntity.getPrevYaw(), pieLauncherBlockEntity.getYaw()) - 180f));

        this.pieLauncherModel.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);

        matrixStack.scale(chargeProgress, chargeProgress, chargeProgress);
        this.pieLauncherModel.renderPie(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);

        matrixStack.pop();
    }
}

