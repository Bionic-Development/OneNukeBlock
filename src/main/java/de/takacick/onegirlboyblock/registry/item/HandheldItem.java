package de.takacick.onegirlboyblock.registry.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;

public interface HandheldItem {

    boolean showRightArm(LivingEntity livingEntity, ItemStack stack, Arm itemArm);

    boolean showLeftArm(LivingEntity livingEntity, ItemStack stack, Arm itemArm);

    default void tickInventory(LivingEntity entity, ItemStack itemStack) {}

    default boolean keepFirstPersonPitch(LivingEntity livingEntity, ItemStack stack, Arm itemArm) {
        return false;
    }

    default boolean allowHandTransformation(LivingEntity livingEntity, ItemStack stack, Arm itemArm) {
        return true;
    }

    default boolean shouldRender(LivingEntity livingEntity, ItemStack stack, Arm itemArm) {
        return true;
    }

    public default boolean allowVanillaUsageAnimation() {
        return true;
    }
}
