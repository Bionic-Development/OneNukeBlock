package de.takacick.onegirlboyblock.registry.item;

import de.takacick.onegirlboyblock.registry.block.OneGirlBlock;
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

public class StarMiner extends PickaxeItem {

    public static ToolMaterial STAR_MINER = new StarMinerMaterial();

    public StarMiner(Settings settings) {
        super(STAR_MINER, settings);
    }

    @Override
    public float getMiningSpeed(ItemStack stack, BlockState state) {
        return state.getBlock() instanceof OneGirlBlock ? 1.2f : super.getMiningSpeed(stack, state);
    }


    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal("§cBlast §rthrough blocks like").withColor(0xFFCCFF));
        tooltip.add(Text.literal("a §essupernova§r!").withColor(0xFFCCFF));

        super.appendTooltip(stack, context, tooltip, type);
    }

    private static class StarMinerMaterial implements ToolMaterial {

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
