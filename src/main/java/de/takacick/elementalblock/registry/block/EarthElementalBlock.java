package de.takacick.elementalblock.registry.block;

import de.takacick.elementalblock.OneElementalBlock;
import de.takacick.utils.bossbar.BossBarUtils;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.util.Identifier;

public class EarthElementalBlock extends ElementalBlock {

    public static final BossBar.Color COLOR = BossBarUtils.register(new Identifier(OneElementalBlock.MOD_ID, "earth_elemental"))
            .texture(new Identifier(OneElementalBlock.MOD_ID, "textures/gui/earth_elemental_bar.png"), 256, 10).build();

    public EarthElementalBlock(Settings settings) {
        super(settings);
    }

    @Override
    public String getElement() {
        return "Earth";
    }

    @Override
    public BossBar.Color getBossBarColor() {
        return COLOR;
    }
}
