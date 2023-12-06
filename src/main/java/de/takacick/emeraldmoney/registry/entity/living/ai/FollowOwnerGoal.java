package de.takacick.emeraldmoney.registry.entity.living.ai;

import de.takacick.emeraldmoney.registry.entity.living.CreepagerEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

import java.util.EnumSet;

public class FollowOwnerGoal extends Goal {
    private final CreepagerEntity creepagerEntity;
    private final WorldView world;
    private final double speed;
    private final EntityNavigation navigation;
    private final float maxDistance;
    private final float minDistance;
    private final boolean leavesAllowed;
    private Entity owner;
    private int updateCountdownTicks;
    private float oldWaterPathfindingPenalty;

    public FollowOwnerGoal(CreepagerEntity creepagerEntity, double speed, float minDistance, float maxDistance, boolean leavesAllowed) {
        this.creepagerEntity = creepagerEntity;
        this.world = creepagerEntity.getWorld();
        this.speed = speed;
        this.navigation = creepagerEntity.getNavigation();
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.leavesAllowed = leavesAllowed;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        if (!(creepagerEntity.getNavigation() instanceof MobNavigation) && !(creepagerEntity.getNavigation() instanceof BirdNavigation)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
        }
    }

    public boolean canStart() {
        this.owner = creepagerEntity.getOwner();
        if (owner == null) {
            return false;
        } else if (owner.isSpectator()) {
            return false;
        } else if (this.creepagerEntity.squaredDistanceTo(owner) < (double) (this.minDistance * this.minDistance)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean shouldContinue() {
        if (this.navigation.isIdle()) {
            return false;
        } else {
            return !(this.creepagerEntity.squaredDistanceTo(this.owner) <= (double) (this.maxDistance * this.maxDistance));
        }
    }

    public void start() {
        this.updateCountdownTicks = 0;
        this.oldWaterPathfindingPenalty = this.creepagerEntity.getPathfindingPenalty(PathNodeType.WATER);
        this.creepagerEntity.setPathfindingPenalty(PathNodeType.WATER, 0.0F);
    }

    public void stop() {
        this.navigation.stop();
        this.creepagerEntity.setPathfindingPenalty(PathNodeType.WATER, this.oldWaterPathfindingPenalty);
        this.creepagerEntity.getLookControl().lookAt(this.owner, 20.0F, (float) this.creepagerEntity.getMaxLookPitchChange());
    }

    public void tick() {
        this.creepagerEntity.getLookControl().lookAt(this.owner, 10.0f, this.creepagerEntity.getMaxLookPitchChange());
        if (--this.updateCountdownTicks > 0) {
            return;
        }
        this.updateCountdownTicks = this.getTickCount(10);
        if (this.creepagerEntity.isLeashed() || this.creepagerEntity.hasVehicle()) {
            return;
        }

        if (this.owner.getWorld() != this.creepagerEntity.getWorld()) {
            this.creepagerEntity.moveToWorld((ServerWorld) this.owner.getWorld());
            this.tryTeleport();
        }

        if (this.creepagerEntity.squaredDistanceTo(this.owner) >= 144.0) {
            this.tryTeleport();
        } else {
            this.navigation.startMovingTo(this.owner, this.speed);
        }
    }

    private void tryTeleport() {
        BlockPos blockPos = this.owner.getBlockPos();

        for (int i = 0; i < 10; ++i) {
            int j = this.getRandomInt(-3, 3);
            int k = this.getRandomInt(-1, 1);
            int l = this.getRandomInt(-3, 3);
            boolean bl = this.tryTeleportTo(blockPos.getX() + j, blockPos.getY() + k, blockPos.getZ() + l);
            if (bl) {
                return;
            }
        }
    }

    private boolean tryTeleportTo(int x, int y, int z) {
        if (Math.abs((double) x - this.owner.getX()) < 2.0D && Math.abs((double) z - this.owner.getZ()) < 2.0D) {
            return false;
        } else if (!this.canTeleportTo(new BlockPos(x, y, z))) {
            return false;
        } else {
            this.creepagerEntity.refreshPositionAndAngles((double) x + 0.5D, y, (double) z + 0.5D, this.creepagerEntity.getYaw(), this.creepagerEntity.getPitch());
            this.navigation.stop();
            return true;
        }
    }

    private boolean canTeleportTo(BlockPos pos) {
        PathNodeType pathNodeType = LandPathNodeMaker.getLandNodeType(this.world, pos.mutableCopy());
        if (pathNodeType != PathNodeType.WALKABLE) {
            return false;
        } else {
            BlockState blockState = this.world.getBlockState(pos.down());
            if (!this.leavesAllowed && blockState.getBlock() instanceof LeavesBlock) {
                return false;
            } else {
                BlockPos blockPos = pos.subtract(this.creepagerEntity.getBlockPos());
                return this.world.isSpaceEmpty(this.creepagerEntity, this.creepagerEntity.getBoundingBox().offset(blockPos));
            }
        }
    }

    private int getRandomInt(int min, int max) {
        return this.creepagerEntity.getRandom().nextInt(max - min + 1) + min;
    }
}