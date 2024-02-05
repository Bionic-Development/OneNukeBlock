package de.takacick.secretcraftbase.registry;

import de.takacick.secretcraftbase.SecretCraftBase;
import de.takacick.secretcraftbase.registry.block.entity.*;
import de.takacick.secretcraftbase.registry.entity.custom.*;
import de.takacick.secretcraftbase.registry.entity.living.SecretGiantJumpySlimeEntity;
import de.takacick.secretcraftbase.registry.entity.living.SecretPigPoweredPortalEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class EntityRegistry {

    public static final EntityType<IronGolemFarmEntity> IRON_GOLEM_FARM = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(SecretCraftBase.MOD_ID, "iron_golem_farm"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, IronGolemFarmEntity::new)
                    .dimensions(EntityDimensions.changing(0.98F, 0.98F))
                    .build()
    );
    public static final EntityType<TreasuryRoomEntity> TREASURY_ROOM = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(SecretCraftBase.MOD_ID, "treasury_room"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, TreasuryRoomEntity::new)
                    .dimensions(EntityDimensions.changing(0.98F, 0.98F))
                    .build()
    );
    public static final EntityType<ArmoryRoomEntity> ARMORY_ROOM = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(SecretCraftBase.MOD_ID, "armory_room"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ArmoryRoomEntity::new)
                    .dimensions(EntityDimensions.changing(0.98F, 0.98F))
                    .build()
    );
    public static final EntityType<XPFarmEntity> XP_FARM = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(SecretCraftBase.MOD_ID, "xp_farm"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, XPFarmEntity::new)
                    .dimensions(EntityDimensions.changing(0.98F, 0.98F))
                    .build()
    );
    public static final EntityType<BreakingBlockEntity> BREAKING_BLOCK = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(SecretCraftBase.MOD_ID, "breaking_block"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, BreakingBlockEntity::new)
                    .dimensions(EntityDimensions.changing(0.24F, 0.24F))
                    .build()
    );
    public static final EntityType<PlacingBlockEntity> PLACING_BLOCK = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(SecretCraftBase.MOD_ID, "placing_block"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, PlacingBlockEntity::new)
                    .dimensions(EntityDimensions.changing(0.24F, 0.24F))
                    .build()
    );
    public static final EntityType<SecretPigPoweredPortalEntity> SECRET_PIG_POWERED_PORTAL = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(SecretCraftBase.MOD_ID, "secret_pig_powered_portal"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, SecretPigPoweredPortalEntity::new)
                    .dimensions(EntityDimensions.fixed(0.9f, 0.9f))
                    .build()
    );
    public static final EntityType<SecretGiantJumpySlimeEntity> SECRET_GIANT_JUMPY_SLIME = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(SecretCraftBase.MOD_ID, "secret_giant_jumpy_slime"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, SecretGiantJumpySlimeEntity::new)
                    .dimensions(EntityDimensions.fixed(2.84f, 2.84f))
                    .build()
    );
    public static final BlockEntityType<SecretMagicWellBlockEntity> SECRET_MAGIC_WELL = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(SecretCraftBase.MOD_ID, "secret_magic_well"),
            FabricBlockEntityTypeBuilder.create(SecretMagicWellBlockEntity::new,
                            ItemRegistry.SECRET_MAGIC_WELL_COBBLESTONE,
                            ItemRegistry.SECRET_MAGIC_WELL_MOSSY_COBBLESTONE,
                            ItemRegistry.SECRET_MAGIC_WELL_OAK_FENCE,
                            ItemRegistry.SECRET_MAGIC_WELL_TORCH,
                            ItemRegistry.SECRET_MAGIC_WELL_WATER)
                    .build()
    );
    public static BlockEntityType<SecretFakeSunBlockEntity> SECRET_FAKE_SUN = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(SecretCraftBase.MOD_ID, "secret_fake_sun"),
            FabricBlockEntityTypeBuilder.create(SecretFakeSunBlockEntity::new, ItemRegistry.SECRET_FAKE_SUN)
                    .build()
    );
    public static BlockEntityType<BigWhiteBlockEntity> BIG_WHITE_BLOCK = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(SecretCraftBase.MOD_ID, "big_white_block"),
            FabricBlockEntityTypeBuilder.create(BigWhiteBlockEntity::new, ItemRegistry.BIG_WHITE_BLOCK)
                    .build()
    );
    public static BlockEntityType<SecretRedstoneMirrorMelterOreBlockEntity> SECRET_REDSTONE_MIRROR_MELTER_ORE = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(SecretCraftBase.MOD_ID, "secret_redstone_mirror_melter_ore"),
            FabricBlockEntityTypeBuilder.create(SecretRedstoneMirrorMelterOreBlockEntity::new,
                            ItemRegistry.STONE_SECRET_REDSTONE_MIRROR_MELTER,
                            ItemRegistry.DEEPSLATE_SECRET_REDSTONE_MIRROR_MELTER)
                    .build()
    );
    public static final BlockEntityType<MeltingBlockEntity> MELTING_BLOCK = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(SecretCraftBase.MOD_ID, "melting_block"),
            FabricBlockEntityTypeBuilder.create(MeltingBlockEntity::new, ItemRegistry.MELTING_BLOCK)
                    .build()
    );

    public static void register() {
        FabricDefaultAttributeRegistry.register(EntityRegistry.SECRET_PIG_POWERED_PORTAL, SecretPigPoweredPortalEntity.createPigAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.SECRET_GIANT_JUMPY_SLIME, HostileEntity.createHostileAttributes().build());
    }
}
