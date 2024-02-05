package de.takacick.secretcraftbase.registry.entity.custom.renderer;

import de.takacick.secretcraftbase.SecretCraftBase;
import de.takacick.secretcraftbase.registry.entity.custom.IronGolemFarmEntity;
import de.takacick.secretcraftbase.registry.entity.custom.model.IronGolemFarmEntityModel;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class IronGolemFarmEntityRenderer extends AbstractSchematicEntityRenderer<IronGolemFarmEntity> {

    private static final Identifier TEXTURE = new Identifier(SecretCraftBase.MOD_ID, "textures/entity/iron_golem_farm.png");
    private final IronGolemFarmEntityModel ironGolemFarmEntityModel;

    public IronGolemFarmEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.ironGolemFarmEntityModel = new IronGolemFarmEntityModel(IronGolemFarmEntityModel.getTexturedModelData().createModel());
    }

    @Override
    public void render(IronGolemFarmEntity ironGolemFarmEntity, float f, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();

        matrixStack.translate(0, 1.501f, 0);
        matrixStack.scale(-1, -1, 1);

        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(ironGolemFarmEntity.getRotation(tickDelta)));

        RenderLayer renderLayer = this.ironGolemFarmEntityModel.getLayer(getTexture(ironGolemFarmEntity));

        this.ironGolemFarmEntityModel.render(matrixStack, vertexConsumerProvider.getBuffer(renderLayer), i, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
        matrixStack.pop();

        super.render(ironGolemFarmEntity, f, tickDelta, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(IronGolemFarmEntity ironGolemFarmEntity) {
        return TEXTURE;
    }
}

