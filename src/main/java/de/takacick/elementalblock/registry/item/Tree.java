package de.takacick.elementalblock.registry.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.sapling.OakSaplingGenerator;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;

public class Tree extends Item {

    private final SaplingGenerator generator;

    public Tree(Settings settings) {
        super(settings);
        this.generator = new OakSaplingGenerator();
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity playerEntity = context.getPlayer();
        ItemStack itemStack = context.getStack();
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos().add(context.getSide().getVector());
        BlockState blockState = world.getBlockState(context.getBlockPos());

        if (blockState.isLiquid()) {
            return ActionResult.FAIL;
        }

        if (blockState.isReplaceable()) {
            blockPos = blockPos.add(0, -1, 0);
        }

        if (!world.isClient) {
            if (this.generator.generate((ServerWorld) world, ((ServerWorld) world).getChunkManager().getChunkGenerator(), blockPos, Blocks.OAK_SAPLING.getDefaultState(), world.getRandom())) {
                world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.NEUTRAL, 1f, 1f);
                world.setBlockState(blockPos, Blocks.OAK_LOG.getDefaultState());
                spawnEffect(world, blockPos, new ArrayList<>(), false);

                if (!playerEntity.getAbilities().creativeMode) {
                    itemStack.decrement(1);
                }
            }
        }

        return ActionResult.success(true);
    }

    public void spawnEffect(World world, BlockPos pos, ArrayList<BlockPos> list, boolean leaves) {
        for (Direction direction : Direction.values()) {
            Block block = world.getBlockState(pos.add(direction.getVector())).getBlock();
            BlockPos target = pos.add(direction.getVector());
            if ((block == Blocks.OAK_LOG || block == Blocks.OAK_LEAVES) && !list.contains(target)) {
                list.add(target);
                if (!leaves) {
                    spawnEffect(world, target, list, block == Blocks.OAK_LEAVES);
                }
                world.syncWorldEvent(821482, target, 0);
            }
        }
    }
}

