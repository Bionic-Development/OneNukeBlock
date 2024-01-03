package de.takacick.raidbase.registry.entity.living.goals;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.ai.brain.EntityLookTarget;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.ai.pathing.LandPathNodeMaker;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.function.Predicate;

public class FollowMobTask extends MultiTickTask<MobEntity> {
    private final Predicate<LivingEntity> predicate;
    private final float maxDistanceSquared;
    private Optional<LivingEntity> target = Optional.empty();
    private MobEntity mob;

    public FollowMobTask(TagKey<EntityType<?>> entityType, float maxDistance) {
        this((LivingEntity entity) -> entity.getType().isIn(entityType), maxDistance);
    }

    public FollowMobTask(SpawnGroup group, float maxDistance) {
        this((LivingEntity entity) -> group.equals(entity.getType().getSpawnGroup()), maxDistance);
    }

    public FollowMobTask(EntityType<?> entityType, float maxDistance) {
        this((LivingEntity entity) -> entityType.equals(entity.getType()), maxDistance);
    }

    public FollowMobTask(float maxDistance) {
        this((LivingEntity entity) -> true, maxDistance);
    }

    public FollowMobTask(Predicate<LivingEntity> predicate, float maxDistance) {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryModuleState.VALUE_ABSENT, MemoryModuleType.VISIBLE_MOBS, MemoryModuleState.VALUE_PRESENT));
        this.predicate = predicate;
        this.maxDistanceSquared = maxDistance * maxDistance;
    }

    @Override
    protected boolean shouldRun(ServerWorld world, MobEntity entity) {
        LivingEntity owner = null;

        if (entity instanceof Tameable tameable) {
            owner = tameable.getOwner();
        }

        this.target = owner != null && owner.getWorld().equals(entity.getWorld()) && this.predicate.and(livingEntity2 -> livingEntity2.squaredDistanceTo(entity)
                <= (double) this.maxDistanceSquared).test(owner) ? Optional.of(owner) : Optional.empty();

        if (target.isEmpty() && owner != null) {
            this.mob = entity;
            tryTeleport(owner);
        }

        return this.target.isPresent();
    }

    @Override
    protected void run(ServerWorld world, MobEntity entity, long time) {
        entity.getBrain().remember(MemoryModuleType.LOOK_TARGET, new EntityLookTarget(this.target.get(), true));
        this.target = Optional.empty();
    }

    private void tryTeleport(LivingEntity owner) {
        if (this.mob == null) {
            return;
        }
        BlockPos blockPos = owner.getBlockPos();

        for (int i = 0; i < 10; ++i) {
            int j = this.getRandomInt(-3, 3);
            int k = this.getRandomInt(-1, 1);
            int l = this.getRandomInt(-3, 3);
            boolean bl = this.tryTeleportTo(owner, blockPos.getX() + j, blockPos.getY() + k, blockPos.getZ() + l);
            if (bl) {
                return;
            }
        }
    }

    private boolean tryTeleportTo(LivingEntity owner, int x, int y, int z) {
        if (Math.abs((double) x - owner.getX()) < 2.0D && Math.abs((double) z - owner.getZ()) < 2.0D) {
            return false;
        } else if (!this.canTeleportTo(owner, new BlockPos(x, y, z))) {
            return false;
        } else {
            if (!owner.getWorld().isClient) {
                this.mob.moveToWorld((ServerWorld) owner.getWorld());
            }
            this.mob.refreshPositionAndAngles((double) x + 0.5D, y, (double) z + 0.5D, this.mob.getYaw(), this.mob.getPitch());
            this.mob.getNavigation().stop();
            return true;
        }
    }

    private boolean canTeleportTo(LivingEntity owner, BlockPos pos) {
        World world = owner.getWorld();
        PathNodeType pathNodeType = LandPathNodeMaker.getLandNodeType(world, pos.mutableCopy());
        if (pathNodeType != PathNodeType.WALKABLE) {
            return false;
        }

        BlockPos blockPos = pos.subtract(this.mob.getBlockPos());
        return world.isSpaceEmpty(this.mob, this.mob.getBoundingBox().offset(blockPos));
    }

    private int getRandomInt(int min, int max) {
        return this.mob.getRandom().nextInt(max - min + 1) + min;
    }
}