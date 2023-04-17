package de.takacick.stealbodyparts.registry;

import de.takacick.stealbodyparts.StealBodyParts;
import de.takacick.stealbodyparts.registry.item.HeadItem;
import de.takacick.stealbodyparts.registry.item.IronHeartCarver;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemRegistry {

    public static final Item HEAD = new HeadItem(new Item.Settings().maxCount(1));
    public static final Item IRON_HEART_CARVER = new IronHeartCarver(new Item.Settings().maxCount(1).group(StealBodyParts.ITEM_GROUP));
    public static final Item HEART = new Item(new Item.Settings().group(StealBodyParts.ITEM_GROUP));

    public static void register() {
        Registry.register(Registry.ITEM, new Identifier(StealBodyParts.MOD_ID, "head"), HEAD);
        Registry.register(Registry.ITEM, new Identifier(StealBodyParts.MOD_ID, "iron_heart_carver"), IRON_HEART_CARVER);
        Registry.register(Registry.ITEM, new Identifier(StealBodyParts.MOD_ID, "heart"), HEART);
    }
}