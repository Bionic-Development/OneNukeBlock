package de.takacick.secretcraftbase.registry.entity.custom;

import de.takacick.secretcraftbase.SecretCraftBase;
import de.takacick.secretcraftbase.SecretCraftBaseClient;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class XPFarmEntity extends AbstractSchematicEntity {

    public XPFarmEntity(EntityType<XPFarmEntity> type, World world) {
        super(type, world);
    }

    public int getColor() {
        if (getWorld().isClient) {
            return SecretCraftBaseClient.getColor(getWorld().getTime());
        }
        return SecretCraftBase.getColor(getWorld().getTime());
    }

    @Override
    public String getSchematic() {
        return "xp_farm";
    }
}
