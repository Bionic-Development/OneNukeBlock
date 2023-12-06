package de.takacick.emeraldmoney.client.entity.model;

import de.takacick.emeraldmoney.EmeraldMoney;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class VillagerDrillModel
        extends SinglePartEntityModel<LivingEntity> {

    public static final Identifier TEXTURE = new Identifier(EmeraldMoney.MOD_ID, "textures/entity/villager_driller.png");

    private final ModelPart root;
    private final ModelPart base;
    private final ModelPart drill;

    public VillagerDrillModel() {
        this.root = getTexturedModelData().createModel();
        this.base = root.getChild("base");
        this.drill = root.getChild("drill");
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData base = modelPartData.addChild("base", ModelPartBuilder.create().uv(0, 16).cuboid(-9.0F, -8.0F, 7.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 10).cuboid(-10.0F, -10.0F, 6.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(14, 7).cuboid(-9.5F, -9.0F, 6.5F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-10.0F, -20.0F, 6.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 21).cuboid(-6.0F, -18.5F, 7.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(13, 0).cuboid(-5.0F, -18.0F, 7.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(23, 0).cuboid(-13.0F, -18.0F, 7.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(21, 22).cuboid(-11.0F, -18.5F, 7.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(14, 13).cuboid(-9.5F, -15.0F, 6.5F, 3.0F, 5.0F, 3.0F, new Dilation(0.0F))
                .uv(7, 20).cuboid(-9.0F, -6.0F, 7.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(8.0F, 24.0F, -8.0F));

        ModelPartData base_r1 = base.addChild("base_r1", ModelPartBuilder.create().uv(17, 0).cuboid(-0.5F, -1.1F, -2.0F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-8.0F, -5.6966F, 9.0391F, -0.3927F, 0.0F, 0.0F));

        ModelPartData drill = modelPartData.addChild("drill", ModelPartBuilder.create().uv(31, 18).cuboid(-1.0F, -1.0F, -3.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(31, 10).cuboid(-2.0F, -2.0F, -1.0F, 4.0F, 4.0F, 2.0F, new Dilation(0.0F))
                .uv(35, 4).cuboid(-1.0F, -3.0F, -2.0F, 2.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(31, 0).cuboid(-3.0F, -3.0F, 1.0F, 6.0F, 6.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 21.0F, 0.0F, 1.5708F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 32);
    }

    @Override
    public void setAngles(LivingEntity livingEntity, float f, float g, float h, float i, float j) {

    }

    public void setRotation(float rotation) {
        this.drill.yaw = rotation;
    }
}