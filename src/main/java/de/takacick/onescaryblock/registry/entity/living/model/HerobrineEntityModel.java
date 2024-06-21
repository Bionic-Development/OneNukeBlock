package de.takacick.onescaryblock.registry.entity.living.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;

public class HerobrineEntityModel<T extends LivingEntity> extends PlayerEntityModel<T> {

    public HerobrineEntityModel(ModelPart root, boolean thinArms) {
        super(root, thinArms);
    }

    @Override
    public void setAngles(LivingEntity livingEntity, float f, float g, float h, float i, float j) {

    }
}
