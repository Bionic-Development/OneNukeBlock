package de.takacick.secretcraftbase.registry.entity.custom.renderer;

import de.takacick.secretcraftbase.SecretCraftBase;
import de.takacick.secretcraftbase.registry.entity.custom.TreasuryRoomEntity;
import de.takacick.secretcraftbase.registry.entity.custom.model.TreasuryRoomEntityModel;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class TreasuryRoomEntityRenderer extends AbstractSchematicEntityRenderer<TreasuryRoomEntity> {

    private static final Identifier TEXTURE = new Identifier(SecretCraftBase.MOD_ID, "textures/entity/treasury_room.png");
    private final TreasuryRoomEntityModel treasuryRoomEntityModel;

    public TreasuryRoomEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.treasuryRoomEntityModel = new TreasuryRoomEntityModel(TreasuryRoomEntityModel.getTexturedModelData().createModel());
    }

    @Override
    public void render(TreasuryRoomEntity treasuryRoomEntity, float f, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();

        matrixStack.translate(0, 1.501f, 0);
        matrixStack.scale(-1, -1, 1);

        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(treasuryRoomEntity.getRotation(tickDelta)));

        RenderLayer renderLayer = this.treasuryRoomEntityModel.getLayer(getTexture(treasuryRoomEntity));

        this.treasuryRoomEntityModel.render(matrixStack, vertexConsumerProvider.getBuffer(renderLayer), i, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
        matrixStack.pop();

        super.render(treasuryRoomEntity, f, tickDelta, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(TreasuryRoomEntity treasuryRoomEntity) {
        return TEXTURE;
    }
}

