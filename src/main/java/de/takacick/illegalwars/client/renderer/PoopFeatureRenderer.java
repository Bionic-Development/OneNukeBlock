package de.takacick.illegalwars.client.renderer;

import de.takacick.illegalwars.IllegalWars;
import de.takacick.illegalwars.access.LivingProperties;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

@Environment(value = EnvType.CLIENT)
public class PoopFeatureRenderer<T extends LivingEntity, M extends BipedEntityModel<T>>
        extends FeatureRenderer<T, M> {

    private static final Identifier TEXTURE = new Identifier(IllegalWars.MOD_ID, "textures/entity/poop_launcher.png");
    private final ModelPart poop;

    public PoopFeatureRenderer(FeatureRendererContext<T, M> context) {
        super(context);
        this.poop = getTexturedModelData().createModel().getChild("head");
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (!(entity instanceof LivingProperties livingProperties && livingProperties.hasPoop())) {
            return;
        }

        matrices.push();
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(this.getContextModel().getLayer(TEXTURE));
        this.poop.copyTransform(this.getContextModel().head);
        this.poop.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
        matrices.pop();
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData poop = head.addChild("poop", ModelPartBuilder.create().uv(7, 7).cuboid(-0.1667F, -0.1667F, -4.4167F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 7).cuboid(-1.1667F, -1.1667F, -3.4167F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-2.1667F, -2.1667F, -2.4167F, 4.0F, 4.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 64).cuboid(-3.1667F, -3.1667F, -0.4167F, 7.0F, 7.0F, 2.0F, new Dilation(0.0F))
                .uv(33, 54).cuboid(-5.1667F, -5.1667F, 1.5833F, 10.0F, 10.0F, 2.0F, new Dilation(0.0F))
                .uv(49, 33).cuboid(-6.1667F, -6.1667F, 3.5833F, 12.0F, 12.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.1667F, -3.8333F, -1.0833F, 0.0F, 3.1416F, 0.0F));
        return TexturedModelData.of(modelData, 128, 128);
    }
}

