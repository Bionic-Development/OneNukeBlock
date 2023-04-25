package de.takacick.everythinghearts.registry.entity.custom.model;

import net.minecraft.client.model.*;

public class HeartHandsModel {

    private final ModelPart root;
    private final ModelPart rightArm;
    private final ModelPart leftArm;

    public HeartHandsModel(boolean slim) {
        this.root = getTexturedModelData(slim).createModel();
        this.rightArm = root.getChild("right_arm");
        this.leftArm = root.getChild("left_arm");
    }

    public ModelPart getRoot() {
        return root;
    }

    public ModelPart getRightArm() {
        return rightArm;
    }

    public ModelPart getLeftArm() {
        return leftArm;
    }

    public static TexturedModelData getTexturedModelData(boolean slim) {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData right_arm = modelPartData.addChild("right_arm", ModelPartBuilder.create(), ModelTransform.pivot(-5.0F, 2.0F, 0.0F));

        right_arm.addChild("right_arm_heart",
                slim ? createRightHeartSlim() : createRightHeart(),
                slim ? ModelTransform.of(-1.4F, 10.2F, -0.3706F, 3.1416F, 0.0F, 0.0F) : ModelTransform.of(-1.6F, 10.2F, -0.2706F, 3.1416F, 0.0F, 0.0F));

        ModelPartData left_arm = modelPartData.addChild("left_arm", ModelPartBuilder.create(), ModelTransform.pivot(5.0F, 2.0F, 0.0F));

        left_arm.addChild("left_arm_heart",
                slim ? createHeartSlim() : createHeart(),
                slim ? ModelTransform.of(-0.1F, 10.2F, -0.2706F, 3.1416F, 0.0F, 0.0F) : ModelTransform.of(0.4F, 10.2F, -0.2706F, 3.1416F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 8, 8);
    }

    private static ModelPartBuilder createRightHeart() {
        return ModelPartBuilder.create().uv(0, 0).cuboid(-3.5F, -0.5F, 1.0294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-2.3F, -0.5F, 1.0294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-2.3F, -0.5F, 2.2294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-1.1F, -0.5F, 2.2294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-1.1F, -0.5F, 1.0294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(0.1F, -0.5F, 1.0294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-3.5F, -0.5F, -0.1706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 6).cuboid(-3.5F, -0.5F, -1.3706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 6).cuboid(-2.3F, -0.5F, -2.5706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 6).cuboid(-1.1F, -0.5F, -3.7706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 6).cuboid(0.1F, -0.5F, -4.9706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 6).cuboid(3.7F, -0.5F, -1.3706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 6).cuboid(2.5F, -0.5F, -2.5706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 6).cuboid(1.3F, -0.5F, -3.7706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-2.3F, -0.5F, -0.1706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-1.1F, -0.5F, -0.1706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(0.1F, -0.5F, -0.1706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-2.3F, -0.5F, -1.3706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-1.1F, -0.5F, -1.3706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(2.5F, -0.5F, -1.3706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(0.1F, -0.5F, -1.3706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-1.1F, -0.5F, -2.5706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(1.3F, -0.5F, -0.1706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(1.3F, -0.5F, 1.0294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(1.3F, -0.5F, -1.3706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(1.3F, -0.5F, -2.5706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(0.1F, -0.5F, -2.5706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(0.1F, -0.5F, -3.7706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(2.5F, -0.5F, -0.1706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(1.3F, -0.5F, 2.2294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(2.5F, -0.5F, 2.2294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(3.7F, -0.5F, 1.0294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(3.7F, -0.5F, -0.1706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 3).cuboid(2.5F, -0.5F, 1.0294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F));
    }

    private static ModelPartBuilder createHeart() {
        return ModelPartBuilder.create().uv(0, 0).cuboid(-3.5F, -0.5F, 1.1294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-2.3F, -0.5F, 1.1294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-2.3F, -0.5F, 2.3294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-1.1F, -0.5F, 2.3294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-1.1F, -0.5F, 1.1294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(0.1F, -0.5F, 1.1294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-3.5F, -0.5F, -0.0706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 6).cuboid(-3.5F, -0.5F, -1.2706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 6).cuboid(-2.3F, -0.5F, -2.4706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 6).cuboid(-1.1F, -0.5F, -3.6706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 6).cuboid(0.1F, -0.5F, -4.8706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 6).cuboid(3.7F, -0.5F, -1.2706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 6).cuboid(2.5F, -0.5F, -2.4706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 6).cuboid(1.3F, -0.5F, -3.6706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-2.3F, -0.5F, -0.0706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-1.1F, -0.5F, -0.0706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(0.1F, -0.5F, -0.0706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-2.3F, -0.5F, -1.2706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-1.1F, -0.5F, -1.2706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(2.5F, -0.5F, -1.2706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(0.1F, -0.5F, -1.2706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-1.1F, -0.5F, -2.4706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(1.3F, -0.5F, -0.0706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(1.3F, -0.5F, 1.1294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(1.3F, -0.5F, -1.2706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(1.3F, -0.5F, -2.4706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(0.1F, -0.5F, -2.4706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(0.1F, -0.5F, -3.6706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(2.5F, -0.5F, -0.0706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(1.3F, -0.5F, 2.3294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(2.5F, -0.5F, 2.3294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(3.7F, -0.5F, 1.1294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(3.7F, -0.5F, -0.0706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 3).cuboid(2.5F, -0.5F, 1.1294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F));
    }

    private static ModelPartBuilder createRightHeartSlim() {
        return ModelPartBuilder.create().uv(0, 0).cuboid(-3.2F, -0.5F, 1.0294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-2.0F, -0.5F, 1.0294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-2.0F, -0.5F, 2.2294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-0.8F, -0.5F, 2.2294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-0.8F, -0.5F, 1.0294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(0.4F, -0.5F, 1.0294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-3.2F, -0.5F, -0.1706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 6).cuboid(-3.2F, -0.5F, -1.3706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 6).cuboid(-2.0F, -0.5F, -2.5706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 6).cuboid(-0.8F, -0.5F, -3.7706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 6).cuboid(0.4F, -0.5F, -4.9706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 6).cuboid(4.0F, -0.5F, -1.3706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 6).cuboid(2.8F, -0.5F, -2.5706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 6).cuboid(1.6F, -0.5F, -3.7706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-2.0F, -0.5F, -0.1706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-0.8F, -0.5F, -0.1706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(0.4F, -0.5F, -0.1706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-2.0F, -0.5F, -1.3706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-0.8F, -0.5F, -1.3706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(2.8F, -0.5F, -1.3706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(0.4F, -0.5F, -1.3706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-0.8F, -0.5F, -2.5706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(1.6F, -0.5F, -0.1706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(1.6F, -0.5F, 1.0294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(1.6F, -0.5F, -1.3706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(1.6F, -0.5F, -2.5706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(0.4F, -0.5F, -2.5706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(0.4F, -0.5F, -3.7706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(2.8F, -0.5F, -0.1706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(1.6F, -0.5F, 2.2294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(2.8F, -0.5F, 2.2294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(4.0F, -0.5F, 1.0294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(4.0F, -0.5F, -0.1706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 3).cuboid(2.8F, -0.5F, 1.0294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F));
    }

    private static ModelPartBuilder createHeartSlim() {
        return ModelPartBuilder.create().uv(0, 0).cuboid(-3.5F, -0.5F, 1.1294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-2.3F, -0.5F, 1.1294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-2.3F, -0.5F, 2.3294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-1.1F, -0.5F, 2.3294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-1.1F, -0.5F, 1.1294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(0.1F, -0.5F, 1.1294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-3.5F, -0.5F, -0.0706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 6).cuboid(-3.5F, -0.5F, -1.2706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 6).cuboid(-2.3F, -0.5F, -2.4706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 6).cuboid(-1.1F, -0.5F, -3.6706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 6).cuboid(0.1F, -0.5F, -4.8706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 6).cuboid(3.7F, -0.5F, -1.2706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 6).cuboid(2.5F, -0.5F, -2.4706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 6).cuboid(1.3F, -0.5F, -3.6706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-2.3F, -0.5F, -0.0706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-1.1F, -0.5F, -0.0706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(0.1F, -0.5F, -0.0706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-2.3F, -0.5F, -1.2706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-1.1F, -0.5F, -1.2706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(2.5F, -0.5F, -1.2706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(0.1F, -0.5F, -1.2706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-1.1F, -0.5F, -2.4706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(1.3F, -0.5F, -0.0706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(1.3F, -0.5F, 1.1294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(1.3F, -0.5F, -1.2706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(1.3F, -0.5F, -2.4706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(0.1F, -0.5F, -2.4706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(0.1F, -0.5F, -3.6706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(2.5F, -0.5F, -0.0706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(1.3F, -0.5F, 2.3294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(2.5F, -0.5F, 2.3294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(3.7F, -0.5F, 1.1294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(3.7F, -0.5F, -0.0706F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 3).cuboid(2.5F, -0.5F, 1.1294F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F));
    }
}