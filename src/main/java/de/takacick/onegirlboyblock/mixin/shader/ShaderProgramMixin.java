package de.takacick.onegirlboyblock.mixin.shader;

import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.ShaderProgramSetupView;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.resource.ResourceFactory;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShaderProgram.class)
public abstract class ShaderProgramMixin implements ShaderProgramSetupView, AutoCloseable {

    @Shadow
    public abstract @Nullable GlUniform getUniform(String name);

    @Unique
    @Nullable
    private GlUniform onegirlboyblock$bloodOverlayProgress;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(ResourceFactory factory, String name, VertexFormat format, CallbackInfo info) {
        this.onegirlboyblock$bloodOverlayProgress = this.getUniform("BloodOverlayProgress");
    }

    public void onegirlboyblock$setBloodOverlayProgress(float bloodOverlayProgress) {
        if (this.onegirlboyblock$bloodOverlayProgress != null) {
            this.onegirlboyblock$bloodOverlayProgress.set(bloodOverlayProgress);
        }
    }
}