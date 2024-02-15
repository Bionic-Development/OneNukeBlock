package de.takacick.secretgirlbase.registry.item;

import de.takacick.secretgirlbase.access.LivingProperties;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class TeenyBerries extends AliasedBlockItem {

    public TeenyBerries(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {

        if (!world.isClient) {
            if (user instanceof LivingProperties livingProperties) {
                livingProperties.setTeenyBerryTicks(300);
            }
        }

        return super.finishUsing(stack, world, user);
    }
}
