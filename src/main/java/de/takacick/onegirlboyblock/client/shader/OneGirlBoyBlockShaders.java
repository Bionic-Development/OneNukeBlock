package de.takacick.onegirlboyblock.client.shader;

import net.minecraft.client.gl.ShaderProgram;
import org.jetbrains.annotations.Nullable;

public class OneGirlBoyBlockShaders {

    @Nullable
    public static ShaderProgram renderTypeBitCannonGlow;

    public static ShaderProgram getRenderTypeBitCannonGlow() {
        return renderTypeBitCannonGlow;
    }
}
