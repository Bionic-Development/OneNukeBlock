package de.takacick.deathmoney.mixin;

import de.takacick.deathmoney.registry.ItemRegistry;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackgroundRenderer.class)
public abstract class BackgroundRendererMixin {

    @Shadow
    private static float red;

    @Shadow
    private static float green;

    @Shadow
    private static float blue;

    @Shadow
    private static long lastWaterFogColorUpdateTime;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/BackgroundRenderer;getFogModifier(Lnet/minecraft/entity/Entity;F)Lnet/minecraft/client/render/BackgroundRenderer$StatusEffectFogModifier;"))
    private static void render(Camera camera, float tickDelta, ClientWorld world, int viewDistance, float skyDarkness, CallbackInfo info) {
        CameraSubmersionType cameraSubmersionType = camera.getSubmersionType();
        if (cameraSubmersionType == CameraSubmersionType.WATER && world.getBlockState(new BlockPos(camera.getPos())).isOf(ItemRegistry.BLOOD_BLOCK)) {
            red = 0.5294117647f;
            green = 0.03921568627f;
            blue = 0.03921568627f;
            lastWaterFogColorUpdateTime = -1L;
        }
    }
}