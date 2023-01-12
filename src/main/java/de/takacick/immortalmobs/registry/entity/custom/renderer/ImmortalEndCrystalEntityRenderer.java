package de.takacick.immortalmobs.registry.entity.custom.renderer;

import de.takacick.immortalmobs.ImmortalMobs;
import de.takacick.immortalmobs.client.CustomLayers;
import de.takacick.immortalmobs.registry.entity.custom.ImmortalEndCrystalEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EnderDragonEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;

public class ImmortalEndCrystalEntityRenderer
        extends EntityRenderer<ImmortalEndCrystalEntity> {
    private static final Identifier TEXTURE = new Identifier("textures/entity/end_crystal/end_crystal.png");
    private static final Identifier IMMORTAL = new Identifier(ImmortalMobs.MOD_ID, "textures/entity/immortal_end_crystal.png");
    private static final RenderLayer END_CRYSTAL = RenderLayer.getEyes(IMMORTAL);
    private static final float SINE_45_DEGREES = (float) Math.sin(0.7853981633974483);
    private static final String GLASS = "glass";
    private static final String BASE = "base";
    private final ModelPart core;
    private final ModelPart frame;
    private final ModelPart bottom;

    public ImmortalEndCrystalEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.shadowRadius = 0.5f;
        ModelPart modelPart = context.getPart(EntityModelLayers.END_CRYSTAL);
        this.frame = modelPart.getChild(GLASS);
        this.core = modelPart.getChild(EntityModelPartNames.CUBE);
        this.bottom = modelPart.getChild(BASE);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(GLASS, ModelPartBuilder.create().uv(0, 0).cuboid(-4.0f, -4.0f, -4.0f, 8.0f, 8.0f, 8.0f), ModelTransform.NONE);
        modelPartData.addChild(EntityModelPartNames.CUBE, ModelPartBuilder.create().uv(32, 0).cuboid(-4.0f, -4.0f, -4.0f, 8.0f, 8.0f, 8.0f), ModelTransform.NONE);
        modelPartData.addChild(BASE, ModelPartBuilder.create().uv(0, 16).cuboid(-6.0f, 0.0f, -6.0f, 12.0f, 4.0f, 12.0f), ModelTransform.NONE);
        return TexturedModelData.of(modelData, 64, 32);
    }

    @Override
    public void render(ImmortalEndCrystalEntity endCrystalEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        Vec3f color = new Vec3f(0.4029412f, 0.14705883f, 0.5411765f);
        float h = ImmortalEndCrystalEntityRenderer.getYOffset(endCrystalEntity, g);
        float j = ((float) endCrystalEntity.endCrystalAge + g) * 3.0f;
        int k = OverlayTexture.DEFAULT_UV;

        matrixStack.push();
        matrixStack.scale(2.0f, 2.0f, 2.0f);
        matrixStack.translate(0.0, -0.5, 0.0);
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(j));
        matrixStack.translate(0.0, 1.5f + h / 2.0f, 0.0);
        matrixStack.multiply(new Quaternion(new Vec3f(SINE_45_DEGREES, 0.0f, SINE_45_DEGREES), 60.0f, true));
        float l = 0.875f;
        matrixStack.scale(0.875f, 0.875f, 0.875f);
        matrixStack.multiply(new Quaternion(new Vec3f(SINE_45_DEGREES, 0.0f, SINE_45_DEGREES), 60.0f, true));
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(j));
        matrixStack.scale(0.875f, 0.875f, 0.875f);
        matrixStack.multiply(new Quaternion(new Vec3f(SINE_45_DEGREES, 0.0f, SINE_45_DEGREES), 60.0f, true));
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(j));
        VertexConsumer buffer = vertexConsumerProvider.getBuffer(CustomLayers.IMMORTAL_CUTOUT.apply(TEXTURE));
        this.core.render(matrixStack, buffer, i, k);
        VertexConsumer  vertexConsumer = vertexConsumerProvider.getBuffer(END_CRYSTAL);
        this.core.render(matrixStack, vertexConsumer, i, k, color.getX(), color.getY(), color.getZ(), 0.2f);
        matrixStack.pop();

        matrixStack.push();
        matrixStack.scale(2.0f, 2.0f, 2.0f);
        matrixStack.translate(0.0, -0.5, 0.0);
        buffer = vertexConsumerProvider.getBuffer(CustomLayers.IMMORTAL_CUTOUT.apply(TEXTURE));
        if (endCrystalEntity.shouldShowBottom()) {
            this.bottom.render(matrixStack, buffer, i, k);
        }
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(j));
        matrixStack.translate(0.0, 1.5f + h / 2.0f, 0.0);
        matrixStack.multiply(new Quaternion(new Vec3f(SINE_45_DEGREES, 0.0f, SINE_45_DEGREES), 60.0f, true));
        vertexConsumer = vertexConsumerProvider.getBuffer(END_CRYSTAL);
        this.frame.render(matrixStack, vertexConsumer, i, k, color.getX(), color.getY(), color.getZ(), 1.0f);
        this.frame.render(matrixStack, vertexConsumer, i, k, color.getX(), color.getY(), color.getZ(), 1.0f);
        matrixStack.scale(0.875f, 0.875f, 0.875f);
        matrixStack.multiply(new Quaternion(new Vec3f(SINE_45_DEGREES, 0.0f, SINE_45_DEGREES), 60.0f, true));
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(j));
        this.frame.render(matrixStack, vertexConsumer, i, k, color.getX(), color.getY(), color.getZ(), 1.0f);
        this.frame.render(matrixStack, vertexConsumer, i, k, color.getX(), color.getY(), color.getZ(), 1.0f);
        matrixStack.scale(0.875f, 0.875f, 0.875f);
        matrixStack.multiply(new Quaternion(new Vec3f(SINE_45_DEGREES, 0.0f, SINE_45_DEGREES), 60.0f, true));
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(j));
        matrixStack.pop();

        BlockPos blockPos = endCrystalEntity.getBeamTarget();
        if (blockPos != null) {
            float m = (float) blockPos.getX() + 0.5f;
            float n = (float) blockPos.getY() + 0.5f;
            float o = (float) blockPos.getZ() + 0.5f;
            float p = (float) ((double) m - endCrystalEntity.getX());
            float q = (float) ((double) n - endCrystalEntity.getY());
            float r = (float) ((double) o - endCrystalEntity.getZ());
            matrixStack.translate(p, q, r);
            EnderDragonEntityRenderer.renderCrystalBeam(-p, -q + h, -r, g, endCrystalEntity.endCrystalAge, matrixStack, vertexConsumerProvider, i);
        }
        super.render(endCrystalEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public static float getYOffset(ImmortalEndCrystalEntity crystal, float tickDelta) {
        float f = (float) crystal.endCrystalAge + tickDelta;
        float g = MathHelper.sin(f * 0.2f) / 2.0f + 0.5f;
        g = (g * g + g) * 0.4f;
        return g - 1.4f;
    }

    @Override
    public Identifier getTexture(ImmortalEndCrystalEntity endCrystalEntity) {
        return TEXTURE;
    }

    @Override
    public boolean shouldRender(ImmortalEndCrystalEntity endCrystalEntity, Frustum frustum, double d, double e, double f) {
        return super.shouldRender(endCrystalEntity, frustum, d, e, f) || endCrystalEntity.getBeamTarget() != null;
    }
}

