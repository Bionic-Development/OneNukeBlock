package de.takacick.tinyhouse.registry.block.entity.renderer;

import de.takacick.tinyhouse.TinyHouse;
import de.takacick.tinyhouse.registry.block.entity.AerialChickenCannonBlockEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.joml.Vector3f;

public class AerialChickenCannonBlockEntityRenderer implements BlockEntityRenderer<AerialChickenCannonBlockEntity> {

    private static final Identifier TEXTURE = new Identifier(TinyHouse.MOD_ID, "textures/entity/aerial_chicken_cannon.png");
    private final ModelPart base;
    private final ModelPart cannon;
    private final ModelPart chicken;

    public AerialChickenCannonBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        ModelPart root = getTexturedModelData().createModel();
        this.base = root.getChild("base");
        this.cannon = root.getChild("cannon");
        this.chicken = root.getChild("chicken");
    }

    @Override
    public void render(AerialChickenCannonBlockEntity blockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int j) {
        float rotation = blockEntity.getYaw();
        float progress = blockEntity.getLoadingProgress(tickDelta);
        matrixStack.translate(0.5, 1.501, 0.5);
        matrixStack.scale(-1, -1, 1);

        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation - 180f));
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutout(TEXTURE));
        //  this.model.yaw = rotation * ((float) Math.PI / 180);
        this.base.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1.0f);
        this.cannon.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1.0f);
        this.chicken.resetTransform();

        if(progress > 0) {
            this.chicken.translate(new Vector3f(0, 0, 3.5f * (1 - progress)));
            this.chicken.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1.0f);
        }
    }

    @Override
    public int getRenderDistance() {
        return 256;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData chicken = modelPartData.addChild("chicken", ModelPartBuilder.create().uv(0, 51).cuboid(-1.0F, -10.25F, -9.75F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F))
                .uv(14, 51).cuboid(-1.0F, -9.25F, -10.75F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(14, 55).cuboid(-0.5F, -8.25F, -10.25F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 24.1F, -7.6F, -0.0436F, 0.0F, 0.0F));

        ModelPartData cannon = modelPartData.addChild("cannon", ModelPartBuilder.create().uv(15, 14).cuboid(-1.5F, 1.4896F, 0.0417F, 3.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 28).cuboid(-1.5F, -1.5104F, -12.9583F, 3.0F, 3.0F, 7.0F, new Dilation(0.05F))
                .uv(0, 14).cuboid(-2.0F, -2.5104F, 1.0417F, 4.0F, 5.0F, 6.0F, new Dilation(0.0F))
                .uv(19, 28).cuboid(-1.0F, -1.0104F, 7.0417F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(27, 22).cuboid(-2.0F, -2.0104F, -4.9583F, 4.0F, 4.0F, 6.0F, new Dilation(0.0F))
                .uv(20, 41).cuboid(1.5F, -2.0104F, -14.4583F, 1.0F, 4.0F, 1.0F, new Dilation(0.01F))
                .uv(15, 41).cuboid(-2.5F, -2.0104F, -14.4583F, 1.0F, 4.0F, 1.0F, new Dilation(0.01F))
                .uv(26, 33).cuboid(-1.5F, 1.4896F, -14.4583F, 3.0F, 1.0F, 1.0F, new Dilation(0.01F))
                .uv(26, 33).cuboid(-1.5F, -2.2604F, -14.4583F, 3.0F, 1.0F, 1.0F, new Dilation(0.01F))
                .uv(32, 41).cuboid(-1.0F, 0.9896F, -13.7083F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(25, 41).cuboid(-1.0F, -2.0104F, -13.7083F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(41, 8).cuboid(1.0F, -2.0104F, -13.7083F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
                .uv(40, 16).cuboid(-2.0F, -2.0104F, -13.7083F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
                .uv(26, 36).cuboid(-1.0F, 0.9896F, -5.9583F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 31).cuboid(-1.0F, -2.0104F, -5.9583F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(10, 40).cuboid(1.0F, -2.0104F, -5.9583F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
                .uv(36, 8).cuboid(-2.0F, -2.0104F, -5.9583F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
                .uv(5, 39).cuboid(-2.5F, -2.5104F, -3.9583F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(33, 0).cuboid(-1.5F, -2.5104F, -3.9583F, 3.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 39).cuboid(1.5F, -2.5104F, -3.9583F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(31, 16).cuboid(-1.5F, 1.4896F, -3.9583F, 3.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(14, 28).cuboid(-2.5F, -2.5104F, 0.0417F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(24, 22).cuboid(-1.5F, -2.5104F, 0.0417F, 3.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(28, 0).cuboid(1.5F, -2.5104F, 0.0417F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 15.5104F, -1.5417F, -0.0436F, 0.0F, 0.0F));

        ModelPartData base = modelPartData.addChild("base", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData right_wheel = base.addChild("right_wheel", ModelPartBuilder.create().uv(36, 0).mirrored().cuboid(-11.5F, -6.0F, 11.5F, 1.0F, 1.0F, 6.0F, new Dilation(0.0F)).mirrored(false)
                .uv(36, 0).mirrored().cuboid(-11.5F, -6.0F, 11.5F, 1.0F, 1.0F, 6.0F, new Dilation(0.0F)).mirrored(false)
                .uv(36, 0).mirrored().cuboid(-11.5F, -1.0F, 11.5F, 1.0F, 1.0F, 6.0F, new Dilation(0.0F)).mirrored(false)
                .uv(0, 14).mirrored().cuboid(-11.5F, -5.0F, 16.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(0, 14).mirrored().cuboid(-11.5F, -5.0F, 11.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(0, 14).mirrored().cuboid(-11.5F, -5.0F, 14.0F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(24, 16).mirrored().cuboid(-11.5F, -3.5F, 12.5F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F)).mirrored(false)
                .uv(0, 26).mirrored().cuboid(-12.0F, -4.0F, 13.5F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(7.5F, 0.0F, -14.5F));

        ModelPartData right_wheel_r1 = right_wheel.addChild("right_wheel_r1", ModelPartBuilder.create().uv(23, 0).mirrored().cuboid(-4.0F, 1.773F, 1.091F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(33, 33).mirrored().cuboid(-4.0F, 4.273F, -1.409F, 1.0F, 1.0F, 6.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-7.5F, -5.25F, 10.0F, 0.7854F, 0.0F, 0.0F));

        ModelPartData left_wheel = base.addChild("left_wheel", ModelPartBuilder.create().uv(36, 0).cuboid(-5.0F, -6.0F, 9.5F, 1.0F, 1.0F, 6.0F, new Dilation(0.0F))
                .uv(36, 0).cuboid(-5.0F, -6.0F, 9.5F, 1.0F, 1.0F, 6.0F, new Dilation(0.0F))
                .uv(36, 0).cuboid(-5.0F, -1.0F, 9.5F, 1.0F, 1.0F, 6.0F, new Dilation(0.0F))
                .uv(0, 14).cuboid(-5.0F, -5.0F, 14.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 14).cuboid(-5.0F, -5.0F, 9.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 14).cuboid(-5.0F, -5.0F, 12.0F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
                .uv(24, 16).cuboid(-5.0F, -3.5F, 10.5F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 26).cuboid(-4.5F, -4.0F, 11.5F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(8.0F, 0.0F, -12.5F));

        ModelPartData left_wheel_r1 = left_wheel.addChild("left_wheel_r1", ModelPartBuilder.create().uv(23, 0).cuboid(3.0F, 1.773F, 1.091F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F))
                .uv(33, 33).cuboid(3.0F, 4.273F, -1.409F, 1.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(-8.0F, -5.25F, 8.0F, 0.7854F, 0.0F, 0.0F));

        ModelPartData body = base.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -0.1667F, 3.9167F, 6.0F, 3.0F, 10.0F, new Dilation(0.0F))
                .uv(14, 32).cuboid(-1.0F, 2.8333F, 11.9167F, 2.0F, 1.0F, 7.0F, new Dilation(0.0F))
                .uv(24, 5).cuboid(-3.0F, -1.1667F, 3.9167F, 1.0F, 1.0F, 9.0F, new Dilation(0.0F))
                .uv(0, 5).cuboid(-3.0F, -2.1667F, 4.4167F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(12, 17).cuboid(2.0F, -1.1667F, 3.9167F, 1.0F, 1.0F, 9.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(2.0F, -2.1667F, 4.4167F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -6.3333F, -8.4167F, -0.1309F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }
}

