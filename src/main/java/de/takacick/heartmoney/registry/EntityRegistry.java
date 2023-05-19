package de.takacick.heartmoney.registry;

import de.takacick.heartmoney.HeartMoney;
import de.takacick.heartmoney.registry.block.entity.BloodBeaconTrapBlockEntity;
import de.takacick.heartmoney.registry.block.entity.HeartwarmingNukeBlockEntity;
import de.takacick.heartmoney.registry.entity.custom.BlockBreakEntity;
import de.takacick.heartmoney.registry.entity.custom.HeartShopPortalEntity;
import de.takacick.heartmoney.registry.entity.custom.ShopItemEntity;
import de.takacick.heartmoney.registry.entity.living.GirlfriendEntity;
import de.takacick.heartmoney.registry.entity.living.HeartAngelEntity;
import de.takacick.heartmoney.registry.entity.projectiles.HeartwarmingNukeEntity;
import de.takacick.heartmoney.registry.entity.projectiles.LifeStealScytheEntity;
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

    public static final EntityType<HeartShopPortalEntity> HEART_SHOP_PORTAL = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(HeartMoney.MOD_ID, "heat_shop_portal"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, HeartShopPortalEntity::new)
                    .dimensions(EntityDimensions.changing(1.45F, 2.4f)).build()
    );
    public static final EntityType<ShopItemEntity> SHOP_ITEM = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(HeartMoney.MOD_ID, "shop_item"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ShopItemEntity::create)
                    .dimensions(EntityDimensions.fixed(0.45f, 0.45f)).build()
    );
    public static final EntityType<HeartAngelEntity> HEART_ANGEL = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(HeartMoney.MOD_ID, "heart_angel"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, HeartAngelEntity::new)
                    .dimensions(EntityDimensions.changing(0.35f, 0.6f))
                    .build()
    );
    public static final EntityType<GirlfriendEntity> GIRLFRIEND = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(HeartMoney.MOD_ID, "girlfriend"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, GirlfriendEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 1.8f)).build()
    );
    public static final EntityType<BlockBreakEntity> BLOCK_BREAK = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(HeartMoney.MOD_ID, "block_break"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, BlockBreakEntity::create)
                    .dimensions(EntityDimensions.changing(0.00001f, 0.00001f)).build()
    );
    public static final EntityType<LifeStealScytheEntity> LIFE_STEAL_SCYTHE = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(HeartMoney.MOD_ID, "life_steal_scythe"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, LifeStealScytheEntity::create)
                    .dimensions(EntityDimensions.changing(0.98F, 0.98F)).build()
    );
    public static final EntityType<HeartwarmingNukeEntity> HEARTWARMING_NUKE = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(HeartMoney.MOD_ID, "heartwarming_nuke"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, HeartwarmingNukeEntity::create)
                    .dimensions(EntityDimensions.changing(1.25F, 1.25F)).build()
    );
    public static final BlockEntityType<BloodBeaconTrapBlockEntity> BLOOD_BEACON_TRAP = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            new Identifier(HeartMoney.MOD_ID, "blood_beacon_trap"),
            FabricBlockEntityTypeBuilder.create(BloodBeaconTrapBlockEntity::new, ItemRegistry.BLOOD_BEACON_TRAP).build()
    );
    public static BlockEntityType<HeartwarmingNukeBlockEntity> HEARTWARMING_NUKE_BLOCK = Registry.register(
            Registry.BLOCK_ENTITY_TYPE, new Identifier(HeartMoney.MOD_ID, "heartwarming_nuke"),
            FabricBlockEntityTypeBuilder.create(HeartwarmingNukeBlockEntity::new, ItemRegistry.HEARTWARMING_NUKE)
                    .build()
    );
    public static void register() {
        FabricDefaultAttributeRegistry.register(EntityRegistry.HEART_ANGEL, HeartAngelEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.GIRLFRIEND, GirlfriendEntity.createAttributes());
    }
}
