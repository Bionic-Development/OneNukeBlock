package de.takacick.onegirlboyblock.utils.data;

import de.takacick.onegirlboyblock.OneGirlBoyBlock;
import de.takacick.onegirlboyblock.utils.data.attachments.BaseballBatAnimationHelper;
import de.takacick.onegirlboyblock.utils.data.attachments.SizeHelper;
import de.takacick.onegirlboyblock.utils.data.attachments.TetrisDamageHelper;
import de.takacick.utils.data.codec.NbtCodecs;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.impl.attachment.AttachmentRegistryImpl;
import net.minecraft.util.Identifier;

public class AttachmentTypes {

    public static final AttachmentType<SizeHelper> SIZE_HELPER = AttachmentRegistryImpl.<SizeHelper>builder()
            .persistent(NbtCodecs.createCodec(SizeHelper::from))
            .initializer(SizeHelper::new)
            .buildAndRegister(Identifier.of(OneGirlBoyBlock.MOD_ID, "size_helper"));
    public static final AttachmentType<BaseballBatAnimationHelper> BASEBALL_BAT = AttachmentRegistryImpl.<BaseballBatAnimationHelper>builder()
            .persistent(NbtCodecs.createCodec(BaseballBatAnimationHelper::from))
            .initializer(BaseballBatAnimationHelper::new)
            .buildAndRegister(Identifier.of(OneGirlBoyBlock.MOD_ID, "baseball_bat"));
    public static final AttachmentType<TetrisDamageHelper> TETRIS_DAMAGE_HELPER = AttachmentRegistryImpl.<TetrisDamageHelper>builder()
            .persistent(NbtCodecs.createCodec(TetrisDamageHelper::from))
            .initializer(TetrisDamageHelper::new)
            .buildAndRegister(Identifier.of(OneGirlBoyBlock.MOD_ID, "tetris_damage_helper"));
    public static void register() {

    }
}
