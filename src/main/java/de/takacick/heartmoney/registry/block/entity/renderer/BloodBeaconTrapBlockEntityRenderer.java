package de.takacick.heartmoney.registry.block.entity.renderer;

import de.takacick.heartmoney.HeartMoney;
import de.takacick.heartmoney.registry.block.entity.BloodBeaconTrapBlockEntity;
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

public class BloodBeaconTrapBlockEntityRenderer implements BlockEntityRenderer<BloodBeaconTrapBlockEntity> {
    public static final Identifier BEAM_TEXTURE = new Identifier("textures/entity/beacon_beam.png");
    private static final Identifier HEART_BEACON = new Identifier(HeartMoney.MOD_ID, "textures/entity/blood_beacon_trap.png");

    private final ModelPart beaconBase;
    private final ModelPart heart;

    public BloodBeaconTrapBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        ModelPart root = getTexturedModelData().createModel();
        this.beaconBase = root.getChild("beacon");
        this.heart = root.getChild("heart");
    }

    @Override
    public void render(BloodBeaconTrapBlockEntity beaconBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {

        if (beaconBlockEntity.toggled && beaconBlockEntity.activated) {
            long l = beaconBlockEntity.getWorld().getTime();
            List<BloodBeaconTrapBlockEntity.BeamSegment> list = beaconBlockEntity.getBeamSegments();
            int k = 0;

            for (int m = 0; m < list.size(); ++m) {
                BloodBeaconTrapBlockEntity.BeamSegment beamSegment = list.get(m);
                renderBeam(matrixStack, vertexConsumerProvider, f, l, k, m == list.size() - 1 ? 1024 : beamSegment.getHeight(), beamSegment.getColor());
                k += beamSegment.getHeight();
            }
        }

        matrixStack.push();
        matrixStack.translate(0.5, 1.5, 0.5);
        matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(HEART_BEACON));
        this.beaconBase.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);

        if (beaconBlockEntity.hasWorld()) {
            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.lerp(f,
                    beaconBlockEntity.prevRotation, beaconBlockEntity.rotation) * 60f));
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

    public boolean rendersOutsideBoundingBox(BloodBeaconTrapBlockEntity beaconBlockEntity) {
        return true;
    }

    public int getRenderDistance() {
        return 256 * 256;
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData beacon = modelPartData.addChild("beacon", ModelPartBuilder.create().uv(0, 33).cuboid(-6.0F, -3.0F, -6.0F, 12.0F, 2.9F, 12.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData spikes = beacon.addChild("spikes", ModelPartBuilder.create(), ModelTransform.pivot(-11.818F, -19.0F, 11.318F));

        ModelPartData spikes_r1 = spikes.addChild("spikes_r1", ModelPartBuilder.create().uv(53, 40).mirrored().cuboid(2.75F, -3.0F, -1.25F, 4.0F, 6.0F, 0.0F, new Dilation(0.0F)).mirrored(false)
                .uv(53, 36).cuboid(4.75F, -3.0F, -3.25F, 0.0F, 6.0F, 4.0F, new Dilation(0.0F))
                .uv(53, 40).mirrored().cuboid(-1.25F, -3.0F, 0.75F, 4.0F, 6.0F, 0.0F, new Dilation(0.0F)).mirrored(false)
                .uv(53, 36).cuboid(0.75F, -3.0F, -1.25F, 0.0F, 6.0F, 4.0F, new Dilation(0.0F))
                .uv(53, 40).mirrored().cuboid(-3.25F, -3.0F, -4.25F, 4.0F, 6.0F, 0.0F, new Dilation(0.0F)).mirrored(false)
                .uv(53, 36).cuboid(-1.25F, -3.0F, -6.25F, 0.0F, 6.0F, 4.0F, new Dilation(0.0F))
                .uv(53, 40).mirrored().cuboid(-7.25F, -3.0F, 0.75F, 4.0F, 6.0F, 0.0F, new Dilation(0.0F)).mirrored(false)
                .uv(53, 36).cuboid(-5.25F, -3.0F, -1.25F, 0.0F, 6.0F, 4.0F, new Dilation(0.0F))
                .uv(53, 40).mirrored().cuboid(-2.25F, -3.0F, 5.75F, 4.0F, 6.0F, 0.0F, new Dilation(0.0F)).mirrored(false)
                .uv(53, 36).cuboid(-0.25F, -3.0F, 3.75F, 0.0F, 6.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(12.2426F, 0.0F, -11.8891F, 0.0F, -0.7854F, 0.0F));

        ModelPartData spikes2 = beacon.addChild("spikes2", ModelPartBuilder.create(), ModelTransform.pivot(-11.818F, -19.0F, 11.318F));

        ModelPartData spikes2_r1 = spikes2.addChild("spikes2_r1", ModelPartBuilder.create().uv(48, 60).mirrored().cuboid(7.4711F, 5.75F, -13.75F, 6.0F, 0.0F, 4.0F, new Dilation(0.0F)).mirrored(false)
                .uv(52, 60).mirrored().cuboid(7.4711F, 3.75F, -11.75F, 6.0F, 4.0F, 0.0F, new Dilation(0.0F)).mirrored(false)
                .uv(48, 60).mirrored().cuboid(7.4711F, 7.75F, -9.75F, 6.0F, 0.0F, 4.0F, new Dilation(0.0F)).mirrored(false)
                .uv(52, 60).mirrored().cuboid(7.4711F, 5.75F, -7.75F, 6.0F, 4.0F, 0.0F, new Dilation(0.0F)).mirrored(false)
                .uv(48, 60).mirrored().cuboid(7.4711F, 2.75F, -7.75F, 6.0F, 0.0F, 4.0F, new Dilation(0.0F)).mirrored(false)
                .uv(52, 60).mirrored().cuboid(7.4711F, 0.75F, -5.75F, 6.0F, 4.0F, 0.0F, new Dilation(0.0F)).mirrored(false)
                .uv(48, 60).mirrored().cuboid(7.4711F, 7.75F, -3.75F, 6.0F, 0.0F, 4.0F, new Dilation(0.0F)).mirrored(false)
                .uv(52, 60).mirrored().cuboid(7.4711F, 5.75F, -1.75F, 6.0F, 4.0F, 0.0F, new Dilation(0.0F)).mirrored(false)
                .uv(48, 60).mirrored().cuboid(7.4711F, 12.75F, -8.75F, 6.0F, 0.0F, 4.0F, new Dilation(0.0F)).mirrored(false)
                .uv(52, 60).mirrored().cuboid(7.4711F, 10.75F, -6.75F, 6.0F, 4.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(12.2426F, 0.0F, -11.8891F, 0.7854F, 0.0F, 0.0F));

        ModelPartData spikes2_r2 = spikes2.addChild("spikes2_r2", ModelPartBuilder.create().uv(47, 40).mirrored().cuboid(9.75F, 5.75F, 8.5711F, 4.0F, 0.0F, 6.0F, new Dilation(0.0F)).mirrored(false)
                .uv(52, 54).cuboid(11.75F, 3.75F, 8.5711F, 0.0F, 4.0F, 6.0F, new Dilation(0.0F))
                .uv(47, 40).mirrored().cuboid(5.75F, 7.75F, 8.5711F, 4.0F, 0.0F, 6.0F, new Dilation(0.0F)).mirrored(false)
                .uv(52, 54).cuboid(7.75F, 5.75F, 8.5711F, 0.0F, 4.0F, 6.0F, new Dilation(0.0F))
                .uv(47, 40).mirrored().cuboid(3.75F, 2.75F, 8.5711F, 4.0F, 0.0F, 6.0F, new Dilation(0.0F)).mirrored(false)
                .uv(52, 54).cuboid(5.75F, 0.75F, 8.5711F, 0.0F, 4.0F, 6.0F, new Dilation(0.0F))
                .uv(47, 40).mirrored().cuboid(-0.25F, 7.75F, 8.5711F, 4.0F, 0.0F, 6.0F, new Dilation(0.0F)).mirrored(false)
                .uv(52, 54).cuboid(1.75F, 5.75F, 8.5711F, 0.0F, 4.0F, 6.0F, new Dilation(0.0F))
                .uv(47, 40).mirrored().cuboid(4.75F, 12.75F, 8.5711F, 4.0F, 0.0F, 6.0F, new Dilation(0.0F)).mirrored(false)
                .uv(52, 54).cuboid(6.75F, 10.75F, 8.5711F, 0.0F, 4.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(12.2426F, 0.0F, -11.8891F, 0.0F, 0.0F, 0.7854F));

        ModelPartData spikes3 = beacon.addChild("spikes3", ModelPartBuilder.create(), ModelTransform.pivot(-11.818F, -19.0F, 11.318F));

        ModelPartData spikes3_r1 = spikes3.addChild("spikes3_r1", ModelPartBuilder.create().uv(48, 60).cuboid(-14.5711F, 6.75F, -14.75F, 6.0F, 0.0F, 4.0F, new Dilation(0.0F))
                .uv(52, 60).cuboid(-14.5711F, 4.75F, -12.75F, 6.0F, 4.0F, 0.0F, new Dilation(0.0F))
                .uv(48, 60).cuboid(-14.5711F, 8.75F, -10.75F, 6.0F, 0.0F, 4.0F, new Dilation(0.0F))
                .uv(52, 60).cuboid(-14.5711F, 6.75F, -8.75F, 6.0F, 4.0F, 0.0F, new Dilation(0.0F))
                .uv(48, 60).cuboid(-14.5711F, 3.75F, -8.75F, 6.0F, 0.0F, 4.0F, new Dilation(0.0F))
                .uv(52, 60).cuboid(-14.5711F, 1.75F, -6.75F, 6.0F, 4.0F, 0.0F, new Dilation(0.0F))
                .uv(48, 60).cuboid(-14.5711F, 8.75F, -4.75F, 6.0F, 0.0F, 4.0F, new Dilation(0.0F))
                .uv(52, 60).cuboid(-14.5711F, 6.75F, -2.75F, 6.0F, 4.0F, 0.0F, new Dilation(0.0F))
                .uv(48, 60).cuboid(-14.5711F, 13.75F, -9.75F, 6.0F, 0.0F, 4.0F, new Dilation(0.0F))
                .uv(52, 60).cuboid(-14.5711F, 11.75F, -7.75F, 6.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(13.3848F, 0.0F, -11.8891F, 0.7854F, 0.0F, 0.0F));

        ModelPartData spikes3_r2 = spikes3.addChild("spikes3_r2", ModelPartBuilder.create().uv(-6, 51).mirrored().cuboid(10.75F, 6.75F, -14.5711F, 4.0F, 0.0F, 6.0F, new Dilation(0.0F)).mirrored(false)
                .uv(0, 54).cuboid(12.75F, 4.75F, -14.5711F, 0.0F, 4.0F, 6.0F, new Dilation(0.0F))
                .uv(-6, 51).mirrored().cuboid(6.75F, 8.75F, -14.5711F, 4.0F, 0.0F, 6.0F, new Dilation(0.0F)).mirrored(false)
                .uv(0, 54).cuboid(8.75F, 6.75F, -14.5711F, 0.0F, 4.0F, 6.0F, new Dilation(0.0F))
                .uv(-6, 51).mirrored().cuboid(4.75F, 3.75F, -14.5711F, 4.0F, 0.0F, 6.0F, new Dilation(0.0F)).mirrored(false)
                .uv(0, 54).cuboid(6.75F, 1.75F, -14.5711F, 0.0F, 4.0F, 6.0F, new Dilation(0.0F))
                .uv(-6, 51).mirrored().cuboid(0.75F, 8.75F, -14.5711F, 4.0F, 0.0F, 6.0F, new Dilation(0.0F)).mirrored(false)
                .uv(0, 54).cuboid(2.75F, 6.75F, -14.5711F, 0.0F, 4.0F, 6.0F, new Dilation(0.0F))
                .uv(-6, 51).mirrored().cuboid(5.75F, 13.75F, -14.5711F, 4.0F, 0.0F, 6.0F, new Dilation(0.0F)).mirrored(false)
                .uv(0, 54).cuboid(7.75F, 11.75F, -14.5711F, 0.0F, 4.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(12.2426F, 0.0F, -10.747F, 0.0F, 0.0F, 0.7854F));

        ModelPartData heart = modelPartData.addChild("heart", ModelPartBuilder.create(), ModelTransform.of(0.0F, 15.4444F, 0.0F, 3.1416F, 0.0F, 0.0F));

        ModelPartData heart_8_r1 = heart.addChild("heart_8_r1", ModelPartBuilder.create().uv(46, 52).cuboid(-1.5F, -0.7F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(46, 55).cuboid(0.5F, -0.7F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(23, 51).cuboid(-4.5F, -5.7F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(23, 56).cuboid(3.5F, -5.7F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(36, 51).cuboid(-3.5F, -6.7F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(31, 51).cuboid(-0.5F, -6.7F, -0.5F, 1.0F, 8.0F, 1.0F, new Dilation(0.0F))
                .uv(41, 51).cuboid(2.5F, -6.7F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(9, 51).cuboid(-2.5F, -7.7F, -0.5F, 2.0F, 7.0F, 1.0F, new Dilation(0.0F))
                .uv(16, 51).cuboid(0.5F, -7.7F, -0.5F, 2.0F, 7.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -3.1444F, 0.0F, 3.1416F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }
}
