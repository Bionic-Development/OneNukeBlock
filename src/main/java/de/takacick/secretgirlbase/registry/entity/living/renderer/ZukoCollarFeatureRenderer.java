package de.takacick.secretgirlbase.registry.entity.living.renderer;

import de.takacick.secretgirlbase.SecretGirlBase;
import de.takacick.secretgirlbase.registry.entity.living.ZukoEntity;
import de.takacick.secretgirlbase.registry.entity.living.model.ZukoEntityModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(value = EnvType.CLIENT)
public class ZukoCollarFeatureRenderer
        extends FeatureRenderer<ZukoEntity, ZukoEntityModel<ZukoEntity>> {
    private static final Identifier SKIN = new Identifier(SecretGirlBase.MOD_ID, "textures/entity/zuko_collar.png");
    private final ZukoEntityModel<ZukoEntity> model;

    public ZukoCollarFeatureRenderer(FeatureRendererContext<ZukoEntity, ZukoEntityModel<ZukoEntity>> context) {
        super(context);
        this.model = new ZukoEntityModel<>(ZukoEntityModel.getTexturedModelData().createModel());
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, ZukoEntity zukoEntity, float f, float g, float h, float j, float k, float l) {
        float[] fs = zukoEntity.getCollarColor().getColorComponents();
        ZukoCollarFeatureRenderer.render(this.getContextModel(), this.model, SKIN,
                matrixStack, vertexConsumerProvider, i, zukoEntity, f, g, j, k, l, h, fs[0], fs[1], fs[2]);
    }
}
