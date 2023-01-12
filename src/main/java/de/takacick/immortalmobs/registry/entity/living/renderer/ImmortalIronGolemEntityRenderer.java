package de.takacick.immortalmobs.registry.entity.living.renderer;

import de.takacick.immortalmobs.ImmortalMobs;
import de.takacick.immortalmobs.client.CustomLayers;
import de.takacick.immortalmobs.registry.entity.living.ImmortalIronGolemEntity;
import de.takacick.immortalmobs.registry.entity.living.model.ImmortalIronGolemEntityModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class ImmortalIronGolemEntityRenderer
        extends MobEntityRenderer<ImmortalIronGolemEntity, ImmortalIronGolemEntityModel<ImmortalIronGolemEntity>> {
    private static final Identifier TEXTURE = new Identifier("textures/entity/iron_golem/iron_golem.png");
    private static final Identifier EYES = new Identifier(ImmortalMobs.MOD_ID, "textures/entity/immortal_iron_golem_eyes.png");
    private static final float HALF_SQRT_3 = (float) (Math.sqrt(3.0) / 2.0);

    public ImmortalIronGolemEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new ImmortalIronGolemEntityModel(context.getPart(EntityModelLayers.IRON_GOLEM)), 0.7f);

        float[] hs = SheepEntity.getRgbColor(DyeColor.PURPLE);
        this.addFeature(new ImmortalEyesFeatureRenderer<>(this, EYES, hs[0], hs[1], hs[2]));
        this.addFeature(new ImmortalFeatureRenderer(this, context));
    }

    @Override
    public void render(ImmortalIronGolemEntity mobEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {

        if (!mobEntity.isAlive()) {
            matrixStack.push();

            float vertexConsumer = ((float) mobEntity.deathTicks + g) / 200.0f;
            ;
            float vertexConsumer2 = Math.min(vertexConsumer > 0.8f ? (vertexConsumer - 0.8f) / 0.2f : 0.0f, 1.0f);
            Random random = new Random(432L);
            VertexConsumer vertexConsumer3 = vertexConsumerProvider.getBuffer(RenderLayer.getLightning());
            matrixStack.push();
            matrixStack.translate(0.0, mobEntity.getHeight() / 2, 0.0);
            int l = 0;
            while ((float) l < (vertexConsumer + vertexConsumer * vertexConsumer) / 2.0f * 60.0f) {
                matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(random.nextFloat() * 360.0f));
                matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(random.nextFloat() * 360.0f));
                matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(random.nextFloat() * 360.0f));
                matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(random.nextFloat() * 360.0f));
                matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(random.nextFloat() * 360.0f));
                matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(random.nextFloat() * 360.0f + vertexConsumer * 90.0f));
                float m = random.nextFloat() * 20.0f + 5.0f + vertexConsumer2 * 10.0f;
                float n = random.nextFloat() * 2.0f + 1.0f + vertexConsumer2 * 2.0f;
                Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
                int o = (int) (255.0f * (1.0f - vertexConsumer2));

                method_23157(vertexConsumer3, matrix4f, getColor(0), o);
                method_23156(vertexConsumer3, matrix4f, getColor(50), m, n);
                method_23158(vertexConsumer3, matrix4f, getColor(100), m, n);
                method_23157(vertexConsumer3, matrix4f, getColor(150), o);
                method_23158(vertexConsumer3, matrix4f, getColor(200), m, n);
                method_23159(vertexConsumer3, matrix4f, m, n);
                method_23157(vertexConsumer3, matrix4f, getColor(250), o);
                method_23159(vertexConsumer3, matrix4f, m, n);
                method_23156(vertexConsumer3, matrix4f, getColor(300), m, n);
                ++l;
            }
            matrixStack.pop();
            matrixStack.pop();
        }

        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    protected void setupTransforms(ImmortalIronGolemEntity immortalIronGolemEntity, MatrixStack matrixStack, float f, float g, float h) {
        super.setupTransforms(immortalIronGolemEntity, matrixStack, f, g, h);
        if ((double) immortalIronGolemEntity.limbDistance < 0.01) {
            return;
        }
        float i = 13.0f;
        float j = immortalIronGolemEntity.limbAngle - immortalIronGolemEntity.limbDistance * (1.0f - h) + 6.0f;
        float k = (Math.abs(j % 13.0f - 6.5f) - 3.25f) / 3.25f;
        matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(6.5f * k));
    }

    @Override
    public Identifier getTexture(ImmortalIronGolemEntity immortalIronGolemEntity) {
        return TEXTURE;
    }

    @Nullable
    @Override
    protected RenderLayer getRenderLayer(ImmortalIronGolemEntity immortalIronGolemEntity, boolean showBody, boolean translucent, boolean showOutline) {
        return CustomLayers.IMMORTAL_CUTOUT.apply(getTexture(immortalIronGolemEntity));
    }

    @Environment(value = EnvType.CLIENT)
    public static class ImmortalFeatureRenderer
            extends FeatureRenderer<ImmortalIronGolemEntity, ImmortalIronGolemEntityModel<ImmortalIronGolemEntity>> {

        private final ImmortalIronGolemEntityModel<ImmortalIronGolemEntity> outerModel;
        private static final Identifier TEXTURE = new Identifier(ImmortalMobs.MOD_ID, "textures/entity/immortal_iron_golem.png");

        public ImmortalFeatureRenderer(FeatureRendererContext<ImmortalIronGolemEntity, ImmortalIronGolemEntityModel<ImmortalIronGolemEntity>> context, EntityRendererFactory.Context loader) {
            super(context);
            this.outerModel = new ImmortalIronGolemEntityModel(ImmortalIronGolemEntityModel.getTexturedModelData().createModel());
        }

        @Override
        public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, ImmortalIronGolemEntity entity, float limbAngle, float limbDistance, float tickDelta, float age, float headYaw, float headPitch) {
            getContextModel().copyStateTo(outerModel);
            outerModel.animateModel(entity, limbAngle, limbDistance, tickDelta);
            outerModel.setAngles(entity, limbAngle, limbDistance, age, headYaw, headPitch);

            float[] hs = SheepEntity.getRgbColor(DyeColor.PURPLE);
            float s = hs[0];
            float t = hs[1];
            float u = hs[2];

            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEyes(TEXTURE));
            outerModel.render(matrixStack, vertexConsumer, i, LivingEntityRenderer.getOverlay(entity, 0.0f), s, t, u, 1.0f);
            outerModel.render(matrixStack, vertexConsumer, i, LivingEntityRenderer.getOverlay(entity, 0.0f), s, t, u, 1.0f);
        }
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

    public float[] getColor(int offset) {
        return new float[]{0.4029412f, 0.14705883f, 0.5411765f};
    }
}

