package de.takacick.onescaryblock.registry.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BloodBucket extends BucketItem {

    public BloodBucket(Fluid fluid, Settings settings) {
        super(fluid, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§7Who's §cblood §7is this...?"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
