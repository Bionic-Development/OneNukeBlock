package de.takacick.imagineanything.registry.entity.living.renderer;

import de.takacick.imagineanything.ImagineAnything;
import de.takacick.imagineanything.registry.entity.living.MysteriousEntity;
import de.takacick.imagineanything.registry.entity.living.model.MysteriousEntityModel;
import de.takacick.utils.BionicUtilsClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class MysteriousEntityRenderer extends LivingEntityRenderer<MysteriousEntity, MysteriousEntityModel<MysteriousEntity>> {

    public static final Identifier TEXTURE = new Identifier(ImagineAnything.MOD_ID, "textures/entity/mysterious.png");
    private static final float HALF_SQRT_3 = 1f;
    private static final RenderLayer LIGHTNING = RenderLayer.of("lightning", VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.QUADS, 256, false, true, RenderLayer.MultiPhaseParameters.builder()
            .shader(RenderLayer.LIGHTNING_SHADER).transparency(RenderLayer.TRANSLUCENT_TRANSPARENCY).cull(RenderLayer.ENABLE_CULLING)
            .build(true));

    public MysteriousEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new MysteriousEntityModel<>(MysteriousEntityModel.getTexturedModelData().createModel()), 0f);
        this.addFeature(new GlowFeatureRenderer(this));
    }

    @Override
    public void render(MysteriousEntity mysteriousEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {

        matrixStack.push();

        float vertexConsumer = ((float) mysteriousEntity.age + g) / 200f;
        float vertexConsumer2 = 0f;
        Random random = new Random(mysteriousEntity.getId());
        VertexConsumer vertexConsumer3 = vertexConsumerProvider.getBuffer(LIGHTNING);
        matrixStack.push();
        matrixStack.translate(0.0, mysteriousEntity.getHeight() / 2, 0.0);
        int l = 0;
        while ((float) l < 0) {
            matrixStack.push();
            matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(random.nextFloat() * 360.0f));
            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(random.nextFloat() * 360.0f));
            matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(random.nextFloat() * 360.0f));
            matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(random.nextFloat() * 360.0f));
            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(random.nextFloat() * 360.0f));
            matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(random.nextFloat() * 360.0f + vertexConsumer * 90.0f));
            float m = 1;
            float n = 1;
            Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
            int o = (int) (255.0f * (1.0f - vertexConsumer2));

            method_23157(vertexConsumer3, matrix4f, getColor(mysteriousEntity, 50 * l), o);
            method_23156(vertexConsumer3, matrix4f, getColor(mysteriousEntity, 50 * l), m, n);
            method_23158(vertexConsumer3, matrix4f, getColor(mysteriousEntity, 50 * l), m, n);
            method_23157(vertexConsumer3, matrix4f, getColor(mysteriousEntity, 50 * l), o);
            method_23158(vertexConsumer3, matrix4f, getColor(mysteriousEntity, 50 * l), m, n);
            method_23159(vertexConsumer3, matrix4f, m, n);
            method_23157(vertexConsumer3, matrix4f, getColor(mysteriousEntity, 50 * l), o);
            method_23159(vertexConsumer3, matrix4f, m, n);
            method_23156(vertexConsumer3, matrix4f, getColor(mysteriousEntity, 50 * l), m, n);
            ++l;
            matrixStack.pop();
        }
        matrixStack.pop();
        matrixStack.pop();
        super.render(mysteriousEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    protected void renderLabelIfPresent(MysteriousEntity entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {

    }

    @Nullable
    @Override
    protected RenderLayer getRenderLayer(MysteriousEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return ((Model) this.model).getLayer(TEXTURE);
    }

    public Identifier getTexture(MysteriousEntity mysteriousEntity) {
        return TEXTURE;
    }

    private static void method_23157(VertexConsumer vertices, Matrix4f matrix, float[] rgb, int alpha) {
        vertices.vertex(matrix, 0.0f, 0.0f, 0.0f).color(rgb[0], rgb[1], rgb[2], alpha / 255f).next();
    }

    private static void method_23156(VertexConsumer vertices, Matrix4f matrix, float[] rgb, float y, float x) {
        vertices.vertex(matrix, -HALF_SQRT_3 * x, y, -0.5f * x).color(rgb[0], rgb[1], rgb[2], 0).next();
    }

    private static void method_23158(VertexConsumer vertices, Matrix4f matrix, float[] rgb, float y, float x) {
        vertices.vertex(matrix, HALF_SQRT_3 * x, y, -0.5f * x).color(rgb[0], rgb[1], rgb[2], 0).next();
    }

    private static void method_23159(VertexConsumer vertices, Matrix4f matrix, float y, float z) {
        vertices.vertex(matrix, 0.0f, y, 1.0f * z).color(120, 120, 120, 0).next();
    }

    public float[] getColor(MysteriousEntity immortalEntity, int offset) {
        if (immortalEntity.hurtTime > 0 || immortalEntity.deathTime > 0) {
            return new float[]{1f, 0f, 0f};
        }

        Vec3f vec3f = new Vec3f(Vec3d.unpackRgb(BionicUtilsClient.getRainbow().getColorAsInt(offset)));
        return new float[]{vec3f.getX(), vec3f.getY(), vec3f.getZ()};
    }

    public static float getYOffset(EndCrystalEntity crystal, float tickDelta) {
        float f = (float) crystal.endCrystalAge + tickDelta;
        float g = MathHelper.sin(f * 0.2f) / 2.0f + 0.5f;
        g = (g * g + g) * 0.4f;
        return g - 1.4f;
    }

    @Environment(value = EnvType.CLIENT)
    public static class GlowFeatureRenderer
            extends FeatureRenderer<MysteriousEntity, MysteriousEntityModel<MysteriousEntity>> {

        private final MysteriousEntityModel<MysteriousEntity> outerModel;
        public static final Identifier TEXTURE = new Identifier(ImagineAnything.MOD_ID, "textures/entity/mysterious.png");

        public GlowFeatureRenderer(FeatureRendererContext<MysteriousEntity, MysteriousEntityModel<MysteriousEntity>> context) {
            super(context);
            this.outerModel = new MysteriousEntityModel<>(MysteriousEntityModel.getOuterTexturedModelData().createModel());
        }

        @Override
        public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, MysteriousEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
            getContextModel().copyStateTo(outerModel);
            outerModel.animateModel(entity, limbAngle, limbDistance, tickDelta);
            outerModel.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);

            float[] hs = SheepEntity.getRgbColor(DyeColor.YELLOW);
            float r = hs[0];
            float g = hs[1];
            float b = hs[2];

            //     VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEyes(TEXTURE));
            //     outerModel.render(matrixStack, vertexConsumer, i, LivingEntityRenderer.getOverlay(entity, 0.0f), r, g, b, 1.0f);
            //    outerModel.render(matrixStack, vertexConsumer, i, LivingEntityRenderer.getOverlay(entity, 0.0f), r, g, b, 1.0f);

            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEyes(TEXTURE));
            outerModel.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
            outerModel.getPart().getChild("bone5").visible = true;
            outerModel.getPart().getChild("bone4").visible = true;
            outerModel.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, r, g, b, 1.0f);
        }
    }
}
