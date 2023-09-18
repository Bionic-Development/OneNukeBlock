package de.takacick.onesuperblock.registry.item;

import de.takacick.superitems.registry.item.SuperItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SuperEnchanterBook extends Item implements SuperItem {
    public SuperEnchanterBook(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§eUses ancient Super knowledge to"));
        tooltip.add(Text.of("§eenchant anything it's dropped on!"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
