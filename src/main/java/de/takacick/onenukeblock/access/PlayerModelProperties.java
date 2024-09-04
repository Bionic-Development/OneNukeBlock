package de.takacick.onenukeblock.access;

import de.takacick.onenukeblock.client.entity.model.AnimationBodyModel;
import net.minecraft.entity.LivingEntity;

public interface PlayerModelProperties {

    AnimationBodyModel<LivingEntity> getAnimationBodyModel();

}
