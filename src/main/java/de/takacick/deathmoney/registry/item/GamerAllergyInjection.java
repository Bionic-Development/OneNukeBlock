package de.takacick.deathmoney.registry.item;

import de.takacick.deathmoney.access.PlayerProperties;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GamerAllergyInjection extends Item {

    public GamerAllergyInjection(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity) user : null;
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        if (!world.isClient) {
            if (playerEntity instanceof PlayerProperties playerProperties) {
                playerProperties.setGamerAllergyTicks(200);
            }
        }
        if (playerEntity != null) {
            playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
            if (!playerEntity.getAbilities().creativeMode) {
                nbtCompound.putInt("usages", nbtCompound.getInt("usages") + 1);
            }
        }

        user.emitGameEvent(GameEvent.DRINK);
        return stack;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        NbtCompound nbtCompound = stack.getNbt();

        if (nbtCompound != null) {
            if (nbtCompound.getInt("usages") >= 3) {
                return TypedActionResult.fail(stack);
            }
        }

        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 32;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§cTouch grass§7? \"B-b-but I'm a gamer!\""));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
