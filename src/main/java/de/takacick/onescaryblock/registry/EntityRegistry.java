package de.takacick.onescaryblock.registry;

import de.takacick.onescaryblock.OneScaryBlock;
import de.takacick.onescaryblock.registry.block.entity.Item303BlockEntity;
import de.takacick.onescaryblock.registry.block.entity.PhantomBlockEntity;
import de.takacick.onescaryblock.registry.block.entity.ScaryOneBlockBlockEntity;
import de.takacick.onescaryblock.registry.entity.custom.HerobrineLightningEffectEntity;
import de.takacick.onescaryblock.registry.entity.custom.HerobrineLightningEntity;
import de.takacick.onescaryblock.registry.entity.custom.ScaryOneBlockEntity;
import de.takacick.onescaryblock.registry.entity.living.BloodManEntity;
import de.takacick.onescaryblock.registry.entity.living.Entity303Entity;
import de.takacick.onescaryblock.registry.entity.living.HerobrineEntity;
import de.takacick.onescaryblock.registry.entity.projectile.HerobrineLightningProjectileEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class EntityRegistry {

    public static final EntityType<ScaryOneBlockEntity> SCARY_ONE_BLOCK = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(OneScaryBlock.MOD_ID, "scary_one_block"),
            EntityType.Builder.<ScaryOneBlockEntity>create(ScaryOneBlockEntity::new, SpawnGroup.MISC)
                    .setDimensions(0.98F, 0.98F)
                    .build()
    );
    public static final EntityType<HerobrineEntity> HEROBRINE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(OneScaryBlock.MOD_ID, "herobrine"),
            EntityType.Builder.create(HerobrineEntity::new, SpawnGroup.MISC)
                    .setDimensions(0.6f, 1.8f)
                    .build()
    );
    public static final EntityType<BloodManEntity> BLOOD_MAN = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(OneScaryBlock.MOD_ID, "blood_man"),
            EntityType.Builder.create(BloodManEntity::new, SpawnGroup.MISC)
                    .setDimensions(0.6f, 1.8f)
                    .build()
    );
    public static final EntityType<Entity303Entity> ENTITY_303 = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(OneScaryBlock.MOD_ID, "entity_303"),
            EntityType.Builder.create(Entity303Entity::new, SpawnGroup.MISC)
                    .setDimensions(0.6f, 1.8f)
                    .build()
    );
    public static final EntityType<HerobrineLightningEntity> HEROBRINE_LIGHTNING_BOLT = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(OneScaryBlock.MOD_ID, "herobrine_lightning_bolt"),
            EntityType.Builder.create(HerobrineLightningEntity::new, SpawnGroup.MISC)
                    .disableSaving()
                    .setDimensions(0.0f, 0.0f)
                    .maxTrackingRange(16)
                    .trackingTickInterval(Integer.MAX_VALUE)
                    .build()
    );
    public static final EntityType<HerobrineLightningProjectileEntity> HEROBRINE_LIGHTNING_PROJECTILE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(OneScaryBlock.MOD_ID, "herobrine_lightning_projectile"),
            EntityType.Builder.<HerobrineLightningProjectileEntity>create(HerobrineLightningProjectileEntity::new, SpawnGroup.MISC)
                    .setDimensions(0.25f, 0.25f)
                    .build()
    );
    public static final EntityType<HerobrineLightningEffectEntity> HEROBRINE_LIGHTNING_EFFECT = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(OneScaryBlock.MOD_ID, "herobrine_lightning_effect"),
            EntityType.Builder.<HerobrineLightningEffectEntity>create(HerobrineLightningEffectEntity::new, SpawnGroup.MISC)
                    .setDimensions(0.00001f, 0.00001f)
                    .build()
    );
    public static BlockEntityType<ScaryOneBlockBlockEntity> SCARY_ONE_BLOCK_BLOCK_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(OneScaryBlock.MOD_ID, "scary_one_block"),
            BlockEntityType.Builder.create(ScaryOneBlockBlockEntity::new, ItemRegistry.SCARY_ONE_BLOCK)
                    .build()
    );
    public static BlockEntityType<PhantomBlockEntity> PHANTOM_BLOCK = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(OneScaryBlock.MOD_ID, "phantom_block"),
            BlockEntityType.Builder.create(PhantomBlockEntity::new, ItemRegistry.PHANTOM_BLOCK)
                    .build()
    );
    public static BlockEntityType<Item303BlockEntity> ITEM_303 = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(OneScaryBlock.MOD_ID, "item_303"),
            BlockEntityType.Builder.create(Item303BlockEntity::new, ItemRegistry.ITEM_303_BLOCK)
                    .build()
    );

    public static void register() {
        FabricDefaultAttributeRegistry.register(EntityRegistry.HEROBRINE, HerobrineEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.BLOOD_MAN, BloodManEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.ENTITY_303, Entity303Entity.createAttributes());

    }
}
