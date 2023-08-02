package de.takacick.onedeathblock.registry.block;

import de.takacick.onedeathblock.damage.DeathDamageSources;
import net.minecraft.block.BlockState;
import net.minecraft.block.DyedCarpetBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LegoCarpet extends DyedCarpetBlock {

    public LegoCarpet(DyeColor dyeColor, Settings settings) {
        super(dyeColor, settings);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient && entity.getVelocity().length() > 0.0784f) {
            entity.damage(DeathDamageSources.LEGO_CARPET, 5f);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {

        tooltip.add(Text.of("§eMost painful carpet known to mankind"));

        super.appendTooltip(stack, world, tooltip, options);
    }

}
