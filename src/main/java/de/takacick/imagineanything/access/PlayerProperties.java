package de.takacick.imagineanything.access;

import net.minecraft.entity.AnimationState;
import net.minecraft.entity.Entity;

public interface PlayerProperties {

    AnimationState getHeadRemovalState();

    void setRemovedHead(boolean removedHead);

    boolean removedHead();

    void setTelekinesisFlight(boolean telekinesisFlight);

    boolean hasTelekinesisFlight();

    void setHolding(Entity entity);

    int getHolding();

    float getDistance();

    void setFallFlying(boolean fallFlying);

    boolean getFallFlying();

    void setIronManForcefield(boolean ironManForcefield);

    boolean hasIronManForcefield();

    void setIronManLaser(boolean ironManLaser);

    boolean hasIronManLaser();

    void setIronManAbility(String ironManAbility);
}
