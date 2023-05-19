package de.takacick.heartmoney.registry.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LoverTotem extends Item {

    public LoverTotem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§eSaves your life with the power of"));
        tooltip.add(Text.of("§elove!"));
        tooltip.add(Text.of("§c§oHigh Tier"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
