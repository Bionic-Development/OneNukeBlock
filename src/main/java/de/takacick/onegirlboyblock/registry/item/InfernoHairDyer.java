package de.takacick.onegirlboyblock.registry.item;

import de.takacick.onegirlboyblock.registry.entity.projectiles.FlameEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class InfernoHairDyer extends Item implements HandheldItem {

    public InfernoHairDyer(Settings settings) {
        super(settings);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {

        if (remainingUseTicks % 2 == 0) {
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        }
        for (int x = -1; x < 2; x++) {
            Vec3d vec3d = user.getRotationVector(user.getPitch(), user.getYaw() + 5 * x).multiply(1.5);

            for (int i = 0; i < 12; i++) {
                Vec3d velocity = new Vec3d(world.getRandom().nextGaussian() * 0.2, world.getRandom().nextGaussian() * 0.2, world.getRandom().nextGaussian() * 0.2);
                world.addImportantParticle(ParticleTypes.FLAME, true,
                        user.getX() + vec3d.getX() * 0.4 + velocity.getX(),
                        user.getBodyY(0.78f) + vec3d.getY() * 0.4 + velocity.getY(),
                        user.getZ() + vec3d.getZ() * 0.4 + velocity.getZ(),
                        vec3d.getX() + velocity.getX(),
                        vec3d.getY() + velocity.getY(),
                        vec3d.getZ() + velocity.getZ());
            }
        }
        if (!world.isClient) {
            for (int x = -1; x < 2; x++) {
                FlameEntity flameEntity = new FlameEntity(world, user.getX(), user.getBodyY(0.78f), user.getZ(), user);
                flameEntity.setVelocity(user, user.getPitch(), user.getYaw() + 5 * x, 0.0f, 1.5f, 1.0f);
                flameEntity.setOwner(user);
                world.spawnEntity(flameEntity);
            }

            if(remainingUseTicks % 2 == 0) {
                stack.damage(1, user, user.getActiveHand().equals(Hand.MAIN_HAND) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            }
        }
        super.usageTick(world, user, stack, remainingUseTicks);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal("Giving §cbad hair days §ra").withColor(0xFFCCFF));
        tooltip.add(Text.literal("whole §enew meaning§r.").withColor(0xFFCCFF));

        super.appendTooltip(stack, context, tooltip, type);
    }

    @Override
    public boolean showRightArm(LivingEntity livingEntity, ItemStack stack, Arm itemArm) {
        return livingEntity.isUsingItem() && livingEntity.getActiveItem().getItem().equals(stack.getItem());
    }

    @Override
    public boolean showLeftArm(LivingEntity livingEntity, ItemStack stack, Arm itemArm) {
        return livingEntity.isUsingItem() && livingEntity.getActiveItem().equals(stack);
    }

    @Override
    public boolean allowHandTransformation(LivingEntity livingEntity, ItemStack stack, Arm itemArm) {
        return livingEntity.isUsingItem() && livingEntity.getActiveItem().equals(stack);
    }

    @Override
    public boolean shouldRender(LivingEntity livingEntity, ItemStack stack, Arm itemArm) {
        return livingEntity.isUsingItem() && livingEntity.getActiveItem().equals(stack);
    }

    @Override
    public boolean keepFirstPersonPitch(LivingEntity livingEntity, ItemStack stack, Arm itemArm) {
        return livingEntity.isUsingItem() && livingEntity.getActiveItem().equals(stack);
    }

    @Override
    public boolean allowVanillaUsageAnimation() {
        return false;
    }
}
