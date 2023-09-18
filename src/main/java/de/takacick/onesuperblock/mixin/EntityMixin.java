package de.takacick.onesuperblock.mixin;

import de.takacick.onesuperblock.access.ItemProperties;
import de.takacick.utils.BionicUtilsClient;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public int age;

    @Shadow
    private int id;

    @Inject(method = "getTeamColorValue", at = @At("HEAD"), cancellable = true)
    public void getTeamColorValue(CallbackInfoReturnable<Integer> info) {
        if (this instanceof ItemProperties itemProperties
                && itemProperties.isRainbow()) {
            info.setReturnValue(BionicUtilsClient.getRainbow().getColorAsInt(this.age + this.id * 601));
        }
    }
}

