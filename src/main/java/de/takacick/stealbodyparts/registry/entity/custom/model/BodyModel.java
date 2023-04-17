package de.takacick.stealbodyparts.registry.entity.custom.model;

import de.takacick.stealbodyparts.access.ModelPartProperties;
import net.minecraft.client.model.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Direction;

public class BodyModel<T extends LivingEntity>
        extends AnimationBodyModel<T> {

    private final ModelPart root;
    public final ModelPart body;
    public final ModelPart inside;

    public BodyModel() {
        this.root = getTexturedModelData().createModel();
        this.body = root.getChild("body");
        this.inside = getInsideTexturedModelData().createModel().getChild("body");

        ((ModelPartProperties) (Object) this.body).setBodyModel(Direction.NORTH);
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(16, 32).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData west = body.addChild("west", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, -24.0F, -2.0F, 0.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData east = body.addChild("east", ModelPartBuilder.create().uv(28, 16).mirrored().cuboid(4.0F, -24.0F, -2.0F, 0.0F, 12.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData south = body.addChild("south", ModelPartBuilder.create().uv(24, 26)
                .cuboid(-4.0F, -18.0F, 10.0F, 8.0F, 6.0F, 0.0F, new Dilation(0.0F))
                .uv(32, 23).cuboid(-4.0F, -21.0F, 10.0F, 4.0F, 3.0F, 0.0F, new Dilation(0.0F))
                .uv(24, 20).cuboid(-4.0F, -24.0F, 10.0F, 8.0F, 3.0F, 0.0F, new Dilation(0.0F))
                .uv(31, 23).cuboid(3.0F, -21.0F, 10.0F, 1.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, -8.0F));

        ModelPartData north = body.addChild("north", ModelPartBuilder.create().uv(20, 26).cuboid(-4.0F, -18.0F, 6.0F, 8.0F, 6.0F, 0.0F, new Dilation(0.0F))
                .uv(20, 23).cuboid(-4.0F, -21.0F, 6.0F, 4.0F, 3.0F, 0.0F, new Dilation(0.0F))
                .uv(20, 20).cuboid(-4.0F, -24.0F, 6.0F, 8.0F, 3.0F, 0.0F, new Dilation(0.0F))
                .uv(27, 23).cuboid(3.0F, -21.0F, 6.0F, 1.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, -8.0F));

        ModelPartData up = body.addChild("up", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 12.0F, 0.0F));

        ModelPartData down = body.addChild("down", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, -12.0F, -2.0F, 8.0F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 12.0F, 0.0F));

        return TexturedModelData.of(modelData, 64, 64);
    }

    public static TexturedModelData getInsideTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        body.addChild("inside", ModelPartBuilder.create().uv(-4, 0).cuboid(0.0F, -18.0F, -2.0F, 3.0F, 0.0F, 4.0F, new Dilation(0.0F))
                .uv(0, -4).cuboid(0.0F, -21.0F, -2.0F, 0.0F, 3.0F, 4.0F, new Dilation(0.0F))
                .uv(-4, 0).cuboid(0.0F, -21.0F, -2.0F, 3.0F, 0.0F, 4.0F, new Dilation(0.0F))
                .uv(0, -4).cuboid(3.0F, -21.0F, -2.0F, 0.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(T livingEntity, float f, float g, float h, float i, float j) {

    }

    public ModelPart getBody() {
        return body;
    }

    public ModelPart getInside() {
        return inside;
    }
}