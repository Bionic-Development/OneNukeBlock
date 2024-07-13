package de.takacick.onegirlboyblock.registry.item;

import de.takacick.onegirlboyblock.OneGirlBoyBlock;
import de.takacick.onegirlboyblock.registry.ParticleRegistry;
import de.takacick.onegirlboyblock.registry.entity.projectiles.TetrisEntity;
import de.takacick.onegirlboyblock.utils.data.ItemDataComponents;
import de.takacick.onegirlboyblock.utils.data.item.BitCannonItemHelper;
import de.takacick.utils.common.event.EventHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class BitCannon extends Item implements HandheldItem {

    public BitCannon(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public void tickInventory(LivingEntity entity, ItemStack itemStack) {
        BitCannonItemHelper itemAnimationHelper = itemStack.getOrDefault(ItemDataComponents.BIT_CANNON_HELPER, new BitCannonItemHelper());

        if (itemStack.get(ItemDataComponents.BIT_CANNON_HELPER) == null) {
            itemStack.set(ItemDataComponents.BIT_CANNON_HELPER, itemAnimationHelper);
        }

        if (itemAnimationHelper != null) {
            itemAnimationHelper.tick(entity, itemStack);
            if (itemAnimationHelper.isDirty()) {
                Vec3d rotation = entity.getRotationVector().multiply(0.5);

                TetrisEntity tetrisEntity = new TetrisEntity(entity.getWorld(), entity.getX() + rotation.getX(), entity.getEyeY() - 0.1 + rotation.getY(), entity.getZ() + rotation.getZ(), entity);
                tetrisEntity.setVariant(itemAnimationHelper.getVariant());
                tetrisEntity.setOwner(entity);
                tetrisEntity.setVelocity(entity, entity.getPitch(), entity.getYaw(), 0.0f, 2f, 1f);
                entity.getWorld().spawnEntity(tetrisEntity);

                itemAnimationHelper.setVariant(TetrisEntity.Variant.byId(entity.getRandom().nextInt(TetrisEntity.Variant.values().length)));
                itemAnimationHelper.setDirty(false);

                int hand = entity.getMainHandStack().equals(itemStack) ? 0 : 1;

                entity.getWorld().playSoundFromEntity(null, entity, ParticleRegistry.BIT_CANNON, SoundCategory.AMBIENT, 2f, 1f + entity.getRandom().nextFloat() * 0.2f);

                EventHandler.sendEntityStatus(entity.getWorld(), entity, OneGirlBoyBlock.IDENTIFIER, 4, (hand << 4) | itemAnimationHelper.getVariant().getId());
            }
        }
    }

    @Override
    public boolean allowComponentsUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
        return false;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal("Fires §epixelated blasts §rof").withColor(0x0066FF));
        tooltip.add(Text.literal("nostalgic power...").withColor(0x0066FF));

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
