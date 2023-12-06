package de.takacick.emeraldmoney.registry.entity.projectile.renderer;

import de.takacick.emeraldmoney.registry.entity.projectile.PillagerProjectileEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.IllagerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class PillagerEntityRenderer extends EntityRenderer<PillagerProjectileEntity> {

    private static final Identifier TEXTURE = new Identifier("textures/entity/illager/pillager.png");

    private final IllagerEntityModel<PillagerEntity> illagerEntityModel;

    public PillagerEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.illagerEntityModel = new IllagerEntityModel<>(context.getPart(EntityModelLayers.PILLAGER));
    }

    public void render(PillagerProjectileEntity pillagerProjectileEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.translate(0, 0.5, 0);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(g, pillagerProjectileEntity.prevYaw, pillagerProjectileEntity.getYaw())));
        matrixStack.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(MathHelper.lerp(g, pillagerProjectileEntity.prevPitch, pillagerProjectileEntity.getPitch()) + 90.0F));
        matrixStack.translate(0, -0.5, 0);

        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(TEXTURE));
        this.illagerEntityModel.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
        matrixStack.pop();

        super.render(pillagerProjectileEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Identifier getTexture(PillagerProjectileEntity pillagerProjectileEntity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }
}
