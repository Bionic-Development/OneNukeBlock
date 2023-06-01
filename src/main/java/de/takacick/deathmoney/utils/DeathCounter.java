package de.takacick.deathmoney.utils;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import de.takacick.deathmoney.DeathMoney;
import de.takacick.deathmoney.access.PlayerProperties;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.text.DecimalFormat;

public class DeathCounter {
    private static final Identifier WIDGETS_TEXTURE = new Identifier(DeathMoney.MOD_ID, "textures/gui/widgets.png");

    public static void render(MinecraftClient client, PlayerProperties playerProperties, MatrixStack matrixStack, float tickDelta) {

        playerProperties.getDeathToasts().forEach(deathToast -> {
            deathToast.render(client, matrixStack, tickDelta);
        });

        if (playerProperties.getDeaths() != 0) {
            int width = (int) ((float) client.getWindow().getScaledWidth() * 0.01f);
            int height = (int) ((float) client.getWindow().getScaledHeight() * 0.99f);

            matrixStack.push();
            renderWidgets(matrixStack, width, height - 20);

            DecimalFormat format = new DecimalFormat("#,###");
            Text text = Text.literal(format.format(playerProperties.getDeaths()));
            int textWidth = -client.textRenderer.getWidth(text) + 100 - 17;
            client.textRenderer.drawWithShadow(matrixStack, text, width + textWidth, height - 13, 0xFFFFFF);

            client.getItemRenderer().renderInGui(Items.SKELETON_SKULL.getDefaultStack(), width + 100 - 18, height - 18);
            matrixStack.pop();
        }
    }

    private static void renderWidgets(MatrixStack matrices, int x, int y) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WIDGETS_TEXTURE);
        DrawableHelper.drawTexture(matrices, x, y, 100, 20, 0, 0, 100, 20, 100, 20);
    }

    public static void renderGuiItemModel(ItemStack stack, float x, float y) {
        BakedModel model = MinecraftClient.getInstance().getItemRenderer().getModel(stack, null, null, 0);
        boolean bl;
        MinecraftClient.getInstance().getTextureManager().getTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).setFilter(false, false);
        RenderSystem.setShaderTexture(0, SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        MatrixStack matrixStack = RenderSystem.getModelViewStack();
        matrixStack.push();
        matrixStack.translate(x, y, 100.0f);
        matrixStack.translate(8.0, 8.0, 0.0);
        matrixStack.scale(1.0f, -1.0f, 1.0f);
        matrixStack.scale(16.0f, 16.0f, 16.0f);
        RenderSystem.applyModelViewMatrix();
        MatrixStack matrixStack2 = new MatrixStack();
        VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        boolean bl2 = bl = !model.isSideLit();
        if (bl) {
            DiffuseLighting.disableGuiDepthLighting();
        }

        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GUI, false, matrixStack2, immediate, LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV, model);
        immediate.draw();
        RenderSystem.enableDepthTest();
        if (bl) {
            DiffuseLighting.enableGuiDepthLighting();
        }
        matrixStack.pop();
        RenderSystem.applyModelViewMatrix();
    }


}
