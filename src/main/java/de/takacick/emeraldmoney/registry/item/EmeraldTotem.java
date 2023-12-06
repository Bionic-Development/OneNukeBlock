package de.takacick.emeraldmoney.registry.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EmeraldTotem extends Item {

    public EmeraldTotem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§9Defeat death with the power of"));
        tooltip.add(Text.of("§aemeralds§9!"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
