package de.takacick.secretgirlbase.registry;

import de.takacick.secretgirlbase.SecretGirlBase;
import de.takacick.secretgirlbase.registry.block.entity.MagicDisappearingPlatformBlockEntity;
import de.takacick.secretgirlbase.registry.block.entity.MagicFlowerDoorBlockEntity;
import de.takacick.secretgirlbase.registry.block.entity.BubbleGumLauncherBlockEntity;
import de.takacick.secretgirlbase.registry.entity.custom.FireworkTimeBombEntity;
import de.takacick.secretgirlbase.registry.entity.living.ZukoEntity;
import de.takacick.secretgirlbase.registry.entity.projectiles.BubbleGumEntity;
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

    public static final EntityType<ZukoEntity> ZUKO = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(SecretGirlBase.MOD_ID, "zuko"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ZukoEntity::new)
                    .dimensions(EntityDimensions.changing(0.98F, 0.98F))
                    .build()
    );
    public static final EntityType<BubbleGumEntity> BUBBLE_GUM = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(SecretGirlBase.MOD_ID, "bubble_gum"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, BubbleGumEntity::create)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f)).build()
    );
    public static final EntityType<FireworkTimeBombEntity> FIREWORK_TIME_BOMB = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(SecretGirlBase.MOD_ID, "firework_time_bomb"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, FireworkTimeBombEntity::create)
                    .dimensions(EntityDimensions.fixed(1f, 3f)).build()
    );
    public static final BlockEntityType<MagicFlowerDoorBlockEntity> MAGIC_FLOWER_DOOR = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(SecretGirlBase.MOD_ID, "magic_flower_door"),
            FabricBlockEntityTypeBuilder.create(MagicFlowerDoorBlockEntity::new,
                            ItemRegistry.MAGIC_FLOWER_DOOR_GRASS_BLOCK,
                            ItemRegistry.MAGIC_FLOWER_DOOR_POPPY)
                    .build()
    );
    public static BlockEntityType<MagicDisappearingPlatformBlockEntity> MAGIC_DISAPPEARING_PLATFORM = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(SecretGirlBase.MOD_ID, "magic_disappearing_platform"),
            FabricBlockEntityTypeBuilder.create(MagicDisappearingPlatformBlockEntity::new, ItemRegistry.MAGIC_DISAPPEARING_PLATFORM)
                    .build()
    );
    public static BlockEntityType<BubbleGumLauncherBlockEntity> BUBBLE_GUM_LAUNCHER = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(SecretGirlBase.MOD_ID, "bubble_gum_launcher"),
            FabricBlockEntityTypeBuilder.create(BubbleGumLauncherBlockEntity::new, ItemRegistry.BUBBLE_GUM_LAUNCHER)
                    .build()
    );
    public static void register() {
        FabricDefaultAttributeRegistry.register(EntityRegistry.ZUKO, ZukoEntity.createCatAttributes());

    }
}
