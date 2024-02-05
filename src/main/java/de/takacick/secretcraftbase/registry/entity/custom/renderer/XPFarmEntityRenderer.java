package de.takacick.secretcraftbase.registry.entity.custom.renderer;

import de.takacick.secretcraftbase.SecretCraftBase;
import de.takacick.secretcraftbase.registry.entity.custom.XPFarmEntity;
import de.takacick.secretcraftbase.registry.entity.custom.model.XPFarmEntityModel;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class XPFarmEntityRenderer extends AbstractSchematicEntityRenderer<XPFarmEntity> {

    private static final Identifier TEXTURE = new Identifier(SecretCraftBase.MOD_ID, "textures/entity/xp_farm.png");
    private final XPFarmEntityModel xpFarmEntityModel;

    public XPFarmEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.xpFarmEntityModel = new XPFarmEntityModel(XPFarmEntityModel.getTexturedModelData().createModel());
    }

    @Override
    public void render(XPFarmEntity xpFarmEntity, float f, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();

        matrixStack.translate(0, 1.501f, 0);
        matrixStack.scale(-1, -1, 1);

        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(xpFarmEntity.getRotation(tickDelta)));

        RenderLayer renderLayer = this.xpFarmEntityModel.getLayer(getTexture(xpFarmEntity));

        this.xpFarmEntityModel.setAngles(xpFarmEntity, 0f, 0, xpFarmEntity.age + tickDelta, 0f, 0f);
        this.xpFarmEntityModel.render(matrixStack, vertexConsumerProvider.getBuffer(renderLayer), i, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
        matrixStack.pop();

        super.render(xpFarmEntity, f, tickDelta, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(XPFarmEntity xpFarmEntity) {
        return TEXTURE;
    }
}

