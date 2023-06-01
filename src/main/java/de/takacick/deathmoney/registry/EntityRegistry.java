package de.takacick.deathmoney.registry;

import de.takacick.deathmoney.DeathMoney;
import de.takacick.deathmoney.registry.block.entity.BlackMatterBlockEntity;
import de.takacick.deathmoney.registry.entity.custom.*;
import de.takacick.deathmoney.registry.entity.living.CrazyExGirlfriendEntity;
import de.takacick.deathmoney.registry.entity.living.HungryTitanEntity;
import de.takacick.deathmoney.registry.entity.living.LittleWitherBullyEntity;
import de.takacick.deathmoney.registry.entity.projectiles.*;
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
            new Identifier(DeathMoney.MOD_ID, "falling_block"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, CustomBlockEntity::create)
                    .dimensions(EntityDimensions.changing(0.98F, 0.98F))
                    .build()
    );
    public static final EntityType<DangerousBlockEntity> DANGEROUS_BLOCK = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(DeathMoney.MOD_ID, "dangerous_block"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, DangerousBlockEntity::create)
                    .dimensions(EntityDimensions.changing(0.48F, 0.48F))
                    .build()
    );
    public static final EntityType<DeathShopPortalEntity> DEATH_SHOP_PORTAL = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(DeathMoney.MOD_ID, "heart_shop_portal"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, DeathShopPortalEntity::new)
                    .dimensions(EntityDimensions.changing(1.45F, 3.4f)).build()
    );
    public static final EntityType<ShopItemEntity> SHOP_ITEM = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(DeathMoney.MOD_ID, "shop_item"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ShopItemEntity::create)
                    .dimensions(EntityDimensions.fixed(0.45f, 0.45f)).build()
    );
    public static final EntityType<MeteorEntity> METEOR = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(DeathMoney.MOD_ID, "meteor"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, MeteorEntity::create)
                    .dimensions(EntityDimensions.changing(2.99F, 2.99F)).build()
    );
    public static final EntityType<LittleWitherBullyEntity> LITTLE_WITHER_BULLY = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(DeathMoney.MOD_ID, "little_wither_bully"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, LittleWitherBullyEntity::new)
                    .dimensions(EntityDimensions.changing(0.35f, 0.6f))
                    .build()
    );
    public static final EntityType<LittleWitherSkullEntity> LITTLE_WITHER_SKULL = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(DeathMoney.MOD_ID, "little_wither_skull"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, LittleWitherSkullEntity::create)
                    .dimensions(EntityDimensions.changing(0.3125f, 0.3125f))
                    .build()
    );
    public static final EntityType<CrazyExGirlfriendEntity> CRAZY_EX_GIRLFRIEND = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(DeathMoney.MOD_ID, "crazy_ex_girlfriend"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, CrazyExGirlfriendEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 1.8f)).build()
    );
    public static final EntityType<DeathMinerEntity> DEATH_MINER = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(DeathMoney.MOD_ID, "death_miner"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, DeathMinerEntity::create)
                    .dimensions(EntityDimensions.fixed(0.01f, 0.01f)).build()
    );
    public static final EntityType<BlockBreakEntity> BLOCK_BREAK = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(DeathMoney.MOD_ID, "block_break"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, BlockBreakEntity::create)
                    .dimensions(EntityDimensions.changing(0.00001f, 0.00001f)).build()
    );
    public static final EntityType<EarthFangsEntity> EARTH_FANGS = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(DeathMoney.MOD_ID, "earth_fangs"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, EarthFangsEntity::create)
                    .dimensions(EntityDimensions.changing(0.5f, 0.8f)).build()
    );
    public static final EntityType<TntNukeEntity> TNT_NUKE = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(DeathMoney.MOD_ID, "tnt_nuke"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, TntNukeEntity::create)
                    .dimensions(EntityDimensions.changing(3.48F, 3.48F)).build()
    );
    public static final EntityType<TntNukeExplosionEntity> TNT_NUKE_EXPLOSION = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(DeathMoney.MOD_ID, "tnt_nuke_explosion"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, TntNukeExplosionEntity::new)
                    .dimensions(EntityDimensions.changing(0.48F, 0.48F)).build()
    );
    public static final EntityType<BlackHoleEntity> BLACK_HOLE = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(DeathMoney.MOD_ID, "black_hole"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, BlackHoleEntity::new)
                    .dimensions(EntityDimensions.changing(0.5F, 0.5F))
                    .build()
    );
    public static final EntityType<BlackMatterShockwaveEntity> BLACK_MATTER_SHOCKWAVE = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(DeathMoney.MOD_ID, "black_matter_shockwave"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, BlackMatterShockwaveEntity::create)
                    .dimensions(EntityDimensions.changing(0.25F, 0.25F)).build()
    );
    public static final EntityType<HungryTitanEntity> HUNGRY_TITAN = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(DeathMoney.MOD_ID, "hungry_titan"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, HungryTitanEntity::new)
                    .dimensions(EntityDimensions.fixed(0.9f * 4f, 2.9f * 3f)).build()
    );
    public static BlockEntityType<BlackMatterBlockEntity> BLACK_MATTER = Registry.register(
            Registry.BLOCK_ENTITY_TYPE, new Identifier(DeathMoney.MOD_ID, "black_matter"),
            FabricBlockEntityTypeBuilder.create(BlackMatterBlockEntity::new, ItemRegistry.BLACK_MATTER)
                    .build()
    );
    public static void register() {
        FabricDefaultAttributeRegistry.register(EntityRegistry.LITTLE_WITHER_BULLY, LittleWitherBullyEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.CRAZY_EX_GIRLFRIEND, CrazyExGirlfriendEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.BLACK_HOLE, BlackHoleEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.HUNGRY_TITAN, HungryTitanEntity.createAttributes());
    }
}
