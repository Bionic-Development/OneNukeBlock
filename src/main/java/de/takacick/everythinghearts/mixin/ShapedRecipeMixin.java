package de.takacick.everythinghearts.mixin;

import de.takacick.everythinghearts.registry.ItemRegistry;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShapedRecipe.class)
public abstract class ShapedRecipeMixin implements CraftingRecipe {

    @Shadow public abstract ItemStack getOutput();

    @Inject(method = "matches(Lnet/minecraft/inventory/CraftingInventory;Lnet/minecraft/world/World;)Z", at = @At("HEAD"), cancellable = true)
    public void matches(CraftingInventory craftingInventory, World world, CallbackInfoReturnable<Boolean> info) {
        if (getOutput().isOf(Items.WOODEN_PICKAXE) || getOutput().isOf(Items.WOODEN_SWORD)) {
            for(int i = 0; i < craftingInventory.size(); i++) {
                if(craftingInventory.getStack(i).isOf(ItemRegistry.HEART_PLANKS_ITEM)) {
                    info.setReturnValue(false);
                }
            }
        }
    }
}
