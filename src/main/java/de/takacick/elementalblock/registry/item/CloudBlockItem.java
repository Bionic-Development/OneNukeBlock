package de.takacick.elementalblock.registry.item;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;

public class CloudBlockItem extends BlockItem implements Cloud {

    public CloudBlockItem(Block block, Settings settings) {
        super(block, settings);
    }
}
