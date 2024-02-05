package de.takacick.secretcraftbase.registry.entity.custom.renderer;

import de.takacick.secretcraftbase.registry.entity.custom.AbstractBlockEntity;
import de.takacick.secretcraftbase.server.datatracker.BezierCurve;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AbstractBlockEntityRenderer extends EntityRenderer<AbstractBlockEntity> {
    private final BlockRenderManager blockRenderManager;

    public AbstractBlockEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.shadowRadius = 0f;
        this.blockRenderManager = context.getBlockRenderManager();
    }

    @Override
    public void render(AbstractBlockEntity abstractBlockEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        BezierCurve bezierCurve = abstractBlockEntity.getBezierCurve();
        float scale = abstractBlockEntity.getSize(g);

        if (abstractBlockEntity.getBlockState().equals(abstractBlockEntity.getWorld().getBlockState(BlockPos.ofFloored(bezierCurve.getEnd())))
                && scale >= 1f) {
            return;
        }

        matrixStack.push();

        matrixStack.translate(0, -0.375, 0);

        matrixStack.translate(0, (1f - scale) * 0.5, 0);

        matrixStack.scale(scale, scale, scale);
        matrixStack.translate(-0.5, 0, -0.5);

        BlockState blockState = abstractBlockEntity.getBlockState();
        renderBlockAsEntity(abstractBlockEntity.getWorld(), blockState, abstractBlockEntity.getFromBlockPos(), matrixStack, vertexConsumerProvider, i, OverlayTexture.DEFAULT_UV, blockState.getRenderingSeed(abstractBlockEntity.getFromBlockPos()));
        matrixStack.pop();
        super.render(abstractBlockEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(AbstractBlockEntity abstractBlockEntity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }

    public void renderBlockAsEntity(World world, BlockState state, BlockPos blockPos, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, long seed) {
        BlockRenderType blockRenderType = state.getRenderType();
        if (blockRenderType == BlockRenderType.INVISIBLE) {
            return;
        }
        switch (blockRenderType) {
            case MODEL: {
                BakedModel bakedModel = this.blockRenderManager.getModel(state);
                int i = MinecraftClient.getInstance().getBlockColors().getColor(state, world, blockPos, 0);
                float f = (float) (i >> 16 & 0xFF) / 255.0f;
                float g = (float) (i >> 8 & 0xFF) / 255.0f;
                float h = (float) (i & 0xFF) / 255.0f;
                render(matrices.peek(), vertexConsumers.getBuffer(RenderLayers.getEntityBlockLayer(state, false)), state, bakedModel, f, g, h, light, overlay, seed);
                break;
            }
        }
    }

    private void render(MatrixStack.Entry entry, VertexConsumer vertexConsumer, @Nullable BlockState state, BakedModel bakedModel, float red, float green, float blue, int light, int overlay, long seed) {
        Random random = Random.create();
        for (Direction direction : Direction.values()) {
            random.setSeed(seed);
            renderQuads(entry, vertexConsumer, red, green, blue, bakedModel.getQuads(state, direction, random), light, overlay);
        }
        random.setSeed(seed);
        renderQuads(entry, vertexConsumer, red, green, blue, bakedModel.getQuads(state, null, random), light, overlay);
    }

    private void renderQuads(MatrixStack.Entry entry, VertexConsumer vertexConsumer, float red, float green, float blue, List<BakedQuad> quads, int light, int overlay) {
        for (BakedQuad bakedQuad : quads) {
            float h;
            float g;
            float f;
            if (bakedQuad.hasColor()) {
                f = MathHelper.clamp(red, 0.0f, 1.0f);
                g = MathHelper.clamp(green, 0.0f, 1.0f);
                h = MathHelper.clamp(blue, 0.0f, 1.0f);
            } else {
                f = 1.0f;
                g = 1.0f;
                h = 1.0f;
            }
            vertexConsumer.quad(entry, bakedQuad, f, g, h, light, overlay);
        }
    }
}

