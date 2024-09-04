package de.takacick.onenukeblock.registry.entity.living.ai.goal;

import de.takacick.onenukeblock.registry.entity.living.MutatedCreeperEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.EntityLookTarget;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.SingleTickTask;
import net.minecraft.entity.ai.brain.task.TaskTriggerer;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.util.Hand;

public class MeleeAttackTask {
    public static SingleTickTask<MutatedCreeperEntity> create(int cooldown) {
        return TaskTriggerer.task(context -> context.group(context.queryMemoryOptional(MemoryModuleType.LOOK_TARGET), context.queryMemoryValue(MemoryModuleType.ATTACK_TARGET), context.queryMemoryAbsent(MemoryModuleType.ATTACK_COOLING_DOWN), context.queryMemoryValue(MemoryModuleType.VISIBLE_MOBS)).apply(context, (lookTarget, attackTarget, attackCoolingDown, visibleMobs) -> (world, entity, time) -> {
            LivingEntity livingEntity = context.getValue(attackTarget);
            if (!MeleeAttackTask.isHoldingUsableRangedWeapon(entity) && !entity.isIgnited() && entity.isInAttackRange(livingEntity) && context.getValue(visibleMobs).contains(livingEntity)) {
                lookTarget.remember(new EntityLookTarget(livingEntity, true));

                if (entity.getRandom().nextDouble() <= 0.3 && !entity.hasFuseDelay()) {
                    entity.ignite();
                } else {
                    entity.swingHand(Hand.MAIN_HAND);
                    entity.tryAttack(livingEntity);
                }
                attackCoolingDown.remember(true, cooldown);
                return true;
            }
            return false;
        }));
    }

    private static boolean isHoldingUsableRangedWeapon(MobEntity mob) {
        return mob.isHolding(stack -> {
            Item item = stack.getItem();
            return item instanceof RangedWeaponItem && mob.canUseRangedWeapon((RangedWeaponItem) item);
        });
    }
}

