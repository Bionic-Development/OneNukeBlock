package de.takacick.emeraldmoney.registry;

import de.takacick.emeraldmoney.EmeraldMoney;
import de.takacick.emeraldmoney.registry.heart.EmeraldHeart;
import de.takacick.emeraldmoney.registry.item.*;
import de.takacick.utils.BionicUtils;
import de.takacick.utils.heart.Heart;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ItemRegistry {

    public static final Heart EMERALD_HEART = BionicUtils.registerHeart(new EmeraldHeart(new Identifier(EmeraldMoney.MOD_ID, "emerald"), true));

    public static final Item EMERALD_SHOP_PORTAL = new EmeraldShopPortal(new Item.Settings().maxCount(1));
    public static final Item VILLAGER_NOSE = new VillagerNose(new Item.Settings());
    public static final Item EMERALD_WALLET = new EmeraldWallet(new Item.Settings().maxCount(1));
    public static final Item EMERALD_GAUNTLET = new EmeraldGauntlet(new Item.Settings().maxCount(1));
    public static final Item VILLAGER_ROBE = new VillagerRobe(new Item.Settings().maxCount(1));
    public static final Item VILLAGER_DRILLER = new VillagerDriller(new Item.Settings().maxCount(1));
    public static final Item EMERALD_HEALTH_JAR = new EmeraldHealthJar(new Item.Settings());
    public static final Item PILLAGER_CANNON = new PillagerCannon(new Item.Settings().maxCount(1));
    public static final Item CREEPAGER_PET = new CreepagerPet(new Item.Settings().maxCount(1));
    public static final Item EMERALD_TOTEM = new EmeraldTotem(new Item.Settings().maxCount(1));
    public static final Item CURSED_EMERALD_STAFF = new CursedEmeraldStaff(new Item.Settings().maxCount(1));

    public static void register() {
        Registry.register(Registries.ITEM, new Identifier(EmeraldMoney.MOD_ID, "emerald_shop_portal"), EMERALD_SHOP_PORTAL);
        Registry.register(Registries.ITEM, new Identifier(EmeraldMoney.MOD_ID, "villager_nose"), VILLAGER_NOSE);
        Registry.register(Registries.ITEM, new Identifier(EmeraldMoney.MOD_ID, "emerald_wallet"), EMERALD_WALLET);
        Registry.register(Registries.ITEM, new Identifier(EmeraldMoney.MOD_ID, "emerald_gauntlet"), EMERALD_GAUNTLET);
        Registry.register(Registries.ITEM, new Identifier(EmeraldMoney.MOD_ID, "villager_robe"), VILLAGER_ROBE);
        Registry.register(Registries.ITEM, new Identifier(EmeraldMoney.MOD_ID, "villager_driller"), VILLAGER_DRILLER);
        Registry.register(Registries.ITEM, new Identifier(EmeraldMoney.MOD_ID, "emerald_health_jar"), EMERALD_HEALTH_JAR);
        Registry.register(Registries.ITEM, new Identifier(EmeraldMoney.MOD_ID, "pillager_cannon"), PILLAGER_CANNON);
        Registry.register(Registries.ITEM, new Identifier(EmeraldMoney.MOD_ID, "creepager_pet"), CREEPAGER_PET);
        Registry.register(Registries.ITEM, new Identifier(EmeraldMoney.MOD_ID, "emerald_totem"), EMERALD_TOTEM);
        Registry.register(Registries.ITEM, new Identifier(EmeraldMoney.MOD_ID, "cursed_emerald_staff"), CURSED_EMERALD_STAFF);
    }
}