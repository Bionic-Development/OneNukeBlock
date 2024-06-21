package de.takacick.onescaryblock.registry.item;

import de.takacick.onescaryblock.registry.entity.projectile.HerobrineLightningProjectileEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HerobrineLightningBolt extends Item {

    public HerobrineLightningBolt(Settings settings) {
        super(settings);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        world.playSoundFromEntity(null, user, SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.PLAYERS, 1f, 3f);
        world.playSoundFromEntity(null, user, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1f, 3f);
        if (!world.isClient) {
            HerobrineLightningProjectileEntity herobrineLightningProjectileEntity = new HerobrineLightningProjectileEntity(world, user);
            herobrineLightningProjectileEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 3.25f, 1.0f);
            world.spawnEntity(herobrineLightningProjectileEntity);

            user.swingHand(user.getStackInHand(Hand.MAIN_HAND).equals(stack) ? Hand.MAIN_HAND : Hand.OFF_HAND, true);

            if (user instanceof PlayerEntity playerEntity) {
                playerEntity.getItemCooldownManager().set(this, 10);
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
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§7Wield §f§lHerobrine§7's vengeance,"));
        tooltip.add(Text.of("§clightning with no mercy§7..."));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
