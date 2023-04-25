package de.takacick.everythinghearts.registry.block.entity.renderer;

import de.takacick.everythinghearts.EverythingHearts;
import de.takacick.everythinghearts.registry.block.entity.WeatherHeartBeaconBlockEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

import java.util.List;

public class WeatherHeartBeaconBlockEntityRenderer implements BlockEntityRenderer<WeatherHeartBeaconBlockEntity> {
    public static final Identifier BEAM_TEXTURE = new Identifier("textures/entity/beacon_beam.png");
    private static final Identifier HEART_CHEST = new Identifier(EverythingHearts.MOD_ID, "textures/entity/weather_heart_beacon.png");

    private final ModelPart beaconBase;
    private final ModelPart heart;

    public WeatherHeartBeaconBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        ModelPart root = getTexturedModelData().createModel();
        this.beaconBase = root.getChild("beacon_base");
        this.heart = root.getChild("heart");
    }

    @Override
    public void render(WeatherHeartBeaconBlockEntity beaconBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {

        if (beaconBlockEntity.toggled && beaconBlockEntity.activated) {
            long l = beaconBlockEntity.getWorld().getTime();
            List<WeatherHeartBeaconBlockEntity.BeamSegment> list = beaconBlockEntity.getBeamSegments();
            int k = 0;

            for (int m = 0; m < list.size(); ++m) {
                WeatherHeartBeaconBlockEntity.BeamSegment beamSegment = list.get(m);
                renderBeam(matrixStack, vertexConsumerProvider, f, l, k, m == list.size() - 1 ? 1024 : beamSegment.getHeight(), beamSegment.getColor());
                k += beamSegment.getHeight();
            }
        }


        matrixStack.push();
        matrixStack.translate(0.5, -0.5, 0.5);
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutout(HEART_CHEST));
        this.beaconBase.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);

        if (beaconBlockEntity.hasWorld()) {
            beaconBlockEntity.prevRotation = beaconBlockEntity.rotation;
            beaconBlockEntity.rotation = beaconBlockEntity.age;

            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.lerpAngleDegrees(f,
                    beaconBlockEntity.prevRotation * 32.25F, beaconBlockEntity.rotation * 32.25F)));
        }

        this.heart.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
        matrixStack.pop();
    }

    private static void renderBeam(MatrixStack matrices, VertexConsumerProvider vertexConsumers, float tickDelta, long worldTime, int yOffset, int maxY, float[] color) {
        renderBeam(matrices, vertexConsumers, BEAM_TEXTURE, tickDelta, 1.0F, worldTime, yOffset, maxY, new float[]{1f, 0.06274509803f, 0.06274509803f}, 0.2F, 0.25F);
    }

    public static void renderBeam(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Identifier textureId, float tickDelta, float heightScale, long worldTime, int yOffset, int maxY, float[] color, float innerRadius, float outerRadius) {
        int i = yOffset + maxY;
        matrices.push();
        matrices.translate(0.5D, 0.0D, 0.5D);
        float f = (float) floorMod(worldTime, 40) + tickDelta;
        float g = maxY < 0 ? f : -f;
        float h = MathHelper.fractionalPart(g * 0.2F - (float) MathHelper.floor(g * 0.1F));
        float j = color[0];
        float k = color[1];
        float l = color[2];
        matrices.push();
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(f * 2.25F - 45.0F));
        float y = 0.0F;
        float ab = 0.0F;
        float ac = -innerRadius;
        float r = 0.0F;
        float s = 0.0F;
        float t = -innerRadius;
        float ag = 0.0F;
        float ah = 1.0F;
        float ai = -1.0F + h;
        float aj = (float) maxY * heightScale * (0.5F / innerRadius) + ai;
        renderBeamLayer(matrices, vertexConsumers.getBuffer(RenderLayer.getBeaconBeam(textureId, false)), j, k, l, 1.0F, yOffset, i, 0.0F, innerRadius, innerRadius, 0.0F, ac, 0.0F, 0.0F, t, 0.0F, 1.0F, aj, ai);
        matrices.pop();
        y = -outerRadius;
        float z = -outerRadius;
        ab = -outerRadius;
        ac = -outerRadius;
        ag = 0.0F;
        ah = 1.0F;
        ai = -1.0F + h;
        aj = (float) maxY * heightScale + ai;
        renderBeamLayer(matrices, vertexConsumers.getBuffer(RenderLayer.getBeaconBeam(textureId, true)), j, k, l, 0.125F, yOffset, i, y, z, outerRadius, ab, ac, outerRadius, outerRadius, outerRadius, 0.0F, 1.0F, aj, ai);
        matrices.pop();
    }

    private static void renderBeamLayer(MatrixStack matrices, VertexConsumer vertices, float red, float green, float blue, float alpha, int yOffset, int height, float x1, float z1, float x2, float z2, float x3, float z3, float x4, float z4, float u1, float u2, float v1, float v2) {
        MatrixStack.Entry entry = matrices.peek();
        Matrix4f matrix4f = entry.getPositionMatrix();
        Matrix3f matrix3f = entry.getNormalMatrix();
        renderBeamFace(matrix4f, matrix3f, vertices, red, green, blue, alpha, yOffset, height, x1, z1, x2, z2, u1, u2, v1, v2);
        renderBeamFace(matrix4f, matrix3f, vertices, red, green, blue, alpha, yOffset, height, x4, z4, x3, z3, u1, u2, v1, v2);
        renderBeamFace(matrix4f, matrix3f, vertices, red, green, blue, alpha, yOffset, height, x2, z2, x4, z4, u1, u2, v1, v2);
        renderBeamFace(matrix4f, matrix3f, vertices, red, green, blue, alpha, yOffset, height, x3, z3, x1, z1, u1, u2, v1, v2);
    }

    private static void renderBeamFace(Matrix4f modelMatrix, Matrix3f normalMatrix, VertexConsumer vertices, float red, float green, float blue, float alpha, int yOffset, int height, float x1, float z1, float x2, float z2, float u1, float u2, float v1, float v2) {
        renderBeamVertex(modelMatrix, normalMatrix, vertices, red, green, blue, alpha, height, x1, z1, u2, v1);
        renderBeamVertex(modelMatrix, normalMatrix, vertices, red, green, blue, alpha, yOffset, x1, z1, u2, v2);
        renderBeamVertex(modelMatrix, normalMatrix, vertices, red, green, blue, alpha, yOffset, x2, z2, u1, v2);
        renderBeamVertex(modelMatrix, normalMatrix, vertices, red, green, blue, alpha, height, x2, z2, u1, v1);
    }

    /**
     * @param u the left-most coordinate of the texture region
     * @param v the top-most coordinate of the texture region
     */
    private static void renderBeamVertex(Matrix4f modelMatrix, Matrix3f normalMatrix, VertexConsumer vertices, float red, float green, float blue, float alpha, int y, float x, float z, float u, float v) {
        vertices.vertex(modelMatrix, x, (float) y, z).color(red, green, blue, alpha).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(15728880).normal(normalMatrix, 0.0F, 1.0F, 0.0F).next();
    }

    public static long floorMod(long x, long y) {
        long mod = x % y;
        // if the signs are different and modulo not zero, adjust result
        if ((x ^ y) < 0 && mod != 0) {
            mod += y;
        }
        return mod;
    }

    public boolean rendersOutsideBoundingBox(WeatherHeartBeaconBlockEntity beaconBlockEntity) {
        return true;
    }

    public int getRenderDistance() {
        return 256 * 256;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData beacon_base = modelPartData.addChild("beacon_base", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData Obsidianbase_r1 = beacon_base.addChild("Obsidianbase_r1", ModelPartBuilder.create().uv(0, 33).cuboid(-6.0F, 2.2F, -6.0F, 12.0F, 3.0F, 12.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-8.0F, -10.7F, -8.0F, 16.0F, 16.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -10.7F, 0.0F, 3.1416F, 0.0F, 0.0F));

        ModelPartData heart = modelPartData.addChild("heart", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 16.4444F, 0.0F));

        ModelPartData heart_8_r1 = heart.addChild("heart_8_r1", ModelPartBuilder.create().uv(40, 52).cuboid(-1.5F, -0.7F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(40, 55).cuboid(0.5F, -0.7F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(17, 51).cuboid(-4.5F, -5.7F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(17, 56).cuboid(3.5F, -5.7F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(30, 51).cuboid(-3.5F, -6.7F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(25, 51).cuboid(-0.5F, -6.7F, -0.5F, 1.0F, 8.0F, 1.0F, new Dilation(0.0F))
                .uv(35, 51).cuboid(2.5F, -6.7F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(3, 51).cuboid(-2.5F, -7.7F, -0.5F, 2.0F, 7.0F, 1.0F, new Dilation(0.0F))
                .uv(10, 51).cuboid(0.5F, -7.7F, -0.5F, 2.0F, 7.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -3.1444F, 0.0F, 3.1416F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }
}
