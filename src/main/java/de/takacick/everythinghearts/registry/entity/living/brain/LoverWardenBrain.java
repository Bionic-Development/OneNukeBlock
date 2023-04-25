package de.takacick.everythinghearts.registry.entity.living.brain;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import de.takacick.everythinghearts.registry.entity.living.LoverWardenEntity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.nbt.NbtOps;

import java.util.List;

public class LoverWardenBrain {

    private static final List<SensorType<? extends Sensor<? super LoverWardenEntity>>> SENSORS = List.of(SensorType.NEAREST_PLAYERS, SensorType.WARDEN_ENTITY_SENSOR);
    private static final List<MemoryModuleType<?>> MEMORY_MODULES = List.of(MemoryModuleType.MOBS, MemoryModuleType.VISIBLE_MOBS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.NEAREST_ATTACKABLE, MemoryModuleType.ROAR_TARGET, MemoryModuleType.DISTURBANCE_LOCATION, MemoryModuleType.RECENT_PROJECTILE, MemoryModuleType.IS_SNIFFING, MemoryModuleType.IS_EMERGING, MemoryModuleType.ROAR_SOUND_DELAY, MemoryModuleType.DIG_COOLDOWN, MemoryModuleType.ROAR_SOUND_COOLDOWN, MemoryModuleType.SNIFF_COOLDOWN, MemoryModuleType.TOUCH_COOLDOWN, MemoryModuleType.VIBRATION_COOLDOWN, MemoryModuleType.SONIC_BOOM_COOLDOWN, MemoryModuleType.SONIC_BOOM_SOUND_COOLDOWN, MemoryModuleType.SONIC_BOOM_SOUND_DELAY);

    public static void updateActivities(LoverWardenEntity warden) {
        warden.getBrain().resetPossibleActivities(ImmutableList.of(Activity.EMERGE, Activity.DIG, Activity.ROAR, Activity.FIGHT, Activity.INVESTIGATE, Activity.SNIFF, Activity.IDLE));
    }

    public static Brain<?> create(LoverWardenEntity warden) {
        Brain.Profile<LoverWardenEntity> profile = Brain.createProfile(MEMORY_MODULES, SENSORS);
        NbtOps nbtOps = NbtOps.INSTANCE;
        Brain<LoverWardenEntity> brain = profile.deserialize(new Dynamic<>(nbtOps, nbtOps.createMap(ImmutableMap.of(nbtOps.createString("memories"), nbtOps.emptyMap()))));
        LoverWardenBrain.addCoreActivities(brain);
        LoverWardenBrain.addIdleActivities(brain);

        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.resetPossibleActivities();

        return brain;
    }

    private static void addCoreActivities(Brain<LoverWardenEntity> brain) {
        brain.setTaskList(Activity.CORE, 0, ImmutableList.of(new WanderAroundTask(), new StayAboveWaterTask(0.8f),
                new LookAtDisturbanceTask(), new LookAroundTask(45, 90)));
    }

    private static void addIdleActivities(Brain<LoverWardenEntity> brain) {
        brain.setTaskList(Activity.IDLE, 10, ImmutableList.of(
                new StartSniffingTask(),
                new RandomTask<>(ImmutableMap.of(MemoryModuleType.IS_SNIFFING, MemoryModuleState.VALUE_ABSENT),
                        ImmutableList.of(Pair.of(new StrollTask(0.5f), 2),
                                Pair.of(new WaitTask(30, 60), 1)))));
    }
}

