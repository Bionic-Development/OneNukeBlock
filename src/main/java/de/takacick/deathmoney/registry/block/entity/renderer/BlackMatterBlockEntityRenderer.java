package de.takacick.deathmoney.registry.block.entity.renderer;

import de.takacick.deathmoney.registry.block.entity.BlackMatterBlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix4f;

public class BlackMatterBlockEntityRenderer implements BlockEntityRenderer<BlackMatterBlockEntity> {

    public BlackMatterBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(BlackMatterBlockEntity blackMatterBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i2, int j2) {
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        this.renderSides(matrix4f, vertexConsumerProvider.getBuffer(RenderLayer.getEndPortal()));
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
        vertices.vertex(model, x1, y1, z1).next();
        vertices.vertex(model, x2, y1, z2).next();
        vertices.vertex(model, x2, y2, z3).next();
        vertices.vertex(model, x1, y2, z4).next();
    }

    protected float getTopYOffset() {
        return 1.0f;
    }

    protected float getBottomYOffset() {
        return 0.0f;
    }

    @Override
    public int getRenderDistance() {
        return 256;
    }
}

