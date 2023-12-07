package de.takacick.upgradebody.access;

import net.minecraft.world.World;

public interface EntityProperties {

    void setEmeraldShopPortal(World world, double x, double y, double z, float yaw, boolean shopPortal);

    void setEmeraldShopPortalCooldown(int portalCooldown);

    boolean isOnEmeraldShopPortalCooldown();

    boolean teleportBackFromEmeraldShop();

}
