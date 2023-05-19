package de.takacick.heartmoney.access;

import net.minecraft.world.World;

public interface EntityProperties {

    void setHeartShopPortal(World world, double x, double y, double z, boolean shopPortal);

    void setHeartShopPortalCooldown(int portalCooldown);

    boolean isOnHeartShopPortalCooldown();

    void teleportBackFromHeartShop();
}
