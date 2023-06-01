package de.takacick.deathmoney.mixin;

import com.mojang.datafixers.util.Pair;
import de.takacick.deathmoney.access.PlayerProperties;
import de.takacick.deathmoney.client.CustomLayers;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Shader;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow
    public abstract Camera getCamera();

    @Shadow
    @Final
    private ResourceManager resourceManager;

    @Redirect(method = "loadShaders", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 1))
    <E> boolean loadShaders(List instance, E e) {
        try {
            ((ArrayList<Pair<Shader, Consumer<Shader>>>) instance).add(Pair.of(new Shader(resourceManager, "rendertype_blackmatter",
                    VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL), shader -> CustomLayers.BLACK_MATTER_SHADER = shader));
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return instance.add(e);
    }

    @Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lnet/minecraft/util/math/Quaternion;)V", ordinal = 3, shift = At.Shift.AFTER))
    public void renderWorld(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo info) {
        if (getCamera().getFocusedEntity() instanceof PlayerProperties playerProperties && playerProperties.getMeteorShakeTicks() > 0) {
            float g = (float) (Math.cos((double) getCamera().getFocusedEntity().age * 3.25) * Math.PI * (double) 0.4f);
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(g));
        }
    }
}