package de.takacick.tinyhouse.registry;

import de.takacick.tinyhouse.TinyHouse;
import de.takacick.tinyhouse.registry.block.*;
import de.takacick.tinyhouse.registry.item.BlockMagnet;
import de.takacick.tinyhouse.registry.item.FireItem;
import de.takacick.tinyhouse.registry.item.GiantCrusherTrapItem;
import net.minecraft.block.*;
import net.minecraft.block.enums.Instrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ItemRegistry {

    public static final Block TINY_BASE = new TinyBase(AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).noBlockBreakParticles().instrument(Instrument.BASEDRUM).requiresTool().strength(1.5f, 6.0f));
    public static final Item TINY_BASE_ITEM = new BlockItem(TINY_BASE, new Item.Settings().maxCount(1));
    public static final Item BLOCK_MAGNET = new BlockMagnet(new Item.Settings().maxCount(1));
    public static final Block CHICKEN = new ChickenBlock(AbstractBlock.Settings.create().mapColor(MapColor.WHITE).instrument(Instrument.GUITAR).strength(0.8f).sounds(BlockSoundGroup.WOOL).burnable());
    public static final Item CHICKEN_ITEM = new BlockItem(CHICKEN, new Item.Settings().maxCount(1));
    public static final Item FIRE = new FireItem(new Item.Settings());
    public static final Block FREEZING_PLATE_TRAP = new FreezingPlateTrap(AbstractBlock.Settings.create().mapColor(MapColor.IRON_GRAY).solid().requiresTool().noCollision().strength(0.5f).pistonBehavior(PistonBehavior.DESTROY), BlockSetType.IRON);
    public static final Item FREEZING_PLATE_TRAP_ITEM = new BlockItem(FREEZING_PLATE_TRAP, new Item.Settings());
    public static final Block BURNING_PLATE_TRAP = new BurningPlateTrap(AbstractBlock.Settings.create().mapColor(MapColor.GOLD).solid().requiresTool().noCollision().strength(0.5f).pistonBehavior(PistonBehavior.DESTROY).luminance(value -> value.get(BurningPlateTrap.POWER) > 1 && value.get(BurningPlateTrap.CHARGE) > 0 ? 13 : 0), BlockSetType.IRON);
    public static final Item BURNING_PLATE_TRAP_ITEM = new BlockItem(BURNING_PLATE_TRAP, new Item.Settings());
    public static final Block GIANT_CRUSHER_TRAP = new GiantCrusherTrapBlock(AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).solidBlock(Blocks::never).strength(5.0f, 6.0f).dynamicBounds().pistonBehavior(PistonBehavior.BLOCK).solidBlock(Blocks::never).nonOpaque().solidBlock(Blocks::never).blockVision(Blocks::never));
    public static final Item GIANT_CRUSHER_TRAP_ITEM = new GiantCrusherTrapItem(GIANT_CRUSHER_TRAP, new Item.Settings());
    public static final Block AERIAL_CHICKEN_CANNON = new AerialChickenCannon(AbstractBlock.Settings.create().mapColor(MapColor.SPRUCE_BROWN).instrument(Instrument.BASS).strength(2.0f, 3.0f).burnable().sounds(BlockSoundGroup.METAL).solidBlock(Blocks::never));
    public static final Item AERIAL_CHICKEN_CANNON_ITEM = new BlockItem(AERIAL_CHICKEN_CANNON, new Item.Settings());
    public static final Block SPINNING_PEEPEE_CHOPPA = new SpinningPeepeeChoppa(AbstractBlock.Settings.create().mapColor(MapColor.BRIGHT_RED).requiresTool().strength(5.0f, 6.0f).sounds(BlockSoundGroup.METAL).solidBlock(Blocks::never));
    public static final Item SPINNING_PEEPEE_CHOPPA_ITEM = new BlockItem(SPINNING_PEEPEE_CHOPPA, new Item.Settings());

    public static void register() {
        Registry.register(Registries.BLOCK, new Identifier(TinyHouse.MOD_ID, "tiny_base"), TINY_BASE);
        Registry.register(Registries.ITEM, new Identifier(TinyHouse.MOD_ID, "tiny_base"), TINY_BASE_ITEM);
        Registry.register(Registries.ITEM, new Identifier(TinyHouse.MOD_ID, "block_magnet"), BLOCK_MAGNET);
        Registry.register(Registries.BLOCK, new Identifier(TinyHouse.MOD_ID, "chicken"), CHICKEN);
        Registry.register(Registries.ITEM, new Identifier(TinyHouse.MOD_ID, "chicken"), CHICKEN_ITEM);
        Registry.register(Registries.ITEM, new Identifier(TinyHouse.MOD_ID, "fire"), FIRE);
        Registry.register(Registries.BLOCK, new Identifier(TinyHouse.MOD_ID, "freezing_plate_trap"), FREEZING_PLATE_TRAP);
        Registry.register(Registries.ITEM, new Identifier(TinyHouse.MOD_ID, "freezing_plate_trap"), FREEZING_PLATE_TRAP_ITEM);
        Registry.register(Registries.BLOCK, new Identifier(TinyHouse.MOD_ID, "burning_plate_trap"), BURNING_PLATE_TRAP);
        Registry.register(Registries.ITEM, new Identifier(TinyHouse.MOD_ID, "burning_plate_trap"), BURNING_PLATE_TRAP_ITEM);
        Registry.register(Registries.BLOCK, new Identifier(TinyHouse.MOD_ID, "giant_crusher_trap"), GIANT_CRUSHER_TRAP);
        Registry.register(Registries.ITEM, new Identifier(TinyHouse.MOD_ID, "giant_crusher_trap"), GIANT_CRUSHER_TRAP_ITEM);
        Registry.register(Registries.BLOCK, new Identifier(TinyHouse.MOD_ID, "aerial_chicken_cannon"), AERIAL_CHICKEN_CANNON);
        Registry.register(Registries.ITEM, new Identifier(TinyHouse.MOD_ID, "aerial_chicken_cannon"), AERIAL_CHICKEN_CANNON_ITEM);
        Registry.register(Registries.BLOCK, new Identifier(TinyHouse.MOD_ID, "spinning_peepee_choppa"), SPINNING_PEEPEE_CHOPPA);
        Registry.register(Registries.ITEM, new Identifier(TinyHouse.MOD_ID, "spinning_peepee_choppa"), SPINNING_PEEPEE_CHOPPA_ITEM);
    }
}