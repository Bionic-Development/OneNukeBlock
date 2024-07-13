package de.takacick.onegirlboyblock.registry.block;

import com.mojang.serialization.MapCodec;
import de.takacick.onegirlboyblock.utils.oneblock.OneBlock;
import de.takacick.onegirlboyblock.utils.oneblock.OneBlockServerState;

public class OneGirlBlock extends AbstractOneBlock {

    public static final MapCodec<OneGirlBlock> CODEC = OneGirlBlock.createCodec(OneGirlBlock::new);

    public OneGirlBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends OneGirlBlock> getCodec() {
        return CODEC;
    }

    @Override
    public OneBlock getOneBlock(OneBlockServerState oneBlockServerState) {
        return oneBlockServerState.getGirlOneBlock();
    }
}
