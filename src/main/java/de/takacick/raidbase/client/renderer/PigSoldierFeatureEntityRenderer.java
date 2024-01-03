package de.takacick.raidbase.client.renderer;

import de.takacick.raidbase.client.model.PigSoldierEntityModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.util.Identifier;

@Environment(value = EnvType.CLIENT)
public class PigSoldierFeatureEntityRenderer<T extends PigEntity, M extends PigSoldierEntityModel<T>>
        extends FeatureRenderer<T, M> {

    private static final Identifier TEXTURE = new Identifier("textures/models/armor/turtle_layer_1.png");

    public PigSoldierFeatureEntityRenderer(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        matrices.push();
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(this.getContextModel().getLayer(TEXTURE));
        this.getContextModel().getPart().rotate(matrices);
        this.getContextModel().bone.rotate(matrices);
        this.getContextModel().head.rotate(matrices);

        this.getContextModel().helmet.visible = true;
        this.getContextModel().helmet.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
        matrices.pop();
    }
}

