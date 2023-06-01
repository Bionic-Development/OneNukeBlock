package de.takacick.deathmoney.registry.item;

import de.takacick.deathmoney.DeathMoney;
import de.takacick.deathmoney.access.PlayerProperties;
import de.takacick.utils.BionicUtils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HeartCarver extends Item {

    public HeartCarver(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        PlayerProperties playerProperties = (PlayerProperties) user;

        if (playerProperties.getHeartRemovalTicks() > 0 || hand.equals(Hand.OFF_HAND)) {
            return TypedActionResult.fail(itemStack);
        }
        if (!world.isClient) {
            playerProperties.setHeartRemovalTicks(35);
            BionicUtils.sendEntityStatus((ServerWorld) world, user, DeathMoney.IDENTIFIER, 2);
            playerProperties.setHeartCarverStack(itemStack);
        }

        return TypedActionResult.success(itemStack, false);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§7Carve your own §chealth §7out!"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
