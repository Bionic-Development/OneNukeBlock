package de.takacick.heartmoney.registry.block;

import de.takacick.heartmoney.registry.ItemRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class HeartCatalystOre extends OreBlock {

    public HeartCatalystOre(Settings settings) {
        super(settings);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {

        if (!world.isClient) {
            for (int i = 0; i < world.getRandom().nextBetween(10, 100); i++) {
                dropStack(world, pos, ItemRegistry.HEART.getDefaultStack());
            }
            world.syncWorldEvent(456789129, pos, 1);
        }

        super.onBreak(world, pos, state, player);
    }

    public static void dropStack(World world, BlockPos pos, ItemStack stack) {
        double d = MathHelper.nextDouble(world.random, -0.25, 0.25);
        double e = MathHelper.nextDouble(world.random, -0.25, 0.25);
        double g = MathHelper.nextDouble(world.random, -0.25, 0.25);
        ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5f + d, pos.getY() + 0.5f + e, pos.getZ() + 0.5f + g, stack, d * 1.5, e * 1.5, g * 1.5);
        itemEntity.setPickupDelay(10);
        itemEntity.setToDefaultPickupDelay();
        world.spawnEntity(itemEntity);
    }

}
