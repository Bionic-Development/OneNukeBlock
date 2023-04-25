package de.takacick.everythinghearts.access;

public interface PlayerProperties {

    void setHeart(boolean heart);

    boolean isHeart();

    void setHeartTouch(boolean heartTouch);

    boolean hasHeartTouch();

    void setHeartTouchLevel(int heartTouchLevel);

    int getHeartTouchLevel();

    void setHeartTransformTicks(int heartTransformTicks);

    int getHeartTransformTicks();
}
