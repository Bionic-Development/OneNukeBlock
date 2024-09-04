package de.takacick.onenukeblock.registry;

import de.takacick.onenukeblock.OneNukeBlock;
import de.takacick.onenukeblock.registry.block.entity.BladedTntBlockEntity;
import de.takacick.onenukeblock.registry.block.entity.NukeOneBlockEntity;
import de.takacick.onenukeblock.registry.block.entity.SkylandTntBlockEntity;
import de.takacick.onenukeblock.registry.entity.custom.BladedTntEntity;
import de.takacick.onenukeblock.registry.entity.custom.SkylandTntEntity;
import de.takacick.onenukeblock.registry.entity.living.CreeperScientistEntity;
import de.takacick.onenukeblock.registry.entity.living.HazmatVillagerEntity;
import de.takacick.onenukeblock.registry.entity.living.MutatedCreeperEntity;
import de.takacick.onenukeblock.registry.entity.living.ProtoEntity;
import de.takacick.onenukeblock.registry.entity.projectiles.CustomBlockEntity;
import de.takacick.onenukeblock.registry.entity.projectiles.DiamondSwordEntity;
import de.takacick.onenukeblock.registry.entity.projectiles.PlacingBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class EntityRegistry {

    public static final EntityType<SkylandTntEntity> SKYLAND_TNT = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(OneNukeBlock.MOD_ID, "skyland_tnt"),
            EntityType.Builder.<SkylandTntEntity>create(SkylandTntEntity::new, SpawnGroup.MISC)
                    .makeFireImmune().dimensions(0.98f, 0.98f).eyeHeight(0.15f).maxTrackingRange(10).trackingTickInterval(10)
                    .build()
    );
    public static final EntityType<PlacingBlockEntity> PLACING_BLOCK = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(OneNukeBlock.MOD_ID, "placing_block"),
            EntityType.Builder.create(PlacingBlockEntity::new, SpawnGroup.MISC)
                    .dimensions(0.25f, 0.25f)
                    .build()
    );
    public static final EntityType<BladedTntEntity> BLADED_TNT = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(OneNukeBlock.MOD_ID, "bladed_tnt"),
            EntityType.Builder.<BladedTntEntity>create(BladedTntEntity::new, SpawnGroup.MISC)
                    .makeFireImmune().dimensions(0.98f, 0.98f).eyeHeight(0.15f).maxTrackingRange(10).trackingTickInterval(10)
                    .build()
    );
    public static final EntityType<DiamondSwordEntity> DIAMOND_SWORD = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(OneNukeBlock.MOD_ID, "diamond_sword"),
            EntityType.Builder.<DiamondSwordEntity>create(DiamondSwordEntity::new, SpawnGroup.MISC)
                    .dimensions(0.25f, 0.25f).maxTrackingRange(4).trackingTickInterval(10)
                    .build()
    );
    public static final EntityType<CustomBlockEntity> FALLING_BLOCK = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(OneNukeBlock.MOD_ID, "falling_block"),
            EntityType.Builder.<CustomBlockEntity>create(CustomBlockEntity::new, SpawnGroup.MISC)
                    .dimensions(0.98F, 0.98F)
                    .build()
    );
    public static final EntityType<ProtoEntity> PROTO = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(OneNukeBlock.MOD_ID, "proto"),
            EntityType.Builder.create(ProtoEntity::new, SpawnGroup.MISC)
                    .dimensions(0.6f, 0.85f)
                    .build()
    );
    public static final EntityType<HazmatVillagerEntity> HAZMAT_VILLAGER = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(OneNukeBlock.MOD_ID, "hazmat_villager"),
            EntityType.Builder.<HazmatVillagerEntity>create(HazmatVillagerEntity::new, SpawnGroup.MISC)
                    .dimensions(0.6f, 1.95f).eyeHeight(1.62f).maxTrackingRange(10)
                    .build()
    );
    public static final EntityType<CreeperScientistEntity> CREEPER_SCIENTIST = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(OneNukeBlock.MOD_ID, "creeper_scientist"),
            EntityType.Builder.create(CreeperScientistEntity::new, SpawnGroup.MISC)
                    .dimensions(0.6f, 1.7f).maxTrackingRange(8)
                    .build()
    );
    public static final EntityType<MutatedCreeperEntity> MUTATED_CREEPER = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(OneNukeBlock.MOD_ID, "mutated_creeper"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, MutatedCreeperEntity::new)
                    .dimensions(EntityDimensions.changing(0.9f, 2.9f))
                    .build()
    );
    public static BlockEntityType<NukeOneBlockEntity> NUKE_ONE_BLOCK = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(OneNukeBlock.MOD_ID, "nuke_one_block"),
            BlockEntityType.Builder.create(NukeOneBlockEntity::new, ItemRegistry.ONE_NUKE_BLOCK)
                    .build()
    );
    public static BlockEntityType<SkylandTntBlockEntity> SKYLAND_TNT_BLOCK_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(OneNukeBlock.MOD_ID, "skyland_tnt"),
            BlockEntityType.Builder.create(SkylandTntBlockEntity::new, ItemRegistry.SKYLAND_TNT)
                    .build()
    );
    public static BlockEntityType<BladedTntBlockEntity> BLADED_TNT_BLOCK_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(OneNukeBlock.MOD_ID, "bladed_tnt"),
            BlockEntityType.Builder.create(BladedTntBlockEntity::new, ItemRegistry.BLADED_TNT)
                    .build()
    );

    public static void register() {
        FabricDefaultAttributeRegistry.register(EntityRegistry.PROTO, ProtoEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.HAZMAT_VILLAGER, HazmatVillagerEntity.createVillagerAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.CREEPER_SCIENTIST, CreeperScientistEntity.createCreeperAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.MUTATED_CREEPER, MutatedCreeperEntity.createAttributes());
    }
}
