package de.takacick.heartmoney.registry;

import de.takacick.heartmoney.HeartMoney;
import de.takacick.heartmoney.registry.block.BloodBeaconTrapBlock;
import de.takacick.heartmoney.registry.block.HeartCatalystOre;
import de.takacick.heartmoney.registry.block.HeartwarmingNukeBlock;
import de.takacick.heartmoney.registry.item.*;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterials;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemRegistry {

    public static final Item HEART_SHOP_PORTAL = new HeartShopPortal(new Item.Settings().group(HeartMoney.ITEM_GROUP).maxCount(1));
    public static final Item HEART = new HeartItem(new Item.Settings().group(HeartMoney.ITEM_GROUP));
    public static final Block HEART_CATALYST_ORE = new HeartCatalystOre(AbstractBlock.Settings.of(Material.STONE).requiresTool().strength(3.0f, 3.0f));
    public static final Item HEART_CATALYST_ORE_ITEM = new BlockItem(HEART_CATALYST_ORE, new Item.Settings().group(HeartMoney.ITEM_GROUP));
    public static final Block DEEPSLATE_HEART_CATALYST_ORE = new HeartCatalystOre(AbstractBlock.Settings.copy(HEART_CATALYST_ORE).mapColor(MapColor.DEEPSLATE_GRAY).strength(4.5f, 3.0f).sounds(BlockSoundGroup.DEEPSLATE));
    public static final Item DEEPSLATE_HEART_CATALYST_ORE_ITEM = new BlockItem(DEEPSLATE_HEART_CATALYST_ORE, new Item.Settings().group(HeartMoney.ITEM_GROUP));
    public static final Item HEART_SWORD = new HeartSword(ToolMaterials.IRON, 4, -2.4f, new Item.Settings().group(HeartMoney.ITEM_GROUP));
    public static final Item HEART_PICKAXE = new HeartPickaxe(ToolMaterials.IRON, 1, -2.8f, new Item.Settings().group(HeartMoney.ITEM_GROUP));
    public static final Item HEARTPPLE = new Heartpple(new Item.Settings().group(HeartMoney.ITEM_GROUP));
    public static final Item MAID_SUIT_ARMOR = new MaidSuitArmor(new Item.Settings().group(HeartMoney.ITEM_GROUP).maxCount(1));
    public static final Item HEART_ANGEL = new HeartAngel(new Item.Settings().group(HeartMoney.ITEM_GROUP));

    public static final Item HEART_PULSE_BLASTER = new HeartPulseBlaster(new Item.Settings().group(HeartMoney.ITEM_GROUP).maxCount(1));
    public static final Item STRONG_HEART_SUCKER = new StrongHeartSucker(new Item.Settings().group(HeartMoney.ITEM_GROUP).maxCount(1));
    public static final Item GIRLFRIEND = new Girlfriend(new Item.Settings().group(HeartMoney.ITEM_GROUP).maxCount(1));
    public static final Item HEART_JET_PACK = new HeartJetPack(new Item.Settings().group(HeartMoney.ITEM_GROUP).maxCount(1));
    public static final Item LOVE_BARRIER_SUIT = new LoveBarrierSuit(new Item.Settings().group(HeartMoney.ITEM_GROUP).maxCount(1));
    public static final Item HOLY_HEART_RING = new HolyHeartRing(new Item.Settings().group(HeartMoney.ITEM_GROUP).maxCount(1));
    public static final Item BLOOD_KATANA = new BloodKatana(ToolMaterials.DIAMOND, 5, -2.4f, new Item.Settings().group(HeartMoney.ITEM_GROUP));
    public static final Block BLOOD_BEACON_TRAP = new BloodBeaconTrapBlock(AbstractBlock.Settings.of(Material.GLASS, MapColor.RED).strength(3.0F).luminance((state) -> 15).nonOpaque().solidBlock((state, world, pos) -> false));
    public static final Item BLOOD_BEACON_TRAP_ITEM = new BlockItem(BLOOD_BEACON_TRAP, new Item.Settings().group(HeartMoney.ITEM_GROUP));

    public static final Item LIFE_STEAL_SCYTHE = new LifeStealScythe(ToolMaterials.DIAMOND, 5, -2.4f, new Item.Settings().group(HeartMoney.ITEM_GROUP));
    public static final Item LOVER_TOTEM = new LoverTotem(new Item.Settings().group(HeartMoney.ITEM_GROUP).maxCount(1));
    public static final Block HEARTWARMING_NUKE = new HeartwarmingNukeBlock(AbstractBlock.Settings.of(Material.METAL, MapColor.RED).strength(3.0F).luminance((state) -> 15).nonOpaque().solidBlock((state, world, pos) -> false));
    public static final Item HEARTWARMING_NUKE_ITEM = new BlockItem(HEARTWARMING_NUKE, new Item.Settings().group(HeartMoney.ITEM_GROUP));

    public static final Item HEART_ANGEL_HEART = new Item(new Item.Settings());

    public static void register() {
        Registry.register(Registry.ITEM, new Identifier(HeartMoney.MOD_ID, "heart_shop_portal"), HEART_SHOP_PORTAL);
        Registry.register(Registry.ITEM, new Identifier(HeartMoney.MOD_ID, "heart"), HEART);
        Registry.register(Registry.BLOCK, new Identifier(HeartMoney.MOD_ID, "heart_catalyst_ore"), HEART_CATALYST_ORE);
        Registry.register(Registry.ITEM, new Identifier(HeartMoney.MOD_ID, "heart_catalyst_ore"), HEART_CATALYST_ORE_ITEM);
        Registry.register(Registry.BLOCK, new Identifier(HeartMoney.MOD_ID, "deepslate_heart_catalyst_ore"), DEEPSLATE_HEART_CATALYST_ORE);
        Registry.register(Registry.ITEM, new Identifier(HeartMoney.MOD_ID, "deepslate_heart_catalyst_ore"), DEEPSLATE_HEART_CATALYST_ORE_ITEM);
        Registry.register(Registry.ITEM, new Identifier(HeartMoney.MOD_ID, "heart_sword"), HEART_SWORD);
        Registry.register(Registry.ITEM, new Identifier(HeartMoney.MOD_ID, "heart_pickaxe"), HEART_PICKAXE);
        Registry.register(Registry.ITEM, new Identifier(HeartMoney.MOD_ID, "heartpple"), HEARTPPLE);
        Registry.register(Registry.ITEM, new Identifier(HeartMoney.MOD_ID, "maid_suit_armor"), MAID_SUIT_ARMOR);
        Registry.register(Registry.ITEM, new Identifier(HeartMoney.MOD_ID, "heart_angel"), HEART_ANGEL);
        Registry.register(Registry.ITEM, new Identifier(HeartMoney.MOD_ID, "heart_pulse_blaster"), HEART_PULSE_BLASTER);
        Registry.register(Registry.ITEM, new Identifier(HeartMoney.MOD_ID, "strong_heart_sucker"), STRONG_HEART_SUCKER);
        Registry.register(Registry.ITEM, new Identifier(HeartMoney.MOD_ID, "girlfriend"), GIRLFRIEND);
        Registry.register(Registry.ITEM, new Identifier(HeartMoney.MOD_ID, "heart_jet_pack"), HEART_JET_PACK);
        Registry.register(Registry.ITEM, new Identifier(HeartMoney.MOD_ID, "love_barrier_suit"), LOVE_BARRIER_SUIT);
        Registry.register(Registry.ITEM, new Identifier(HeartMoney.MOD_ID, "holy_heart_ring"), HOLY_HEART_RING);
        Registry.register(Registry.ITEM, new Identifier(HeartMoney.MOD_ID, "blood_katana"), BLOOD_KATANA);
        Registry.register(Registry.BLOCK, new Identifier(HeartMoney.MOD_ID, "blood_beacon_trap"), BLOOD_BEACON_TRAP);
        Registry.register(Registry.ITEM, new Identifier(HeartMoney.MOD_ID, "blood_beacon_trap"), BLOOD_BEACON_TRAP_ITEM);
        Registry.register(Registry.ITEM, new Identifier(HeartMoney.MOD_ID, "life_steal_scythe"), LIFE_STEAL_SCYTHE);
        Registry.register(Registry.ITEM, new Identifier(HeartMoney.MOD_ID, "lover_totem"), LOVER_TOTEM);
        Registry.register(Registry.BLOCK, new Identifier(HeartMoney.MOD_ID, "heartwarming_nuke"), HEARTWARMING_NUKE);
        Registry.register(Registry.ITEM, new Identifier(HeartMoney.MOD_ID, "heartwarming_nuke"), HEARTWARMING_NUKE_ITEM);
        Registry.register(Registry.ITEM, new Identifier(HeartMoney.MOD_ID, "heart_angel_heart"), HEART_ANGEL_HEART);
    }
}