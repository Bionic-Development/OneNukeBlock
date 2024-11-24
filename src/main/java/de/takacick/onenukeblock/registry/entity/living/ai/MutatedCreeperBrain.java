package de.takacick.onenukeblock.registry.entity.living.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import de.takacick.onenukeblock.registry.entity.living.MutatedCreeperEntity;
import de.takacick.onenukeblock.registry.entity.living.ai.goal.MeleeAttackTask;
import de.takacick.onenukeblock.registry.entity.living.ai.goal.WanderAroundTask;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.Unit;
import net.minecraft.util.math.MathHelper;

import java.util.List;
import java.util.Optional;

public class MutatedCreeperBrain {

    public static final int EMERGE_DURATION = MathHelper.ceil(133.59999f);

    private static final List<SensorType<? extends Sensor<? super MutatedCreeperEntity>>> SENSORS = List.of(SensorType.NEAREST_PLAYERS, SensorType.WARDEN_ENTITY_SENSOR);
    private static final List<MemoryModuleType<?>> MEMORY_MODULES = List.of(MemoryModuleType.MOBS, MemoryModuleType.VISIBLE_MOBS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.NEAREST_ATTACKABLE, MemoryModuleType.ROAR_TARGET, MemoryModuleType.DISTURBANCE_LOCATION, MemoryModuleType.RECENT_PROJECTILE, MemoryModuleType.IS_SNIFFING, MemoryModuleType.IS_EMERGING, MemoryModuleType.ROAR_SOUND_DELAY, MemoryModuleType.DIG_COOLDOWN, MemoryModuleType.ROAR_SOUND_COOLDOWN, MemoryModuleType.SNIFF_COOLDOWN, MemoryModuleType.TOUCH_COOLDOWN, MemoryModuleType.VIBRATION_COOLDOWN, MemoryModuleType.SONIC_BOOM_COOLDOWN, MemoryModuleType.SONIC_BOOM_SOUND_COOLDOWN, MemoryModuleType.SONIC_BOOM_SOUND_DELAY);

    public static void updateActivities(MutatedCreeperEntity warden) {
        warden.getBrain().resetPossibleActivities(ImmutableList.of(Activity.EMERGE, Activity.DIG, Activity.ROAR, Activity.FIGHT, Activity.INVESTIGATE, Activity.SNIFF, Activity.IDLE));
    }

    public static Brain<?> create(MutatedCreeperEntity warden) {
        Brain.Profile<MutatedCreeperEntity> profile = Brain.createProfile(MEMORY_MODULES, SENSORS);
        NbtOps nbtOps = NbtOps.INSTANCE;
        Brain<MutatedCreeperEntity> brain = profile.deserialize(new Dynamic<>(nbtOps, nbtOps.createMap(ImmutableMap.of(nbtOps.createString("memories"), nbtOps.emptyMap()))));
        MutatedCreeperBrain.addCoreActivities(brain);
        MutatedCreeperBrain.addEmergeActivities(brain);
        MutatedCreeperBrain.addDigActivities(brain);
        MutatedCreeperBrain.addIdleActivities(brain);
        MutatedCreeperBrain.addRoarActivities(brain);
        MutatedCreeperBrain.addFightActivities(warden, brain);
        MutatedCreeperBrain.addInvestigateActivities(brain);
        MutatedCreeperBrain.addSniffActivities(brain);
        brain.remember(MemoryModuleType.SONIC_BOOM_COOLDOWN, Unit.INSTANCE, 1);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.resetPossibleActivities();

        return brain;
    }

    private static void addCoreActivities(Brain<MutatedCreeperEntity> brain) {
        brain.setTaskList(Activity.CORE, 0, ImmutableList.of(new StayAboveWaterTask(0.8f),
                LookAtDisturbanceTask.create(), new LookAroundTask(45, 90), new WanderAroundTask(),
                FindRoarTargetTask.create(MutatedCreeperBrain::getPreferredTarget)));
    }

    private static void addEmergeActivities(Brain<MutatedCreeperEntity> brain) {
        brain.setTaskList(Activity.EMERGE,
                5,
                ImmutableList.of(new EmergeTask<>(EMERGE_DURATION)),
                MemoryModuleType.IS_EMERGING);
    }

    private static void addDigActivities(Brain<MutatedCreeperEntity> brain) {

    }

    private static void addIdleActivities(Brain<MutatedCreeperEntity> brain) {
        brain.setTaskList(Activity.IDLE, 10, ImmutableList.of(
                FindRoarTargetTask.create(MutatedCreeperBrain::getPreferredTarget),
                StartSniffingTask.create(),
                new RandomTask<>(ImmutableMap.of(MemoryModuleType.IS_SNIFFING, MemoryModuleState.VALUE_ABSENT),
                        ImmutableList.of(Pair.of(StrollTask.create(0.5f), 2),
                                Pair.of(new WaitTask(30, 60), 1)))));
    }

    private static void addInvestigateActivities(Brain<MutatedCreeperEntity> brain) {
        brain.setTaskList(Activity.INVESTIGATE, 5, ImmutableList.of(FindRoarTargetTask.create(MutatedCreeperBrain::getPreferredTarget),
                WalkTowardsPosTask.create(MemoryModuleType.DISTURBANCE_LOCATION, 2, 0.7f)), MemoryModuleType.DISTURBANCE_LOCATION);
    }

    private static void addSniffActivities(Brain<MutatedCreeperEntity> brain) {
        brain.setTaskList(Activity.SNIFF, 5, ImmutableList.of());
    }

    private static void addRoarActivities(Brain<MutatedCreeperEntity> brain) {
        brain.setTaskList(Activity.ROAR, 10, ImmutableList.of(new RoarTask()), MemoryModuleType.ROAR_TARGET);
    }

    private static void addFightActivities(MutatedCreeperEntity warden, Brain<MutatedCreeperEntity> brain) {
        brain.setTaskList(Activity.FIGHT, 10, ImmutableList.of(
                ForgetAttackTargetTask.create(entity -> !warden.isValidTarget(entity),
                        MutatedCreeperBrain::removeDeadSuspect, false),
                LookAtMobTask.create((LivingEntity entity) -> MutatedCreeperBrain.isTargeting(warden, entity),
                        (float) warden.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE)),
                RangedApproachTask.create(1.2f),
                MeleeAttackTask.create(50)), MemoryModuleType.ATTACK_TARGET);
    }

    private static boolean isTargeting(MutatedCreeperEntity warden, LivingEntity entity2) {
        return warden.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).filter(entity -> entity == entity2).isPresent();
    }

    private static void removeDeadSuspect(MutatedCreeperEntity warden, LivingEntity suspect) {
        if (!warden.isValidTarget(suspect)) {
            warden.removeSuspect(suspect);
        }
        MutatedCreeperBrain.resetDigCooldown(warden);
    }

    public static void resetDigCooldown(LivingEntity warden) {
        if (warden.getBrain().hasMemoryModule(MemoryModuleType.DIG_COOLDOWN)) {
            warden.getBrain().remember(MemoryModuleType.DIG_COOLDOWN, Unit.INSTANCE, 1200L);
        }
    }

    private static Optional<? extends LivingEntity> getPreferredTarget(WardenEntity wardenEntity) {
        Optional<PlayerEntity> optional2;
        Brain<WardenEntity> brain = wardenEntity.getBrain();

        if ((optional2 = brain.getOptionalMemory(MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER)).isPresent()) {
            return optional2;
        }

        return Optional.empty();
    }
}

