package de.takacick.stealbodyparts.access;

import de.takacick.stealbodyparts.utils.BodyPart;
import net.minecraft.entity.AnimationState;

public interface PlayerProperties {

    AnimationState getHeartRemovalState();

    void setRemovedHeart(boolean removedHeart);

    boolean removedHeart();

    void setHeartRemovalTicks(int heartRemovalTicks);

    void setBodyPart(int index, boolean value);

    boolean hasBodyPart(int index);

    void setNextBodyPart(BodyPart bodyPart);

    BodyPart getNextBodyPart();
}
