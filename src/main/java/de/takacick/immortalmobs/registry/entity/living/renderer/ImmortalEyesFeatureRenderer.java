package de.takacick.immortalmobs.registry.entity.living.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

@Environment(value = EnvType.CLIENT)
public class ImmortalEyesFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>> extends EyesFeatureRenderer<T, M> {
    private final RenderLayer SKIN;
    private float r = 1f;
    private float g = 1f;
    private float b = 1f;

    public ImmortalEyesFeatureRenderer(FeatureRendererContext<T, M> featureRendererContext, Identifier skin) {
        super(featureRendererContext);
        SKIN = RenderLayer.getEyes(skin);

        float[] hs = SheepEntity.getRgbColor(DyeColor.PURPLE);
        this.r = hs[0];
        this.g = hs[1];
        this.b = hs[2];
    }


    public ImmortalEyesFeatureRenderer(FeatureRendererContext<T, M> featureRendererContext, Identifier skin, float r, float g, float b) {
        super(featureRendererContext);

        SKIN = RenderLayer.getEyes(skin);
        this.r = r;
        this.g = g;
        this.b = b;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(this.getEyesTexture());
        this.getContextModel().render(matrices, vertexConsumer, 0xF00000, OverlayTexture.DEFAULT_UV, r, g, b, 1.0f);
        this.getContextModel().render(matrices, vertexConsumer, 0xF00000, OverlayTexture.DEFAULT_UV, r, g, b, 1.0f);
    }

    @Override
    public RenderLayer getEyesTexture() {
        return SKIN;
    }
}
