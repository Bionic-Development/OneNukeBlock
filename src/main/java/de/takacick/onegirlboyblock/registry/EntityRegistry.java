package de.takacick.onegirlboyblock.registry;

import de.takacick.onegirlboyblock.OneGirlBoyBlock;
import de.takacick.onegirlboyblock.registry.entity.living.TurboBoardEntity;
import de.takacick.onegirlboyblock.registry.entity.projectiles.FlameEntity;
import de.takacick.onegirlboyblock.registry.entity.projectiles.TetrisEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class EntityRegistry {

    public static final EntityType<TurboBoardEntity> TURBO_BOARD = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(OneGirlBoyBlock.MOD_ID, "turbo_board"),
            EntityType.Builder.create(TurboBoardEntity::new, SpawnGroup.MISC)
                    .dimensions(1f, 0.2f)
                    .passengerAttachments(new Vec3d(0d, 0.625d, 0d))
                    .build()
    );
    public static final EntityType<TetrisEntity> TETRIS = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(OneGirlBoyBlock.MOD_ID, "tetris"),
            EntityType.Builder.<TetrisEntity>create(TetrisEntity::new, SpawnGroup.MISC)
                    .dimensions(0.35f, 0.35f)
                    .build()
    );
    public static final EntityType<FlameEntity> FLAME = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(OneGirlBoyBlock.MOD_ID, "flame"),
            EntityType.Builder.<FlameEntity>create(FlameEntity::new, SpawnGroup.MISC)
                    .dimensions(0.35f, 0.35f)
                    .build()
    );

    public static void register() {
        FabricDefaultAttributeRegistry.register(EntityRegistry.TURBO_BOARD, TurboBoardEntity.createAttributes());

    }
}
