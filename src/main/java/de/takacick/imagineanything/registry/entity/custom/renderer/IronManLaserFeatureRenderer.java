package de.takacick.imagineanything.registry.entity.custom.renderer;

import de.takacick.imagineanything.ImagineAnything;
import de.takacick.imagineanything.access.PlayerProperties;
import net.minecraft.client.model.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec3f;

import java.util.function.BiFunction;

public class IronManLaserFeatureRenderer<T extends PlayerEntity, M extends PlayerEntityModel<T>>
        extends FeatureRenderer<T, M> {
    public static final Identifier LASER = new Identifier(ImagineAnything.MOD_ID, "textures/entity/laser.png");
    private final ModelPart laser;
    private final ModelPart innerLaser;
    private static final BiFunction<Identifier, Boolean, RenderLayer> BEACON_BEAM = Util.memoize((texture, affectsOutline) -> {
        RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder()
                .shader(RenderLayer.BEACON_BEAM_SHADER).texture(new RenderPhase.Texture((Identifier) texture, false, false))
                .transparency(affectsOutline != false ? RenderLayer.TRANSLUCENT_TRANSPARENCY : RenderLayer.NO_TRANSPARENCY)
                .writeMaskState(affectsOutline != false ? RenderLayer.COLOR_MASK : RenderLayer.ALL_MASK).build(false);
        return RenderLayer.of("beacon_beam", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, false, true, multiPhaseParameters);
    });

    public IronManLaserFeatureRenderer(FeatureRendererContext<T, M> context) {
        super(context);
        this.laser = getTexturedModelData().createModel().getChild("laser");
        this.innerLaser = getTexturedModelData().createModel().getChild("innerLaser");
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
        if (!(livingEntity instanceof PlayerProperties playerProperties) || !playerProperties.hasIronManLaser()) {
            return;
        }

        matrixStack.push();
        matrixStack.push();
        float y = (float) livingEntity.getCameraPosVec(h).distanceTo(livingEntity.raycast(150, h, false).getPos()) + 0.3f;
        innerLaser.copyTransform(getContextModel().head);
        innerLaser.roll = 0f;
        innerLaser.scale(new Vec3f(0f, 0f, y));
        innerLaser.render(matrixStack, vertexConsumerProvider.getBuffer(BEACON_BEAM.apply(LASER, false)), i, OverlayTexture.DEFAULT_UV, 1f, 0f, 0, 1f);
        matrixStack.pop();

        matrixStack.push();
        laser.copyTransform(getContextModel().head);
        innerLaser.roll = 0f;
        laser.scale(new Vec3f(0f, 0f, y));
        laser.render(matrixStack, vertexConsumerProvider.getBuffer(BEACON_BEAM.apply(LASER, true)), i, OverlayTexture.DEFAULT_UV, 1f, 0f, 0, 0.125F);
        matrixStack.pop();

        matrixStack.pop();
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData laser = modelPartData.addChild("laser", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData laser_r1 = laser.addChild("laser_r1", ModelPartBuilder.create().uv(13, 6).cuboid(-3.0F, 1.0F, 0.0F, 0.0F, 6.0F, 16.0F, new Dilation(0.0F))
                .uv(13, 6).cuboid(-3.0F, 1.0F, 0.0F, 6.0F, 6.0F, 0.0F, new Dilation(0.0F))
                .uv(13, 6).cuboid(3.0F, 1.0F, 0.0F, 0.0F, 6.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

        ModelPartData laser_r2 = laser.addChild("laser_r2", ModelPartBuilder.create().uv(13, 6).cuboid(-3.0F, 1.0F, 8.0F, 6.0F, 6.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -8.0F, 3.1416F, 0.0F, 0.0F));

        ModelPartData laser_r3 = laser.addChild("laser_r3", ModelPartBuilder.create().uv(13, 6).cuboid(3.0F, -6.0F, 0.0F, 0.0F, 6.0F, 16.0F, new Dilation(0.0F))
                .uv(13, 6).cuboid(-3.0F, -6.0F, 0.0F, 0.0F, 6.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, -4.0F, 0.0F, 3.1416F, 0.0F, 1.5708F));

        ModelPartData innerLaser = modelPartData.addChild("innerLaser", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData laser_r4 = innerLaser.addChild("laser_r4", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, 2.0F, 0.0F, 4.0F, 4.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 3.1416F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }
}

