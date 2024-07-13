package de.takacick.onegirlboyblock.registry.item;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;

import java.util.List;

public class FootballGear extends ArmorItem {

    public FootballGear(RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
        super(material, type, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal("Peak §cAme§fri§bcan §rdefense baby!").withColor(0x0066FF));
        tooltip.add(Text.literal("§eHOORAH§r!").withColor(0x0066FF));

        super.appendTooltip(stack, context, tooltip, type);
    }
}
