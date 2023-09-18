package de.takacick.onesuperblock.registry;

import de.takacick.onesuperblock.OneSuperBlock;
import de.takacick.onesuperblock.registry.block.SuperBlock;
import de.takacick.onesuperblock.registry.block.SuperOreBlock;
import de.takacick.onesuperblock.registry.block.SuperWoolBlock;
import de.takacick.onesuperblock.registry.item.SuperBridgeEgg;
import de.takacick.onesuperblock.registry.item.SuperEnchanterBook;
import de.takacick.onesuperblock.registry.item.SuperEnderPearl;
import de.takacick.onesuperblock.registry.item.SuperProto;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemRegistry {

    public static final Block SUPER_BLOCK = new SuperBlock(AbstractBlock.Settings.of(Material.GLASS).strength(0.6f).sounds(BlockSoundGroup.GLASS).nonOpaque().solidBlock((state, world, pos) -> false).suffocates((state, world, pos) -> false).blockVision((state, world, pos) -> false));
    public static final Item SUPER_BLOCK_ITEM = new BlockItem(SUPER_BLOCK, new Item.Settings().group(OneSuperBlock.ITEM_GROUP));
    public static final Block SUPER_ORE = new SuperOreBlock(AbstractBlock.Settings.of(Material.STONE).requiresTool().strength(3.0f, 3.0f));
    public static final Item SUPER_ORE_ITEM = new BlockItem(SUPER_ORE, new Item.Settings().group(OneSuperBlock.ITEM_GROUP));
    public static final Block DEEPSLATE_SUPER_ORE = new SuperOreBlock(AbstractBlock.Settings.copy(SUPER_ORE).mapColor(MapColor.DEEPSLATE_GRAY).strength(4.5f, 3.0f).sounds(BlockSoundGroup.DEEPSLATE));
    public static final Item DEEPSLATE_SUPER_ORE_ITEM = new BlockItem(DEEPSLATE_SUPER_ORE, new Item.Settings().group(OneSuperBlock.ITEM_GROUP));
    public static final Item SUPER_ENDER_PEARL = new SuperEnderPearl(new Item.Settings().group(OneSuperBlock.ITEM_GROUP).maxDamage(4));
    public static final Item SUPER_ENCHANTER_BOOK = new SuperEnchanterBook(new Item.Settings().group(OneSuperBlock.ITEM_GROUP).maxCount(1));
    public static final Item SUPER_PROTO = new SuperProto(new Item.Settings().group(OneSuperBlock.ITEM_GROUP).maxCount(1));
    public static final Item SUPER_BRIDGE_EGG = new SuperBridgeEgg(new Item.Settings().group(OneSuperBlock.ITEM_GROUP));
    public static final Block SUPER_WOOL = new SuperWoolBlock(AbstractBlock.Settings.of(Material.WOOL, MapColor.BLUE).strength(0.8f).sounds(BlockSoundGroup.WOOL));
    public static final Item SUPER_WOOL_ITEM = new BlockItem(SUPER_WOOL, new Item.Settings().group(OneSuperBlock.ITEM_GROUP));

    public static void register() {
        Registry.register(Registry.BLOCK, new Identifier(OneSuperBlock.MOD_ID, "super_block"), SUPER_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(OneSuperBlock.MOD_ID, "super_block"), SUPER_BLOCK_ITEM);
        Registry.register(Registry.BLOCK, new Identifier(OneSuperBlock.MOD_ID, "super_ore"), SUPER_ORE);
        Registry.register(Registry.ITEM, new Identifier(OneSuperBlock.MOD_ID, "super_ore"), SUPER_ORE_ITEM);
        Registry.register(Registry.BLOCK, new Identifier(OneSuperBlock.MOD_ID, "deepslate_super_ore"), DEEPSLATE_SUPER_ORE);
        Registry.register(Registry.ITEM, new Identifier(OneSuperBlock.MOD_ID, "deepslate_super_ore"), DEEPSLATE_SUPER_ORE_ITEM);
        Registry.register(Registry.ITEM, new Identifier(OneSuperBlock.MOD_ID, "super_ender_pearl"), SUPER_ENDER_PEARL);
        Registry.register(Registry.ITEM, new Identifier(OneSuperBlock.MOD_ID, "super_enchanter_book"), SUPER_ENCHANTER_BOOK);
        Registry.register(Registry.ITEM, new Identifier(OneSuperBlock.MOD_ID, "super_proto"), SUPER_PROTO);
        Registry.register(Registry.ITEM, new Identifier(OneSuperBlock.MOD_ID, "super_bridge_egg"), SUPER_BRIDGE_EGG);
        Registry.register(Registry.BLOCK, new Identifier(OneSuperBlock.MOD_ID, "super_wool"), SUPER_WOOL);
        Registry.register(Registry.ITEM, new Identifier(OneSuperBlock.MOD_ID, "super_wool"), SUPER_WOOL_ITEM);
    }
}