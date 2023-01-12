package de.takacick.immortalmobs.registry.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ImmortalArrow extends ArrowItem implements ImmortalItem {

    public ImmortalArrow(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§eReleases the power of"));
        tooltip.add(Text.of("§dImmortality §eupon impact..."));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
