package de.takacick.upgradebody.access;

import de.takacick.upgradebody.registry.bodypart.BodyPart;
import de.takacick.upgradebody.registry.bodypart.BodyPartManager;
import de.takacick.upgradebody.registry.entity.custom.UpgradeShopPortalEntity;

public interface PlayerProperties {

    void setUpgradeShopPortal(UpgradeShopPortalEntity upgradeShopPortalEntity);

    void setUpgrading(boolean upgrading);

    boolean isUpgrading();

    void setBodyPartManager(BodyPartManager bodyPartManager);

    BodyPartManager getBodyPartManager();

    void setBodyPart(BodyPart bodyPart, boolean enable);

    boolean hasBodyPart(BodyPart bodyPart);

    float getStretch();

    float getLastStretch();

    void setHeadbutt(boolean headbutt);

    boolean isUsingHeadbutt();

    void setEnergyBellyBlast(boolean energyBellyBlast);

    boolean isUsingEnergyBellyBlast();

    int getEnergyBellyBlastUsageTicks();

    void setKillerDrilling(boolean killerDrilling);

    boolean isKillerDrilling();

    void setCyberSlice(boolean cyberSlice);

    boolean isUsingCyberSlice();

    void setCyberChainsawTicks(int cyberChainsawTicks);

    boolean hasCyberChainsawAnimation();

}
