package de.takacick.everythinghearts.registry.item;

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

        tooltip.add(Text.of("§eEnhances the power of §clove §eusing"));
        tooltip.add(Text.of("§eancient limitless techniques..."));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
