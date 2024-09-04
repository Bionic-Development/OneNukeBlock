package de.takacick.onenukeblock.client.bossbar.renderer;

import de.takacick.onenukeblock.OneNukeBlock;
import de.takacick.utils.bossbar.client.render.BossBarRenderContext;
import de.takacick.utils.bossbar.client.render.bossbar.BossBarRenderer;
import net.minecraft.util.Identifier;

public class NukeBossBarRenderer extends BossBarRenderer {

    private final static Identifier TEXTURE = Identifier.of(OneNukeBlock.MOD_ID, "boss_bar/nuke_progress");
    private final static Identifier BACKGROUND = Identifier.of(OneNukeBlock.MOD_ID, "boss_bar/nuke_background");

    public NukeBossBarRenderer() {
    }

    @Override
    public int getTextureHeight(BossBarRenderContext context) {
        return 11;
    }

    @Override
    public int getHeightOffset(BossBarRenderContext bossBarRenderContext) {
        return super.getHeightOffset(bossBarRenderContext) + 3;
    }

    @Override
    public Identifier getTexture(BossBarRenderContext bossBarRenderContext) {
        return TEXTURE;
    }

    @Override
    public Identifier getBackgroundTexture(BossBarRenderContext bossBarRenderContext) {
        return BACKGROUND;
    }
}
