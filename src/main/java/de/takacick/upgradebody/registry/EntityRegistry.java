package de.takacick.upgradebody.registry;

import de.takacick.upgradebody.UpgradeBody;
import de.takacick.upgradebody.registry.entity.custom.BlockBreakEntity;
import de.takacick.upgradebody.registry.entity.custom.ShopItemEntity;
import de.takacick.upgradebody.registry.entity.custom.UpgradeShopPortalEntity;
import de.takacick.upgradebody.registry.entity.living.AllSeeingWardenEntity;
import de.takacick.upgradebody.registry.entity.living.SentientDesertPyramidEntity;
import de.takacick.upgradebody.registry.entity.living.TurretPillagerEntity;
import de.takacick.upgradebody.registry.entity.projectiles.CustomBlockEntity;
import de.takacick.upgradebody.registry.entity.projectiles.EnergyBulletEntity;
import de.takacick.upgradebody.registry.entity.projectiles.TntEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class EntityRegistry {

    public static final EntityType<UpgradeShopPortalEntity> UPGRADE_SHOP_PORTAL = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(UpgradeBody.MOD_ID, "upgrade_shop_portal"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, UpgradeShopPortalEntity::new)
                    .dimensions(EntityDimensions.changing(2f, 3f)).build()
    );
    public static final EntityType<ShopItemEntity> SHOP_ITEM = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(UpgradeBody.MOD_ID, "shop_item"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ShopItemEntity::create)
                    .dimensions(EntityDimensions.changing(0.45f, 0.45f)).build()
    );
    public static final EntityType<TurretPillagerEntity> TURRET_PILLAGER = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(UpgradeBody.MOD_ID, "turret_pillager"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, TurretPillagerEntity::new)
                    .dimensions(EntityDimensions.changing(0.6f, 1.95f))
                    .trackRangeBlocks(8)
                    .build()
    );
    public static final EntityType<AllSeeingWardenEntity> ALL_SEEING_WARDEN = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(UpgradeBody.MOD_ID, "all_seeing_warden"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, AllSeeingWardenEntity::new)
                    .dimensions(EntityDimensions.changing(0.9f, 2.9f))
                    .trackRangeBlocks(8)
                    .build()
    );
    public static final EntityType<SentientDesertPyramidEntity> SENTIENT_DESERT_PYRAMID = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(UpgradeBody.MOD_ID, "sentient_desert_pyramid"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, SentientDesertPyramidEntity::new)
                    .dimensions(EntityDimensions.changing(21f, 11f))
                    .build()
    );
    public static final EntityType<TntEntity> TNT = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(UpgradeBody.MOD_ID, "tnt"),
            FabricEntityTypeBuilder.<TntEntity>create(SpawnGroup.MISC, TntEntity::new)
                    .dimensions(EntityDimensions.changing(0.75F, 0.75F)).build()
    );
    public static final EntityType<EnergyBulletEntity> ENERGY_BULLET = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(UpgradeBody.MOD_ID, "energy_bullet"),
            FabricEntityTypeBuilder.<EnergyBulletEntity>create(SpawnGroup.MISC, EnergyBulletEntity::new)
                    .dimensions(EntityDimensions.changing(0.75F, 0.75F)).build()
    );
    public static final EntityType<BlockBreakEntity> BLOCK_BREAK = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(UpgradeBody.MOD_ID, "block_break"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, BlockBreakEntity::create)
                    .dimensions(EntityDimensions.changing(0.00001f, 0.00001f)).build()
    );
    public static final EntityType<CustomBlockEntity> FALLING_BLOCK = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(UpgradeBody.MOD_ID, "falling_block"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, CustomBlockEntity::create)
                    .dimensions(EntityDimensions.changing(0.98F, 0.98F))
                    .build()
    );

    public static void register() {
        FabricDefaultAttributeRegistry.register(EntityRegistry.TURRET_PILLAGER, TurretPillagerEntity.createTurretPillagerAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.ALL_SEEING_WARDEN, AllSeeingWardenEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.SENTIENT_DESERT_PYRAMID, SentientDesertPyramidEntity.createAttributes());

    }
}
