package de.takacick.onedeathblock.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.text.DecimalFormat;

public class DeathToast {

    private int deaths = 0;
    private int x = 0;
    private int y = 0;
    private boolean shouldRemove;
    private int tick = 0;

    public DeathToast(int deaths, int x, int y) {
        this.deaths = deaths;
        this.x = x;
        this.y = y;
    }

    public void tick() {
        this.y += 1;
        this.tick += 1;
    }

    public void render(MinecraftClient client, MatrixStack matrixStack, float tickDelta) {

        int width = (int) ((float) client.getWindow().getScaledWidth() * 0.8f);
        int height = (int) ((float) client.getWindow().getScaledHeight() * (1f - (0.05f * client.getWindow().getScaleFactor())));

        float alpha = Math.min(Math.max(1 - (float) MathHelper.getLerpProgress(height - 15 + this.y, height - 10, client.getWindow().getScaledHeight() * 0.99), 0), 1);

        DecimalFormat format = new DecimalFormat("#,###");
        Text text = Text.literal((this.deaths >= 0 ? "+" : "") + format.format(this.deaths));
        matrixStack.push();
        matrixStack.translate(0, this.y + tickDelta, 0);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        client.textRenderer.drawWithShadow(matrixStack, text, width + 16, height, convertToARGB(this.deaths >= 0 ? 0xFFFFFF : 0xFF5555, alpha));
        RenderSystem.disableBlend();
        matrixStack.pop();

        height = height - 5 + this.y;

        ItemStack itemStack = Items.SKELETON_SKULL.getDefaultStack();
        itemStack.getOrCreateNbt().putFloat("onedeathblock", this.deaths);
        itemStack.getOrCreateNbt().putFloat("alpha", alpha);

        DeathCounter.renderGuiItemModel(itemStack, width, height + tickDelta);

        if (height > client.getWindow().getScaledHeight() * 1.01) {
            this.shouldRemove = true;
        }
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public boolean shouldRemove() {
        return this.shouldRemove;
    }

    public int getDeaths() {
        return this.deaths;
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
