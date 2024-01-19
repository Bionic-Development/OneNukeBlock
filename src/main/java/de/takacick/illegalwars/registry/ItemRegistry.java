package de.takacick.illegalwars.registry;

import de.takacick.illegalwars.IllegalWars;
import de.takacick.illegalwars.registry.block.PieLauncherBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.Instrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ItemRegistry {

    public static final Block PIE_LAUNCHER = new PieLauncherBlock(AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).instrument(Instrument.BASEDRUM).requiresTool().strength(3.5f));
    public static final Item PIE_LAUNCHER_ITEM = new BlockItem(PIE_LAUNCHER, new Item.Settings());
    public static final Item PIE = new Item(new Item.Settings().food(new FoodComponent.Builder().hunger(6).saturationModifier(1.2f).alwaysEdible().build()));

    public static void register() {
        Registry.register(Registries.BLOCK, new Identifier(IllegalWars.MOD_ID, "pie_launcher"), PIE_LAUNCHER);
        Registry.register(Registries.ITEM, new Identifier(IllegalWars.MOD_ID, "pie_launcher"), PIE_LAUNCHER_ITEM);
        Registry.register(Registries.ITEM, new Identifier(IllegalWars.MOD_ID, "pie"), PIE);
    }
}