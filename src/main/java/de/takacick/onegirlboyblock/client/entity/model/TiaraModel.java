package de.takacick.onegirlboyblock.client.entity.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.entity.LivingEntity;

import java.util.Optional;

public class TiaraModel<T extends LivingEntity>
        extends SinglePartEntityModel<T> {

    private final ModelPart root;
    public final ModelPart head;

    public TiaraModel(ModelPart root) {
        this.root = root;
        this.head = this.root.getChild("head");
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -9.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.501F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 32);
    }

    public void copyFromBipedState(BipedEntityModel<T> model) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        model.copyStateTo(this);

        this.head.copyTransform(model.head);
    }

    @Override
    public void setAngles(T playerEntity, float f, float g, float h, float i, float j) {

    }

    @Override
    public Optional<ModelPart> getChild(String name) {
        return super.getChild(name);
    }
}