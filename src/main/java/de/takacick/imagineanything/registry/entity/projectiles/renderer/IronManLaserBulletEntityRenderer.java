package de.takacick.imagineanything.registry.entity.projectiles.renderer;


import de.takacick.imagineanything.registry.entity.projectiles.IronManLaserBulletEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class IronManLaserBulletEntityRenderer extends EntityRenderer<IronManLaserBulletEntity> {

    private final IronManLaserBulletEntityModel rainbowLaserEntityModel;


    public IronManLaserBulletEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        rainbowLaserEntityModel = new IronManLaserBulletEntityModel();
    }

    public void render(IronManLaserBulletEntity ironManLaserBulletEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();


        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.lerp(g, ironManLaserBulletEntity.prevYaw, ironManLaserBulletEntity.getYaw())));
        matrixStack.multiply(Vec3f.NEGATIVE_X.getDegreesQuaternion(MathHelper.lerp(g, ironManLaserBulletEntity.prevPitch, ironManLaserBulletEntity.getPitch())));
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getLightning());
        rainbowLaserEntityModel.render(matrixStack, vertexConsumer, i, 0, 1f, 0f, 0f, 0.7F);
        matrixStack.pop();

        super.render(ironManLaserBulletEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Identifier getTexture(IronManLaserBulletEntity ironManLaserBulletEntity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }

    public static class IronManLaserBulletEntityModel<T extends Entity> extends EntityModel<T> {
        private final ModelPart root;

        public IronManLaserBulletEntityModel() {
            root = getTexturedModelData().createModel();
        }

        @Override
        public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

        }

        public TexturedModelData getTexturedModelData() {
            ModelData modelData = new ModelData();
            ModelPartData modelPartData = modelData.getRoot();

            modelPartData.addChild("head", ModelPartBuilder.create().cuboid(-0.5F, 0.0F, -5.0F, 1.0F, 1.0F, 10.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
            return TexturedModelData.of(modelData, 0, 0);
        }


        @Override
        public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
            root.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        }

    }
}
