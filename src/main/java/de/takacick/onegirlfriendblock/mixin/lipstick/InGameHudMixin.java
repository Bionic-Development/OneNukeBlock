package de.takacick.onegirlfriendblock.mixin.lipstick;

import de.takacick.onegirlfriendblock.OneGirlfriendBlock;
import de.takacick.onegirlfriendblock.access.LivingProperties;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    protected abstract void renderOverlay(DrawContext context, Identifier texture, float opacity);

    @Unique
    private static final Identifier onegirlfriendblock$BUBBLE_GUM_OVERLAY = new Identifier(OneGirlfriendBlock.MOD_ID, "textures/misc/bubble_gum_overlay.png");

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getFrozenTicks()I"))
    private void render(DrawContext context, float tickDelta, CallbackInfo info) {
        if (this.client.options.getPerspective().isFirstPerson()) {
            if (this.client.player instanceof LivingProperties livingProperties && livingProperties.getLipstickStrength() > 0f) {
                this.renderOverlay(context, onegirlfriendblock$BUBBLE_GUM_OVERLAY, livingProperties.getLipstickStrength());
            }
        }
    }
}