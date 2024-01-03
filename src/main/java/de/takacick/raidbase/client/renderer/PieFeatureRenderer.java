package de.takacick.raidbase.client.renderer;

import de.takacick.raidbase.RaidBase;
import de.takacick.raidbase.access.LivingProperties;
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
public class PieFeatureRenderer<T extends LivingEntity, M extends BipedEntityModel<T>>
        extends FeatureRenderer<T, M> {

    private static final Identifier TEXTURE = new Identifier(RaidBase.MOD_ID, "textures/entity/pie_launcher.png");
    private final ModelPart pie;

    public PieFeatureRenderer(FeatureRendererContext<T, M> context) {
        super(context);
        this.pie = getTexturedModelData().createModel().getChild("head");
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (!(entity instanceof LivingProperties livingProperties && livingProperties.hasPie())) {
            return;
        }

        matrices.push();
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(this.getContextModel().getLayer(TEXTURE));
        this.pie.copyTransform(this.getContextModel().head);
        this.pie.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
        matrices.pop();
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData pie = head.addChild("pie", ModelPartBuilder.create(), ModelTransform.of(0.0F, 16.0F, -13.0F, 0.0F, 3.1416F, 0.0F));

        ModelPartData pie_bottom = pie.addChild("pie_bottom", ModelPartBuilder.create().uv(27, 54).cuboid(-6.0F, -25.25F, -9.25F, 12.0F, 12.0F, 1.0F, new Dilation(0.0F))
                .uv(65, 29).cuboid(-5.0F, -26.25F, -9.25F, 10.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 43).cuboid(-3.0F, -27.25F, -9.25F, 6.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 40).cuboid(-3.0F, -12.25F, -9.25F, 6.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(37, 68).cuboid(-8.0F, -22.25F, -9.25F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F))
                .uv(49, 33).cuboid(7.0F, -22.25F, -9.25F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F))
                .uv(65, 26).cuboid(-5.0F, -13.25F, -9.25F, 10.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(54, 0).cuboid(-7.0F, -24.25F, -9.25F, 1.0F, 10.0F, 1.0F, new Dilation(0.0F))
                .uv(49, 0).cuboid(6.0F, -24.25F, -9.25F, 1.0F, 10.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -0.5F, 0.3F));

        ModelPartData pie_center = pie.addChild("pie_center", ModelPartBuilder.create().uv(21, 68).cuboid(-8.5F, -22.25F, -11.25F, 1.0F, 6.0F, 2.0F, new Dilation(0.0F))
                .uv(52, 67).cuboid(-3.0F, -11.75F, -11.25F, 6.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(65, 47).cuboid(-3.0F, -27.75F, -11.25F, 6.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(7, 68).cuboid(-7.5F, -24.25F, -11.25F, 1.0F, 10.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 68).cuboid(6.5F, -24.25F, -11.25F, 1.0F, 10.0F, 2.0F, new Dilation(0.0F))
                .uv(65, 22).cuboid(-5.0F, -12.75F, -11.25F, 10.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(65, 18).cuboid(-5.0F, -26.75F, -11.25F, 10.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(65, 14).cuboid(-6.5F, -13.75F, -11.25F, 13.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(49, 43).cuboid(-6.5F, -25.75F, -11.25F, 13.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 54).cuboid(-5.5F, -24.75F, -10.75F, 11.0F, 11.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 33).cuboid(-2.0F, -21.25F, -11.25F, 4.0F, 4.0F, 2.0F, new Dilation(0.0F))
                .uv(28, 68).cuboid(2.5F, -22.75F, -11.25F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(67, 69).cuboid(0.0F, -15.75F, -11.25F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(42, 68).cuboid(-4.5F, -23.25F, -11.25F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(7, 0).cuboid(5.5F, -24.75F, -11.25F, 1.0F, 11.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-6.5F, -24.75F, -11.25F, 1.0F, 11.0F, 2.0F, new Dilation(0.0F))
                .uv(14, 68).cuboid(7.5F, -22.25F, -11.25F, 1.0F, 6.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -0.5F, 0.3F));

        ModelPartData pie_top = pie.addChild("pie_top", ModelPartBuilder.create().uv(46, 75).cuboid(14.0F, -11.25F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(41, 75).cuboid(16.0F, -19.75F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(48, 71).cuboid(16.0F, -17.75F, -10.75F, 1.0F, 2.0F, 1.0F, new Dilation(0.001F))
                .uv(32, 75).cuboid(16.0F, -14.75F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(57, 74).cuboid(15.0F, -13.75F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(52, 74).cuboid(15.0F, -20.75F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(74, 38).cuboid(1.0F, -20.75F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(74, 35).cuboid(0.0F, -19.75F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(54, 54).cuboid(0.0F, -17.75F, -10.75F, 1.0F, 2.0F, 1.0F, new Dilation(0.001F))
                .uv(74, 32).cuboid(0.0F, -14.75F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(73, 72).cuboid(1.0F, -13.75F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(68, 73).cuboid(11.5F, -24.25F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(73, 57).cuboid(10.5F, -25.25F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(7, 46).cuboid(7.5F, -25.25F, -10.75F, 2.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(73, 54).cuboid(5.5F, -25.25F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(28, 73).cuboid(4.5F, -24.25F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(72, 67).cuboid(11.5F, -10.25F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(63, 72).cuboid(10.5F, -9.25F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(0, 46).cuboid(7.5F, -9.25F, -10.75F, 2.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(42, 72).cuboid(5.5F, -9.25F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(58, 71).cuboid(4.5F, -10.25F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(53, 71).cuboid(14.0F, -23.25F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(70, 51).cuboid(2.0F, -23.25F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(65, 51).cuboid(2.0F, -11.25F, -10.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.001F)), ModelTransform.pivot(-8.5F, -3.0F, -0.7F));
        return TexturedModelData.of(modelData, 128, 128);
    }
}

