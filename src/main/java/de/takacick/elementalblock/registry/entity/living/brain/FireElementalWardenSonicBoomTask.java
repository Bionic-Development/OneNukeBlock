package de.takacick.elementalblock.registry.entity.living.brain;

import com.google.common.collect.ImmutableMap;
import de.takacick.elementalblock.OneElementalBlock;
import de.takacick.elementalblock.registry.entity.living.FireElementalWardenEntity;
import de.takacick.elementalblock.registry.entity.projectile.MagmaEntity;
import de.takacick.utils.BionicUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Unit;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;
import java.util.Optional;

public class FireElementalWardenSonicBoomTask extends MultiTickTask<FireElementalWardenEntity> {

    private static final int SOUND_DELAY = MathHelper.ceil(34.0);
    private static final int RUN_TIME = MathHelper.ceil(60.0F);

    public FireElementalWardenSonicBoomTask() {
        super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_PRESENT, MemoryModuleType.SONIC_BOOM_COOLDOWN, MemoryModuleState.VALUE_ABSENT, MemoryModuleType.SONIC_BOOM_SOUND_COOLDOWN, MemoryModuleState.REGISTERED, MemoryModuleType.SONIC_BOOM_SOUND_DELAY, MemoryModuleState.REGISTERED), RUN_TIME);
    }

    protected boolean shouldRun(ServerWorld serverWorld, FireElementalWardenEntity wardenEntity) {
        return wardenEntity.isInRange(wardenEntity.getBrain().getOptionalRegisteredMemory(MemoryModuleType.ATTACK_TARGET).get(), 15.0, 20.0);
    }

    protected boolean shouldKeepRunning(ServerWorld serverWorld, FireElementalWardenEntity wardenEntity, long l) {
        return true;
    }

    protected void run(ServerWorld serverWorld, FireElementalWardenEntity wardenEntity, long l) {
        wardenEntity.getBrain().remember(MemoryModuleType.ATTACK_COOLING_DOWN, true, RUN_TIME);
        wardenEntity.getBrain().remember(MemoryModuleType.SONIC_BOOM_SOUND_DELAY, Unit.INSTANCE, SOUND_DELAY);
        serverWorld.sendEntityStatus(wardenEntity, (byte) 62);
        wardenEntity.playSound(SoundEvents.ENTITY_WARDEN_SONIC_CHARGE, 3.0F, 1.0F);
    }

    protected void keepRunning(ServerWorld serverWorld, FireElementalWardenEntity wardenEntity, long l) {
        wardenEntity.getBrain().getOptionalRegisteredMemory(MemoryModuleType.ATTACK_TARGET).ifPresent((target) -> {
            wardenEntity.getLookControl().lookAt(target.getPos());
        });
        if (!wardenEntity.getBrain().hasMemoryModule(MemoryModuleType.SONIC_BOOM_SOUND_DELAY) && !wardenEntity.getBrain().hasMemoryModule(MemoryModuleType.SONIC_BOOM_SOUND_COOLDOWN)) {
            wardenEntity.getBrain().remember(MemoryModuleType.SONIC_BOOM_SOUND_COOLDOWN, Unit.INSTANCE, RUN_TIME - SOUND_DELAY);
            Optional<LivingEntity> var10000 = wardenEntity.getBrain().getOptionalRegisteredMemory(MemoryModuleType.ATTACK_TARGET);
            Objects.requireNonNull(wardenEntity);
            var10000.filter(wardenEntity::isValidTarget).filter((target) -> {
                return true;
            }).ifPresent((target) -> {
                Vec3d vec3d = wardenEntity.getPos().add(0.0, 1.600000023841858, 0.0);
                Vec3d vec3d2 = new Vec3d(target.getX(), target.getBodyY(0.35), target.getZ()).subtract(vec3d);
                Vec3d vec3d3 = vec3d2.normalize().multiply(2);

                wardenEntity.playSound(SoundEvents.ENTITY_WARDEN_SONIC_BOOM, 3.0F, 1.0F);
                wardenEntity.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 2.0F, 1.0F);
                BionicUtils.sendEntityStatus((ServerWorld) wardenEntity.getWorld(), wardenEntity, OneElementalBlock.IDENTIFIER, 5);

                MagmaEntity magmaEntity = new MagmaEntity(wardenEntity.getWorld(), wardenEntity.getX(), wardenEntity.getBodyY(0.5), wardenEntity.getZ(), wardenEntity);
                magmaEntity.setOwner(wardenEntity);
                magmaEntity.setVelocity(vec3d3.getX(), vec3d3.getY(), vec3d3.getZ());
                wardenEntity.getWorld().spawnEntity(magmaEntity);
            });
        }
    }

    protected void finishRunning(ServerWorld serverWorld, FireElementalWardenEntity wardenEntity, long l) {
        cooldown(wardenEntity, 40);
    }

    public static void cooldown(LivingEntity warden, int cooldown) {
        warden.getBrain().remember(MemoryModuleType.SONIC_BOOM_COOLDOWN, Unit.INSTANCE, cooldown);
    }
}
