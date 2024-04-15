package de.takacick.onegirlfriendblock.client.model;

import de.takacick.onegirlfriendblock.OneGirlfriendBlock;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class FrenchFriesModel extends Model {

    public static final Identifier TEXTURE = new Identifier(OneGirlfriendBlock.MOD_ID, "textures/entity/french_fries.png");

    private final ModelPart root;

    public FrenchFriesModel(ModelPart part) {
        super(RenderLayer::getItemEntityTranslucentCull);

        this.root = part;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        this.root.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData box = modelPartData.addChild("box", ModelPartBuilder.create().uv(0, 0).cuboid(-4.5F, -3.0F, -1.5F, 8.0F, 5.0F, 5.0F, new Dilation(0.0F))
                .uv(0, 10).cuboid(-4.0F, 2.0F, -1.0F, 7.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.5F, 19.0F, 0.0F));

        ModelPartData french_fries = box.addChild("french_fries", ModelPartBuilder.create(), ModelTransform.pivot(-0.5F, 5.0F, 0.0F));

        ModelPartData french_fries_r1 = french_fries.addChild("french_fries_r1", ModelPartBuilder.create().uv(0, 17).cuboid(2.9F, -2.5F, -1.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-5.2F, -9.5F, 1.2F, 0.0F, 0.0F, 0.0698F));

        ModelPartData french_fries_r2 = french_fries.addChild("french_fries_r2", ModelPartBuilder.create().uv(0, 17).cuboid(1.7F, -1.4F, -1.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-5.2F, -9.5F, 1.2F, 0.0F, 0.0F, -0.1222F));

        ModelPartData french_fries_r3 = french_fries.addChild("french_fries_r3", ModelPartBuilder.create().uv(0, 17).mirrored().cuboid(-0.9F, -1.4F, -1.5F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-1.9F, -10.3F, 2.4F, 0.25F, -0.11F, -0.1161F));

        ModelPartData french_fries_r4 = french_fries.addChild("french_fries_r4", ModelPartBuilder.create().uv(0, 17).cuboid(2.9F, -2.5F, -1.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-4.2F, -10.5F, 2.6F, 0.0F, 0.0F, 0.0698F));

        ModelPartData french_fries_r5 = french_fries.addChild("french_fries_r5", ModelPartBuilder.create().uv(0, 17).cuboid(1.7F, -1.4F, -1.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-4.2F, -10.5F, 2.6F, 0.0F, 0.0F, -0.1222F));

        ModelPartData french_fries_r6 = french_fries.addChild("french_fries_r6", ModelPartBuilder.create().uv(0, 17).cuboid(0.2F, -1.4F, -1.5F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-4.2F, -10.5F, 2.6F, 0.25F, 0.11F, -0.0584F));

        ModelPartData french_fries_r7 = french_fries.addChild("french_fries_r7", ModelPartBuilder.create().uv(0, 17).mirrored().cuboid(-2.7F, -1.4F, -1.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-0.6F, -10.1F, 3.1F, 0.0F, 0.0F, -0.0524F));

        ModelPartData french_fries_r8 = french_fries.addChild("french_fries_r8", ModelPartBuilder.create().uv(0, 17).mirrored().cuboid(-3.9F, -2.5F, -1.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-0.6F, -10.1F, 3.1F, 0.0F, 0.0F, -0.2443F));

        ModelPartData french_fries_r9 = french_fries.addChild("french_fries_r9", ModelPartBuilder.create().uv(0, 17).mirrored().cuboid(-0.9F, -1.4F, -1.5F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-0.9F, -11.3F, 3.8F, -0.134F, -0.11F, -0.1161F));

        ModelPartData french_fries_r10 = french_fries.addChild("french_fries_r10", ModelPartBuilder.create().uv(0, 17).cuboid(-1.5F, -2.1F, -2.5F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 17).cuboid(-0.1F, -1.4F, -1.5F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.4F, -10.3F, 2.4F, 0.25F, 0.11F, 0.1161F));

        ModelPartData french_fries_r11 = french_fries.addChild("french_fries_r11", ModelPartBuilder.create().uv(0, 17).cuboid(2.9F, -2.5F, -1.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.1F, -9.1F, 1.7F, 0.0F, 0.0F, 0.2443F));

        ModelPartData french_fries_r12 = french_fries.addChild("french_fries_r12", ModelPartBuilder.create().uv(0, 17).cuboid(1.7F, -1.4F, -1.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.1F, -9.1F, 1.7F, 0.0F, 0.0F, 0.0524F));

        ModelPartData french_fries_r13 = french_fries.addChild("french_fries_r13", ModelPartBuilder.create().uv(0, 17).mirrored().cuboid(-0.9F, -1.4F, -1.5F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(3.7F, -9.5F, 1.2F, 0.25F, -0.11F, 0.0584F));

        ModelPartData french_fries_r14 = french_fries.addChild("french_fries_r14", ModelPartBuilder.create().uv(0, 17).mirrored().cuboid(-2.7F, -1.4F, -1.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(3.7F, -9.5F, 1.2F, 0.0F, 0.0F, 0.1222F));

        ModelPartData french_fries_r15 = french_fries.addChild("french_fries_r15", ModelPartBuilder.create().uv(0, 17).mirrored().cuboid(-3.9F, -2.5F, -1.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(3.7F, -9.5F, 1.2F, 0.0F, 0.0F, -0.0698F));

        ModelPartData french_fries_r16 = french_fries.addChild("french_fries_r16", ModelPartBuilder.create().uv(0, 17).cuboid(-0.1F, -1.4F, -1.5F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.6F, -11.3F, 3.8F, -0.134F, 0.11F, 0.1161F));

        ModelPartData french_fries_r17 = french_fries.addChild("french_fries_r17", ModelPartBuilder.create().uv(0, 17).cuboid(2.9F, -2.5F, -1.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.9F, -10.1F, 3.1F, 0.0F, 0.0F, 0.2443F));

        ModelPartData french_fries_r18 = french_fries.addChild("french_fries_r18", ModelPartBuilder.create().uv(0, 17).cuboid(1.7F, -1.4F, -1.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.9F, -10.1F, 3.1F, 0.0F, 0.0F, 0.0524F));

        ModelPartData french_fries_r19 = french_fries.addChild("french_fries_r19", ModelPartBuilder.create().uv(0, 17).mirrored().cuboid(-0.9F, -1.4F, -1.5F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(2.7F, -10.5F, 2.6F, 0.25F, -0.11F, 0.0584F));

        ModelPartData french_fries_r20 = french_fries.addChild("french_fries_r20", ModelPartBuilder.create().uv(0, 17).mirrored().cuboid(-2.7F, -1.4F, -1.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(2.7F, -10.5F, 2.6F, 0.0F, 0.0F, 0.1222F));

        ModelPartData french_fries_r21 = french_fries.addChild("french_fries_r21", ModelPartBuilder.create().uv(0, 17).mirrored().cuboid(-2.0F, -0.2F, -2.4F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-0.2F, -9.3F, 0.8F, 0.25F, -0.11F, -0.1161F));

        ModelPartData french_fries_r22 = french_fries.addChild("french_fries_r22", ModelPartBuilder.create().uv(0, 17).mirrored().cuboid(-2.7F, -1.4F, -1.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.1F, -8.1F, 0.1F, 0.0F, 0.0F, -0.0524F));

        ModelPartData french_fries_r23 = french_fries.addChild("french_fries_r23", ModelPartBuilder.create().uv(0, 17).mirrored().cuboid(-3.9F, -2.5F, -1.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.1F, -8.1F, 0.1F, 0.0F, 0.0F, -0.2443F));

        ModelPartData french_fries_r24 = french_fries.addChild("french_fries_r24", ModelPartBuilder.create().uv(0, 17).mirrored().cuboid(-2.7F, -1.4F, -1.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(3.1F, -8.1F, 0.1F, 0.0F, 0.0F, -0.0524F));

        ModelPartData french_fries_r25 = french_fries.addChild("french_fries_r25", ModelPartBuilder.create().uv(0, 17).mirrored().cuboid(-3.9F, -2.5F, -1.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(3.1F, -8.1F, 0.1F, 0.0F, 0.0F, -0.2443F));

        ModelPartData french_fries_r26 = french_fries.addChild("french_fries_r26", ModelPartBuilder.create().uv(0, 17).mirrored().cuboid(-0.9F, -1.4F, -1.5F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(2.8F, -9.3F, 0.8F, 0.25F, -0.11F, -0.1161F));

        ModelPartData french_fries_r27 = french_fries.addChild("french_fries_r27", ModelPartBuilder.create().uv(0, 17).cuboid(-0.1F, -1.4F, -1.5F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -8.5F, -0.4F, 0.25F, 0.11F, -0.0584F));

        ModelPartData french_fries_r28 = french_fries.addChild("french_fries_r28", ModelPartBuilder.create().uv(0, 17).cuboid(1.7F, -1.4F, -1.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -8.5F, -0.4F, 0.0F, 0.0F, -0.1222F));

        ModelPartData french_fries_r29 = french_fries.addChild("french_fries_r29", ModelPartBuilder.create().uv(0, 17).cuboid(2.9F, -2.5F, -1.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -8.5F, -0.4F, 0.0F, 0.0F, 0.0698F));
        return TexturedModelData.of(modelData, 32, 32);
    }
}
