package de.takacick.immortalmobs.registry;

import de.takacick.immortalmobs.ImmortalMobs;
import de.takacick.immortalmobs.registry.block.entity.ImmortalChainTrapBlockEntity;
import de.takacick.immortalmobs.registry.block.entity.ImmortalWoolBlockEntity;
import de.takacick.immortalmobs.registry.entity.custom.BlackHoleEntity;
import de.takacick.immortalmobs.registry.entity.custom.ImmortalEndCrystalEntity;
import de.takacick.immortalmobs.registry.entity.custom.ImmortalItemEntity;
import de.takacick.immortalmobs.registry.entity.dragon.ImmortalEnderDragonEntity;
import de.takacick.immortalmobs.registry.entity.living.*;
import de.takacick.immortalmobs.registry.entity.projectiles.*;
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
            new Identifier(ImmortalMobs.MOD_ID, "falling_block"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, CustomBlockEntity::create)
                    .dimensions(EntityDimensions.changing(0.98F, 0.98F))
                    .build()
    );
    public static final EntityType<ImmortalSheepEntity> IMMORTAL_SHEEP = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(ImmortalMobs.MOD_ID, "immortal_sheep"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ImmortalSheepEntity::new)
                    .dimensions(EntityDimensions.changing(0.9f, 1.3f))
                    .build()
    );
    public static final EntityType<ImmortalPigEntity> IMMORTAL_PIG = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(ImmortalMobs.MOD_ID, "immortal_pig"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ImmortalPigEntity::new)
                    .dimensions(EntityDimensions.changing(0.9f, 0.9f))
                    .build()
    );
    public static final EntityType<ImmortalWolfEntity> IMMORTAL_WOLF = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(ImmortalMobs.MOD_ID, "immortal_wolf"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ImmortalWolfEntity::new)
                    .dimensions(EntityDimensions.changing(0.6f, 0.85f))
                    .build()
    );
    public static final EntityType<ImmortalIronGolemEntity> IMMORTAL_IRON_GOLEM = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(ImmortalMobs.MOD_ID, "immortal_iron_golem"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ImmortalIronGolemEntity::new)
                    .dimensions(EntityDimensions.changing(1.4f, 2.7f))
                    .build()
    );
    public static final EntityType<ImmortalSkeletonEntity> IMMORTAL_SKELETON = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(ImmortalMobs.MOD_ID, "immortal_skeleton"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ImmortalSkeletonEntity::new)
                    .dimensions(EntityDimensions.changing(0.6f, 1.99f))
                    .build()
    );
    public static final EntityType<ImmortalCreeperEntity> IMMORTAL_CREEPER = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(ImmortalMobs.MOD_ID, "immortal_creeper"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ImmortalCreeperEntity::new)
                    .dimensions(EntityDimensions.changing(0.6f, 1.7f))
                    .build()
    );
    public static final EntityType<ImmortalEndermanEntity> IMMORTAL_ENDERMAN = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(ImmortalMobs.MOD_ID, "immortal_enderman"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ImmortalEndermanEntity::new)
                    .dimensions(EntityDimensions.changing(0.6f, 2.9f))
                    .build()
    );
    public static final EntityType<ImmortalEnderDragonEntity> IMMORTAL_ENDER_DRAGON = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(ImmortalMobs.MOD_ID, "immortal_ender_dragon"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ImmortalEnderDragonEntity::new)
                    .dimensions(EntityDimensions.changing(16.0F, 8.0F))
                    .build()
    );
    public static final EntityType<ImmortalFireworkEntity> IMMORTAL_FIREWORK = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(ImmortalMobs.MOD_ID, "immortal_firework"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ImmortalFireworkEntity::create)
                    .dimensions(EntityDimensions.changing(0.25f, 0.25f))
                    .build()
    );
    public static final EntityType<ImmortalFireworkExplosionEntity> IMMORTAL_FIREWORK_EXPLOSION = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(ImmortalMobs.MOD_ID, "immortal_firework_explosion"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ImmortalFireworkExplosionEntity::create)
                    .dimensions(EntityDimensions.changing(0.25f, 0.25f))
                    .build()
    );
    public static final EntityType<ImmortalEndCrystalEntity> IMMORTAL_END_CRYSTAL = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(ImmortalMobs.MOD_ID, "immortal_end_crystal"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ImmortalEndCrystalEntity::create)
                    .dimensions(EntityDimensions.changing(2.0f, 2.0f))
                    .build()
    );
    public static final EntityType<ImmortalItemEntity> IMMORTAL_ITEM = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(ImmortalMobs.MOD_ID, "immortal_item"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ImmortalItemEntity::create)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f)).build()
    );
    public static final EntityType<ImmortalPickaxeEntity> IMMORTAL_PICKAXE = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(ImmortalMobs.MOD_ID, "immortal_pickaxe"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ImmortalPickaxeEntity::create)
                    .dimensions(EntityDimensions.changing(0.98F, 0.98F)).build()
    );
    public static final EntityType<ImmortalDragonBallEntity> IMMORTAL_DRAGON_BALL = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(ImmortalMobs.MOD_ID, "immortal_dragon_ball"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ImmortalDragonBallEntity::create)
                    .dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build()
    );
    public static final EntityType<ImmortalDragonBreathEntity> IMMORTAL_DRAGON_BREATH = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(ImmortalMobs.MOD_ID, "immortal_dragon_breath"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ImmortalDragonBreathEntity::create)
                    .dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build()
    );
    public static final EntityType<ImmortalArrowEntity> IMMORTAL_ARROW = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(ImmortalMobs.MOD_ID, "immortal_arrow"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ImmortalArrowEntity::create)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.5f)).build()
    );
    public static final EntityType<BlackHoleEntity> BLACK_HOLE = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(ImmortalMobs.MOD_ID, "black_hole"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, BlackHoleEntity::new)
                    .dimensions(EntityDimensions.changing(0.5F, 0.5F))
                    .build()
    );
    public static BlockEntityType<ImmortalWoolBlockEntity> IMMORTAL_WOOL = Registry.register(
            Registry.BLOCK_ENTITY_TYPE, new Identifier(ImmortalMobs.MOD_ID, "immortal_wool"),
            FabricBlockEntityTypeBuilder.create(ImmortalWoolBlockEntity::new, ItemRegistry.IMMORTAL_WOOL)
                    .build()
    );
    public static BlockEntityType<ImmortalChainTrapBlockEntity> IMMORTAL_CHAIN_TRAP = Registry.register(
            Registry.BLOCK_ENTITY_TYPE, new Identifier(ImmortalMobs.MOD_ID, "immortal_chain_trap"),
            FabricBlockEntityTypeBuilder.create(ImmortalChainTrapBlockEntity::new, ItemRegistry.IMMORTAL_CHAIN_TRAP)
                    .build()
    );

    public static void register() {
        FabricDefaultAttributeRegistry.register(EntityRegistry.IMMORTAL_SHEEP, ImmortalSheepEntity.createSheepAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.IMMORTAL_PIG, ImmortalPigEntity.createPigAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.IMMORTAL_WOLF, ImmortalWolfEntity.createWolfAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.IMMORTAL_IRON_GOLEM, ImmortalIronGolemEntity.createIronGolemAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.IMMORTAL_SKELETON, ImmortalSkeletonEntity.createAbstractSkeletonAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.IMMORTAL_CREEPER, ImmortalCreeperEntity.createCreeperAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.IMMORTAL_ENDERMAN, ImmortalEndermanEntity.createEndermanAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.IMMORTAL_ENDER_DRAGON, ImmortalEnderDragonEntity.createEnderDragonAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.BLACK_HOLE, BlackHoleEntity.createAttributes());
    }
}
