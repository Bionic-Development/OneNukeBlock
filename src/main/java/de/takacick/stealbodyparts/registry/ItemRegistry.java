package de.takacick.stealbodyparts.registry;

import de.takacick.stealbodyparts.StealBodyParts;
import de.takacick.stealbodyparts.registry.item.AliveMoldingBody;
import de.takacick.stealbodyparts.registry.item.BodyPartItem;
import de.takacick.stealbodyparts.registry.item.IronHeartCarver;
import de.takacick.stealbodyparts.registry.item.IronScalpel;
import de.takacick.stealbodyparts.utils.BodyPart;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemRegistry {

    public static final Item IRON_HEART_CARVER = new IronHeartCarver(new Item.Settings().group(StealBodyParts.ITEM_GROUP).maxCount(1));
    public static final Item ALIVE_MOLDING_BODY = new AliveMoldingBody(new Item.Settings().group(StealBodyParts.ITEM_GROUP).maxCount(1));
    public static final Item IRON_SCALPEL = new IronScalpel(new Item.Settings().group(StealBodyParts.ITEM_GROUP).maxCount(1));
    public static final Item HEART = new Item(new Item.Settings().group(StealBodyParts.ITEM_GROUP));
    public static final Item HEAD = new BodyPartItem(BodyPart.HEAD, new Item.Settings().group(StealBodyParts.ITEM_GROUP).maxCount(1));
    public static final Item RIGHT_ARM = new BodyPartItem(BodyPart.RIGHT_ARM, new Item.Settings().group(StealBodyParts.ITEM_GROUP).maxCount(1));
    public static final Item LEFT_ARM = new BodyPartItem(BodyPart.LEFT_ARM, new Item.Settings().group(StealBodyParts.ITEM_GROUP).maxCount(1));
    public static final Item RIGHT_LEG = new BodyPartItem(BodyPart.RIGHT_LEG, new Item.Settings().group(StealBodyParts.ITEM_GROUP).maxCount(1));
    public static final Item LEFT_LEG = new BodyPartItem(BodyPart.LEFT_LEG, new Item.Settings().group(StealBodyParts.ITEM_GROUP).maxCount(1));

    public static void register() {
        Registry.register(Registry.ITEM, new Identifier(StealBodyParts.MOD_ID, "iron_heart_carver"), IRON_HEART_CARVER);
        Registry.register(Registry.ITEM, new Identifier(StealBodyParts.MOD_ID, "alive_molding_body"), ALIVE_MOLDING_BODY);
        Registry.register(Registry.ITEM, new Identifier(StealBodyParts.MOD_ID, "iron_scalpel"), IRON_SCALPEL);
        Registry.register(Registry.ITEM, new Identifier(StealBodyParts.MOD_ID, "heart"), HEART);
        Registry.register(Registry.ITEM, new Identifier(StealBodyParts.MOD_ID, "head"), HEAD);
        Registry.register(Registry.ITEM, new Identifier(StealBodyParts.MOD_ID, "right_arm"), RIGHT_ARM);
        Registry.register(Registry.ITEM, new Identifier(StealBodyParts.MOD_ID, "left_arm"), LEFT_ARM);
        Registry.register(Registry.ITEM, new Identifier(StealBodyParts.MOD_ID, "right_leg"), RIGHT_LEG);
        Registry.register(Registry.ITEM, new Identifier(StealBodyParts.MOD_ID, "left_leg"), LEFT_LEG);
    }
}