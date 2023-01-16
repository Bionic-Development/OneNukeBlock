package de.takacick.imagineanything.access;

import net.minecraft.entity.LivingEntity;

public interface LivingProperties {

    void setHolder(LivingEntity livingEntity);

    boolean hasHolder();

    void setThrown();

    int getVibratingTicks();

    void setVibratingTicks(int vibratingTicks);

}
