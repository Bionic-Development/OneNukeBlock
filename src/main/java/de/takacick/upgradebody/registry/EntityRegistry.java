package de.takacick.upgradebody.registry;

import de.takacick.upgradebody.UpgradeBody;
import de.takacick.upgradebody.registry.entity.custom.EmeraldShopPortalEntity;
import de.takacick.upgradebody.registry.entity.custom.ShopItemEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class EntityRegistry {

    public static final EntityType<EmeraldShopPortalEntity> EMERALD_SHOP_PORTAL = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(UpgradeBody.MOD_ID, "emerald_shop_portal"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, EmeraldShopPortalEntity::new)
                    .dimensions(EntityDimensions.changing(2F, 6f)).build()
    );
    public static final EntityType<ShopItemEntity> SHOP_ITEM = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(UpgradeBody.MOD_ID, "shop_item"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ShopItemEntity::create)
                    .dimensions(EntityDimensions.changing(0.45f, 0.45f)).build()
    );

    public static void register() {

    }
}
