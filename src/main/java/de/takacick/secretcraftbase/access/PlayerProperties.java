package de.takacick.secretcraftbase.access;

import net.minecraft.entity.AnimationState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public interface PlayerProperties {

    void setHeartCarverStack(ItemStack heartCarver);

    AnimationState getHeartRemovalState();

    void setHeartRemovalTicks(int heartRemovalTicks);

    int getHeartRemovalTicks();

    AnimationState getFrogTongueState();

    void setFrogTarget(Entity target);

    void eatFrogTarget();

    boolean isUsingFrogTongue();

    void setFrogTongueLength(float frogTongueLength);

    float getFrogTongueLength();
}
