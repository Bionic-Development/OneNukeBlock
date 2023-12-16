package de.takacick.tinyhouse.registry;

import de.takacick.tinyhouse.TinyHouse;
import de.takacick.tinyhouse.registry.block.entity.AerialChickenCannonBlockEntity;
import de.takacick.tinyhouse.registry.block.entity.SpinningPeepeeChoppaBlockEntity;
import de.takacick.tinyhouse.registry.block.entity.GiantCrusherTrapBlockEntity;
import de.takacick.tinyhouse.registry.entity.living.RatEntity;
import de.takacick.tinyhouse.registry.entity.projectile.ChickenProjectileEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class EntityRegistry {

    public static final EntityType<RatEntity> RAT = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(TinyHouse.MOD_ID, "rat"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, RatEntity::new)
                    .dimensions(EntityDimensions.changing(0.4f, 0.3f))
                    .build()
    );
    public static final EntityType<ChickenProjectileEntity> CHICKEN = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(TinyHouse.MOD_ID, "chicken"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ChickenProjectileEntity::create)
                    .dimensions(EntityDimensions.fixed(0.4f, 0.7f)).build()
    );
    public static BlockEntityType<SpinningPeepeeChoppaBlockEntity> SPINNING_PEEPEE_CHOPPA = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(TinyHouse.MOD_ID, "spinning_peepee_choppa"),
            FabricBlockEntityTypeBuilder.create(SpinningPeepeeChoppaBlockEntity::new, ItemRegistry.SPINNING_PEEPEE_CHOPPA)
                    .build()
    );
    public static BlockEntityType<AerialChickenCannonBlockEntity> AERIAL_CHICKEN_CANNON = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(TinyHouse.MOD_ID, "aerial_chicken_cannon"),
            FabricBlockEntityTypeBuilder.create(AerialChickenCannonBlockEntity::new, ItemRegistry.AERIAL_CHICKEN_CANNON)
                    .build()
    );
    public static BlockEntityType<GiantCrusherTrapBlockEntity> GIANT_CRUSHER_TRAP = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(TinyHouse.MOD_ID, "giant_crusher_trap"),
            FabricBlockEntityTypeBuilder.create(GiantCrusherTrapBlockEntity::new, ItemRegistry.GIANT_CRUSHER_TRAP)
                    .build()
    );
    public static void register() {
        FabricDefaultAttributeRegistry.register(EntityRegistry.RAT, RatEntity.createRatAttributes());
    }
}
