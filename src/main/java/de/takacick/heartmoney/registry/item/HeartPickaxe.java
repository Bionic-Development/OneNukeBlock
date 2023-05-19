package de.takacick.heartmoney.registry.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HeartPickaxe extends PickaxeItem {

    public HeartPickaxe(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§eBlocks don't have hearts, but this"));
        tooltip.add(Text.of("§ewill get you them!"));
        tooltip.add(Text.of("§9§oBasic Tier"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
