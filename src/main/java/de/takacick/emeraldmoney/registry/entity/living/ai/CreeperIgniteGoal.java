package de.takacick.emeraldmoney.registry.entity.living.ai;

import de.takacick.emeraldmoney.registry.entity.living.CreepagerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class CreeperIgniteGoal
        extends Goal {
    private final CreepagerEntity creeper;
    @Nullable
    private LivingEntity target;

    public CreeperIgniteGoal(CreepagerEntity creeper) {
        this.creeper = creeper;
        this.setControls(EnumSet.of(Goal.Control.MOVE));
    }

    public boolean canStart() {
        LivingEntity livingEntity = this.creeper.getTarget();
        return this.creeper.getFuseSpeed() > 0 || livingEntity != null && this.creeper.squaredDistanceTo(livingEntity) < 9.0;
    }

    public void start() {
        this.creeper.getNavigation().stop();
        this.target = this.creeper.getTarget();
    }

    public void stop() {
        this.target = null;
    }

    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.creeper.getCooldown() > 0) {
            this.creeper.setFuseSpeed(-10);
            return;
        }
        if (this.creeper.isIgnited()) {
            return;
        }
        if (this.target == null) {
            this.creeper.setFuseSpeed(-1);
        } else if (this.creeper.squaredDistanceTo(this.target) > 49.0) {
            this.creeper.setFuseSpeed(-1);
        } else if (!this.creeper.getVisibilityCache().canSee(this.target)) {
            this.creeper.setFuseSpeed(-1);
        } else {
            this.creeper.setFuseSpeed(1);
        }
    }
}

