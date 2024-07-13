package de.takacick.onegirlboyblock.registry.block;

import com.mojang.serialization.MapCodec;
import de.takacick.onegirlboyblock.utils.oneblock.OneBlock;
import de.takacick.onegirlboyblock.utils.oneblock.OneBlockServerState;

public class OneBoyBlock extends AbstractOneBlock {

    public static final MapCodec<OneBoyBlock> CODEC = OneBoyBlock.createCodec(OneBoyBlock::new);

    public OneBoyBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends OneBoyBlock> getCodec() {
        return CODEC;
    }

    @Override
    public OneBlock getOneBlock(OneBlockServerState oneBlockServerState) {
        return oneBlockServerState.getBoyOneBlock();
    }
}
