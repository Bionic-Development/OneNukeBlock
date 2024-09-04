package de.takacick.onenukeblock.registry.entity.projectiles.renderer;

import de.takacick.onenukeblock.registry.entity.projectiles.CustomBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class CustomBlockEntityRenderer extends EntityRenderer<CustomBlockEntity> {

    private final ItemRenderer itemRenderer;

    public CustomBlockEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    public void render(CustomBlockEntity customBlockEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.translate(0, 0.5, 0);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-MathHelper.lerp(g, customBlockEntity.prevYaw, customBlockEntity.getYaw()) - 90));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-(MathHelper.lerp(g, customBlockEntity.prevPitch, customBlockEntity.getPitch()))
                + customBlockEntity.getRoll(g)));

        this.itemRenderer.renderItem(customBlockEntity.getItemStack(), ModelTransformationMode.NONE, i, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumerProvider, customBlockEntity.getWorld(), customBlockEntity.getId());

        matrixStack.pop();

        super.render(customBlockEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Identifier getTexture(CustomBlockEntity customBlockEntity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }
}
