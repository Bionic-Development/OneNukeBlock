package de.takacick.everythinghearts.access;

import de.takacick.everythinghearts.registry.entity.custom.model.HeartHandsModel;
import net.minecraft.entity.LivingEntity;

public interface PlayerEntityModelProperties {

    <T extends LivingEntity> HeartHandsModel getHeartHandsModel();

}
