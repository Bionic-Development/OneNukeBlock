package de.takacick.upgradebody.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import de.takacick.upgradebody.client.model.parts.*;
import de.takacick.upgradebody.registry.bodypart.BodyPart;
import de.takacick.upgradebody.registry.bodypart.BodyPartManager;
import de.takacick.upgradebody.registry.bodypart.BodyParts;
import net.minecraft.client.model.*;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;

import java.util.HashMap;

public class BodyEntityModel extends PlayerEntityModel<AbstractClientPlayerEntity> {

    public static BodyEntityModel factory(BodyPartManager bodyPartManager) {
        final HashMap<BodyPart, BodyPartModel> bodyParts = new HashMap<>();
        bodyParts.put(BodyParts.HEAD, new HeadModel(BodyParts.HEAD, bodyPartManager));
        bodyParts.put(BodyParts.TANK_TRACKS, new TankTracksModel(BodyParts.TANK_TRACKS, bodyPartManager));
        bodyParts.put(BodyParts.ENERGY_BELLY_CANNON, new EnergyBellyCannonModel(BodyParts.ENERGY_BELLY_CANNON, bodyPartManager));
        bodyParts.put(BodyParts.KILLER_DRILLER, new KillerDrillerModel(BodyParts.KILLER_DRILLER, bodyPartManager));
        bodyParts.put(BodyParts.CYBER_CHAINSAWS, new CyberChainsawsModel(BodyParts.CYBER_CHAINSAWS, bodyPartManager));

        if (bodyPartManager.hasBodyPart(BodyParts.KILLER_DRILLER)) {
            bodyParts.remove(BodyParts.HEAD);
        }

        BodyParts.BODY_PARTS.forEach(bodyPart -> {
            if (!bodyPartManager.hasBodyPart(bodyPart)) {
                bodyParts.remove(bodyPart);
            }
        });

        return new BodyEntityModel(getTexturedModelData().createModel(), bodyParts);
    }

    private final HashMap<BodyPart, BodyPartModel> bodyParts;
    protected final ModelPart root;

    public BodyEntityModel(ModelPart root) {
        this(root, new HashMap<>());
    }

    public BodyEntityModel(ModelPart root, HashMap<BodyPart, BodyPartModel> bodyParts) {
        super(root, true);
        this.root = root;
        this.bodyParts = bodyParts;
    }

    @Override
    public void setAngles(AbstractClientPlayerEntity livingEntity, float f, float g, float h, float i, float j) {
        this.sneaking = false;
        super.setAngles(livingEntity, f, g, h, i, j);

        this.bodyParts.forEach((bodyPart, part) -> {
            part.setAngles(this, livingEntity, f, g, h, i, j);
        });
    }

    public void render(AbstractClientPlayerEntity abstractClientPlayerEntity, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider,
                       boolean showBody, boolean translucent, boolean showOutline,
                       int light, int overlay, float red, float green, float blue, float alpha) {

        this.bodyParts.forEach((bodyPart, model) -> {
            RenderLayer renderLayer = getRenderLayer(abstractClientPlayerEntity, model, showBody, translucent, showOutline);
            model.render(matrices, vertexConsumerProvider, renderLayer, light, overlay, red, green, blue, alpha);
        });
    }

    public RenderLayer getRenderLayer(AbstractClientPlayerEntity entity, BodyPartModel bodyPartModel, boolean showBody, boolean translucent, boolean showOutline) {
        RenderLayer layer = bodyPartModel.getRenderLayer(this, entity, showBody, translucent, showOutline);
        if (layer != null) {
            return layer;
        }

        Identifier identifier = bodyPartModel.getTexture(entity);
        if (translucent) {
            return RenderLayer.getItemEntityTranslucentCull(identifier);
        }
        if (showBody) {
            return getLayer(identifier);
        }
        if (showOutline) {
            return RenderLayer.getOutline(identifier);
        }
        return null;
    }

