package de.takacick.tinyhouse.registry.entity.projectile.renderer;

import de.takacick.tinyhouse.registry.entity.projectile.ChickenProjectileEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.ChickenEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class ChickenProjectileEntityRenderer extends EntityRenderer<ChickenProjectileEntity> {

    private static final Identifier TEXTURE = new Identifier("textures/entity/chicken.png");

    private final ChickenEntityModel<Entity> chickenEntityModel;

    public ChickenProjectileEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.chickenEntityModel = new ChickenEntityModel<>(context.getPart(EntityModelLayers.CHICKEN));
    }

    public void render(ChickenProjectileEntity chickenProjectileEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();

        matrixStack.translate(0, 0.5, 0);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(g, chickenProjectileEntity.prevYaw, chickenProjectileEntity.getYaw())));
        matrixStack.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(180.0F));
        matrixStack.translate(0, -1.001, 0);

        int j =  chickenProjectileEntity.getCurrentFuseTime() / 5 % 2 == 0  ? OverlayTexture.packUv(OverlayTexture.getU(1.0f), 10) : OverlayTexture.DEFAULT_UV;

         VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(TEXTURE));
        this.chickenEntityModel.setAngles(chickenProjectileEntity,
                0f, 0f, getAnimationProgress(chickenProjectileEntity, g), 0f, 0f);
        this.chickenEntityModel.render(matrixStack, vertexConsumer, i, j, 1f, 1f, 1f, 1f);
        matrixStack.pop();

        super.render(chickenProjectileEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    protected float getAnimationProgress(ChickenProjectileEntity chickenProjectileEntity, float f) {
        float g = MathHelper.lerp(f, chickenProjectileEntity.prevFlapProgress, chickenProjectileEntity.flapProgress);
        float h = MathHelper.lerp(f, chickenProjectileEntity.prevMaxWingDeviation, chickenProjectileEntity.maxWingDeviation);
        return (MathHelper.sin(g) + 1.0f) * h;
    }

    public Identifier getTexture(ChickenProjectileEntity chickenProjectileEntity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }
}
