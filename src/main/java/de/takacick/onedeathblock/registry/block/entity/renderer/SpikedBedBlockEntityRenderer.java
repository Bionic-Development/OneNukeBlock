package de.takacick.onedeathblock.registry.block.entity.renderer;

import de.takacick.onedeathblock.OneDeathBlock;
import de.takacick.onedeathblock.registry.block.entity.SpikedBedBlockEntity;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.block.entity.BedBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.BedPart;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.LightmapCoordinatesRetriever;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

public class SpikedBedBlockEntityRenderer implements BlockEntityRenderer<SpikedBedBlockEntity> {

    private static final Identifier TEXTURE = new Identifier(OneDeathBlock.MOD_ID, "textures/entity/spiked_bed.png");

    private final ModelPart bedHead;
    private final ModelPart bedFoot;
    private final ModelPart bedHeadSpikes;
    private final ModelPart bedFootSpikes;

    public SpikedBedBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.bedHead = ctx.getLayerModelPart(EntityModelLayers.BED_HEAD);
        this.bedFoot = ctx.getLayerModelPart(EntityModelLayers.BED_FOOT);
        this.bedHeadSpikes = getHeadTexturedModelData().createModel();
        this.bedFootSpikes = getFootTexturedModelData().createModel();
    }

    @Override
    public void render(SpikedBedBlockEntity bedBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        SpriteIdentifier spriteIdentifier = TexturedRenderLayers.BED_TEXTURES[bedBlockEntity.getColor().getId()];
        World world2 = bedBlockEntity.getWorld();
        if (world2 != null) {
            BlockState blockState = bedBlockEntity.getCachedState();
            DoubleBlockProperties.PropertySource<BedBlockEntity> propertySource = DoubleBlockProperties.toPropertySource(BlockEntityType.BED, BedBlock::getBedPart, BedBlock::getOppositePartDirection, ChestBlock.FACING, blockState, world2, bedBlockEntity.getPos(), (world, pos) -> false);
            int k = propertySource.apply(new LightmapCoordinatesRetriever<>()).get(i);
            this.renderPart(matrixStack, vertexConsumerProvider, blockState.get(BedBlock.PART) == BedPart.HEAD ? this.bedHead : this.bedFoot, blockState.get(BedBlock.FACING), spriteIdentifier, k, j, false);
            this.renderPart(matrixStack, vertexConsumerProvider, blockState.get(BedBlock.PART) == BedPart.HEAD ? this.bedHeadSpikes : this.bedFootSpikes, blockState.get(BedBlock.FACING), k, j, false);
        } else {
            this.renderPart(matrixStack, vertexConsumerProvider, this.bedHead, Direction.SOUTH, spriteIdentifier, i, j, false);
            this.renderPart(matrixStack, vertexConsumerProvider, this.bedHeadSpikes, Direction.SOUTH, i, j, false);
            this.renderPart(matrixStack, vertexConsumerProvider, this.bedFoot, Direction.SOUTH, spriteIdentifier, i, j, true);
            this.renderPart(matrixStack, vertexConsumerProvider, this.bedFootSpikes, Direction.SOUTH, i, j, true);
        }
    }

    private void renderPart(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ModelPart part, Direction direction, SpriteIdentifier sprite, int light, int overlay, boolean isFoot) {
        matrices.push();
        matrices.translate(0.0, 0.5625, isFoot ? -1.0 : 0.0);
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90.0f));
        matrices.translate(0.5, 0.5, 0.5);
        matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0f + direction.asRotation()));
        matrices.translate(-0.5, -0.5, -0.5);
        VertexConsumer vertexConsumer = sprite.getVertexConsumer(vertexConsumers, RenderLayer::getEntitySolid);
        part.render(matrices, vertexConsumer, light, overlay);
        matrices.pop();
    }

    private void renderPart(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ModelPart part, Direction direction, int light, int overlay, boolean isFoot) {
        matrices.push();
        matrices.translate(0.0, 0.5625, isFoot ? -1.0 : 0.0);
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90.0f));
        matrices.translate(0.5, 0.5, 0.5);
        matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0f + direction.asRotation()));
        matrices.translate(-0.5, -0.5, -0.5);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(TEXTURE));
        part.render(matrices, vertexConsumer, light, overlay);
        matrices.pop();
    }

    public static TexturedModelData getHeadTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create(), ModelTransform.NONE);

        ModelPartData head_spikes = head.addChild("head_spikes", ModelPartBuilder.create(), ModelTransform.of(8.3F, 9.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

        ModelPartData head_spikes2 = head_spikes.addChild("head_spikes2", ModelPartBuilder.create(), ModelTransform.pivot(-3.0F, 0.0F, -1.0F));

        ModelPartData head_spikes_r1 = head_spikes2.addChild("head_spikes_r1", ModelPartBuilder.create().uv(2, 51).cuboid(1.3284F, -2.5F, -2.8284F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -2.5F, -1.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r2 = head_spikes2.addChild("head_spikes_r2", ModelPartBuilder.create().uv(2, 51).cuboid(-4.3284F, -2.5F, -2.8284F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -2.5F, -1.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes3 = head_spikes.addChild("head_spikes3", ModelPartBuilder.create(), ModelTransform.pivot(-3.0F, 0.0F, -1.0F));

        ModelPartData head_spikes_r3 = head_spikes3.addChild("head_spikes_r3", ModelPartBuilder.create().uv(2, 51).cuboid(1.3284F, -2.5F, -2.8284F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(5.5F, -2.5F, -1.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r4 = head_spikes3.addChild("head_spikes_r4", ModelPartBuilder.create().uv(2, 51).cuboid(-4.3284F, -2.5F, -2.8284F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(5.5F, -2.5F, -1.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes4 = head_spikes.addChild("head_spikes4", ModelPartBuilder.create(), ModelTransform.pivot(-6.0F, 0.0F, -4.0F));

        ModelPartData head_spikes_r5 = head_spikes4.addChild("head_spikes_r5", ModelPartBuilder.create().uv(2, 51).cuboid(1.3284F, -2.5F, -2.8284F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -2.5F, 5.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r6 = head_spikes4.addChild("head_spikes_r6", ModelPartBuilder.create().uv(2, 51).cuboid(-4.3284F, -2.5F, -2.8284F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -2.5F, 5.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes5 = head_spikes.addChild("head_spikes5", ModelPartBuilder.create(), ModelTransform.pivot(-3.0F, 0.0F, -4.0F));

        ModelPartData head_spikes_r7 = head_spikes5.addChild("head_spikes_r7", ModelPartBuilder.create().uv(2, 51).cuboid(1.3284F, -2.5F, -2.8284F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(8.5F, -2.5F, 5.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r8 = head_spikes5.addChild("head_spikes_r8", ModelPartBuilder.create().uv(2, 51).cuboid(-4.3284F, -2.5F, -2.8284F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(8.5F, -2.5F, 5.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes6 = head_spikes.addChild("head_spikes6", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, -4.0F));

        ModelPartData head_spikes_r9 = head_spikes6.addChild("head_spikes_r9", ModelPartBuilder.create().uv(2, 51).cuboid(1.3284F, -2.5F, -2.8284F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -2.5F, 5.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r10 = head_spikes6.addChild("head_spikes_r10", ModelPartBuilder.create().uv(2, 51).cuboid(-4.3284F, -2.5F, -2.8284F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -2.5F, 5.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes7 = head_spikes.addChild("head_spikes7", ModelPartBuilder.create(), ModelTransform.pivot(-3.0F, 0.0F, -1.0F));

        ModelPartData head_spikes_r11 = head_spikes7.addChild("head_spikes_r11", ModelPartBuilder.create().uv(2, 51).cuboid(1.3284F, -2.5F, -2.8284F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(5.5F, -2.5F, 5.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r12 = head_spikes7.addChild("head_spikes_r12", ModelPartBuilder.create().uv(2, 51).cuboid(-4.3284F, -2.5F, -2.8284F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(5.5F, -2.5F, 5.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes8 = head_spikes.addChild("head_spikes8", ModelPartBuilder.create(), ModelTransform.pivot(-3.0F, 0.0F, -1.0F));

        ModelPartData head_spikes_r13 = head_spikes8.addChild("head_spikes_r13", ModelPartBuilder.create().uv(2, 51).cuboid(1.3284F, -2.5F, -2.8284F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -2.5F, 5.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r14 = head_spikes8.addChild("head_spikes_r14", ModelPartBuilder.create().uv(2, 51).cuboid(-4.3284F, -2.5F, -2.8284F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -2.5F, 5.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes9 = head_spikes.addChild("head_spikes9", ModelPartBuilder.create(), ModelTransform.pivot(-3.0F, 0.0F, -5.0F));

        ModelPartData head_spikes_r15 = head_spikes9.addChild("head_spikes_r15", ModelPartBuilder.create().uv(2, 51).cuboid(1.3284F, -2.5F, -2.8284F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(8.5F, -2.5F, 11.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r16 = head_spikes9.addChild("head_spikes_r16", ModelPartBuilder.create().uv(2, 51).cuboid(-4.3284F, -2.5F, -2.8284F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(8.5F, -2.5F, 11.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes10 = head_spikes.addChild("head_spikes10", ModelPartBuilder.create(), ModelTransform.pivot(-6.0F, 0.0F, -5.0F));

        ModelPartData head_spikes_r17 = head_spikes10.addChild("head_spikes_r17", ModelPartBuilder.create().uv(2, 51).cuboid(1.3284F, -2.5F, -2.8284F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -2.5F, 11.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r18 = head_spikes10.addChild("head_spikes_r18", ModelPartBuilder.create().uv(2, 51).cuboid(-4.3284F, -2.5F, -2.8284F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -2.5F, 11.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes11 = head_spikes.addChild("head_spikes11", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, -5.0F));

        ModelPartData head_spikes_r19 = head_spikes11.addChild("head_spikes_r19", ModelPartBuilder.create().uv(2, 51).cuboid(1.3284F, -2.5F, -2.8284F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -2.5F, 11.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r20 = head_spikes11.addChild("head_spikes_r20", ModelPartBuilder.create().uv(2, 51).cuboid(-4.3284F, -2.5F, -2.8284F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -2.5F, 11.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes12 = head_spikes.addChild("head_spikes12", ModelPartBuilder.create(), ModelTransform.pivot(-3.0F, 0.0F, -2.0F));

        ModelPartData head_spikes_r21 = head_spikes12.addChild("head_spikes_r21", ModelPartBuilder.create().uv(2, 51).cuboid(1.3284F, -2.5F, -2.8284F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -2.5F, 11.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r22 = head_spikes12.addChild("head_spikes_r22", ModelPartBuilder.create().uv(2, 51).cuboid(-4.3284F, -2.5F, -2.8284F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -2.5F, 11.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes14 = head_spikes.addChild("head_spikes14", ModelPartBuilder.create(), ModelTransform.pivot(-3.0F, 0.0F, -2.0F));

        ModelPartData head_spikes_r23 = head_spikes14.addChild("head_spikes_r23", ModelPartBuilder.create().uv(2, 51).cuboid(1.3284F, -2.5F, -2.8284F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(5.5F, -2.5F, 11.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r24 = head_spikes14.addChild("head_spikes_r24", ModelPartBuilder.create().uv(2, 51).cuboid(-4.3284F, -2.5F, -2.8284F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(5.5F, -2.5F, 11.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes13 = head_spikes.addChild("head_spikes13", ModelPartBuilder.create(), ModelTransform.pivot(-6.0F, 0.0F, -4.0F));

        ModelPartData head_spikes_r25 = head_spikes13.addChild("head_spikes_r25", ModelPartBuilder.create().uv(2, 51).cuboid(1.3284F, -2.5F, -2.8284F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -2.5F, 16.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r26 = head_spikes13.addChild("head_spikes_r26", ModelPartBuilder.create().uv(2, 51).cuboid(-4.3284F, -2.5F, -2.8284F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -2.5F, 16.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes15 = head_spikes.addChild("head_spikes15", ModelPartBuilder.create(), ModelTransform.pivot(-3.0F, 0.0F, -4.0F));

        ModelPartData head_spikes_r27 = head_spikes15.addChild("head_spikes_r27", ModelPartBuilder.create().uv(2, 51).cuboid(1.3284F, -2.5F, -2.8284F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(8.5F, -2.5F, 16.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r28 = head_spikes15.addChild("head_spikes_r28", ModelPartBuilder.create().uv(2, 51).cuboid(-4.3284F, -2.5F, -2.8284F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(8.5F, -2.5F, 16.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes16 = head_spikes.addChild("head_spikes16", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, -4.0F));

        ModelPartData head_spikes_r29 = head_spikes16.addChild("head_spikes_r29", ModelPartBuilder.create().uv(2, 51).cuboid(1.3284F, -2.5F, -2.8284F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -2.5F, 16.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r30 = head_spikes16.addChild("head_spikes_r30", ModelPartBuilder.create().uv(2, 51).cuboid(-4.3284F, -2.5F, -2.8284F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -2.5F, 16.0F, 0.0F, -0.7854F, 0.0F));

        return TexturedModelData.of(modelData, 64, 64);
    }

    public static TexturedModelData getFootTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData foot = modelPartData.addChild("foot", ModelPartBuilder.create(), ModelTransform.NONE);

        ModelPartData foot_spikes = foot.addChild("foot_spikes", ModelPartBuilder.create(), ModelTransform.of(8.5F, 10.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

        ModelPartData foot_spikes2 = foot_spikes.addChild("foot_spikes2", ModelPartBuilder.create(), ModelTransform.pivot(-6.0F, 0.0F, -4.0F));

        ModelPartData foot_spikes_r31 = foot_spikes2.addChild("foot_spikes_r31", ModelPartBuilder.create().uv(2, 51).cuboid(-1.5F, -2.5F, 0.0F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -2.5F, -1.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData foot_spikes_r32 = foot_spikes2.addChild("foot_spikes_r32", ModelPartBuilder.create().uv(2, 51).cuboid(-1.5F, -2.5F, 0.0F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -2.5F, -1.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData foot_spikes3 = foot_spikes.addChild("foot_spikes3", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, -4.0F));

        ModelPartData foot_spikes_r33 = foot_spikes3.addChild("foot_spikes_r33", ModelPartBuilder.create().uv(2, 51).cuboid(-1.5F, -2.5F, 0.0F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -2.5F, -1.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData foot_spikes_r34 = foot_spikes3.addChild("foot_spikes_r34", ModelPartBuilder.create().uv(2, 51).cuboid(-1.5F, -2.5F, 0.0F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -2.5F, -1.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData foot_spikes4 = foot_spikes.addChild("foot_spikes4", ModelPartBuilder.create(), ModelTransform.pivot(-3.0F, 0.0F, -1.0F));

        ModelPartData foot_spikes_r35 = foot_spikes4.addChild("foot_spikes_r35", ModelPartBuilder.create().uv(2, 51).cuboid(-1.5F, -2.5F, 0.0F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -2.5F, -1.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData foot_spikes_r36 = foot_spikes4.addChild("foot_spikes_r36", ModelPartBuilder.create().uv(2, 51).cuboid(-1.5F, -2.5F, 0.0F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -2.5F, -1.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData foot_spikes5 = foot_spikes.addChild("foot_spikes5", ModelPartBuilder.create(), ModelTransform.pivot(-3.0F, 0.0F, -1.0F));

        ModelPartData foot_spikes_r37 = foot_spikes5.addChild("foot_spikes_r37", ModelPartBuilder.create().uv(2, 51).cuboid(-1.5F, -2.5F, 0.0F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(5.5F, -2.5F, -1.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData foot_spikes_r38 = foot_spikes5.addChild("foot_spikes_r38", ModelPartBuilder.create().uv(2, 51).cuboid(-1.5F, -2.5F, 0.0F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(5.5F, -2.5F, -1.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData foot_spikes6 = foot_spikes.addChild("foot_spikes6", ModelPartBuilder.create(), ModelTransform.pivot(-3.0F, 0.0F, -4.0F));

        ModelPartData foot_spikes_r39 = foot_spikes6.addChild("foot_spikes_r39", ModelPartBuilder.create().uv(2, 51).cuboid(-1.5F, -2.5F, 0.0F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(8.5F, -2.5F, -1.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData foot_spikes_r40 = foot_spikes6.addChild("foot_spikes_r40", ModelPartBuilder.create().uv(2, 51).cuboid(-1.5F, -2.5F, 0.0F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(8.5F, -2.5F, -1.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData foot_spikes7 = foot_spikes.addChild("foot_spikes7", ModelPartBuilder.create(), ModelTransform.pivot(-6.0F, 0.0F, -4.0F));

        ModelPartData foot_spikes_r41 = foot_spikes7.addChild("foot_spikes_r41", ModelPartBuilder.create().uv(2, 51).cuboid(-1.5F, -2.5F, 0.0F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -2.5F, 5.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData foot_spikes_r42 = foot_spikes7.addChild("foot_spikes_r42", ModelPartBuilder.create().uv(2, 51).cuboid(-1.5F, -2.5F, 0.0F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -2.5F, 5.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData foot_spikes8 = foot_spikes.addChild("foot_spikes8", ModelPartBuilder.create(), ModelTransform.pivot(-3.0F, 0.0F, -4.0F));

        ModelPartData foot_spikes_r43 = foot_spikes8.addChild("foot_spikes_r43", ModelPartBuilder.create().uv(2, 51).cuboid(-1.5F, -2.5F, 0.0F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(8.5F, -2.5F, 5.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData foot_spikes_r44 = foot_spikes8.addChild("foot_spikes_r44", ModelPartBuilder.create().uv(2, 51).cuboid(-1.5F, -2.5F, 0.0F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(8.5F, -2.5F, 5.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData foot_spikes9 = foot_spikes.addChild("foot_spikes9", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, -4.0F));

        ModelPartData foot_spikes_r45 = foot_spikes9.addChild("foot_spikes_r45", ModelPartBuilder.create().uv(2, 51).cuboid(-1.5F, -2.5F, 0.0F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -2.5F, 5.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData foot_spikes_r46 = foot_spikes9.addChild("foot_spikes_r46", ModelPartBuilder.create().uv(2, 51).cuboid(-1.5F, -2.5F, 0.0F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -2.5F, 5.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData foot_spikes10 = foot_spikes.addChild("foot_spikes10", ModelPartBuilder.create(), ModelTransform.pivot(-3.0F, 0.0F, -1.0F));

        ModelPartData foot_spikes_r47 = foot_spikes10.addChild("foot_spikes_r47", ModelPartBuilder.create().uv(2, 51).cuboid(-1.5F, -2.5F, 0.0F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(5.5F, -2.5F, 5.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData foot_spikes_r48 = foot_spikes10.addChild("foot_spikes_r48", ModelPartBuilder.create().uv(2, 51).cuboid(-1.5F, -2.5F, 0.0F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(5.5F, -2.5F, 5.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData foot_spikes11 = foot_spikes.addChild("foot_spikes11", ModelPartBuilder.create(), ModelTransform.pivot(-3.0F, 0.0F, -1.0F));

        ModelPartData foot_spikes_r49 = foot_spikes11.addChild("foot_spikes_r49", ModelPartBuilder.create().uv(2, 51).cuboid(-1.5F, -2.5F, 0.0F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -2.5F, 5.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData foot_spikes_r50 = foot_spikes11.addChild("foot_spikes_r50", ModelPartBuilder.create().uv(2, 51).cuboid(-1.5F, -2.5F, 0.0F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -2.5F, 5.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData foot_spikes12 = foot_spikes.addChild("foot_spikes12", ModelPartBuilder.create(), ModelTransform.pivot(-3.0F, 0.0F, -4.0F));

        ModelPartData foot_spikes_r51 = foot_spikes12.addChild("foot_spikes_r51", ModelPartBuilder.create().uv(2, 51).cuboid(-1.5F, -2.5F, 0.0F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(8.5F, -2.5F, 11.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData foot_spikes_r52 = foot_spikes12.addChild("foot_spikes_r52", ModelPartBuilder.create().uv(2, 51).cuboid(-1.5F, -2.5F, 0.0F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(8.5F, -2.5F, 11.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData foot_spikes13 = foot_spikes.addChild("foot_spikes13", ModelPartBuilder.create(), ModelTransform.pivot(-6.0F, 0.0F, -4.0F));

        ModelPartData foot_spikes_r53 = foot_spikes13.addChild("foot_spikes_r53", ModelPartBuilder.create().uv(2, 51).cuboid(-1.5F, -2.5F, 0.0F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -2.5F, 11.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData foot_spikes_r54 = foot_spikes13.addChild("foot_spikes_r54", ModelPartBuilder.create().uv(2, 51).cuboid(-1.5F, -2.5F, 0.0F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -2.5F, 11.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData foot_spikes14 = foot_spikes.addChild("foot_spikes14", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, -4.0F));

        ModelPartData foot_spikes_r55 = foot_spikes14.addChild("foot_spikes_r55", ModelPartBuilder.create().uv(2, 51).cuboid(-1.5F, -2.5F, 0.0F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -2.5F, 11.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData foot_spikes_r56 = foot_spikes14.addChild("foot_spikes_r56", ModelPartBuilder.create().uv(2, 51).cuboid(-1.5F, -2.5F, 0.0F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -2.5F, 11.0F, 0.0F, -0.7854F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }
}

