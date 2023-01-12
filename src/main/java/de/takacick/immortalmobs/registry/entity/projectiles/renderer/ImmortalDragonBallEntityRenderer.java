package de.takacick.immortalmobs.registry.entity.projectiles.renderer;

import de.takacick.immortalmobs.ImmortalMobs;
import de.takacick.immortalmobs.client.CustomLayers;
import de.takacick.immortalmobs.registry.entity.projectiles.ImmortalDragonBallEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class ImmortalDragonBallEntityRenderer extends EntityRenderer<ImmortalDragonBallEntity> {

    private static final Identifier TEXTURE = new Identifier("textures/entity/enderdragon/dragon_fireball.png");
    private static final Identifier FIREBALL = new Identifier(ImmortalMobs.MOD_ID, "textures/entity/immortal_dragon_fireball.png");
    private static final RenderLayer LAYER = CustomLayers.IMMORTAL_CUTOUT.apply(TEXTURE);

    public ImmortalDragonBallEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    public void render(ImmortalDragonBallEntity immortalDragonBallEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.scale(2.0f, 2.0f, 2.0f);
        matrixStack.multiply(this.dispatcher.getRotation());
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0f));
        MatrixStack.Entry entry = matrixStack.peek();
        Matrix4f matrix4f = entry.getPositionMatrix();
        Matrix3f matrix3f = entry.getNormalMatrix();
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(LAYER);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 0.0f, 0, 0, 1);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 1.0f, 0, 1, 1);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 1.0f, 1, 1, 0);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 0.0f, 1, 0, 0);

        vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEyes(FIREBALL));
        for (int j = 0; j < 2; j++) {
            produceVertex(vertexConsumer, matrix4f, matrix3f, i, 0.0f, 0, 0, 1);
            produceVertex(vertexConsumer, matrix4f, matrix3f, i, 1.0f, 0, 1, 1);
            produceVertex(vertexConsumer, matrix4f, matrix3f, i, 1.0f, 1, 1, 0);
            produceVertex(vertexConsumer, matrix4f, matrix3f, i, 0.0f, 1, 0, 0);
        }

        matrixStack.pop();
        super.render(immortalDragonBallEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    private static void produceVertex(VertexConsumer vertexConsumer, Matrix4f positionMatrix, Matrix3f normalMatrix, int light, float x, int y, int textureU, int textureV) {
        vertexConsumer.vertex(positionMatrix, x - 0.5f, (float) y - 0.25f, 0.0f).color(255, 255, 255, 255).texture(textureU, textureV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, 0.0f, 1.0f, 0.0f).next();
    }

    @Override
    protected int getBlockLight(ImmortalDragonBallEntity immortalDragonBallEntity, BlockPos blockPos) {
        return 15;
    }

    public Identifier getTexture(ImmortalDragonBallEntity immortalDragonBallEntity) {
        return TEXTURE;
    }
}
