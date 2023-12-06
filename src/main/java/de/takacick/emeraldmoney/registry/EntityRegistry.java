package de.takacick.emeraldmoney.registry;

import de.takacick.emeraldmoney.EmeraldMoney;
import de.takacick.emeraldmoney.registry.entity.custom.*;
import de.takacick.emeraldmoney.registry.entity.living.CreepagerEntity;
import de.takacick.emeraldmoney.registry.entity.projectile.CustomBlockEntity;
import de.takacick.emeraldmoney.registry.entity.projectile.PillagerProjectileEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class EntityRegistry {

    public static final EntityType<EmeraldShopPortalEntity> EMERALD_SHOP_PORTAL = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(EmeraldMoney.MOD_ID, "emerald_shop_portal"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, EmeraldShopPortalEntity::new)
                    .dimensions(EntityDimensions.changing(2F, 6f)).build()
    );
    public static final EntityType<ShopItemEntity> SHOP_ITEM = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(EmeraldMoney.MOD_ID, "shop_item"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ShopItemEntity::create)
                    .dimensions(EntityDimensions.changing(0.45f, 0.45f)).build()
    );
    public static final EntityType<BlockBreakEntity> BLOCK_BREAK = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(EmeraldMoney.MOD_ID, "block_break"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, BlockBreakEntity::create)
                    .dimensions(EntityDimensions.changing(0.00001f, 0.00001f)).build()
    );
    public static final EntityType<PillagerProjectileEntity> PILLAGER = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(EmeraldMoney.MOD_ID, "pillager"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, PillagerProjectileEntity::create)
                    .dimensions(EntityDimensions.fixed(1f, 1f)).build()
    );
    public static final EntityType<CreepagerEntity> CREEPAGER = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(EmeraldMoney.MOD_ID, "creepager"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, CreepagerEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 1.7f)).trackRangeBlocks(8).build()
    );
    public static final EntityType<VillagerNoseEntity> VILLAGER_NOSE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(EmeraldMoney.MOD_ID, "villager_nose"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, VillagerNoseEntity::create)
                    .dimensions(EntityDimensions.changing(0.01f, 0.01f)).build()
    );
    public static final EntityType<CustomBlockEntity> FALLING_BLOCK = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(EmeraldMoney.MOD_ID, "falling_block"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, CustomBlockEntity::create)
                    .dimensions(EntityDimensions.changing(0.98F, 0.98F))
                    .build()
    );
    public static final EntityType<VillagerSpikeEntity> VILLAGER_SPIKE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(EmeraldMoney.MOD_ID, "villager_spike"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, VillagerSpikeEntity::create)
                    .dimensions(EntityDimensions.fixed(0.75f, 1.4f)).build()
    );
    public static void register() {
        FabricDefaultAttributeRegistry.register(EntityRegistry.CREEPAGER, CreepagerEntity.createCreeperAttributes());

    }
}
