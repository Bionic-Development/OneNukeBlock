package de.takacick.elementalblock.registry.item;

import de.takacick.elementalblock.OneElementalBlock;
import de.takacick.elementalblock.access.PlayerProperties;
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

public class MagicLavaScripture extends Item {

    public MagicLavaScripture(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (user instanceof PlayerProperties playerProperties && !world.isClient) {
            playerProperties.setLavaBionic(600);
            BionicUtils.sendEntityStatus((ServerWorld) world, user, OneElementalBlock.IDENTIFIER, 3);
        }

        if (!user.getAbilities().creativeMode) {
            itemStack.damage(1, user, p -> p.sendToolBreakStatus(user.getActiveHand()));
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.of("§eThose who read will have their veins"));
        tooltip.add(Text.of("§efilled with §6lava §etemporarily!"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
