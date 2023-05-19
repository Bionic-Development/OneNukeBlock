package de.takacick.heartmoney.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Shadow
    protected abstract PlayerEntity getCameraPlayer();

    @Redirect(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getAbsorptionAmount()F"))
    private float modifyAbsorption(PlayerEntity instance) {
        PlayerEntity playerEntity = this.getCameraPlayer();
        if (playerEntity.getHealth() > 500) {
            return 0f;
        }

        return playerEntity.getAbsorptionAmount();
    }

    @Redirect(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/entity/attribute/EntityAttribute;)D"))
    private double modifyHealth(PlayerEntity instance, EntityAttribute entityAttribute) {
        if (instance.getHealth() > 500) {
            return 10d;
        }

        return (float) instance.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH);
    }

    @Redirect(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(II)I", ordinal = 0))
    public int modifyRenderHealth(int a, int b) {
        PlayerEntity playerEntity = this.getCameraPlayer();
        if (playerEntity.getHealth() > 500) {
            return 10;
        }

        return Math.max(a, b);
    }
}