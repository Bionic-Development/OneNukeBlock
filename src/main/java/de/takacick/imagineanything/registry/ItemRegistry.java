package de.takacick.imagineanything.registry;

import de.takacick.imagineanything.ImagineAnything;
import de.takacick.imagineanything.registry.block.ImaginedGiantBedrockSpeakersBlock;
import de.takacick.imagineanything.registry.block.ImaginedInfinityTrappedBedBlock;
import de.takacick.imagineanything.registry.block.PoopBlock;
import de.takacick.imagineanything.registry.item.*;
import net.minecraft.block.*;
import net.minecraft.block.enums.BedPart;
import net.minecraft.item.BedItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemRegistry {

    public static final Item HEAD = new HeadItem(new Item.Settings().maxCount(1));
    public static final Item MYSTERIOUS_LAMP = new MysteriousLamp(new Item.Settings().group(ImagineAnything.ITEM_GROUP).maxCount(1));
    public static final Item THOUGHT = new Thought(new Item.Settings().group(ImagineAnything.ITEM_GROUP).maxCount(1));
    public static final Item IMAGINED_GIANT_AXE_METEOR = new ImaginedGiantAxeMeteor(new Item.Settings().group(ImagineAnything.ITEM_GROUP).maxCount(1));
    public static final Item IMAGINED_CAVE_EXTRACTOR = new ImaginedCaveExtractor(new Item.Settings().group(ImagineAnything.ITEM_GROUP).maxCount(1));
    public static final Item IMAGINED_ALFRED_THE_PICKAXE = new ImaginedAlfredThePickaxe(new Item.Settings().group(ImagineAnything.ITEM_GROUP).maxDamage(3));
    public static final Item IMAGINED_MALL_BARTERING = new ImaginedMallBartering(new Item.Settings().group(ImagineAnything.ITEM_GROUP).maxCount(1));
    public static final Item IMAGINED_IRON_MAN = new ImaginedIronMan(new Item.Settings().group(ImagineAnything.ITEM_GROUP).maxCount(1));
    public static final Item IRON_MAN_SUIT = new IronManSuit(new Item.Settings().group(ImagineAnything.ITEM_GROUP).maxCount(1));
    public static final Block IMAGINED_INFINITY_TRAPPED_BED = new ImaginedInfinityTrappedBedBlock(DyeColor.RED, AbstractBlock.Settings.of(Material.WOOL, state -> state.get(BedBlock.PART) == BedPart.FOOT ? DyeColor.RED.getMapColor() : MapColor.WHITE_GRAY).sounds(BlockSoundGroup.WOOD).strength(0.2f).nonOpaque());
    public static final Item IMAGINED_INFINITY_TRAPPED_BED_ITEM = new BedItem(IMAGINED_INFINITY_TRAPPED_BED, new Item.Settings().group(ImagineAnything.ITEM_GROUP));
    public static final Item IMAGINED_GIANT_NETHERITE_FEATHER = new ImaginedGiantNetheriteFeather(new Item.Settings().group(ImagineAnything.ITEM_GROUP).maxCount(1));
    public static final Block IMAGINED_GIANT_BEDROCK_SPEAKERS = new ImaginedGiantBedrockSpeakersBlock(AbstractBlock.Settings.copy(Blocks.BEDROCK));
    public static final Item IMAGINED_GIANT_BEDROCK_SPEAKERS_ITEM = new BlockItem(IMAGINED_GIANT_BEDROCK_SPEAKERS, new Item.Settings().group(ImagineAnything.ITEM_GROUP));
    public static final Item INFINITY_GAUNTLET_DISABLED = new InfinityGauntletDisabled(new Item.Settings().group(ImagineAnything.ITEM_GROUP).maxCount(1));

    public static final Block POOP = new PoopBlock(AbstractBlock.Settings.copy(Blocks.DIRT).luminance(state -> 1).allowsSpawning((state, world, pos, type) -> true).nonOpaque().solidBlock((state, world, pos) -> false).blockVision((state, world, pos) -> false).suffocates((state, world, pos) -> true).sounds(BlockSoundGroup.MUD));
    public static final Item POOP_ITEM = new BlockItem(POOP, new Item.Settings().group(ImagineAnything.ITEM_GROUP));
    public static final Item THINK = new Item(new Item.Settings());
    public static final Item GIANT_AXE = new Item(new Item.Settings());

    public static void register() {
        Registry.register(Registry.ITEM, new Identifier(ImagineAnything.MOD_ID, "head"), HEAD);
        Registry.register(Registry.ITEM, new Identifier(ImagineAnything.MOD_ID, "mysterious_lamp"), MYSTERIOUS_LAMP);
        Registry.register(Registry.ITEM, new Identifier(ImagineAnything.MOD_ID, "thought"), THOUGHT);
        Registry.register(Registry.ITEM, new Identifier(ImagineAnything.MOD_ID, "imagined_giant_axe_meteor"), IMAGINED_GIANT_AXE_METEOR);
        Registry.register(Registry.ITEM, new Identifier(ImagineAnything.MOD_ID, "imagined_cave_extractor"), IMAGINED_CAVE_EXTRACTOR);
        Registry.register(Registry.ITEM, new Identifier(ImagineAnything.MOD_ID, "imagined_alfred_the_pickaxe"), IMAGINED_ALFRED_THE_PICKAXE);
        Registry.register(Registry.ITEM, new Identifier(ImagineAnything.MOD_ID, "imagined_mall_bartering"), IMAGINED_MALL_BARTERING);
        Registry.register(Registry.ITEM, new Identifier(ImagineAnything.MOD_ID, "imagined_iron_man"), IMAGINED_IRON_MAN);
        Registry.register(Registry.ITEM, new Identifier(ImagineAnything.MOD_ID, "iron_man_suit"), IRON_MAN_SUIT);
        Registry.register(Registry.BLOCK, new Identifier(ImagineAnything.MOD_ID, "imagined_infinity_trapped_bed"), IMAGINED_INFINITY_TRAPPED_BED);
        Registry.register(Registry.ITEM, new Identifier(ImagineAnything.MOD_ID, "imagined_infinity_trapped_bed"), IMAGINED_INFINITY_TRAPPED_BED_ITEM);
        Registry.register(Registry.ITEM, new Identifier(ImagineAnything.MOD_ID, "imagined_giant_netherite_feather"), IMAGINED_GIANT_NETHERITE_FEATHER);
        Registry.register(Registry.BLOCK, new Identifier(ImagineAnything.MOD_ID, "imagined_giant_bedrock_speakers"), IMAGINED_GIANT_BEDROCK_SPEAKERS);
        Registry.register(Registry.ITEM, new Identifier(ImagineAnything.MOD_ID, "imagined_giant_bedrock_speakers"), IMAGINED_GIANT_BEDROCK_SPEAKERS_ITEM);
        Registry.register(Registry.ITEM, new Identifier(ImagineAnything.MOD_ID, "infinity_gauntlet_disabled"), INFINITY_GAUNTLET_DISABLED);
        Registry.register(Registry.BLOCK, new Identifier(ImagineAnything.MOD_ID, "poop"), POOP);
        Registry.register(Registry.ITEM, new Identifier(ImagineAnything.MOD_ID, "poop"), POOP_ITEM);
        Registry.register(Registry.ITEM, new Identifier(ImagineAnything.MOD_ID, "think"), THINK);
        Registry.register(Registry.ITEM, new Identifier(ImagineAnything.MOD_ID, "giant_axe"), GIANT_AXE);
    }
}