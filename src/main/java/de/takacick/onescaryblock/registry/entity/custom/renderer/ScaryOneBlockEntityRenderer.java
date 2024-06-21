package de.takacick.onescaryblock.registry.entity.custom.renderer;

import de.takacick.onescaryblock.client.item.model.ScaryOneBlockItemModel;
import de.takacick.onescaryblock.client.utils.GlowRenderer;
import de.takacick.onescaryblock.registry.ItemRegistry;
import de.takacick.onescaryblock.registry.entity.custom.ScaryOneBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class ScaryOneBlockEntityRenderer extends EntityRenderer<ScaryOneBlockEntity> {

    private final ScaryOneBlockItemModel scaryLuckyBlockItemModel;
    private final BlockRenderManager blockRenderManager;

    public ScaryOneBlockEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.shadowRadius = 0f;
        this.scaryLuckyBlockItemModel = new ScaryOneBlockItemModel(ScaryOneBlockItemModel.getTexturedModelData().createModel());
        this.blockRenderManager = context.getBlockRenderManager();
    }

    @Override
    public void render(ScaryOneBlockEntity scaryOneBlockEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        ItemStack itemStack = ItemRegistry.SCARY_ONE_BLOCK_ITEM.getDefaultStack();

        this.scaryLuckyBlockItemModel.setAngles(
                null,
                itemStack,
                scaryOneBlockEntity.age,
                g
        );
        this.scaryLuckyBlockItemModel.animateDying(
                null,
                itemStack,
                scaryOneBlockEntity.age + scaryOneBlockEntity.getWorld().getRandom().nextInt(scaryOneBlockEntity.getFuse() * 20),
                g
        );
        matrixStack.push();
        matrixStack.scale(-1f, -1f, 1f);
        matrixStack.translate(0f, -1.501f, 0f);

        RenderLayer renderLayer = this.scaryLuckyBlockItemModel.getLayer(this.scaryLuckyBlockItemModel.getTexture(itemStack));

        this.scaryLuckyBlockItemModel.render(matrixStack,
                vertexConsumerProvider.getBuffer(renderLayer),
                i, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
        matrixStack.pop();

        Random rotation = Random.create(scaryOneBlockEntity.age + 300 + scaryOneBlockEntity.getBlockPos().asLong());

        rotation.nextDouble();

        if (rotation.nextDouble() <= scaryOneBlockEntity.getFuseProgress(g)) {
            for (int x = 0; x < scaryOneBlockEntity.getRotationSpeed(); x++) {
                Random random = Random.create((rotation.nextDouble() <= 0.2 ? System.nanoTime() : scaryOneBlockEntity.age) + x + scaryOneBlockEntity.getBlockPos().asLong());
                Registries.BLOCK.getRandom(random).ifPresent(blockReference -> {
                    Block block = blockReference.value();
                    BlockState blockState = block.getStateManager().getStates().get(random.nextInt(block.getStateManager().getStates().size()));

                    matrixStack.push();
                    matrixStack.scale((float) (0.45 + rotation.nextGaussian() * 0.4f), (float) (0.45 + rotation.nextGaussian() * 0.4f), (float) (0.45 + rotation.nextGaussian() * 0.4f));
                    matrixStack.translate(-0.5, 0, -0.5);
                    matrixStack.multiply(RotationAxis.POSITIVE_X.rotation((float) rotation.nextGaussian() * 0.3f));
                    matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation((float) rotation.nextGaussian() * 0.3f));
                    matrixStack.multiply(RotationAxis.POSITIVE_Z.rotation((float) rotation.nextGaussian() * 0.3f));

                    matrixStack.translate(rotation.nextGaussian() * 0.1f,
                            rotation.nextDouble() * 1f,
                            rotation.nextGaussian() * 0.1f);

                    this.blockRenderManager.renderBlockAsEntity(blockState,
                            matrixStack, vertexConsumerProvider,
                            i, OverlayTexture.DEFAULT_UV);
                    matrixStack.pop();
                });
            }
        }

        float progress = scaryOneBlockEntity.getFuseProgress(g);
        if (progress > 0.8) {
            progress = (progress - 0.8f) * 10f;
            float age = scaryOneBlockEntity.age + g;
            float consumer = age ;
            float vertexConsumer2 = (3.25f) * 1.5f * progress;
            VertexConsumer vertexConsumer3 = vertexConsumerProvider.getBuffer(GlowRenderer.getShader());
            Random random = Random.create(5);

            Vector3f vector3f = Vec3d.unpackRgb(0x820A0A).toVector3f();
            float[] color = new float[]{vector3f.x(), vector3f.y(), vector3f.z()};

            matrixStack.push();
            matrixStack.translate(0, 0.5, 0);
            matrixStack.scale(vertexConsumer2, vertexConsumer2, vertexConsumer2);

            int l = 0;
            while ((float) l < 10) {
                matrixStack.push();
                matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(random.nextFloat() * 360.0f));
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(random.nextFloat() * 360.0f));
                matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(random.nextFloat() * 360.0f));
                matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(random.nextFloat() * 360.0f));
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(random.nextFloat() * 360.0f));
                matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(random.nextFloat() * 360.0f + consumer * 90.0f));
                float m = 1;
                float n = 1;
                Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
                int o = (int) (255.0f * (1.0f));

                GlowRenderer.method_23157(vertexConsumer3, matrix4f, color, o);
                GlowRenderer.method_23156(vertexConsumer3, matrix4f, color, m, n);
                GlowRenderer.method_23158(vertexConsumer3, matrix4f, color, m, n);
                GlowRenderer.method_23157(vertexConsumer3, matrix4f, color, o);
                GlowRenderer.method_23158(vertexConsumer3, matrix4f, color, m, n);
                GlowRenderer.method_23159(vertexConsumer3, matrix4f, m, n);
                GlowRenderer.method_23157(vertexConsumer3, matrix4f, color, o);
                GlowRenderer.method_23159(vertexConsumer3, matrix4f, m, n);
                GlowRenderer.method_23156(vertexConsumer3, matrix4f, color, m, n);
                ++l;
                matrixStack.pop();
            }
            matrixStack.pop();
        }

        super.render(scaryOneBlockEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(ScaryOneBlockEntity scaryOneBlockEntity) {
        return ScaryOneBlockItemModel.TEXTURE;
    }
}

