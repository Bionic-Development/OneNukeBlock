package de.takacick.secretcraftbase.registry.entity.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class ArmoryRoomEntity extends AbstractSchematicEntity {

    public ArmoryRoomEntity(EntityType<ArmoryRoomEntity> type, World world) {
        super(type, world);
    }

    public int getColor() {
        return 0x6BF3E3;
    }

    @Override
    public String getSchematic() {
        return "armory_room";
    }
}
