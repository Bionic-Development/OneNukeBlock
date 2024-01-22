package de.takacick.illegalwars.mixin.sludge;

import de.takacick.illegalwars.access.ShaderProgramProperties;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.ShaderProgramSetupView;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.resource.ResourceFactory;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShaderProgram.class)
@Implements({@Interface(iface = ShaderProgramProperties.class, prefix = "illegalwars$")})
public abstract class ShaderProgramMixin implements ShaderProgramSetupView, AutoCloseable {

    @Shadow
    public abstract @Nullable GlUniform getUniform(String name);

    @Unique
    @Nullable
    private GlUniform cameraPos;

    @Unique
    @Nullable
    private GlUniform illegalwars$sludgeStrength;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(ResourceFactory factory, String name, VertexFormat format, CallbackInfo info) {
        this.cameraPos = this.getUniform("CameraPos");
        this.illegalwars$sludgeStrength = this.getUniform("SludgeStrength");
    }

    @Inject(method = "bind", at = @At("HEAD"))
    private void bind(CallbackInfo info) {
        if (this.cameraPos != null && MinecraftClient.getInstance().player != null) {
            Vector3f pos = MinecraftClient.getInstance().gameRenderer.getCamera().getPos().toVector3f();
            float[] arr = {pos.x(), pos.y(), pos.z()};
            this.cameraPos.set(arr);
        }
    }

    public void illegalwars$setSludgeStrength(float sludgeStrength) {
        if (this.illegalwars$sludgeStrength != null) {
            this.illegalwars$sludgeStrength.set(sludgeStrength);
        }
    }
}