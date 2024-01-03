package de.takacick.raidbase.registry.entity.living.goals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.entity.passive.PigEntity;

import java.util.EnumSet;

public class TrackOwnerAttackerGoal extends TrackTargetGoal {
    private final Tameable tameable;
    private LivingEntity attacker;
    private int lastAttackedTime;

    public TrackOwnerAttackerGoal(PigEntity pigEntity) {
        super(pigEntity, false);
        this.tameable = (Tameable) pigEntity;
        this.setControls(EnumSet.of(Control.TARGET));
    }

    @Override
    public boolean canStart() {
        if (this.tameable.getOwnerUuid() == null) {
            return false;
        }
        LivingEntity livingEntity = this.tameable.getOwner();
        if (livingEntity == null) {
            return false;
        }
        this.attacker = livingEntity.getAttacker();
        int i = livingEntity.getLastAttackedTime();
        return i != this.lastAttackedTime && this.canTrack(this.attacker, TargetPredicate.DEFAULT);
    }

    @Override
    public void start() {
        this.mob.setTarget(this.attacker);
        LivingEntity livingEntity = this.tameable.getOwner();
        if (livingEntity != null) {
            this.lastAttackedTime = livingEntity.getLastAttackedTime();
        }
        super.start();
    }
}

