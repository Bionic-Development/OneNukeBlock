package de.takacick.upgradebody.registry;

import de.takacick.upgradebody.UpgradeBody;
import de.takacick.upgradebody.registry.item.EmeraldShopPortal;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ItemRegistry {

    public static final Item EMERALD_SHOP_PORTAL = new EmeraldShopPortal(new Item.Settings().maxCount(1));

    public static void register() {
        Registry.register(Registries.ITEM, new Identifier(UpgradeBody.MOD_ID, "emerald_shop_portal"), EMERALD_SHOP_PORTAL);

    }
}