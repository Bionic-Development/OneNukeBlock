package de.takacick.emeraldmoney.registry.item;

import de.takacick.emeraldmoney.EmeraldMoney;
import de.takacick.emeraldmoney.access.PlayerProperties;
import de.takacick.utils.BionicUtils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EmeraldWallet extends Item {

    public EmeraldWallet(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (user instanceof PlayerProperties playerProperties
                && playerProperties.hasEmeraldWallet()) {
            return TypedActionResult.fail(itemStack);
        }

        if (!world.isClient) {
            if (user instanceof PlayerProperties playerProperties) {
                int emeralds = 0;
                for (int i = 0; i < user.getInventory().size(); i++) {
                    ItemStack stack = user.getInventory().getStack(i);
                    int count = (stack.isOf(Items.EMERALD) ? 1 : stack.isOf(Items.EMERALD_BLOCK) ? 9 : 0) * stack.getCount();
                    if (count > 0) {
                        stack.setCount(0);
                        emeralds += count;
                    }
                }

                playerProperties.addEmeralds(emeralds, true);
                playerProperties.setEmeraldWallet(true);
                BionicUtils.sendEntityStatus((ServerWorld) world, user, EmeraldMoney.IDENTIFIER, 1);
            }
        }

        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }
        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§9Stores infinite §aemeralds §9digitally!"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
