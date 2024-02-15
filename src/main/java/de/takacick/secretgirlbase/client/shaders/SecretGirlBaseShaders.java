package de.takacick.secretgirlbase.client.shaders;

import net.minecraft.client.gl.ShaderProgram;
import org.jetbrains.annotations.Nullable;

public class SecretGirlBaseShaders {

    @Nullable
    public static ShaderProgram renderTypeBubbleGumProgram;

    @Nullable
    public static ShaderProgram renderTypePoppyTranslucentProgram;

    public static @Nullable ShaderProgram getRenderTypeBubbleGumProgram() {
        return renderTypeBubbleGumProgram;
    }

    public static @Nullable ShaderProgram getRenderTypePoppyTranslucentProgram() {
        return renderTypePoppyTranslucentProgram;
    }
}
