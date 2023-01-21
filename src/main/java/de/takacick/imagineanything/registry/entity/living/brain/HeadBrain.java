package de.takacick.imagineanything.registry.entity.living.brain;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import de.takacick.imagineanything.registry.entity.living.HeadEntity;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.ai.brain.*;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.passive.AllayBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public class HeadBrain {

    public static Brain<?> create(HeadEntity allayHealerEntity, Brain<HeadEntity> brain) {
        HeadBrain.addCoreActivities(brain);
        HeadBrain.addIdleActivities(allayHealerEntity, brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.resetPossibleActivities();
        return brain;
    }

    private static void addCoreActivities(Brain<HeadEntity> brain) {
        brain.setTaskList(Activity.CORE, 0,
                ImmutableList.of(new StayAboveWaterTask(0.8f),
                        new WalkTask(2.5f), new LookAroundTask(45, 90),
                        new WanderAroundTask()));
    }

    private static void addIdleActivities(HeadEntity headEntity, Brain<HeadEntity> brain) {
        brain.setTaskList(Activity.IDLE, ImmutableList.of(
                        Pair.of(0, new FollowMobTask(allay -> allay
                                instanceof PlayerEntity playerEntity &&
                                playerEntity.equals(headEntity.getOwner()), 6.0f)),
                        Pair.of(1, new GoTowardsLookTarget(1.0f, 3))),
                ImmutableSet.of());
    }

    public static void updateActivities(HeadEntity allay) {
        allay.getBrain().resetPossibleActivities(ImmutableList.of(Activity.IDLE));
    }

    public static void rememberNoteBlock(LivingEntity allay, BlockPos pos) {
        Brain<?> brain = allay.getBrain();
        GlobalPos globalPos = GlobalPos.create(allay.getWorld().getRegistryKey(), pos);
        Optional<GlobalPos> optional = brain.getOptionalMemory(MemoryModuleType.LIKED_NOTEBLOCK);
        if (optional.isEmpty()) {
            brain.remember(MemoryModuleType.LIKED_NOTEBLOCK, globalPos);
            brain.remember(MemoryModuleType.LIKED_NOTEBLOCK_COOLDOWN_TICKS, 600);
        } else if (optional.get().equals(globalPos)) {
            brain.remember(MemoryModuleType.LIKED_NOTEBLOCK_COOLDOWN_TICKS, 600);
        }
    }

    private static Optional<LookTarget> getLookTarget(LivingEntity allay) {
        Brain<?> brain = allay.getBrain();
        Optional<GlobalPos> optional = brain.getOptionalMemory(MemoryModuleType.LIKED_NOTEBLOCK);
        if (optional.isPresent()) {
            GlobalPos globalPos = optional.get();
            if (HeadBrain.shouldGoTowardsNoteBlock(allay, brain, globalPos)) {
                return Optional.of(new BlockPosLookTarget(globalPos.getPos().up()));
            }
            brain.forget(MemoryModuleType.LIKED_NOTEBLOCK);
        }
        return HeadBrain.getLikedLookTarget(allay);
    }

    private static boolean shouldGoTowardsNoteBlock(LivingEntity allay, Brain<?> brain, GlobalPos pos) {
        Optional<Integer> optional = brain.getOptionalMemory(MemoryModuleType.LIKED_NOTEBLOCK_COOLDOWN_TICKS);
        World world = allay.getWorld();
        return world.getRegistryKey() == pos.getDimension() && world.getBlockState(pos.getPos()).isOf(Blocks.NOTE_BLOCK) && optional.isPresent();
    }

    private static Optional<LookTarget> getLikedLookTarget(LivingEntity allay) {
        return HeadBrain.getLikedPlayer(allay).map(player -> new EntityLookTarget((Entity) player, true));
    }

    public static Optional<ServerPlayerEntity> getLikedPlayer(LivingEntity allay) {
        World world = allay.getWorld();
        if (!world.isClient() && world instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) world;
            Optional<UUID> optional = allay.getBrain().getOptionalMemory(MemoryModuleType.LIKED_PLAYER);
            if (optional.isPresent()) {
                Entity entity = serverWorld.getEntity(optional.get());
                if (entity instanceof ServerPlayerEntity) {
                    ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) entity;
                    if ((serverPlayerEntity.interactionManager.isSurvivalLike() || serverPlayerEntity.interactionManager.isCreative()) && serverPlayerEntity.isInRange(allay, 64.0)) {
                        return Optional.of(serverPlayerEntity);
                    }
                }
                return Optional.empty();
            }
        }
        return Optional.empty();
    }


    public static class FollowMobTask
            extends Task<HeadEntity> {
        private final Predicate<LivingEntity> predicate;
        private final float maxDistanceSquared;
        private Optional<LivingEntity> target = Optional.empty();

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
        protected boolean shouldRun(ServerWorld world, HeadEntity entity) {
            LivingTargetCache livingTargetCache = entity.getBrain().getOptionalMemory(MemoryModuleType.VISIBLE_MOBS).get();
            this.target = livingTargetCache.findFirst(this.predicate.and(livingEntity2 -> livingEntity2.squaredDistanceTo(entity) <= (double) this.maxDistanceSquared));
            entity.setTarget(target.orElse(null));
            return this.target.isPresent();
        }

        @Override
        protected void run(ServerWorld world, HeadEntity entity, long time) {
            entity.getBrain().remember(MemoryModuleType.LOOK_TARGET, new EntityLookTarget(this.target.get(), true));
            this.target = Optional.empty();
        }
    }


}

