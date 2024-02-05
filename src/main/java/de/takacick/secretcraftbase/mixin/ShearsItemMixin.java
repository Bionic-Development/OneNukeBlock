package de.takacick.secretcraftbase.mixin;

import de.takacick.secretcraftbase.registry.ItemRegistry;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ShearsItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShearsItem.class)
public abstract class ShearsItemMixin extends Item {

    public ShearsItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    public void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> info) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);

        if (blockState.isOf(Blocks.REDSTONE_ORE) || blockState.isOf(Blocks.DEEPSLATE_REDSTONE_ORE)) {
            PlayerEntity playerEntity = context.getPlayer();
            ItemStack itemStack = context.getStack();
            if (playerEntity instanceof ServerPlayerEntity) {
                Criteria.ITEM_USED_ON_BLOCK.trigger((ServerPlayerEntity) playerEntity, blockPos, itemStack);
            }

            world.playSound(playerEntity, blockPos, SoundEvents.BLOCK_DEEPSLATE_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
            if(!world.isClient) {
                Block.dropStack(world, blockPos, context.getSide(), ItemRegistry.REDSTONE_ORE_CHUNKS.getDefaultStack());
            }
            BlockState blockState2 = (blockState.isOf(Blocks.REDSTONE_ORE) ? Blocks.STONE : Blocks.DEEPSLATE).getDefaultState();

            world.setBlockState(blockPos, blockState2);
            world.syncWorldEvent(82139123, blockPos, 0);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Emitter.of(context.getPlayer(), blockState2));
            if (playerEntity != null) {
                itemStack.damage(1, playerEntity, (player) -> {
                    player.sendToolBreakStatus(context.getHand());
                });
            }

            info.setReturnValue(ActionResult.success(world.isClient));
        }
    }
}