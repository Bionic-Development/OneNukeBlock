package de.takacick.everythinghearts.registry;

import de.takacick.everythinghearts.EverythingHearts;
import de.takacick.everythinghearts.registry.block.*;
import de.takacick.everythinghearts.registry.item.*;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.item.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemRegistry {

    public static final Item MYSTIC_HEART_TOUCH = new MysticHeartTouch(new Item.Settings().group(EverythingHearts.ITEM_GROUP).maxCount(1));
    public static final Item HEART = new HeartItem(new Item.Settings().group(EverythingHearts.ITEM_GROUP));
    public static final Block BASIC_HEART_BLOCK = new BasicHeartBlock(AbstractBlock.Settings.of(Material.STONE, MapColor.BRIGHT_RED).strength(0.35f));
    public static final Item BASIC_HEART_BLOCK_ITEM = new BlockItem(BASIC_HEART_BLOCK, new Item.Settings().group(EverythingHearts.ITEM_GROUP));
    public static final Block MULTI_HEART_BLOCK = new Block(AbstractBlock.Settings.of(Material.STONE, MapColor.BRIGHT_RED).strength(0.35f));
    public static final Item MULTI_HEART_BLOCK_ITEM = new BlockItem(MULTI_HEART_BLOCK, new Item.Settings().group(EverythingHearts.ITEM_GROUP));
    public static final Block HEART_LOG = new PillarBlock(AbstractBlock.Settings.of(Material.WOOD, MapColor.BRIGHT_RED).strength(2.0f).sounds(BlockSoundGroup.WOOD));
    public static final Item HEART_LOG_ITEM = new BlockItem(HEART_LOG, new Item.Settings().group(EverythingHearts.ITEM_GROUP));
    public static final Block HEART_PLANKS = new Block(AbstractBlock.Settings.of(Material.WOOD, MapColor.BRIGHT_RED).strength(2.0f, 3.0f).sounds(BlockSoundGroup.WOOD));
    public static final Item HEART_PLANKS_ITEM = new BlockItem(HEART_PLANKS, new Item.Settings().group(EverythingHearts.ITEM_GROUP));
    public static final Block HEART_LEAVES = new HeartLeavesBlock(AbstractBlock.Settings.of(Material.LEAVES, MapColor.BRIGHT_RED).strength(0.2f).ticksRandomly().sounds(BlockSoundGroup.GRASS).nonOpaque().allowsSpawning((state, world, pos, type) -> type == EntityType.OCELOT || type == EntityType.PARROT).suffocates((state, world, pos) -> false).blockVision((state, world, pos) -> false));
    public static final Item HEART_LEAVES_ITEM = new BlockItem(HEART_LEAVES, new Item.Settings().group(EverythingHearts.ITEM_GROUP));
    public static final Block HEART_CRAFTING_TABLE = new HeartCraftingTableBlock(AbstractBlock.Settings.of(Material.WOOD, MapColor.BRIGHT_RED).strength(2.5f).sounds(BlockSoundGroup.WOOD));
    public static final Item HEART_CRAFTING_TABLE_ITEM = new BlockItem(HEART_CRAFTING_TABLE, new Item.Settings().group(EverythingHearts.ITEM_GROUP));
    public static final Item WOODEN_HEART_SWORD = new SwordItem(ToolMaterials.WOOD, 6, -2.4f, new Item.Settings().group(EverythingHearts.ITEM_GROUP));
    public static final Item WOODEN_HEART_PICKAXE = new WoodenHeartPickaxe(ToolMaterials.IRON, 1, -2.8f, new Item.Settings().group(EverythingHearts.ITEM_GROUP));
    public static final Block HEARTBALE = new HayBlock(AbstractBlock.Settings.of(Material.SOLID_ORGANIC, MapColor.BRIGHT_RED).strength(0.5f).sounds(BlockSoundGroup.GRASS));
    public static final Item HEARTBALE_ITEM = new BlockItem(HEARTBALE, new Item.Settings().group(EverythingHearts.ITEM_GROUP));
    public static final Item HEART_BREAD = new HeartBread(new Item.Settings().group(EverythingHearts.ITEM_GROUP));
    public static final Block HEART_CHEST = new HeartChestBlock(AbstractBlock.Settings.of(Material.WOOD, MapColor.BRIGHT_RED).strength(2.5F).sounds(BlockSoundGroup.WOOD));
    public static final Item HEART_CHEST_ITEM = new BlockItem(HEART_CHEST, new Item.Settings().group(EverythingHearts.ITEM_GROUP));
    public static final Block HEARTMOND_ORE = new HeartmondOreBlock(AbstractBlock.Settings.of(Material.STONE, MapColor.BRIGHT_RED).requiresTool().strength(3.0f, 3.0f));
    public static final Item HEARTMOND_ORE_ITEM = new BlockItem(HEARTMOND_ORE, new Item.Settings().group(EverythingHearts.ITEM_GROUP));
    public static final Block DEEPSLATE_HEARTMOND_ORE = new HeartmondOreBlock(AbstractBlock.Settings.of(Material.STONE, MapColor.BRIGHT_RED).requiresTool().strength(4.5f, 3.0f).sounds(BlockSoundGroup.DEEPSLATE));
    public static final Item DEEPSLATE_HEARTMOND_ORE_ITEM = new BlockItem(DEEPSLATE_HEARTMOND_ORE, new Item.Settings().group(EverythingHearts.ITEM_GROUP));
    public static final Item HEARTMOND = new Heartmond(new Item.Settings().group(EverythingHearts.ITEM_GROUP));
    public static final Item HEARTMOND_BLASTER = new HeartmondBlaster(new Item.Settings().group(EverythingHearts.ITEM_GROUP).maxCount(1));
    public static final Item LOVER_TOTEM = new LoverTotem(new Item.Settings().group(EverythingHearts.ITEM_GROUP).maxCount(1));
    public static final Item HEART_SCYTHE = new HeartScythe(ToolMaterials.DIAMOND, 7, -2.4f, new Item.Settings().group(EverythingHearts.ITEM_GROUP).maxCount(1));
    public static final Block WEATHER_HEART_BEACON = new WeatherHeartBeaconBlock(AbstractBlock.Settings.of(Material.GLASS, MapColor.RED).strength(3.0F).luminance((state) -> 15).nonOpaque().solidBlock((state, world, pos) -> false));
    public static final Item WEATHER_HEART_BEACON_ITEM = new BlockItem(WEATHER_HEART_BEACON, new Item.Settings().group(EverythingHearts.ITEM_GROUP));

    public static void register() {
        Registry.register(Registry.ITEM, new Identifier(EverythingHearts.MOD_ID, "mystic_heart_touch"), MYSTIC_HEART_TOUCH);
        Registry.register(Registry.ITEM, new Identifier(EverythingHearts.MOD_ID, "heart"), HEART);
        Registry.register(Registry.BLOCK, new Identifier(EverythingHearts.MOD_ID, "basic_heart_block"), BASIC_HEART_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(EverythingHearts.MOD_ID, "basic_heart_block"), BASIC_HEART_BLOCK_ITEM);
        Registry.register(Registry.BLOCK, new Identifier(EverythingHearts.MOD_ID, "multi_heart_block"), MULTI_HEART_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(EverythingHearts.MOD_ID, "multi_heart_block"), MULTI_HEART_BLOCK_ITEM);
        Registry.register(Registry.BLOCK, new Identifier(EverythingHearts.MOD_ID, "heart_log"), HEART_LOG);
        Registry.register(Registry.ITEM, new Identifier(EverythingHearts.MOD_ID, "heart_log"), HEART_LOG_ITEM);
        Registry.register(Registry.BLOCK, new Identifier(EverythingHearts.MOD_ID, "heart_planks"), HEART_PLANKS);
        Registry.register(Registry.ITEM, new Identifier(EverythingHearts.MOD_ID, "heart_planks"), HEART_PLANKS_ITEM);
        Registry.register(Registry.BLOCK, new Identifier(EverythingHearts.MOD_ID, "heart_leaves"), HEART_LEAVES);
        Registry.register(Registry.ITEM, new Identifier(EverythingHearts.MOD_ID, "heart_leaves"), HEART_LEAVES_ITEM);
        Registry.register(Registry.BLOCK, new Identifier(EverythingHearts.MOD_ID, "heart_crafting_table"), HEART_CRAFTING_TABLE);
        Registry.register(Registry.ITEM, new Identifier(EverythingHearts.MOD_ID, "heart_crafting_table"), HEART_CRAFTING_TABLE_ITEM);
        Registry.register(Registry.ITEM, new Identifier(EverythingHearts.MOD_ID, "wooden_heart_sword"), WOODEN_HEART_SWORD);
        Registry.register(Registry.ITEM, new Identifier(EverythingHearts.MOD_ID, "wooden_heart_pickaxe"), WOODEN_HEART_PICKAXE);
        Registry.register(Registry.BLOCK, new Identifier(EverythingHearts.MOD_ID, "heartbale"), HEARTBALE);
        Registry.register(Registry.ITEM, new Identifier(EverythingHearts.MOD_ID, "heartbale"), HEARTBALE_ITEM);
        Registry.register(Registry.ITEM, new Identifier(EverythingHearts.MOD_ID, "heart_bread"), HEART_BREAD);
        Registry.register(Registry.BLOCK, new Identifier(EverythingHearts.MOD_ID, "heart_chest"), HEART_CHEST);
        Registry.register(Registry.ITEM, new Identifier(EverythingHearts.MOD_ID, "heart_chest"), HEART_CHEST_ITEM);
        Registry.register(Registry.BLOCK, new Identifier(EverythingHearts.MOD_ID, "heartmond_ore"), HEARTMOND_ORE);
        Registry.register(Registry.ITEM, new Identifier(EverythingHearts.MOD_ID, "heartmond_ore"), HEARTMOND_ORE_ITEM);
        Registry.register(Registry.BLOCK, new Identifier(EverythingHearts.MOD_ID, "deepslate_heartmond_ore"), DEEPSLATE_HEARTMOND_ORE);
        Registry.register(Registry.ITEM, new Identifier(EverythingHearts.MOD_ID, "deepslate_heartmond_ore"), DEEPSLATE_HEARTMOND_ORE_ITEM);
        Registry.register(Registry.ITEM, new Identifier(EverythingHearts.MOD_ID, "heartmond"), HEARTMOND);
        Registry.register(Registry.ITEM, new Identifier(EverythingHearts.MOD_ID, "heartmond_blaster"), HEARTMOND_BLASTER);
        Registry.register(Registry.ITEM, new Identifier(EverythingHearts.MOD_ID, "lover_totem"), LOVER_TOTEM);
        Registry.register(Registry.ITEM, new Identifier(EverythingHearts.MOD_ID, "heart_scythe"), HEART_SCYTHE);
        Registry.register(Registry.BLOCK, new Identifier(EverythingHearts.MOD_ID, "weather_heart_beacon"), WEATHER_HEART_BEACON);
        Registry.register(Registry.ITEM, new Identifier(EverythingHearts.MOD_ID, "weather_heart_beacon"), WEATHER_HEART_BEACON_ITEM);
    }
}