package de.takacick.raidbase.registry;

import de.takacick.raidbase.RaidBase;
import de.takacick.raidbase.registry.block.*;
import de.takacick.raidbase.registry.block.fluid.LightningWaterFluid;
import de.takacick.raidbase.registry.item.BeaconDeathLaserItem;
import de.takacick.raidbase.registry.item.Lightning;
import de.takacick.raidbase.registry.item.SelfBanHammer;
import de.takacick.raidbase.registry.item.SlimeSuit;
import net.minecraft.block.*;
import net.minecraft.block.enums.Instrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ItemRegistry {
    public static final FlowableFluid STILL_LIGHTNING_WATER = new LightningWaterFluid.Still();
    public static final FlowableFluid FLOWING_LIGHTNING_WATER = new LightningWaterFluid.Flowing();
    public static final FluidBlock LIGHTNING_WATER = new LightningWaterBlock(STILL_LIGHTNING_WATER, AbstractBlock.Settings.create().mapColor(MapColor.PALE_GREEN).replaceable().noCollision().strength(100.0f).pistonBehavior(PistonBehavior.DESTROY).dropsNothing().liquid().sounds(BlockSoundGroup.INTENTIONALLY_EMPTY));

    public static final Block PIE_LAUNCHER = new PieLauncherBlock(AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).instrument(Instrument.BASEDRUM).requiresTool().strength(3.5f));
    public static final Item PIE_LAUNCHER_ITEM = new BlockItem(PIE_LAUNCHER, new Item.Settings());
    public static final Item PIE = new Item(new Item.Settings().food(new FoodComponent.Builder().hunger(6).saturationModifier(1.2f).alwaysEdible().build()));
    public static final Block GLITCHY_QUICKSAND = new GlitchyQuicksand(AbstractBlock.Settings.create().mapColor(MapColor.PALE_YELLOW).instrument(Instrument.SNARE).strength(0.5f).sounds(BlockSoundGroup.SAND));
    public static final Item GLITCHY_QUICKSAND_ITEM = new BlockItem(GLITCHY_QUICKSAND, new Item.Settings());
    public static final Block BEACON_DEATH_LASER = new BeaconDeathLaserBlock(AbstractBlock.Settings.create().mapColor(MapColor.BRIGHT_RED).instrument(Instrument.HAT).strength(3.0f).luminance(state -> 15).nonOpaque().solidBlock(Blocks::never).dynamicBounds());
    public static final Item BEACON_DEATH_LASER_ITEM = new BeaconDeathLaserItem(BEACON_DEATH_LASER, new Item.Settings());
    public static final Item BUCKET_OF_ELECTRO_WATER = new BucketItem(STILL_LIGHTNING_WATER, new Item.Settings().recipeRemainder(Items.BUCKET).maxCount(1));
    public static final Item SLIME_SUIT = new SlimeSuit(new Item.Settings().maxCount(1));
    public static final Item LIGHTNING = new Lightning(new Item.Settings().maxCount(1));
    public static final Item SELF_BAN_HAMMER = new SelfBanHammer(new Item.Settings().maxCount(1));

    public static final Block COPPER_HOPPER = new CopperHopperBlock(Oxidizable.OxidationLevel.UNAFFECTED, AbstractBlock.Settings.create().mapColor(MapColor.ORANGE).requiresTool().strength(3.0f, 6.0f).sounds(BlockSoundGroup.COPPER));
    public static final Item COPPER_HOPPER_ITEM = new BlockItem(COPPER_HOPPER, new Item.Settings());
    public static final Block EXPOSED_COPPER_HOPPER = new CopperHopperBlock(Oxidizable.OxidationLevel.EXPOSED, AbstractBlock.Settings.create().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).requiresTool().strength(3.0f, 6.0f).sounds(BlockSoundGroup.COPPER));
    public static final Item EXPOSED_COPPER_HOPPER_ITEM = new BlockItem(EXPOSED_COPPER_HOPPER, new Item.Settings());
    public static final Block WEATHERED_COPPER_HOPPER = new CopperHopperBlock(Oxidizable.OxidationLevel.WEATHERED, AbstractBlock.Settings.create().mapColor(MapColor.DARK_AQUA).requiresTool().strength(3.0f, 6.0f).sounds(BlockSoundGroup.COPPER));
    public static final Item WEATHERED_COPPER_HOPPER_ITEM = new BlockItem(WEATHERED_COPPER_HOPPER, new Item.Settings());
    public static final Block OXIDIZED_COPPER_HOPPER = new CopperHopperBlock(Oxidizable.OxidationLevel.OXIDIZED, AbstractBlock.Settings.create().mapColor(MapColor.TEAL).requiresTool().strength(3.0f, 6.0f).sounds(BlockSoundGroup.COPPER));
    public static final Item OXIDIZED_COPPER_HOPPER_ITEM = new BlockItem(OXIDIZED_COPPER_HOPPER, new Item.Settings());
    public static final Block WAXED_COPPER_HOPPER = new CopperHopperBlock(Oxidizable.OxidationLevel.UNAFFECTED, AbstractBlock.Settings.create().mapColor(MapColor.ORANGE).requiresTool().strength(3.0f, 6.0f).sounds(BlockSoundGroup.COPPER));
    public static final Item WAXED_COPPER_HOPPER_ITEM = new BlockItem(WAXED_COPPER_HOPPER, new Item.Settings());
    public static final Block WAXED_EXPOSED_COPPER_HOPPER = new CopperHopperBlock(Oxidizable.OxidationLevel.EXPOSED, AbstractBlock.Settings.create().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).requiresTool().strength(3.0f, 6.0f).sounds(BlockSoundGroup.COPPER));
    public static final Item WAXED_EXPOSED_COPPER_HOPPER_ITEM = new BlockItem(WAXED_EXPOSED_COPPER_HOPPER, new Item.Settings());
    public static final Block WAXED_WEATHERED_COPPER_HOPPER = new CopperHopperBlock(Oxidizable.OxidationLevel.WEATHERED, AbstractBlock.Settings.create().mapColor(MapColor.DARK_AQUA).requiresTool().strength(3.0f, 6.0f).sounds(BlockSoundGroup.COPPER));
    public static final Item WAXED_WEATHERED_COPPER_HOPPER_ITEM = new BlockItem(WAXED_WEATHERED_COPPER_HOPPER, new Item.Settings());
    public static final Block WAXED_OXIDIZED_COPPER_HOPPER = new CopperHopperBlock(Oxidizable.OxidationLevel.OXIDIZED, AbstractBlock.Settings.create().mapColor(MapColor.TEAL).requiresTool().strength(3.0f, 6.0f).sounds(BlockSoundGroup.COPPER));
    public static final Item WAXED_OXIDIZED_COPPER_HOPPER_ITEM = new BlockItem(WAXED_OXIDIZED_COPPER_HOPPER, new Item.Settings());

    public static void register() {
        Registry.register(Registries.BLOCK, new Identifier(RaidBase.MOD_ID, "lightning_water"), LIGHTNING_WATER);
        Registry.register(Registries.FLUID, new Identifier(RaidBase.MOD_ID, "still_lightning_water"), STILL_LIGHTNING_WATER);
        Registry.register(Registries.FLUID, new Identifier(RaidBase.MOD_ID, "flowing_lightning_water"), FLOWING_LIGHTNING_WATER);

        Registry.register(Registries.BLOCK, new Identifier(RaidBase.MOD_ID, "pie_launcher"), PIE_LAUNCHER);
        Registry.register(Registries.ITEM, new Identifier(RaidBase.MOD_ID, "pie_launcher"), PIE_LAUNCHER_ITEM);
        Registry.register(Registries.ITEM, new Identifier(RaidBase.MOD_ID, "pie"), PIE);
        Registry.register(Registries.BLOCK, new Identifier(RaidBase.MOD_ID, "glitchy_quicksand"), GLITCHY_QUICKSAND);
        Registry.register(Registries.ITEM, new Identifier(RaidBase.MOD_ID, "glitchy_quicksand"), GLITCHY_QUICKSAND_ITEM);
        Registry.register(Registries.BLOCK, new Identifier(RaidBase.MOD_ID, "beacon_death_laser"), BEACON_DEATH_LASER);
        Registry.register(Registries.ITEM, new Identifier(RaidBase.MOD_ID, "beacon_death_laser"), BEACON_DEATH_LASER_ITEM);
        Registry.register(Registries.ITEM, new Identifier(RaidBase.MOD_ID, "bucket_of_electro_water"), BUCKET_OF_ELECTRO_WATER);
        Registry.register(Registries.ITEM, new Identifier(RaidBase.MOD_ID, "slime_suit"), SLIME_SUIT);
        Registry.register(Registries.ITEM, new Identifier(RaidBase.MOD_ID, "lightning"), LIGHTNING);
        Registry.register(Registries.ITEM, new Identifier(RaidBase.MOD_ID, "self_ban_hammer"), SELF_BAN_HAMMER);
        Registry.register(Registries.BLOCK, new Identifier(RaidBase.MOD_ID, "copper_hopper"), COPPER_HOPPER);
        Registry.register(Registries.ITEM, new Identifier(RaidBase.MOD_ID, "copper_hopper"), COPPER_HOPPER_ITEM);
        Registry.register(Registries.BLOCK, new Identifier(RaidBase.MOD_ID, "exposed_copper_hopper"), EXPOSED_COPPER_HOPPER);
        Registry.register(Registries.ITEM, new Identifier(RaidBase.MOD_ID, "exposed_copper_hopper"), EXPOSED_COPPER_HOPPER_ITEM);
        Registry.register(Registries.BLOCK, new Identifier(RaidBase.MOD_ID, "weathered_copper_hopper"), WEATHERED_COPPER_HOPPER);
        Registry.register(Registries.ITEM, new Identifier(RaidBase.MOD_ID, "weathered_copper_hopper"), WEATHERED_COPPER_HOPPER_ITEM);
        Registry.register(Registries.BLOCK, new Identifier(RaidBase.MOD_ID, "oxidized_copper_hopper"), OXIDIZED_COPPER_HOPPER);
        Registry.register(Registries.ITEM, new Identifier(RaidBase.MOD_ID, "oxidized_copper_hopper"), OXIDIZED_COPPER_HOPPER_ITEM);
        Registry.register(Registries.BLOCK, new Identifier(RaidBase.MOD_ID, "waxed_copper_hopper"), WAXED_COPPER_HOPPER);
        Registry.register(Registries.ITEM, new Identifier(RaidBase.MOD_ID, "waxed_copper_hopper"), WAXED_COPPER_HOPPER_ITEM);
        Registry.register(Registries.BLOCK, new Identifier(RaidBase.MOD_ID, "waxed_exposed_copper_hopper"), WAXED_EXPOSED_COPPER_HOPPER);
        Registry.register(Registries.ITEM, new Identifier(RaidBase.MOD_ID, "waxed_exposed_copper_hopper"), WAXED_EXPOSED_COPPER_HOPPER_ITEM);
        Registry.register(Registries.BLOCK, new Identifier(RaidBase.MOD_ID, "waxed_weathered_copper_hopper"), WAXED_WEATHERED_COPPER_HOPPER);
        Registry.register(Registries.ITEM, new Identifier(RaidBase.MOD_ID, "waxed_weathered_copper_hopper"), WAXED_WEATHERED_COPPER_HOPPER_ITEM);
        Registry.register(Registries.BLOCK, new Identifier(RaidBase.MOD_ID, "waxed_oxidized_copper_hopper"), WAXED_OXIDIZED_COPPER_HOPPER);
        Registry.register(Registries.ITEM, new Identifier(RaidBase.MOD_ID, "waxed_oxidized_copper_hopper"), WAXED_OXIDIZED_COPPER_HOPPER_ITEM);
    }
}