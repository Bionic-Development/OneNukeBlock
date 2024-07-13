package de.takacick.onegirlboyblock.client.item.model;

import de.takacick.utils.item.client.model.SinglePartItemModel;
import net.minecraft.client.model.*;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class TurboBoardItemModel extends SinglePartItemModel {

    private final ModelPart turboBoard;
    private final ModelPart turboBeamFront;
    private final ModelPart turboBeamBack;

    public TurboBoardItemModel(ModelPart root) {
        super(root);
        this.turboBoard = getPart().getChild("turbo_board");
        this.turboBeamFront = this.turboBoard.getChild("turbo_beam_front");
        this.turboBeamBack = this.turboBoard.getChild("turbo_beam_back");
    }

    @Override
    public void setAngles(ItemStack itemStack, LivingEntity livingEntity, ModelTransformationMode modelTransformationMode, long time, float tickDelta) {

    }

    public void rotateTurboBeam(MatrixStack matrixStack, boolean front) {
        getPart().rotate(matrixStack);
        this.turboBoard.rotate(matrixStack);
        if (front) {
            this.turboBeamFront.rotate(matrixStack);
        } else {
            this.turboBeamBack.rotate(matrixStack);
        }
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData turbo_board = modelPartData.addChild("turbo_board", ModelPartBuilder.create().uv(0, 0).cuboid(-16.0F, -3.75F, 0.0F, 16.0F, 1.0F, 16.0F, new Dilation(0.0F))
                .uv(37, 38).cuboid(-15.0F, -4.5F, 24.0F, 14.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(41, 22).cuboid(-14.0F, -5.0F, 28.0F, 12.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(41, 31).cuboid(-12.0F, -5.5F, 30.0F, 8.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 28).cuboid(-16.0F, -4.0F, 16.0F, 16.0F, 1.0F, 8.0F, new Dilation(0.0F))
                .uv(0, 38).cuboid(-15.0F, -4.5F, -12.0F, 14.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(41, 18).cuboid(-14.0F, -5.0F, -14.0F, 12.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(41, 28).cuboid(-12.0F, -5.5F, -15.0F, 8.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 18).cuboid(-16.0F, -4.0F, -8.0F, 16.0F, 1.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(8.0F, 24.0F, -8.0F));

        ModelPartData turbo_beam_front = turbo_board.addChild("turbo_beam_front", ModelPartBuilder.create(), ModelTransform.of(-8.0F, -3.0F, -4.0F, 3.1416F, 0.0F, 0.0F));

        ModelPartData turbo_beam_back = turbo_board.addChild("turbo_beam_back", ModelPartBuilder.create(), ModelTransform.of(-8.0F, -3.0F, 20.0F, 3.1416F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 128, 128);
    }
}
