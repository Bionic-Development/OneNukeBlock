package de.takacick.onenukeblock.registry;

import de.takacick.onenukeblock.OneNukeBlock;
import de.takacick.onenukeblock.registry.block.BladedTntBlock;
import de.takacick.onenukeblock.registry.block.NuclearWaterBlock;
import de.takacick.onenukeblock.registry.block.NukeOneBlock;
import de.takacick.onenukeblock.registry.block.SkylandTntBlock;
import de.takacick.onenukeblock.registry.block.fluid.NuclearWaterFluid;
import de.takacick.onenukeblock.registry.item.*;
import de.takacick.onenukeblock.utils.NukeArmorMaterials;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ItemRegistry {

    public static final FlowableFluid STILL_NUCLEAR_WATER = new NuclearWaterFluid.Still();
    public static final FlowableFluid FLOWING_NUCLEAR_WATER = new NuclearWaterFluid.Flowing();
    public static final FluidBlock NUCLEAR_WATER = new NuclearWaterBlock(STILL_NUCLEAR_WATER, AbstractBlock.Settings.create().mapColor(MapColor.LICHEN_GREEN).replaceable().noCollision().strength(100.0f).pistonBehavior(PistonBehavior.DESTROY).dropsNothing().liquid().sounds(BlockSoundGroup.INTENTIONALLY_EMPTY));

    public static final Block ONE_NUKE_BLOCK = new NukeOneBlock(AbstractBlock.Settings.create().mapColor(MapColor.YELLOW).ticksRandomly().strength(0.6f).sounds(BlockSoundGroup.GRASS));
    public static final Item ONE_NUKE_BLOCK_ITEM = new BlockItem(ONE_NUKE_BLOCK, new Item.Settings());
    public static final Item KABOOM_MINER = new KaboomMiner(KaboomMiner.MATERIAL, new Item.Settings().attributeModifiers(PickaxeItem.createAttributeModifiers(KaboomMiner.MATERIAL, 1.0f, -2.8f)));
    public static final Block SKYLAND_TNT = new SkylandTntBlock(AbstractBlock.Settings.create().mapColor(MapColor.PALE_GREEN).breakInstantly().sounds(BlockSoundGroup.GRASS).burnable().solidBlock(Blocks::never));
    public static final Item SKYLAND_TNT_ITEM = new SkylandTnt(SKYLAND_TNT, new Item.Settings());
    public static final Block BLADED_TNT = new BladedTntBlock(AbstractBlock.Settings.create().mapColor(MapColor.PALE_GREEN).breakInstantly().sounds(BlockSoundGroup.GRASS).burnable().solidBlock(Blocks::never));
    public static final Item BLADED_TNT_ITEM = new BladedTnt(BLADED_TNT, new Item.Settings().maxDamage(32));
    public static final Item EXPLOSIVE_GUMMY_BEAR = new ExplosiveGummyBear(new Item.Settings());
    public static final Item BANG_MACE = new BangMace(new Item.Settings().component(DataComponentTypes.TOOL, BangMace.createToolComponent()).attributeModifiers(BangMace.createAttributeModifiers()).maxCount(1));
    public static final Item DIAMOND_HAZMAT_HELMET = new DiamondHazmatArmorItem(NukeArmorMaterials.DIAMOND_HAZMAT_ARMOR, ArmorItem.Type.HELMET, new Item.Settings().maxCount(1));
    public static final Item DIAMOND_HAZMAT_CHESTPLATE = new DiamondHazmatArmorItem(NukeArmorMaterials.DIAMOND_HAZMAT_ARMOR, ArmorItem.Type.CHESTPLATE, new Item.Settings().maxCount(1));
    public static final Item DIAMOND_HAZMAT_LEGGINGS = new DiamondHazmatArmorItem(NukeArmorMaterials.DIAMOND_HAZMAT_ARMOR, ArmorItem.Type.LEGGINGS, new Item.Settings().maxCount(1));
    public static final Item DIAMOND_HAZMAT_BOOTS = new DiamondHazmatArmorItem(NukeArmorMaterials.DIAMOND_HAZMAT_ARMOR, ArmorItem.Type.BOOTS, new Item.Settings().maxCount(1));
    public static final Item NUCLEAR_WATER_BUCKET = new BucketItem(STILL_NUCLEAR_WATER, new Item.Settings().recipeRemainder(Items.BUCKET).maxCount(1));

    public static void register() {
        Registry.register(Registries.BLOCK, Identifier.of(OneNukeBlock.MOD_ID, "nuclear_water"), NUCLEAR_WATER);
        Registry.register(Registries.FLUID, Identifier.of(OneNukeBlock.MOD_ID, "still_nuclear_water"), STILL_NUCLEAR_WATER);
        Registry.register(Registries.FLUID, Identifier.of(OneNukeBlock.MOD_ID, "flowing_nuclear_water"), FLOWING_NUCLEAR_WATER);

        Registry.register(Registries.BLOCK, Identifier.of(OneNukeBlock.MOD_ID, "nuke_one_block"), ONE_NUKE_BLOCK);
        Registry.register(Registries.ITEM, Identifier.of(OneNukeBlock.MOD_ID, "nuke_one_block"), ONE_NUKE_BLOCK_ITEM);
        Registry.register(Registries.ITEM, Identifier.of(OneNukeBlock.MOD_ID, "kaboom_miner"), KABOOM_MINER);
        Registry.register(Registries.BLOCK, Identifier.of(OneNukeBlock.MOD_ID, "skyland_tnt"), SKYLAND_TNT);
        Registry.register(Registries.ITEM, Identifier.of(OneNukeBlock.MOD_ID, "skyland_tnt"), SKYLAND_TNT_ITEM);
        Registry.register(Registries.BLOCK, Identifier.of(OneNukeBlock.MOD_ID, "bladed_tnt"), BLADED_TNT);
        Registry.register(Registries.ITEM, Identifier.of(OneNukeBlock.MOD_ID, "bladed_tnt"), BLADED_TNT_ITEM);
        Registry.register(Registries.ITEM, Identifier.of(OneNukeBlock.MOD_ID, "explosive_gummy_bear"), EXPLOSIVE_GUMMY_BEAR);
        Registry.register(Registries.ITEM, Identifier.of(OneNukeBlock.MOD_ID, "bang_mace"), BANG_MACE);
        Registry.register(Registries.ITEM, Identifier.of(OneNukeBlock.MOD_ID, "diamond_hazmat_helmet"), DIAMOND_HAZMAT_HELMET);
        Registry.register(Registries.ITEM, Identifier.of(OneNukeBlock.MOD_ID, "diamond_hazmat_chestplate"), DIAMOND_HAZMAT_CHESTPLATE);
        Registry.register(Registries.ITEM, Identifier.of(OneNukeBlock.MOD_ID, "diamond_hazmat_leggings"), DIAMOND_HAZMAT_LEGGINGS);
        Registry.register(Registries.ITEM, Identifier.of(OneNukeBlock.MOD_ID, "diamond_hazmat_boots"), DIAMOND_HAZMAT_BOOTS);
        Registry.register(Registries.ITEM, Identifier.of(OneNukeBlock.MOD_ID, "nuclear_water_bucket"), NUCLEAR_WATER_BUCKET);
    }
}