package de.takacick.immortalmobs.registry.block.entity.renderer;

import de.takacick.immortalmobs.ImmortalMobs;
import de.takacick.immortalmobs.client.CustomLayers;
import de.takacick.immortalmobs.registry.block.entity.ImmortalChainTrapBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ImmortalChainTrapBlockEntityRenderer implements BlockEntityRenderer<ImmortalChainTrapBlockEntity> {

    private static final Identifier TEXTURE = new Identifier(ImmortalMobs.MOD_ID, "textures/entity/immortal_wool.png");

    public ImmortalChainTrapBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {

    }

    @Override
    public void render(ImmortalChainTrapBlockEntity immortalChainTrapBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {

        World world = immortalChainTrapBlockEntity.getWorld() == null ? MinecraftClient.getInstance().world : immortalChainTrapBlockEntity.getWorld();

        if (immortalChainTrapBlockEntity.getWorld() == null) {
            MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(Blocks.IRON_BLOCK.getDefaultState(), matrixStack, vertexConsumerProvider, i, j);
        } else {
            BlockState state = Blocks.IRON_BLOCK.getDefaultState();
            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getCutout());
            BakedModel bakedModel = MinecraftClient.getInstance().getBlockRenderManager().getModels().getModel(Blocks.IRON_BLOCK.getDefaultState());
            long l = state.getRenderingSeed(immortalChainTrapBlockEntity.getPos());
            MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer().render(world, bakedModel, state, immortalChainTrapBlockEntity.getPos(),
                    matrixStack, vertexConsumer, false, world.getRandom(), l, OverlayTexture.DEFAULT_UV);
        }

        Vec3f color = new Vec3f(0.4029412f, 0.14705883f, 0.5411765f);

        renderBlockAsEntity(immortalChainTrapBlockEntity.getCachedState(), matrixStack, vertexConsumerProvider, i, OverlayTexture.DEFAULT_UV, color.getX(), color.getY(), color.getZ());
        renderBlockAsEntity(immortalChainTrapBlockEntity.getCachedState(), matrixStack, vertexConsumerProvider, i, OverlayTexture.DEFAULT_UV, color.getX(), color.getY(), color.getZ());
        renderBlockAsEntity(immortalChainTrapBlockEntity.getCachedState(), matrixStack, vertexConsumerProvider, i, OverlayTexture.DEFAULT_UV, color.getX(), color.getY(), color.getZ());
    }

    public void renderBlockAsEntity(BlockState state, MatrixStack matrices, VertexConsumerProvider vertexConsumer, int light, int overlay, float r, float g, float b) {
        BakedModel bakedModel = MinecraftClient.getInstance().getBlockRenderManager().getModel(state);
        int i = -1;
        render(matrices.peek(), vertexConsumer, state, bakedModel, r, g, b, light, overlay);
    }

    public void render(MatrixStack.Entry entry, VertexConsumerProvider vertexConsumer, @Nullable BlockState state, BakedModel bakedModel, float red, float green, float blue, int light, int overlay) {
        Random random = Random.create();
        long l = 42L;
        for (Direction direction : Direction.values()) {
            random.setSeed(42L);
            renderQuads(entry, vertexConsumer, red, green, blue, bakedModel.getQuads(state, direction, random), light, overlay);
        }
        random.setSeed(42L);
        renderQuads(entry, vertexConsumer, red, green, blue, bakedModel.getQuads(state, null, random), light, overlay);
    }

    private static void renderQuads(MatrixStack.Entry entry, VertexConsumerProvider vertexConsumer, float red, float green, float blue, List<BakedQuad> quads, int light, int overlay) {
        VertexConsumer vertex = vertexConsumer.getBuffer(CustomLayers.IMMORTAL_CUTOUT.apply(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE));
        boolean lastColor = false;

        for (BakedQuad bakedQuad : quads) {
            if (bakedQuad.hasColor()) {
                if (!lastColor) {
                    vertex = vertexConsumer.getBuffer(RenderLayer.getEyes(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE));
                    lastColor = true;
                }
            } else if (lastColor) {
                vertex = vertexConsumer.getBuffer(CustomLayers.IMMORTAL_CUTOUT.apply(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE));
                lastColor = false;
            }
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
            vertex.quad(entry, bakedQuad, f, g, h, light, overlay);
        }
    }

}

