package de.takacick.onenukeblock.client.item.model;

import de.takacick.onenukeblock.client.utils.SpotlightRenderUtils;
import de.takacick.onenukeblock.registry.block.entity.NukeOneBlockEntity;
import de.takacick.utils.item.client.model.SinglePartItemModel;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class OneNukeBlockItemModel extends SinglePartItemModel {
    private final ModelPart bone;
    private final ModelPart base;
    private final ModelPart redstone;
    private final ModelPart signal;
    private final ModelPart light;
    private final List<ModelPart> lights = new ArrayList<>();

    public OneNukeBlockItemModel(ModelPart root) {
        super(root, RenderLayer::getEntityTranslucent);

        this.bone = root.getChild("bone");
        this.base = this.bone.getChild("base");
        this.redstone = this.bone.getChild("redstone");
        this.signal = this.redstone.getChild("signal");
        this.light = this.signal.getChild("light");

        this.light.children.forEach((s, modelPart) -> {
            this.lights.add(modelPart);
        });
    }

    @Override
    public void setAngles(ItemStack itemStack, LivingEntity livingEntity, ModelTransformationMode modelTransformationMode, long time, float tickDelta) {

    }

    public void setAngles(NukeOneBlockEntity nukeOneBlockEntity, float fuse, float tickDelta) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);

        if (nukeOneBlockEntity.isIgnited()) {
            this.signal.yaw += (nukeOneBlockEntity.getMaxFuse() - fuse) * ((float) Math.PI / 180) * 10.8f * 2f;
        }
    }

    public ModelPart getSignal() {
        return this.signal;
    }


    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        super.render(matrices, vertices, light, overlay, color);
    }

    public void setBaseVisible(boolean visible) {
        this.base.visible = visible;
    }

    public void renderTnt(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        matrices.push();
        this.getPart().rotate(matrices);
        this.bone.rotate(matrices);
        this.base.render(matrices, vertices, light, overlay, color);
        matrices.pop();
    }

    public void renderLights(MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider) {
        matrices.push();
        this.getPart().rotate(matrices);
        this.bone.rotate(matrices);
        this.redstone.rotate(matrices);
        this.signal.rotate(matrices);
        this.light.rotate(matrices);

        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getBeaconBeam(SpotlightRenderUtils.SPOTLIGHT_TEXTURE, true));

        this.lights.forEach(modelPart -> {
            matrices.push();
            modelPart.rotate(matrices);

            float width = 0.135f;

            SpotlightRenderUtils.renderSpotlight(matrices, vertexConsumer,
                    1f, 0f, 0f, 1f, 0.25f, 1f, 0.0f, width, width, 0.0f, -width, 0.0f, 0.0f, -width, 0.0f, 1.0f, 0f, 0f);

            matrices.pop();
        });
        matrices.pop();
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 16.0F, 0.0F));

        ModelPartData base = bone.addChild("base", ModelPartBuilder.create().uv(0, 26).cuboid(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, new Dilation(0.0F))
                .uv(28, 91).cuboid(5.9999F, -9.0F, -8.9999F, 2.0F, 18.0F, 1.0F, new Dilation(0.0F))
                .uv(21, 91).cuboid(-7.9999F, -9.0F, -8.9999F, 2.0F, 18.0F, 1.0F, new Dilation(0.0F))
                .uv(14, 91).cuboid(-5.25F, -9.0F, -9.0F, 2.0F, 18.0F, 1.0F, new Dilation(0.0F))
                .uv(7, 91).cuboid(3.25F, -9.0F, -9.0F, 2.0F, 18.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 91).cuboid(0.5F, -9.0F, -9.0F, 2.0F, 18.0F, 1.0F, new Dilation(0.0F))
                .uv(89, 20).cuboid(-2.25F, -9.0F, -9.0F, 2.0F, 18.0F, 1.0F, new Dilation(0.0F))
                .uv(51, 102).cuboid(6.0F, -9.0F, -8.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(60, 102).cuboid(3.25F, -9.0F, -8.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(74, 102).cuboid(0.5F, -9.0F, -8.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(91, 102).cuboid(-2.25F, -9.0F, -8.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(103, 2).cuboid(-5.25F, -9.0F, -8.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(103, 6).cuboid(-8.0F, -9.0F, -8.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(98, 92).cuboid(6.0F, -9.0F, -5.25F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(98, 96).cuboid(3.25F, -9.0F, -5.25F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(35, 99).cuboid(0.5F, -9.0F, -5.25F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(44, 100).cuboid(-2.25F, -9.0F, -5.25F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(67, 100).cuboid(-5.25F, -9.0F, -5.25F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(98, 100).cuboid(-8.0F, -9.0F, -5.25F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(98, 64).cuboid(6.0F, -9.0F, 0.25F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(98, 68).cuboid(3.25F, -9.0F, 0.25F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(98, 72).cuboid(-5.25F, -9.0F, 0.25F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(98, 76).cuboid(-8.0F, -9.0F, 0.25F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(67, 96).cuboid(6.0F, -9.0F, 3.25F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(51, 98).cuboid(3.25F, -9.0F, 3.25F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(98, 52).cuboid(0.5F, -9.0F, 3.25F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(98, 56).cuboid(-2.25F, -9.0F, 3.25F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(60, 98).cuboid(-5.25F, -9.0F, 3.25F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(98, 60).cuboid(-8.0F, -9.0F, 3.25F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(96, 20).cuboid(6.0F, -9.0F, 6.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(96, 24).cuboid(3.25F, -9.0F, 6.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(96, 28).cuboid(0.5F, -9.0F, 6.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(96, 32).cuboid(-2.25F, -9.0F, 6.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(96, 36).cuboid(-5.25F, -9.0F, 6.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(44, 96).cuboid(-8.0F, -9.0F, 6.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(35, 95).cuboid(6.0F, 8.0F, 6.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(96, 0).cuboid(3.25F, 8.0F, 6.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(96, 4).cuboid(0.5F, 8.0F, 6.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(96, 8).cuboid(-2.25F, 8.0F, 6.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(96, 12).cuboid(-5.25F, 8.0F, 6.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(96, 16).cuboid(-8.0F, 8.0F, 6.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(91, 86).cuboid(6.0F, 8.0F, 3.25F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(91, 90).cuboid(3.25F, 8.0F, 3.25F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(44, 92).cuboid(0.5F, 8.0F, 3.25F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(51, 94).cuboid(-2.25F, 8.0F, 3.25F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(60, 94).cuboid(-5.25F, 8.0F, 3.25F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(91, 94).cuboid(-8.0F, 8.0F, 3.25F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(91, 62).cuboid(6.0F, 8.0F, 0.25F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(91, 66).cuboid(3.25F, 8.0F, 0.25F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(91, 70).cuboid(0.5F, 8.0F, 0.25F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(91, 74).cuboid(-2.25F, 8.0F, 0.25F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(91, 78).cuboid(-5.25F, 8.0F, 0.25F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(91, 82).cuboid(-8.0F, 8.0F, 0.25F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(74, 46).cuboid(6.0F, 8.0F, -2.5F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(80, 0).cuboid(3.25F, 8.0F, -2.5F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(35, 91).cuboid(0.5F, 8.0F, -2.5F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(91, 50).cuboid(-2.25F, 8.0F, -2.5F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(91, 54).cuboid(-5.25F, 8.0F, -2.5F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(91, 58).cuboid(-8.0F, 8.0F, -2.5F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(65, 46).cuboid(6.0F, 8.0F, -5.25F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(67, 15).cuboid(3.25F, 8.0F, -5.25F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(68, 62).cuboid(0.5F, 8.0F, -5.25F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(68, 66).cuboid(-2.25F, 8.0F, -5.25F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(70, 50).cuboid(-5.25F, 8.0F, -5.25F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(70, 54).cuboid(-8.0F, 8.0F, -5.25F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(49, 37).cuboid(6.0F, 8.0F, -8.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(58, 15).cuboid(3.25F, 8.0F, -8.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(16, 59).cuboid(0.5F, 8.0F, -8.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(34, 59).cuboid(-2.25F, 8.0F, -8.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(41, 61).cuboid(-5.25F, 8.0F, -8.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(34, 63).cuboid(-8.0F, 8.0F, -8.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(98, 80).cuboid(6.0F, -9.0F, -2.5F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(98, 84).cuboid(3.25F, -9.0F, -2.5F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(98, 88).cuboid(-5.25F, -9.0F, -2.5F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(91, 98).cuboid(-8.0F, -9.0F, -2.5F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(77, 81).cuboid(-7.9999F, -9.0F, 7.999F, 2.0F, 18.0F, 1.0F, new Dilation(0.0F))
                .uv(82, 15).cuboid(-5.25F, -9.0F, 8.0F, 2.0F, 18.0F, 1.0F, new Dilation(0.0F))
                .uv(84, 46).cuboid(-2.25F, -9.0F, 8.0F, 2.0F, 18.0F, 1.0F, new Dilation(0.0F))
                .uv(84, 66).cuboid(0.5F, -9.0F, 8.0F, 2.0F, 18.0F, 1.0F, new Dilation(0.0F))
                .uv(84, 86).cuboid(3.25F, -9.0F, 8.0F, 2.0F, 18.0F, 1.0F, new Dilation(0.0F))
                .uv(89, 0).cuboid(5.999F, -9.0F, 7.999F, 2.0F, 18.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 70).cuboid(7.9999F, -9.0F, 5.9999F, 1.0F, 18.0F, 2.0F, new Dilation(0.0F))
                .uv(7, 70).cuboid(8.0F, -9.0F, 3.25F, 1.0F, 18.0F, 2.0F, new Dilation(0.0F))
                .uv(14, 70).cuboid(8.0F, -9.0F, 0.25F, 1.0F, 18.0F, 2.0F, new Dilation(0.0F))
                .uv(21, 70).cuboid(8.0F, -9.0F, -2.5F, 1.0F, 18.0F, 2.0F, new Dilation(0.0F))
                .uv(28, 70).cuboid(8.0F, -9.0F, -5.25F, 1.0F, 18.0F, 2.0F, new Dilation(0.0F))
                .uv(35, 70).cuboid(7.9999F, -9.0F, -7.9999F, 1.0F, 18.0F, 2.0F, new Dilation(0.0F))
                .uv(42, 70).cuboid(-8.9999F, -9.0F, -7.9999F, 1.0F, 18.0F, 2.0F, new Dilation(0.0F))
                .uv(49, 71).cuboid(-9.0F, -9.0F, -5.25F, 1.0F, 18.0F, 2.0F, new Dilation(0.0F))
                .uv(56, 73).cuboid(-9.0F, -9.0F, -2.5F, 1.0F, 18.0F, 2.0F, new Dilation(0.0F))
                .uv(63, 73).cuboid(-9.0F, -9.0F, 0.25F, 1.0F, 18.0F, 2.0F, new Dilation(0.0F))
                .uv(70, 73).cuboid(-9.0F, -9.0F, 3.25F, 1.0F, 18.0F, 2.0F, new Dilation(0.0F))
                .uv(77, 60).cuboid(-8.999F, -9.0F, 5.9999F, 1.0F, 18.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData frame = base.addChild("frame", ModelPartBuilder.create().uv(0, 0).cuboid(-9.5F, -3.0F, -9.5F, 19.0F, 6.0F, 19.0F, new Dilation(0.0F))
                .uv(55, 62).cuboid(7.5F, -5.0F, -4.0F, 2.0F, 2.0F, 8.0F, new Dilation(0.0F))
                .uv(42, 59).cuboid(7.5F, 3.0F, -4.0F, 2.0F, 2.0F, 8.0F, new Dilation(0.0F))
                .uv(57, 51).cuboid(-9.5F, -5.0F, -4.0F, 2.0F, 2.0F, 8.0F, new Dilation(0.0F))
                .uv(21, 59).cuboid(-9.5F, 3.0F, -4.0F, 2.0F, 2.0F, 8.0F, new Dilation(0.0F))
                .uv(86, 40).cuboid(-4.0F, -5.0F, 7.5F, 8.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(91, 45).cuboid(-4.0F, 3.0F, 7.5F, 8.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(22, 103).cuboid(-9.5469F, -5.5F, -6.0F, 0.0F, 11.0F, 12.0F, new Dilation(0.0F))
                .uv(22, 103).mirrored().cuboid(9.5625F, -5.5F, -6.0F, 0.0F, 11.0F, 12.0F, new Dilation(0.0F)).mirrored(false)
                .uv(22, 115).cuboid(-6.0F, -5.5F, 9.6406F, 12.0F, 11.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.5F, 0.0F));

        ModelPartData redstone = bone.addChild("redstone", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 8.0F, 0.0F));

        ModelPartData signal = redstone.addChild("signal", ModelPartBuilder.create().uv(0, 59).cuboid(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new Dilation(-0.04F)), ModelTransform.pivot(0.0F, -20.5F, 0.0F));

        ModelPartData light = signal.addChild("light", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 20.5F, 0.0F));

        ModelPartData east_light = light.addChild("east_light", ModelPartBuilder.create(), ModelTransform.of(2.5F, -20.5F, 0.0F, 0.0F, 0.0F, 1.5708F));

        ModelPartData south_light = light.addChild("south_light", ModelPartBuilder.create(), ModelTransform.of(0.0F, -20.4F, 2.6F, -1.5708F, 0.0F, 0.0F));

        ModelPartData west_light = light.addChild("west_light", ModelPartBuilder.create(), ModelTransform.of(-2.5F, -20.5F, 0.0F, 0.0F, 0.0F, -1.5708F));

        ModelPartData north_light = light.addChild("north_light", ModelPartBuilder.create(), ModelTransform.of(0.0F, -20.4F, -2.4F, 1.5708F, 0.0F, 0.0F));

        ModelPartData glass = redstone.addChild("glass", ModelPartBuilder.create().uv(58, 0).cuboid(-3.5F, -25.0F, -3.5F, 7.0F, 7.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData chest = redstone.addChild("chest", ModelPartBuilder.create().uv(65, 37).cuboid(-3.5F, -3.5F, -12.0F, 7.0F, 5.0F, 3.0F, new Dilation(0.0F))
                .uv(49, 30).cuboid(-0.5F, -2.0F, -12.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -8.0F, 0.0F));

        ModelPartData wires = chest.addChild("wires", ModelPartBuilder.create().uv(9, 8).cuboid(2.5F, -9.5F, -7.0F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 36).cuboid(2.5F, -9.5F, -8.0F, 4.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(49, 26).cuboid(5.5F, -9.5F, -10.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(5, 26).cuboid(5.5F, -8.5F, -10.0F, 1.0F, 8.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 39).cuboid(3.5F, -1.5F, -10.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 8).cuboid(0.75F, -9.5F, -10.0F, 1.0F, 1.0F, 6.0F, new Dilation(0.0F))
                .uv(10, 26).cuboid(0.75F, -8.5F, -10.0F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 8).cuboid(1.75F, -6.5F, -10.0F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(10, 31).cuboid(-2.5F, -6.5F, -10.0F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(49, 26).cuboid(-4.0F, -10.0F, -4.0F, 8.0F, 2.0F, 8.0F, new Dilation(0.0F))
                .uv(9, 0).cuboid(-3.5F, -9.5F, -7.0F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 16).cuboid(-6.5F, -9.5F, -8.0F, 4.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(9, 37).cuboid(-6.5F, -9.5F, -10.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-1.5F, -9.5F, -10.0F, 1.0F, 1.0F, 6.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-1.5F, -8.5F, -10.0F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 26).cuboid(-6.5F, -8.5F, -10.0F, 1.0F, 8.0F, 1.0F, new Dilation(0.0F))
                .uv(11, 16).cuboid(-5.5F, -1.5F, -10.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 128, 128);
    }
}
