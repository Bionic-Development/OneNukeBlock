package de.takacick.emeraldmoney.client.gui;

import de.takacick.emeraldmoney.EmeraldMoney;
import de.takacick.emeraldmoney.access.PlayerProperties;
import de.takacick.emeraldmoney.registry.ItemRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.text.DecimalFormat;

public class EmeraldCounter {
    private static final Identifier WIDGETS_TEXTURE = new Identifier(EmeraldMoney.MOD_ID, "textures/gui/emerald_counter.png");
    private static final Identifier MULTIPLIED_WIDGETS_TEXTURE = new Identifier(EmeraldMoney.MOD_ID, "textures/gui/emerald_counter_multiplied.png");

    public static void render(MinecraftClient client, PlayerEntity playerEntity, DrawContext drawContext, float tickDelta) {
        if (playerEntity instanceof PlayerProperties playerProperties
                && playerProperties.hasEmeraldWallet()) {
            playerProperties.getEmeraldToasts().forEach(emeraldToast -> {
                emeraldToast.render(client, drawContext, tickDelta);
            });

            int width = (int) ((float) client.getWindow().getScaledWidth() * 0.02f);
            int height = (int) ((float) client.getWindow().getScaledHeight() * 0.5f) + 18;

            MatrixStack matrixStack = drawContext.getMatrices();

            matrixStack.push();
            renderWidgets(playerEntity, drawContext, width, height - 39);

            DecimalFormat format = new DecimalFormat("#,###");
            OrderedText text = Text.literal(format.format(playerProperties.getEmeralds())).asOrderedText();
            int textWidth = (int) (Math.log10(Math.abs(playerProperties.getEmeralds())) + 1) * 3;
            matrixStack.translate(width + 18, height - 21.75f, 0f);
            if (textWidth > 15) {
                int numbers = ((textWidth - 12) / 3);
                for (int i = 0; i < numbers; i++) {
                    float scale = (0.4f - (0.05f * (Math.min(i - 1, 3)))) * (float) i / (float) numbers;
                    matrixStack.translate(0, 4.75 * (scale), 0);
                    matrixStack.scale(1f - scale, 1f - scale, 1f - scale);
                }
            }
            drawContext.drawCenteredTextWithShadow(client.textRenderer, text, 0, 0, 0xFFFFFF);
            matrixStack.pop();
        }
    }

    private static void renderWidgets(PlayerEntity playerEntity, DrawContext drawContext, int x, int y) {
        drawContext.drawTexture(playerEntity.getEquippedStack(EquipmentSlot.CHEST).isOf(ItemRegistry.VILLAGER_ROBE)
                        ? MULTIPLIED_WIDGETS_TEXTURE : WIDGETS_TEXTURE,
                x, y, 36, 42, 0, 0, 12, 14, 12, 14);
    }
}
