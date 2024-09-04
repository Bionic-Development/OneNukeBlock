package de.takacick.onenukeblock.registry.item;

import de.takacick.onenukeblock.OneNukeBlock;
import de.takacick.utils.common.event.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class KaboomMiner extends PickaxeItem {
    public static ToolMaterial MATERIAL = new KaboomMinerMaterial();

    public KaboomMiner(ToolMaterial material, Settings settings) {
        super(material, settings);
    }

    public void mine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        EventHandler.sendWorldStatus(world, pos.toCenterPos(), OneNukeBlock.IDENTIFIER, 2, Block.getRawIdFromState(state));
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {

        tooltip.add(Text.of("§7Detonates a §cburst §7with every swing!"));

        super.appendTooltip(stack, context, tooltip, type);
    }

    @Override
    public boolean allowComponentsUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
        return false;
    }

    private static class KaboomMinerMaterial implements ToolMaterial {

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
            return Ingredient.ofItems(Items.TNT);
        }
    }
}
