package de.takacick.onegirlboyblock.registry.item;

import de.takacick.onegirlboyblock.registry.block.OneBoyBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;

import java.util.List;

public class WeightedAnvilHammer extends PickaxeItem {

    public static ToolMaterial WEIGHTED_ANVIL_HAMMER = new WeightedAnvilHammerMaterial();

    public WeightedAnvilHammer(Settings settings) {
        super(WEIGHTED_ANVIL_HAMMER, settings);
    }

    @Override
    public float getMiningSpeed(ItemStack stack, BlockState state) {
        return state.getBlock() instanceof OneBoyBlock ? 1.2f : super.getMiningSpeed(stack, state);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal("A §bhammer §rthat embodies the peak").withColor(0x0066FF));
        tooltip.add(Text.literal("of §egym-forged strength§r...").withColor(0x0066FF));

        super.appendTooltip(stack, context, tooltip, type);
    }

    private static class WeightedAnvilHammerMaterial implements ToolMaterial {

        @Override
        public int getDurability() {
            return 1561;
        }

        @Override
        public float getMiningSpeedMultiplier() {
            return 8.0f;
        }

        @Override
        public float getAttackDamage() {
            return 3.0f;
        }

        @Override
        public TagKey<Block> getInverseTag() {
            return BlockTags.INCORRECT_FOR_DIAMOND_TOOL;
        }

        @Override
        public int getEnchantability() {
            return 10;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.empty();
        }
    }
}
