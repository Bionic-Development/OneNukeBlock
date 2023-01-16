package de.takacick.imagineanything.registry.entity.living.renderer;

import de.takacick.imagineanything.registry.entity.living.EndPortalEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EndPortalFrameBlock;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.random.Random;

public class EndPortalEntityRenderer extends EntityRenderer<EndPortalEntity> {

    private final BlockRenderManager blockRenderManager;

    public EndPortalEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.blockRenderManager = ctx.getBlockRenderManager();
    }

    @Override
    public void render(EndPortalEntity endPortalEntity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {

        matrices.push();

        matrices.translate(-0.5, 0, -0.5);
        for (int x = -1; x < 2; x++) {
            for (int z = -1; z < 2; z++) {
                matrices.push();
                matrices.translate(x, 0, z);
                Matrix4f matrix4f = matrices.peek().getPositionMatrix();
                this.renderSides(matrix4f, vertexConsumers.getBuffer(RenderLayer.getEndPortal()));
                matrices.pop();
            }
        }

        matrices.translate(-2, 0, -2);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getCutout());
        Random random = endPortalEntity.getRandom();
        BlockState defaultState = Blocks.END_PORTAL_FRAME.getDefaultState().with(EndPortalFrameBlock.FACING, Direction.EAST).with(EndPortalFrameBlock.EYE, true);
        BakedModel bakedModel = blockRenderManager.getModels().getModel(defaultState);
        render(matrices, vertexConsumers, endPortalEntity, bakedModel, defaultState, random, 0, 0, 1, light);
        render(matrices, vertexConsumers, endPortalEntity, bakedModel, defaultState, random, 0, 0, 2, light);
        render(matrices, vertexConsumers, endPortalEntity, bakedModel, defaultState, random, 0, 0, 3, light);

        defaultState = Blocks.END_PORTAL_FRAME.getDefaultState().with(EndPortalFrameBlock.FACING, Direction.WEST).with(EndPortalFrameBlock.EYE, true);
        bakedModel = blockRenderManager.getModels().getModel(defaultState);
        render(matrices, vertexConsumers, endPortalEntity, bakedModel, defaultState, random, 4, 0, 1, light);
        render(matrices, vertexConsumers, endPortalEntity, bakedModel, defaultState, random, 4, 0, 2, light);
        render(matrices, vertexConsumers, endPortalEntity, bakedModel, defaultState, random, 4, 0, 3, light);

        defaultState = Blocks.END_PORTAL_FRAME.getDefaultState().with(EndPortalFrameBlock.FACING, Direction.SOUTH).with(EndPortalFrameBlock.EYE, true);
        bakedModel = blockRenderManager.getModels().getModel(defaultState);
        render(matrices, vertexConsumers, endPortalEntity, bakedModel, defaultState, random, 1, 0, 0, light);
        render(matrices, vertexConsumers, endPortalEntity, bakedModel, defaultState, random, 2, 0, 0, light);
        render(matrices, vertexConsumers, endPortalEntity, bakedModel, defaultState, random, 3, 0, 0, light);

        defaultState = Blocks.END_PORTAL_FRAME.getDefaultState().with(EndPortalFrameBlock.FACING, Direction.NORTH).with(EndPortalFrameBlock.EYE, true);
        bakedModel = blockRenderManager.getModels().getModel(defaultState);
        render(matrices, vertexConsumers, endPortalEntity, bakedModel, defaultState, random, 1, 0, 4, light);
        render(matrices, vertexConsumers, endPortalEntity, bakedModel, defaultState, random, 2, 0, 4, light);
        render(matrices, vertexConsumers, endPortalEntity, bakedModel, defaultState, random, 3, 0, 4, light);

        matrices.pop();

        super.render(endPortalEntity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumer, EndPortalEntity entity, BakedModel bakedModel, BlockState blockState, Random random, int x, int y, int z, int l) {
        matrices.push();
        matrices.translate(x, y, z);
        blockRenderManager.renderBlockAsEntity(blockState, matrices, vertexConsumer, l, OverlayTexture.DEFAULT_UV);
        matrices.pop();
    }

    private void renderSides(Matrix4f matrix, VertexConsumer vertexConsumer) {
        float f = this.getBottomYOffset();
        float g = this.getTopYOffset();
        this.renderSide(matrix, vertexConsumer, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, Direction.SOUTH);
        this.renderSide(matrix, vertexConsumer, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, Direction.NORTH);
        this.renderSide(matrix, vertexConsumer, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, Direction.EAST);
        this.renderSide(matrix, vertexConsumer, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, Direction.WEST);
        this.renderSide(matrix, vertexConsumer, 0.0f, 1.0f, f, f, 0.0f, 0.0f, 1.0f, 1.0f, Direction.DOWN);
        this.renderSide(matrix, vertexConsumer, 0.0f, 1.0f, g, g, 1.0f, 1.0f, 0.0f, 0.0f, Direction.UP);
    }

    private void renderSide(Matrix4f model, VertexConsumer vertices, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4, Direction side) {
        if (shouldDrawSide(side)) {
            vertices.vertex(model, x1, y1, z1).next();
            vertices.vertex(model, x2, y1, z2).next();
            vertices.vertex(model, x2, y2, z3).next();
            vertices.vertex(model, x1, y2, z4).next();
        }
    }

    public boolean shouldDrawSide(Direction direction) {
        return direction.getAxis() == Direction.Axis.Y;
    }

    protected float getTopYOffset() {
        return 0.75f;
    }

    protected float getBottomYOffset() {
        return 0.375f;
    }

    @Override
    public Identifier getTexture(EndPortalEntity endPortalEntity) {
        return null;
    }
}
