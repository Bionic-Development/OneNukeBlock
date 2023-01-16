package de.takacick.imagineanything.registry;

import de.takacick.imagineanything.ImagineAnything;
import de.takacick.imagineanything.registry.block.entity.ImaginedGiantBedrockSpeakersBlockEntity;
import de.takacick.imagineanything.registry.block.entity.TrappedBedBlockEntity;
import de.takacick.imagineanything.registry.entity.custom.CaveConduitEntity;
import de.takacick.imagineanything.registry.entity.custom.GiantAxeMeteorShockwaveEntity;
import de.takacick.imagineanything.registry.entity.custom.HologramItemEntity;
import de.takacick.imagineanything.registry.entity.custom.ThoughtEntity;
import de.takacick.imagineanything.registry.entity.living.*;
import de.takacick.imagineanything.registry.entity.projectiles.*;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EntityRegistry {

    public static final EntityType<CustomBlockEntity> FALLING_BLOCK = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(ImagineAnything.MOD_ID, "falling_block"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, CustomBlockEntity::create)
                    .dimensions(EntityDimensions.changing(0.98F, 0.98F))
                    .build()
    );
    public static final EntityType<EndPortalEntity> END_PORTAL = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(ImagineAnything.MOD_ID, "end_portal"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, EndPortalEntity::new)
                    .dimensions(EntityDimensions.changing(2.5f, 1f)).build()
    );
    public static final EntityType<HeadEntity> HEAD = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(ImagineAnything.MOD_ID, "head"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, HeadEntity::new)
                    .dimensions(EntityDimensions.changing(0.5f, 0.5f)).build()
    );
    public static final EntityType<GiantAxeMeteorEntity> GIANT_AXE_METEOR = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(ImagineAnything.MOD_ID, "giant_axe_meteor"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, GiantAxeMeteorEntity::create)
                    .dimensions(EntityDimensions.changing(2.48F, 2.48F)).build()
    );
    public static final EntityType<GiantAxeMeteorShockwaveEntity> GIANT_AXE_METEOR_SHOCKWAVE = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(ImagineAnything.MOD_ID, "giant_axe_meteor_shockwave"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, GiantAxeMeteorShockwaveEntity::create)
                    .dimensions(EntityDimensions.changing(0.25F, 0.25F)).build()
    );
    public static final EntityType<CaveConduitEntity> CAVE_CONDUIT = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(ImagineAnything.MOD_ID, "cave_conduit"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, CaveConduitEntity::create)
                    .dimensions(EntityDimensions.changing(0.5f, 0.5f)).build()
    );
    public static final EntityType<MysteriousEntity> MYSTERIOUS_ENTITY = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(ImagineAnything.MOD_ID, "mysterious_entity"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, MysteriousEntity::new)
                    .dimensions(EntityDimensions.changing(0.98F, 0.98F))
                    .build()
    );
    public static final EntityType<AlfredThePickaxeEntity> ALFRED_THE_PICKAXE = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(ImagineAnything.MOD_ID, "alfred_the_pickaxe"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, AlfredThePickaxeEntity::new)
                    .dimensions(EntityDimensions.changing(0.8f, 1.95f)).build()
    );
    public static final EntityType<MallPillagerGuardsEntity> MALL_PILLAGER_GUARDS = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(ImagineAnything.MOD_ID, "mall_pillager_guards"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, MallPillagerGuardsEntity::new)
                    .dimensions(EntityDimensions.changing(0.6f, 1.95f)).build()
    );
    public static final EntityType<ThanosChadEntity> THANOS_CHAD = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(ImagineAnything.MOD_ID, "thanos_chad"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ThanosChadEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 1.8f)).build()
    );
    public static final EntityType<ThanosShotEntity> THANOS_SHOT = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(ImagineAnything.MOD_ID, "thanos_shot"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ThanosShotEntity::create)
                    .dimensions(EntityDimensions.changing(0.98F, 0.98F))
                    .build()
    );
    public static final EntityType<HologramItemEntity> HOLOGRAM_ITEM = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(ImagineAnything.MOD_ID, "hologram_item"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, HologramItemEntity::create)
                    .dimensions(EntityDimensions.fixed(0.45f, 0.45f)).build()
    );
    public static final EntityType<ThoughtEntity> THOUGHT = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(ImagineAnything.MOD_ID, "thought"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ThoughtEntity::create)
                    .dimensions(EntityDimensions.fixed(0.7f, 0.7f)).build()
    );
    public static final EntityType<IronManLaserBulletEntity> IRON_MAN_LASER_BULLET = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(ImagineAnything.MOD_ID, "iron_man_laser_bullet"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, IronManLaserBulletEntity::create)
                    .dimensions(EntityDimensions.fixed(0.5F, 0.5F)).build()
    );
    public static final EntityType<GiantNetheriteFeatherEntity> GIANT_NETHERITE_FEATHER = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(ImagineAnything.MOD_ID, "giant_netherite_feather"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, GiantNetheriteFeatherEntity::create)
                    .dimensions(EntityDimensions.changing(0.5F, 0.5F)).build()
    );
    public static final EntityType<GiantVibrationEntity> GIANT_VIBRATION = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(ImagineAnything.MOD_ID, "giant_vibration"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, GiantVibrationEntity::create)
                    .dimensions(EntityDimensions.changing(0.2F, 0.2F)).build()
    );
    public static final BlockEntityType<TrappedBedBlockEntity> TRAPPED_BED_BLOCK = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            new Identifier(ImagineAnything.MOD_ID, "trapped_bed"),
            FabricBlockEntityTypeBuilder.create(TrappedBedBlockEntity::new, ItemRegistry.IMAGINED_INFINITY_TRAPPED_BED)
                    .build()
    );
    public static final BlockEntityType<ImaginedGiantBedrockSpeakersBlockEntity> IMAGINED_GIANT_BEDROCK_SPEAKERS_ENTITY = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            new Identifier(ImagineAnything.MOD_ID, "imagined_giant_bedrock_speakers"),
            FabricBlockEntityTypeBuilder.create(ImaginedGiantBedrockSpeakersBlockEntity::new, ItemRegistry.IMAGINED_GIANT_BEDROCK_SPEAKERS)
                    .build()
    );

    public static void register() {
        FabricDefaultAttributeRegistry.register(EntityRegistry.END_PORTAL, EndPortalEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.HEAD, HeadEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.CAVE_CONDUIT, CaveConduitEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.MYSTERIOUS_ENTITY, MysteriousEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.ALFRED_THE_PICKAXE, AlfredThePickaxeEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.MALL_PILLAGER_GUARDS, MallPillagerGuardsEntity.createPillagerAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.THANOS_CHAD, ThanosChadEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.THOUGHT, ThoughtEntity.createMobAttributes());
    }
}
