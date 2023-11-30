package de.takacick.elementalblock.registry.entity.projectile.renderer;

import de.takacick.elementalblock.OneElementalBlock;
import de.takacick.elementalblock.registry.entity.projectile.TsunamicTridentEntity;
import de.takacick.elementalblock.registry.entity.projectile.model.TsunamicTridentEntityModel;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class TsunamicTridentEntityRenderer extends EntityRenderer<TsunamicTridentEntity> {

    public static final Identifier TEXTURE = new Identifier(OneElementalBlock.MOD_ID, "textures/entity/tsunamic_trident.png");
    private final TsunamicTridentEntityModel model;

    public TsunamicTridentEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new TsunamicTridentEntityModel(context.getPart(EntityModelLayers.TRIDENT));
    }

    @Override
    public void render(TsunamicTridentEntity tsunamicTridentEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(g, tsunamicTridentEntity.prevYaw, tsunamicTridentEntity.getYaw()) - 90.0f));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerp(g, tsunamicTridentEntity.prevPitch, tsunamicTridentEntity.getPitch()) + 90.0f));
        VertexConsumer vertexConsumer = ItemRenderer.getDirectItemGlintConsumer(vertexConsumerProvider, this.model.getLayer(this.getTexture(tsunamicTridentEntity)), false, tsunamicTridentEntity.isEnchanted());
        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
        matrixStack.pop();
        super.render(tsunamicTridentEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(TsunamicTridentEntity tsunamicTridentEntity) {
        return TEXTURE;
    }
}

