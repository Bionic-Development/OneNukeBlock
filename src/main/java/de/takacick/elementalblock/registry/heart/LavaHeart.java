package de.takacick.elementalblock.registry.heart;

import de.takacick.utils.heart.Heart;
import net.minecraft.util.Identifier;

public class LavaHeart extends Heart {

    private int step = 1;

    public LavaHeart(Identifier identifier, boolean blinking, int textureScale) {
        super(identifier, blinking, textureScale);
    }

    @Override
    public int getU(boolean halfHeart, boolean blinking) {
        return super.getU(halfHeart, blinking);
    }

    @Override
    public int getV() {
        return 18 * step;
    }

    @Override
    public int getWidth() {
        return 72;
    }

    @Override
    public int getHeight() {
        return 576;
    }

    @Override
    public int getRegionHeight() {
        return 18;
    }

    @Override
    public int getRegionWidth() {
        return 18;
    }

    @Override
    public boolean useHeartContainer() {
        return true;
    }

    @Override
    public boolean isAnimated() {
        return true;
    }

    @Override
    public void step() {
        step++;
    }
}

