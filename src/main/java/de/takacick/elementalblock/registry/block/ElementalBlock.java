package de.takacick.elementalblock.registry.block;

import net.minecraft.block.Block;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.text.Text;

public abstract class ElementalBlock extends Block {

    protected int requiredBlocks = 100;

    public ElementalBlock(Settings settings) {
        super(settings);
    }

    public int getRequiredBlocks() {
        return requiredBlocks;
    }

    public void setRequiredBlocks(int requiredBlocks) {
        this.requiredBlocks = requiredBlocks;
    }

    public Text getBossBarTitle() {
        return Text.of("Progress: ");
    }

    public BossBar.Color getBossBarColor() {
        return BossBar.Color.WHITE;
    }

    public abstract String getElement();
}
