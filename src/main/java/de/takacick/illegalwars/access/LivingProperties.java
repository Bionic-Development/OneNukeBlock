package de.takacick.illegalwars.access;

public interface LivingProperties {

    void setPoopTicks(int poopTicks);

    boolean hasPoop();

    void setSludgeStrength(float strength);

    float getSludgeStrength(float tickDelta);

}
