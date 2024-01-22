package de.takacick.illegalwars.registry.block.entity.renderer;

import de.takacick.illegalwars.IllegalWars;
import de.takacick.illegalwars.registry.block.entity.PiglinGoldTurretBlockEntity;
import de.takacick.illegalwars.registry.block.entity.model.PiglinGoldTurretModel;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class PiglinGoldTurretBlockEntityRenderer implements BlockEntityRenderer<PiglinGoldTurretBlockEntity> {

    private static final Identifier TEXTURE = new Identifier(IllegalWars.MOD_ID, "textures/entity/piglin_gold_turret.png");
    private final PiglinGoldTurretModel piglinGoldTurretModel;

    public PiglinGoldTurretBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.piglinGoldTurretModel = new PiglinGoldTurretModel(PiglinGoldTurretModel.getTexturedModelData().createModel());
    }

    @Override
    public void render(PiglinGoldTurretBlockEntity piglinGoldTurretBlockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        matrixStack.push();

        matrixStack.scale(-1.0f, -1.0f, 1.0f);
        matrixStack.translate(-0.5, -1.501f, 0.5);

        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(piglinGoldTurretModel.getLayer(TEXTURE));
        this.piglinGoldTurretModel.setAngles(piglinGoldTurretBlockEntity, MathHelper.lerpAngleDegrees(tickDelta, piglinGoldTurretBlockEntity.getPrevYaw(), piglinGoldTurretBlockEntity.getYaw()) - 180f);
        this.piglinGoldTurretModel.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
        matrixStack.pop();
    }
}

