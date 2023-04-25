package de.takacick.everythinghearts.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import de.takacick.everythinghearts.access.PlayerProperties;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    @Final
    private static Identifier VIGNETTE_TEXTURE;

    @Shadow
    private int scaledHeight;

    @Shadow
    private int scaledWidth;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;isFancyGraphicsOrBetter()Z", shift = At.Shift.BEFORE))
    public void render(MatrixStack matrices, float tickDelta, CallbackInfo info) {
        if (client.player != null) {
            renderHeartTransformVignette();
        }
    }

    private void renderHeartTransformVignette() {
        if (client.player instanceof PlayerProperties playerProperties && playerProperties.getHeartTransformTicks() > 0) {
            float f = Math.min((200 - MathHelper.lerp(client.getTickDelta(),
                    playerProperties.getHeartTransformTicks() + 1, playerProperties.getHeartTransformTicks())) / 20, 1f);

            if (f > 0.0f) {
                RenderSystem.disableDepthTest();
                RenderSystem.depthMask(false);
                RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);

                f = MathHelper.clamp(f, 0.0f, 1.0f);
                RenderSystem.setShaderColor(0, f, f, 1.0f);

                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderTexture(0, VIGNETTE_TEXTURE);
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferBuilder = tessellator.getBuffer();
                bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
                bufferBuilder.vertex(0.0, this.scaledHeight, -90.0).texture(0.0f, 1.0f).next();
                bufferBuilder.vertex(this.scaledWidth, this.scaledHeight, -90.0).texture(1.0f, 1.0f).next();
                bufferBuilder.vertex(this.scaledWidth, 0.0, -90.0).texture(1.0f, 0.0f).next();
                bufferBuilder.vertex(0.0, 0.0, -90.0).texture(0.0f, 0.0f).next();
                tessellator.draw();
                RenderSystem.depthMask(true);
                RenderSystem.enableDepthTest();
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                RenderSystem.defaultBlendFunc();
            }
        }
    }
}