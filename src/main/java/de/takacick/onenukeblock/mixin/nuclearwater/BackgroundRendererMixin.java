package de.takacick.onenukeblock.mixin.nuclearwater;

import com.llamalad7.mixinextras.sugar.Local;
import de.takacick.onenukeblock.registry.block.fluid.NuclearWaterFluid;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/BackgroundRenderer;getFogModifier(Lnet/minecraft/entity/Entity;F)Lnet/minecraft/client/render/BackgroundRenderer$StatusEffectFogModifier;"))
    private static void render(Camera camera, float tickDelta, ClientWorld world, int viewDistance, float skyDarkness, CallbackInfo info) {
        CameraSubmersionType cameraSubmersionType = camera.getSubmersionType();
        if (cameraSubmersionType == CameraSubmersionType.WATER) {
            BlockState blockState = world.getBlockState(BlockPos.ofFloored(camera.getPos()));

            if (blockState.getFluidState().isIn(NuclearWaterFluid.NUCLEAR_WATER)) {
                red = NuclearWaterFluid.COLOR.x();
                green = NuclearWaterFluid.COLOR.y();
                blue = NuclearWaterFluid.COLOR.z();
            }
        }
    }

    @Inject(method = "applyFog", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogStart(F)V", shift = At.Shift.BEFORE))
    private static void applyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo info, @Local BackgroundRenderer.FogData fogData) {
        CameraSubmersionType cameraSubmersionType = camera.getSubmersionType();
        if (cameraSubmersionType == CameraSubmersionType.WATER) {
            Entity entity = camera.getFocusedEntity();
            World world = entity.getWorld();
            BlockState blockState = world.getBlockState(BlockPos.ofFloored(camera.getPos()));

            if (blockState.getFluidState().isIn(NuclearWaterFluid.NUCLEAR_WATER)) {
                if (entity.isSpectator()) {
                    fogData.fogStart = -8.0F;
                    fogData.fogEnd = viewDistance * 0.5F;
                } else {
                    fogData.fogStart = 0.25F;
                    fogData.fogEnd = 2.0F;
                }
            }
        }
    }
}