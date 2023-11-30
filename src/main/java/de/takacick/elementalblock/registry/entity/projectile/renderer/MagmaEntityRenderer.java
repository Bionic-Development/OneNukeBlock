package de.takacick.elementalblock.registry.entity.projectile.renderer;

import de.takacick.elementalblock.registry.entity.projectile.MagmaEntity;
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
public class MagmaEntityRenderer extends EntityRenderer<MagmaEntity> {

    private final ItemRenderer itemRenderer;

    public MagmaEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    public void render(MagmaEntity magmaEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.scale(1.5f, 1.5f, 1.5f);
        matrixStack.translate(0, 0.5, 0);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(g, magmaEntity.prevYaw, magmaEntity.getYaw())));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(MathHelper.lerp(g, magmaEntity.prevPitch, magmaEntity.getPitch()) - 45.0F));
        matrixStack.multiply(RotationAxis.NEGATIVE_X.rotation(-MathHelper.lerp(g, (magmaEntity.age - 1 + magmaEntity.getId()) * 0.3f, (magmaEntity.age + magmaEntity.getId()) * 0.3f)));

        this.itemRenderer.renderItem(Items.MAGMA_BLOCK.getDefaultStack(), ModelTransformationMode.NONE, i, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumerProvider, magmaEntity.getWorld(), magmaEntity.getId());

        matrixStack.pop();

        super.render(magmaEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Identifier getTexture(MagmaEntity magmaEntity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }
}
