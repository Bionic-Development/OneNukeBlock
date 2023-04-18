package de.takacick.stealbodyparts.registry;

import de.takacick.stealbodyparts.StealBodyParts;
import de.takacick.stealbodyparts.registry.entity.living.AliveMoldedBossEntity;
import de.takacick.stealbodyparts.registry.entity.living.AliveMoldingBodyEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EntityRegistry {
    public static final EntityType<AliveMoldedBossEntity> ALIVE_MOLDED_BOSS = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(StealBodyParts.MOD_ID, "alive_molded_boss"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, AliveMoldedBossEntity::new)
                    .dimensions(EntityDimensions.fixed(0.9f, 2.9f)).build()
    );
    public static final EntityType<AliveMoldingBodyEntity> ALIVE_MOLDING_BODY = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(StealBodyParts.MOD_ID, "alive_molding_body"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, AliveMoldingBodyEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 1.8f)).build()
    );

    public static void register() {
        FabricDefaultAttributeRegistry.register(EntityRegistry.ALIVE_MOLDED_BOSS, AliveMoldedBossEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.ALIVE_MOLDING_BODY, AliveMoldingBodyEntity.createAttributes());
    }
}
