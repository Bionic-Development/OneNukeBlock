package de.takacick.onenukeblock.registry.item;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.List;

public class SkylandTnt extends BlockItem {

    public SkylandTnt(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {

        tooltip.add(Text.of("§7Upon exploding, summons a §crandomized"));
        tooltip.add(Text.of("§csky island §econtaining riches§7!"));

        super.appendTooltip(stack, context, tooltip, type);
    }
}
