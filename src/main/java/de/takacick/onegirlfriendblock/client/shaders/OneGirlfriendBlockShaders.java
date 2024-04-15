package de.takacick.onegirlfriendblock.client.shaders;

import net.minecraft.client.gl.ShaderProgram;
import org.jetbrains.annotations.Nullable;

public class OneGirlfriendBlockShaders {

    @Nullable
    public static ShaderProgram renderTypeBubbleGumProgram;

    public static @Nullable ShaderProgram getRenderTypeBubbleGumProgram() {
        return renderTypeBubbleGumProgram;
    }

}
