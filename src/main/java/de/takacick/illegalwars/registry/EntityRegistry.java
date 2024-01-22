package de.takacick.illegalwars.registry;

import de.takacick.illegalwars.IllegalWars;
import de.takacick.illegalwars.registry.block.entity.*;
import de.takacick.illegalwars.registry.entity.custom.MoneyBlockEntity;
import de.takacick.illegalwars.registry.entity.living.*;
import de.takacick.illegalwars.registry.entity.projectiles.CyberLaserEntity;
import de.takacick.illegalwars.registry.entity.projectiles.GoldBlockEntity;
import de.takacick.illegalwars.registry.entity.projectiles.PoopEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
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

    public static final EntityType<MoneyBlockEntity> MONEY_BLOCK = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(IllegalWars.MOD_ID, "money_block"),
            FabricEntityTypeBuilder.<MoneyBlockEntity>create(SpawnGroup.MISC, MoneyBlockEntity::new)
                    .dimensions(EntityDimensions.changing(0.98F, 0.98F))
                    .build()
    );
    public static final EntityType<ProtoPuppyEntity> PROTO_PUPPY = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(IllegalWars.MOD_ID, "proto_puppy"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ProtoPuppyEntity::new)
                    .dimensions(EntityDimensions.changing(0.6f, 0.85f)).build()
    );
    public static final EntityType<RatEntity> RAT = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(IllegalWars.MOD_ID, "rat"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, RatEntity::new)
                    .dimensions(EntityDimensions.changing(0.4f, 0.3f))
                    .build()
    );
    public static final EntityType<PoopEntity> POOP = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(IllegalWars.MOD_ID, "poop"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, PoopEntity::create)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.5f)).build()
    );
    public static final EntityType<GoldBlockEntity> GOLD_BLOCK = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(IllegalWars.MOD_ID, "gold_block"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, GoldBlockEntity::create)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.5f)).build()
    );
    public static final EntityType<SharkEntity> SHARK = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(IllegalWars.MOD_ID, "shark"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, SharkEntity::new)
                    .dimensions(EntityDimensions.changing(0.9f, 0.6f))
                    .build()
    );
    public static final EntityType<KingRatEntity> KING_RAT = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(IllegalWars.MOD_ID, "king_rat"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, KingRatEntity::new)
                    .dimensions(EntityDimensions.changing(0.9f * 0.4f, 2.9f * 0.4f))
                    .build()
    );
    public static final EntityType<CyberWardenSecurityEntity> CYBER_WARDEN_SECURITY = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(IllegalWars.MOD_ID, "cyber_warden_security"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, CyberWardenSecurityEntity::new)
                    .dimensions(EntityDimensions.changing(0.9f, 2.9f))
                    .build()
    );
    public static final EntityType<CyberLaserEntity> CYBER_LASER = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(IllegalWars.MOD_ID, "cyber_laser"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, CyberLaserEntity::create)
                    .dimensions(EntityDimensions.changing(0.1125f, 0.1125f))
                    .build()
    );
    public static BlockEntityType<BaseWarsMoneyWheelBlockEntity> BASE_WARS_MONEY_WHEEL = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(IllegalWars.MOD_ID, "base_wars_money_wheel"),
            FabricBlockEntityTypeBuilder.create(BaseWarsMoneyWheelBlockEntity::new, ItemRegistry.BASE_WARS_MONEY_WHEEl)
                    .build()
    );
    public static BlockEntityType<PoopLauncherBlockEntity> POOP_LAUNCHER = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(IllegalWars.MOD_ID, "poop_launcher"),
            FabricBlockEntityTypeBuilder.create(PoopLauncherBlockEntity::new, ItemRegistry.POOP_LAUNCHER)
                    .build()
    );
    public static BlockEntityType<CommandBlockPressurePlateBlockEntity> COMMAND_BLOCK_PRESSURE_PLATE = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(IllegalWars.MOD_ID, "command_block_pressure_plate"),
            FabricBlockEntityTypeBuilder.create(CommandBlockPressurePlateBlockEntity::new, ItemRegistry.COMMAND_BLOCK_PRESSURE_PLATE)
                    .build()
    );
    public static BlockEntityType<KingRatTrialSpawnerBlockEntity> KING_RAT_TRIAL_SPAWNER = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(IllegalWars.MOD_ID, "king_rat_trial_spawner"),
            FabricBlockEntityTypeBuilder.create(KingRatTrialSpawnerBlockEntity::new, ItemRegistry.KING_RAT_TRIAL_SPAWNER)
                    .build()
    );
    public static BlockEntityType<CyberWardenSecurityTrialSpawnerBlockEntity> CYBER_WARDEN_SECURITY_TRIAL_SPAWNER = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(IllegalWars.MOD_ID, "cyber_warden_security_trial_spawner"),
            FabricBlockEntityTypeBuilder.create(CyberWardenSecurityTrialSpawnerBlockEntity::new, ItemRegistry.CYBER_WARDEN_SECURITY_TRIAL_SPAWNER)
                    .build()
    );
    public static BlockEntityType<PiglinGoldTurretBlockEntity> PIGLIN_GOLD_TURRET = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(IllegalWars.MOD_ID, "piglin_gold_turret"),
            FabricBlockEntityTypeBuilder.create(PiglinGoldTurretBlockEntity::new, ItemRegistry.PIGLIN_GOLD_TURRET)
                    .build()
    );

    public static void register() {
        FabricDefaultAttributeRegistry.register(EntityRegistry.PROTO_PUPPY, ProtoPuppyEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.RAT, RatEntity.createRatAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.SHARK, SharkEntity.createSharkAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.KING_RAT, KingRatEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.CYBER_WARDEN_SECURITY, CyberWardenSecurityEntity.createAttributes());
    }
}
