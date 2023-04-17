package de.takacick.stealbodyparts.access;

import net.minecraft.entity.AnimationState;

public interface PlayerProperties {

    AnimationState getHeartRemovalState();

    void setRemovedHeart(boolean removedHeart);

    boolean removedHeart();

    void setHeartRemovalTicks(int heartRemovalTicks);

}
