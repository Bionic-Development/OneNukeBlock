package de.takacick.onenukeblock.utils.data;

import de.takacick.onenukeblock.OneNukeBlock;
import de.takacick.onenukeblock.utils.data.attachments.BangMaceAnimationHelper;
import de.takacick.onenukeblock.utils.data.attachments.BezierCurve;
import de.takacick.onenukeblock.utils.data.attachments.ExplosiveGummyBearAnimationHelper;
import de.takacick.onenukeblock.utils.data.attachments.NuclearMutations;
import de.takacick.utils.data.codec.NbtCodecs;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.impl.attachment.AttachmentRegistryImpl;
import net.minecraft.util.Identifier;

public class AttachmentTypes {

    public static final AttachmentType<ExplosiveGummyBearAnimationHelper> EXPLOSIVE_GUMMY_BEAR = AttachmentRegistryImpl.<ExplosiveGummyBearAnimationHelper>builder()
            .persistent(NbtCodecs.createCodec(ExplosiveGummyBearAnimationHelper::from))
            .initializer(ExplosiveGummyBearAnimationHelper::new)
            .buildAndRegister(Identifier.of(OneNukeBlock.MOD_ID, "explosive_gummy_bear"));
    public static final AttachmentType<BangMaceAnimationHelper> BANG_MACE = AttachmentRegistryImpl.<BangMaceAnimationHelper>builder()
            .persistent(NbtCodecs.createCodec(BangMaceAnimationHelper::from))
            .initializer(BangMaceAnimationHelper::new)
            .buildAndRegister(Identifier.of(OneNukeBlock.MOD_ID, "bang_mace"));
    public static final AttachmentType<BezierCurve> BEZIER_CURVE = AttachmentRegistryImpl.<BezierCurve>builder()
            .persistent(NbtCodecs.createCodec(BezierCurve::from))
            .initializer(BezierCurve::new)
            .buildAndRegister(Identifier.of(OneNukeBlock.MOD_ID, "bezier_curve"));
    public static final AttachmentType<NuclearMutations> NUCLEAR_MUTATIONS = AttachmentRegistryImpl.<NuclearMutations>builder()
            .persistent(NbtCodecs.createCodec(NuclearMutations::from))
            .initializer(NuclearMutations::new)
            .buildAndRegister(Identifier.of(OneNukeBlock.MOD_ID, "nuclear_mutation"));

    public static void register() {

    }
}
