package de.takacick.onescaryblock.client.shader;

import net.minecraft.client.gl.ShaderProgram;
import org.jetbrains.annotations.Nullable;

public class OneScaryBlockShaders {

    @Nullable
    public static ShaderProgram renderTypeEntityOverlay;

    @Nullable
    public static ShaderProgram renderTypeSoulFlame;

    public static ShaderProgram getRenderTypeEntityOverlay() {
        return renderTypeEntityOverlay;
    }

    public static ShaderProgram getRenderTypeSoulFlame() {
        return renderTypeSoulFlame;
    }
}
