package de.takacick.illegalwars.client.shaders;

import net.minecraft.client.gl.ShaderProgram;
import org.jetbrains.annotations.Nullable;

public class IllegalWarsShaders {

    @Nullable
    public static ShaderProgram renderTypeEntityTranslucentCullProgram;

    public static @Nullable ShaderProgram getRenderTypeEntityTranslucentCullProgram() {
        return renderTypeEntityTranslucentCullProgram;
    }

}
