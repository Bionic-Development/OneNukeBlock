package de.takacick.illegalwars.registry.block.entity.renderer;

import de.takacick.illegalwars.IllegalWars;
import de.takacick.illegalwars.registry.block.BaseWarsMoneyWheelBlock;
import de.takacick.illegalwars.registry.block.entity.BaseWarsMoneyWheelBlockEntity;
import de.takacick.illegalwars.registry.block.entity.model.BaseWarsMoneyWheelModel;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class BaseWarsMoneyWheelBlockEntityRenderer implements BlockEntityRenderer<BaseWarsMoneyWheelBlockEntity> {

    private static final Identifier TEXTURE = new Identifier(IllegalWars.MOD_ID, "textures/entity/base_wars_money_wheel_base.png");
    private static final Identifier WHEEL = new Identifier(IllegalWars.MOD_ID, "textures/entity/base_wars_money_wheel.png");
    private final BaseWarsMoneyWheelModel baseWarsMoneyWheelModel;

    public BaseWarsMoneyWheelBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.baseWarsMoneyWheelModel = new BaseWarsMoneyWheelModel();
    }

    @Override
    public void render(BaseWarsMoneyWheelBlockEntity baseWarsMoneyWheelBlockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        if (!baseWarsMoneyWheelBlockEntity.isOwner()) {
            return;
        }

        matrixStack.push();

        matrixStack.scale(-1.0f, -1.0f, 1.0f);
        matrixStack.translate(-0.5, -1.501f, 0.5);
        matrixStack.translate(0f, 1f, 0);

        if (baseWarsMoneyWheelBlockEntity.gui) {
            matrixStack.scale(1.25f, 1.25f, 1.25f);
        } else {
            matrixStack.scale(3f, 3f, 3f);
        }

        matrixStack.translate(0f, -1f, 0);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(baseWarsMoneyWheelBlockEntity.getCachedState().get(BaseWarsMoneyWheelBlock.FACING).asRotation() - 180f));

        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(baseWarsMoneyWheelModel.getLayer(TEXTURE));
        this.baseWarsMoneyWheelModel.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);

        vertexConsumer = vertexConsumerProvider.getBuffer(baseWarsMoneyWheelModel.getLayer(WHEEL));
        this.baseWarsMoneyWheelModel.setRotation(baseWarsMoneyWheelBlockEntity.getRotation(tickDelta));
        this.baseWarsMoneyWheelModel.renderWheel(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
        matrixStack.pop();
    }
}

