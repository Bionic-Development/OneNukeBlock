package de.takacick.onescaryblock.registry.item;

import de.takacick.onescaryblock.registry.ItemRegistry;
import de.takacick.onescaryblock.registry.block.ScaryOneBlock;
import net.fabricmc.yarn.constants.MiningLevels;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SoulPiercer extends PickaxeItem {

    public static ToolMaterial SOUL_MATERIAL = new SoulMaterial();

    public SoulPiercer(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }
    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        return state.getBlock() instanceof ScaryOneBlock ? 1.3f : super.getMiningSpeedMultiplier(stack, state);
    }
    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {

        if (!world.isClient && world.getRandom().nextDouble() <= 0.1) {
            if (state.getBlock() instanceof ScaryOneBlock) {
                Block.dropStack(world, pos, Direction.UP, ItemRegistry.SOUL_FRAGMENT.getDefaultStack());
            } else {
                Block.dropStack(world, pos, ItemRegistry.SOUL_FRAGMENT.getDefaultStack());
            }
        }

        return super.postMine(stack, world, state, pos, miner);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§cPierce any block §7using the"));
        tooltip.add(Text.of("§7power of it's §bharvested souls§7!"));

        super.appendTooltip(stack, world, tooltip, context);
    }

    private static class SoulMaterial implements ToolMaterial {

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
        public int getMiningLevel() {
            return MiningLevels.DIAMOND;
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
