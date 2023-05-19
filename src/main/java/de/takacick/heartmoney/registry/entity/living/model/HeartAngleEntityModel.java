package de.takacick.heartmoney.registry.entity.living.model;

import de.takacick.heartmoney.registry.entity.living.HeartAngelEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

public class HeartAngleEntityModel extends SinglePartEntityModel<HeartAngelEntity> implements ModelWithArms {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightWing;
    private final ModelPart leftWing;
    private final ModelPart halo;

    public HeartAngleEntityModel(ModelPart root) {
        this.root = root.getChild(EntityModelPartNames.ROOT);
        this.head = this.root.getChild(EntityModelPartNames.HEAD);
        this.body = this.root.getChild(EntityModelPartNames.BODY);
        this.rightArm = this.body.getChild(EntityModelPartNames.RIGHT_ARM);
        this.leftArm = this.body.getChild(EntityModelPartNames.LEFT_ARM);
        this.rightWing = this.body.getChild(EntityModelPartNames.RIGHT_WING);
        this.leftWing = this.body.getChild(EntityModelPartNames.LEFT_WING);
        this.halo = this.head.getChild("halo");
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData root = modelPartData.addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData head = root.addChild("head", ModelPartBuilder.create().uv(25, 0).cuboid(-2.5F, -5.01F, -2.5F, 5.0F, 5.0F, 5.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-3.0F, -5.51F, -3.0F, 6.0F, 6.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -4.0F, 0.0F));

        ModelPartData halo = head.addChild("halo", ModelPartBuilder.create().uv(0, 27).cuboid(-2.5F, -10.01F, -2.5F, 5.0F, 0.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 4.0F, 0.0F));

        ModelPartData body = root.addChild("body", ModelPartBuilder.create().uv(21, 27).cuboid(-1.5F, 0.0F, -1.0F, 3.0F, 4.0F, 2.0F, new Dilation(0.0F))
                .uv(26, 34).cuboid(-1.5F, 4.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(21, 34).cuboid(0.5F, 4.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -4.0F, 0.0F));

        ModelPartData rightItem = body.addChild("rightItem", ModelPartBuilder.create(), ModelTransform.of(0.0F, 5.0F, -2.0F, -1.3963F, 0.0F, 0.0F));

        ModelPartData right_arm = body.addChild("right_arm", ModelPartBuilder.create().uv(0, 34).cuboid(-0.75F, -0.5F, -1.0F, 1.0F, 4.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.75F, 0.5F, 0.0F));

        ModelPartData left_arm = body.addChild("left_arm", ModelPartBuilder.create().uv(32, 27).cuboid(-0.25F, -0.5F, -1.0F, 1.0F, 4.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(1.75F, 0.5F, 0.0F));

        ModelPartData left_wing = body.addChild("left_wing", ModelPartBuilder.create().uv(17, 13).cuboid(0.0F, 0.0F, 0.0F, 0.0F, 5.0F, 8.0F, new Dilation(0.0F))
                .uv(26, 38).cuboid(0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(7, 34).cuboid(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.5F, 1.0F, 1.0F));

        ModelPartData right_wing = body.addChild("right_wing", ModelPartBuilder.create().uv(0, 13).cuboid(0.0F, 0.0F, 0.0F, 0.0F, 5.0F, 8.0F, new Dilation(0.0F))
                .uv(14, 34).cuboid(-1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 41).cuboid(-1.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-0.5F, 1.0F, 1.0F));
        return TexturedModelData.of(modelData, 48, 48);
    }

    @Override
    public void setAngles(HeartAngelEntity heartAngelEntity, float f, float g, float h, float i, float j) {
        float r;
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.head.pitch = j * ((float) Math.PI / 180);
        this.head.yaw = i * ((float) Math.PI / 180);
        float k = h * 20.0f * ((float) Math.PI / 180) + g;
        float l = MathHelper.cos(k) * (float) Math.PI * 0.15f;
        float m = h - (float) heartAngelEntity.age;
        float n = h * 9.0f * ((float) Math.PI / 180);
        float o = Math.min(g / 0.3f, 1.0f);
        float p = 1.0f - o;
        float q = heartAngelEntity.method_43397(m);
        this.rightWing.pitch = 0.43633232f;
        this.rightWing.yaw = -0.61086524f + l;
        this.leftWing.pitch = 0.43633232f;
        this.leftWing.yaw = 0.61086524f - l;
        this.body.pitch = r = o * 0.6981317f;
        float s = MathHelper.lerp(q, r, MathHelper.lerp(o, -1.0471976f, -0.7853982f));
        this.root.pivotY += (float) Math.cos(n) * 0.25f * p;
        this.rightArm.pitch = s;
        this.leftArm.pitch = s;
        float t = p * (1.0f - q);
        float u = 0.43633232f - MathHelper.cos(n + 4.712389f) * (float) Math.PI * 0.075f * t;
        this.leftArm.roll = -u;
        this.rightArm.roll = u;
        this.rightArm.yaw = 0.27925268f * q;
        this.leftArm.yaw = -0.27925268f * q;

        float tickDelta = MinecraftClient.getInstance().getTickDelta();
        float haloRot = (float) (Math.sin((heartAngelEntity.age + tickDelta) / 25d) + 1) / 2;

        this.halo.setAngles(0, (float) ((heartAngelEntity.age + tickDelta) / 50d) * ((float) Math.PI), 0);
        this.halo.translate(new Vec3f(0, haloRot * -0.5f, 0));
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        this.root.render(matrices, vertices, light, overlay);
    }

    @Override
    public void setArmAngle(Arm arm, MatrixStack matrices) {
        this.root.rotate(matrices);
        this.body.rotate(matrices);
        matrices.translate(0.0, -0.09375, 0.09375);
        matrices.multiply(Vec3f.POSITIVE_X.getRadialQuaternion(this.rightArm.pitch + 0.43633232f));
        matrices.scale(0.7f, 0.7f, 0.7f);
        matrices.translate(0.0625, 0.0, 0.0);
    }
}