package de.takacick.deathmoney.registry.entity.living.brain;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import de.takacick.deathmoney.registry.entity.living.LittleWitherBullyEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.*;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.passive.AllayBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class LittleWitherBullyBrain {

    public static Brain<?> create(LittleWitherBullyEntity littleWitherBullyEntity, Brain<LittleWitherBullyEntity> brain) {
        LittleWitherBullyBrain.addCoreActivities(brain);
        LittleWitherBullyBrain.addIdleActivities(littleWitherBullyEntity, brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.resetPossibleActivities();
        return brain;
    }

    private static void addCoreActivities(Brain<LittleWitherBullyEntity> brain) {
        brain.setTaskList(Activity.CORE, 0, ImmutableList.of(new StayAboveWaterTask(0.8f),
                new WalkTask(2.5f), new LookAroundTask(45, 90),
                new WanderAroundTask()));
    }

    private static void addIdleActivities(LittleWitherBullyEntity littleWitherBullyEntity, Brain<LittleWitherBullyEntity> brain) {
        brain.setTaskList(Activity.IDLE, ImmutableList.of(
                        Pair.of(0, new FollowMobTask(allay -> allay
                                instanceof PlayerEntity playerEntity &&
                                littleWitherBullyEntity.isOwner(playerEntity), 12f)),
                        Pair.of(1, new GoTowardsLookTarget(1.8f, 12))),
                ImmutableSet.of());
    }

    public static void updateActivities(LittleWitherBullyEntity littleWitherBullyEntity) {
        littleWitherBullyEntity.getBrain().resetPossibleActivities(ImmutableList.of(Activity.IDLE));
    }

    public static class FollowMobTask
            extends Task<LittleWitherBullyEntity> {
        private final Predicate<LivingEntity> predicate;
        private final float maxDistanceSquared;
        private Optional<LivingEntity> target = Optional.empty();
        private LittleWitherBullyEntity mob;

        public FollowMobTask(Predicate<LivingEntity> predicate, float maxDistance) {
            super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryModuleState.VALUE_ABSENT, MemoryModuleType.VISIBLE_MOBS, MemoryModuleState.VALUE_PRESENT));
            this.predicate = predicate;
            this.maxDistanceSquared = maxDistance * maxDistance;
        }

        @Override
        protected boolean shouldRun(ServerWorld world, LittleWitherBullyEntity entity) {
            LivingEntity owner = entity.getOwner();

            this.target = owner != null && owner.getWorld().equals(entity.getWorld()) && this.predicate
                    .and(livingEntity2 -> livingEntity2.squaredDistanceTo(entity)
                            <= (double) this.maxDistanceSquared).test(owner) ? Optional.of(owner) : Optional.empty();

            if (target.isEmpty() && owner != null) {
                this.mob = entity;
                tryTeleport(owner);
            }

            return this.target.isPresent();
        }

        @Override
        protected void run(ServerWorld world, LittleWitherBullyEntity entity, long time) {
            entity.getBrain().remember(MemoryModuleType.LOOK_TARGET, new net.minecraft.entity.ai.brain.EntityLookTarget(this.target.get(), true));
            this.target = Optional.empty();
        }

        private void tryTeleport(LivingEntity owner) {
            if (this.mob == null) {
                return;
            }
            BlockPos blockPos = owner.getBlockPos();

            for (int i = 0; i < 10; ++i) {
                int j = this.getRandomInt(-7, 7);
                int k = this.getRandomInt(-1, 3);
                int l = this.getRandomInt(-7, 7);
                boolean bl = this.tryTeleportTo(owner, blockPos.getX() + j, blockPos.getY() + k, blockPos.getZ() + l);
                if (bl) {
                    return;
                }
            }
        }

        private boolean tryTeleportTo(LivingEntity owner, int x, int y, int z) {
            if (Math.abs((double) x - owner.getX()) < 3.0D && Math.abs((double) z - owner.getZ()) < 3.0D) {
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
            BlockPos blockPos = pos.subtract(this.mob.getBlockPos());
            return owner.world.isSpaceEmpty(mob, mob.getBoundingBox().offset(blockPos));
        }

        private int getRandomInt(int min, int max) {
            return this.mob.getRandom().nextInt(max - min + 1) + min;
        }
    }

    public static class GoTowardsLookTarget
            extends Task<LivingEntity> {
        private final Function<LivingEntity, Float> speed;
        private final int completionRange;
        private final Predicate<LivingEntity> predicate;

        public GoTowardsLookTarget(float speed, int completionRange) {
            this((LivingEntity entity) -> true, entity -> Float.valueOf(speed), completionRange);
        }

        public GoTowardsLookTarget(Predicate<LivingEntity> predicate, Function<LivingEntity, Float> speed, int completionRange) {
            super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleState.VALUE_ABSENT, MemoryModuleType.LOOK_TARGET, MemoryModuleState.VALUE_PRESENT));
            this.speed = speed;
            this.completionRange = completionRange;
            this.predicate = predicate;
        }

        @Override
        protected boolean shouldRun(ServerWorld world, LivingEntity entity) {
            return this.predicate.test(entity);
        }

        @Override
        protected void run(ServerWorld world, LivingEntity entity, long time) {
            Brain<?> brain = entity.getBrain();

            LookTarget lookTarget = brain.getOptionalMemory(MemoryModuleType.LOOK_TARGET).get();
            brain.remember(MemoryModuleType.WALK_TARGET, new WalkTarget(lookTarget, this.speed.apply(entity).floatValue(), this.completionRange));
        }
    }

    public static class WalkTarget extends net.minecraft.entity.ai.brain.WalkTarget {

        public WalkTarget(LookTarget lookTarget, float speed, int completionRange) {
            super(lookTarget instanceof net.minecraft.entity.ai.brain.EntityLookTarget target ?
                    new EntityLookTarget(target.getEntity(), true) : lookTarget, speed, completionRange);
        }
    }

    public static class EntityLookTarget
            implements LookTarget {
        private final Entity entity;
        private final boolean useEyeHeight;

        public EntityLookTarget(Entity entity, boolean useEyeHeight) {
            this.entity = entity;
            this.useEyeHeight = useEyeHeight;
        }

        @Override
        public Vec3d getPos() {
            return this.useEyeHeight ?
                    this.entity.getPos().add(0.0, this.entity.getStandingEyeHeight() + 1, 0.0) : this.entity.getPos().add(0, 1, 0);
        }

        @Override
        public BlockPos getBlockPos() {
            return this.entity.getBlockPos().add(0, 2, 0);
        }

        @Override
        public boolean isSeenBy(LivingEntity entity) {
            Entity entity2 = this.entity;
            if (!(entity2 instanceof LivingEntity livingEntity)) {
                return true;
            }
            if (!livingEntity.isAlive()) {
                return false;
            }
            Optional<LivingTargetCache> optional = entity.getBrain().getOptionalMemory(MemoryModuleType.VISIBLE_MOBS);
            return optional.isPresent() && optional.get().contains(livingEntity);
        }

        public Entity getEntity() {
            return this.entity;
        }

        public String toString() {
            return "EntityTracker for " + this.entity;
        }
    }

}
