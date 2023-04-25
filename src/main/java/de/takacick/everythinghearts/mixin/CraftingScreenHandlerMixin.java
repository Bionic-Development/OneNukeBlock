package de.takacick.everythinghearts.mixin;

import de.takacick.everythinghearts.registry.ItemRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CraftingScreenHandler.class)
public abstract class CraftingScreenHandlerMixin extends AbstractRecipeScreenHandler<CraftingInventory> {

    @Shadow
    @Final
    private ScreenHandlerContext context;

    public CraftingScreenHandlerMixin(ScreenHandlerType<?> screenHandlerType, int i) {
        super(screenHandlerType, i);
    }

    @Inject(method = "canUse", at = @At("RETURN"), cancellable = true)
    public void canUse(PlayerEntity player, CallbackInfoReturnable<Boolean> info) {
        if (!info.getReturnValue()) {
            info.setReturnValue(CraftingScreenHandler.canUse(this.context, player, ItemRegistry.HEART_CRAFTING_TABLE));
        }
    }
}
