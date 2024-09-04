package de.takacick.onenukeblock.client.item.model;

import de.takacick.utils.item.client.model.SinglePartItemModel;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class BangMaceItemModel extends SinglePartItemModel {

    private final ModelPart bone;
    private final ModelPart core;
    private final ModelPart tnt;

    public BangMaceItemModel(ModelPart root) {
        super(root, RenderLayer::getEntityTranslucent);
        this.bone = root.getChild("bone");
        this.core = this.bone.getChild("core");
        this.tnt = this.core.getChild("tnt");
    }

    @Override
    public void setAngles(ItemStack itemStack, LivingEntity livingEntity, ModelTransformationMode modelTransformationMode, long time, float tickDelta) {

    }

    public void renderTnt(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        matrices.push();
        this.getPart().rotate(matrices);
        this.bone.rotate(matrices);
        this.core.rotate(matrices);
        this.tnt.render(matrices, vertices, light, overlay, color);
        matrices.pop();
    }

    public void setTntScale(float x, float y, float z) {
        this.tnt.xScale = x;
        this.tnt.yScale = y;
        this.tnt.zScale = z;
    }

    public void setTntVisible(boolean visible) {
        this.tnt.visible = visible;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 22.4F, 0.0F));

        ModelPartData core = bone.addChild("core", ModelPartBuilder.create(), ModelTransform.pivot(2.6059F, -15.9652F, 2.3F));

        ModelPartData tnt = core.addChild("tnt", ModelPartBuilder.create().uv(14, 17).cuboid(-1.2118F, -7.5579F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.2F))
                .uv(0, 0).cuboid(-4.2118F, -6.6696F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.3941F, -1.0652F, -2.5F));

        ModelPartData string = tnt.addChild("string", ModelPartBuilder.create(), ModelTransform.pivot(-0.1118F, -5.7696F, -0.1F));

        ModelPartData string_r1 = string.addChild("string_r1", ModelPartBuilder.create().uv(2, 1).cuboid(-0.7592F, -4.9104F, -1.1802F, 1.0F, 2.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.2182F, 0.1309F, 0.0873F));

        ModelPartData string_r2 = string.addChild("string_r2", ModelPartBuilder.create().uv(2, 1).cuboid(-0.5F, -4.9886F, -0.2385F, 1.0F, 2.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.of(0.0F, 1.4F, 0.2F, 0.0873F, 0.0F, 0.0F));

        ModelPartData tnt_spikes = tnt.addChild("tnt_spikes", ModelPartBuilder.create(), ModelTransform.pivot(-4.0118F, 3.8304F, 3.8F));

        ModelPartData tnt_spikes_r1 = tnt_spikes.addChild("tnt_spikes_r1", ModelPartBuilder.create().uv(14, 23).cuboid(-1.0F, -3.7189F, -2.2679F, 2.0F, 2.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.4363F, -0.7854F, 0.0F));

        ModelPartData tnt_spikes_r2 = tnt_spikes.addChild("tnt_spikes_r2", ModelPartBuilder.create().uv(14, 23).cuboid(-1.0F, -3.7189F, 0.2679F, 2.0F, 2.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.of(0.0F, -7.0F, 0.0F, 0.4363F, -0.7854F, 0.0F));

        ModelPartData tnt_spikes_r3 = tnt_spikes.addChild("tnt_spikes_r3", ModelPartBuilder.create().uv(14, 23).cuboid(-1.0F, -3.7189F, -2.2679F, 2.0F, 2.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.of(0.0F, -7.0F, -7.6F, -0.4363F, 0.7854F, 0.0F));

        ModelPartData tnt_spikes_r4 = tnt_spikes.addChild("tnt_spikes_r4", ModelPartBuilder.create().uv(14, 23).cuboid(-1.0F, -3.7189F, 0.2679F, 2.0F, 2.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.of(0.0F, 0.0F, -7.6F, 0.4363F, 0.7854F, 0.0F));

        ModelPartData tnt_spikes_r5 = tnt_spikes.addChild("tnt_spikes_r5", ModelPartBuilder.create().uv(14, 23).cuboid(-1.0F, -3.7189F, 0.2679F, 2.0F, 2.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.of(7.6F, 0.0F, -7.6F, 0.4363F, -0.7854F, 0.0F));

        ModelPartData tnt_spikes_r6 = tnt_spikes.addChild("tnt_spikes_r6", ModelPartBuilder.create().uv(14, 23).cuboid(-1.0F, -3.7189F, -2.2679F, 2.0F, 2.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.of(7.6F, -7.0F, -7.6F, -0.4363F, -0.7854F, 0.0F));

        ModelPartData tnt_spikes_r7 = tnt_spikes.addChild("tnt_spikes_r7", ModelPartBuilder.create().uv(14, 23).cuboid(-1.0F, -3.7189F, 0.2679F, 2.0F, 2.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.of(7.6F, -7.0F, 0.0F, 0.4363F, 0.7854F, 0.0F));

        ModelPartData tnt_spikes_r8 = tnt_spikes.addChild("tnt_spikes_r8", ModelPartBuilder.create().uv(14, 23).cuboid(-1.0F, -3.7189F, -2.2679F, 2.0F, 2.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.of(7.6F, 0.0F, 0.0F, -0.4363F, 0.7854F, 0.0F));

        ModelPartData handle = bone.addChild("handle", ModelPartBuilder.create().uv(37, 4).cuboid(2.0F, -5.401F, -1.0F, 2.0F, 15.0F, 2.0F, new Dilation(0.0F))
                .uv(5, 17).cuboid(2.0F, 9.8118F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.2F)), ModelTransform.pivot(-3.0F, -10.4F, -0.2F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    public static TexturedModelData getThirdPersonTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData bang_mace = modelPartData.addChild("bone", ModelPartBuilder.create(), ModelTransform.of(-4.5F, -10.9F, -7.2F, 1.5708F, 0.0F, 0.0F));

        ModelPartData core = bang_mace.addChild("core", ModelPartBuilder.create(), ModelTransform.pivot(8.1059F, -4.0652F, -6.5F));

        ModelPartData tnt = core.addChild("tnt", ModelPartBuilder.create().uv(14, 17).cuboid(-1.2118F, -7.5579F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.2F))
                .uv(0, 0).cuboid(-4.2118F, -6.6696F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.3941F, -1.0652F, -2.5F));

        ModelPartData string = tnt.addChild("string", ModelPartBuilder.create(), ModelTransform.pivot(-0.1118F, -5.7696F, -0.1F));

        ModelPartData string_r1 = string.addChild("string_r1", ModelPartBuilder.create().uv(2, 1).cuboid(-0.7592F, -4.9104F, -1.1802F, 1.0F, 2.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.2182F, 0.1309F, 0.0873F));

        ModelPartData string_r2 = string.addChild("string_r2", ModelPartBuilder.create().uv(2, 1).cuboid(-0.5F, -4.9886F, -0.2385F, 1.0F, 2.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.of(0.0F, 1.4F, 0.2F, 0.0873F, 0.0F, 0.0F));

        ModelPartData tnt_spikes = tnt.addChild("tnt_spikes", ModelPartBuilder.create(), ModelTransform.pivot(-4.0118F, 3.8304F, 3.8F));

        ModelPartData tnt_spikes_r1 = tnt_spikes.addChild("tnt_spikes_r1", ModelPartBuilder.create().uv(14, 23).cuboid(-1.0F, -3.7189F, -2.2679F, 2.0F, 2.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.4363F, -0.7854F, 0.0F));

        ModelPartData tnt_spikes_r2 = tnt_spikes.addChild("tnt_spikes_r2", ModelPartBuilder.create().uv(14, 23).cuboid(-1.0F, -3.7189F, 0.2679F, 2.0F, 2.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.of(0.0F, -7.0F, 0.0F, 0.4363F, -0.7854F, 0.0F));

        ModelPartData tnt_spikes_r3 = tnt_spikes.addChild("tnt_spikes_r3", ModelPartBuilder.create().uv(14, 23).cuboid(-1.0F, -3.7189F, -2.2679F, 2.0F, 2.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.of(0.0F, -7.0F, -7.6F, -0.4363F, 0.7854F, 0.0F));

        ModelPartData tnt_spikes_r4 = tnt_spikes.addChild("tnt_spikes_r4", ModelPartBuilder.create().uv(14, 23).cuboid(-1.0F, -3.7189F, 0.2679F, 2.0F, 2.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.of(0.0F, 0.0F, -7.6F, 0.4363F, 0.7854F, 0.0F));

        ModelPartData tnt_spikes_r5 = tnt_spikes.addChild("tnt_spikes_r5", ModelPartBuilder.create().uv(14, 23).cuboid(-1.0F, -3.7189F, 0.2679F, 2.0F, 2.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.of(7.6F, 0.0F, -7.6F, 0.4363F, -0.7854F, 0.0F));

        ModelPartData tnt_spikes_r6 = tnt_spikes.addChild("tnt_spikes_r6", ModelPartBuilder.create().uv(14, 23).cuboid(-1.0F, -3.7189F, -2.2679F, 2.0F, 2.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.of(7.6F, -7.0F, -7.6F, -0.4363F, -0.7854F, 0.0F));

        ModelPartData tnt_spikes_r7 = tnt_spikes.addChild("tnt_spikes_r7", ModelPartBuilder.create().uv(14, 23).cuboid(-1.0F, -3.7189F, 0.2679F, 2.0F, 2.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.of(7.6F, -7.0F, 0.0F, 0.4363F, 0.7854F, 0.0F));

        ModelPartData tnt_spikes_r8 = tnt_spikes.addChild("tnt_spikes_r8", ModelPartBuilder.create().uv(14, 23).cuboid(-1.0F, -3.7189F, -2.2679F, 2.0F, 2.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.of(7.6F, 0.0F, 0.0F, -0.4363F, 0.7854F, 0.0F));

        ModelPartData handle = bang_mace.addChild("handle", ModelPartBuilder.create().uv(37, 4).cuboid(2.0F, -5.401F, -1.0F, 2.0F, 15.0F, 2.0F, new Dilation(0.0F))
                .uv(5, 17).cuboid(2.0F, 9.8118F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.2F)), ModelTransform.pivot(2.5F, 1.5F, -9.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }
}
