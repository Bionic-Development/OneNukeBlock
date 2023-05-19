package de.takacick.heartmoney.mixin;

import de.takacick.heartmoney.registry.ItemRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.block.RedstoneOreBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(LootTable.class)
public class LootTableMixin {

    @Inject(method = "generateLoot*", at = @At(value = "HEAD"))
    public void generateLoot(LootContext context, Consumer<ItemStack> lootConsumer, CallbackInfo info) {
        LootTable lootTable = (LootTable) (Object) this;
        if (lootTable.getType().equals(LootContextTypes.BLOCK) && context.hasParameter(LootContextParameters.TOOL)
                && context.hasParameter(LootContextParameters.THIS_ENTITY)) {
            Entity entity = context.get(LootContextParameters.THIS_ENTITY);
            World world = context.get(LootContextParameters.THIS_ENTITY).getEntityWorld();

            if (context.hasParameter(LootContextParameters.BLOCK_STATE)) {
                BlockState blockState = context.get(LootContextParameters.BLOCK_STATE);

                if ((blockState.getBlock() instanceof OreBlock || blockState.getBlock() instanceof RedstoneOreBlock) && entity instanceof PlayerEntity playerEntity
                        && playerEntity.canHarvest(blockState)) {
                    int count = 0;

                    if (blockState.isIn(BlockTags.COAL_ORES)
                            || blockState.isIn(BlockTags.COPPER_ORES)) {
                        count = world.getRandom().nextBetween(1, 3);
                    } else if (blockState.isIn(BlockTags.IRON_ORES)) {
                        count = world.getRandom().nextBetween(2, 5);
                    } else if (blockState.isIn(BlockTags.GOLD_ORES)
                            || blockState.isIn(BlockTags.REDSTONE_ORES)) {
                        count = world.getRandom().nextBetween(5, 10);
                    } else if (blockState.isIn(BlockTags.LAPIS_ORES)) {
                        count = world.getRandom().nextBetween(10, 20);
                    } else if (blockState.isIn(BlockTags.DIAMOND_ORES)) {
                        count = world.getRandom().nextBetween(20, 50);
                    } else if (blockState.isIn(BlockTags.EMERALD_ORES)) {
                        count = world.getRandom().nextBetween(25, 100);
                    }

                    for (int i = 0; i < count + (context.get(LootContextParameters.TOOL).isOf(ItemRegistry.HEART_PICKAXE) ? 1 : 0); i++) {
                        lootConsumer.accept(ItemRegistry.HEART.getDefaultStack());
                    }
                    world.syncWorldEvent(456789129, new BlockPos(context.get(LootContextParameters.ORIGIN)), 0);
                } else if (context.get(LootContextParameters.TOOL).isOf(ItemRegistry.HEART_PICKAXE)) {
                    lootConsumer.accept(ItemRegistry.HEART.getDefaultStack());

                    if (world instanceof ServerWorld && context.hasParameter(LootContextParameters.ORIGIN)) {
                        world.syncWorldEvent(456789129, new BlockPos(context.get(LootContextParameters.ORIGIN)), 0);
                    }
                }
            }
        }
    }
}
