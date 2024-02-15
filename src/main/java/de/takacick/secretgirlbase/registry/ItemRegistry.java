package de.takacick.secretgirlbase.registry;

import de.takacick.secretgirlbase.SecretGirlBase;
import de.takacick.secretgirlbase.registry.block.*;
import de.takacick.secretgirlbase.registry.block.RedButtonBlock;
import de.takacick.secretgirlbase.registry.item.*;
import net.minecraft.block.*;
import net.minecraft.block.enums.Instrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

public class ItemRegistry {

    public static final Item ZUKO = new Zuko(new Item.Settings().maxCount(1));
    public static final Item PHONE = new Phone(new Item.Settings().maxCount(1));
    public static final Block MAGIC_FLOWER_DOOR_GRASS_BLOCK = new MagicFlowerDoorGrassBlock(AbstractBlock.Settings.create().mapColor(MapColor.PALE_GREEN).ticksRandomly().strength(0.6f).sounds(BlockSoundGroup.GRASS).pistonBehavior(PistonBehavior.BLOCK).dynamicBounds());
    public static final Block MAGIC_FLOWER_DOOR_POPPY = new MagicFlowerDoorPoppyBlock(AbstractBlock.Settings.create().mapColor(MapColor.DARK_GREEN).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS).pistonBehavior(PistonBehavior.DESTROY).pistonBehavior(PistonBehavior.BLOCK).dynamicBounds().noCollision());
    public static final Item MAGIC_FLOWER_DOOR = new MagicFlowerDoor(MAGIC_FLOWER_DOOR_GRASS_BLOCK, new Item.Settings());
    public static final Block MAGIC_DISAPPEARING_PLATFORM = new MagicDisappearingPlatformBlock(AbstractBlock.Settings.create().mapColor(DyeColor.WHITE).instrument(Instrument.BASEDRUM).requiresTool().strength(1.8f).pistonBehavior(PistonBehavior.BLOCK).dynamicBounds().solidBlock(Blocks::never).nonOpaque().solidBlock(Blocks::never).blockVision(Blocks::never));
    public static final Item MAGIC_DISAPPEARING_PLATFORM_ITEM = new MagicDisappearingPlatformItem(MAGIC_DISAPPEARING_PLATFORM, new Item.Settings());
    public static final Block BUBBLE_GUM_LAUNCHER = new BubbleGumLauncherBlock(AbstractBlock.Settings.create().mapColor(MapColor.PINK).instrument(Instrument.BASEDRUM).requiresTool().strength(3.5f));
    public static final Item BUBBLE_GUM_LAUNCHER_ITEM = new BlockItem(BUBBLE_GUM_LAUNCHER, new Item.Settings());
    public static final Block TEENY_BERRY_BUSH = new TeenyBerryBush(AbstractBlock.Settings.create().mapColor(MapColor.DARK_GREEN).ticksRandomly().noCollision().sounds(BlockSoundGroup.SWEET_BERRY_BUSH).pistonBehavior(PistonBehavior.DESTROY));
    public static final Item TEENY_BERRIES = new TeenyBerries(TEENY_BERRY_BUSH, new Item.Settings().food(new FoodComponent.Builder().hunger(2).saturationModifier(0.1F).alwaysEdible().build()));
    public static final Block GREAT_VINES = new GreatVinesHeadBlock(AbstractBlock.Settings.create().mapColor(MapColor.DARK_GREEN).ticksRandomly().noCollision().luminance(CaveVines.getLuminanceSupplier(14)).breakInstantly().sounds(BlockSoundGroup.CAVE_VINES).pistonBehavior(PistonBehavior.DESTROY));
    public static final Block GREAT_VINES_PLANT = new GreatVinesBodyBlock(AbstractBlock.Settings.create().mapColor(MapColor.DARK_GREEN).noCollision().luminance(CaveVines.getLuminanceSupplier(14)).breakInstantly().sounds(BlockSoundGroup.CAVE_VINES).pistonBehavior(PistonBehavior.DESTROY));
    public static final Item GREAT_BERRIES = new GreatBerries(GREAT_VINES, new Item.Settings().food(new FoodComponent.Builder().hunger(2).saturationModifier(0.1F).alwaysEdible().build()));
    public static final Block RED_BUTTON = new RedButtonBlock(AbstractBlock.Settings.copy(Blocks.STONE).strength(-1.0f, 3600000.0f));
    public static final Item RED_BUTTON_ITEM = new BlockItem(RED_BUTTON, new Item.Settings());
    public static final Item LEAD_CUFFS = new LeadCuffs(new Item.Settings().maxCount(1));
    public static final Item FIREWORK_TIME_BOMB = new FireworkTimeBomb(new Item.Settings().maxCount(1));

    public static void register() {
        Registry.register(Registries.ITEM, new Identifier(SecretGirlBase.MOD_ID, "zuko"), ZUKO);
        Registry.register(Registries.ITEM, new Identifier(SecretGirlBase.MOD_ID, "phone"), PHONE);
        Registry.register(Registries.BLOCK, new Identifier(SecretGirlBase.MOD_ID, "magic_flower_door_grass_block"), MAGIC_FLOWER_DOOR_GRASS_BLOCK);
        Registry.register(Registries.BLOCK, new Identifier(SecretGirlBase.MOD_ID, "magic_flower_door_poppy"), MAGIC_FLOWER_DOOR_POPPY);
        Registry.register(Registries.ITEM, new Identifier(SecretGirlBase.MOD_ID, "magic_flower_door"), MAGIC_FLOWER_DOOR);
        Registry.register(Registries.BLOCK, new Identifier(SecretGirlBase.MOD_ID, "magic_disappearing_platform"), MAGIC_DISAPPEARING_PLATFORM);
        Registry.register(Registries.ITEM, new Identifier(SecretGirlBase.MOD_ID, "magic_disappearing_platform"), MAGIC_DISAPPEARING_PLATFORM_ITEM);
        Registry.register(Registries.BLOCK, new Identifier(SecretGirlBase.MOD_ID, "bubble_gum_launcher"), BUBBLE_GUM_LAUNCHER);
        Registry.register(Registries.ITEM, new Identifier(SecretGirlBase.MOD_ID, "bubble_gum_launcher"), BUBBLE_GUM_LAUNCHER_ITEM);
        Registry.register(Registries.BLOCK, new Identifier(SecretGirlBase.MOD_ID, "teeny_berry_bush"), TEENY_BERRY_BUSH);
        Registry.register(Registries.ITEM, new Identifier(SecretGirlBase.MOD_ID, "teeny_berries"), TEENY_BERRIES);
        Registry.register(Registries.BLOCK, new Identifier(SecretGirlBase.MOD_ID, "great_vines"), GREAT_VINES);
        Registry.register(Registries.BLOCK, new Identifier(SecretGirlBase.MOD_ID, "great_vines_plant"), GREAT_VINES_PLANT);
        Registry.register(Registries.ITEM, new Identifier(SecretGirlBase.MOD_ID, "great_berries"), GREAT_BERRIES);
        Registry.register(Registries.BLOCK, new Identifier(SecretGirlBase.MOD_ID, "red_button"), RED_BUTTON);
        Registry.register(Registries.ITEM, new Identifier(SecretGirlBase.MOD_ID, "red_button"), RED_BUTTON_ITEM);
        Registry.register(Registries.ITEM, new Identifier(SecretGirlBase.MOD_ID, "lead_cuffs"), LEAD_CUFFS);
        Registry.register(Registries.ITEM, new Identifier(SecretGirlBase.MOD_ID, "firework_time_bomb"), FIREWORK_TIME_BOMB);
    }
}