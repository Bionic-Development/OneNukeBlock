package de.takacick.onesuperblock.registry.entity.living.model;

import de.takacick.onesuperblock.registry.entity.living.SuperWitherEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class SuperWitherEntityModel<T extends SuperWitherEntity>
        extends SinglePartEntityModel<T> {
    /**
     * The key of the ribcage model part, whose value is {@value}.
     */
    private static final String RIBCAGE = "ribcage";
    /**
     * The key of the center head model part, whose value is {@value}.
     */
    private static final String CENTER_HEAD = "center_head";
    /**
     * The key of the right head model part, whose value is {@value}.
     */
    private static final String RIGHT_HEAD = "right_head";
    /**
     * The key of the left head model part, whose value is {@value}.
     */
    private static final String LEFT_HEAD = "left_head";

    private final ModelPart root;
    private final ModelPart centerHead;
    private final ModelPart rightHead;
    private final ModelPart leftHead;
    private final ModelPart ribcage;
    private final ModelPart tail;
    private final ModelPart commandBlock;

    public SuperWitherEntityModel(ModelPart root) {
        this.root = root.getChild("bone");
        this.ribcage = this.root.getChild(RIBCAGE);
        this.tail = ribcage.getChild(EntityModelPartNames.TAIL);
        this.centerHead = this.root.getChild(CENTER_HEAD);
        this.rightHead = this.root.getChild("shoulders").getChild(RIGHT_HEAD);
        this.leftHead = this.root.getChild("shoulders").getChild(LEFT_HEAD);
        this.commandBlock = this.ribcage.getChild("command_block");
    }

    public static TexturedModelData getTexturedModelData(Dilation dilation) {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData shoulders = bone.addChild("shoulders", ModelPartBuilder.create().uv(0, 16).cuboid(-10.0F, 3.9F, -0.5F, 20.0F, 3.0F, 3.0F, dilation), ModelTransform.pivot(0.0F, -24.0F, 0.0F));

        ModelPartData left_head = shoulders.addChild("left_head", ModelPartBuilder.create().uv(32, 0).cuboid(-3.0F, -4.0F, -4.0F, 6.0F, 6.0F, 6.0F, dilation), ModelTransform.pivot(9.0F, 4.0F, 0.0F));

        ModelPartData right_head = shoulders.addChild("right_head", ModelPartBuilder.create().uv(32, 0).cuboid(-3.0F, -4.0F, -4.0F, 6.0F, 6.0F, 6.0F, dilation), ModelTransform.pivot(-9.0F, 4.0F, 0.0F));

        ModelPartData ribcage = bone.addChild("ribcage", ModelPartBuilder.create().uv(24, 22).cuboid(-8.0F, 6.5F, 0.5F, 11.0F, 2.0F, 2.0F, dilation)
                .uv(24, 22).cuboid(-8.0F, 4.0F, 0.5F, 11.0F, 2.0F, 2.0F, dilation)
                .uv(24, 22).cuboid(-8.0F, 1.5F, 0.5F, 11.0F, 2.0F, 2.0F, dilation)
                .uv(0, 22).cuboid(-3.5F, 0.0F, 0.0F, 3.0F, 10.0F, 3.0F, dilation), ModelTransform.pivot(2.0F, -17.1F, -0.5F));

        ModelPartData cage = ribcage.addChild("cage", ModelPartBuilder.create().uv(0, 52).cuboid(1.501F, 6.5F, -9.0F, 1.5F, 2.0F, 9.5F, dilation)
                .uv(42, 36).cuboid(-8.001F, 6.5F, -9.0F, 1.5F, 2.0F, 9.5F, dilation)
                .uv(42, 36).cuboid(-8.001F, 4.0F, -9.0F, 1.5F, 2.0F, 9.5F, dilation)
                .uv(0, 52).cuboid(1.501F, 4.0F, -9.0F, 1.5F, 2.0F, 9.5F, dilation)
                .uv(56, 36).cuboid(-6.5F, 6.5F, -9.002F, 2.5F, 2.0F, 1.0F, dilation)
                .uv(56, 36).cuboid(-6.5F, 4.0F, -9.002F, 2.5F, 2.0F, 1.0F, dilation)
                .uv(56, 36).cuboid(-6.5F, 1.5F, -9.002F, 2.5F, 2.0F, 1.0F, dilation)
                .uv(56, 30).cuboid(-1.0F, 1.5F, -9.002F, 2.5F, 2.0F, 1.0F, dilation)
                .uv(56, 30).cuboid(-1.0F, 6.5F, -9.002F, 2.5F, 2.0F, 1.0F, dilation)
                .uv(56, 30).cuboid(-1.0F, 4.0F, -9.002F, 2.5F, 2.0F, 1.0F, dilation)
                .uv(42, 36).cuboid(-8.001F, 1.5F, -9.0F, 1.5F, 2.0F, 9.5F, dilation)
                .uv(0, 52).cuboid(1.501F, 1.5F, -9.0F, 1.5F, 2.0F, 9.5F, dilation), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData tail = ribcage.addChild("tail", ModelPartBuilder.create().uv(12, 22).cuboid(-1.5F, 0.0F, 0.0F, 3.0F, 6.0F, 3.0F, dilation), ModelTransform.pivot(-2.0F, 10.0F, 0.0F));

        ModelPartData command_block = ribcage.addChild("command_block", ModelPartBuilder.create().uv(32, 48).cuboid(-5.5F, -16.0F, -8.501F, 8.0F, 8.0F, 8.0F, dilation), ModelTransform.pivot(-1.0F, 17.1F, 0.5F));

        ModelPartData center_head = bone.addChild("center_head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, dilation), ModelTransform.pivot(0.0F, -24.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    @Override
    public void setAngles(T witherEntity, float f, float g, float h, float i, float j) {
        float k = MathHelper.cos(h * 0.1f);
        this.ribcage.pitch = (0.065f + 0.05f * k) * (float) Math.PI;
        this.tail.pitch = (0.265f + 0.1f * k) * (float) Math.PI;
        this.centerHead.yaw = i * ((float) Math.PI / 180);
        this.centerHead.pitch = j * ((float) Math.PI / 180);
    }

    @Override
    public void animateModel(T witherEntity, float f, float g, float h) {
        rotateHead(witherEntity, this.rightHead, 1, h);
        rotateHead(witherEntity, this.leftHead, 0, h);
    }

    private static <T extends SuperWitherEntity> void rotateHead(T entity, ModelPart head, int sigma, float tickDelta) {
        head.yaw = (entity.getHeadYaw(sigma, tickDelta) - MathHelper.lerp(tickDelta, entity.prevBodyYaw, entity.getBodyYaw())) * ((float) Math.PI / 180f);
        head.pitch = entity.getHeadPitch(sigma, tickDelta) * ((float) Math.PI / 180f);
    }

    public void transformCommandBlock(MatrixStack matrixStack) {
        this.root.rotate(matrixStack);
        this.ribcage.rotate(matrixStack);
    }

    public ModelPart getCommandBlock() {
        return commandBlock;
    }
}

