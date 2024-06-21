package de.takacick.onescaryblock.registry.item;

import de.takacick.onescaryblock.OneScaryBlock;
import de.takacick.onescaryblock.access.PlayerProperties;
import de.takacick.utils.BionicUtils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SoulFragment extends Item {

    public SoulFragment(Settings settings) {
        super(settings.food(new FoodComponent.Builder().alwaysEdible().snack().build()));
    }

    @Override
    public ItemStack finishUsing(ItemStack itemStack, World world, LivingEntity user) {
        if (!world.isClient) {
            if (user instanceof PlayerProperties playerProperties) {
                playerProperties.getSoulFragmentHelper().setTick(playerProperties.getSoulFragmentHelper().getMaxTicks());
                BionicUtils.sendEntityStatus(world, user, OneScaryBlock.IDENTIFIER, 3);
                itemStack.decrement(1);
            }
        }

        return super.finishUsing(itemStack, world, user);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§7A fleeting gift from the"));
        tooltip.add(Text.of("§bspirit world§7, granting"));
        tooltip.add(Text.of("§emomentary flight§7."));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
