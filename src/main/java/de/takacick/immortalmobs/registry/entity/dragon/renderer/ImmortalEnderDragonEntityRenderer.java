package de.takacick.immortalmobs.registry.entity.dragon.renderer;

import de.takacick.immortalmobs.ImmortalMobs;
import de.takacick.immortalmobs.client.CustomLayers;
import de.takacick.immortalmobs.registry.entity.custom.renderer.ImmortalEndCrystalEntityRenderer;
import de.takacick.immortalmobs.registry.entity.dragon.ImmortalEnderDragonEntity;
import de.takacick.utils.BionicUtilsClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

public class ImmortalEnderDragonEntityRenderer
        extends EntityRenderer<ImmortalEnderDragonEntity> {
    public static final Identifier CRYSTAL_BEAM_TEXTURE = new Identifier("textures/entity/end_crystal/end_crystal_beam.png");
    private static final Identifier EXPLOSION_TEXTURE = new Identifier("textures/entity/enderdragon/dragon_exploding.png");
    private static final Identifier TEXTURE = new Identifier(ImmortalMobs.MOD_ID, "textures/entity/immortal_dragon.png");
    private static final Identifier TEXTURE_WHITE = new Identifier(ImmortalMobs.MOD_ID, "textures/entity/immortal_dragon_white.png");
    private static final Identifier EYE_TEXTURE = new Identifier("textures/entity/enderdragon/dragon_eyes.png");
    private static final RenderLayer DRAGON_CUTOUT = CustomLayers.IMMORTAL_CUTOUT.apply(TEXTURE);
    private static final RenderLayer DRAGON_DECAL = RenderLayer.getEntityDecal(TEXTURE);
    private static final RenderLayer DRAGON_EYES = RenderLayer.getEyes(EYE_TEXTURE);
    private static final RenderLayer CRYSTAL_BEAM_LAYER = CustomLayers.IMMORTAL_CUTOUT.apply(CRYSTAL_BEAM_TEXTURE);
    private static final RenderLayer CRYSTAL_BEAM_LAYER_GLOW = RenderLayer.getEyes(CRYSTAL_BEAM_TEXTURE);
    private static final float HALF_SQRT_3 = (float) (Math.sqrt(3.0) / 2.0);
    private final ImmortalEnderDragonEntityRenderer.DragonEntityModel model;
    private final ImmortalEnderDragonEntityRenderer.DragonEntityModel model2;

    public ImmortalEnderDragonEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.shadowRadius = 0.5f;
        this.model = new ImmortalEnderDragonEntityRenderer.DragonEntityModel(ImmortalEnderDragonEntityRenderer.getInnerTexturedModelData().createModel());
        this.model2 = new ImmortalEnderDragonEntityRenderer.DragonEntityModel(ImmortalEnderDragonEntityRenderer.getTexturedModelData().createModel());
    }

    @Override
    public void render(ImmortalEnderDragonEntity enderDragonEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        float h = (float) enderDragonEntity.getSegmentProperties(7, g)[0];
        float j = (float) (enderDragonEntity.getSegmentProperties(5, g)[1] - enderDragonEntity.getSegmentProperties(10, g)[1]);
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-h));
        matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(j * 10.0f));
        matrixStack.translate(0.0, 0.0, 1.0);
        matrixStack.scale(-1.0f, -1.0f, 1.0f);
        matrixStack.translate(0.0, -1.501f, 0.0);
        boolean bl = enderDragonEntity.hurtTime > 0;
        this.model.animateModel(enderDragonEntity, 0.0f, 0.0f, g);
        this.model2.animateModel(enderDragonEntity, 0.0f, 0.0f, g);

        Vec3f vec3f = new Vec3f(Vec3d.unpackRgb(BionicUtilsClient.getRainbow().getColorAsInt(enderDragonEntity.getId() % 602)));

        if (enderDragonEntity.ticksSinceDeath > 0) {
            float k = (float) enderDragonEntity.ticksSinceDeath / 200.0f;
            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityAlpha(EXPLOSION_TEXTURE));
            this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, k);
            VertexConsumer vertexConsumer2 = vertexConsumerProvider.getBuffer(DRAGON_DECAL);
            this.model.render(matrixStack, vertexConsumer2, i, OverlayTexture.getUv(0.0f, bl), 1.0f, 1.0f, 1.0f, 1.0f);
        } else {
            VertexConsumer vertexConsumer3 = vertexConsumerProvider.getBuffer(DRAGON_CUTOUT);
            this.model.render(matrixStack, vertexConsumer3, i, OverlayTexture.getUv(0.0f, bl), 1.0f, 1.0f, 1.0f, 1.0f);
            VertexConsumer vertexConsumer4 = vertexConsumerProvider.getBuffer(RenderLayer.getEyes(TEXTURE_WHITE));
            this.model2.render(matrixStack, vertexConsumer4, i, OverlayTexture.DEFAULT_UV, vec3f.getX(), vec3f.getY(), vec3f.getZ(), 0.4f);
        }
        VertexConsumer vertexConsumer3 = vertexConsumerProvider.getBuffer(DRAGON_EYES);
        this.model.render(matrixStack, vertexConsumer3, i, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
        this.model2.render(matrixStack, vertexConsumer3, i, OverlayTexture.DEFAULT_UV, vec3f.getX(), vec3f.getY(), vec3f.getZ(), 0.4f);
        if (enderDragonEntity.ticksSinceDeath > 0) {
            float l = ((float) enderDragonEntity.ticksSinceDeath + g) / 200.0f;
            float m = Math.min(l > 0.8f ? (l - 0.8f) / 0.2f : 0.0f, 1.0f);
            Random random = Random.create(432L);
            VertexConsumer vertexConsumer4 = vertexConsumerProvider.getBuffer(RenderLayer.getLightning());
            matrixStack.push();
            matrixStack.translate(0.0, -1.0, -2.0);
            int n = 0;
            while ((float) n < (l + l * l) / 2.0f * 60.0f) {
                matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(random.nextFloat() * 360.0f));
                matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(random.nextFloat() * 360.0f));
                matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(random.nextFloat() * 360.0f));
                matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(random.nextFloat() * 360.0f));
                matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(random.nextFloat() * 360.0f));
                matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(random.nextFloat() * 360.0f + l * 90.0f));
                float o = random.nextFloat() * 20.0f + 5.0f + m * 10.0f;
                float p = random.nextFloat() * 2.0f + 1.0f + m * 2.0f;
                Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
                int q = (int) (255.0f * (1.0f - m));
                ImmortalEnderDragonEntityRenderer.method_23157(vertexConsumer4, matrix4f, q);
                ImmortalEnderDragonEntityRenderer.method_23156(vertexConsumer4, matrix4f, o, p);
                ImmortalEnderDragonEntityRenderer.method_23158(vertexConsumer4, matrix4f, o, p);
                ImmortalEnderDragonEntityRenderer.method_23157(vertexConsumer4, matrix4f, q);
                ImmortalEnderDragonEntityRenderer.method_23158(vertexConsumer4, matrix4f, o, p);
                ImmortalEnderDragonEntityRenderer.method_23159(vertexConsumer4, matrix4f, o, p);
                ImmortalEnderDragonEntityRenderer.method_23157(vertexConsumer4, matrix4f, q);
                ImmortalEnderDragonEntityRenderer.method_23159(vertexConsumer4, matrix4f, o, p);
                ImmortalEnderDragonEntityRenderer.method_23156(vertexConsumer4, matrix4f, o, p);
                ++n;
            }
            matrixStack.pop();
        }
        matrixStack.pop();
        if (enderDragonEntity.connectedCrystal != null && enderDragonEntity.getPlayerEntity() != null) {
            matrixStack.push();
            float l = (float) (enderDragonEntity.connectedCrystal.getX() - MathHelper.lerp(g, enderDragonEntity.getPlayerEntity().prevX, enderDragonEntity.getPlayerEntity().getX()));
            float m = (float) (enderDragonEntity.connectedCrystal.getY() - MathHelper.lerp(g, enderDragonEntity.getPlayerEntity().prevY, enderDragonEntity.getPlayerEntity().getY()));
            float r = (float) (enderDragonEntity.connectedCrystal.getZ() - MathHelper.lerp(g, enderDragonEntity.getPlayerEntity().prevZ, enderDragonEntity.getPlayerEntity().getZ()));
            ImmortalEnderDragonEntityRenderer.renderCrystalBeam(enderDragonEntity, l, m + ImmortalEndCrystalEntityRenderer.getYOffset(enderDragonEntity.connectedCrystal, g), r, g, enderDragonEntity.getPlayerEntity().age, matrixStack, vertexConsumerProvider, i);
            matrixStack.pop();
        }
        super.render(enderDragonEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    private static void method_23157(VertexConsumer vertices, Matrix4f matrix, int alpha) {
        vertices.vertex(matrix, 0.0f, 0.0f, 0.0f).color(255, 255, 255, alpha).next();
    }

    private static void method_23156(VertexConsumer vertices, Matrix4f matrix, float y, float x) {
        vertices.vertex(matrix, -HALF_SQRT_3 * x, y, -0.5f * x).color(255, 0, 255, 0).next();
    }

    private static void method_23158(VertexConsumer vertices, Matrix4f matrix, float y, float x) {
        vertices.vertex(matrix, HALF_SQRT_3 * x, y, -0.5f * x).color(255, 0, 255, 0).next();
    }

    private static void method_23159(VertexConsumer vertices, Matrix4f matrix, float y, float z) {
        vertices.vertex(matrix, 0.0f, y, 1.0f * z).color(255, 0, 255, 0).next();
    }

    public static void renderCrystalBeam(ImmortalEnderDragonEntity immortalEnderDragonEntity, float dx, float dy, float dz, float tickDelta, int age, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        float f = MathHelper.sqrt(dx * dx + dz * dz);
        float g = MathHelper.sqrt(dx * dx + dy * dy + dz * dz);
        matrices.push();
        matrices.translate(0.0, 2.0, 0.0);
        matrices.multiply(Vec3f.POSITIVE_Y.getRadialQuaternion((float) (-Math.atan2(dz, dx)) - 1.5707964f));
        matrices.multiply(Vec3f.POSITIVE_X.getRadialQuaternion((float) (-Math.atan2(f, dy)) - 1.5707964f));

        float h = 0.0f - ((float) age + tickDelta) * 0.01f;
        float i = MathHelper.sqrt(dx * dx + dy * dy + dz * dz) / 32.0f - ((float) age + tickDelta) * 0.01f;
        int j = 8;
        float k = 0.0f;
        float l = 0.75f;
        float m = 0.0f;
        MatrixStack.Entry entry = matrices.peek();
        Matrix4f matrix4f = entry.getPositionMatrix();
        Matrix3f matrix3f = entry.getNormalMatrix();
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(CRYSTAL_BEAM_LAYER);
        for (int n = 1; n <= 8; ++n) {
            float o = MathHelper.sin((float) n * ((float) Math.PI * 2) / 8.0f) * 0.75f;
            float p = MathHelper.cos((float) n * ((float) Math.PI * 2) / 8.0f) * 0.75f;
            float q = (float) n / 8.0f;
            vertexConsumer.vertex(matrix4f, k * 0.2f, l * 0.2f, 0.0f).color(0, 0, 0, 255).texture(m, h).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0f, -1.0f, 0.0f).next();
            vertexConsumer.vertex(matrix4f, k, l, g).color(255, 255, 255, 255).texture(m, i).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0f, -1.0f, 0.0f).next();
            vertexConsumer.vertex(matrix4f, o, p, g).color(255, 255, 255, 255).texture(q, i).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0f, -1.0f, 0.0f).next();
            vertexConsumer.vertex(matrix4f, o * 0.2f, p * 0.2f, 0.0f).color(0, 0, 0, 255).texture(q, h).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0f, -1.0f, 0.0f).next();
            k = o;
            l = p;
            m = q;
        }

        Vec3f vec3f = new Vec3f(Vec3d.unpackRgb(BionicUtilsClient.getRainbow().getColorAsInt(immortalEnderDragonEntity.getId() % 602)));
        vertexConsumer = vertexConsumers.getBuffer(CRYSTAL_BEAM_LAYER_GLOW);
        for (int n = 1; n <= 8; ++n) {
            float o = MathHelper.sin((float) n * ((float) Math.PI * 2) / 8.0f) * 0.75f;
            float p = MathHelper.cos((float) n * ((float) Math.PI * 2) / 8.0f) * 0.75f;
            float q = (float) n / 8.0f;
            vertexConsumer.vertex(matrix4f, k * 0.2f, l * 0.2f, 0.0f).color(vec3f.getX(), vec3f.getY(), vec3f.getZ(), 0.1f).texture(m, h).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0f, -1.0f, 0.0f).next();
            vertexConsumer.vertex(matrix4f, k, l, g).color(0.4029412f, 0.14705883f, 0.5411765f, 0.1f).texture(m, i).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0f, -1.0f, 0.0f).next();
            vertexConsumer.vertex(matrix4f, o, p, g).color(0.4029412f, 0.14705883f, 0.5411765f, 0.1f).texture(q, i).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0f, -1.0f, 0.0f).next();
            vertexConsumer.vertex(matrix4f, o * 0.2f, p * 0.2f, 0.0f).color(vec3f.getX(), vec3f.getY(), vec3f.getZ(), 0.1f).texture(q, h).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0f, -1.0f, 0.0f).next();
            k = o;
            l = p;
            m = q;
        }
        matrices.pop();
    }

    @Override
    public Identifier getTexture(ImmortalEnderDragonEntity enderDragonEntity) {
        return TEXTURE;
    }

    public static TexturedModelData getInnerTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        float f = -16.0f;
        ModelPartData modelPartData2 = modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().cuboid("upperlip", -6.0f, -1.0f, -24.0f, 12, 5, 16, 176, 44).cuboid("upperhead", -8.0f, -8.0f, -10.0f, 16, 16, 16, 112, 30).mirrored().cuboid("scale", -5.0f, -12.0f, -4.0f, 2, 4, 6, 0, 0).cuboid("nostril", -5.0f, -3.0f, -22.0f, 2, 2, 4, 112, 0).mirrored().cuboid("scale", 3.0f, -12.0f, -4.0f, 2, 4, 6, 0, 0).cuboid("nostril", 3.0f, -3.0f, -22.0f, 2, 2, 4, 112, 0), ModelTransform.NONE);
        modelPartData2.addChild(EntityModelPartNames.JAW, ModelPartBuilder.create().cuboid(EntityModelPartNames.JAW, -6.0f, 0.0f, -16.0f, 12, 4, 16, 176, 65), ModelTransform.pivot(0.0f, 4.0f, -8.0f));
        modelPartData.addChild(EntityModelPartNames.NECK, ModelPartBuilder.create().cuboid("box", -5.0f, -5.0f, -5.0f, 10, 10, 10, 192, 104).cuboid("scale", -1.0f, -9.0f, -3.0f, 2, 4, 6, 48, 0), ModelTransform.NONE);
        modelPartData.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create().cuboid(EntityModelPartNames.BODY, -12.0f, 0.0f, -16.0f, 24, 24, 64, 0, 0).cuboid("scale", -1.0f, -6.0f, -10.0f, 2, 6, 12, 220, 53).cuboid("scale", -1.0f, -6.0f, 10.0f, 2, 6, 12, 220, 53).cuboid("scale", -1.0f, -6.0f, 30.0f, 2, 6, 12, 220, 53), ModelTransform.pivot(0.0f, 4.0f, 8.0f));
        ModelPartData modelPartData3 = modelPartData.addChild(EntityModelPartNames.LEFT_WING, ModelPartBuilder.create().mirrored().cuboid(EntityModelPartNames.BONE, 0.0f, -4.0f, -4.0f, 56, 8, 8, 112, 88).cuboid("skin", 0.0f, 0.0f, 2.0f, 56, 0, 56, -56, 89), ModelTransform.pivot(12.0f, 5.0f, 2.0f));
        modelPartData3.addChild(EntityModelPartNames.LEFT_WING_TIP, ModelPartBuilder.create().mirrored().cuboid(EntityModelPartNames.BONE, 0.0f, -2.0f, -2.0f, 56, 4, 4, 112, 136).cuboid("skin", 0.0f, 0.0f, 2.0f, 56, 0, 56, -56, 145), ModelTransform.pivot(56.0f, 0.0f, 0.0f));
        ModelPartData modelPartData4 = modelPartData.addChild(EntityModelPartNames.LEFT_FRONT_LEG, ModelPartBuilder.create().cuboid("main", -4.0f, -4.0f, -4.0f, 8, 24, 8, 112, 104), ModelTransform.pivot(12.0f, 20.0f, 2.0f));
        ModelPartData modelPartData5 = modelPartData4.addChild(EntityModelPartNames.LEFT_FRONT_LEG_TIP, ModelPartBuilder.create().cuboid("main", -3.0f, -1.0f, -3.0f, 6, 24, 6, 226, 138), ModelTransform.pivot(0.0f, 20.0f, -1.0f));
        modelPartData5.addChild(EntityModelPartNames.LEFT_FRONT_FOOT, ModelPartBuilder.create().cuboid("main", -4.0f, 0.0f, -12.0f, 8, 4, 16, 144, 104), ModelTransform.pivot(0.0f, 23.0f, 0.0f));
        ModelPartData modelPartData6 = modelPartData.addChild(EntityModelPartNames.LEFT_HIND_LEG, ModelPartBuilder.create().cuboid("main", -8.0f, -4.0f, -8.0f, 16, 32, 16, 0, 0), ModelTransform.pivot(16.0f, 16.0f, 42.0f));
        ModelPartData modelPartData7 = modelPartData6.addChild(EntityModelPartNames.LEFT_HIND_LEG_TIP, ModelPartBuilder.create().cuboid("main", -6.0f, -2.0f, 0.0f, 12, 32, 12, 196, 0), ModelTransform.pivot(0.0f, 32.0f, -4.0f));
        modelPartData7.addChild(EntityModelPartNames.LEFT_HIND_FOOT, ModelPartBuilder.create().cuboid("main", -9.0f, 0.0f, -20.0f, 18, 6, 24, 112, 0), ModelTransform.pivot(0.0f, 31.0f, 4.0f));
        ModelPartData modelPartData8 = modelPartData.addChild(EntityModelPartNames.RIGHT_WING, ModelPartBuilder.create().cuboid(EntityModelPartNames.BONE, -56.0f, -4.0f, -4.0f, 56, 8, 8, 112, 88).cuboid("skin", -56.0f, 0.0f, 2.0f, 56, 0, 56, -56, 89), ModelTransform.pivot(-12.0f, 5.0f, 2.0f));
        modelPartData8.addChild(EntityModelPartNames.RIGHT_WING_TIP, ModelPartBuilder.create().cuboid(EntityModelPartNames.BONE, -56.0f, -2.0f, -2.0f, 56, 4, 4, 112, 136).cuboid("skin", -56.0f, 0.0f, 2.0f, 56, 0, 56, -56, 145), ModelTransform.pivot(-56.0f, 0.0f, 0.0f));
        ModelPartData modelPartData9 = modelPartData.addChild(EntityModelPartNames.RIGHT_FRONT_LEG, ModelPartBuilder.create().cuboid("main", -4.0f, -4.0f, -4.0f, 8, 24, 8, 112, 104), ModelTransform.pivot(-12.0f, 20.0f, 2.0f));
        ModelPartData modelPartData10 = modelPartData9.addChild(EntityModelPartNames.RIGHT_FRONT_LEG_TIP, ModelPartBuilder.create().cuboid("main", -3.0f, -1.0f, -3.0f, 6, 24, 6, 226, 138), ModelTransform.pivot(0.0f, 20.0f, -1.0f));
        modelPartData10.addChild(EntityModelPartNames.RIGHT_FRONT_FOOT, ModelPartBuilder.create().cuboid("main", -4.0f, 0.0f, -12.0f, 8, 4, 16, 144, 104), ModelTransform.pivot(0.0f, 23.0f, 0.0f));
        ModelPartData modelPartData11 = modelPartData.addChild(EntityModelPartNames.RIGHT_HIND_LEG, ModelPartBuilder.create().cuboid("main", -8.0f, -4.0f, -8.0f, 16, 32, 16, 0, 0), ModelTransform.pivot(-16.0f, 16.0f, 42.0f));
        ModelPartData modelPartData12 = modelPartData11.addChild(EntityModelPartNames.RIGHT_HIND_LEG_TIP, ModelPartBuilder.create().cuboid("main", -6.0f, -2.0f, 0.0f, 12, 32, 12, 196, 0), ModelTransform.pivot(0.0f, 32.0f, -4.0f));
        modelPartData12.addChild(EntityModelPartNames.RIGHT_HIND_FOOT, ModelPartBuilder.create().cuboid("main", -9.0f, 0.0f, -20.0f, 18, 6, 24, 112, 0), ModelTransform.pivot(0.0f, 31.0f, 4.0f));
        return TexturedModelData.of(modelData, 256, 256);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        float f = -16.0f;
        ModelPartData modelPartData2 = modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().cuboid("upperlip", -6.0f, -1.0f, -24.0f, 12, 5, 16, new Dilation(0.1f), 176, 44).cuboid("upperhead", -8.0f, -8.0f, -10.0f, 16, 16, 16, new Dilation(0.1f), 112, 30).mirrored().cuboid("scale", -5.0f, -12.0f, -4.0f, 2, 4, 6, new Dilation(0.1f), 0, 0).cuboid("nostril", -5.0f, -3.0f, -22.0f, 2, 2, 4, new Dilation(0.1f), 112, 0).mirrored().cuboid("scale", 3.0f, -12.0f, -4.0f, 2, 4, 6, new Dilation(0.1f), 0, 0).cuboid("nostril", 3.0f, -3.0f, -22.0f, 2, 2, 4, new Dilation(0.1f), 112, 0), ModelTransform.NONE);
        modelPartData2.addChild(EntityModelPartNames.JAW, ModelPartBuilder.create().cuboid(EntityModelPartNames.JAW, -6.0f, 0.0f, -16.0f, 12, 4, 16, new Dilation(0.1f), 176, 65), ModelTransform.pivot(0.0f, 4.0f, -8.0f));
        modelPartData.addChild(EntityModelPartNames.NECK, ModelPartBuilder.create().cuboid("box", -5.0f, -5.0f, -5.0f, 10, 10, 10, new Dilation(0.1f), 192, 104).cuboid("scale", -1.0f, -9.0f, -3.0f, 2, 4, 6, new Dilation(0.1f), 48, 0), ModelTransform.NONE);
        modelPartData.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create().cuboid(EntityModelPartNames.BODY, -12.0f, 0.0f, -16.0f, 24, 24, 64, new Dilation(0.1f), 0, 0).cuboid("scale", -1.0f, -6.0f, -10.0f, 2, 6, 12, new Dilation(0.1f), 220, 53).cuboid("scale", -1.0f, -6.0f, 10.0f, 2, 6, 12, new Dilation(0.1f), 220, 53).cuboid("scale", -1.0f, -6.0f, 30.0f, 2, 6, 12, new Dilation(0.1f), 220, 53), ModelTransform.pivot(0.0f, 4.0f, 8.0f));
        ModelPartData modelPartData3 = modelPartData.addChild(EntityModelPartNames.LEFT_WING, ModelPartBuilder.create().mirrored().cuboid(EntityModelPartNames.BONE, 0.0f, -4.0f, -4.0f, 56, 8, 8, new Dilation(0.1f), 112, 88).cuboid("skin", 0.0f, 0.0f, 2.0f, 56, 0, 56, new Dilation(0.1f), -56, 89), ModelTransform.pivot(12.0f, 5.0f, 2.0f));
        modelPartData3.addChild(EntityModelPartNames.LEFT_WING_TIP, ModelPartBuilder.create().mirrored().cuboid(EntityModelPartNames.BONE, 0.0f, -2.0f, -2.0f, 56, 4, 4, new Dilation(0.1f), 112, 136).cuboid("skin", 0.0f, 0.0f, 2.0f, 56, 0, 56, new Dilation(0.1f), -56, 145), ModelTransform.pivot(56.0f, 0.0f, 0.0f));
        ModelPartData modelPartData4 = modelPartData.addChild(EntityModelPartNames.LEFT_FRONT_LEG, ModelPartBuilder.create().cuboid("main", -4.0f, -4.0f, -4.0f, 8, 24, 8, new Dilation(0.1f), 112, 104), ModelTransform.pivot(12.0f, 20.0f, 2.0f));
        ModelPartData modelPartData5 = modelPartData4.addChild(EntityModelPartNames.LEFT_FRONT_LEG_TIP, ModelPartBuilder.create().cuboid("main", -3.0f, -1.0f, -3.0f, 6, 24, 6, new Dilation(0.1f), 226, 138), ModelTransform.pivot(0.0f, 20.0f, -1.0f));
        modelPartData5.addChild(EntityModelPartNames.LEFT_FRONT_FOOT, ModelPartBuilder.create().cuboid("main", -4.0f, 0.0f, -12.0f, 8, 4, 16, new Dilation(0.1f), 144, 104), ModelTransform.pivot(0.0f, 23.0f, 0.0f));
        ModelPartData modelPartData6 = modelPartData.addChild(EntityModelPartNames.LEFT_HIND_LEG, ModelPartBuilder.create().cuboid("main", -8.0f, -4.0f, -8.0f, 16, 32, 16, new Dilation(0.1f), 0, 0), ModelTransform.pivot(16.0f, 16.0f, 42.0f));
        ModelPartData modelPartData7 = modelPartData6.addChild(EntityModelPartNames.LEFT_HIND_LEG_TIP, ModelPartBuilder.create().cuboid("main", -6.0f, -2.0f, 0.0f, 12, 32, 12, new Dilation(0.1f), 196, 0), ModelTransform.pivot(0.0f, 32.0f, -4.0f));
        modelPartData7.addChild(EntityModelPartNames.LEFT_HIND_FOOT, ModelPartBuilder.create().cuboid("main", -9.0f, 0.0f, -20.0f, 18, 6, 24, new Dilation(0.1f), 112, 0), ModelTransform.pivot(0.0f, 31.0f, 4.0f));
        ModelPartData modelPartData8 = modelPartData.addChild(EntityModelPartNames.RIGHT_WING, ModelPartBuilder.create().cuboid(EntityModelPartNames.BONE, -56.0f, -4.0f, -4.0f, 56, 8, 8, new Dilation(0.1f), 112, 88).cuboid("skin", -56.0f, 0.0f, 2.0f, 56, 0, 56, new Dilation(0.1f), -56, 89), ModelTransform.pivot(-12.0f, 5.0f, 2.0f));
        modelPartData8.addChild(EntityModelPartNames.RIGHT_WING_TIP, ModelPartBuilder.create().cuboid(EntityModelPartNames.BONE, -56.0f, -2.0f, -2.0f, 56, 4, 4, new Dilation(0.1f), 112, 136).cuboid("skin", -56.0f, 0.0f, 2.0f, 56, 0, 56, new Dilation(0.1f), -56, 145), ModelTransform.pivot(-56.0f, 0.0f, 0.0f));
        ModelPartData modelPartData9 = modelPartData.addChild(EntityModelPartNames.RIGHT_FRONT_LEG, ModelPartBuilder.create().cuboid("main", -4.0f, -4.0f, -4.0f, 8, 24, 8, new Dilation(0.1f), 112, 104), ModelTransform.pivot(-12.0f, 20.0f, 2.0f));
        ModelPartData modelPartData10 = modelPartData9.addChild(EntityModelPartNames.RIGHT_FRONT_LEG_TIP, ModelPartBuilder.create().cuboid("main", -3.0f, -1.0f, -3.0f, 6, 24, 6, new Dilation(0.1f), 226, 138), ModelTransform.pivot(0.0f, 20.0f, -1.0f));
        modelPartData10.addChild(EntityModelPartNames.RIGHT_FRONT_FOOT, ModelPartBuilder.create().cuboid("main", -4.0f, 0.0f, -12.0f, 8, 4, 16, new Dilation(0.1f), 144, 104), ModelTransform.pivot(0.0f, 23.0f, 0.0f));
        ModelPartData modelPartData11 = modelPartData.addChild(EntityModelPartNames.RIGHT_HIND_LEG, ModelPartBuilder.create().cuboid("main", -8.0f, -4.0f, -8.0f, 16, 32, 16, new Dilation(0.1f), 0, 0), ModelTransform.pivot(-16.0f, 16.0f, 42.0f));
        ModelPartData modelPartData12 = modelPartData11.addChild(EntityModelPartNames.RIGHT_HIND_LEG_TIP, ModelPartBuilder.create().cuboid("main", -6.0f, -2.0f, 0.0f, 12, 32, 12, new Dilation(0.1f), 196, 0), ModelTransform.pivot(0.0f, 32.0f, -4.0f));
        modelPartData12.addChild(EntityModelPartNames.RIGHT_HIND_FOOT, ModelPartBuilder.create().cuboid("main", -9.0f, 0.0f, -20.0f, 18, 6, 24, new Dilation(0.1f), 112, 0), ModelTransform.pivot(0.0f, 31.0f, 4.0f));
        return TexturedModelData.of(modelData, 256, 256);
    }

    @Environment(value = EnvType.CLIENT)
    public static class DragonEntityModel
            extends EntityModel<ImmortalEnderDragonEntity> {
        private final ModelPart head;
        private final ModelPart neck;
        private final ModelPart jaw;
        private final ModelPart body;
        private final ModelPart leftWing;
        private final ModelPart leftWingTip;
        private final ModelPart leftFrontLeg;
        private final ModelPart leftFrontLegTip;
        private final ModelPart leftFrontFoot;
        private final ModelPart leftHindLeg;
        private final ModelPart leftHindLegTip;
        private final ModelPart leftHindFoot;
        private final ModelPart rightWing;
        private final ModelPart rightWingTip;
        private final ModelPart rightFrontLeg;
        private final ModelPart rightFrontLegTip;
        private final ModelPart rightFrontFoot;
        private final ModelPart rightHindLeg;
        private final ModelPart rightHindLegTip;
        private final ModelPart rightHindFoot;
        @Nullable
        private ImmortalEnderDragonEntity dragon;
        private float tickDelta;

        public DragonEntityModel(ModelPart part) {
            this.head = part.getChild(EntityModelPartNames.HEAD);
            this.jaw = this.head.getChild(EntityModelPartNames.JAW);
            this.neck = part.getChild(EntityModelPartNames.NECK);
            this.body = part.getChild(EntityModelPartNames.BODY);
            this.leftWing = part.getChild(EntityModelPartNames.LEFT_WING);
            this.leftWingTip = this.leftWing.getChild(EntityModelPartNames.LEFT_WING_TIP);
            this.leftFrontLeg = part.getChild(EntityModelPartNames.LEFT_FRONT_LEG);
            this.leftFrontLegTip = this.leftFrontLeg.getChild(EntityModelPartNames.LEFT_FRONT_LEG_TIP);
            this.leftFrontFoot = this.leftFrontLegTip.getChild(EntityModelPartNames.LEFT_FRONT_FOOT);
            this.leftHindLeg = part.getChild(EntityModelPartNames.LEFT_HIND_LEG);
            this.leftHindLegTip = this.leftHindLeg.getChild(EntityModelPartNames.LEFT_HIND_LEG_TIP);
            this.leftHindFoot = this.leftHindLegTip.getChild(EntityModelPartNames.LEFT_HIND_FOOT);
            this.rightWing = part.getChild(EntityModelPartNames.RIGHT_WING);
            this.rightWingTip = this.rightWing.getChild(EntityModelPartNames.RIGHT_WING_TIP);
            this.rightFrontLeg = part.getChild(EntityModelPartNames.RIGHT_FRONT_LEG);
            this.rightFrontLegTip = this.rightFrontLeg.getChild(EntityModelPartNames.RIGHT_FRONT_LEG_TIP);
            this.rightFrontFoot = this.rightFrontLegTip.getChild(EntityModelPartNames.RIGHT_FRONT_FOOT);
            this.rightHindLeg = part.getChild(EntityModelPartNames.RIGHT_HIND_LEG);
            this.rightHindLegTip = this.rightHindLeg.getChild(EntityModelPartNames.RIGHT_HIND_LEG_TIP);
            this.rightHindFoot = this.rightHindLegTip.getChild(EntityModelPartNames.RIGHT_HIND_FOOT);
        }

        @Override
        public void animateModel(ImmortalEnderDragonEntity enderDragonEntity, float f, float g, float h) {
            this.dragon = enderDragonEntity;
            this.tickDelta = h;
        }

        @Override
        public void setAngles(ImmortalEnderDragonEntity enderDragonEntity, float f, float g, float h, float i, float j) {
        }

        @Override
        public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
            float p;
            matrices.push();
            float f = MathHelper.lerp(this.tickDelta, this.dragon.prevWingPosition, this.dragon.wingPosition);
            this.jaw.pitch = (float) (Math.sin(f * ((float) Math.PI * 2)) + 1.0) * 0.2f;
            float g = (float) (Math.sin(f * ((float) Math.PI * 2) - 1.0f) + 1.0);
            g = (g * g + g * 2.0f) * 0.05f;
            matrices.translate(0.0, g - 2.0f, -3.0);
            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(g * 2.0f));
            float h = 0.0f;
            float i = 20.0f;
            float j = -12.0f;
            float k = 1.5f;
            double[] ds = this.dragon.getSegmentProperties(6, this.tickDelta);
            float l = MathHelper.fwrapDegrees(this.dragon.getSegmentProperties(5, this.tickDelta)[0] - this.dragon.getSegmentProperties(10, this.tickDelta)[0]);
            float m = MathHelper.fwrapDegrees(this.dragon.getSegmentProperties(5, this.tickDelta)[0] + (double) (l / 2.0f));
            float n = f * ((float) Math.PI * 2);
            for (int o = 0; o < 5; ++o) {
                double[] es = this.dragon.getSegmentProperties(5 - o, this.tickDelta);
                p = (float) Math.cos((float) o * 0.45f + n) * 0.15f;
                this.neck.yaw = MathHelper.fwrapDegrees(es[0] - ds[0]) * ((float) Math.PI / 180) * 1.5f;
                this.neck.pitch = p + this.dragon.getChangeInNeckPitch(o, ds, es) * ((float) Math.PI / 180) * 1.5f * 5.0f;
                this.neck.roll = -MathHelper.fwrapDegrees(es[0] - (double) m) * ((float) Math.PI / 180) * 1.5f;
                this.neck.pivotY = i;
                this.neck.pivotZ = j;
                this.neck.pivotX = h;
                i += MathHelper.sin(this.neck.pitch) * 10.0f;
                j -= MathHelper.cos(this.neck.yaw) * MathHelper.cos(this.neck.pitch) * 10.0f;
                h -= MathHelper.sin(this.neck.yaw) * MathHelper.cos(this.neck.pitch) * 10.0f;
                this.neck.render(matrices, vertices, light, overlay, red, green, blue, alpha);
            }
            this.head.pivotY = i;
            this.head.pivotZ = j;
            this.head.pivotX = h;
            double[] fs = this.dragon.getSegmentProperties(0, this.tickDelta);
            this.head.yaw = MathHelper.fwrapDegrees(fs[0] - ds[0]) * ((float) Math.PI / 180);
            this.head.pitch = MathHelper.fwrapDegrees(this.dragon.getChangeInNeckPitch(6, ds, fs)) * ((float) Math.PI / 180) * 1.5f * 5.0f;
            this.head.roll = -MathHelper.fwrapDegrees(fs[0] - (double) m) * ((float) Math.PI / 180);
            this.head.render(matrices, vertices, light, overlay, red, green, blue, alpha);
            matrices.push();
            matrices.translate(0.0, 1.0, 0.0);
            matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(-l * 1.5f));
            matrices.translate(0.0, -1.0, 0.0);
            this.body.roll = 0.0f;
            this.body.render(matrices, vertices, light, overlay, red, green, blue, alpha);
            float q = f * ((float) Math.PI * 2);
            this.leftWing.pitch = 0.125f - (float) Math.cos(q) * 0.2f;
            this.leftWing.yaw = -0.25f;
            this.leftWing.roll = -((float) (Math.sin(q) + 0.125)) * 0.8f;
            this.leftWingTip.roll = (float) (Math.sin(q + 2.0f) + 0.5) * 0.75f;
            this.rightWing.pitch = this.leftWing.pitch;
            this.rightWing.yaw = -this.leftWing.yaw;
            this.rightWing.roll = -this.leftWing.roll;
            this.rightWingTip.roll = -this.leftWingTip.roll;
            this.setLimbRotation(matrices, vertices, light, overlay, g, this.leftWing, this.leftFrontLeg, this.leftFrontLegTip, this.leftFrontFoot, this.leftHindLeg, this.leftHindLegTip, this.leftHindFoot, red, green, blue, alpha);
            this.setLimbRotation(matrices, vertices, light, overlay, g, this.rightWing, this.rightFrontLeg, this.rightFrontLegTip, this.rightFrontFoot, this.rightHindLeg, this.rightHindLegTip, this.rightHindFoot, red, green, blue, alpha);
            matrices.pop();
            p = -MathHelper.sin(f * ((float) Math.PI * 2)) * 0.0f;
            n = f * ((float) Math.PI * 2);
            i = 10.0f;
            j = 60.0f;
            h = 0.0f;
            ds = this.dragon.getSegmentProperties(11, this.tickDelta);
            for (int r = 0; r < 12; ++r) {
                fs = this.dragon.getSegmentProperties(12 + r, this.tickDelta);
                this.neck.yaw = (MathHelper.fwrapDegrees(fs[0] - ds[0]) * 1.5f + 180.0f) * ((float) Math.PI / 180);
                this.neck.pitch = (p += MathHelper.sin((float) r * 0.45f + n) * 0.05f) + (float) (fs[1] - ds[1]) * ((float) Math.PI / 180) * 1.5f * 5.0f;
                this.neck.roll = MathHelper.fwrapDegrees(fs[0] - (double) m) * ((float) Math.PI / 180) * 1.5f;
                this.neck.pivotY = i;
                this.neck.pivotZ = j;
                this.neck.pivotX = h;
                i += MathHelper.sin(this.neck.pitch) * 10.0f;
                j -= MathHelper.cos(this.neck.yaw) * MathHelper.cos(this.neck.pitch) * 10.0f;
                h -= MathHelper.sin(this.neck.yaw) * MathHelper.cos(this.neck.pitch) * 10.0f;
                this.neck.render(matrices, vertices, light, overlay, red, green, blue, alpha);
            }
            matrices.pop();
        }

        private void setLimbRotation(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float offset, ModelPart wing, ModelPart frontLeg, ModelPart frontLegTip, ModelPart frontFoot, ModelPart hindLeg, ModelPart hindLegTip, ModelPart hindFoot, float red, float green, float blue, float alpha) {
            hindLeg.pitch = 1.0f + offset * 0.1f;
            hindLegTip.pitch = 0.5f + offset * 0.1f;
            hindFoot.pitch = 0.75f + offset * 0.1f;
            frontLeg.pitch = 1.3f + offset * 0.1f;
            frontLegTip.pitch = -0.5f - offset * 0.1f;
            frontFoot.pitch = 0.75f + offset * 0.1f;
            wing.render(matrices, vertices, light, overlay, red, green, blue, alpha);
            frontLeg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
            hindLeg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        }
    }
}