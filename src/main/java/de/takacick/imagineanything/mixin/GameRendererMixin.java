package de.takacick.imagineanything.mixin;

import de.takacick.imagineanything.ImagineAnything;
import de.takacick.imagineanything.access.PlayerProperties;
import de.takacick.imagineanything.registry.ItemRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow
    @Final
    private ResourceManager resourceManager;


    private static final Identifier LASER_SHADER = new Identifier(ImagineAnything.MOD_ID, "shaders/post/iron_man_laser.json");

    @Shadow
    protected abstract void loadShader(Identifier id);

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    @Nullable
    public abstract ShaderEffect getShader();

    @Shadow
    public abstract void disableShader();

    @Shadow
    private boolean shadersEnabled;

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo info) {
        if (client.player != null && client.player.getEquippedStack(EquipmentSlot.CHEST).isOf(ItemRegistry.IRON_MAN_SUIT) && ((PlayerProperties) client.player).hasIronManLaser() && client.options.getPerspective().isFirstPerson()) {
            shadersEnabled = true;
            if (getShader() == null || !getShader().getName().equals(LASER_SHADER.toString())) {
                this.loadShader(LASER_SHADER);
            }
        } else if (getShader() != null && getShader().getName().equals(LASER_SHADER.toString())) {
            getShader().close();
            disableShader();
        }
    }
}
