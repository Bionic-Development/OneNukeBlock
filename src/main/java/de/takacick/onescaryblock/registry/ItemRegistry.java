package de.takacick.onescaryblock.registry;

import de.takacick.onescaryblock.OneScaryBlock;
import de.takacick.onescaryblock.registry.block.BloodBlock;
import de.takacick.onescaryblock.registry.block.Item303Block;
import de.takacick.onescaryblock.registry.block.PhantomBlock;
import de.takacick.onescaryblock.registry.block.ScaryOneBlock;
import de.takacick.onescaryblock.registry.block.fluid.BloodFluid;
import de.takacick.onescaryblock.registry.item.*;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ItemRegistry {

    public static final FlowableFluid STILL_BLOOD = new BloodFluid.Still();
    public static final FlowableFluid FLOWING_BLOOD = new BloodFluid.Flowing();
    public static final FluidBlock BLOOD = new BloodBlock(STILL_BLOOD, AbstractBlock.Settings.create().mapColor(MapColor.DARK_RED).replaceable().noCollision().strength(100.0f).pistonBehavior(PistonBehavior.DESTROY).dropsNothing().liquid().sounds(BlockSoundGroup.INTENTIONALLY_EMPTY));

    public static final Block SCARY_ONE_BLOCK = new ScaryOneBlock(AbstractBlock.Settings.create().mapColor(MapColor.DARK_RED).strength(0.5f).sounds(BlockSoundGroup.SOUL_SAND));
    public static final Item SCARY_ONE_BLOCK_ITEM = new BlockItem(SCARY_ONE_BLOCK, new Item.Settings());
    public static final Item SOUL_PIERCER = new SoulPiercer(SoulPiercer.SOUL_MATERIAL, 1, -2.8f, new Item.Settings());
    public static final Item SOUL_FRAGMENT = new SoulFragment(new Item.Settings());
    public static final Block PHANTOM_BLOCK = new PhantomBlock(AbstractBlock.Settings.create().mapColor(MapColor.LIGHT_BLUE).slipperiness(0.8f).sounds(BlockSoundGroup.SLIME));
    public static final Item PHANTOM_BLOCK_ITEM = new PhantomBlockItem(PHANTOM_BLOCK, new Item.Settings().maxDamage(32));
    public static final Item HEROBRINE_LIGHTNING_BOLT = new HerobrineLightningBolt(new Item.Settings().maxCount(1));
    public static final Item BLOOD_BORDER_SUIT = new BloodBorderSuit(new Item.Settings().maxCount(1));
    public static final Item BLOOD_BUCKET = new BloodBucket(STILL_BLOOD, new Item.Settings().maxCount(1));
    public static final Item ITEM_303 = new Item303(new Item.Settings().maxCount(1));
    public static final Block ITEM_303_BLOCK = new Item303Block(AbstractBlock.Settings.create().mapColor(MapColor.DARK_RED).noBlockBreakParticles().strength(-1));

    public static void register() {
        Registry.register(Registries.BLOCK, new Identifier(OneScaryBlock.MOD_ID, "blood"), BLOOD);
        Registry.register(Registries.FLUID, new Identifier(OneScaryBlock.MOD_ID, "still_blood"), STILL_BLOOD);
        Registry.register(Registries.FLUID, new Identifier(OneScaryBlock.MOD_ID, "flowing_blood"), FLOWING_BLOOD);

        Registry.register(Registries.BLOCK, new Identifier(OneScaryBlock.MOD_ID, "scary_one_block"), SCARY_ONE_BLOCK);
        Registry.register(Registries.ITEM, new Identifier(OneScaryBlock.MOD_ID, "scary_one_block"), SCARY_ONE_BLOCK_ITEM);
        Registry.register(Registries.ITEM, new Identifier(OneScaryBlock.MOD_ID, "soul_piercer"), SOUL_PIERCER);
        Registry.register(Registries.ITEM, new Identifier(OneScaryBlock.MOD_ID, "soul_fragment"), SOUL_FRAGMENT);
        Registry.register(Registries.BLOCK, new Identifier(OneScaryBlock.MOD_ID, "phantom_block"), PHANTOM_BLOCK);
        Registry.register(Registries.ITEM, new Identifier(OneScaryBlock.MOD_ID, "phantom_block"), PHANTOM_BLOCK_ITEM);
        Registry.register(Registries.ITEM, new Identifier(OneScaryBlock.MOD_ID, "herobrine_lightning_bolt"), HEROBRINE_LIGHTNING_BOLT);
        Registry.register(Registries.ITEM, new Identifier(OneScaryBlock.MOD_ID, "blood_border_suit"), BLOOD_BORDER_SUIT);
        Registry.register(Registries.ITEM, new Identifier(OneScaryBlock.MOD_ID, "blood_bucket"), BLOOD_BUCKET);
        Registry.register(Registries.ITEM, new Identifier(OneScaryBlock.MOD_ID, "item_303"), ITEM_303);
        Registry.register(Registries.BLOCK, new Identifier(OneScaryBlock.MOD_ID, "item_303_block"), ITEM_303_BLOCK);
    }
}