package de.takacick.secretcraftbase.registry.entity.custom.renderer;

import de.takacick.secretcraftbase.SecretCraftBase;
import de.takacick.secretcraftbase.registry.entity.custom.ArmoryRoomEntity;
import de.takacick.secretcraftbase.registry.entity.custom.model.ArmoryRoomEntityModel;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class ArmoryRoomEntityRenderer extends AbstractSchematicEntityRenderer<ArmoryRoomEntity> {

    private static final Identifier TEXTURE = new Identifier(SecretCraftBase.MOD_ID, "textures/entity/armory_room.png");
    private final ArmoryRoomEntityModel armoryRoomEntityModel;

    public ArmoryRoomEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.armoryRoomEntityModel = new ArmoryRoomEntityModel(ArmoryRoomEntityModel.getTexturedModelData().createModel());
    }

    @Override
    public void render(ArmoryRoomEntity armoryRoomEntity, float f, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();

        matrixStack.translate(0, 1.501f, 0);
        matrixStack.scale(-1, -1, 1);

        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(armoryRoomEntity.getRotation(tickDelta)));

        RenderLayer renderLayer = this.armoryRoomEntityModel.getLayer(getTexture(armoryRoomEntity));

        this.armoryRoomEntityModel.render(matrixStack, vertexConsumerProvider.getBuffer(renderLayer), i, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
        matrixStack.pop();

        super.render(armoryRoomEntity, f, tickDelta, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(ArmoryRoomEntity armoryRoomEntity) {
        return TEXTURE;
    }
}

