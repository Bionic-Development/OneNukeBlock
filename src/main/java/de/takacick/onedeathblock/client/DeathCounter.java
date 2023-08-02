package de.takacick.onedeathblock.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import de.takacick.onedeathblock.OneDeathBlock;
import de.takacick.onedeathblock.access.PlayerProperties;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.SkullBlockEntityModel;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;

public class DeathCounter {
    private static final Identifier WIDGETS_TEXTURE = new Identifier(OneDeathBlock.MOD_ID, "textures/gui/widgets.png");

    public static void render(MinecraftClient client, PlayerProperties playerProperties, MatrixStack matrixStack, float tickDelta) {
        if (playerProperties.hasDeathsDisplay()) {
            playerProperties.getDeathToasts().forEach(blockToast -> {
                blockToast.render(client, matrixStack, tickDelta);
            });

            float width = (float) client.getWindow().getScaledWidth() * 0.01f;
            float height = (float) client.getWindow().getScaledHeight() * 0.92f;

            matrixStack.push();
            renderWidgets(matrixStack, (int) width, (int) height);

            DecimalFormat format = new DecimalFormat("#,###");
            Text text = Text.literal(format.format(playerProperties.getDeaths()));
            int textWidth = -client.textRenderer.getWidth(text) + 97;

            width = (float) client.getWindow().getScaledWidth() * 0.036f;
            height = (float) client.getWindow().getScaledHeight() * 0.905f;

            client.textRenderer.drawWithShadow(matrixStack, text, width + textWidth, height, 0xFFFFFF);
            matrixStack.pop();
        }
    }

    private static void renderWidgets(MatrixStack matrices, int x, int y) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WIDGETS_TEXTURE);
        DrawableHelper.drawTexture(matrices, x, y - 30, 113, 60, 0, 0, 1130, 600, 1130, 600);
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

    public static void renderSkull(@Nullable Direction direction, ItemStack stack, float yaw, float animationProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, SkullBlockEntityModel model, RenderLayer renderLayer) {
        matrices.push();
        if (direction == null) {
            matrices.translate(0.5, 0.0, 0.5);
        } else {
            float f = 0.25f;
            matrices.translate(0.5f - (float) direction.getOffsetX() * 0.25f, 0.25, 0.5f - (float) direction.getOffsetZ() * 0.25f);
        }
        matrices.scale(-1.0f, -1.0f, 1.0f);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);
        model.setHeadRotation(animationProgress, yaw, 0.0f);

        NbtCompound nbtCompound = stack.getOrCreateNbt();

        float red = 1f;
        float green = 1f;
        float blue = 1f;
        float alpha = nbtCompound.contains("alpha", NbtElement.FLOAT_TYPE) ? nbtCompound.getFloat("alpha") : 1f;

        if(nbtCompound.getInt("onedeathblock") < 0) {
            green = 0.33333f;
            blue = 0.33333f;
        }

        model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, red, green, blue, alpha);
        matrices.pop();
    }


}
