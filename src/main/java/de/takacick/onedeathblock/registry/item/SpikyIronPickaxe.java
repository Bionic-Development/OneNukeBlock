package de.takacick.onedeathblock.registry.item;

import de.takacick.onedeathblock.access.PlayerProperties;
import de.takacick.onedeathblock.damage.DeathDamageSources;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpikyIronPickaxe extends PickaxeItem {

    public SpikyIronPickaxe(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {

        if (miner instanceof PlayerProperties playerProperties) {
            playerProperties.resetDamageDelay();
        }

        miner.damage(DeathDamageSources.SPIKY_IRON_PICKAXE, 4f);

        return super.postMine(stack, world, state, pos, miner);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§eThis has to be a tool of torture...!"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
