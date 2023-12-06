package de.takacick.emeraldmoney.access;

import de.takacick.emeraldmoney.client.gui.EmeraldToast;
import de.takacick.emeraldmoney.registry.entity.custom.EmeraldShopPortalEntity;

import java.util.List;

public interface PlayerProperties {

    void setEmeraldShopPortal(EmeraldShopPortalEntity emeraldShopPortal);

    void setEmeralds(int emeralds);

    int addEmeralds(int emeralds, boolean multiplier);

    int getEmeralds();
    
    void setEmeraldMultiplier(double emeraldsMultiplier);
    
    double getEmeraldMultiplier();

    void setEmeraldWallet(boolean emeraldsWallet);

    boolean hasEmeraldWallet();

    List<EmeraldToast> getEmeraldToasts();

}
