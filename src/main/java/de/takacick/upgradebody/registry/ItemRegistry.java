package de.takacick.upgradebody.registry;

import de.takacick.upgradebody.UpgradeBody;
import de.takacick.upgradebody.registry.item.*;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ItemRegistry {

    public static final Item UPGRADE_SHOP_PORTAL = new UpradeShopPortal(new Item.Settings().maxCount(1));
    public static final Item TANK_TRACKS = new TankTracks(new Item.Settings().maxCount(1));
    public static final Item ENERGY_BELLY_CANNON = new EnergyBellyCannon(new Item.Settings().maxCount(1));
    public static final Item KILLER_DRILLER = new KillerDriller(new Item.Settings().maxCount(1));
    public static final Item CYBER_CHAINSAWS = new CyberChainsaws(new Item.Settings().maxCount(1));
    public static final Item SENTIENT_DESERT_TEMPLE = new Item(new Item.Settings().maxCount(1));

    public static void register() {
        Registry.register(Registries.ITEM, new Identifier(UpgradeBody.MOD_ID, "upgrade_shop_portal"), UPGRADE_SHOP_PORTAL);
        Registry.register(Registries.ITEM, new Identifier(UpgradeBody.MOD_ID, "tank_tracks"), TANK_TRACKS);
        Registry.register(Registries.ITEM, new Identifier(UpgradeBody.MOD_ID, "energy_belly_cannon"), ENERGY_BELLY_CANNON);
        Registry.register(Registries.ITEM, new Identifier(UpgradeBody.MOD_ID, "killer_driller"), KILLER_DRILLER);
        Registry.register(Registries.ITEM, new Identifier(UpgradeBody.MOD_ID, "cyber_chainsaws"), CYBER_CHAINSAWS);
        Registry.register(Registries.ITEM, new Identifier(UpgradeBody.MOD_ID, "sentient_desert_temple"), SENTIENT_DESERT_TEMPLE);

    }
}