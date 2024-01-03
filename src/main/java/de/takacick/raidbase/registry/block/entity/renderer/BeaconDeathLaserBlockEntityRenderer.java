package de.takacick.raidbase.registry.block.entity.renderer;

import de.takacick.raidbase.RaidBase;
import de.takacick.raidbase.registry.block.entity.BeaconDeathLaserBlockEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.joml.Vector3f;

import java.util.List;

public class BeaconDeathLaserBlockEntityRenderer implements BlockEntityRenderer<BeaconDeathLaserBlockEntity> {

    public static final Identifier LASER = new Identifier(RaidBase.MOD_ID, "textures/entity/laser.png");

    private final BlockRenderManager blockRenderManager;

    private final ModelPart laser;
    private final ModelPart innerLaser;

    public BeaconDeathLaserBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.blockRenderManager = ctx.getRenderManager();

        this.laser = getLaserTexturedModelData().createModel().getChild("laser");
        this.innerLaser = getLaserTexturedModelData().createModel().getChild("innerLaser");
    }

    @Override
    public void render(BeaconDeathLaserBlockEntity blockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int j) {
        Vec3d offset = blockEntity.getOffset();

        float yaw = blockEntity.getYaw(tickDelta);
        float pitch = blockEntity.getPitch();

        matrixStack.push();
        matrixStack.translate(0, -0.5, 0);
        matrixStack.translate(offset.getX(), offset.getY(), offset.getZ());

        matrixStack.translate(0.5, 0.5, 0.5);
        matrixStack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(yaw));
        matrixStack.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(pitch));
        matrixStack.translate(-0.5, -0.5, -0.5);


        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getCutoutMipped());
        BakedModel bakedModel = blockRenderManager.getModels().getModel(blockEntity.getCachedState());

        renderBakedItemQuads(matrixStack, vertexConsumer, bakedModel
                .getQuads(null, null, Random.create()), light, OverlayTexture.DEFAULT_UV);
        matrixStack.pop();

        float y = Math.max(blockEntity.getLength() - 0.2f, 0f);

        this.laser.resetTransform();
        this.innerLaser.resetTransform();

        matrixStack.push();
        matrixStack.translate(0.5, 0, 0.5);
        matrixStack.translate(offset.getX(), offset.getY(), offset.getZ());

        matrixStack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(yaw));
        matrixStack.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(pitch));

        matrixStack.scale(-1, -1, 1);

        matrixStack.scale(0.25f, 0.25f, 0.25f);

        matrixStack.push();
        this.innerLaser.scale(new Vector3f(0f, 0f, y * 4));
        this.innerLaser.render(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getBeaconBeam(LASER, false)), light, OverlayTexture.DEFAULT_UV, 1f, 0f, 0, 1f);
        matrixStack.pop();

        matrixStack.push();
        this.laser.scale(new Vector3f(0f, 0f, y * 4));
        this.laser.render(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getBeaconBeam(LASER, true)), light, OverlayTexture.DEFAULT_UV, 1f, 0f, 0, 0.125F);
        matrixStack.pop();
        matrixStack.pop();
    }

    @Override
    public int getRenderDistance() {
        return 256;
    }

    public static TexturedModelData getLaserTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData laser = modelPartData.addChild("laser", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 4.0F, 0.0F));

        ModelPartData laser_r1 = laser.addChild("laser_r1", ModelPartBuilder.create().uv(13, 6).cuboid(-3.0F, 1.0F, 0.0F, 0.0F, 6.0F, 16.0F, new Dilation(0.0F))
                .uv(13, 6).cuboid(-3.0F, 1.0F, 0.0F, 6.0F, 6.0F, 0.0F, new Dilation(0.0F))
                .uv(13, 6).cuboid(3.0F, 1.0F, 0.0F, 0.0F, 6.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

        ModelPartData laser_r2 = laser.addChild("laser_r2", ModelPartBuilder.create().uv(13, 6).cuboid(-3.0F, 1.0F, 8.0F, 6.0F, 6.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -8.0F, 3.1416F, 0.0F, 0.0F));

        ModelPartData laser_r3 = laser.addChild("laser_r3", ModelPartBuilder.create().uv(13, 6).cuboid(3.0F, -6.0F, 0.0F, 0.0F, 6.0F, 16.0F, new Dilation(0.0F))
                .uv(13, 6).cuboid(-3.0F, -6.0F, 0.0F, 0.0F, 6.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, -4.0F, 0.0F, 3.1416F, 0.0F, 1.5708F));

        ModelPartData innerLaser = modelPartData.addChild("innerLaser", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 4.0F, 0.0F));

        ModelPartData laser_r4 = innerLaser.addChild("laser_r4", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, 2.0F, 0.0F, 4.0F, 4.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 3.1416F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public boolean rendersOutsideBoundingBox(BeaconDeathLaserBlockEntity blockEntity) {
        return true;
    }

    protected void renderBakedItemQuads(MatrixStack matrices, VertexConsumer vertices, List<BakedQuad> quads, int light, int overlay) {
        MatrixStack.Entry entry = matrices.peek();
        for (BakedQuad bakedQuad : quads) {
            int i = -1;
            float f = (float) (i >> 16 & 0xFF) / 255.0f;
            float g = (float) (i >> 8 & 0xFF) / 255.0f;
            float h = (float) (i & 0xFF) / 255.0f;
            vertices.quad(entry, bakedQuad, f, g, h, light, overlay);
        }
    }
}

