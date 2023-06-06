package de.takacick.deathmoney.registry;

import de.takacick.deathmoney.DeathMoney;
import de.takacick.deathmoney.registry.block.BlackMatterBlock;
import de.takacick.deathmoney.registry.block.BloodFluidBlock;
import de.takacick.deathmoney.registry.block.fluid.BloodFluid;
import de.takacick.deathmoney.registry.item.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemRegistry {

    public static final FlowableFluid BLOOD_STILL = new BloodFluid.Still();
    public static final FlowableFluid BLOOD_FLOW = new BloodFluid.Flowing();
    public static final FluidBlock BLOOD_BLOCK = new BloodFluidBlock(BLOOD_STILL, FabricBlockSettings.copy(Blocks.WATER));

    public static final Item DEATH_SHOP_PORTAL = new DeathShopPortal(new Item.Settings().group(DeathMoney.ITEM_GROUP).maxCount(1));
    public static final Item DANGEROUS_MINER_MAGNET = new DangerousMinerMagnet(new Item.Settings().group(DeathMoney.ITEM_GROUP).maxDamage(250));
    public static final Item LITTLE_WITHER_BULLY = new LittleWitherBully(new Item.Settings().group(DeathMoney.ITEM_GROUP));
    public static final Item SWEET_BERRY_SUIT = new SweetBerrySuit(new Item.Settings().group(DeathMoney.ITEM_GROUP).maxDamage(30));
    public static final Item INFINITE_DEATH_TOTEM = new InfiniteDeathTotem(new Item.Settings().group(DeathMoney.ITEM_GROUP).maxCount(1));
    public static final Item EXPLOSIVE_CAKE_POP = new ExplosiveCakePop(new Item.Settings().group(DeathMoney.ITEM_GROUP));
    public static final Item HEART_CARVER = new HeartCarver(new Item.Settings().group(DeathMoney.ITEM_GROUP).maxDamage(1));
    public static final Item CRAZY_EX_GIRLFRIENDS = new CrazyExGirlfriends(new Item.Settings().group(DeathMoney.ITEM_GROUP).maxDamage(10));
    public static final Item METEOR_SHOWER = new MeteorShower(new Item.Settings().group(DeathMoney.ITEM_GROUP).maxDamage(10));
    public static final Item DEATH_DROP_MINER = new DeathDropMiner(ToolMaterials.DIAMOND, 1, -2.8f, new Item.Settings().group(DeathMoney.ITEM_GROUP));
    public static final Item GAMER_ALLERGY_INJECTION = new GamerAllergyInjection(new Item.Settings().group(DeathMoney.ITEM_GROUP).maxCount(1));
    public static final Item FIRE_SUIT = new FireSuit(new Item.Settings().group(DeathMoney.ITEM_GROUP).maxDamage(50));
    public static final Item CACTUS_ONESIE = new CactusOnesie(new Item.Settings().group(DeathMoney.ITEM_GROUP).maxDamage(40));
    public static final Item HUNGRY_TITAN = new HungryTitan(new Item.Settings().group(DeathMoney.ITEM_GROUP).maxCount(1));
    public static final Item DEATH_NOTE = new DeathNote(new Item.Settings().group(DeathMoney.ITEM_GROUP).maxDamage(5));
    public static final Item BLACK_HOLE = new BlackHole(new Item.Settings().group(DeathMoney.ITEM_GROUP).maxCount(1));
    public static final Item HEART = new Item(new Item.Settings());
    public static final Block BLACK_MATTER = new BlackMatterBlock(AbstractBlock.Settings.of(Material.PORTAL, MapColor.BLACK).luminance(state -> 15).strength(-1.0f, 3600000.0f).dropsNothing());

    public static void register() {
        Registry.register(Registry.BLOCK, new Identifier(DeathMoney.MOD_ID, "blood"), BLOOD_BLOCK);
        Registry.register(Registry.FLUID, new Identifier(DeathMoney.MOD_ID, "blood"), BLOOD_STILL);
        Registry.register(Registry.FLUID, new Identifier(DeathMoney.MOD_ID, "blood_flow"), BLOOD_FLOW);
        Registry.register(Registry.ITEM, new Identifier(DeathMoney.MOD_ID, "death_shop_portal"), DEATH_SHOP_PORTAL);
        Registry.register(Registry.ITEM, new Identifier(DeathMoney.MOD_ID, "dangerous_miner_magnet"), DANGEROUS_MINER_MAGNET);
        Registry.register(Registry.ITEM, new Identifier(DeathMoney.MOD_ID, "little_wither_bully"), LITTLE_WITHER_BULLY);
        Registry.register(Registry.ITEM, new Identifier(DeathMoney.MOD_ID, "sweet_berry_suit"), SWEET_BERRY_SUIT);
        Registry.register(Registry.ITEM, new Identifier(DeathMoney.MOD_ID, "infinite_death_totem"), INFINITE_DEATH_TOTEM);
        Registry.register(Registry.ITEM, new Identifier(DeathMoney.MOD_ID, "explosive_cake_pop"), EXPLOSIVE_CAKE_POP);
        Registry.register(Registry.ITEM, new Identifier(DeathMoney.MOD_ID, "heart_carver"), HEART_CARVER);
        Registry.register(Registry.ITEM, new Identifier(DeathMoney.MOD_ID, "crazy_ex_girlfriends"), CRAZY_EX_GIRLFRIENDS);
        Registry.register(Registry.ITEM, new Identifier(DeathMoney.MOD_ID, "meteor_shower"), METEOR_SHOWER);
        Registry.register(Registry.ITEM, new Identifier(DeathMoney.MOD_ID, "death_drop_miner"), DEATH_DROP_MINER);
        Registry.register(Registry.ITEM, new Identifier(DeathMoney.MOD_ID, "gamer_allergy_injection"), GAMER_ALLERGY_INJECTION);
        Registry.register(Registry.ITEM, new Identifier(DeathMoney.MOD_ID, "fire_suit"), FIRE_SUIT);
        Registry.register(Registry.ITEM, new Identifier(DeathMoney.MOD_ID, "cactus_onesie"), CACTUS_ONESIE);
        Registry.register(Registry.ITEM, new Identifier(DeathMoney.MOD_ID, "hungry_titan"), HUNGRY_TITAN);
        Registry.register(Registry.ITEM, new Identifier(DeathMoney.MOD_ID, "death_note"), DEATH_NOTE);
        Registry.register(Registry.ITEM, new Identifier(DeathMoney.MOD_ID, "black_hole"), BLACK_HOLE);
        Registry.register(Registry.ITEM, new Identifier(DeathMoney.MOD_ID, "heart"), HEART);
        Registry.register(Registry.BLOCK, new Identifier(DeathMoney.MOD_ID, "black_matter"), BLACK_MATTER);
    }
}