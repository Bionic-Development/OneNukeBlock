package de.takacick.secretcraftbase.registry.entity.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class IronGolemFarmEntity extends AbstractSchematicEntity {

    public IronGolemFarmEntity(EntityType<IronGolemFarmEntity> type, World world) {
        super(type, world);
    }

    public int getColor() {
        return 0xD6D6D6;
    }

    @Override
    public String getSchematic() {
        return "iron_golem_farm";
    }
}
