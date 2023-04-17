package de.takacick.stealbodyparts.access;

import de.takacick.stealbodyparts.registry.entity.custom.model.BodyModel;
import net.minecraft.entity.LivingEntity;

public interface PlayerEntityModelProperties {

    <T extends LivingEntity> BodyModel<T> getBodyModel();

}
