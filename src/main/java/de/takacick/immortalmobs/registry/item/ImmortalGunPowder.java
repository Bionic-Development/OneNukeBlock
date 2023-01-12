package de.takacick.immortalmobs.registry.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ImmortalGunPowder extends Item implements ImmortalItem {

    public ImmortalGunPowder(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§eThis can't be dangerous..."));
        tooltip.add(Text.of("§eright?"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
