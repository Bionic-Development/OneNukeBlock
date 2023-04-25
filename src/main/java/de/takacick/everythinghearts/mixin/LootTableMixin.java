package de.takacick.everythinghearts.mixin;

import de.takacick.everythinghearts.registry.ItemRegistry;
import de.takacick.everythinghearts.registry.item.WoodenHeartPickaxe;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(LootTable.class)
public class LootTableMixin {

    @Inject(method = "generateLoot*", at = @At(value = "HEAD"), cancellable = true)
    public void generateLoot(LootContext context, Consumer<ItemStack> lootConsumer, CallbackInfo info) {
        if (context.hasParameter(LootContextParameters.BLOCK_STATE)) {
            BlockState blockState = context.get(LootContextParameters.BLOCK_STATE);
            if (blockState.isOf(ItemRegistry.HEARTMOND_ORE)
                    && blockState.isOf(ItemRegistry.BASIC_HEART_BLOCK)
                    && blockState.isOf(ItemRegistry.MULTI_HEART_BLOCK)
                    && !(context.hasParameter(LootContextParameters.TOOL)
                    && context.get(LootContextParameters.TOOL).getItem() instanceof WoodenHeartPickaxe)) {
                info.cancel();
            }
        }
    }
}