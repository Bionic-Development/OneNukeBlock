package de.takacick.elementalblock.registry.entity.living.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SlimeEntityModel;
import net.minecraft.entity.Entity;

public class WaterElementalSlimeEntityModel<T extends Entity> extends SlimeEntityModel<T> {
    public WaterElementalSlimeEntityModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getOuterTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("cube", ModelPartBuilder.create().uv(0, 0).cuboid(-6.0F, 13.0F, -6.0F, 12.0F, 11.0F, 12.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    public static TexturedModelData getInnerTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData cube = modelPartData.addChild("cube", ModelPartBuilder.create().uv(29, 24).cuboid(-3.0F, 16.0F, -3.0F, 6.0F, 6.0F, 6.0F, new Dilation(1.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        cube.addChild("right_eye", ModelPartBuilder.create().uv(9, 45).cuboid(-3.3F, -5.5F, -4.5F, 2.0F, 1.0F, 2.0F, new Dilation(0.25F)), ModelTransform.pivot(-1.0F, 24.0F, 0.0F));
        cube.addChild("left_eye", ModelPartBuilder.create().uv(0, 45).cuboid(2.3F, -5.5F, -4.5F, 2.0F, 1.0F, 2.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData hat = cube.addChild("hat", ModelPartBuilder.create().uv(0, 24).cuboid(-3.0F, 10.999F, -2.0F, 6.0F, 2.0F, 8.0F, new Dilation(0.0F))
                .uv(23, 37).cuboid(3.0F, 11.999F, 0.0F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F))
                .uv(23, 37).mirrored().cuboid(-5.0F, 11.999F, 0.0F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F)).mirrored(false)
                .uv(0, 37).cuboid(-5.0F, 12.0F, 6.001F, 10.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
       hat.addChild("hat_r1", ModelPartBuilder.create().uv(40, 37).cuboid(-2.0F, -3.0F, -0.5F, 4.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 12.0F, 6.0F, -1.2654F, 0.0F, 0.0F));
    return TexturedModelData.of(modelData, 64, 64);
    }
}
