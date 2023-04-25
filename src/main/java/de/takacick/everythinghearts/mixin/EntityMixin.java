package de.takacick.everythinghearts.mixin;

import de.takacick.everythinghearts.access.LivingProperties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public abstract EntityType<?> getType();

    @Inject(method = "getName", at = @At(value = "RETURN"), cancellable = true)
    public void getName(CallbackInfoReturnable<Text> info) {
        if (this instanceof LivingProperties livingProperties && livingProperties.isHeartInfected()) {
            info.setReturnValue(getType().equals(EntityType.CREEPER) ? Text.literal("Lover Creeper")
                    : getType().equals(EntityType.IRON_GOLEM) ? Text.literal("Heart Golem")
                    : getType().equals(EntityType.PILLAGER) ? Text.literal("Heartager") : Text.literal("Heart ").append(info.getReturnValue()));
        }
    }
}