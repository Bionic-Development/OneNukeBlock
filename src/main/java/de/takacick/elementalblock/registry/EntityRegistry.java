package de.takacick.elementalblock.registry;

import de.takacick.elementalblock.OneElementalBlock;
import de.takacick.elementalblock.registry.entity.custom.BlockBreakEntity;
import de.takacick.elementalblock.registry.entity.living.*;
import de.takacick.elementalblock.registry.entity.projectile.CobblestoneEntity;
import de.takacick.elementalblock.registry.entity.projectile.MagmaEntity;
import de.takacick.elementalblock.registry.entity.projectile.TsunamicTridentEntity;
import de.takacick.elementalblock.registry.entity.projectile.WhisperwindEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class EntityRegistry {

    public static final EntityType<BlockBreakEntity> BLOCK_BREAK = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(OneElementalBlock.MOD_ID, "block_break"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, BlockBreakEntity::create)
                    .dimensions(EntityDimensions.changing(0.00001f, 0.00001f)).build()
    );
    public static final EntityType<CobblestoneEntity> COBBLESTONE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(OneElementalBlock.MOD_ID, "cobblestone"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, CobblestoneEntity::create)
                    .dimensions(EntityDimensions.fixed(2.9f, 2.9f)).build()
    );
    public static final EntityType<MagmaEntity> MAGMA = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(OneElementalBlock.MOD_ID, "magma"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, MagmaEntity::create)
                    .dimensions(EntityDimensions.fixed(1.5f, 1.5f)).build()
    );
    public static final EntityType<WhisperwindEntity> WHISPERWIND = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(OneElementalBlock.MOD_ID, "whisperwind"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, WhisperwindEntity::create)
                    .dimensions(EntityDimensions.changing(0.3125f, 0.3125f))
                    .build()
    );
    public static final EntityType<TsunamicTridentEntity> TSUNAMIC_TRIDENT = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(OneElementalBlock.MOD_ID, "tsunamic_trident"),
            FabricEntityTypeBuilder.<TsunamicTridentEntity>create(SpawnGroup.MISC, TsunamicTridentEntity::new)
                    .dimensions(EntityDimensions.changing(2.04f, 2.04f)).build()
    );
    public static final EntityType<MagicCloudBuddyEntity> MAGIC_CLOUD_BUDDY = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(OneElementalBlock.MOD_ID, "magic_cloud_buddy"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, MagicCloudBuddyEntity::new)
                    .dimensions(EntityDimensions.changing(3, 1))
                    .build()
    );
    public static final EntityType<EarthElementalCreeperEntity> EARTH_ELEMENTAL_CREEPER = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(OneElementalBlock.MOD_ID, "earth_elemental_creeper"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, EarthElementalCreeperEntity::new)
                    .dimensions(EntityDimensions.changing(0.6f, 1.7f))
                    .build()
    );
    public static final EntityType<AirElementalGolemEntity> AIR_ELEMENTAL_GOLEM = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(OneElementalBlock.MOD_ID, "air_elemental_golem"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, AirElementalGolemEntity::new)
                    .dimensions(EntityDimensions.changing(0.4f, 0.8f))
                    .build()
    );
    public static final EntityType<WaterElementalSlimeEntity> WATER_ELEMENTAL_SLIME = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(OneElementalBlock.MOD_ID, "water_elemental_slime"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, WaterElementalSlimeEntity::new)
                    .dimensions(EntityDimensions.changing(2.04f, 2.04f)).build()
    );
    public static final EntityType<FireElementalWardenEntity> FIRE_ELEMENTAL_WARDEN = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(OneElementalBlock.MOD_ID, "fire_elemental_warden"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, FireElementalWardenEntity::new)
                    .dimensions(EntityDimensions.changing(0.9f, 2.9f)).build()
    );

    public static void register() {
        FabricDefaultAttributeRegistry.register(EntityRegistry.MAGIC_CLOUD_BUDDY, MagicCloudBuddyEntity.createMagicCloudBuddyAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.EARTH_ELEMENTAL_CREEPER, EarthElementalCreeperEntity.createCreeperAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.AIR_ELEMENTAL_GOLEM, AirElementalGolemEntity.createVexAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.WATER_ELEMENTAL_SLIME, HostileEntity.createHostileAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.FIRE_ELEMENTAL_WARDEN, FireElementalWardenEntity.createAttributes());
    }
}
