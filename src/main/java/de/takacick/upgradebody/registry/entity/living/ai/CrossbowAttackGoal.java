package de.takacick.upgradebody.registry.entity.living.ai;

import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.math.intprovider.UniformIntProvider;

import java.util.EnumSet;

public class CrossbowAttackGoal<T extends HostileEntity & CrossbowUser>
        extends Goal {
    public static final UniformIntProvider COOLDOWN_RANGE = TimeHelper.betweenSeconds(1, 2);
    private final T actor;
    private final double speed;
    private final float squaredRange;
    private int seeingTargetTicker;
    private int chargedTicksLeft;
    private int cooldown;

    public CrossbowAttackGoal(T actor, double speed, float range) {
        this.actor = actor;
        this.speed = speed;
        this.squaredRange = range * range;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }

    @Override
    public boolean canStart() {
        return this.hasAliveTarget() && this.isEntityHoldingCrossbow();
    }

    private boolean isEntityHoldingCrossbow() {
        return true;
    }

    @Override
    public boolean shouldContinue() {
        return this.hasAliveTarget() && (this.canStart() || !this.actor.getNavigation().isIdle()) && this.isEntityHoldingCrossbow();
    }

    private boolean hasAliveTarget() {
        return this.actor.getTarget() != null && this.actor.getTarget().isAlive();
    }

    @Override
    public void stop() {
        super.stop();
        this.actor.setAttacking(false);
        this.actor.setTarget(null);
        this.seeingTargetTicker = 0;
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        boolean bl3;
        boolean bl2;
        LivingEntity livingEntity = this.actor.getTarget();
        if (livingEntity == null) {
            return;
        }
        boolean bl = this.actor.getVisibilityCache().canSee(livingEntity);
        boolean bl4 = bl2 = this.seeingTargetTicker > 0;
        if (bl != bl2) {
            this.seeingTargetTicker = 0;
        }
        this.seeingTargetTicker = bl ? ++this.seeingTargetTicker : --this.seeingTargetTicker;

        double d = this.actor.squaredDistanceTo(livingEntity);
        boolean bl5 = bl3 = (d > (double) this.squaredRange || this.seeingTargetTicker < 5);
        if (bl3) {
            this.actor.getNavigation().startMovingTo(livingEntity, this.speed);
        } else {
            this.actor.getNavigation().stop();
        }

        this.actor.getLookControl().lookAt(livingEntity, 30.0f, 30.0f);
        if (bl && !bl3 && this.cooldown <= 0) {
            this.actor.attack(livingEntity, 1.0f);
            this.cooldown = this.actor.getRandom().nextBetween(1, 2);
        } else if (this.cooldown > 0) {
            this.cooldown--;
        }
    }
}

