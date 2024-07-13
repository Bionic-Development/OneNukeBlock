package de.takacick.onegirlboyblock.registry;

import de.takacick.onegirlboyblock.OneGirlBoyBlock;
import de.takacick.onegirlboyblock.registry.block.OneBoyBlock;
import de.takacick.onegirlboyblock.registry.block.OneGirlBlock;
import de.takacick.onegirlboyblock.registry.item.*;
import de.takacick.onegirlboyblock.utils.GirlBoyArmorMaterials;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ItemRegistry {

    public static final Block ONE_GIRL_BLOCK = new OneGirlBlock(AbstractBlock.Settings.create().mapColor(MapColor.PINK).ticksRandomly().strength(0.6f).sounds(BlockSoundGroup.GRASS));
    public static final Item ONE_GIRL_BLOCK_ITEM = new BlockItem(ONE_GIRL_BLOCK, new Item.Settings());
    public static final Block ONE_BOY_BLOCK = new OneBoyBlock(AbstractBlock.Settings.create().mapColor(MapColor.LIGHT_BLUE).ticksRandomly().strength(0.6f).sounds(BlockSoundGroup.GRASS));
    public static final Item ONE_BOY_BLOCK_ITEM = new BlockItem(ONE_BOY_BLOCK, new Item.Settings());
    public static final Item BASEBALL_BAT = new BaseballBat(new Item.Settings().attributeModifiers(SwordItem.createAttributeModifiers(BaseballBat.BASEBALL_BAT_MATERIAL, 3, -2.4f)));
    public static final Item FOOTBALL_GEAR = new FootballGear(GirlBoyArmorMaterials.FOOTBALL_GEAR, ArmorItem.Type.CHESTPLATE, new Item.Settings().maxCount(1));
    public static final Item TURBO_BOARD = new TurboBoard(new Item.Settings().maxDamage(250));
    public static final Item BIT_CANNON = new BitCannon(new Item.Settings().maxCount(1));
    public static final Item GOLDEN_MEAT = new GoldenMeat(new Item.Settings());
    public static final Item WEIGHTED_ANVIL_HAMMER = new WeightedAnvilHammer(new Item.Settings().attributeModifiers(PickaxeItem.createAttributeModifiers(WeightedAnvilHammer.WEIGHTED_ANVIL_HAMMER, 1.0f, -2.8f)));
    public static final Item GLITTER_BLADE = new GlitterBlade(new Item.Settings().attributeModifiers(SwordItem.createAttributeModifiers(GlitterBlade.GLITTER_BLADE_MATERIAL, 3, -2.4f)));
    public static final Item TIARA = new Tiara(new Item.Settings().maxCount(1));
    public static final Item BUTTERFLY_WINGS = new ButterflyWings(new Item.Settings().maxDamage(250));
    public static final Item INFERNO_HAIR_DRYER = new InfernoHairDyer(new Item.Settings().maxDamage(250));
    public static final Item STRAWBERRY_SHORTCAKE = new StrawberryShortcake(new Item.Settings());
    public static final Item STAR_MINER = new StarMiner(new Item.Settings().attributeModifiers(PickaxeItem.createAttributeModifiers(StarMiner.STAR_MINER, 1.0f, -2.8f)));

    public static void register() {
        Registry.register(Registries.BLOCK, Identifier.of(OneGirlBoyBlock.MOD_ID, "one_girl_block"), ONE_GIRL_BLOCK);
        Registry.register(Registries.ITEM, Identifier.of(OneGirlBoyBlock.MOD_ID, "one_girl_block"), ONE_GIRL_BLOCK_ITEM);
        Registry.register(Registries.BLOCK, Identifier.of(OneGirlBoyBlock.MOD_ID, "one_boy_block"), ONE_BOY_BLOCK);
        Registry.register(Registries.ITEM, Identifier.of(OneGirlBoyBlock.MOD_ID, "one_boy_block"), ONE_BOY_BLOCK_ITEM);
        Registry.register(Registries.ITEM, Identifier.of(OneGirlBoyBlock.MOD_ID, "baseball_bat"), BASEBALL_BAT);
        Registry.register(Registries.ITEM, Identifier.of(OneGirlBoyBlock.MOD_ID, "football_gear"), FOOTBALL_GEAR);
        Registry.register(Registries.ITEM, Identifier.of(OneGirlBoyBlock.MOD_ID, "turbo_board"), TURBO_BOARD);
        Registry.register(Registries.ITEM, Identifier.of(OneGirlBoyBlock.MOD_ID, "8_bit_cannon"), BIT_CANNON);
        Registry.register(Registries.ITEM, Identifier.of(OneGirlBoyBlock.MOD_ID, "golden_meat"), GOLDEN_MEAT);
        Registry.register(Registries.ITEM, Identifier.of(OneGirlBoyBlock.MOD_ID, "weighted_anvil_hammer"), WEIGHTED_ANVIL_HAMMER);
        Registry.register(Registries.ITEM, Identifier.of(OneGirlBoyBlock.MOD_ID, "glitter_blade"), GLITTER_BLADE);
        Registry.register(Registries.ITEM, Identifier.of(OneGirlBoyBlock.MOD_ID, "tiara"), TIARA);
        Registry.register(Registries.ITEM, Identifier.of(OneGirlBoyBlock.MOD_ID, "butterfly_wings"), BUTTERFLY_WINGS);
        Registry.register(Registries.ITEM, Identifier.of(OneGirlBoyBlock.MOD_ID, "inferno_hair_dryer"), INFERNO_HAIR_DRYER);
        Registry.register(Registries.ITEM, Identifier.of(OneGirlBoyBlock.MOD_ID, "strawberry_shortcake"), STRAWBERRY_SHORTCAKE);
        Registry.register(Registries.ITEM, Identifier.of(OneGirlBoyBlock.MOD_ID, "star_miner"), STAR_MINER);
    }
}