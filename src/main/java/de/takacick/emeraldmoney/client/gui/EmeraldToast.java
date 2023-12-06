package de.takacick.emeraldmoney.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.text.DecimalFormat;

public class EmeraldToast {

    private int size = 0;
    private int x = 0;
    private int y = 0;
    private boolean shouldRemove;
    private int tick = 0;

    public EmeraldToast(int size, int x, int y) {
        this.size = size;
        this.x = x;
        this.y = y;
    }

    public void tick() {
        this.y += 1;
        this.tick += 1;
    }

    public void render(MinecraftClient client, DrawContext drawContext, float tickDelta) {
        int width = (int) ((float) client.getWindow().getScaledWidth() * 0.8f);
        int height = (int) ((float) client.getWindow().getScaledHeight() * (1f - (0.05f * client.getWindow().getScaleFactor())));

        float alpha = Math.min(Math.max(1 - (float) MathHelper.getLerpProgress(height - 15 + this.y, height - 10, client.getWindow().getScaledHeight() * 0.99), 0), 1);

        MatrixStack matrixStack = drawContext.getMatrices();

        boolean positive = this.size >= 0;

        DecimalFormat format = new DecimalFormat("#,###");
        Text text = Text.literal((positive ? "+" : "-") + format.format(Math.abs(this.size)))
                .setStyle(Style.EMPTY.withColor(positive ? 0x55FF55 : 0xFF5555))
                .append(Text.literal(positive ? "\uE003" : "\uE004").setStyle(Style.EMPTY.withColor(0xFFFFFF)));
        matrixStack.push();
        matrixStack.translate(0, this.y + tickDelta, 0);
        drawContext.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, text, width + 16, height, convertToARGB(0xFFFFFF, alpha));
        matrixStack.pop();

        height = height - 5 + this.y;

        if (height > client.getWindow().getScaledHeight() * 1.01) {
            this.shouldRemove = true;
        }
    }

    public void setEmeralds(int size) {
        this.size = size;
    }

    public boolean shouldRemove() {
        return this.shouldRemove;
    }

    public int getEmeralds() {
        return this.size;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    private static int convertToARGB(int hexColor, float alpha) {
        int red = (hexColor >> 16) & 0xFF;
        int green = (hexColor >> 8) & 0xFF;
        int blue = hexColor & 0xFF;

        int alphaValue = (int) (alpha * 255);

        return (alphaValue << 24) | (red << 16) | (green << 8) | blue;
    }

    public int getTick() {
        return tick;
    }
}
