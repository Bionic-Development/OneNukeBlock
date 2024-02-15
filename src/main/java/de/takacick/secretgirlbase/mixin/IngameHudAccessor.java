package de.takacick.secretgirlbase.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(InGameHud.class)
public interface IngameHudAccessor {

    @Accessor("lastHealthValue")
    void setLastHealthValue(int lastHealthValue);

    @Accessor("renderHealthValue")
    void setRenderHealthValue(int renderHealthValue);

}