package de.takacick.imagineanything.registry.entity.custom.renderer;

import de.takacick.imagineanything.registry.entity.custom.HologramItemEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;

public class HologramItemEntityRenderer extends EntityRenderer<HologramItemEntity> {

    private final ItemRenderer itemRenderer;

    public HologramItemEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(HologramItemEntity hologramItemEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        if (hologramItemEntity.getItemStack() == null) {
            return;
        }

        matrixStack.push();

        Quaternion quaternion = new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);
        quaternion.hamiltonProduct(Vec3f.POSITIVE_Y.getDegreesQuaternion(-this.dispatcher.camera.getYaw()));

        matrixStack.multiply(quaternion);
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0f));

        this.itemRenderer.renderItem(hologramItemEntity.getItemStack(), ModelTransformation.Mode.NONE, i, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumerProvider, ((Entity) hologramItemEntity).getId());
        matrixStack.pop();

        super.render(hologramItemEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(HologramItemEntity hologramItemEntity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }
}

