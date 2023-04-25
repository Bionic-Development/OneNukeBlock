package de.takacick.everythinghearts.registry;

import de.takacick.everythinghearts.EverythingHearts;
import de.takacick.everythinghearts.registry.block.entity.HeartChestBlockEntity;
import de.takacick.everythinghearts.registry.block.entity.WeatherHeartBeaconBlockEntity;
import de.takacick.everythinghearts.registry.entity.living.LoverWardenEntity;
import de.takacick.everythinghearts.registry.entity.living.ProtoEntity;
import de.takacick.everythinghearts.registry.entity.living.RevivedPlayerEntity;
import de.takacick.everythinghearts.registry.entity.projectiles.HeartEntity;
import de.takacick.everythinghearts.registry.entity.projectiles.HeartScytheEntity;
import de.takacick.everythinghearts.registry.entity.projectiles.HeartmondEntity;
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

    public static final EntityType<ProtoEntity> PROTO = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(EverythingHearts.MOD_ID, "proto"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ProtoEntity::new)
                    .dimensions(EntityDimensions.changing(0.6f, 0.85f)).build()
    );

    public static final EntityType<RevivedPlayerEntity> REVIVED_PLAYER = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(EverythingHearts.MOD_ID, "revived_player"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, RevivedPlayerEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 1.8f)).build()
    );
    public static final EntityType<LoverWardenEntity> LOVER_WARDEN = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(EverythingHearts.MOD_ID, "lover_warden"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, LoverWardenEntity::new)
                    .dimensions(EntityDimensions.changing(0.9f, 2.9f)).build()
    );
    public static final EntityType<HeartEntity> HEART = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(EverythingHearts.MOD_ID, "heart"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, HeartEntity::create)
                    .dimensions(EntityDimensions.changing(0.48F, 0.48F))
                    .build()
    );
    public static final EntityType<HeartScytheEntity> HEART_SCYTHE = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(EverythingHearts.MOD_ID, "heart_scythe"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, HeartScytheEntity::create)
                    .dimensions(EntityDimensions.changing(0.98F, 0.98F)).build()
    );
    public static final EntityType<HeartmondEntity> HEARTMOND = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(EverythingHearts.MOD_ID, "heartmond"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, HeartmondEntity::create)
                    .dimensions(EntityDimensions.changing(0.1F, 0.1F)).build()
    );
    public static BlockEntityType<HeartChestBlockEntity> HEART_CHEST = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            new Identifier(EverythingHearts.MOD_ID, "heart_chest"),
            FabricBlockEntityTypeBuilder.create(HeartChestBlockEntity::new,
                    ItemRegistry.HEART_CHEST).build()
    );
    public static final BlockEntityType<WeatherHeartBeaconBlockEntity> WEATHER_HEART_BEACON = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            new Identifier(EverythingHearts.MOD_ID, "weather_heart_beacon"),
            FabricBlockEntityTypeBuilder.create(WeatherHeartBeaconBlockEntity::new, ItemRegistry.WEATHER_HEART_BEACON).build()
    );

    public static void register() {
        FabricDefaultAttributeRegistry.register(EntityRegistry.PROTO, ProtoEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.REVIVED_PLAYER, RevivedPlayerEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.LOVER_WARDEN, LoverWardenEntity.createAttributes());
    }
}
