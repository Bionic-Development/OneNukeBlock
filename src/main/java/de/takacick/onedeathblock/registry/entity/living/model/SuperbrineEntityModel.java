package de.takacick.onedeathblock.registry.entity.living.model;

import de.takacick.onedeathblock.registry.entity.living.SuperbrineEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.PlayerEntityModel;

public class SuperbrineEntityModel extends PlayerEntityModel<SuperbrineEntity> {

    public SuperbrineEntityModel(ModelPart root, boolean thinArms) {
        super(root, thinArms);
    }

    @Override
    public void setAngles(SuperbrineEntity superbrineEntity, float f, float g, float h, float i, float j) {
        super.setAngles(superbrineEntity, f, g, h, i, j);

        if(!superbrineEntity.getPassengerList().isEmpty()) {

            this.rightArm.roll = -15f * ((float) Math.PI / 180);
            this.rightArm.pitch = -40f * ((float) Math.PI / 180);
            this.leftArm.roll = 15f * ((float) Math.PI / 180);
            this.leftArm.pitch = -40f * ((float) Math.PI / 180);

            this.rightSleeve.roll = this.rightArm.roll;
            this.rightSleeve.pitch = this.rightArm.pitch;
            this.leftSleeve.roll = this.leftArm.roll;
            this.leftSleeve.pitch = this.leftArm.pitch;
        }
    }

    @Override
    protected void animateArms(SuperbrineEntity superbrineEntity, float animationProgress) {
        if(superbrineEntity.getPassengerList().isEmpty()) {
            super.animateArms(superbrineEntity, animationProgress);
        }
    }
}
