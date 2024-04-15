package de.takacick.onegirlfriendblock.registry;

import de.takacick.onegirlfriendblock.OneGirlfriendBlock;
import de.takacick.onegirlfriendblock.registry.block.GirlfriendOneBlock;
import de.takacick.onegirlfriendblock.registry.item.*;
import de.takacick.onegirlfriendblock.server.GirlToolMaterials;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ItemRegistry {

    public static final Block GIRLFRIEND_ONE_BLOCK = new GirlfriendOneBlock(AbstractBlock.Settings.create().mapColor(MapColor.PINK).strength(0.5f).sounds(BlockSoundGroup.WOOL));
    public static final Item GIRLFRIEND_ONE_BLOCK_ITEM = new BlockItem(GIRLFRIEND_ONE_BLOCK, new Item.Settings());

    public static final Item GIRLFRIEND_HEAD = new Item(new Item.Settings().maxCount(1));
    public static final Item GIRLFRIEND_TORSO = new Item(new Item.Settings().maxCount(1));
    public static final Item GIRLFRIEND_RIGHT_ARM = new Item(new Item.Settings().maxCount(1));
    public static final Item GIRLFRIEND_LEFT_ARM = new Item(new Item.Settings().maxCount(1));
    public static final Item GIRLFRIEND_LEGS = new Item(new Item.Settings().maxCount(1));
    public static final Item GIRLFRIEND = new Girlfriend(new Item.Settings().maxCount(1));
    public static final Item MAID_SUIT = new MaidSuit(new Item.Settings().maxCount(1));
    public static final Item LIPSTICK_KATANA = new LipstickKatana(GirlToolMaterials.LIPSTICK, 5, -2.4f, new Item.Settings().maxCount(1));
    public static final Item CUTE_PLUSHY_MINER = new CutePlushyMiner(GirlToolMaterials.CUTE_PLUSHY, 1, -2.8f, new Item.Settings());
    public static final Item HEART = new HeartItem(new Item.Settings());
    public static final Item FRENCH_FRIES = new FrenchFries(new Item.Settings());

    public static void register() {
        Registry.register(Registries.BLOCK, new Identifier(OneGirlfriendBlock.MOD_ID, "girlfriend_one_block"), GIRLFRIEND_ONE_BLOCK);
        Registry.register(Registries.ITEM, new Identifier(OneGirlfriendBlock.MOD_ID, "girlfriend_one_block"), GIRLFRIEND_ONE_BLOCK_ITEM);
        Registry.register(Registries.ITEM, new Identifier(OneGirlfriendBlock.MOD_ID, "girlfriend_head"), GIRLFRIEND_HEAD);
        Registry.register(Registries.ITEM, new Identifier(OneGirlfriendBlock.MOD_ID, "girlfriend_torso"), GIRLFRIEND_TORSO);
        Registry.register(Registries.ITEM, new Identifier(OneGirlfriendBlock.MOD_ID, "girlfriend_right_arm"), GIRLFRIEND_RIGHT_ARM);
        Registry.register(Registries.ITEM, new Identifier(OneGirlfriendBlock.MOD_ID, "girlfriend_left_arm"), GIRLFRIEND_LEFT_ARM);
        Registry.register(Registries.ITEM, new Identifier(OneGirlfriendBlock.MOD_ID, "girlfriend_legs"), GIRLFRIEND_LEGS);
        Registry.register(Registries.ITEM, new Identifier(OneGirlfriendBlock.MOD_ID, "girlfriend"), GIRLFRIEND);
        Registry.register(Registries.ITEM, new Identifier(OneGirlfriendBlock.MOD_ID, "maid_suit"), MAID_SUIT);
        Registry.register(Registries.ITEM, new Identifier(OneGirlfriendBlock.MOD_ID, "lipstick_katana"), LIPSTICK_KATANA);
        Registry.register(Registries.ITEM, new Identifier(OneGirlfriendBlock.MOD_ID, "cute_plushy_miner"), CUTE_PLUSHY_MINER);
        Registry.register(Registries.ITEM, new Identifier(OneGirlfriendBlock.MOD_ID, "heart"), HEART);
        Registry.register(Registries.ITEM, new Identifier(OneGirlfriendBlock.MOD_ID, "french_fries"), FRENCH_FRIES);
    }
}