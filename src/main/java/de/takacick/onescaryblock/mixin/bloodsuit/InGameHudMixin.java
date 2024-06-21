package de.takacick.onescaryblock.mixin.bloodsuit;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import de.takacick.onescaryblock.access.BloodBorderProperties;
import de.takacick.onescaryblock.client.entity.feature.BloodBorderSuitFeatureRenderer;
import de.takacick.onescaryblock.registry.item.BloodBorderSuit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
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
    private static Identifier VIGNETTE_TEXTURE;

    @Shadow
    private int scaledWidth;

    @Shadow
    private int scaledHeight;

    @Inject(method = "renderVignetteOverlay", at = @At("HEAD"), cancellable = true)
    public void renderVignetteOverlay(DrawContext context, Entity entity, CallbackInfo info) {
        if (!(entity instanceof LivingEntity livingEntity && livingEntity.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof BloodBorderSuit)) {
            return;
        }

        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);

        float progress = 0f;
        if (entity instanceof BloodBorderProperties bloodBorderProperties) {
            float tickDelta = MinecraftClient.getInstance().isPaused() ? 0f : MinecraftClient.getInstance().getTickDelta();
            progress = bloodBorderProperties.getBloodBorderSuitHelper().getProgress(tickDelta);
        }

        float[] color = BloodBorderSuitFeatureRenderer.lerpColor(Vec3d.unpackRgb(0x820A0A).toVector3f(), Vec3d.unpackRgb(2138367).toVector3f(), progress);

        context.setShaderColor(0f, color[0], color[0], 1f);

        context.drawTexture(VIGNETTE_TEXTURE, 0, 0, -90, 0.0f, 0.0f, this.scaledWidth, this.scaledHeight, this.scaledWidth, this.scaledHeight);
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        context.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.defaultBlendFunc();
        info.cancel();
    }
}
