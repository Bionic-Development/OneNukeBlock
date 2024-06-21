package de.takacick.onescaryblock.registry.item;

import de.takacick.onescaryblock.registry.ItemRegistry;
import de.takacick.onescaryblock.registry.block.Item303Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Item303 extends Item {

    public Item303(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);

        if (blockState.getBlock() instanceof Item303Block || blockState.isOf(ItemRegistry.SCARY_ONE_BLOCK)) {
            return ActionResult.PASS;
        }

        if (!world.isClient) {
            world.setBlockState(blockPos, ItemRegistry.ITEM_303_BLOCK.getDefaultState());
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§7Possess the §4dark code§7,"));
        tooltip.add(Text.of("§fEntity§c3§80§c3§7's forbidden power..."));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
