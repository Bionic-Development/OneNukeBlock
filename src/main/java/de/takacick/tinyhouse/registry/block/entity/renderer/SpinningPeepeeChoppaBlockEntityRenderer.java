package de.takacick.tinyhouse.registry.block.entity.renderer;

import de.takacick.tinyhouse.TinyHouse;
import de.takacick.tinyhouse.registry.block.entity.SpinningPeepeeChoppaBlockEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class SpinningPeepeeChoppaBlockEntityRenderer implements BlockEntityRenderer<SpinningPeepeeChoppaBlockEntity> {

    private static final Identifier TEXTURE = new Identifier(TinyHouse.MOD_ID, "textures/entity/spinning_peepee_choppa.png");
    private final ModelPart model;
    private final ModelPart spinner;

    public SpinningPeepeeChoppaBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.model = getTexturedModelData().createModel();
        this.spinner = this.model.getChild("body").getChild("spinner");
    }

    @Override
    public void render(SpinningPeepeeChoppaBlockEntity blockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int j) {
        float rotation = blockEntity.getRotation(tickDelta);
        matrixStack.translate(0.5, 1.501, 0.5);
        matrixStack.scale(-1, -1, 1);

        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutout(TEXTURE));
        this.spinner.resetTransform();
        this.spinner.yaw = -rotation * ((float) Math.PI / 180) * 50f;
        this.model.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1.0f);
    }

    @Override
    public int getRenderDistance() {
        return 256;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData spinner = body.addChild("spinner", ModelPartBuilder.create().uv(40, 13).cuboid(-1.0F, -15.0F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(36, 0).cuboid(-2.0F, -16.0F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData west_diamond_bone = spinner.addChild("west_diamond_bone", ModelPartBuilder.create(), ModelTransform.pivot(6.1786F, -15.5F, -0.2F));

        ModelPartData west_diamond_axe = west_diamond_bone.addChild("west_diamond_axe", ModelPartBuilder.create().uv(0, 33).cuboid(-3.1786F, -0.5F, -7.5357F, 2.0F, 1.0F, 7.0F, new Dilation(0.0F))
                .uv(11, 23).cuboid(-1.1786F, -0.5F, -6.5357F, 1.0F, 1.0F, 8.0F, new Dilation(0.0F))
                .uv(24, 37).cuboid(-4.1786F, -0.5F, -6.5357F, 1.0F, 1.0F, 7.0F, new Dilation(0.0F))
                .uv(21, 46).cuboid(-0.1786F, -0.5F, -5.5357F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 35).cuboid(0.8214F, -0.5F, -4.5357F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(8, 44).cuboid(-5.1786F, -0.5F, -4.5357F, 1.0F, 1.0F, 5.0F, new Dilation(0.0F))
                .uv(12, 33).cuboid(-6.1786F, -0.5F, -2.5357F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(21, 53).cuboid(-0.1786F, -0.5F, -0.5357F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(25, 5).cuboid(-2.1786F, -0.5F, -0.5357F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(53, 0).cuboid(0.8214F, -0.5F, 0.4643F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(49, 52).cuboid(1.8214F, -0.5F, 1.4643F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(52, 34).cuboid(2.8214F, -0.5F, 2.4643F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(34, 52).cuboid(3.8214F, -0.5F, 3.4643F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 31).cuboid(4.8214F, -0.5F, 4.4643F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(7.0F, 0.0F, 0.7357F, 0.0F, -2.3998F, 0.0F));

        ModelPartData east_diamond_bone = spinner.addChild("east_diamond_bone", ModelPartBuilder.create(), ModelTransform.pivot(-6.7214F, -15.5F, 0.0F));

        ModelPartData east_diamond_axe = east_diamond_bone.addChild("east_diamond_axe", ModelPartBuilder.create().uv(23, 26).cuboid(-3.1786F, -0.5F, -7.5357F, 2.0F, 1.0F, 7.0F, new Dilation(0.0F))
                .uv(17, 13).cuboid(-1.1786F, -0.5F, -6.5357F, 1.0F, 1.0F, 8.0F, new Dilation(0.0F))
                .uv(35, 19).cuboid(-4.1786F, -0.5F, -6.5357F, 1.0F, 1.0F, 7.0F, new Dilation(0.0F))
                .uv(45, 20).cuboid(-0.1786F, -0.5F, -5.5357F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 25).cuboid(0.8214F, -0.5F, -4.5357F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(40, 6).cuboid(-5.1786F, -0.5F, -4.5357F, 1.0F, 1.0F, 5.0F, new Dilation(0.0F))
                .uv(11, 21).cuboid(-6.1786F, -0.5F, -2.5357F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 49).cuboid(-0.1786F, -0.5F, -0.5357F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-2.1786F, -0.5F, -0.5357F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(55, 54).cuboid(0.8214F, -0.5F, 0.4643F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(6, 56).cuboid(1.8214F, -0.5F, 1.4643F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(55, 47).cuboid(2.8214F, -0.5F, 2.4643F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(16, 44).cuboid(3.8214F, -0.5F, 3.4643F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 21).cuboid(4.8214F, -0.5F, 4.4643F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-7.1F, 0.0F, -0.4643F, 0.0F, 0.7854F, 0.0F));

        ModelPartData north_diamond_bone = spinner.addChild("north_diamond_bone", ModelPartBuilder.create(), ModelTransform.pivot(-0.3F, -15.5F, -6.2643F));

        ModelPartData north_diamond_axe = north_diamond_bone.addChild("north_diamond_axe", ModelPartBuilder.create().uv(12, 35).cuboid(-3.1786F, -0.5F, -7.5357F, 2.0F, 1.0F, 7.0F, new Dilation(0.0F))
                .uv(25, 0).cuboid(-1.1786F, -0.5F, -6.5357F, 1.0F, 1.0F, 8.0F, new Dilation(0.0F))
                .uv(34, 39).cuboid(-4.1786F, -0.5F, -6.5357F, 1.0F, 1.0F, 7.0F, new Dilation(0.0F))
                .uv(47, 44).cuboid(-0.1786F, -0.5F, -5.5357F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(34, 37).cuboid(0.8214F, -0.5F, -4.5357F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(44, 37).cuboid(-5.1786F, -0.5F, -4.5357F, 1.0F, 1.0F, 5.0F, new Dilation(0.0F))
                .uv(35, 28).cuboid(-6.1786F, -0.5F, -2.5357F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(40, 55).cuboid(-0.1786F, -0.5F, -0.5357F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(28, 0).cuboid(-2.1786F, -0.5F, -0.5357F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(27, 55).cuboid(0.8214F, -0.5F, 0.4643F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(55, 17).cuboid(1.8214F, -0.5F, 1.4643F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(54, 41).cuboid(2.8214F, -0.5F, 2.4643F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 54).cuboid(3.8214F, -0.5F, 3.4643F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(24, 35).cuboid(4.8214F, -0.5F, 4.4643F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.4786F, 0.0F, -7.2F, 0.0F, -0.7854F, 0.0F));

        ModelPartData south_diamond_bone = spinner.addChild("south_diamond_bone", ModelPartBuilder.create(), ModelTransform.pivot(-0.8214F, -15.5F, 13.5357F));

        ModelPartData south_diamond_axe = south_diamond_bone.addChild("south_diamond_axe", ModelPartBuilder.create().uv(28, 10).cuboid(-3.1786F, -0.5F, -7.5357F, 2.0F, 1.0F, 7.0F, new Dilation(0.0F))
                .uv(0, 21).cuboid(-1.1786F, -0.5F, -6.5357F, 1.0F, 1.0F, 8.0F, new Dilation(0.0F))
                .uv(35, 28).cuboid(-4.1786F, -0.5F, -6.5357F, 1.0F, 1.0F, 7.0F, new Dilation(0.0F))
                .uv(45, 28).cuboid(-0.1786F, -0.5F, -5.5357F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(28, 13).cuboid(0.8214F, -0.5F, -4.5357F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 42).cuboid(-5.1786F, -0.5F, -4.5357F, 1.0F, 1.0F, 5.0F, new Dilation(0.0F))
                .uv(22, 26).cuboid(-6.1786F, -0.5F, -2.5357F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(52, 26).cuboid(-0.1786F, -0.5F, -0.5357F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 3).cuboid(-2.1786F, -0.5F, -0.5357F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(15, 51).cuboid(0.8214F, -0.5F, 0.4643F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(6, 51).cuboid(1.8214F, -0.5F, 1.4643F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(43, 50).cuboid(2.8214F, -0.5F, 2.4643F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(50, 10).cuboid(3.8214F, -0.5F, 3.4643F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(11, 25).cuboid(4.8214F, -0.5F, 4.4643F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 2.3126F, 0.0F));

        ModelPartData chain = spinner.addChild("chain", ModelPartBuilder.create().uv(28, 48).cuboid(0.0F, -1.0F, 2.0F, 0.0F, 2.0F, 4.0F, new Dilation(0.0F))
                .uv(19, 0).cuboid(-1.0F, 0.0F, -6.0F, 2.0F, 0.0F, 4.0F, new Dilation(0.0F))
                .uv(25, 10).cuboid(2.0F, -1.0F, 0.0F, 4.0F, 2.0F, 0.0F, new Dilation(0.0F))
                .uv(33, 48).cuboid(2.0F, 0.0F, -1.0F, 4.0F, 0.0F, 2.0F, new Dilation(0.0F))
                .uv(22, 23).cuboid(-6.0F, -1.0F, 0.0F, 4.0F, 2.0F, 0.0F, new Dilation(0.0F))
                .uv(45, 15).cuboid(-1.0F, 0.0F, 2.0F, 2.0F, 0.0F, 4.0F, new Dilation(0.0F))
                .uv(48, 6).cuboid(-6.0F, 0.0F, -1.0F, 4.0F, 0.0F, 2.0F, new Dilation(0.0F))
                .uv(32, 19).cuboid(0.0F, -1.0F, -6.0F, 0.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -15.5F, 0.0F));

        ModelPartData base = body.addChild("base", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -14.0F, -3.0F, 6.0F, 14.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }
}

