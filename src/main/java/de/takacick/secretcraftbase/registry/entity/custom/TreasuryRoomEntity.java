package de.takacick.secretcraftbase.registry.entity.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class TreasuryRoomEntity extends AbstractSchematicEntity {

    public TreasuryRoomEntity(EntityType<TreasuryRoomEntity> type, World world) {
        super(type, world);
    }

    public int getColor() {
        return 0xFEE048;
    }

    @Override
    public String getSchematic() {
        return "treasury_room";
    }
}
