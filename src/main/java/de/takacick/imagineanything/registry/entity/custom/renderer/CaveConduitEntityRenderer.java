package de.takacick.imagineanything.registry.entity.custom.renderer;

import de.takacick.imagineanything.ImagineAnything;
import de.takacick.imagineanything.registry.entity.custom.CaveConduitEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.*;

import java.util.function.Function;

public class CaveConduitEntityRenderer extends EntityRenderer<CaveConduitEntity> {

    private static final Function<Identifier, RenderLayer> EYES = Util.memoize(texture -> {
        RenderPhase.Texture texture2 = new RenderPhase.Texture(texture, false, false);
        return RenderLayer.of("eyes", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, false, true, RenderLayer.MultiPhaseParameters.builder()
                .shader(RenderLayer.EYES_SHADER).texture(texture2).transparency(RenderLayer.ADDITIVE_TRANSPARENCY).writeMaskState(RenderLayer.COLOR_MASK).cull(RenderPhase.Cull.ENABLE_CULLING).build(false));
    });

    public static final Identifier CAGE_TEXTURE = new Identifier(ImagineAnything.MOD_ID, "textures/entity/conduit/cage.png");
    public static final SpriteIdentifier WIND_TEXTURE = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("entity/conduit/wind"));
    public static final SpriteIdentifier WIND_VERTICAL_TEXTURE = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("entity/conduit/wind_vertical"));
    public static final Identifier CRYSTAL_BEAM_TEXTURE = new Identifier(ImagineAnything.MOD_ID, "textures/entity/conduit/beam.png");
    private static final RenderLayer CRYSTAL_BEAM_LAYER = EYES.apply(CRYSTAL_BEAM_TEXTURE);

    private final ModelPart conduitWind;
    private final ModelPart conduit;

    private final ItemRenderer itemRenderer;

    public CaveConduitEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.conduitWind = ctx.getPart(EntityModelLayers.CONDUIT_WIND);
        this.conduit = ctx.getPart(EntityModelLayers.CONDUIT);
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public boolean shouldRender(CaveConduitEntity entity, Frustum frustum, double x, double y, double z) {
        return super.shouldRender(entity, frustum, x, y, z);
    }

    @Override
    public void render(CaveConduitEntity caveConduitEntity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        float g = (float) caveConduitEntity.age + tickDelta;

        matrices.push();
        matrices.translate(0, 0.35f, 0);

        float h = caveConduitEntity.getRotation(tickDelta) * 57.295776f;
        float k = MathHelper.sin(g * 0.1f) / 2.0f + 0.5f;
        k = k * k + k;
        int l = caveConduitEntity.age / 66 % 3;
        matrices.push();
        if (l == 1) {
            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90.0f));
        } else if (l == 2) {
            matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(90.0f));
        }
        VertexConsumer vertexConsumer2 = (l == 1 ? WIND_VERTICAL_TEXTURE : WIND_TEXTURE).getVertexConsumer(vertexConsumers, RenderLayer::getEntityCutoutNoCull);
        //  this.conduitWind.render(matrices, vertexConsumer2, light, OverlayTexture.DEFAULT_UV);
        vertexConsumer2 = (l == 1 ? WIND_VERTICAL_TEXTURE : WIND_TEXTURE).getVertexConsumer(vertexConsumers, identifier -> RenderLayer.getLightning());
        this.conduitWind.render(matrices, vertexConsumer2, light, OverlayTexture.DEFAULT_UV, 1f, 0f, 0f, 1f);
        matrices.pop();
        matrices.push();
        matrices.scale(0.875f, 0.875f, 0.875f);
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(180.0f));
        matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0f));
        this.conduitWind.render(matrices, vertexConsumer2, light, OverlayTexture.DEFAULT_UV, 1f, 0f, 0f, 1f);
        matrices.pop();

        Camera camera = this.dispatcher.camera;
        matrices.push();
        matrices.scale(0.5f, 0.5f, 0.5f);
        float m = -camera.getYaw();
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(m));
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(camera.getPitch()));
        matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0f));
        matrices.scale(-1.1333334f, -1.1333334f, 1.1333334f);
        this.itemRenderer.renderItem(caveConduitEntity.getItemStack(), ModelTransformation.Mode.NONE, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, caveConduitEntity.getId());
        matrices.pop();
        matrices.push();
        Vec3f vec3f = new Vec3f(0.5f, 1.0f, 0.5f);
        vec3f.normalize();
        matrices.multiply(vec3f.getDegreesQuaternion(h));
        this.conduit.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(CAGE_TEXTURE)), light, OverlayTexture.DEFAULT_UV);
        matrices.pop();
        matrices.pop();

        matrices.push();
        matrices.translate(0, -1.61f, 0);
        for (int i = 0; i < 4; i++) {
            matrices.push();

            BlockPos blockPos = caveConduitEntity.getBlockPos(i);
            if (blockPos != null && blockPos.getY() > -100) {
                float lx = (float) (blockPos.getX() + 0.5 - MathHelper.lerp(g, caveConduitEntity.prevX, caveConduitEntity.getX()));
                float mx = (float) (blockPos.getY() + 0.5 - MathHelper.lerp(g, caveConduitEntity.prevY, caveConduitEntity.getY()));
                float rx = (float) (blockPos.getZ() + 0.5 - MathHelper.lerp(g, caveConduitEntity.prevZ, caveConduitEntity.getZ()));
                renderCrystalBeam(caveConduitEntity, lx, mx, rx, g, caveConduitEntity.age, matrices, vertexConsumers, i);
            }
            matrices.pop();
        }
        matrices.pop();

        super.render(caveConduitEntity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(CaveConduitEntity entity) {
        return null;
    }

    public static void renderCrystalBeam(CaveConduitEntity caveConduitEntity, float dx, float dy, float dz, float tickDelta, int age, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
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
            vertexConsumer.vertex(matrix4f, k * 0.2f, l * 0.2f, 0.0f).color(255, 0, 0, 255).texture(m, h).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0f, -1.0f, 0.0f).next();
            vertexConsumer.vertex(matrix4f, k, l, g).color(230, 100, 16, 255).texture(m, i).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0f, -1.0f, 0.0f).next();
            vertexConsumer.vertex(matrix4f, o, p, g).color(230, 100, 16, 255).texture(q, i).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0f, -1.0f, 0.0f).next();
            vertexConsumer.vertex(matrix4f, o * 0.2f, p * 0.2f, 0.0f).color(255, 0, 0, 255).texture(q, h).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0f, -1.0f, 0.0f).next();
            k = o;
            l = p;
            m = q;
        }
        matrices.pop();
    }
}
