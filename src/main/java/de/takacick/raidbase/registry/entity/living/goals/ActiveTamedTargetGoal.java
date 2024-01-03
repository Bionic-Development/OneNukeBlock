package de.takacick.raidbase.registry.entity.living.goals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.function.Predicate;

public class ActiveTamedTargetGoal<T extends LivingEntity>
        extends TrackTargetGoal {
    protected final Class<T> targetClass;

    protected final int reciprocalChance;
    @Nullable
    protected LivingEntity targetEntity;
    protected TargetPredicate targetPredicate;

    public ActiveTamedTargetGoal(MobEntity mob, Class<T> targetClass, boolean checkVisibility) {
        this(mob, targetClass, 10, checkVisibility, false, null);
    }

    public ActiveTamedTargetGoal(MobEntity mob, Class<T> targetClass, int reciprocalChance, boolean checkVisibility, boolean checkCanNavigate, @Nullable Predicate<LivingEntity> targetPredicate) {
        super(mob, checkVisibility, checkCanNavigate);
        this.targetClass = targetClass;
        this.reciprocalChance = ActiveTamedTargetGoal.toGoalTicks(reciprocalChance);
        this.setControls(EnumSet.of(Control.TARGET));

        if (mob instanceof Tameable tameable) {
            this.targetPredicate = TargetPredicate.createAttackable().setBaseMaxDistance(this.getFollowRange())
                    .setPredicate(targetPredicate != null ? targetPredicate.and(target -> {
                        return !(target instanceof Tameable targetTameable
                                && targetTameable.getOwnerUuid() != null
                                && targetTameable.getOwnerUuid().equals(tameable.getOwnerUuid()))
                                && !target.getUuid().equals(tameable.getOwnerUuid());
                    }) : target -> {
                        return !(target instanceof Tameable targetTameable
                                && targetTameable.getOwnerUuid() != null
                                && targetTameable.getOwnerUuid().equals(tameable.getOwnerUuid()))
                                && !target.getUuid().equals(tameable.getOwnerUuid());
                    });
        } else {
            this.targetPredicate = TargetPredicate.createAttackable().setBaseMaxDistance(this.getFollowRange())
                    .setPredicate(targetPredicate);
        }
    }

    @Override
    public boolean canStart() {
        if (this.reciprocalChance > 0 && this.mob.getRandom().nextInt(this.reciprocalChance) != 0) {
            return false;
        }

        if (!(mob instanceof Tameable tameable) || tameable.getOwnerUuid() == null) {
            return false;
        }


        this.findClosestTarget(tameable.getOwner());
        return this.targetEntity != null;
    }

    protected Box getSearchBox(double distance) {
        return this.mob.getBoundingBox().expand(distance, 4.0, distance);
    }

    protected void findClosestTarget(LivingEntity owner) {
        this.targetEntity = this.targetClass == PlayerEntity.class
                || this.targetClass == ServerPlayerEntity.class ? this.mob.getWorld()
                .getClosestPlayer(this.targetPredicate, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ())
                : this.mob.getWorld()
                .getClosestEntity(this.mob.getWorld().getEntitiesByClass(this.targetClass,
                        this.getSearchBox(this.getFollowRange()), livingEntity -> true), this.targetPredicate, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
    }

    @Override
    public void start() {
        this.mob.setTarget(this.targetEntity);
        super.start();
    }
}

