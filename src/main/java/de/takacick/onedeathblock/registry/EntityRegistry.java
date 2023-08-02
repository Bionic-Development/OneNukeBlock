package de.takacick.onedeathblock.registry;

import de.takacick.onedeathblock.OneDeathBlock;
import de.takacick.onedeathblock.registry.block.entity.SpikedBedBlockEntity;
import de.takacick.onedeathblock.registry.entity.living.SkullagerEntity;
import de.takacick.onedeathblock.registry.entity.living.SuperbrineEntity;
import de.takacick.onedeathblock.registry.entity.projectiles.BuildMeteorEntity;
import de.takacick.onedeathblock.registry.entity.projectiles.CustomBlockEntity;
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
            new Identifier(OneDeathBlock.MOD_ID, "falling_block"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, CustomBlockEntity::create)
                    .dimensions(EntityDimensions.changing(0.98F, 0.98F))
                    .build()
    );
    public static final EntityType<SkullagerEntity> SKULLAGER = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(OneDeathBlock.MOD_ID, "skullager"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, SkullagerEntity::new)
                    .dimensions(EntityDimensions.changing(0.6f, 1.95f)).build()
    );
    public static final EntityType<SuperbrineEntity> SUPERBRINE = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(OneDeathBlock.MOD_ID, "superbrine"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, SuperbrineEntity::new)
                    .dimensions(EntityDimensions.changing(0.6f, 1.8f)).build()
    );
    public static final EntityType<BuildMeteorEntity> BUILD_METEOR = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(OneDeathBlock.MOD_ID, "build_meteor"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, BuildMeteorEntity::create)
                    .dimensions(EntityDimensions.changing(7f, 7f)).build()
    );
    public static final BlockEntityType<SpikedBedBlockEntity> SPIKED_BED = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            new Identifier(OneDeathBlock.MOD_ID, "spiked_bed"),
            FabricBlockEntityTypeBuilder.create(SpikedBedBlockEntity::new, ItemRegistry.RED_SPIKED_BED, ItemRegistry.BLACK_SPIKED_BED, ItemRegistry.BLUE_SPIKED_BED, ItemRegistry.BROWN_SPIKED_BED, ItemRegistry.CYAN_SPIKED_BED, ItemRegistry.GRAY_SPIKED_BED, ItemRegistry.GREEN_SPIKED_BED, ItemRegistry.LIGHT_BLUE_SPIKED_BED, ItemRegistry.LIGHT_GRAY_SPIKED_BED, ItemRegistry.LIME_SPIKED_BED, ItemRegistry.MAGENTA_SPIKED_BED, ItemRegistry.ORANGE_SPIKED_BED, ItemRegistry.PINK_SPIKED_BED, ItemRegistry.PURPLE_SPIKED_BED, ItemRegistry.WHITE_SPIKED_BED, ItemRegistry.YELLOW_SPIKED_BED)
                    .build()
    );

    public static void register() {
        FabricDefaultAttributeRegistry.register(EntityRegistry.SKULLAGER, SkullagerEntity.createPillagerAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.SUPERBRINE, SuperbrineEntity.createAttributes());
    }
}
