package de.takacick.secretcraftbase.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import de.takacick.secretcraftbase.registry.ItemRegistry;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @ModifyVariable(method = "renderBakedItemModel", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    public int renderBakedItemModel(int light, @Local(ordinal = 0) ItemStack stack) {
        if (stack.isOf(ItemRegistry.NETHER_PORTAL_BLOCK) || stack.isOf(ItemRegistry.SECRET_FAKE_SUN_ITEM)) {
            return 15728880;
        }

        return light;
    }
}