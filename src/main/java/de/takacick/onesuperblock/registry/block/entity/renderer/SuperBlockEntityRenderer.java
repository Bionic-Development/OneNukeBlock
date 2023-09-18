package de.takacick.onesuperblock.registry.block.entity.renderer;

import de.takacick.onesuperblock.client.render.SuperRenderLayers;
import de.takacick.onesuperblock.registry.block.entity.SuperBlockEntity;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.BitSet;
import java.util.List;

public class SuperBlockEntityRenderer implements BlockEntityRenderer<SuperBlockEntity> {
    private static final Direction[] DIRECTIONS = Direction.values();

    private final BlockRenderManager blockRenderManager;

    public SuperBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.blockRenderManager = ctx.getRenderManager();
    }

    public void render(SuperBlockEntity superBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int j) {
        World world = superBlockEntity.getWorld() == null ? MinecraftClient.getInstance().world : superBlockEntity.getWorld();
        if (world == null) {
            return;
        }

        float age = world.getTime() +
                Random.create(superBlockEntity.getPos().asLong()).nextInt(200000) + f;

        Random random = Random.create((long) (age / 10f));

        Block block = Registry.BLOCK.getRandom(random).get().value();
        BlockState blockState = block.getDefaultState();

        for (int i = 0; i < 16; i++) {
            if (block.getTranslationKey().contains("minecraft")
                    && blockState.getMaterial().blocksMovement()
                    && !(block instanceof TrapdoorBlock || block instanceof SlabBlock
                    || block instanceof PressurePlateBlock || block instanceof PlantBlock
                    || block instanceof Fertilizable
                    || block instanceof HorizontalConnectingBlock
                    || block instanceof PistonHeadBlock
                    || block instanceof StainedGlassBlock
                    || block instanceof PointedDripstoneBlock
                    || block instanceof CoralParentBlock || block instanceof DoorBlock)
                    && block.getRenderType(blockState) == BlockRenderType.MODEL) {
                break;
            }

            block = Registry.BLOCK.getRandom(random).get().value();
            blockState = block.getDefaultState();
        }

        matrixStack.push();
        float size = 0.5f;
        matrixStack.translate(0.5, 0.25, 0.5);
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(age * 7.5f));
        matrixStack.translate(-0.25, 0, -0.25);

        matrixStack.scale(size, size, size);

        BakedModel bakedModel = blockRenderManager.getModels().getModel(blockState);
        long l = blockState.getRenderingSeed(superBlockEntity.getPos());

        vertexConsumerProvider = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();

        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayers.getBlockLayer(blockState));
        renderFlat(world, bakedModel, blockState, superBlockEntity.getPos(),
                matrixStack, vertexConsumer, false, Random.create(superBlockEntity.getPos().asLong()), Math.min(l, 7),
                OverlayTexture.DEFAULT_UV);

        vertexConsumer = vertexConsumerProvider.getBuffer(SuperRenderLayers.getBlockOverlay());
        renderFlat(world, bakedModel, blockState, superBlockEntity.getPos(),
                matrixStack, vertexConsumer, false, Random.create(superBlockEntity.getPos().asLong()),
                Math.min(l, 7), OverlayTexture.DEFAULT_UV);
        matrixStack.pop();
    }

    public void renderFlat(BlockRenderView world, BakedModel model, BlockState state, BlockPos pos, MatrixStack matrices, VertexConsumer vertexConsumer, boolean cull, Random random, long seed, int overlay) {
        BitSet bitSet = new BitSet(3);
        BlockPos.Mutable mutable = pos.mutableCopy();
        for (Direction direction : DIRECTIONS) {
            random.setSeed(seed);
            List<BakedQuad> list = model.getQuads(state, direction, random);
            if (list.isEmpty()) continue;
            mutable.set(pos, direction);
            if (cull && !Block.shouldDrawSide(state, world, pos, direction, mutable)) continue;
            int i = 0xF000F0;
            this.renderQuadsFlat(world, state, pos, i, overlay, false, matrices, vertexConsumer, list, bitSet);
        }
        random.setSeed(seed);
        List<BakedQuad> list2 = model.getQuads(state, null, random);
        if (!list2.isEmpty()) {
            this.renderQuadsFlat(world, state, pos, -1, overlay, true, matrices, vertexConsumer, list2, bitSet);
        }
    }

    private void renderQuadsFlat(BlockRenderView world, BlockState state, BlockPos pos, int light, int overlay, boolean useWorldLight, MatrixStack matrices, VertexConsumer vertexConsumer, List<BakedQuad> quads, BitSet flags) {
        for (BakedQuad bakedQuad : quads) {
            if (useWorldLight) {
                this.getQuadDimensions(world, state, pos, bakedQuad.getVertexData(), bakedQuad.getFace(), null, flags);
                light = 0xF000F0;
            }
            float f = world.getBrightness(bakedQuad.getFace(), bakedQuad.hasShade());
            this.renderQuad(world, state, pos, vertexConsumer, matrices.peek(), bakedQuad, f, f, f, f, light, light, light, light, overlay);
        }
    }

    private void renderQuad(BlockRenderView world, BlockState state, BlockPos pos, VertexConsumer vertexConsumer, MatrixStack.Entry matrixEntry, BakedQuad quad, float brightness0, float brightness1, float brightness2, float brightness3, int light0, int light1, int light2, int light3, int overlay) {
        float h;
        float g;
        float f;
        if (quad.hasColor()) {
            int i = MinecraftClient.getInstance().getBlockColors().getColor(state, world, pos, quad.getColorIndex());
            f = (float) (i >> 16 & 0xFF) / 255.0f;
            g = (float) (i >> 8 & 0xFF) / 255.0f;
            h = (float) (i & 0xFF) / 255.0f;
        } else {
            f = 1.0f;
            g = 1.0f;
            h = 1.0f;
        }
        vertexConsumer.quad(matrixEntry, quad, new float[]{brightness0, brightness1, brightness2, brightness3}, f, g, h, new int[]{light0, light1, light2, light3}, overlay, true);
    }

    private void getQuadDimensions(BlockRenderView world, BlockState state, BlockPos pos, int[] vertexData, Direction face, @Nullable float[] box, BitSet flags) {
        float m;
        int l;
        float f = 32.0f;
        float g = 32.0f;
        float h = 32.0f;
        float i = -32.0f;
        float j = -32.0f;
        float k = -32.0f;
        for (l = 0; l < 4; ++l) {
            m = Float.intBitsToFloat(vertexData[l * 8]);
            float n = Float.intBitsToFloat(vertexData[l * 8 + 1]);
            float o = Float.intBitsToFloat(vertexData[l * 8 + 2]);
            f = Math.min(f, m);
            g = Math.min(g, n);
            h = Math.min(h, o);
            i = Math.max(i, m);
            j = Math.max(j, n);
            k = Math.max(k, o);
        }
        if (box != null) {
            box[Direction.WEST.getId()] = f;
            box[Direction.EAST.getId()] = i;
            box[Direction.DOWN.getId()] = g;
            box[Direction.UP.getId()] = j;
            box[Direction.NORTH.getId()] = h;
            box[Direction.SOUTH.getId()] = k;
            l = DIRECTIONS.length;
            box[Direction.WEST.getId() + l] = 1.0f - f;
            box[Direction.EAST.getId() + l] = 1.0f - i;
            box[Direction.DOWN.getId() + l] = 1.0f - g;
            box[Direction.UP.getId() + l] = 1.0f - j;
            box[Direction.NORTH.getId() + l] = 1.0f - h;
            box[Direction.SOUTH.getId() + l] = 1.0f - k;
        }
        float p = 1.0E-4f;
        m = 0.9999f;
        switch (face) {
            case DOWN: {
                flags.set(1, f >= 1.0E-4f || h >= 1.0E-4f || i <= 0.9999f || k <= 0.9999f);
                flags.set(0, g == j && (g < 1.0E-4f || state.isFullCube(world, pos)));
                break;
            }
            case UP: {
                flags.set(1, f >= 1.0E-4f || h >= 1.0E-4f || i <= 0.9999f || k <= 0.9999f);
                flags.set(0, g == j && (j > 0.9999f || state.isFullCube(world, pos)));
                break;
            }
            case NORTH: {
                flags.set(1, f >= 1.0E-4f || g >= 1.0E-4f || i <= 0.9999f || j <= 0.9999f);
                flags.set(0, h == k && (h < 1.0E-4f || state.isFullCube(world, pos)));
                break;
            }
            case SOUTH: {
                flags.set(1, f >= 1.0E-4f || g >= 1.0E-4f || i <= 0.9999f || j <= 0.9999f);
                flags.set(0, h == k && (k > 0.9999f || state.isFullCube(world, pos)));
                break;
            }
            case WEST: {
                flags.set(1, g >= 1.0E-4f || h >= 1.0E-4f || j <= 0.9999f || k <= 0.9999f);
                flags.set(0, f == i && (f < 1.0E-4f || state.isFullCube(world, pos)));
                break;
            }
            case EAST: {
                flags.set(1, g >= 1.0E-4f || h >= 1.0E-4f || j <= 0.9999f || k <= 0.9999f);
                flags.set(0, f == i && (i > 0.9999f || state.isFullCube(world, pos)));
            }
        }
    }

    @Override
    public int getRenderDistance() {
        return BlockEntityRenderer.super.getRenderDistance() * 2;
    }

    @Override
    public boolean rendersOutsideBoundingBox(SuperBlockEntity oneMegaBlockEntity) {
        return true;
    }
}
