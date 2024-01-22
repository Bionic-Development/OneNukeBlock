package de.takacick.illegalwars.registry.block.entity.spawner;

import net.minecraft.block.enums.TrialSpawnerState;
import net.minecraft.block.spawner.EntityDetector;
import net.minecraft.block.spawner.TrialSpawnerConfig;
import net.minecraft.block.spawner.TrialSpawnerData;
import net.minecraft.block.spawner.TrialSpawnerLogic;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.List;
import java.util.Optional;

public class TrialSpawnerStateHandler {
    public static TrialSpawnerConfig TRIAL_SPAWNER_CONFIG = new TrialSpawnerConfig(14, 4, 1.0f, 2.0f, 2.0f, 1.0f, 40, 36000,
            new DataPool<>(List.of()), DataPool.of(new Identifier("minecraft", "spawners/trial_chamber/key")));
    private static final int BETWEEN_EJECTING_REWARDS_COOLDOWN = MathHelper.floor(30.0f);
    private static final float START_EJECTING_REWARDS_COOLDOWN = 40.0f;

    public static TrialSpawnerState tick(TrialSpawnerState trialSpawnerState, BlockPos pos, TrialSpawnerLogic logic, ServerWorld world) {
        TrialSpawnerData trialSpawnerData = logic.getData();
        TrialSpawnerConfig trialSpawnerConfig = logic.getConfig();
        EntityDetector entityDetector = logic.getEntityDetector();
        return switch (trialSpawnerState) {
            default -> throw new IncompatibleClassChangeError();
            case INACTIVE -> {
                if (trialSpawnerData.setDisplayEntity(logic, world, TrialSpawnerState.WAITING_FOR_PLAYERS) == null) {
                    yield trialSpawnerState;
                }
                yield TrialSpawnerState.WAITING_FOR_PLAYERS;
            }
            case WAITING_FOR_PLAYERS -> {
                if (!trialSpawnerData.hasSpawnData()) {
                    yield TrialSpawnerState.INACTIVE;
                }
                trialSpawnerData.updatePlayers(world, pos, entityDetector, trialSpawnerConfig.requiredPlayerRange());
                if (trialSpawnerData.players.isEmpty()) {
                    yield trialSpawnerState;
                }
                yield TrialSpawnerState.ACTIVE;
            }
            case ACTIVE -> {
                if (!trialSpawnerData.hasSpawnData()) {
                    yield TrialSpawnerState.INACTIVE;
                }
                int i = trialSpawnerData.getAdditionalPlayers(pos);
                trialSpawnerData.updatePlayers(world, pos, entityDetector, trialSpawnerConfig.requiredPlayerRange());
                if (trialSpawnerData.hasSpawnedAllMobs(trialSpawnerConfig, i)) {
                    if (trialSpawnerData.areMobsDead()) {
                        trialSpawnerData.cooldownEnd = world.getTime() + (long) trialSpawnerConfig.targetCooldownLength();
                        trialSpawnerData.totalSpawnedMobs = 0;
                        trialSpawnerData.nextMobSpawnsAt = 0L;
                        yield TrialSpawnerState.WAITING_FOR_REWARD_EJECTION;
                    }
                } else if (trialSpawnerData.canSpawnMore(world, trialSpawnerConfig, i)) {
                    logic.trySpawnMob(world, pos).ifPresent(uuid -> {
                        trialSpawnerData.spawnedMobsAlive.add(uuid);
                        ++trialSpawnerData.totalSpawnedMobs;
                        trialSpawnerData.nextMobSpawnsAt = world.getTime() + (long) trialSpawnerConfig.ticksBetweenSpawn();
                        trialSpawnerData.spawnDataPool.getOrEmpty(world.getRandom()).ifPresent(spawnData -> {
                            trialSpawnerData.spawnData = Optional.of(spawnData.getData());
                            logic.updateListeners();
                        });
                    });
                }
                yield trialSpawnerState;
            }
            case WAITING_FOR_REWARD_EJECTION -> {
                if (trialSpawnerData.isCooldownPast(world, trialSpawnerConfig, START_EJECTING_REWARDS_COOLDOWN)) {
                    world.playSound(null, pos, SoundEvents.BLOCK_TRIAL_SPAWNER_OPEN_SHUTTER, SoundCategory.BLOCKS);
                    yield TrialSpawnerState.EJECTING_REWARD;
                }
                yield trialSpawnerState;
            }
            case EJECTING_REWARD -> {
                if (!trialSpawnerData.isCooldownAtRepeating(world, trialSpawnerConfig, BETWEEN_EJECTING_REWARDS_COOLDOWN)) {
                    yield trialSpawnerState;
                }
                if (trialSpawnerData.players.isEmpty()) {
                    world.playSound(null, pos, SoundEvents.BLOCK_TRIAL_SPAWNER_CLOSE_SHUTTER, SoundCategory.BLOCKS);
                    trialSpawnerData.rewardLootTable = Optional.empty();
                    yield TrialSpawnerState.COOLDOWN;
                }
                if (trialSpawnerData.rewardLootTable.isEmpty()) {
                    trialSpawnerData.rewardLootTable = trialSpawnerConfig.lootTablesToEject().getDataOrEmpty(world.getRandom());
                }
                trialSpawnerData.rewardLootTable.ifPresent(lootTable -> logic.ejectLootTable(world, pos, lootTable));
                trialSpawnerData.players.remove(trialSpawnerData.players.iterator().next());
                yield trialSpawnerState;
            }
            case COOLDOWN -> {
                if (trialSpawnerData.isCooldownOver(world)) {
                    trialSpawnerData.cooldownEnd = 0L;
                    yield TrialSpawnerState.WAITING_FOR_PLAYERS;
                }
                yield trialSpawnerState;
            }
        };
    }
}

