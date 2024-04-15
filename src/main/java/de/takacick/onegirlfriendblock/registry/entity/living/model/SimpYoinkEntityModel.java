package de.takacick.onegirlfriendblock.registry.entity.living.model;

import de.takacick.onegirlfriendblock.registry.entity.living.SimpYoinkEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.PlayerEntityModel;

public class SimpYoinkEntityModel extends PlayerEntityModel<SimpYoinkEntity> {

    public SimpYoinkEntityModel(ModelPart root, boolean thinArms) {
        super(root, thinArms);
    }

    @Override
    public void setAngles(SimpYoinkEntity simpYoinkEntity, float f, float g, float h, float i, float j) {
        super.setAngles(simpYoinkEntity, f, g, h, i, j);
        if (simpYoinkEntity.hasPassengers()) {
            this.leftArm.pitch = -180f * ((float) Math.PI / 180f);
            this.rightArm.pitch = -180f * ((float) Math.PI / 180f);

            this.rightSleeve.copyTransform(this.rightArm);
            this.leftSleeve.copyTransform(this.leftArm);
        }
    }
}
