package de.takacick.onescaryblock.mixin.shader;

import de.takacick.onescaryblock.access.ShaderProgramProperties;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.ShaderProgramSetupView;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.resource.ResourceFactory;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShaderProgram.class)
@Implements({@Interface(iface = ShaderProgramProperties.class, prefix = "onescaryblock$")})
public abstract class ShaderProgramMixin implements ShaderProgramSetupView, AutoCloseable {

    @Shadow
    public abstract @Nullable GlUniform getUniform(String name);

    @Unique
    @Nullable
    private GlUniform onescaryblock$bloodOverlayProgress;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(ResourceFactory factory, String name, VertexFormat format, CallbackInfo info) {
        this.onescaryblock$bloodOverlayProgress = this.getUniform("BloodOverlayProgress");
    }

    public void onescaryblock$setBloodOverlayProgress(float bloodOverlayProgress) {
        if (this.onescaryblock$bloodOverlayProgress != null) {
            this.onescaryblock$bloodOverlayProgress.set(bloodOverlayProgress);
        }
    }
}