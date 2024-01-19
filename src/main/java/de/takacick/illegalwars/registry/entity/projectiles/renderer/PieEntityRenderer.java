package de.takacick.illegalwars.registry.entity.projectiles.renderer;

import de.takacick.illegalwars.IllegalWars;
import de.takacick.illegalwars.registry.entity.projectiles.PieEntity;
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
public class PieEntityRenderer extends EntityRenderer<PieEntity> {

    private static final Identifier TEXTURE = new Identifier(IllegalWars.MOD_ID, "textures/entity/pie_launcher.png");

    private final ModelPart modelPart;

    public PieEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.modelPart = getTexturedModelData().createModel();
    }

    public void render(PieEntity pieEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.translate(0, -0.75, 0);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerpAngleDegrees(g, pieEntity.prevYaw, pieEntity.getYaw()) + 180));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(MathHelper.lerp(g, pieEntity.prevPitch, pieEntity.getPitch())));

        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(getTexture(pieEntity)));
        this.modelPart.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);

        matrixStack.pop();

        super.render(pieEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Identifier getTexture(PieEntity pieEntity) {
        return TEXTURE;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData pie = modelPartData.addChild("pie", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 15.5F, 0.0F));

        ModelPartData pie_bottom = pie.addChild("pie_bottom", ModelPartBuilder.create().uv(27, 54).cuboid(-6.0F, -14.25F, 0.75F, 12.0F, 12.0F, 1.0F, new Dilation(0.0F))
                .uv(65, 29).cuboid(-5.0F, -15.25F, 0.75F, 10.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 43).cuboid(-3.0F, -16.25F, 0.75F, 6.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 40).cuboid(-3.0F, -1.25F, 0.75F, 6.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(37, 68).cuboid(-8.0F, -11.25F, 0.75F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F))
                .uv(49, 33).cuboid(7.0F, -11.25F, 0.75F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F))
                .uv(65, 26).cuboid(-5.0F, -2.25F, 0.75F, 10.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(54, 0).cuboid(-7.0F, -13.25F, 0.75F, 1.0F, 10.0F, 1.0F, new Dilation(0.0F))
                .uv(49, 0).cuboid(6.0F, -13.25F, 0.75F, 1.0F, 10.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 8.2F, 0.3F));

        ModelPartData pie_center = pie.addChild("pie_center", ModelPartBuilder.create().uv(21, 68).cuboid(-8.5F, -11.25F, -1.25F, 1.0F, 6.0F, 2.0F, new Dilation(0.0F))
                .uv(52, 67).cuboid(-3.0F, -0.75F, -1.25F, 6.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(65, 47).cuboid(-3.0F, -16.75F, -1.25F, 6.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(7, 68).cuboid(-7.5F, -13.25F, -1.25F, 1.0F, 10.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 68).cuboid(6.5F, -13.25F, -1.25F, 1.0F, 10.0F, 2.0F, new Dilation(0.0F))
                .uv(65, 22).cuboid(-5.0F, -1.75F, -1.25F, 10.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(65, 18).cuboid(-5.0F, -15.75F, -1.25F, 10.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(65, 14).cuboid(-6.5F, -2.75F, -1.25F, 13.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(49, 43).cuboid(-6.5F, -14.75F, -1.25F, 13.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 54).cuboid(-5.5F, -13.75F, -0.75F, 11.0F, 11.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 33).cuboid(-2.0F, -10.25F, -1.25F, 4.0F, 4.0F, 2.0F, new Dilation(0.0F))
                .uv(28, 68).cuboid(2.5F, -11.75F, -1.25F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(67, 69).cuboid(0.0F, -4.75F, -1.25F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(42, 68).cuboid(-4.5F, -12.25F, -1.25F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(7, 0).cuboid(5.5F, -13.75F, -1.25F, 1.0F, 11.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-6.5F, -13.75F, -1.25F, 1.0F, 11.0F, 2.0F, new Dilation(0.0F))
                .uv(14, 68).cuboid(7.5F, -11.25F, -1.25F, 1.0F, 6.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 8.2F, 0.3F));

        ModelPartData pie_top = pie.addChild("pie_top", ModelPartBuilder.create().uv(46, 75).cuboid(14.0F, -0.25F, -0.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(41, 75).cuboid(16.0F, -8.75F, -0.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(48, 71).cuboid(16.0F, -6.75F, -0.75F, 1.0F, 2.0F, 1.0F, new Dilation(0.001F))
                .uv(32, 75).cuboid(16.0F, -3.75F, -0.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(57, 74).cuboid(15.0F, -2.75F, -0.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(52, 74).cuboid(15.0F, -9.75F, -0.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(74, 38).cuboid(1.0F, -9.75F, -0.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(74, 35).cuboid(0.0F, -8.75F, -0.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(54, 54).cuboid(0.0F, -6.75F, -0.75F, 1.0F, 2.0F, 1.0F, new Dilation(0.001F))
                .uv(74, 32).cuboid(0.0F, -3.75F, -0.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(73, 72).cuboid(1.0F, -2.75F, -0.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(68, 73).cuboid(11.5F, -13.25F, -0.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(73, 57).cuboid(10.5F, -14.25F, -0.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(7, 46).cuboid(7.5F, -14.25F, -0.75F, 2.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(73, 54).cuboid(5.5F, -14.25F, -0.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(28, 73).cuboid(4.5F, -13.25F, -0.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(72, 67).cuboid(11.5F, 0.75F, -0.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(63, 72).cuboid(10.5F, 1.75F, -0.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(0, 46).cuboid(7.5F, 1.75F, -0.75F, 2.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(42, 72).cuboid(5.5F, 1.75F, -0.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(58, 71).cuboid(4.5F, 0.75F, -0.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(53, 71).cuboid(14.0F, -12.25F, -0.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(70, 51).cuboid(2.0F, -12.25F, -0.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(65, 51).cuboid(2.0F, -0.25F, -0.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F)), ModelTransform.pivot(-8.5F, 5.7F, -0.7F));

        ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 20.0F, 0.0F));

        ModelPartData ground = bone.addChild("ground", ModelPartBuilder.create(), ModelTransform.pivot(8.0F, 4.0F, -8.0F));

        ModelPartData launcher = bone.addChild("launcher", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -0.1667F, 0.0F));

        ModelPartData dispenser = launcher.addChild("dispenser", ModelPartBuilder.create(), ModelTransform.pivot(8.0F, 4.1667F, -8.0F));

        ModelPartData legs = launcher.addChild("legs", ModelPartBuilder.create(), ModelTransform.pivot(8.0F, 4.1667F, -8.0F));

        ModelPartData bottom = modelPartData.addChild("bottom", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 20.75F, 0.0F));
        return TexturedModelData.of(modelData, 128, 128);
    }
}
