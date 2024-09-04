package de.takacick.onenukeblock.registry.entity.projectiles.renderer;

import de.takacick.onenukeblock.registry.entity.projectiles.DiamondSwordEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
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
public class DiamondSwordEntityRenderer extends EntityRenderer<DiamondSwordEntity> {

    public DiamondSwordEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    public void render(DiamondSwordEntity diamondSwordEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();

        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(g, diamondSwordEntity.prevYaw, diamondSwordEntity.getYaw()) - 90));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerp(g, diamondSwordEntity.prevPitch, diamondSwordEntity.getPitch()) - 45));

        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        itemRenderer.renderItem(diamondSwordEntity.getItemStack(), ModelTransformationMode.NONE, i, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumerProvider, null, diamondSwordEntity.getId());
        matrixStack.pop();

        super.render(diamondSwordEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Identifier getTexture(DiamondSwordEntity diamondSwordEntity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }
}