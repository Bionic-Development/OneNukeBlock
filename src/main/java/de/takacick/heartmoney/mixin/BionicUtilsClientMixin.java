package de.takacick.heartmoney.mixin;

import de.takacick.utils.BionicUtilsClient;
import de.takacick.utils.heart.Heart;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.text.NumberFormat;
import java.util.Locale;

@Mixin(BionicUtilsClient.class)
public abstract class BionicUtilsClientMixin {

    @Inject(method = "drawHeart", at = @At("HEAD"), cancellable = true)
    private static void drawHeart(PlayerEntity playerEntity, Heart textureHeart, Heart heart, MatrixStack matrixStack, boolean haftHeart, boolean blinking, int x, int y, int index, CallbackInfo info) {
        if (playerEntity.getHealth() > 500) {
            if (index == 0) {
                Locale locale = new Locale("en", "US");
                NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
                int o = MinecraftClient.getInstance().getWindow().getScaledHeight() - 39;

                Text text = Text.literal(currencyFormatter.format(playerEntity.getHealth() / 2)
                                .replace("$", "").split("\\.")[0])
                        .setStyle(Style.EMPTY.withColor(16744319));
                MinecraftClient.getInstance().textRenderer.drawWithShadow(matrixStack, text,
                        (float) x + 11f, (float) o + 0.75f, 8355711);
            } else {
                info.cancel();
            }
        }
    }

    @Inject(method = "drawContainer", at = @At("HEAD"), cancellable = true)
    private static void drawContainer(PlayerEntity playerEntity, Heart heart, MatrixStack matrixStack, boolean haftHeart, boolean blinking, int x, int y, int index, CallbackInfo info) {
        if (playerEntity.getHealth() > 500) {
            if (index != 0) {
                info.cancel();
            }
        }
    }
}