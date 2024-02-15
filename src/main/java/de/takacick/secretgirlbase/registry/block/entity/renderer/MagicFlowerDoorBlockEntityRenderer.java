package de.takacick.secretgirlbase.registry.block.entity.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import de.takacick.secretgirlbase.SecretGirlBase;
import de.takacick.secretgirlbase.client.shaders.SecretGirlBaseLayers;
import de.takacick.secretgirlbase.client.shaders.vertex.TranslucentVertexConsumer;
import de.takacick.secretgirlbase.registry.block.entity.MagicFlowerDoorBlockEntity;
import de.takacick.secretgirlbase.registry.block.entity.model.MagicFlowerCircleModel;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Range;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.util.SortedSet;

public class MagicFlowerDoorBlockEntityRenderer implements BlockEntityRenderer<MagicFlowerDoorBlockEntity> {

    private static final Identifier INSIDE_TEXTURE = new Identifier(SecretGirlBase.MOD_ID, "textures/entity/magic_flower_door.png");
    private static final Identifier OUTSIDE_TEXTURE = new Identifier(SecretGirlBase.MOD_ID, "textures/entity/magic_flower_door_outside.png");

    private final BlockRenderManager blockRenderManager;
    private final MagicFlowerCircleModel magicFlowerCircleModel;

