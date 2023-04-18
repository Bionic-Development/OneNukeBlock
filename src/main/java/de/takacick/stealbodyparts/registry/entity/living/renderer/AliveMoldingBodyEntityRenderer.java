package de.takacick.stealbodyparts.registry.entity.living.renderer;

import de.takacick.stealbodyparts.StealBodyParts;
import de.takacick.stealbodyparts.registry.entity.custom.renderer.AliveMoldingBodyFeatureRenderer;
import de.takacick.stealbodyparts.registry.entity.living.AliveMoldingBodyEntity;
import de.takacick.stealbodyparts.registry.entity.living.model.AliveMoldingBodyEntityModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class AliveMoldingBodyEntityRenderer extends MobEntityRenderer<AliveMoldingBodyEntity, AliveMoldingBodyEntityModel<AliveMoldingBodyEntity>> {

    private static final Identifier TEXTURE = new Identifier(StealBodyParts.MOD_ID, "textures/entity/alive_molded_boss.png");

    public AliveMoldingBodyEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new AliveMoldingBodyEntityModel<>(AliveMoldingBodyEntityModel.getTexturedModelData().createModel()), 0.4f);

        this.addFeature(new AliveMoldingBodyFeatureRenderer<>(this));
    }

    @Override
    protected void scale(AliveMoldingBodyEntity aliveMoldingBodyEntity, MatrixStack matrices, float amount) {
        matrices.scale(0.9375f, 0.9375f, 0.9375f);
        super.scale(aliveMoldingBodyEntity, matrices, amount);
    }

    @Override
    public Identifier getTexture(AliveMoldingBodyEntity aliveMoldingBodyEntity) {
        return TEXTURE;
    }
}

