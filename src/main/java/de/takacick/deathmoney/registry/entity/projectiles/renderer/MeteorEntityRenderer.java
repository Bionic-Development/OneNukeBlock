package de.takacick.deathmoney.registry.entity.projectiles.renderer;

import de.takacick.deathmoney.registry.entity.projectiles.MeteorEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.random.Random;

public class MeteorEntityRenderer extends EntityRenderer<MeteorEntity> {
    private final BlockRenderManager blockRenderManager;

    public MeteorEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.shadowRadius = 0f;
        this.blockRenderManager = context.getBlockRenderManager();
    }

    @Override
    public void render(MeteorEntity meteorEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        if (meteorEntity.getWorld() == null) {
            return;
        }

        float size = 4f;
        matrixStack.push();

        matrixStack.translate(0, 1, 0);
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.lerp(g, meteorEntity.prevYaw, meteorEntity.getYaw()) - 90.0F));
        matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.lerp(g, meteorEntity.prevPitch, meteorEntity.getPitch()) - 45.0F));
        matrixStack.multiply(Vec3f.NEGATIVE_Z.getRadialQuaternion(MathHelper.lerp(g, meteorEntity.age - 1 + meteorEntity.getId(), meteorEntity.age + meteorEntity.getId()) / 2));
        matrixStack.translate(0, -1, 0);

        matrixStack.scale(size, size, size);
        matrixStack.translate(-0.5, 0, -0.5);

        BlockState blockState = Blocks.MAGMA_BLOCK.getDefaultState();

        BlockPos blockPos = new BlockPos(meteorEntity.getX(), meteorEntity.getBoundingBox().maxY, meteorEntity.getZ());
        this.blockRenderManager.getModelRenderer().render(meteorEntity.getWorld(), this.blockRenderManager.getModel(blockState), blockState, blockPos, matrixStack, vertexConsumerProvider.getBuffer(RenderLayers.getMovingBlockLayer(blockState)), false, Random.create(), 1, OverlayTexture.DEFAULT_UV);
        matrixStack.pop();
    }

    @Override
    public Identifier getTexture(MeteorEntity meteorEntity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }
}

