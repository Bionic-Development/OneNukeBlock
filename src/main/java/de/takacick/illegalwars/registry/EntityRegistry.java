package de.takacick.illegalwars.registry;

import de.takacick.illegalwars.IllegalWars;
import de.takacick.illegalwars.registry.block.entity.PieLauncherBlockEntity;
import de.takacick.illegalwars.registry.entity.projectiles.PieEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class EntityRegistry {

    public static final EntityType<PieEntity> PIE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(IllegalWars.MOD_ID, "pie"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, PieEntity::create)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.5f)).build()
    );
    public static BlockEntityType<PieLauncherBlockEntity> PIE_LAUNCHER = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(IllegalWars.MOD_ID, "pie_launcher"),
            FabricBlockEntityTypeBuilder.create(PieLauncherBlockEntity::new, ItemRegistry.PIE_LAUNCHER)
                    .build()
    );

    public static void register() {

    }
}
