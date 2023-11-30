package de.takacick.elementalblock.registry.block;

import de.takacick.elementalblock.OneElementalBlock;
import de.takacick.utils.bossbar.BossBarUtils;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.util.Identifier;

public class FireElementalBlock extends ElementalBlock {

    public static final BossBar.Color COLOR = BossBarUtils.register(new Identifier(OneElementalBlock.MOD_ID, "fire_elemental"))
            .texture(new Identifier(OneElementalBlock.MOD_ID, "textures/gui/fire_elemental_bar.png"), 256, 10).build();

    public FireElementalBlock(Settings settings) {
        super(settings);
    }

    @Override
    public String getElement() {
        return "Fire";
    }

    @Override
    public BossBar.Color getBossBarColor() {
        return COLOR;
    }
}
