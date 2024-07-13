package de.takacick.onegirlboyblock.utils;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;

public class ArmHelper {

    public static ItemStack getArmStack(LivingEntity livingEntity, Arm arm) {
        if (isArm(livingEntity, Hand.MAIN_HAND, arm)) {
            return livingEntity.getMainHandStack();
        } else if (isArm(livingEntity, Hand.OFF_HAND, arm)) {
            return livingEntity.getOffHandStack();
        }

        return ItemStack.EMPTY;
    }

    public static boolean isHoldingItem(LivingEntity livingEntity, Arm arm, Item item) {
        if (livingEntity.getMainHandStack().isOf(item) && livingEntity.getMainArm().equals(arm)) {
            return true;
        } else if (livingEntity.getOffHandStack().isOf(item) && livingEntity.getMainArm().getOpposite().equals(arm)) {
            return true;
        }

        return false;
    }

    public static boolean isArm(LivingEntity livingEntity, Hand hand, Arm arm) {
        if (livingEntity.getMainArm().equals(arm) && hand.equals(Hand.MAIN_HAND)) {
            return true;
        } else if (livingEntity.getMainArm().getOpposite().equals(arm) && hand.equals(Hand.OFF_HAND)) {
            return true;
        }

        return false;
    }

    public static Arm getArm(LivingEntity livingEntity, Hand hand) {
        if (hand.equals(Hand.MAIN_HAND)) {
            return livingEntity.getMainArm();
        } else {
            return livingEntity.getMainArm().getOpposite();
        }
    }

}
