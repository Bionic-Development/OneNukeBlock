package de.takacick.secretgirlbase.access;

import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.player.PlayerEntity;

public interface LeadCuffProperties {

    void setLeadCuffed(PlayerEntity playerEntity);

    void leadCuff(PlayerEntity playerEntity);

    PlayerEntity getLeadCuffedOwner();

    PlayerEntity getLeadCuffedTarget();

    boolean isLeadCuffed();

    void trackedDataSet(TrackedData<?> data);

}
