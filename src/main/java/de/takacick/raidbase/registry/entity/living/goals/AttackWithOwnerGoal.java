package de.takacick.raidbase.registry.entity.living.goals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.entity.passive.PigEntity;

import java.util.EnumSet;

public class AttackWithOwnerGoal extends TrackTargetGoal {
    private final Tameable tameable;
    private LivingEntity attacking;
    private int lastAttackTime;

    public AttackWithOwnerGoal(PigEntity pigEntity) {
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
        this.attacking = livingEntity.getAttacking();
        int i = livingEntity.getLastAttackTime();
        return i != this.lastAttackTime && this.canTrack(this.attacking, TargetPredicate.DEFAULT);
    }

    @Override
    public void start() {
        this.mob.setTarget(this.attacking);
        LivingEntity livingEntity = this.tameable.getOwner();
        if (livingEntity != null) {
            this.lastAttackTime = livingEntity.getLastAttackTime();
        }
        super.start();
    }
}

