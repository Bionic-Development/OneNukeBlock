package de.takacick.raidbase.access;

import net.minecraft.entity.AnimationState;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public interface PigProperties {

    void setPigSoldier(boolean pigSoldier);

    boolean isPigSoldier();

    AnimationState getStandUpState();

    void setSoldierOwner(@Nullable LivingEntity livingEntity);

}