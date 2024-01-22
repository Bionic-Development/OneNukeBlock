package de.takacick.illegalwars.registry.entity.projectiles.renderer;

import de.takacick.illegalwars.registry.entity.projectiles.CyberLaserEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

@Environment(value = EnvType.CLIENT)
public class CyberLaserEntityRenderer<T extends Entity>
        extends EntityRenderer<T> {

    private final ModelPart laser;

    public CyberLaserEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.laser = getTexturedModelData().createModel();
    }

    @Override
    protected int getBlockLight(T entity, BlockPos pos) {
        return 15;
    }

    @Override
    public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (entity.age < 2 && this.dispatcher.camera.getFocusedEntity().squaredDistanceTo(entity) < 12.25) {
            return;
        }

        matrices.push();
        matrices.scale(-1.0f, -1.0f, 1.0f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(entity.getYaw()));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch())));
        matrices.translate(0, -1.501, 0);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getLightning());

        Vector3f vector3f = CyberLaserEntity.COLOR;

        laser.render(matrices, vertexConsumer, light, 0, vector3f.x(), vector3f.y(), vector3f.z(), 0.7F);
        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(Entity entity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }

    public TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("laser", ModelPartBuilder.create().uv(-5, -5).cuboid(-0.5F, -1.0F, -3.5F, 1.0F, 1.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
        return TexturedModelData.of(modelData, 16, 16);
    }
}

