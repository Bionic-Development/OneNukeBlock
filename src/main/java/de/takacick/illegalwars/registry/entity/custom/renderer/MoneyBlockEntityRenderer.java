package de.takacick.illegalwars.registry.entity.custom.renderer;

import de.takacick.illegalwars.registry.ItemRegistry;
import de.takacick.illegalwars.registry.entity.custom.MoneyBlockEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
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
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.List;

public class MoneyBlockEntityRenderer extends EntityRenderer<MoneyBlockEntity> {

    private final BlockRenderManager blockRenderManager;

    public MoneyBlockEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.shadowRadius = 0.5f;
        this.blockRenderManager = context.getBlockRenderManager();
    }

    @Override
    public void render(MoneyBlockEntity moneyBlockEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        int j = moneyBlockEntity.getFuse();
        if ((float) j - g + 1.0f < 10.0f) {
            float h = 1.0f - ((float) j - g + 1.0f) / 10.0f;
            h = MathHelper.clamp(h, 0.0f, 1.0f);
            h *= h;
            h *= h;
            float k = 1.0f + h * 0.3f;
            matrixStack.scale(k, k, k);
        }

        BlockState blockState = ItemRegistry.MONEY_BLOCK.getDefaultState();

        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(moneyBlockEntity.getRotation(g)));
        matrixStack.translate(-0.5, 0, -0.5);

        renderFlashingBlock(moneyBlockEntity.getWorld(), blockState, moneyBlockEntity.getFromBlockPos(), matrixStack, vertexConsumerProvider, i, j / 5 % 2 == 0, blockState.getRenderingSeed(moneyBlockEntity.getFromBlockPos()));
        matrixStack.pop();
        super.render(moneyBlockEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public void renderFlashingBlock(World world, BlockState state, BlockPos blockPos, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, boolean drawFlash, long seed) {
        int overlay = drawFlash ? OverlayTexture.packUv(OverlayTexture.getU(1.0f), 10) : OverlayTexture.DEFAULT_UV;

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
                render(matrices.peek(), vertexConsumers.getBuffer(RenderLayers.getEntityBlockLayer(state, false)), bakedModel, f, g, h, light, overlay, seed);
                break;
            }
        }
    }

    private void render(MatrixStack.Entry entry, VertexConsumer vertexConsumer, BakedModel bakedModel, float red, float green, float blue, int light, int overlay, long seed) {
        Random random = Random.create();
        for (Direction direction : Direction.values()) {
            random.setSeed(seed);
            renderQuads(entry, vertexConsumer, red, green, blue, bakedModel.getQuads(null, direction, random), light, overlay);
        }
        random.setSeed(seed);
        renderQuads(entry, vertexConsumer, red, green, blue, bakedModel.getQuads(null, null, random), light, overlay);
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

    @Override
    public Identifier getTexture(MoneyBlockEntity colorBombEntity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }
}

