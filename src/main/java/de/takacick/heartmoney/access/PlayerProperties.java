package de.takacick.heartmoney.access;

import de.takacick.heartmoney.registry.entity.custom.HeartShopPortalEntity;

public interface PlayerProperties {

    void setHeart(boolean heart);

    boolean isHeart();

    void addRemoveHearts(int removeHearts);

    int getRemoveHearts();

    void setHeartShopPortal(HeartShopPortalEntity heartShopPortalEntity);

    void setHeartMultiplier(double heartMultiplier);

    double getHeartMultiplier();

    void setBloodRiptideTicks(int bloodRiptideTicks);

    boolean hasBloodRiptide();

}
