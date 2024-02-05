package de.takacick.secretcraftbase.registry.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class RedstoneOreChunks extends Item {

    public RedstoneOreChunks(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos blockPos;
        World world = context.getWorld();
        BlockState blockState = world.getBlockState(blockPos = context.getBlockPos());
        if (blockState.isOf(Blocks.STONE) || blockState.isOf(Blocks.DEEPSLATE)) {
            PlayerEntity playerEntity = context.getPlayer();
            ItemStack itemStack = context.getStack();

            world.playSound(playerEntity, blockPos, SoundEvents.BLOCK_DEEPSLATE_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            BlockState blockState2 = (blockState.isOf(Blocks.STONE) ? Blocks.REDSTONE_ORE : Blocks.DEEPSLATE_REDSTONE_ORE).getDefaultState();
            world.syncWorldEvent(82139123, blockPos, 1);

            world.setBlockState(blockPos, blockState2);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Emitter.of(context.getPlayer(), blockState2));
            if (playerEntity != null && !playerEntity.isCreative()) {
                itemStack.decrement(1);
            }

            return ActionResult.success(world.isClient);
        }
        return super.useOnBlock(context);
    }
}
