package de.takacick.tinyhouse.mixin;

import de.takacick.tinyhouse.access.EntityProperties;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameOverlayRenderer.class)
public abstract class InGameOverlayRendererMixin {

    @Shadow
    protected static void renderInWallOverlay(Sprite sprite, MatrixStack matrices) {
    }

    @Inject(method = "renderOverlays", at = @At("HEAD"), cancellable = true)
    private static void renderOverlays(MinecraftClient client, MatrixStack matrices, CallbackInfo info) {
        ClientPlayerEntity playerEntity = client.player;

        if (playerEntity instanceof EntityProperties entityProperties && entityProperties.isStuckInsidePiston()) {
            renderInWallOverlay(client.getBlockRenderManager().getModels().getModelParticleSprite(Blocks.PISTON.getDefaultState()), matrices);
            info.cancel();
        }
    }
}
