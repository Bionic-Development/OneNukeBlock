package de.takacick.tinyhouse.access;

import net.minecraft.entity.Entity;

public interface EntityProperties {

    void setBlockMagnetOwner(Entity blockMagnet);

    int getBlockMagnetOwner();

    void setCrushed(int crushedTicks);

    boolean isCrushed();

    float getCrushedHeight(float tickDelta);

    void setStuckInsidePiston(int stuckInsidePistonTicks);

    boolean isStuckInsidePiston();

}
