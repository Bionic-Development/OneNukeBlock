package de.takacick.emeraldmoney.registry.item;

import de.takacick.emeraldmoney.registry.entity.projectile.PillagerProjectileEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PillagerCannon extends Item {

    public PillagerCannon(Settings settings) {
        super(settings);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        float progress = BowItem.getPullProgress(this.getMaxUseTime(stack) - remainingUseTicks);

        if (progress >= 0.25f) {

            if (!world.isClient) {
                PillagerProjectileEntity pillagerProjectileEntity = new PillagerProjectileEntity(user.getWorld(), user.getX(), user.getBodyY(0.5), user.getZ(), user);
                pillagerProjectileEntity.setOwner(user);
                pillagerProjectileEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 2f * progress, 1f);
                world.spawnEntity(pillagerProjectileEntity);

                world.playSoundFromEntity(null, user, SoundEvents.ENTITY_PILLAGER_CELEBRATE, SoundCategory.BLOCKS, 0.5f, 1f);
                world.playSoundFromEntity(null, user, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.2f, 1f);
            }
        }
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§9Somehow contains infinite §ePillagers"));
        tooltip.add(Text.of("§9for an §aemerald §eexplosion§9!"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
