package de.takacick.onesuperblock.registry;

import de.takacick.onesuperblock.OneSuperBlock;
import de.takacick.onesuperblock.registry.block.entity.SuperBlockEntity;
import de.takacick.onesuperblock.registry.entity.living.SuperFiedWardenEntity;
import de.takacick.onesuperblock.registry.entity.living.SuperProtoEntity;
import de.takacick.onesuperblock.registry.entity.living.SuperVillagerEntity;
import de.takacick.onesuperblock.registry.entity.living.SuperWitherEntity;
import de.takacick.onesuperblock.registry.entity.projectiles.CustomBlockEntity;
import de.takacick.onesuperblock.registry.entity.projectiles.SuperBridgeEggEntity;
import de.takacick.onesuperblock.registry.entity.projectiles.SuperEnderPearlEntity;
import de.takacick.onesuperblock.registry.entity.projectiles.SuperWitherSkullEntity;
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
            new Identifier(OneSuperBlock.MOD_ID, "falling_block"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, CustomBlockEntity::create)
                    .dimensions(EntityDimensions.changing(0.98F, 0.98F))
                    .build()
    );
    public static final EntityType<SuperVillagerEntity> SUPER_VILLAGER = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(OneSuperBlock.MOD_ID, "super_villager"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, SuperVillagerEntity::new)
                    .dimensions(EntityDimensions.changing(0.6f, 1.95f)).build()
    );
    public static final EntityType<SuperProtoEntity> SUPER_PROTO = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(OneSuperBlock.MOD_ID, "super_proto"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, SuperProtoEntity::new)
                    .dimensions(EntityDimensions.changing(0.6f, 0.85f)).build()
    );
    public static final EntityType<SuperEnderPearlEntity> SUPER_ENDER_PEARL = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(OneSuperBlock.MOD_ID, "super_ender_pearl"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, SuperEnderPearlEntity::create)
                    .dimensions(EntityDimensions.changing(0.25f, 0.25f)).build()
    );
    public static final EntityType<SuperFiedWardenEntity> SUPER_FIED_WARDEN = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(OneSuperBlock.MOD_ID, "super_fied_warden"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, SuperFiedWardenEntity::new)
                    .dimensions(EntityDimensions.changing(0.9f, 2.9f)).build()
    );
    public static final EntityType<SuperWitherEntity> SUPER_WITHER = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(OneSuperBlock.MOD_ID, "super_wither"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, SuperWitherEntity::new)
                    .dimensions(EntityDimensions.fixed(0.9f, 3.5f)).build()
    );
    public static final EntityType<SuperBridgeEggEntity> SUPER_BRIDGE_EGG = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(OneSuperBlock.MOD_ID, "super_bridge_egg"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, SuperBridgeEggEntity::create)
                    .dimensions(EntityDimensions.changing(0.25f, 0.25f)).build()
    );
    public static final EntityType<SuperWitherSkullEntity> SUPER_WITHER_SKULL = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(OneSuperBlock.MOD_ID, "super_wither_skull"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, SuperWitherSkullEntity::create)
                    .dimensions(EntityDimensions.changing(0.3125f, 0.3125f)).build()
    );
    public static final BlockEntityType<SuperBlockEntity> SUPER_BLOCK = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            new Identifier(OneSuperBlock.MOD_ID, "super_block"),
            FabricBlockEntityTypeBuilder.create(SuperBlockEntity::new, ItemRegistry.SUPER_BLOCK)
                    .build()
    );

    public static void register() {
        FabricDefaultAttributeRegistry.register(EntityRegistry.SUPER_VILLAGER, SuperVillagerEntity.createVillagerAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.SUPER_PROTO, SuperProtoEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.SUPER_FIED_WARDEN, SuperFiedWardenEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.SUPER_WITHER, SuperWitherEntity.createWitherAttributes());
    }
}
