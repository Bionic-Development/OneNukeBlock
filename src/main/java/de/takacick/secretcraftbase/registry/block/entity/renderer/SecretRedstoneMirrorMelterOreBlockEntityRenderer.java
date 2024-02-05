package de.takacick.secretcraftbase.registry.block.entity.renderer;

import de.takacick.secretcraftbase.registry.block.SecretRedstoneMirrorMelterOre;
import de.takacick.secretcraftbase.registry.block.entity.SecretRedstoneMirrorMelterOreBlockEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class SecretRedstoneMirrorMelterOreBlockEntityRenderer implements BlockEntityRenderer<SecretRedstoneMirrorMelterOreBlockEntity> {

    public static final Identifier BEAM_TEXTURE = new Identifier("textures/entity/beacon_beam.png");

    public SecretRedstoneMirrorMelterOreBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(SecretRedstoneMirrorMelterOreBlockEntity secretRedstoneMirrorMelterOreBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        if (!secretRedstoneMirrorMelterOreBlockEntity.getCachedState().get(SecretRedstoneMirrorMelterOre.LIT)) {
            return;
        }

        long l = secretRedstoneMirrorMelterOreBlockEntity.getWorld().getTime();
        int k = 0;

        Direction direction = secretRedstoneMirrorMelterOreBlockEntity.getCachedState().get(SecretRedstoneMirrorMelterOre.FACING);
        if (!direction.equals(Direction.DOWN)) {
            matrixStack.translate(direction.getOffsetX(), direction.getOffsetY(), direction.getOffsetZ());
        }
        SecretRedstoneMirrorMelterOreBlockEntityRenderer.renderBeam(matrixStack, direction, vertexConsumerProvider,
                f, l, k, secretRedstoneMirrorMelterOreBlockEntity.getLength(), new float[]{1f, 0f, 0f});
    }

    private static void renderBeam(MatrixStack matrices, Direction direction, VertexConsumerProvider vertexConsumers, float tickDelta, long worldTime, int yOffset, int maxY, float[] color) {
        SecretRedstoneMirrorMelterOreBlockEntityRenderer.renderBeam(matrices, direction, vertexConsumers, BEAM_TEXTURE, tickDelta, 1.0f, worldTime, yOffset, maxY, color, 0.2f, 0.25f);
    }

    public static void renderBeam(MatrixStack matrices, Direction direction, VertexConsumerProvider vertexConsumers, Identifier textureId, float tickDelta, float heightScale, long worldTime, int yOffset, int maxY, float[] color, float innerRadius, float outerRadius) {
        int i = yOffset + maxY;
        matrices.push();
        matrices.translate(0.5, 0.0, 0.5);
        float f = (float) Math.floorMod(worldTime, 20) + tickDelta;
        float g = maxY < 0 ? f : -f;
        float h = MathHelper.fractionalPart(g * 0.2f - (float) MathHelper.floor(g * 0.1f));
        float j = color[0];
        float k = color[1];
        float l = color[2];

        if (direction.getAxis().equals(Direction.Axis.Y)) {
            matrices.translate(0f, 0.01f * direction.getOffsetY(), 0f);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(direction.equals(Direction.UP) ? 0f : 180f));
        } else {
            matrices.translate(-0.49f * direction.getOffsetX(), 0.5, -0.49f * direction.getOffsetZ());
            matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(direction.asRotation()));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90f));
        }

        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(f * 4.5f - 45.0f));

        float m = 0.0f;
        float n = innerRadius;
        float o = innerRadius;
        float p = 0.0f;
        float q = -innerRadius;
        float r = 0.0f;
        float s = 0.0f;
        float t = -innerRadius;
        float u = 0.0f;
        float v = 1.0f;
        float w = -1.0f + h;
        float x = (float) maxY * heightScale * (0.5f / innerRadius) + w;
        SecretRedstoneMirrorMelterOreBlockEntityRenderer.renderBeamLayer(matrices, vertexConsumers.getBuffer(RenderLayer.getBeaconBeam(textureId, false)), j, k, l, 1.0f, yOffset, i, 0.0f, n, o, 0.0f, q, 0.0f, 0.0f, t, 0.0f, 1.0f, x, w);
        matrices.pop();
        m = -outerRadius;
        n = -outerRadius;
        o = outerRadius;
        p = -outerRadius;
        q = -outerRadius;
        r = outerRadius;
        s = outerRadius;
        t = outerRadius;
        u = 0.0f;
        v = 1.0f;
        w = -1.0f + h;
        x = (float) maxY * heightScale + w;
        SecretRedstoneMirrorMelterOreBlockEntityRenderer.renderBeamLayer(matrices, vertexConsumers.getBuffer(RenderLayer.getBeaconBeam(textureId, true)), j, k, l, 0.125f, yOffset, i, m, n, o, p, q, r, s, t, 0.0f, 1.0f, x, w);
        matrices.pop();
    }

    private static void renderBeamLayer(MatrixStack matrices, VertexConsumer vertices, float red, float green, float blue, float alpha, int yOffset, int height, float x1, float z1, float x2, float z2, float x3, float z3, float x4, float z4, float u1, float u2, float v1, float v2) {
        MatrixStack.Entry entry = matrices.peek();
        Matrix4f matrix4f = entry.getPositionMatrix();
        Matrix3f matrix3f = entry.getNormalMatrix();

        SecretRedstoneMirrorMelterOreBlockEntityRenderer.renderBeamFace(matrix4f, matrix3f, vertices, red, green, blue, alpha, yOffset, height, x1, z1, x2, z2, u1, u2, v1, v2);
        SecretRedstoneMirrorMelterOreBlockEntityRenderer.renderBeamFace(matrix4f, matrix3f, vertices, red, green, blue, alpha, yOffset, height, x4, z4, x3, z3, u1, u2, v1, v2);
        SecretRedstoneMirrorMelterOreBlockEntityRenderer.renderBeamFace(matrix4f, matrix3f, vertices, red, green, blue, alpha, yOffset, height, x2, z2, x4, z4, u1, u2, v1, v2);
        SecretRedstoneMirrorMelterOreBlockEntityRenderer.renderBeamFace(matrix4f, matrix3f, vertices, red, green, blue, alpha, yOffset, height, x3, z3, x1, z1, u1, u2, v1, v2);
    }

    private static void renderBeamFace(Matrix4f positionMatrix, Matrix3f normalMatrix, VertexConsumer vertices, float red, float green, float blue, float alpha, int yOffset, int height, float x1, float z1, float x2, float z2, float u1, float u2, float v1, float v2) {
        SecretRedstoneMirrorMelterOreBlockEntityRenderer.renderBeamVertex(positionMatrix, normalMatrix, vertices, red, green, blue, alpha, height, x1, z1, u2, v1);
        SecretRedstoneMirrorMelterOreBlockEntityRenderer.renderBeamVertex(positionMatrix, normalMatrix, vertices, red, green, blue, alpha, yOffset, x1, z1, u2, v2);
        SecretRedstoneMirrorMelterOreBlockEntityRenderer.renderBeamVertex(positionMatrix, normalMatrix, vertices, red, green, blue, alpha, yOffset, x2, z2, u1, v2);
        SecretRedstoneMirrorMelterOreBlockEntityRenderer.renderBeamVertex(positionMatrix, normalMatrix, vertices, red, green, blue, alpha, height, x2, z2, u1, v1);
    }

    private static void renderBeamVertex(Matrix4f positionMatrix, Matrix3f normalMatrix, VertexConsumer vertices, float red, float green, float blue, float alpha, int y, float x, float z, float u, float v) {
        vertices.vertex(positionMatrix, x, y, z).color(red, green, blue, alpha).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(normalMatrix, 0.0f, 1.0f, 0.0f).next();
    }

    @Override
    public boolean rendersOutsideBoundingBox(SecretRedstoneMirrorMelterOreBlockEntity beaconBlockEntity) {
        return true;
    }

    @Override
    public int getRenderDistance() {
        return 256;
    }

    @Override
    public boolean isInRenderDistance(SecretRedstoneMirrorMelterOreBlockEntity beaconBlockEntity, Vec3d vec3d) {
        return Vec3d.ofCenter(beaconBlockEntity.getPos()).multiply(1.0, 0.0, 1.0).isInRange(vec3d.multiply(1.0, 0.0, 1.0), this.getRenderDistance());
    }
}

