package de.takacick.onegirlfriendblock.registry.entity.living.model;

import de.takacick.onegirlfriendblock.client.model.AnimationBodyModel;
import de.takacick.onegirlfriendblock.registry.entity.living.SimpEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.PlayerEntityModel;

public class SimpEntityModel extends PlayerEntityModel<SimpEntity> {

    private final AnimationBodyModel<SimpEntity> simpPleaseAnimation;

    public SimpEntityModel(ModelPart root, boolean thinArms) {
        super(root, thinArms);
        this.simpPleaseAnimation = new AnimationBodyModel<>();
    }

    @Override
    public void setAngles(SimpEntity simpEntity, float f, float g, float h, float i, float j) {
        super.setAngles(simpEntity, f, g, h, i, j);

        if (simpEntity.getSimpPleaseState().isRunning()) {

            this.simpPleaseAnimation.setAngles(simpEntity, f, g, h, i, j);

            this.rightArm.copyTransform(this.simpPleaseAnimation.rightArm);
            this.rightSleeve.copyTransform(this.simpPleaseAnimation.rightArm);

            this.leftArm.copyTransform(this.simpPleaseAnimation.leftArm);
            this.leftSleeve.copyTransform(this.simpPleaseAnimation.leftArm);

            if (simpEntity.getSimpPleaseState().getTimeRunning() > 1300) {
                simpEntity.getSimpPleaseState().stop();
            }
        }
    }
}
