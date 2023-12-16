package de.takacick.tinyhouse.access;

public interface LivingProperties {

    void setBurning(boolean burning);

    boolean isBurning();

    void setFrozenBody(int frozenBodyTicks);

    boolean hasFrozenBody();
}
