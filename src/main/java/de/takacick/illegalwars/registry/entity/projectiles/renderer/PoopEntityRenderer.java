package de.takacick.illegalwars.registry.entity.projectiles.renderer;

import de.takacick.illegalwars.IllegalWars;
import de.takacick.illegalwars.registry.entity.projectiles.PoopEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class PoopEntityRenderer extends EntityRenderer<PoopEntity> {

    private static final Identifier TEXTURE = new Identifier(IllegalWars.MOD_ID, "textures/entity/poop_launcher.png");

    private final ModelPart modelPart;

    public PoopEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.modelPart = getTexturedModelData().createModel();
    }

    public void render(PoopEntity poopEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.translate(0, 0.25, 0);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerpAngleDegrees(g, poopEntity.prevYaw, poopEntity.getYaw()) + 180));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(MathHelper.lerp(g, poopEntity.prevPitch, poopEntity.getPitch())));

        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(getTexture(poopEntity)));
        this.modelPart.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);

        matrixStack.pop();

        super.render(poopEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Identifier getTexture(PoopEntity poopEntity) {
        return TEXTURE;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData poop = modelPartData.addChild("poop", ModelPartBuilder.create().uv(7, 7).cuboid(0.0F, 0.0F, -7.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 7).cuboid(-1.0F, -1.0F, -6.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-2.0F, -2.0F, -5.0F, 4.0F, 4.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 64).cuboid(-3.0F, -3.0F, -3.0F, 7.0F, 7.0F, 2.0F, new Dilation(0.0F))
                .uv(33, 54).cuboid(-5.0F, -5.0F, -1.0F, 10.0F, 10.0F, 2.0F, new Dilation(0.0F))
                .uv(49, 33).cuboid(-6.0F, -6.0F, 1.0F, 12.0F, 12.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0f, 0.0F));
        return TexturedModelData.of(modelData, 128, 128);
    }
}
