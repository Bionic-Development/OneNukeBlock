package de.takacick.onescaryblock.mixin.item;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import de.takacick.onescaryblock.client.hud.CursedTextRenderer;
import de.takacick.onescaryblock.registry.ItemRegistry;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DrawContext.class)
public abstract class DrawContextMixin {

    @Shadow
    public abstract int drawText(TextRenderer textRenderer, @Nullable String text, int x, int y, int color, boolean shadow);

    @WrapWithCondition(method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;IIIZ)I"))
    public boolean drawItemInSlot(DrawContext instance, TextRenderer textRenderer, String text, int x1, int y, int color, boolean shadow, @Local(argsOnly = true) ItemStack stack, @Local(argsOnly = true, ordinal = 0) int x) {
        if (stack.isOf(ItemRegistry.ITEM_303)) {
            String number = "303";
            this.drawText(new CursedTextRenderer(textRenderer), number, x + 19 - 2 - textRenderer.getWidth(number),
                    y, 0xFFFFFF, true);
            return false;
        }

        return true;
    }

    @ModifyExpressionValue(method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getCount()I", ordinal = 0))
    public int drawItem303Text(int original, @Local(argsOnly = true) ItemStack itemStack) {

        if (itemStack.isOf(ItemRegistry.ITEM_303)) {
            return 2;
        }

        return original;
    }

}
