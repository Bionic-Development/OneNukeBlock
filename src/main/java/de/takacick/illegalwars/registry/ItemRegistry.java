package de.takacick.illegalwars.registry;

import de.takacick.illegalwars.IllegalWars;
import de.takacick.illegalwars.registry.block.*;
import de.takacick.illegalwars.registry.block.fluid.SludgeLiquid;
import de.takacick.illegalwars.registry.item.BaseWarsMoneyWheel;
import de.takacick.illegalwars.registry.item.EntityItem;
import de.takacick.illegalwars.registry.item.PoopLauncher;
import de.takacick.illegalwars.registry.item.TrialSpawnerItem;
import net.minecraft.block.*;
import net.minecraft.block.enums.Instrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ItemRegistry {

    public static final FlowableFluid STILL_SLUDGE_LIQUID = new SludgeLiquid.Still();
    public static final FlowableFluid FLOWING_SLUDGE_LIQUID = new SludgeLiquid.Flowing();
    public static final FluidBlock SLUDGE_LIQUID_BLOCK = new SludgeLiquidBlock(STILL_SLUDGE_LIQUID, AbstractBlock.Settings.create().mapColor(MapColor.PALE_GREEN).replaceable().noCollision().strength(100.0f).pistonBehavior(PistonBehavior.DESTROY).dropsNothing().liquid().sounds(BlockSoundGroup.INTENTIONALLY_EMPTY));

    public static final Block BASE_WARS_MONEY_WHEEl = new BaseWarsMoneyWheelBlock(AbstractBlock.Settings.create().requiresTool().strength(5.0f, 6.0f).sounds(BlockSoundGroup.METAL).nonOpaque().dynamicBounds());
    public static final Item BASE_WARS_MONEY_WHEEl_ITEM = new BaseWarsMoneyWheel(BASE_WARS_MONEY_WHEEl, new Item.Settings());
    public static final Block MONEY_BLOCK = new MoneyBlock(AbstractBlock.Settings.copy(Blocks.ACACIA_LEAVES).nonOpaque().solidBlock((state, world, pos) -> false).blockVision((state, world, pos) -> false).suffocates((state, world, pos) -> true));
    public static final Item MONEY_BLOCK_ITEM = new BlockItem(MONEY_BLOCK, new Item.Settings());
    public static final Block POOP_LAUNCHER = new PoopLauncherBlock(AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).instrument(Instrument.BASEDRUM).requiresTool().strength(3.5f));
    public static final Item POOP_LAUNCHER_ITEM = new PoopLauncher(POOP_LAUNCHER, new Item.Settings());
    public static final Item SLUDGE_BUCKET = new BucketItem(STILL_SLUDGE_LIQUID, new Item.Settings().maxCount(1));
    public static final Item RAT = new EntityItem(EntityRegistry.RAT, new Item.Settings().maxCount(1));
    public static final Block KING_RAT_TRIAL_SPAWNER = new KingRatTrialSpawner(AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).instrument(Instrument.BASEDRUM).requiresTool().luminance(state -> state.get(TrialSpawnerBlock.TRIAL_SPAWNER_STATE).getLuminance()).strength(50.0f).sounds(BlockSoundGroup.TRIAL_SPAWNER).blockVision(Blocks::never).nonOpaque());
    public static final Item KING_RAT_TRIAL_SPAWNER_ITEM = new TrialSpawnerItem(KING_RAT_TRIAL_SPAWNER, new Item.Settings());
    public static final Block CYBER_WARDEN_SECURITY_TRIAL_SPAWNER = new CyberWardenSecurityTrialSpawner(AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).instrument(Instrument.BASEDRUM).requiresTool().luminance(state -> state.get(TrialSpawnerBlock.TRIAL_SPAWNER_STATE).getLuminance()).strength(50.0f).sounds(BlockSoundGroup.TRIAL_SPAWNER).blockVision(Blocks::never).nonOpaque());
    public static final Item CYBER_WARDEN_SECURITY_TRIAL_SPAWNER_ITEM = new TrialSpawnerItem(CYBER_WARDEN_SECURITY_TRIAL_SPAWNER, new Item.Settings());
    public static final Block DRIPSTONE_SPIKES = new DripstoneSpikes(AbstractBlock.Settings.create().mapColor(MapColor.TERRACOTTA_BROWN).solid().instrument(Instrument.BASEDRUM).nonOpaque().sounds(BlockSoundGroup.POINTED_DRIPSTONE).noCollision().ticksRandomly().strength(1.5f, 3.0f).dynamicBounds().solidBlock(Blocks::never));
    public static final Item DRIPSTONE_SPIKES_ITEM = new BlockItem(DRIPSTONE_SPIKES, new Item.Settings());
    public static final Block PIGLIN_GOLD_TURRET = new PiglinGoldTurretBlock(AbstractBlock.Settings.copyShallow(Blocks.BLACKSTONE).sounds(BlockSoundGroup.GILDED_BLACKSTONE));
    public static final Item PIGLIN_GOLD_TURRET_ITEM = new BlockItem(PIGLIN_GOLD_TURRET, new Item.Settings());
    public static final Item SHARK = new EntityItem(EntityRegistry.SHARK, new Item.Settings().maxCount(1));
    public static final Item PROTO_PUPPY = new EntityItem(EntityRegistry.PROTO_PUPPY, new Item.Settings().maxCount(1));
    public static final Block COMMAND_BLOCK_PRESSURE_PLATE = new CommandBlockPressurePlateBlock(BlockSetType.STONE, AbstractBlock.Settings.create().mapColor(MapColor.BROWN).requiresTool().solid().instrument(Instrument.BASEDRUM).requiresTool().noCollision().strength(-1.0f, 3600000.0f).dropsNothing().pistonBehavior(PistonBehavior.DESTROY));
    public static final Item COMMAND_BLOCK_PRESSURE_PLATES_ITEM = new BlockItem(COMMAND_BLOCK_PRESSURE_PLATE, new Item.Settings());

    public static void register() {
        Registry.register(Registries.BLOCK, new Identifier(IllegalWars.MOD_ID, "sludge_liquid"), SLUDGE_LIQUID_BLOCK);
        Registry.register(Registries.FLUID, new Identifier(IllegalWars.MOD_ID, "still_sludge_liquid"), STILL_SLUDGE_LIQUID);
        Registry.register(Registries.FLUID, new Identifier(IllegalWars.MOD_ID, "flowing_sludge_liquid"), FLOWING_SLUDGE_LIQUID);

        Registry.register(Registries.BLOCK, new Identifier(IllegalWars.MOD_ID, "base_wars_money_wheel"), BASE_WARS_MONEY_WHEEl);
        Registry.register(Registries.ITEM, new Identifier(IllegalWars.MOD_ID, "base_wars_money_wheel"), BASE_WARS_MONEY_WHEEl_ITEM);
        Registry.register(Registries.BLOCK, new Identifier(IllegalWars.MOD_ID, "money_block"), MONEY_BLOCK);
        Registry.register(Registries.ITEM, new Identifier(IllegalWars.MOD_ID, "money_block"), MONEY_BLOCK_ITEM);
        Registry.register(Registries.BLOCK, new Identifier(IllegalWars.MOD_ID, "poop_launcher"), POOP_LAUNCHER);
        Registry.register(Registries.ITEM, new Identifier(IllegalWars.MOD_ID, "poop_launcher"), POOP_LAUNCHER_ITEM);
        Registry.register(Registries.ITEM, new Identifier(IllegalWars.MOD_ID, "sludge_bucket"), SLUDGE_BUCKET);
        Registry.register(Registries.ITEM, new Identifier(IllegalWars.MOD_ID, "rat"), RAT);
        Registry.register(Registries.BLOCK, new Identifier(IllegalWars.MOD_ID, "king_rat_trial_spawner"), KING_RAT_TRIAL_SPAWNER);
        Registry.register(Registries.ITEM, new Identifier(IllegalWars.MOD_ID, "king_rat_trial_spawner"), KING_RAT_TRIAL_SPAWNER_ITEM);
        Registry.register(Registries.BLOCK, new Identifier(IllegalWars.MOD_ID, "dripstone_spikes"), DRIPSTONE_SPIKES);
        Registry.register(Registries.ITEM, new Identifier(IllegalWars.MOD_ID, "dripstone_spikes"), DRIPSTONE_SPIKES_ITEM);
        Registry.register(Registries.BLOCK, new Identifier(IllegalWars.MOD_ID, "piglin_gold_turret"), PIGLIN_GOLD_TURRET);
        Registry.register(Registries.ITEM, new Identifier(IllegalWars.MOD_ID, "piglin_gold_turret"), PIGLIN_GOLD_TURRET_ITEM);
        Registry.register(Registries.ITEM, new Identifier(IllegalWars.MOD_ID, "shark"), SHARK);
        Registry.register(Registries.ITEM, new Identifier(IllegalWars.MOD_ID, "proto_puppy"), PROTO_PUPPY);
        Registry.register(Registries.BLOCK, new Identifier(IllegalWars.MOD_ID, "cyber_warden_security_trial_spawner"), CYBER_WARDEN_SECURITY_TRIAL_SPAWNER);
        Registry.register(Registries.ITEM, new Identifier(IllegalWars.MOD_ID, "cyber_warden_security_trial_spawner"), CYBER_WARDEN_SECURITY_TRIAL_SPAWNER_ITEM);
        Registry.register(Registries.BLOCK, new Identifier(IllegalWars.MOD_ID, "command_block_pressure_plate"), COMMAND_BLOCK_PRESSURE_PLATE);
        Registry.register(Registries.ITEM, new Identifier(IllegalWars.MOD_ID, "command_block_pressure_plate"), COMMAND_BLOCK_PRESSURE_PLATES_ITEM);
    }
}