package de.takacick.everythinghearts.access;

import net.minecraft.entity.Entity;

public interface LivingProperties {

    void setHeartInfected(boolean heartInfected);

    boolean isHeartInfected();

    void tryToHeartInfect(Entity entity);

}
