package de.takacick.secretcraftbase.registry;

import de.takacick.secretcraftbase.SecretCraftBase;
import de.takacick.secretcraftbase.registry.block.*;
import de.takacick.secretcraftbase.registry.block.fluid.MagicWellWaterFluid;
import de.takacick.secretcraftbase.registry.item.*;
import net.minecraft.block.*;
import net.minecraft.block.enums.Instrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class ItemRegistry {

    public static final FlowableFluid STILL_MAGIC_WELL_WATER = new MagicWellWaterFluid.Still();
    public static final FlowableFluid FLOWING_MAGIC_WELL_WATER = new MagicWellWaterFluid.Flowing();
    public static final FluidBlock SECRET_MAGIC_WELL_WATER = new SecretMagicWellWaterBlock(STILL_MAGIC_WELL_WATER, AbstractBlock.Settings.create().mapColor(MapColor.PALE_GREEN).noCollision().strength(100.0f).pistonBehavior(PistonBehavior.DESTROY).dropsNothing().liquid().sounds(BlockSoundGroup.INTENTIONALLY_EMPTY).dynamicBounds());

    public static final Item IRON_GOLEM_FARM = new SchematicItem(EntityRegistry.IRON_GOLEM_FARM, new Item.Settings().maxCount(1));
    public static final Item TREASURY_ROOM = new SchematicItem(EntityRegistry.TREASURY_ROOM, new Item.Settings().maxCount(1));
    public static final Item ARMORY_ROOM = new SchematicItem(EntityRegistry.ARMORY_ROOM, new Item.Settings().maxCount(1));
    public static final Item XP_FARM = new SchematicItem(EntityRegistry.XP_FARM, new Item.Settings().maxCount(1));
    public static final Item SECRET_PIG_POWERED_PORTAL = new SecretPigPoweredPortal(new Item.Settings().maxCount(1));
    public static final Item PIG = new Pig(new Item.Settings().maxCount(1));
    public static final Item NETHER_PORTAL_BLOCK = new NetherPortal(Blocks.NETHER_PORTAL, new Item.Settings());
    public static final Item REDSTONE_ORE_CHUNKS = new RedstoneOreChunks(new Item.Settings());
    public static final Block STONE_SECRET_REDSTONE_MIRROR_MELTER = new SecretRedstoneMirrorMelterOre(AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).instrument(Instrument.BASEDRUM).nonOpaque().solidBlock(Blocks::never).requiresTool().ticksRandomly().luminance(Blocks.createLightLevelFromLitBlockState(9)).strength(3.0f, 3.0f));
    public static final Item STONE_SECRET_REDSTONE_MIRROR_MELTER_ITEM = new BlockItem(STONE_SECRET_REDSTONE_MIRROR_MELTER, new Item.Settings());
    public static final Block DEEPSLATE_SECRET_REDSTONE_MIRROR_MELTER = new SecretRedstoneMirrorMelterOre(AbstractBlock.Settings.copyShallow(STONE_SECRET_REDSTONE_MIRROR_MELTER).mapColor(MapColor.DEEPSLATE_GRAY).strength(4.5f, 3.0f).sounds(BlockSoundGroup.DEEPSLATE));
    public static final Item DEEPSLATE_SECRET_REDSTONE_MIRROR_MELTER_ITEM = new BlockItem(DEEPSLATE_SECRET_REDSTONE_MIRROR_MELTER, new Item.Settings());
    public static final Block SECRET_MAGIC_WELL_COBBLESTONE = new SecretMagicWellBlock(AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).instrument(Instrument.BASEDRUM).pistonBehavior(PistonBehavior.BLOCK).requiresTool().strength(2.0f, 6.0f).dynamicBounds());
    public static final Block SECRET_MAGIC_WELL_MOSSY_COBBLESTONE = new SecretMagicWellBlock(AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).instrument(Instrument.BASEDRUM).pistonBehavior(PistonBehavior.BLOCK).requiresTool().strength(2.0f, 6.0f).dynamicBounds());
    public static final Block SECRET_MAGIC_WELL_OAK_FENCE = new SecretMagicWellFenceBlock(AbstractBlock.Settings.create().mapColor(Blocks.OAK_PLANKS.getDefaultMapColor()).solid().pistonBehavior(PistonBehavior.BLOCK).instrument(Instrument.BASS).strength(2.0f, 3.0f).sounds(BlockSoundGroup.WOOD).dynamicBounds());
    public static final Block SECRET_MAGIC_WELL_TORCH = new SecretMagicWellTorchBlock(AbstractBlock.Settings.create().breakInstantly().luminance(state -> 14).sounds(BlockSoundGroup.WOOD).pistonBehavior(PistonBehavior.BLOCK).dynamicBounds().noCollision());
    public static final Item SECRET_MAGIC_WELL = new SecretMagicWell(SECRET_MAGIC_WELL_COBBLESTONE, new Item.Settings());
    public static final Item SECRET_GIANT_JUMPY_SLIME = new SecretGiantJumpySlime(new Item.Settings().maxCount(1));
    public static final Item IRON_HEART_CARVER = new IronHeartCarver(new Item.Settings().maxDamage(1));
    public static final Item HEART = new Item(new Item.Settings());
    public static final Block SECRET_FAKE_SUN = new SecretFakeSunBlock(AbstractBlock.Settings.create().mapColor(DyeColor.WHITE).luminance(state -> 16).instrument(Instrument.BASEDRUM).requiresTool().strength(1.8f).pistonBehavior(PistonBehavior.BLOCK).solidBlock(Blocks::never).nonOpaque().solidBlock(Blocks::never).blockVision(Blocks::never));
    public static final Item SECRET_FAKE_SUN_ITEM = new SecretFakeSunBlockItem(SECRET_FAKE_SUN, new Item.Settings());
    public static final Block BIG_WHITE_BLOCK = new BigWhiteBlock(AbstractBlock.Settings.create().mapColor(DyeColor.YELLOW).instrument(Instrument.BASEDRUM).requiresTool().strength(1.8f).pistonBehavior(PistonBehavior.BLOCK).solidBlock(Blocks::never).nonOpaque().solidBlock(Blocks::never).blockVision(Blocks::never));
    public static final Item BIG_WHITE_BLOCK_ITEM = new BigWhiteBlockItem(BIG_WHITE_BLOCK, new Item.Settings());
    public static final Item FROG = new Frog(new Item.Settings().maxCount(1));
    public static final Block MELTING_BLOCK = new MeltingBlock(AbstractBlock.Settings.copy(Blocks.STONE).mapColor(MapColor.RED).dynamicBounds().strength(1.5f, 6.0f));
    public static final Block DIAMOND_ORE_CHUNKS = new DiamondOreChunks(UniformIntProvider.create(3, 7), AbstractBlock.Settings.create().mapColor(MapColor.DIAMOND_BLUE).instrument(Instrument.BASEDRUM).nonOpaque().solidBlock(Blocks::never).requiresTool().noCollision().strength(3.0f, 3.0f));

    public static void register() {
        Registry.register(Registries.BLOCK, new Identifier(SecretCraftBase.MOD_ID, "secret_magic_well_water"), SECRET_MAGIC_WELL_WATER);
        Registry.register(Registries.FLUID, new Identifier(SecretCraftBase.MOD_ID, "still_magic_well_water"), STILL_MAGIC_WELL_WATER);
        Registry.register(Registries.FLUID, new Identifier(SecretCraftBase.MOD_ID, "flowing_magic_well_water"), FLOWING_MAGIC_WELL_WATER);

        Registry.register(Registries.ITEM, new Identifier(SecretCraftBase.MOD_ID, "iron_golem_farm"), IRON_GOLEM_FARM);
        Registry.register(Registries.ITEM, new Identifier(SecretCraftBase.MOD_ID, "treasury_room"), TREASURY_ROOM);
        Registry.register(Registries.ITEM, new Identifier(SecretCraftBase.MOD_ID, "armory_room"), ARMORY_ROOM);
        Registry.register(Registries.ITEM, new Identifier(SecretCraftBase.MOD_ID, "xp_farm"), XP_FARM);
        Registry.register(Registries.ITEM, new Identifier(SecretCraftBase.MOD_ID, "secret_pig_powered_portal"), SECRET_PIG_POWERED_PORTAL);
        Registry.register(Registries.ITEM, new Identifier(SecretCraftBase.MOD_ID, "pig"), PIG);
        Registry.register(Registries.ITEM, new Identifier(SecretCraftBase.MOD_ID, "nether_portal_block"), NETHER_PORTAL_BLOCK);
        Registry.register(Registries.ITEM, new Identifier(SecretCraftBase.MOD_ID, "redstone_ore_chunks"), REDSTONE_ORE_CHUNKS);
        Registry.register(Registries.BLOCK, new Identifier(SecretCraftBase.MOD_ID, "stone_secret_redstone_mirror_melter_ore"), STONE_SECRET_REDSTONE_MIRROR_MELTER);
        Registry.register(Registries.ITEM, new Identifier(SecretCraftBase.MOD_ID, "stone_secret_redstone_mirror_melter_ore"), STONE_SECRET_REDSTONE_MIRROR_MELTER_ITEM);
        Registry.register(Registries.BLOCK, new Identifier(SecretCraftBase.MOD_ID, "deepslate_secret_redstone_mirror_melter_ore"), DEEPSLATE_SECRET_REDSTONE_MIRROR_MELTER);
        Registry.register(Registries.ITEM, new Identifier(SecretCraftBase.MOD_ID, "deepslate_secret_redstone_mirror_melter_ore"), DEEPSLATE_SECRET_REDSTONE_MIRROR_MELTER_ITEM);
        Registry.register(Registries.BLOCK, new Identifier(SecretCraftBase.MOD_ID, "secret_magic_well_cobblestone"), SECRET_MAGIC_WELL_COBBLESTONE);
        Registry.register(Registries.BLOCK, new Identifier(SecretCraftBase.MOD_ID, "secret_magic_well_mossy_cobblestone"), SECRET_MAGIC_WELL_MOSSY_COBBLESTONE);
        Registry.register(Registries.BLOCK, new Identifier(SecretCraftBase.MOD_ID, "secret_magic_well_oak_fence"), SECRET_MAGIC_WELL_OAK_FENCE);
        Registry.register(Registries.BLOCK, new Identifier(SecretCraftBase.MOD_ID, "secret_magic_well_torch"), SECRET_MAGIC_WELL_TORCH);
        Registry.register(Registries.ITEM, new Identifier(SecretCraftBase.MOD_ID, "secret_magic_well"), SECRET_MAGIC_WELL);
        Registry.register(Registries.ITEM, new Identifier(SecretCraftBase.MOD_ID, "secret_giant_jumpy_slime"), SECRET_GIANT_JUMPY_SLIME);
        Registry.register(Registries.ITEM, new Identifier(SecretCraftBase.MOD_ID, "iron_heart_carver"), IRON_HEART_CARVER);
        Registry.register(Registries.ITEM, new Identifier(SecretCraftBase.MOD_ID, "heart"), HEART);
        Registry.register(Registries.BLOCK, new Identifier(SecretCraftBase.MOD_ID, "secret_fake_sun"), SECRET_FAKE_SUN);
        Registry.register(Registries.ITEM, new Identifier(SecretCraftBase.MOD_ID, "secret_fake_sun"), SECRET_FAKE_SUN_ITEM);
        Registry.register(Registries.BLOCK, new Identifier(SecretCraftBase.MOD_ID, "big_white_block"), BIG_WHITE_BLOCK);
        Registry.register(Registries.ITEM, new Identifier(SecretCraftBase.MOD_ID, "big_white_block"), BIG_WHITE_BLOCK_ITEM);
        Registry.register(Registries.ITEM, new Identifier(SecretCraftBase.MOD_ID, "frog"), FROG);
        Registry.register(Registries.BLOCK, new Identifier(SecretCraftBase.MOD_ID, "melting_block"), MELTING_BLOCK);
        Registry.register(Registries.BLOCK, new Identifier(SecretCraftBase.MOD_ID, "diamond_ore_chunks"), DIAMOND_ORE_CHUNKS);
    }
}