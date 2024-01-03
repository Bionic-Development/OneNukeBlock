package de.takacick.raidbase.client.shaders;

import net.minecraft.client.gl.ShaderProgram;
import org.jetbrains.annotations.Nullable;

public class RaidBaseShaders {

    @Nullable
    public static ShaderProgram renderTypeSolidProgram;

    @Nullable
    public static ShaderProgram renderTypeEntityTranslucentCullProgram;

    @Nullable
    public static ShaderProgram renderTypeItemEntityTranslucentCullProgram;

    @Nullable
    public static ShaderProgram renderTypeGlitchyItemEntityTranslucentCullProgram;

    public static @Nullable ShaderProgram getRenderTypeSolidProgram() {
        return renderTypeSolidProgram;
    }

    public static @Nullable ShaderProgram getRenderTypeEntityTranslucentCullProgram() {
        return renderTypeEntityTranslucentCullProgram;
    }

    public static @Nullable ShaderProgram getRenderTypeItemEntityTranslucentCullProgram() {
        return renderTypeItemEntityTranslucentCullProgram;
    }

    public static @Nullable ShaderProgram getRenderTypeGlitchyItemEntityTranslucentCullProgram() {
        return renderTypeGlitchyItemEntityTranslucentCullProgram;
    }
}
