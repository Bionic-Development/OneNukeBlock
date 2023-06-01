package de.takacick.deathmoney.access;

import net.minecraft.world.World;

public interface EntityProperties {

    void setDeathShopPortal(World world, double x, double y, double z, boolean shopPortal);

    void setDeathShopPortalCooldown(int portalCooldown);

    boolean isOnDeathShopPortalCooldown();

    void teleportBackFromDeathShop();
}
