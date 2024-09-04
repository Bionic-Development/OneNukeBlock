package de.takacick.onenukeblock.client.item.model;

import de.takacick.utils.item.client.model.SinglePartItemModel;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class KaboomMinerItemModel extends SinglePartItemModel {

    private final ModelPart bone;
    private final ModelPart tnt;

    public KaboomMinerItemModel(ModelPart root) {
        super(root, RenderLayer::getEntityTranslucent);
        this.bone = root.getChild("bone");
        this.tnt = this.bone.getChild("tnt");
    }

    @Override
    public void setAngles(ItemStack itemStack, LivingEntity livingEntity, ModelTransformationMode modelTransformationMode, long time, float tickDelta) {

    }

    public void renderTnt(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        matrices.push();
        this.getPart().rotate(matrices);
        this.bone.rotate(matrices);
        this.tnt.render(matrices, vertices, light, overlay, color);
        matrices.pop();
    }

    public void rotateTnt(MatrixStack matrices) {
        this.getPart().rotate(matrices);
        this.bone.rotate(matrices);
        this.tnt.rotate(matrices);
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
        ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(8.0F, 24.0F, -8.0F));

        ModelPartData tnt = bone.addChild("tnt", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -22.5F, 5.0F, 8.0F, 7.0F, 6.0F, new Dilation(0.0F))
                .uv(9, 14).cuboid(7.0F, -21.0F, 5.0F, 3.0F, 2.0F, 6.0F, new Dilation(0.0F))
                .uv(9, 14).mirrored().cuboid(-4.0F, -21.0F, 5.0F, 3.0F, 2.0F, 6.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-11.0F, 0.0F, 0.0F));

        ModelPartData tnt_r1 = tnt.addChild("tnt_r1", ModelPartBuilder.create().uv(22, 14).mirrored().cuboid(-6.5F, -1.25F, -2.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)).mirrored(false)
                .uv(22, 14).mirrored().cuboid(-6.5F, -1.25F, -2.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-3.6296F, -21.7674F, 9.0F, 0.0F, 0.0F, -0.7854F));

        ModelPartData tnt_r2 = tnt.addChild("tnt_r2", ModelPartBuilder.create().uv(9, 23).mirrored().cuboid(-4.5F, -1.75F, -2.0F, 4.0F, 2.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-2.8684F, -19.5745F, 8.0F, 0.0F, 0.0F, -0.3927F));

        ModelPartData tnt_r3 = tnt.addChild("tnt_r3", ModelPartBuilder.create().uv(9, 23).cuboid(0.5F, -1.75F, -2.0F, 4.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(8.8684F, -19.5745F, 8.0F, 0.0F, 0.0F, 0.3927F));

        ModelPartData tnt_r4 = tnt.addChild("tnt_r4", ModelPartBuilder.create().uv(22, 14).cuboid(4.5F, -1.25F, -2.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(9.6296F, -21.7674F, 9.0F, 0.0F, 0.0F, 0.7854F));

        ModelPartData handle = bone.addChild("handle", ModelPartBuilder.create().uv(23, 0).cuboid(-9.5F, -1.5F, 6.5F, 3.0F, 2.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 14).cuboid(-9.0F, -18.0F, 7.0F, 2.0F, 18.0F, 2.0F, new Dilation(0.0F))
                .uv(26, 26).cuboid(-9.0F, -24.0F, 7.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }
}
