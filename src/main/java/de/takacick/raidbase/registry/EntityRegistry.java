package de.takacick.raidbase.registry;

import de.takacick.raidbase.RaidBase;
import de.takacick.raidbase.registry.block.entity.BeaconDeathLaserBlockEntity;
import de.takacick.raidbase.registry.block.entity.CopperHopperBlockEntity;
import de.takacick.raidbase.registry.block.entity.PieLauncherBlockEntity;
import de.takacick.raidbase.registry.entity.projectiles.PieEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class EntityRegistry {

    public static final EntityType<PieEntity> PIE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(RaidBase.MOD_ID, "pie"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, PieEntity::create)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.5f)).build()
    );
    public static final EntityType<LightningEntity> BAN_LIGHTNING = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(RaidBase.MOD_ID, "ban_lightning"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, LightningEntity::new)
                    .dimensions(EntityDimensions.changing(0.25f, 0.25f))
                    .build()
    );
    public static BlockEntityType<BeaconDeathLaserBlockEntity> BEACON_DEATH_LASER = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(RaidBase.MOD_ID, "beacon_death_laser"),
            FabricBlockEntityTypeBuilder.create(BeaconDeathLaserBlockEntity::new, ItemRegistry.BEACON_DEATH_LASER)
                    .build()
    );
    public static BlockEntityType<CopperHopperBlockEntity> COPPER_HOPPER = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(RaidBase.MOD_ID, "copper_hopper"),
            FabricBlockEntityTypeBuilder.create(CopperHopperBlockEntity::new, ItemRegistry.COPPER_HOPPER, ItemRegistry.EXPOSED_COPPER_HOPPER,
                            ItemRegistry.WEATHERED_COPPER_HOPPER, ItemRegistry.OXIDIZED_COPPER_HOPPER,
                            ItemRegistry.WAXED_COPPER_HOPPER, ItemRegistry.WAXED_EXPOSED_COPPER_HOPPER,
                            ItemRegistry.WAXED_WEATHERED_COPPER_HOPPER, ItemRegistry.WAXED_OXIDIZED_COPPER_HOPPER)
                    .build()
    );
    public static BlockEntityType<PieLauncherBlockEntity> PIE_LAUNCHER = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(RaidBase.MOD_ID, "pie_launcher"),
            FabricBlockEntityTypeBuilder.create(PieLauncherBlockEntity::new, ItemRegistry.PIE_LAUNCHER)
                    .build()
    );

    public static void register() {

    }
}
