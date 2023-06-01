package de.takacick.deathmoney.registry.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InfiniteDeathTotem extends Item {

    public InfiniteDeathTotem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§7Let's you §cdie smoothly§7... Infinitely!"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
