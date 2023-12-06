package de.takacick.emeraldmoney.registry.entity.custom.renderer;

import de.takacick.emeraldmoney.registry.entity.custom.VillagerNoseEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;

public class VillagerNoseEntityRenderer extends EntityRenderer<VillagerNoseEntity> {

    public VillagerNoseEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(VillagerNoseEntity villagerNoseEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {

        if (MinecraftClient.getInstance().player != null) {
            if (MinecraftClient.getInstance().player.equals(villagerNoseEntity.getOwnerUuid())) {
                return;
            }
        }

        matrixStack.push();
        matrixStack.translate(-0.5, 0, -0.5);
        renderBlockAsEntity(villagerNoseEntity.getBlockStateAtPos(), matrixStack, vertexConsumerProvider, i);
        matrixStack.pop();

        super.render(villagerNoseEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(VillagerNoseEntity villagerNoseEntity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }

    public void renderBlockAsEntity(BlockState state, MatrixStack matrices, VertexConsumerProvider vertexConsumer, int light) {
        BlockRenderType blockRenderType = state.getRenderType();
        if (blockRenderType == BlockRenderType.INVISIBLE) {
            return;
        }
        if (blockRenderType == BlockRenderType.MODEL) {
            VertexConsumer vertex = vertexConsumer.getBuffer(RenderLayer.getOutline(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE))
                    .color(0.3f, 0.5f, 0.5f, 1);
            BakedModel bakedModel = MinecraftClient.getInstance().getBlockRenderManager().getModel(state);
            MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer().render(matrices.peek(), vertex, state, bakedModel, 1, 1, 1, light, 0);
        }
    }
}

