package de.takacick.onegirlfriendblock.registry;

import de.takacick.onegirlfriendblock.OneGirlfriendBlock;
import de.takacick.onegirlfriendblock.registry.entity.living.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class EntityRegistry {

    public static final EntityType<GirlfriendEntity> GIRLFRIEND = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(OneGirlfriendBlock.MOD_ID, "girlfriend"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, GirlfriendEntity::new)
                    .dimensions(EntityDimensions.changing(0.6f, 1.8f))
                    .build()
    );
    public static final EntityType<BuffChadVillagerEntity> BUFF_CHAD_VILLAGER = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(OneGirlfriendBlock.MOD_ID, "buff_chad_villager"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, BuffChadVillagerEntity::new)
                    .dimensions(EntityDimensions.changing(1.4f, 2.7f))
                    .build()
    );
    public static final EntityType<SimpEntity> SIMP = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(OneGirlfriendBlock.MOD_ID, "simp"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, SimpEntity::new)
                    .dimensions(EntityDimensions.changing(0.6f, 1.8f))
                    .build()
    );
    public static final EntityType<ZukoHumanoidEntity> ZUKO_HUMANOID = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(OneGirlfriendBlock.MOD_ID, "zuko_humanoid"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ZukoHumanoidEntity::new)
                    .dimensions(EntityDimensions.changing(0.9f, 2.9f))
                    .build()
    );
    public static final EntityType<ZukoEntity> ZUKO = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(OneGirlfriendBlock.MOD_ID, "zuko"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ZukoEntity::new)
                    .dimensions(EntityDimensions.changing(0.98F, 0.98F))
                    .build()
    );
    public static final EntityType<SimpYoinkEntity> SIMP_YOINK = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(OneGirlfriendBlock.MOD_ID, "simp_yoink"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, SimpYoinkEntity::new)
                    .dimensions(EntityDimensions.changing(0.6f, 1.8f))
                    .build()
    );

    public static void register() {
        FabricDefaultAttributeRegistry.register(EntityRegistry.GIRLFRIEND, GirlfriendEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.BUFF_CHAD_VILLAGER, BuffChadVillagerEntity.createIronGolemAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.SIMP, SimpEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.ZUKO_HUMANOID, ZukoHumanoidEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.ZUKO, ZukoEntity.createCatAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.SIMP_YOINK, SimpYoinkEntity.createAttributes());
    }
}
