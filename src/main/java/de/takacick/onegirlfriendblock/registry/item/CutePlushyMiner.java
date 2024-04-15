package de.takacick.onegirlfriendblock.registry.item;

import de.takacick.onegirlfriendblock.OneGirlfriendBlock;
import de.takacick.onegirlfriendblock.registry.ItemRegistry;
import de.takacick.utils.BionicUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CutePlushyMiner extends PickaxeItem {

    public CutePlushyMiner(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        return state.isOf(ItemRegistry.GIRLFRIEND_ONE_BLOCK) ? 1.2f : super.getMiningSpeedMultiplier(stack, state);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {

        BionicUtils.sendEntityStatus(target.getWorld(), target, OneGirlfriendBlock.IDENTIFIER, 8);

        return super.postHit(stack, target, attacker);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {

        world.syncWorldEvent(19238412, pos, 0);

        return super.postMine(stack, world, state, pos, miner);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§eSoftly destroy any block with a"));
        tooltip.add(Text.of("§egentle whack!"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