    public MagicFlowerDoorBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.blockRenderManager = ctx.getRenderManager();
        this.magicFlowerCircleModel = new MagicFlowerCircleModel(MagicFlowerCircleModel.getTexturedModelData().createModel());
    }

    public void render(MagicFlowerDoorBlockEntity magicFlowerDoorBlockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int j) {
        World world = magicFlowerDoorBlockEntity.getWorld();
        if (!magicFlowerDoorBlockEntity.isOwner() || world == null) {
            return;
        }

        float alpha = magicFlowerDoorBlockEntity.getAlpha(tickDelta);
        float prog = magicFlowerDoorBlockEntity.getProgress(tickDelta);
        float progress = prog - (1f - alpha) * 0.8f;

        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        VertexConsumerProvider.Immediate consumerProvider = VertexConsumerProvider.immediate(tessellator.getBuffer());

        if (alpha > 0) {
            MagicFlowerDoorBlockEntity.BLOCKS.forEach((vec3d, blockState) -> {
                if (world.getBlockEntity(magicFlowerDoorBlockEntity.getPos().add(BlockPos.ofFloored(vec3d)))
                        instanceof MagicFlowerDoorBlockEntity blockEntity) {

                    MatrixStack.Entry entry = matrixStack.peek();

                    BlockState state = blockEntity.getBlockState();
                    if (blockEntity.getWorld() == null || state.isAir()) {
                        return;
                    }

                    matrixStack.push();
                    matrixStack.translate(vec3d.getX(), vec3d.getY(), vec3d.getZ());

                    RenderLayer renderLayer = RenderLayers.getBlockLayer(state);

                    VertexConsumerProvider vertexConsumerProvider2;

                    SortedSet<BlockBreakingInfo> set = MinecraftClient.getInstance().worldRenderer.blockBreakingProgressions
                            .get(blockEntity.getPos().asLong());

                    if (set != null && set.last().getStage() >= 0) {
                        OverlayVertexConsumer vertexConsumer = new OverlayVertexConsumer(MinecraftClient.getInstance()
                                .getBufferBuilders().getEffectVertexConsumers().getBuffer(ModelLoader.BLOCK_DESTRUCTION_RENDER_LAYERS.get(set.last().getStage())),
                                entry.getPositionMatrix(), entry.getNormalMatrix(), 1.0f);

                        vertexConsumerProvider2 = layer -> {
                            VertexConsumer vertexConsumer2 = consumerProvider.getBuffer(layer);
                            if (layer.hasCrumbling()) {
                                return VertexConsumers.union(vertexConsumer, vertexConsumer2);
                            }
                            return vertexConsumer2;
                        };
                    } else {
                        vertexConsumerProvider2 = consumerProvider;
                    }

                    VertexConsumer vertexConsumer;

                    if (alpha < 1f) {
                        TranslucentVertexConsumer translucentVertexConsumer = new TranslucentVertexConsumer(vertexConsumerProvider2
                                .getBuffer(SecretGirlBaseLayers.getPoppyTranslucent(1f)));
                        translucentVertexConsumer.setAlpha(alpha);

                        vertexConsumer = translucentVertexConsumer;
                    } else if (progress > 0f) {
                        vertexConsumer = vertexConsumerProvider2.getBuffer(SecretGirlBaseLayers.getPoppyTranslucent(progress));
                    } else {
                        vertexConsumer = vertexConsumerProvider2.getBuffer(renderLayer);
                    }

                    renderBlock(state, blockEntity.getCachedState(), blockEntity.getPos(), blockEntity.getWorld(),
                            matrixStack, vertexConsumer, true, blockEntity.getWorld().getRandom());

                    matrixStack.pop();
                }
            });
            consumerProvider.draw();
            tessellator.getBuffer().clear();
        }

        if (progress > 0) {
            float time = MathHelper.lerpAngleDegrees(tickDelta, world.getTime(), world.getTime() + 1);

            matrixStack.push();
            matrixStack.translate(0.5f, 1.001f, 0.5f);
            matrixStack.scale(-4.8f, -4.8f, 4.8f);
            matrixStack.push();
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(time * 5f));
            this.magicFlowerCircleModel.render(matrixStack, consumerProvider.getBuffer(RenderLayer.getEntityTranslucent(INSIDE_TEXTURE)),
                    15728880, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 0.9f * progress);
            matrixStack.pop();

            matrixStack.push();
            matrixStack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(time * 15f));
            this.magicFlowerCircleModel.render(matrixStack, consumerProvider.getBuffer(RenderLayer.getEntityTranslucent(OUTSIDE_TEXTURE)),
                    15728880, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 0.9f * progress);
            matrixStack.pop();
            matrixStack.pop();

            consumerProvider.draw();
            tessellator.getBuffer().clear();

            matrixStack.translate(0.5f, 1.01f, 0.5f);
            matrixStack.push();
            matrixStack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(time * 15f));
            renderCylinder(matrixStack, 2.2f, 0.5f, 0xEA1CD0, progress * 0.5f, 360);
            matrixStack.pop();

            matrixStack.push();
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(time * 5f));
            renderCylinder(matrixStack, 1.35f, 0.3f, 0xEA1CD0, progress * 0.5f, 360);
            matrixStack.pop();
        }
    }

    public void renderBlock(BlockState state, BlockState renderState, BlockPos pos, BlockRenderView world, MatrixStack matrices, VertexConsumer vertexConsumer, boolean cull, Random random) {
        try {
            BlockRenderType blockRenderType = state.getRenderType();
            if (blockRenderType == BlockRenderType.MODEL) {
                this.blockRenderManager.getModelRenderer().render(world, this.blockRenderManager.getModel(state), renderState, pos, matrices, vertexConsumer, cull, random, state.getRenderingSeed(pos), OverlayTexture.DEFAULT_UV);
            }
        } catch (Throwable throwable) {
            CrashReport crashReport = CrashReport.create(throwable, "Tesselating block in world");
            CrashReportSection crashReportSection = crashReport.addElement("Block being tesselated");
            CrashReportSection.addBlockInfo(crashReportSection, world, pos, state);
            throw new CrashException(crashReport);
        }
    }

    @Override
    public int getRenderDistance() {
        return BlockEntityRenderer.super.getRenderDistance() * 2;
    }

    @Override
    public boolean rendersOutsideBoundingBox(MagicFlowerDoorBlockEntity magicFlowerDoorBlockEntity) {
        return true;
    }

    private static void setupRender() {
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(GL11.GL_LEQUAL);
    }

    private static void endRender() {
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    public static void renderCylinder(MatrixStack matrices, float radius, float height, int hexColor, float alpha, @Range(from = 4, to = 360) int segments) {
        segments = MathHelper.clamp(segments, 4, 360);

        Matrix4f matrix = matrices.peek().getPositionMatrix();
        Vector3f color = Vec3d.unpackRgb(hexColor).toVector3f();

        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        RenderSystem.depthMask(false);
        for (int i = 0; i < segments; i++) {
            float angle1 = (float) Math.toRadians(i * 360.0 / segments);
            float angle2 = (float) Math.toRadians((i + 1) * 360.0 / segments);

            float x1Outer = (MathHelper.cos(angle1) * radius);
            float z1Outer = (MathHelper.sin(angle1) * radius);

            float x2Outer = (MathHelper.cos(angle2) * radius);
            float z2Outer = MathHelper.sin(angle2) * radius;

            bufferBuilder.vertex(matrix, x1Outer, height, z1Outer).color(color.x(), color.y(), color.z(), 0.0F).next();
            bufferBuilder.vertex(matrix, x2Outer, height, z2Outer).color(color.x(), color.y(), color.z(), 0.0F).next();

            bufferBuilder.vertex(matrix, x2Outer, 0.0F, z2Outer).color(color.x(), color.y(), color.z(), alpha).next();
            bufferBuilder.vertex(matrix, x1Outer, 0.0F, z1Outer).color(color.x(), color.y(), color.z(), alpha).next();

            bufferBuilder.vertex(matrix, x1Outer, height, z1Outer).color(color.x(), color.y(), color.z(), 0.0F).next();
            bufferBuilder.vertex(matrix, x2Outer, height, z2Outer).color(color.x(), color.y(), color.z(), 0.0F).next();
            bufferBuilder.vertex(matrix, x2Outer, 0.0F, z2Outer).color(color.x(), color.y(), color.z(), alpha).next();
            bufferBuilder.vertex(matrix, x1Outer, 0.0F, z1Outer).color(color.x(), color.y(), color.z(), alpha).next();

            bufferBuilder.vertex(matrix, x1Outer, height, z1Outer).color(color.x(), color.y(), color.z(), 0.0F).next();
            bufferBuilder.vertex(matrix, x1Outer, 0.0F, z1Outer).color(color.x(), color.y(), color.z(), alpha).next();
            bufferBuilder.vertex(matrix, x2Outer, 0.0F, z2Outer).color(color.x(), color.y(), color.z(), alpha).next();
            bufferBuilder.vertex(matrix, x2Outer, height, z2Outer).color(color.x(), color.y(), color.z(), 0.0F).next();
        }

        setupRender();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        RenderSystem.depthMask(true);
        endRender();
    }
}