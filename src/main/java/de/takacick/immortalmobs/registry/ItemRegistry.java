package de.takacick.immortalmobs.registry;

import de.takacick.immortalmobs.ImmortalMobs;
import de.takacick.immortalmobs.registry.block.HolyWaterBlock;
import de.takacick.immortalmobs.registry.block.ImmortalChainTrapBlock;
import de.takacick.immortalmobs.registry.block.ImmortalWoolBlock;
import de.takacick.immortalmobs.registry.block.fluid.HolyWaterFluid;
import de.takacick.immortalmobs.registry.item.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;

public class ItemRegistry {
    public static final FlowableFluid HOLY_WATER_STILL = new HolyWaterFluid.Still();
    public static final FlowableFluid HOLY_WATER_FLOWING = new HolyWaterFluid.Flowing();
    public static final FluidBlock HOLY_WATER_BLOCK = new HolyWaterBlock(HOLY_WATER_STILL, FabricBlockSettings.copy(Blocks.WATER));

    public static final Item IMMORTAL_ARROW = new ImmortalArrow(new Item.Settings().group(ImmortalMobs.ITEM_GROUP));
    public static final Item IMMORTAL_CANNON = new ImmortalCannon(new Item.Settings().group(ImmortalMobs.ITEM_GROUP).maxCount(1));
    public static final Block IMMORTAL_CHAIN_TRAP = new ImmortalChainTrapBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK));
    public static final Item IMMORTAL_CHAIN_TRAP_ITEM = new BlockItem(IMMORTAL_CHAIN_TRAP, new Item.Settings().group(ImmortalMobs.ITEM_GROUP));
    public static final Item IMMORTAL_FIREWORK = new ImmortalFirework(new Item.Settings().group(ImmortalMobs.ITEM_GROUP));
    public static final Item IMMORTAL_GUNPOWDER = new ImmortalGunPowder(new Item.Settings().group(ImmortalMobs.ITEM_GROUP));
    public static final Item IMMORTAL_INGOT = new ImmortalIngot(new Item.Settings().group(ImmortalMobs.ITEM_GROUP));
    public static final Item IMMORTAL_PICKAXE = new ImmortalPickaxe(ToolMaterials.NETHERITE, 1, -2.8f, new Item.Settings().group(ImmortalMobs.ITEM_GROUP));
    public static final Item IMMORTAL_PORKCHOP = new ImmortalPorkchop(new Item.Settings().group(ImmortalMobs.ITEM_GROUP));
    public static final Item IMMORTAL_SWORD = new ImmortalSword(ToolMaterials.NETHERITE, 994, -2.4f, new Item.Settings().group(ImmortalMobs.ITEM_GROUP));
    public static final Item IMMORTAL_ORB = new ImmortalOrb(new Item.Settings().group(ImmortalMobs.ITEM_GROUP));
    public static final Block IMMORTAL_WOOL = new ImmortalWoolBlock(AbstractBlock.Settings.of(Material.WOOL, MapColor.PURPLE).strength(0.8f).sounds(BlockSoundGroup.WOOL).nonOpaque().allowsSpawning(ItemRegistry::never).solidBlock(ItemRegistry::never).suffocates(ItemRegistry::never).blockVision(ItemRegistry::never));
    public static final Item IMMORTAL_WOOL_ITEM = new BlockItem(IMMORTAL_WOOL, new Item.Settings().group(ImmortalMobs.ITEM_GROUP));
    public static final Item IMMORTAL_SHIRT = new ImmortalShirt(new Item.Settings().group(ImmortalMobs.ITEM_GROUP).maxCount(1));
    public static final Item HOLY_WATER_BUCKET = new BucketItem(HOLY_WATER_STILL, new Item.Settings().group(ImmortalMobs.ITEM_GROUP).recipeRemainder(Items.BUCKET).maxCount(1));
    public static final Item IMMORTAL_DIAMOND = new ImmortalDiamond(new Item.Settings().group(ImmortalMobs.ITEM_GROUP));
    public static final Item IMMORTAL_ELYTRA = new ImmortalElytra(new Item.Settings().group(ImmortalMobs.ITEM_GROUP).maxCount(1));
    public static final Item IMMORTAL_END_CRYSTAL = new ImmortalEndCrystal(new Item.Settings().group(ImmortalMobs.ITEM_GROUP));
    public static final Item IMMORTAL_STRING = new ImmortalString(new Item.Settings().group(ImmortalMobs.ITEM_GROUP));
    public static final Item SUPER_SHEAR_SAW = new SuperShearSaw(new Item.Settings().group(ImmortalMobs.ITEM_GROUP).maxCount(1));

    public static void register() {
        Registry.register(Registry.BLOCK, new Identifier(ImmortalMobs.MOD_ID, "holy_water"), HOLY_WATER_BLOCK);
        Registry.register(Registry.FLUID, new Identifier(ImmortalMobs.MOD_ID, "holy_water_still"), HOLY_WATER_STILL);
        Registry.register(Registry.FLUID, new Identifier(ImmortalMobs.MOD_ID, "holy_water_flowing"), HOLY_WATER_FLOWING);

        Registry.register(Registry.ITEM, new Identifier(ImmortalMobs.MOD_ID, "immortal_arrow"), IMMORTAL_ARROW);
        Registry.register(Registry.ITEM, new Identifier(ImmortalMobs.MOD_ID, "immortal_cannon"), IMMORTAL_CANNON);
        Registry.register(Registry.BLOCK, new Identifier(ImmortalMobs.MOD_ID, "immortal_chain_trap"), IMMORTAL_CHAIN_TRAP);
        Registry.register(Registry.ITEM, new Identifier(ImmortalMobs.MOD_ID, "immortal_chain_trap"), IMMORTAL_CHAIN_TRAP_ITEM);
        Registry.register(Registry.ITEM, new Identifier(ImmortalMobs.MOD_ID, "immortal_firework"), IMMORTAL_FIREWORK);
        Registry.register(Registry.ITEM, new Identifier(ImmortalMobs.MOD_ID, "immortal_gunpowder"), IMMORTAL_GUNPOWDER);
        Registry.register(Registry.ITEM, new Identifier(ImmortalMobs.MOD_ID, "immortal_ingot"), IMMORTAL_INGOT);
        Registry.register(Registry.ITEM, new Identifier(ImmortalMobs.MOD_ID, "immortal_pickaxe"), IMMORTAL_PICKAXE);
        Registry.register(Registry.ITEM, new Identifier(ImmortalMobs.MOD_ID, "immortal_porkchop"), IMMORTAL_PORKCHOP);
        Registry.register(Registry.ITEM, new Identifier(ImmortalMobs.MOD_ID, "immortal_sword"), IMMORTAL_SWORD);
        Registry.register(Registry.ITEM, new Identifier(ImmortalMobs.MOD_ID, "immortal_orb"), IMMORTAL_ORB);
        Registry.register(Registry.BLOCK, new Identifier(ImmortalMobs.MOD_ID, "immortal_wool"), IMMORTAL_WOOL);
        Registry.register(Registry.ITEM, new Identifier(ImmortalMobs.MOD_ID, "immortal_wool"), IMMORTAL_WOOL_ITEM);
        Registry.register(Registry.ITEM, new Identifier(ImmortalMobs.MOD_ID, "immortal_shirt"), IMMORTAL_SHIRT);
        Registry.register(Registry.ITEM, new Identifier(ImmortalMobs.MOD_ID, "immortal_diamond"), IMMORTAL_DIAMOND);
        Registry.register(Registry.ITEM, new Identifier(ImmortalMobs.MOD_ID, "holy_water_bucket"), HOLY_WATER_BUCKET);
        Registry.register(Registry.ITEM, new Identifier(ImmortalMobs.MOD_ID, "immortal_elytra"), IMMORTAL_ELYTRA);
        Registry.register(Registry.ITEM, new Identifier(ImmortalMobs.MOD_ID, "immortal_end_crystal"), IMMORTAL_END_CRYSTAL);
        Registry.register(Registry.ITEM, new Identifier(ImmortalMobs.MOD_ID, "immortal_string"), IMMORTAL_STRING);
        Registry.register(Registry.ITEM, new Identifier(ImmortalMobs.MOD_ID, "super_shear_saw"), SUPER_SHEAR_SAW);
    }

    private static Boolean never(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
        return false;
    }

    private static boolean never(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }
}