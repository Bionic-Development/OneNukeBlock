package de.takacick.onesuperblock.registry.item;

import de.takacick.onesuperblock.registry.entity.projectiles.SuperEnderPearlEntity;
import de.takacick.superitems.registry.item.SuperItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SuperEnderPearl extends Item implements SuperItem {

    public SuperEnderPearl(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ENDER_PEARL_THROW, SoundCategory.PLAYERS, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        if (!world.isClient) {
            SuperEnderPearlEntity superEnderPearlEntity = new SuperEnderPearlEntity(world, user);
            superEnderPearlEntity.setItem(itemStack);
            superEnderPearlEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 2.5f, 1.0f);
            world.spawnEntity(superEnderPearlEntity);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.damage(1, user, playerEntity -> playerEntity.sendToolBreakStatus(hand));
        }
        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§eA pearl so light, thrown as if you"));
        tooltip.add(Text.of("§ehad Super strength!"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
