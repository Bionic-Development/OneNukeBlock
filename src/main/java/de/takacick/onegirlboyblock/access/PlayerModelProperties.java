package de.takacick.onegirlboyblock.access;

import de.takacick.onegirlboyblock.client.entity.model.AnimationBodyModel;
import net.minecraft.entity.LivingEntity;

public interface PlayerModelProperties {

    AnimationBodyModel<LivingEntity> getAnimationBodyModel();

}