    @Override
    public void renderCape(MatrixStack matrices, VertexConsumer vertices, int light, int overlay) {

    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {

    }

    @Override
    protected Iterable<ModelPart> getBodyParts() {
        return Iterables.concat(super.getBodyParts(), ImmutableList.of(this.leftPants, this.rightPants, this.leftSleeve, this.rightSleeve, this.jacket));
    }

    public void copyTransform(ModelPart modelPart, ModelPart owner) {
        ModelTransform transform = owner.getDefaultTransform();
        float x = transform.pivotX - owner.pivotX;
        float y = transform.pivotY - owner.pivotY;
        float z = transform.pivotZ - owner.pivotZ;

        ModelTransform modelTransform = modelPart.getTransform();
        modelPart.copyTransform(owner);
        modelPart.setPivot(modelTransform.pivotX - x, modelTransform.pivotY - y, modelTransform.pivotZ - z);
    }

    public ModelPart getArmForAngle(Arm arm) {
        if (this.bodyParts.containsKey(BodyParts.CYBER_CHAINSAWS)) {
            if (arm == Arm.LEFT) {
                return this.bodyParts.get(BodyParts.CYBER_CHAINSAWS).getRoot().getChild("left_arm");
            }
            return this.bodyParts.get(BodyParts.CYBER_CHAINSAWS).getRoot().getChild("right_arm");
        }

        return super.getArm(arm);
    }

    @Override
    public void setArmAngle(Arm arm, MatrixStack matrices) {
        if (this.bodyParts.containsKey(BodyParts.CYBER_CHAINSAWS)) {
            ModelPart modelPart = getArmForAngle(arm);
            float f = 0f * (float) (arm == Arm.RIGHT ? -1 : 1);

            modelPart.pivotX += f;
            modelPart.rotate(matrices);
            modelPart.pivotX -= f;
        } else if (this.bodyParts.containsKey(BodyParts.HEAD) || this.bodyParts.containsKey(BodyParts.KILLER_DRILLER)) {
            ModelPart modelPart = this.bodyParts.containsKey(BodyParts.KILLER_DRILLER)
                    ? this.bodyParts.get(BodyParts.KILLER_DRILLER).getRoot() : this.bodyParts.get(BodyParts.HEAD).getRoot();
            float f = 3.6f * (float) (arm == Arm.RIGHT ? -1 : 1);

            boolean only = this.bodyParts.size() == 1;

            modelPart.pivotX += f;
            modelPart.rotate(matrices);
            modelPart.pivotX -= f;

            matrices.translate(0, only ? -0.5 : -0.75, 0);
        } else {
            matrices.scale(0, 0, 0);
        }
    }

    public static TexturedModelData getTexturedModelData() {
        Dilation dilation = Dilation.NONE;
        ModelData modelData = BipedEntityModel.getModelData(dilation, 0.0f);
        ModelPartData modelPartData = modelData.getRoot();

        modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create(), ModelTransform.pivot(0.0f, 0.0f, 0.0f));
        modelPartData.addChild(EntityModelPartNames.HAT, ModelPartBuilder.create(), ModelTransform.pivot(0.0f, 0.0f, 0.0f));
        modelPartData.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create(), ModelTransform.pivot(0.0f, 0.0f, 0.0f));

        modelPartData.addChild("ear", ModelPartBuilder.create(), ModelTransform.NONE);
        modelPartData.addChild("cloak", ModelPartBuilder.create(), ModelTransform.pivot(0.0f, 0.0f, 0.0f));

        modelPartData.addChild(EntityModelPartNames.LEFT_ARM, ModelPartBuilder.create().uv(32, 48).cuboid(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, dilation), ModelTransform.pivot(5.0f, 2.0f, 0.0f));
        modelPartData.addChild("left_sleeve", ModelPartBuilder.create().uv(48, 48).cuboid(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, dilation.add(0.25f)), ModelTransform.pivot(5.0f, 2.0f, 0.0f));
        modelPartData.addChild("right_sleeve", ModelPartBuilder.create().uv(40, 32).cuboid(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, dilation.add(0.25f)), ModelTransform.pivot(-5.0f, 2.0f, 0.0f));

        modelPartData.addChild(EntityModelPartNames.LEFT_LEG, ModelPartBuilder.create(), ModelTransform.pivot(1.9f, 12.0f, 0.0f));
        modelPartData.addChild("left_pants", ModelPartBuilder.create(), ModelTransform.pivot(1.9f, 12.0f, 0.0f));
        modelPartData.addChild(EntityModelPartNames.RIGHT_LEG, ModelPartBuilder.create(), ModelTransform.pivot(-1.9f, 12.0f + 0, 0.0f));
        modelPartData.addChild("right_pants", ModelPartBuilder.create(), ModelTransform.pivot(-1.9f, 12.0f, 0.0f));
        modelPartData.addChild(EntityModelPartNames.JACKET, ModelPartBuilder.create(), ModelTransform.NONE);
        return TexturedModelData.of(modelData, 64, 64);
    }

    public void copy(ModelPart modelPart, String childName) {
        if (childName != null && getRoot().hasChild(childName)) {
            copyTransform(modelPart, getRoot().getChild(childName));
            getRoot().getChild(childName).copyTransform(modelPart);
        }
    }

    public ModelPart getRoot() {
        return root;
    }

    public HashMap<BodyPart, BodyPartModel> getUpgradedParts() {
        return this.bodyParts;
    }
}
