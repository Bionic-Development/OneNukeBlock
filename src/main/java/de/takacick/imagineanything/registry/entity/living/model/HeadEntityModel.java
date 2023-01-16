package de.takacick.imagineanything.registry.entity.living.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.entity.LivingEntity;

import java.util.Optional;

public class HeadEntityModel<T extends LivingEntity>
        extends SinglePartEntityModel<T> {

    private final ModelPart root;
    public final ModelPart head;
    public float leaningPitch;

    public HeadEntityModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("bone").getChild("head");
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(-5.0F, 2.0F, 0.0F));

        bone.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
                .uv(32, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.5F)), ModelTransform.pivot(5.0F, 22.0F, 0.0F));

        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void animateModel(T livingEntity, float f, float g, float h) {
        this.leaningPitch = livingEntity.getLeaningPitch(h);
        super.animateModel(livingEntity, f, g, h);
    }

    @Override
    public void setAngles(T livingEntity, float f, float g, float h, float i, float j) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        boolean bl = livingEntity.getRoll() > 4;
        this.head.yaw = i * ((float) Math.PI / 180);
        this.head.pitch = bl ? -0.7853982f : (this.leaningPitch > 0.0f ? (this.lerpAngle(this.leaningPitch, this.head.pitch, j * ((float) Math.PI / 180))) : j * ((float) Math.PI / 180));
    }

    @Override
    public Optional<ModelPart> getChild(String name) {
        return super.getChild(name);
    }

    protected float lerpAngle(float angleOne, float angleTwo, float magnitude) {
        float f = (magnitude - angleTwo) % ((float) Math.PI * 2);
        if (f < (float) (-Math.PI)) {
            f += (float) Math.PI * 2;
        }
        if (f >= (float) Math.PI) {
            f -= (float) Math.PI * 2;
        }
        return angleTwo + angleOne * f;
    }
}