package de.takacick.elementalblock.registry.entity.projectile.renderer;

import de.takacick.elementalblock.registry.entity.projectile.CobblestoneEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Items;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class CobblestoneEntityRenderer extends EntityRenderer<CobblestoneEntity> {

    private final ItemRenderer itemRenderer;

    public CobblestoneEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    public void render(CobblestoneEntity cobblestoneEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.scale(3, 3, 3);
        matrixStack.translate(0, 0.5, 0);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(g, cobblestoneEntity.prevYaw, cobblestoneEntity.getYaw())));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(MathHelper.lerp(g, cobblestoneEntity.prevPitch, cobblestoneEntity.getPitch()) - 45.0F));
        matrixStack.multiply(RotationAxis.NEGATIVE_X.rotation(-MathHelper.lerp(g, (cobblestoneEntity.age - 1 + cobblestoneEntity.getId()) * 0.3f, (cobblestoneEntity.age + cobblestoneEntity.getId()) * 0.3f)));

        this.itemRenderer.renderItem(Items.COBBLESTONE.getDefaultStack(), ModelTransformationMode.NONE, i, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumerProvider, cobblestoneEntity.getWorld(), cobblestoneEntity.getId());

        matrixStack.pop();

        super.render(cobblestoneEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Identifier getTexture(CobblestoneEntity cobblestoneEntity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }
}
