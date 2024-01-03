package de.takacick.raidbase.access;

import net.minecraft.entity.Entity;

public interface LivingProperties {

    void setGettingBanned(int banTicks);

    boolean isGettingBanned();

    int getBanTicks();

    Entity getRenderEntity();

    void setWaterElectroShock(int waterElectroShock);

    boolean isWaterElectroShocked();

    void setGlitchy(int ticks);

    boolean isGlitchy();

    float getGlitchSytrength(float tickDelta);

    void setPieTicks(int pieTicks);

    boolean hasPie();

}
