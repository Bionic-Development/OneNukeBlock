package de.takacick.tinyhouse.registry.block.entity.renderer;

import de.takacick.tinyhouse.registry.block.entity.GiantCrusherTrapBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.PistonHeadBlock;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;

public class GiantCrusherTrapBlockEntityRenderer implements BlockEntityRenderer<GiantCrusherTrapBlockEntity> {

    private final BlockRenderManager blockRenderManager;

    public GiantCrusherTrapBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.blockRenderManager = ctx.getRenderManager();
    }

    public void render(GiantCrusherTrapBlockEntity blockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int j) {
        if (!blockEntity.isOwner()) {
            return;
        }
        float size = 3f;

        Direction direction = Direction.DOWN;

        BlockState blockState = blockEntity.getCachedState();
        BlockState piston = Blocks.PISTON.getDefaultState()
                .with(PistonBlock.FACING,direction)
                .with(PistonBlock.EXTENDED, true);

        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getCutoutMipped());
        BakedModel bakedModel = blockRenderManager.getModels().getModel(piston);
        long l = blockState.getRenderingSeed(blockEntity.getPos());
        matrixStack.push();

        matrixStack.translate(0.5, 0.5, 0.5);
        matrixStack.scale(size, size, size);
        matrixStack.translate(-0.5, -0.5, -0.5);

        blockRenderManager.getModelRenderer().render(blockEntity.getWorld(), bakedModel, piston, blockEntity.getPos(),
                matrixStack, vertexConsumer, false, blockEntity.getWorld().getRandom(), Math.min(l, 7), OverlayTexture.DEFAULT_UV);
        matrixStack.pop();

        float extension = blockEntity.getExtensionLevel(f);

        piston = Blocks.PISTON_HEAD.getDefaultState()
                .with(PistonBlock.FACING,direction)
         .with(PistonHeadBlock.SHORT, extension < 2);

        vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getCutoutMipped());
        bakedModel = blockRenderManager.getModels().getModel(piston);
        matrixStack.push();
        matrixStack.translate(direction.getOffsetX() * extension, direction.getOffsetY() * extension, direction.getOffsetZ() * extension);
        matrixStack.translate(0.5, 0.5, 0.5);
        matrixStack.scale(size, size, size);
        matrixStack.translate(-0.5, -0.5, -0.5);

        blockRenderManager.getModelRenderer().render(blockEntity.getWorld(), bakedModel, piston, blockEntity.getPos(),
                matrixStack, vertexConsumer, false, blockEntity.getWorld().getRandom(), Math.min(l, 7), OverlayTexture.DEFAULT_UV);
        matrixStack.pop();
    }

    @Override
    public int getRenderDistance() {
        return BlockEntityRenderer.super.getRenderDistance() * 2;
    }

    @Override
    public boolean rendersOutsideBoundingBox(GiantCrusherTrapBlockEntity giantCrusherTrapBlockEntity) {
        return true;
    }
}
