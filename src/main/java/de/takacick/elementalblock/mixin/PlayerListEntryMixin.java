package de.takacick.elementalblock.mixin;

import de.takacick.elementalblock.access.PlayerListProperties;
import de.takacick.elementalblock.client.renderer.LavaBionicEntityRenderer;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListEntry.class)
@Implements({@Interface(iface = PlayerListProperties.class, prefix = "elementalblock$")})
public abstract class PlayerListEntryMixin {

    private boolean elementalblock$lavaBionic = false;

    @Inject(method = "getSkinTexture", at = @At("HEAD"), cancellable = true)
    public void getSkinTexture(CallbackInfoReturnable<Identifier> info) {
        if (elementalblock$isLavaBionic()) {
            info.setReturnValue(LavaBionicEntityRenderer.TEXTURE);
        }
    }

    public void elementalblock$setLavaBionic(boolean lavaBionic) {
        this.elementalblock$lavaBionic = lavaBionic;
    }

    public boolean elementalblock$isLavaBionic() {
        return this.elementalblock$lavaBionic;
    }

}
