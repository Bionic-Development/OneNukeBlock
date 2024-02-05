package de.takacick.secretcraftbase.client.shaders;

import net.minecraft.client.gl.ShaderProgram;
import org.jetbrains.annotations.Nullable;

public class SecretCraftBaseShaders {

    @Nullable
    public static ShaderProgram renderTypeEntityNetherPortalProgram;

    @Nullable
    public static ShaderProgram renderTypeXPProgram;

    public static @Nullable ShaderProgram getRenderTypeEntityNetherPortalProgram() {
        return renderTypeEntityNetherPortalProgram;
    }

    public static @Nullable ShaderProgram getRenderTypeXPProgram() {
        return renderTypeXPProgram;
    }

}
