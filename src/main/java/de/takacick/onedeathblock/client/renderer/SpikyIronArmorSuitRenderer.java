package de.takacick.onedeathblock.client.renderer;

import de.takacick.onedeathblock.OneDeathBlock;
import de.takacick.onedeathblock.registry.ItemRegistry;
import net.minecraft.client.model.*;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;

public class SpikyIronArmorSuitRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public static final Identifier TEXTURE = new Identifier(OneDeathBlock.MOD_ID, "textures/entity/spiky_iron_armor_suit.png");
    public static final ModelPart MODEL_PART = getTexturedModelData().createModel();
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public SpikyIronArmorSuitRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext) {
        super(featureRendererContext);
        this.root = getTexturedModelData().createModel();
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.rightArm = root.getChild("right_arm");
        this.leftArm = root.getChild("left_arm");
        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (!entity.getEquippedStack(EquipmentSlot.CHEST).isOf(ItemRegistry.SPIKY_IRON_ARMOR_SUIT)) {
            return;
        }

        this.head.copyTransform(getContextModel().head);
        this.body.copyTransform(getContextModel().body);
        this.rightArm.copyTransform(getContextModel().rightArm);
        this.leftArm.copyTransform(getContextModel().leftArm);
        this.rightLeg.copyTransform(getContextModel().rightLeg);
        this.leftLeg.copyTransform(getContextModel().leftLeg);

        this.root.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(TEXTURE)), light, OverlayTexture.DEFAULT_UV);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(1.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData head_spikes = head.addChild("head_spikes", ModelPartBuilder.create(), ModelTransform.of(-12.0F, 2.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        ModelPartData head_spikes2 = head_spikes.addChild("head_spikes2", ModelPartBuilder.create(), ModelTransform.of(-3.7F, -26.0263F, -1.0131F, 3.1416F, 0.0F, 0.0F));

        ModelPartData head_spikes_r1 = head_spikes2.addChild("head_spikes_r1", ModelPartBuilder.create().uv(57, 11).cuboid(-2.9142F, -14.25F, -5.6569F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -0.75F, 3.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r2 = head_spikes2.addChild("head_spikes_r2", ModelPartBuilder.create().uv(57, 11).cuboid(-7.1569F, -14.25F, 1.4142F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -0.75F, 3.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes3 = head_spikes.addChild("head_spikes3", ModelPartBuilder.create(), ModelTransform.of(-4.7F, -26.0263F, -1.0131F, 3.1416F, 0.0F, 0.0F));

        ModelPartData head_spikes_r3 = head_spikes3.addChild("head_spikes_r3", ModelPartBuilder.create().uv(49, 10).cuboid(-2.9142F, -14.25F, -5.6569F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-3.0F, -0.75F, 1.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r4 = head_spikes3.addChild("head_spikes_r4", ModelPartBuilder.create().uv(49, 10).cuboid(-7.1569F, -14.25F, 1.4142F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-3.0F, -0.75F, 1.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes4 = head_spikes.addChild("head_spikes4", ModelPartBuilder.create(), ModelTransform.of(-4.7F, -26.0263F, -1.0131F, 3.1416F, 0.0F, 0.0F));

        ModelPartData head_spikes_r5 = head_spikes4.addChild("head_spikes_r5", ModelPartBuilder.create().uv(49, 10).cuboid(-2.9142F, -14.25F, -5.6569F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -0.75F, -1.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r6 = head_spikes4.addChild("head_spikes_r6", ModelPartBuilder.create().uv(49, 10).cuboid(-7.1569F, -14.25F, 1.4142F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -0.75F, -1.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes5 = head_spikes.addChild("head_spikes5", ModelPartBuilder.create(), ModelTransform.of(-4.7F, -26.0263F, -1.0131F, 3.1416F, 0.0F, 0.0F));

        ModelPartData head_spikes_r7 = head_spikes5.addChild("head_spikes_r7", ModelPartBuilder.create().uv(49, 10).cuboid(-2.9142F, -14.25F, -5.6569F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-5.0F, -0.75F, 5.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r8 = head_spikes5.addChild("head_spikes_r8", ModelPartBuilder.create().uv(49, 10).cuboid(-7.1569F, -14.25F, 1.4142F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-5.0F, -0.75F, 5.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes6 = head_spikes.addChild("head_spikes6", ModelPartBuilder.create(), ModelTransform.of(-4.7F, -26.0263F, -1.0131F, 3.1416F, 0.0F, 0.0F));

        ModelPartData head_spikes_r9 = head_spikes6.addChild("head_spikes_r9", ModelPartBuilder.create().uv(57, 11).cuboid(-2.9142F, -14.25F, -5.6569F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-6.0F, -0.75F, -1.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r10 = head_spikes6.addChild("head_spikes_r10", ModelPartBuilder.create().uv(57, 11).cuboid(-7.1569F, -14.25F, 1.4142F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-6.0F, -0.75F, -1.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes7 = head_spikes.addChild("head_spikes7", ModelPartBuilder.create(), ModelTransform.of(-4.7F, -26.0263F, -0.8131F, 3.1416F, 0.0F, 0.0F));

        ModelPartData head_spikes_r11 = head_spikes7.addChild("head_spikes_r11", ModelPartBuilder.create().uv(49, 10).cuboid(-2.9142F, -14.25F, -5.6569F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, -0.75F, 6.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r12 = head_spikes7.addChild("head_spikes_r12", ModelPartBuilder.create().uv(49, 10).cuboid(-7.1569F, -14.25F, 1.4142F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, -0.75F, 6.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes8 = head_spikes.addChild("head_spikes8", ModelPartBuilder.create(), ModelTransform.of(-8.7F, -9.0263F, -6.8131F, -1.5708F, 0.0F, 0.0F));

        ModelPartData head_spikes_r13 = head_spikes8.addChild("head_spikes_r13", ModelPartBuilder.create().uv(49, 10).cuboid(-2.9142F, -14.05F, -5.6569F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(5.0F, 12.25F, 3.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r14 = head_spikes8.addChild("head_spikes_r14", ModelPartBuilder.create().uv(49, 10).cuboid(-7.1569F, -14.05F, 1.4142F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(5.0F, 12.25F, 3.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes9 = head_spikes.addChild("head_spikes9", ModelPartBuilder.create(), ModelTransform.of(-8.7F, -9.0263F, -6.8131F, -1.5708F, 0.0F, 0.0F));

        ModelPartData head_spikes_r15 = head_spikes9.addChild("head_spikes_r15", ModelPartBuilder.create().uv(49, 10).cuboid(-2.9142F, -14.05F, -5.6569F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, 12.25F, 2.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r16 = head_spikes9.addChild("head_spikes_r16", ModelPartBuilder.create().uv(49, 10).cuboid(-7.1569F, -14.05F, 1.4142F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, 12.25F, 2.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes10 = head_spikes.addChild("head_spikes10", ModelPartBuilder.create(), ModelTransform.of(-8.7F, -9.0263F, -6.8131F, -1.5708F, 0.0F, 0.0F));

        ModelPartData head_spikes_r17 = head_spikes10.addChild("head_spikes_r17", ModelPartBuilder.create().uv(49, 10).cuboid(-2.9142F, -14.05F, -5.6569F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, 12.25F, 5.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r18 = head_spikes10.addChild("head_spikes_r18", ModelPartBuilder.create().uv(49, 10).cuboid(-7.1569F, -14.05F, 1.4142F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, 12.25F, 5.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes11 = head_spikes.addChild("head_spikes11", ModelPartBuilder.create(), ModelTransform.of(-8.7F, -9.0263F, -6.8131F, -1.5708F, 0.0F, 0.0F));

        ModelPartData head_spikes_r19 = head_spikes11.addChild("head_spikes_r19", ModelPartBuilder.create().uv(49, 10).cuboid(-2.9142F, -14.05F, -5.6569F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(5.0F, 12.25F, 7.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r20 = head_spikes11.addChild("head_spikes_r20", ModelPartBuilder.create().uv(49, 10).cuboid(-7.1569F, -14.05F, 1.4142F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(5.0F, 12.25F, 7.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes12 = head_spikes.addChild("head_spikes12", ModelPartBuilder.create(), ModelTransform.of(-8.8F, -7.8263F, -6.8131F, -1.5708F, 0.0F, 0.0F));

        ModelPartData head_spikes_r21 = head_spikes12.addChild("head_spikes_r21", ModelPartBuilder.create().uv(49, 10).cuboid(-2.9142F, -14.05F, -5.6569F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, 12.25F, 3.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r22 = head_spikes12.addChild("head_spikes_r22", ModelPartBuilder.create().uv(49, 10).cuboid(-7.1569F, -14.05F, 1.4142F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, 12.25F, 3.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes13 = head_spikes.addChild("head_spikes13", ModelPartBuilder.create(), ModelTransform.of(-8.2F, -7.9263F, -6.8131F, -1.5708F, 0.0F, 0.0F));

        ModelPartData head_spikes_r23 = head_spikes13.addChild("head_spikes_r23", ModelPartBuilder.create().uv(49, 10).cuboid(-2.9142F, -14.05F, -5.6569F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, 12.25F, 7.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r24 = head_spikes13.addChild("head_spikes_r24", ModelPartBuilder.create().uv(49, 10).cuboid(-7.1569F, -14.05F, 1.4142F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, 12.25F, 7.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes14 = head_spikes.addChild("head_spikes14", ModelPartBuilder.create(), ModelTransform.of(-8.7F, -9.0263F, -6.8131F, -1.5708F, 0.0F, 0.0F));

        ModelPartData head_spikes_r25 = head_spikes14.addChild("head_spikes_r25", ModelPartBuilder.create().uv(49, 10).cuboid(-2.9142F, -14.05F, -5.6569F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, 12.25F, 7.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r26 = head_spikes14.addChild("head_spikes_r26", ModelPartBuilder.create().uv(49, 10).cuboid(-7.1569F, -14.05F, 1.4142F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, 12.25F, 7.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes15 = head_spikes.addChild("head_spikes15", ModelPartBuilder.create(), ModelTransform.of(-5.475F, -12.0263F, 3.8869F, 1.5708F, 1.5708F, 0.0F));

        ModelPartData head_spikes_r27 = head_spikes15.addChild("head_spikes_r27", ModelPartBuilder.create().uv(57, 11).cuboid(-2.9142F, -13.25F, -5.6569F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(5.0F, 11.75F, 0.05F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r28 = head_spikes15.addChild("head_spikes_r28", ModelPartBuilder.create().uv(57, 11).cuboid(-7.1569F, -13.25F, 1.4142F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(5.0F, 11.75F, 0.05F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes16 = head_spikes.addChild("head_spikes16", ModelPartBuilder.create(), ModelTransform.of(-5.475F, -12.0263F, 3.8869F, 1.5708F, 1.5708F, 0.0F));

        ModelPartData head_spikes_r29 = head_spikes16.addChild("head_spikes_r29", ModelPartBuilder.create().uv(49, 10).cuboid(-2.9142F, -14.25F, -5.6569F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(12.0F, 12.75F, -2.95F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r30 = head_spikes16.addChild("head_spikes_r30", ModelPartBuilder.create().uv(49, 10).cuboid(-7.1569F, -14.25F, 1.4142F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(12.0F, 12.75F, -2.95F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes17 = head_spikes.addChild("head_spikes17", ModelPartBuilder.create(), ModelTransform.of(-5.475F, -12.0263F, 3.8869F, 1.5708F, 1.5708F, 0.0F));

        ModelPartData head_spikes_r31 = head_spikes17.addChild("head_spikes_r31", ModelPartBuilder.create().uv(49, 10).cuboid(-2.9142F, -14.25F, -5.6569F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(8.0F, 12.75F, -0.95F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r32 = head_spikes17.addChild("head_spikes_r32", ModelPartBuilder.create().uv(49, 10).cuboid(-7.1569F, -14.25F, 1.4142F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(8.0F, 12.75F, -0.95F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes18 = head_spikes.addChild("head_spikes18", ModelPartBuilder.create(), ModelTransform.of(-5.475F, -12.0263F, 3.8869F, 1.5708F, 1.5708F, 0.0F));

        ModelPartData head_spikes_r33 = head_spikes18.addChild("head_spikes_r33", ModelPartBuilder.create().uv(49, 10).cuboid(-2.9142F, -14.25F, -5.6569F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(10.9F, 12.75F, 0.05F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r34 = head_spikes18.addChild("head_spikes_r34", ModelPartBuilder.create().uv(49, 10).cuboid(-7.1569F, -14.25F, 1.4142F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(10.9F, 12.75F, 0.05F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes19 = head_spikes.addChild("head_spikes19", ModelPartBuilder.create(), ModelTransform.of(-19.025F, -9.0763F, -1.9881F, 1.5708F, -1.5708F, 0.0F));

        ModelPartData head_spikes_r35 = head_spikes19.addChild("head_spikes_r35", ModelPartBuilder.create().uv(49, 10).cuboid(-2.9142F, -14.25F, -5.6569F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(5.0F, 12.25F, 3.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r36 = head_spikes19.addChild("head_spikes_r36", ModelPartBuilder.create().uv(49, 10).cuboid(-7.1569F, -14.25F, 1.4142F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(5.0F, 12.25F, 3.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes20 = head_spikes.addChild("head_spikes20", ModelPartBuilder.create(), ModelTransform.of(-19.025F, -9.0763F, -1.9881F, 1.5708F, -1.5708F, 0.0F));

        ModelPartData head_spikes_r37 = head_spikes20.addChild("head_spikes_r37", ModelPartBuilder.create().uv(49, 10).cuboid(-2.9142F, -14.25F, -5.6569F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(9.0F, 12.25F, 2.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r38 = head_spikes20.addChild("head_spikes_r38", ModelPartBuilder.create().uv(49, 10).cuboid(-7.1569F, -14.25F, 1.4142F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(9.0F, 12.25F, 2.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes21 = head_spikes.addChild("head_spikes21", ModelPartBuilder.create(), ModelTransform.of(-19.025F, -9.0763F, -1.9881F, 1.5708F, -1.5708F, 0.0F));

        ModelPartData head_spikes_r39 = head_spikes21.addChild("head_spikes_r39", ModelPartBuilder.create().uv(49, 10).cuboid(-2.9142F, -14.25F, -5.6569F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(4.0F, 12.25F, 0.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r40 = head_spikes21.addChild("head_spikes_r40", ModelPartBuilder.create().uv(49, 10).cuboid(-7.1569F, -14.25F, 1.4142F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(4.0F, 12.25F, 0.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes22 = head_spikes.addChild("head_spikes22", ModelPartBuilder.create(), ModelTransform.of(-8.7F, -9.0263F, -6.8131F, -1.5708F, 0.0F, 0.0F));

        ModelPartData head_spikes_r41 = head_spikes22.addChild("head_spikes_r41", ModelPartBuilder.create().uv(49, 1).cuboid(-2.9142F, -14.05F, -5.6569F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(5.0F, -1.75F, 3.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r42 = head_spikes22.addChild("head_spikes_r42", ModelPartBuilder.create().uv(49, 1).cuboid(-7.1569F, -14.05F, 1.4142F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(5.0F, -1.75F, 3.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes23 = head_spikes.addChild("head_spikes23", ModelPartBuilder.create(), ModelTransform.of(-8.7F, -9.0263F, -6.8131F, -1.5708F, 0.0F, 0.0F));

        ModelPartData head_spikes_r43 = head_spikes23.addChild("head_spikes_r43", ModelPartBuilder.create().uv(49, 1).cuboid(-2.9142F, -14.05F, -5.6569F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, -1.75F, 3.7F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r44 = head_spikes23.addChild("head_spikes_r44", ModelPartBuilder.create().uv(49, 1).cuboid(-7.1569F, -14.05F, 1.4142F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, -1.75F, 3.7F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes24 = head_spikes.addChild("head_spikes24", ModelPartBuilder.create(), ModelTransform.of(-8.7F, -9.0263F, -6.8131F, -1.5708F, 0.0F, 0.0F));

        ModelPartData head_spikes_r45 = head_spikes24.addChild("head_spikes_r45", ModelPartBuilder.create().uv(49, 1).cuboid(-2.9142F, -14.05F, -5.6569F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(1.6F, -1.75F, 4.5F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r46 = head_spikes24.addChild("head_spikes_r46", ModelPartBuilder.create().uv(49, 1).cuboid(-7.1569F, -14.05F, 1.4142F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(1.6F, -1.75F, 4.5F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes25 = head_spikes.addChild("head_spikes25", ModelPartBuilder.create(), ModelTransform.of(-15.7F, -5.0263F, -3.5131F, -1.5708F, 3.1416F, 0.0F));

        ModelPartData head_spikes_r47 = head_spikes25.addChild("head_spikes_r47", ModelPartBuilder.create().uv(57, 11).cuboid(-2.9142F, -13.05F, -5.6569F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(5.0F, 11.55F, 3.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r48 = head_spikes25.addChild("head_spikes_r48", ModelPartBuilder.create().uv(57, 11).cuboid(-7.1569F, -13.05F, 1.4142F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(5.0F, 11.55F, 3.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes26 = head_spikes.addChild("head_spikes26", ModelPartBuilder.create(), ModelTransform.of(-15.7F, -5.0263F, -3.5131F, -1.5708F, 3.1416F, 0.0F));

        ModelPartData head_spikes_r49 = head_spikes26.addChild("head_spikes_r49", ModelPartBuilder.create().uv(57, 11).cuboid(-2.9142F, -13.05F, -5.6569F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 11.55F, -1.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r50 = head_spikes26.addChild("head_spikes_r50", ModelPartBuilder.create().uv(57, 11).cuboid(-7.1569F, -13.05F, 1.4142F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 11.55F, -1.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes27 = head_spikes.addChild("head_spikes27", ModelPartBuilder.create(), ModelTransform.of(-15.7F, -5.0263F, -3.5131F, -1.5708F, 3.1416F, 0.0F));

        ModelPartData head_spikes_r51 = head_spikes27.addChild("head_spikes_r51", ModelPartBuilder.create().uv(57, 11).cuboid(-2.9142F, -13.05F, -5.6569F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, 11.55F, 4.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r52 = head_spikes27.addChild("head_spikes_r52", ModelPartBuilder.create().uv(57, 11).cuboid(-7.1569F, -13.05F, 1.4142F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, 11.55F, 4.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes28 = head_spikes.addChild("head_spikes28", ModelPartBuilder.create(), ModelTransform.of(-15.7F, -5.0263F, -3.5131F, -1.5708F, 3.1416F, 0.0F));

        ModelPartData head_spikes_r53 = head_spikes28.addChild("head_spikes_r53", ModelPartBuilder.create().uv(57, 11).cuboid(-2.9142F, -13.05F, -5.6569F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, 11.55F, 2.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r54 = head_spikes28.addChild("head_spikes_r54", ModelPartBuilder.create().uv(57, 11).cuboid(-7.1569F, -13.05F, 1.4142F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, 11.55F, 2.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes29 = head_spikes.addChild("head_spikes29", ModelPartBuilder.create(), ModelTransform.of(-15.7F, -5.0263F, -3.5131F, -1.5708F, 3.1416F, 0.0F));

        ModelPartData head_spikes_r55 = head_spikes29.addChild("head_spikes_r55", ModelPartBuilder.create().uv(57, 11).cuboid(-2.9142F, -13.05F, -5.6569F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(2.6F, 11.55F, 1.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r56 = head_spikes29.addChild("head_spikes_r56", ModelPartBuilder.create().uv(57, 11).cuboid(-7.1569F, -13.05F, 1.4142F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(2.6F, 11.55F, 1.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes30 = head_spikes.addChild("head_spikes30", ModelPartBuilder.create(), ModelTransform.of(-15.525F, -8.0763F, 2.5119F, 1.5708F, 1.5708F, 0.0F));

        ModelPartData head_spikes_r57 = head_spikes30.addChild("head_spikes_r57", ModelPartBuilder.create().uv(57, 11).cuboid(-2.9142F, -13.25F, -5.6569F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(5.0F, 11.75F, 3.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r58 = head_spikes30.addChild("head_spikes_r58", ModelPartBuilder.create().uv(57, 11).cuboid(-7.1569F, -13.25F, 1.4142F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(5.0F, 11.75F, 3.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes31 = head_spikes.addChild("head_spikes31", ModelPartBuilder.create(), ModelTransform.of(-15.525F, -8.0763F, 2.5119F, 1.5708F, 1.5708F, 0.0F));

        ModelPartData head_spikes_r59 = head_spikes31.addChild("head_spikes_r59", ModelPartBuilder.create().uv(57, 11).cuboid(-2.9142F, -13.25F, -5.6569F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(9.0F, 11.75F, 3.4F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r60 = head_spikes31.addChild("head_spikes_r60", ModelPartBuilder.create().uv(57, 11).cuboid(-7.1569F, -13.25F, 1.4142F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(9.0F, 11.75F, 3.4F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes32 = head_spikes.addChild("head_spikes32", ModelPartBuilder.create(), ModelTransform.of(-8.525F, -8.4763F, 0.5119F, 1.5708F, -1.5708F, 0.0F));

        ModelPartData head_spikes_r61 = head_spikes32.addChild("head_spikes_r61", ModelPartBuilder.create().uv(57, 11).cuboid(-2.9142F, -13.25F, -5.6569F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(5.0F, 11.75F, 3.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r62 = head_spikes32.addChild("head_spikes_r62", ModelPartBuilder.create().uv(57, 11).cuboid(-7.1569F, -13.25F, 1.4142F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(5.0F, 11.75F, 3.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes33 = head_spikes.addChild("head_spikes33", ModelPartBuilder.create(), ModelTransform.pivot(-11.9F, -9.4763F, 2.9869F));

        ModelPartData head_spikes_r63 = head_spikes33.addChild("head_spikes_r63", ModelPartBuilder.create().uv(57, 11).cuboid(-2.9142F, -14.25F, -5.6569F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(5.0F, 12.75F, 3.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r64 = head_spikes33.addChild("head_spikes_r64", ModelPartBuilder.create().uv(57, 11).cuboid(-7.1569F, -14.25F, 1.4142F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(5.0F, 12.75F, 3.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData head_spikes34 = head_spikes.addChild("head_spikes34", ModelPartBuilder.create(), ModelTransform.pivot(-11.9F, -9.4763F, 2.9869F));

        ModelPartData head_spikes_r65 = head_spikes34.addChild("head_spikes_r65", ModelPartBuilder.create().uv(57, 11).cuboid(-2.9142F, -14.25F, -5.6569F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, 12.75F, 0.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData head_spikes_r66 = head_spikes34.addChild("head_spikes_r66", ModelPartBuilder.create().uv(57, 11).cuboid(-7.1569F, -14.25F, 1.4142F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, 12.75F, 0.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(1.01F))
                .uv(16, 48).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.51F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData body_spikes = body.addChild("body_spikes", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData body_spikes2 = body_spikes.addChild("body_spikes2", ModelPartBuilder.create(), ModelTransform.of(-2.5F, 3.3737F, 0.8369F, -1.5708F, -1.5708F, 0.0F));

        ModelPartData body_spikes_r1 = body_spikes2.addChild("body_spikes_r1", ModelPartBuilder.create().uv(49, 10).cuboid(0.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -1.3F, 3.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData body_spikes_r2 = body_spikes2.addChild("body_spikes_r2", ModelPartBuilder.create().uv(49, 10).cuboid(-3.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -1.3F, 3.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData body_spikes3 = body_spikes.addChild("body_spikes3", ModelPartBuilder.create(), ModelTransform.of(-1.5F, 3.3737F, 0.8369F, -1.5708F, -1.5708F, 0.0F));

        ModelPartData body_spikes_r3 = body_spikes3.addChild("body_spikes_r3", ModelPartBuilder.create().uv(49, 10).cuboid(0.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, -2.3F, 0.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData body_spikes_r4 = body_spikes3.addChild("body_spikes_r4", ModelPartBuilder.create().uv(49, 10).cuboid(-3.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, -2.3F, 0.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData body_spikes4 = body_spikes.addChild("body_spikes4", ModelPartBuilder.create(), ModelTransform.of(-1.5F, 3.3737F, 0.8369F, -1.5708F, -1.5708F, 0.0F));

        ModelPartData body_spikes_r5 = body_spikes4.addChild("body_spikes_r5", ModelPartBuilder.create().uv(49, 10).cuboid(0.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, -2.3F, 5.7F, 0.0F, 0.7854F, 0.0F));

        ModelPartData body_spikes_r6 = body_spikes4.addChild("body_spikes_r6", ModelPartBuilder.create().uv(49, 10).cuboid(-3.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, -2.3F, 5.7F, 0.0F, -0.7854F, 0.0F));

        ModelPartData body_spikes5 = body_spikes.addChild("body_spikes5", ModelPartBuilder.create(), ModelTransform.of(-1.5F, 3.3737F, 0.8369F, -1.5708F, -1.5708F, 0.0F));

        ModelPartData body_spikes_r7 = body_spikes5.addChild("body_spikes_r7", ModelPartBuilder.create().uv(49, 10).cuboid(0.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -1.8F, 9.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData body_spikes_r8 = body_spikes5.addChild("body_spikes_r8", ModelPartBuilder.create().uv(49, 10).cuboid(-3.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -1.8F, 9.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData body_spikes6 = body_spikes.addChild("body_spikes6", ModelPartBuilder.create(), ModelTransform.of(3.0F, 2.3737F, 0.8369F, -1.5708F, 1.5708F, 0.0F));

        ModelPartData body_spikes_r9 = body_spikes6.addChild("body_spikes_r9", ModelPartBuilder.create().uv(49, 10).cuboid(0.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -0.8F, 3.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData body_spikes_r10 = body_spikes6.addChild("body_spikes_r10", ModelPartBuilder.create().uv(49, 10).cuboid(-3.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -0.8F, 3.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData body_spikes7 = body_spikes.addChild("body_spikes7", ModelPartBuilder.create(), ModelTransform.of(3.0F, 3.3737F, 0.8369F, -1.5708F, 1.5708F, 0.0F));

        ModelPartData body_spikes_r11 = body_spikes7.addChild("body_spikes_r11", ModelPartBuilder.create().uv(49, 10).cuboid(0.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(2.0F, -0.3F, 9.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData body_spikes_r12 = body_spikes7.addChild("body_spikes_r12", ModelPartBuilder.create().uv(49, 10).cuboid(-3.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(2.0F, -0.3F, 9.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData body_spikes8 = body_spikes.addChild("body_spikes8", ModelPartBuilder.create(), ModelTransform.of(0.0F, 7.3737F, 1.8369F, -1.5708F, 0.0F, 0.0F));

        ModelPartData body_spikes_r13 = body_spikes8.addChild("body_spikes_r13", ModelPartBuilder.create().uv(57, 11).cuboid(0.6213F, -0.2F, -2.1213F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -0.4F, 3.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData body_spikes_r14 = body_spikes8.addChild("body_spikes_r14", ModelPartBuilder.create().uv(57, 11).cuboid(-3.6213F, -0.2F, -2.1213F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -0.4F, 3.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData body_spikes9 = body_spikes.addChild("body_spikes9", ModelPartBuilder.create(), ModelTransform.of(0.0F, 7.3737F, 1.4869F, -1.5708F, 0.0F, 0.0F));

        ModelPartData body_spikes_r15 = body_spikes9.addChild("body_spikes_r15", ModelPartBuilder.create().uv(57, 11).cuboid(0.6213F, -0.2F, -2.1213F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, -0.8F, 5.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData body_spikes_r16 = body_spikes9.addChild("body_spikes_r16", ModelPartBuilder.create().uv(57, 11).cuboid(-3.6213F, -0.2F, -2.1213F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, -0.8F, 5.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData body_spikes10 = body_spikes.addChild("body_spikes10", ModelPartBuilder.create(), ModelTransform.of(0.0F, 7.3737F, 1.4619F, -1.5708F, 0.0F, 0.0F));

        ModelPartData body_spikes_r17 = body_spikes10.addChild("body_spikes_r17", ModelPartBuilder.create().uv(57, 11).cuboid(0.6213F, -0.2F, -2.1213F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, -0.8F, -1.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData body_spikes_r18 = body_spikes10.addChild("body_spikes_r18", ModelPartBuilder.create().uv(57, 11).cuboid(-3.6213F, -0.2F, -2.1213F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, -0.8F, -1.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData body_spikes11 = body_spikes.addChild("body_spikes11", ModelPartBuilder.create(), ModelTransform.of(1.0F, 3.3737F, -0.9631F, -1.5708F, 3.1416F, 0.0F));

        ModelPartData body_spikes_r19 = body_spikes11.addChild("body_spikes_r19", ModelPartBuilder.create().uv(57, 11).cuboid(0.6213F, -0.2F, -2.1213F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -1.225F, 3.025F, 0.0F, 0.7854F, 0.0F));

        ModelPartData body_spikes_r20 = body_spikes11.addChild("body_spikes_r20", ModelPartBuilder.create().uv(57, 11).cuboid(-3.6213F, -0.2F, -2.1213F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -1.225F, 3.025F, 0.0F, -0.7854F, 0.0F));

        ModelPartData body_spikes12 = body_spikes.addChild("body_spikes12", ModelPartBuilder.create(), ModelTransform.of(-1.5F, 3.3737F, 0.8369F, -1.5708F, -1.5708F, 0.0F));

        ModelPartData body_spikes_r21 = body_spikes12.addChild("body_spikes_r21", ModelPartBuilder.create().uv(49, 10).cuboid(0.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, 7.7F, 6.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData body_spikes_r22 = body_spikes12.addChild("body_spikes_r22", ModelPartBuilder.create().uv(49, 10).cuboid(-3.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, 7.7F, 6.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData body_spikes13 = body_spikes.addChild("body_spikes13", ModelPartBuilder.create(), ModelTransform.of(-1.5F, 3.3737F, 0.8369F, -1.5708F, -1.5708F, 0.0F));

        ModelPartData body_spikes_r23 = body_spikes13.addChild("body_spikes_r23", ModelPartBuilder.create().uv(49, 10).cuboid(0.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, 7.7F, 7.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData body_spikes_r24 = body_spikes13.addChild("body_spikes_r24", ModelPartBuilder.create().uv(49, 10).cuboid(-3.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, 7.7F, 7.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData body_spikes14 = body_spikes.addChild("body_spikes14", ModelPartBuilder.create(), ModelTransform.of(-1.5F, 3.3737F, 0.8369F, -1.5708F, -1.5708F, 0.0F));

        ModelPartData body_spikes_r25 = body_spikes14.addChild("body_spikes_r25", ModelPartBuilder.create().uv(49, 10).cuboid(0.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, 7.2F, 10.4F, 0.0F, 0.7854F, 0.0F));

        ModelPartData body_spikes_r26 = body_spikes14.addChild("body_spikes_r26", ModelPartBuilder.create().uv(49, 10).cuboid(-3.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, 7.2F, 10.4F, 0.0F, -0.7854F, 0.0F));

        ModelPartData body_spikes15 = body_spikes.addChild("body_spikes15", ModelPartBuilder.create(), ModelTransform.of(-1.5F, 3.3737F, 0.8369F, -1.5708F, -1.5708F, 0.0F));

        ModelPartData body_spikes_r27 = body_spikes15.addChild("body_spikes_r27", ModelPartBuilder.create().uv(49, 1).cuboid(0.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, -5.825F, 10.375F, 0.0F, 0.7854F, 0.0F));

        ModelPartData body_spikes_r28 = body_spikes15.addChild("body_spikes_r28", ModelPartBuilder.create().uv(49, 1).cuboid(-3.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, -5.825F, 10.375F, 0.0F, -0.7854F, 0.0F));

        ModelPartData body_spikes16 = body_spikes.addChild("body_spikes16", ModelPartBuilder.create(), ModelTransform.of(-1.3F, 3.3987F, 2.8369F, -1.5708F, -1.5708F, 0.0F));

        ModelPartData body_spikes_r29 = body_spikes16.addChild("body_spikes_r29", ModelPartBuilder.create().uv(49, 1).cuboid(0.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-1.025F, -6.525F, 7.375F, 0.0F, 0.7854F, 0.0F));

        ModelPartData body_spikes_r30 = body_spikes16.addChild("body_spikes_r30", ModelPartBuilder.create().uv(49, 1).cuboid(-3.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-1.025F, -6.525F, 7.375F, 0.0F, -0.7854F, 0.0F));

        ModelPartData body_spikes17 = body_spikes.addChild("body_spikes17", ModelPartBuilder.create(), ModelTransform.of(-1.3F, 3.3987F, 2.8369F, -1.5708F, -1.5708F, 0.0F));

        ModelPartData body_spikes_r31 = body_spikes17.addChild("body_spikes_r31", ModelPartBuilder.create().uv(49, 1).cuboid(0.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-4.025F, -6.525F, 6.375F, 0.0F, 0.7854F, 0.0F));

        ModelPartData body_spikes_r32 = body_spikes17.addChild("body_spikes_r32", ModelPartBuilder.create().uv(49, 1).cuboid(-3.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-4.025F, -6.525F, 6.375F, 0.0F, -0.7854F, 0.0F));

        ModelPartData body_spikes18 = body_spikes.addChild("body_spikes18", ModelPartBuilder.create(), ModelTransform.of(-1.3F, 3.3987F, 2.8369F, -1.5708F, -1.5708F, 0.0F));

        ModelPartData body_spikes_r33 = body_spikes18.addChild("body_spikes_r33", ModelPartBuilder.create().uv(49, 1).cuboid(0.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-1.025F, -6.525F, 4.375F, 0.0F, 0.7854F, 0.0F));

        ModelPartData body_spikes_r34 = body_spikes18.addChild("body_spikes_r34", ModelPartBuilder.create().uv(49, 1).cuboid(-3.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-1.025F, -6.525F, 4.375F, 0.0F, -0.7854F, 0.0F));

        ModelPartData body_spikes19 = body_spikes.addChild("body_spikes19", ModelPartBuilder.create(), ModelTransform.of(-3.025F, 1.7737F, 5.1119F, -1.5708F, 0.0F, 0.0F));

        ModelPartData body_spikes_r35 = body_spikes19.addChild("body_spikes_r35", ModelPartBuilder.create().uv(49, 1).cuboid(0.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -0.8F, 3.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData body_spikes_r36 = body_spikes19.addChild("body_spikes_r36", ModelPartBuilder.create().uv(49, 1).cuboid(-3.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -0.8F, 3.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData body_spikes20 = body_spikes.addChild("body_spikes20", ModelPartBuilder.create(), ModelTransform.of(-3.025F, 1.7737F, 5.1119F, -1.5708F, 0.0F, 0.0F));

        ModelPartData body_spikes_r37 = body_spikes20.addChild("body_spikes_r37", ModelPartBuilder.create().uv(49, 1).cuboid(0.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(6.0F, -0.8F, 4.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData body_spikes_r38 = body_spikes20.addChild("body_spikes_r38", ModelPartBuilder.create().uv(49, 1).cuboid(-3.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(6.0F, -0.8F, 4.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData body_spikes21 = body_spikes.addChild("body_spikes21", ModelPartBuilder.create(), ModelTransform.of(-3.025F, 1.7737F, 5.1119F, -1.5708F, 0.0F, 0.0F));

        ModelPartData body_spikes_r39 = body_spikes21.addChild("body_spikes_r39", ModelPartBuilder.create().uv(49, 1).cuboid(0.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -0.8F, 7.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData body_spikes_r40 = body_spikes21.addChild("body_spikes_r40", ModelPartBuilder.create().uv(49, 1).cuboid(-3.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -0.8F, 7.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData body_spikes22 = body_spikes.addChild("body_spikes22", ModelPartBuilder.create(), ModelTransform.of(-3.025F, 1.7737F, 5.1119F, -1.5708F, 0.0F, 0.0F));

        ModelPartData body_spikes_r41 = body_spikes22.addChild("body_spikes_r41", ModelPartBuilder.create().uv(49, 1).cuboid(0.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, -0.8F, 5.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData body_spikes_r42 = body_spikes22.addChild("body_spikes_r42", ModelPartBuilder.create().uv(49, 1).cuboid(-3.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, -0.8F, 5.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData body_spikes23 = body_spikes.addChild("body_spikes23", ModelPartBuilder.create(), ModelTransform.of(-3.025F, 1.7737F, 5.1119F, -1.5708F, 0.0F, 0.0F));

        ModelPartData body_spikes_r43 = body_spikes23.addChild("body_spikes_r43", ModelPartBuilder.create().uv(49, 1).cuboid(0.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, -0.8F, 10.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData body_spikes_r44 = body_spikes23.addChild("body_spikes_r44", ModelPartBuilder.create().uv(49, 1).cuboid(-3.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, -0.8F, 10.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData body_spikes24 = body_spikes.addChild("body_spikes24", ModelPartBuilder.create(), ModelTransform.of(-3.025F, 1.7737F, 5.1119F, -1.5708F, 0.0F, 0.0F));

        ModelPartData body_spikes_r45 = body_spikes24.addChild("body_spikes_r45", ModelPartBuilder.create().uv(49, 1).cuboid(0.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(6.6F, -0.8F, 7.5F, 0.0F, 0.7854F, 0.0F));

        ModelPartData body_spikes_r46 = body_spikes24.addChild("body_spikes_r46", ModelPartBuilder.create().uv(49, 1).cuboid(-3.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(6.6F, -0.8F, 7.5F, 0.0F, -0.7854F, 0.0F));

        ModelPartData body_spikes25 = body_spikes.addChild("body_spikes25", ModelPartBuilder.create(), ModelTransform.of(-3.025F, 1.7737F, 4.9869F, -1.5708F, 0.0F, 0.0F));

        ModelPartData body_spikes_r47 = body_spikes25.addChild("body_spikes_r47", ModelPartBuilder.create().uv(49, 10).cuboid(0.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(6.6F, 9.2F, 4.5F, 0.0F, 0.7854F, 0.0F));

        ModelPartData body_spikes_r48 = body_spikes25.addChild("body_spikes_r48", ModelPartBuilder.create().uv(49, 10).cuboid(-3.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(6.6F, 9.2F, 4.5F, 0.0F, -0.7854F, 0.0F));

        ModelPartData body_spikes26 = body_spikes.addChild("body_spikes26", ModelPartBuilder.create(), ModelTransform.of(-3.025F, 1.7737F, 4.9869F, -1.5708F, 0.0F, 0.0F));

        ModelPartData body_spikes_r49 = body_spikes26.addChild("body_spikes_r49", ModelPartBuilder.create().uv(49, 10).cuboid(0.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.4F, 9.2F, 2.5F, 0.0F, 0.7854F, 0.0F));

        ModelPartData body_spikes_r50 = body_spikes26.addChild("body_spikes_r50", ModelPartBuilder.create().uv(49, 10).cuboid(-3.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.4F, 9.2F, 2.5F, 0.0F, -0.7854F, 0.0F));

        ModelPartData body_spikes27 = body_spikes.addChild("body_spikes27", ModelPartBuilder.create(), ModelTransform.of(-3.025F, 1.7737F, 4.9869F, -1.5708F, 0.0F, 0.0F));

        ModelPartData body_spikes_r51 = body_spikes27.addChild("body_spikes_r51", ModelPartBuilder.create().uv(49, 10).cuboid(0.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(2.6F, 9.2F, 4.5F, 0.0F, 0.7854F, 0.0F));

        ModelPartData body_spikes_r52 = body_spikes27.addChild("body_spikes_r52", ModelPartBuilder.create().uv(49, 10).cuboid(-3.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(2.6F, 9.2F, 4.5F, 0.0F, -0.7854F, 0.0F));

        ModelPartData body_spikes28 = body_spikes.addChild("body_spikes28", ModelPartBuilder.create(), ModelTransform.of(-3.025F, 1.7737F, 4.9869F, -1.5708F, 0.0F, 0.0F));

        ModelPartData body_spikes_r53 = body_spikes28.addChild("body_spikes_r53", ModelPartBuilder.create().uv(49, 10).cuboid(0.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(4.6F, 9.2F, 11.5F, 0.0F, 0.7854F, 0.0F));

        ModelPartData body_spikes_r54 = body_spikes28.addChild("body_spikes_r54", ModelPartBuilder.create().uv(49, 10).cuboid(-3.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(4.6F, 9.2F, 11.5F, 0.0F, -0.7854F, 0.0F));

        ModelPartData body_spikes29 = body_spikes.addChild("body_spikes29", ModelPartBuilder.create(), ModelTransform.of(-3.025F, 1.7737F, 4.9869F, -1.5708F, 0.0F, 0.0F));

        ModelPartData body_spikes_r55 = body_spikes29.addChild("body_spikes_r55", ModelPartBuilder.create().uv(49, 10).cuboid(0.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.6F, 9.2F, 9.5F, 0.0F, 0.7854F, 0.0F));

        ModelPartData body_spikes_r56 = body_spikes29.addChild("body_spikes_r56", ModelPartBuilder.create().uv(49, 10).cuboid(-3.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.6F, 9.2F, 9.5F, 0.0F, -0.7854F, 0.0F));

        ModelPartData body_spikes30 = body_spikes.addChild("body_spikes30", ModelPartBuilder.create(), ModelTransform.of(-3.025F, 1.7737F, 4.9869F, -1.5708F, 0.0F, 0.0F));

        ModelPartData body_spikes_r57 = body_spikes30.addChild("body_spikes_r57", ModelPartBuilder.create().uv(49, 10).cuboid(0.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(4.6F, 9.2F, 7.5F, 0.0F, 0.7854F, 0.0F));

        ModelPartData body_spikes_r58 = body_spikes30.addChild("body_spikes_r58", ModelPartBuilder.create().uv(49, 10).cuboid(-3.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(4.6F, 9.2F, 7.5F, 0.0F, -0.7854F, 0.0F));

        ModelPartData body_spikes31 = body_spikes.addChild("body_spikes31", ModelPartBuilder.create(), ModelTransform.of(-3.025F, 1.7737F, 4.9869F, -1.5708F, 0.0F, 0.0F));

        ModelPartData body_spikes_r59 = body_spikes31.addChild("body_spikes_r59", ModelPartBuilder.create().uv(49, 10).cuboid(0.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.4F, 9.2F, 6.5F, 0.0F, 0.7854F, 0.0F));

        ModelPartData body_spikes_r60 = body_spikes31.addChild("body_spikes_r60", ModelPartBuilder.create().uv(49, 10).cuboid(-3.6213F, -1.2F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.4F, 9.2F, 6.5F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_arm = modelPartData.addChild("right_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.pivot(-5.0F, 2.0F, 0.0F));

        ModelPartData right_arm_spikes = right_arm.addChild("right_arm_spikes", ModelPartBuilder.create(), ModelTransform.of(-7.0F, 22.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        ModelPartData right_arm_spikes2 = right_arm_spikes.addChild("right_arm_spikes2", ModelPartBuilder.create(), ModelTransform.of(-1.5F, -14.5263F, -1.3631F, -1.5708F, -1.5708F, 0.0F));

        ModelPartData right_arm_spikes_r1 = right_arm_spikes2.addChild("right_arm_spikes_r1", ModelPartBuilder.create().uv(49, 9).cuboid(0.6213F, -0.8F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(1.4F, -0.7F, -3.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_arm_spikes_r2 = right_arm_spikes2.addChild("right_arm_spikes_r2", ModelPartBuilder.create().uv(49, 9).cuboid(-3.6213F, -0.8F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(1.4F, -0.7F, -3.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_arm_spikes3 = right_arm_spikes.addChild("right_arm_spikes3", ModelPartBuilder.create(), ModelTransform.of(-1.5F, -14.5263F, -1.3631F, -1.5708F, -1.5708F, 0.0F));

        ModelPartData right_arm_spikes_r3 = right_arm_spikes3.addChild("right_arm_spikes_r3", ModelPartBuilder.create().uv(49, 9).cuboid(0.6213F, -0.8F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.3F, -0.7F, -5.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_arm_spikes_r4 = right_arm_spikes3.addChild("right_arm_spikes_r4", ModelPartBuilder.create().uv(49, 9).cuboid(-3.6213F, -0.8F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.3F, -0.7F, -5.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_arm_spikes4 = right_arm_spikes.addChild("right_arm_spikes4", ModelPartBuilder.create(), ModelTransform.of(-1.5F, -14.5263F, -1.3631F, -1.5708F, -1.5708F, 0.0F));

        ModelPartData right_arm_spikes_r5 = right_arm_spikes4.addChild("right_arm_spikes_r5", ModelPartBuilder.create().uv(49, 9).cuboid(0.6213F, -0.8F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(2.7F, -0.7F, -6.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_arm_spikes_r6 = right_arm_spikes4.addChild("right_arm_spikes_r6", ModelPartBuilder.create().uv(49, 9).cuboid(-3.6213F, -0.8F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(2.7F, -0.7F, -6.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_arm_spikes5 = right_arm_spikes.addChild("right_arm_spikes5", ModelPartBuilder.create(), ModelTransform.of(-1.9F, -22.3263F, 5.2369F, -1.5708F, 0.0F, 0.0F));

        ModelPartData right_arm_spikes_r7 = right_arm_spikes5.addChild("right_arm_spikes_r7", ModelPartBuilder.create().uv(49, 1).cuboid(0.6213F, -2.25F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.8F, 0.5F, 4.9F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_arm_spikes_r8 = right_arm_spikes5.addChild("right_arm_spikes_r8", ModelPartBuilder.create().uv(49, 1).cuboid(-3.6213F, -2.25F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.8F, 0.5F, 4.9F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_arm_spikes6 = right_arm_spikes.addChild("right_arm_spikes6", ModelPartBuilder.create(), ModelTransform.of(-1.9F, -21.8263F, 5.2369F, -1.5708F, 0.0F, 0.0F));

        ModelPartData right_arm_spikes_r9 = right_arm_spikes6.addChild("right_arm_spikes_r9", ModelPartBuilder.create().uv(49, 1).cuboid(0.6213F, -2.25F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.8F, 0.5F, 0.9F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_arm_spikes_r10 = right_arm_spikes6.addChild("right_arm_spikes_r10", ModelPartBuilder.create().uv(49, 1).cuboid(-3.6213F, -2.25F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.8F, 0.5F, 0.9F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_arm_spikes7 = right_arm_spikes.addChild("right_arm_spikes7", ModelPartBuilder.create(), ModelTransform.of(-1.9F, -22.2263F, 5.2369F, -1.5708F, 0.0F, 0.0F));

        ModelPartData right_arm_spikes_r11 = right_arm_spikes7.addChild("right_arm_spikes_r11", ModelPartBuilder.create().uv(49, 1).cuboid(0.6213F, -2.25F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-5.8F, 0.5F, 4.9F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_arm_spikes_r12 = right_arm_spikes7.addChild("right_arm_spikes_r12", ModelPartBuilder.create().uv(49, 1).cuboid(-3.6213F, -2.25F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-5.8F, 0.5F, 4.9F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_arm_spikes8 = right_arm_spikes.addChild("right_arm_spikes8", ModelPartBuilder.create(), ModelTransform.of(-1.9F, -22.0263F, 5.2369F, -1.5708F, 0.0F, 0.0F));

        ModelPartData right_arm_spikes_r13 = right_arm_spikes8.addChild("right_arm_spikes_r13", ModelPartBuilder.create().uv(49, 1).cuboid(0.6213F, -2.25F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-5.2F, 0.5F, 2.1F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_arm_spikes_r14 = right_arm_spikes8.addChild("right_arm_spikes_r14", ModelPartBuilder.create().uv(49, 1).cuboid(-3.6213F, -2.25F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-5.2F, 0.5F, 2.1F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_arm_spikes9 = right_arm_spikes.addChild("right_arm_spikes9", ModelPartBuilder.create(), ModelTransform.of(-1.9F, -21.8263F, 5.2369F, -1.5708F, 0.0F, 0.0F));

        ModelPartData right_arm_spikes_r15 = right_arm_spikes9.addChild("right_arm_spikes_r15", ModelPartBuilder.create().uv(49, 10).cuboid(0.6213F, -1.25F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-5.2F, 9.5F, 2.1F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_arm_spikes_r16 = right_arm_spikes9.addChild("right_arm_spikes_r16", ModelPartBuilder.create().uv(49, 10).cuboid(-3.6213F, -1.25F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-5.2F, 9.5F, 2.1F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_arm_spikes10 = right_arm_spikes.addChild("right_arm_spikes10", ModelPartBuilder.create(), ModelTransform.of(-1.9F, -21.8263F, 5.2369F, -1.5708F, 0.0F, 0.0F));

        ModelPartData right_arm_spikes_r17 = right_arm_spikes10.addChild("right_arm_spikes_r17", ModelPartBuilder.create().uv(49, 10).cuboid(0.6213F, -1.25F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.8F, 9.5F, 0.9F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_arm_spikes_r18 = right_arm_spikes10.addChild("right_arm_spikes_r18", ModelPartBuilder.create().uv(49, 10).cuboid(-3.6213F, -1.25F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.8F, 9.5F, 0.9F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_arm_spikes11 = right_arm_spikes.addChild("right_arm_spikes11", ModelPartBuilder.create(), ModelTransform.of(-1.9F, -22.2263F, 5.2369F, -1.5708F, 0.0F, 0.0F));

        ModelPartData right_arm_spikes_r19 = right_arm_spikes11.addChild("right_arm_spikes_r19", ModelPartBuilder.create().uv(49, 10).cuboid(0.6213F, -1.25F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.8F, 9.5F, 4.9F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_arm_spikes_r20 = right_arm_spikes11.addChild("right_arm_spikes_r20", ModelPartBuilder.create().uv(49, 10).cuboid(-3.6213F, -1.25F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.8F, 9.5F, 4.9F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_arm_spikes12 = right_arm_spikes.addChild("right_arm_spikes12", ModelPartBuilder.create(), ModelTransform.of(-4.7F, -26.0263F, -1.0131F, 3.1416F, 0.0F, 0.0F));

        ModelPartData right_arm_spikes_r21 = right_arm_spikes12.addChild("right_arm_spikes_r21", ModelPartBuilder.create().uv(57, 11).cuboid(0.6213F, -0.25F, -2.1213F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -0.75F, 3.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_arm_spikes_r22 = right_arm_spikes12.addChild("right_arm_spikes_r22", ModelPartBuilder.create().uv(57, 11).cuboid(-3.6213F, -0.25F, -2.1213F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -0.75F, 3.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_arm_spikes13 = right_arm_spikes.addChild("right_arm_spikes13", ModelPartBuilder.create(), ModelTransform.of(-4.7F, -26.0263F, -1.0131F, 3.1416F, 0.0F, 0.0F));

        ModelPartData right_arm_spikes_r23 = right_arm_spikes13.addChild("right_arm_spikes_r23", ModelPartBuilder.create().uv(49, 10).cuboid(0.6213F, -0.25F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-3.0F, -0.75F, 1.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_arm_spikes_r24 = right_arm_spikes13.addChild("right_arm_spikes_r24", ModelPartBuilder.create().uv(49, 10).cuboid(-3.6213F, -0.25F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-3.0F, -0.75F, 1.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData left_arm = modelPartData.addChild("left_arm", ModelPartBuilder.create().uv(40, 16).mirrored().cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)).mirrored(false), ModelTransform.pivot(5.0F, 2.0F, 0.0F));

        ModelPartData left_arm_spikes = left_arm.addChild("left_arm_spikes", ModelPartBuilder.create(), ModelTransform.pivot(-3.7F, 8.0F, 15.0F));

        ModelPartData left_arm_spikes2 = left_arm_spikes.addChild("left_arm_spikes2", ModelPartBuilder.create(), ModelTransform.of(9.2F, -0.5263F, -16.3631F, -1.5708F, -1.5708F, 0.0F));

        ModelPartData left_arm_spikes_r1 = left_arm_spikes2.addChild("left_arm_spikes_r1", ModelPartBuilder.create().uv(49, 9).cuboid(0.6213F, -0.8F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(1.4F, -0.7F, -3.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData left_arm_spikes_r2 = left_arm_spikes2.addChild("left_arm_spikes_r2", ModelPartBuilder.create().uv(49, 9).cuboid(-3.6213F, -0.8F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(1.4F, -0.7F, -3.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData left_arm_spikes3 = left_arm_spikes.addChild("left_arm_spikes3", ModelPartBuilder.create(), ModelTransform.of(9.2F, -0.5263F, -16.3631F, -1.5708F, -1.5708F, 0.0F));

        ModelPartData left_arm_spikes_r3 = left_arm_spikes3.addChild("left_arm_spikes_r3", ModelPartBuilder.create().uv(49, 9).cuboid(0.6213F, -0.8F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.3F, -0.7F, -5.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData left_arm_spikes_r4 = left_arm_spikes3.addChild("left_arm_spikes_r4", ModelPartBuilder.create().uv(49, 9).cuboid(-3.6213F, -0.8F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.3F, -0.7F, -5.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData left_arm_spikes4 = left_arm_spikes.addChild("left_arm_spikes4", ModelPartBuilder.create(), ModelTransform.of(9.2F, -0.5263F, -16.3631F, -1.5708F, -1.5708F, 0.0F));

        ModelPartData left_arm_spikes_r5 = left_arm_spikes4.addChild("left_arm_spikes_r5", ModelPartBuilder.create().uv(49, 9).cuboid(0.6213F, -0.8F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(2.7F, -0.7F, -6.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData left_arm_spikes_r6 = left_arm_spikes4.addChild("left_arm_spikes_r6", ModelPartBuilder.create().uv(49, 9).cuboid(-3.6213F, -0.8F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(2.7F, -0.7F, -6.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData left_arm_spikes5 = left_arm_spikes.addChild("left_arm_spikes5", ModelPartBuilder.create(), ModelTransform.of(8.8F, -8.3263F, -9.7631F, -1.5708F, 0.0F, 0.0F));

        ModelPartData left_arm_spikes_r7 = left_arm_spikes5.addChild("left_arm_spikes_r7", ModelPartBuilder.create().uv(49, 1).cuboid(0.6213F, -2.25F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.8F, 0.5F, 4.9F, 0.0F, 0.7854F, 0.0F));

        ModelPartData left_arm_spikes_r8 = left_arm_spikes5.addChild("left_arm_spikes_r8", ModelPartBuilder.create().uv(49, 1).cuboid(-3.6213F, -2.25F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.8F, 0.5F, 4.9F, 0.0F, -0.7854F, 0.0F));

        ModelPartData left_arm_spikes6 = left_arm_spikes.addChild("left_arm_spikes6", ModelPartBuilder.create(), ModelTransform.of(8.8F, -7.8263F, -9.7631F, -1.5708F, 0.0F, 0.0F));

        ModelPartData left_leg_spikes_r1 = left_arm_spikes6.addChild("left_leg_spikes_r1", ModelPartBuilder.create().uv(49, 1).cuboid(0.6213F, -2.25F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.8F, 0.5F, 0.9F, 0.0F, 0.7854F, 0.0F));

        ModelPartData left_leg_spikes_r2 = left_arm_spikes6.addChild("left_leg_spikes_r2", ModelPartBuilder.create().uv(49, 1).cuboid(-3.6213F, -2.25F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.8F, 0.5F, 0.9F, 0.0F, -0.7854F, 0.0F));

        ModelPartData left_arm_spikes7 = left_arm_spikes.addChild("left_arm_spikes7", ModelPartBuilder.create(), ModelTransform.of(8.8F, -8.2263F, -9.7631F, -1.5708F, 0.0F, 0.0F));

        ModelPartData left_arm_spikes_r9 = left_arm_spikes7.addChild("left_arm_spikes_r9", ModelPartBuilder.create().uv(49, 1).cuboid(0.6213F, -2.25F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-5.8F, 0.5F, 4.9F, 0.0F, 0.7854F, 0.0F));

        ModelPartData left_arm_spikes_r10 = left_arm_spikes7.addChild("left_arm_spikes_r10", ModelPartBuilder.create().uv(49, 1).cuboid(-3.6213F, -2.25F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-5.8F, 0.5F, 4.9F, 0.0F, -0.7854F, 0.0F));

        ModelPartData left_arm_spikes8 = left_arm_spikes.addChild("left_arm_spikes8", ModelPartBuilder.create(), ModelTransform.of(8.8F, -8.0263F, -9.7631F, -1.5708F, 0.0F, 0.0F));

        ModelPartData left_arm_spikes_r11 = left_arm_spikes8.addChild("left_arm_spikes_r11", ModelPartBuilder.create().uv(49, 1).cuboid(0.6213F, -2.25F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-5.2F, 0.5F, 2.1F, 0.0F, 0.7854F, 0.0F));

        ModelPartData left_arm_spikes_r12 = left_arm_spikes8.addChild("left_arm_spikes_r12", ModelPartBuilder.create().uv(49, 1).cuboid(-3.6213F, -2.25F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-5.2F, 0.5F, 2.1F, 0.0F, -0.7854F, 0.0F));

        ModelPartData left_arm_spikes9 = left_arm_spikes.addChild("left_arm_spikes9", ModelPartBuilder.create(), ModelTransform.of(8.8F, -7.8263F, -9.7631F, -1.5708F, 0.0F, 0.0F));

        ModelPartData left_arm_spikes_r13 = left_arm_spikes9.addChild("left_arm_spikes_r13", ModelPartBuilder.create().uv(49, 10).cuboid(0.6213F, -1.25F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-5.2F, 9.5F, 2.1F, 0.0F, 0.7854F, 0.0F));

        ModelPartData left_arm_spikes_r14 = left_arm_spikes9.addChild("left_arm_spikes_r14", ModelPartBuilder.create().uv(49, 10).cuboid(-3.6213F, -1.25F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-5.2F, 9.5F, 2.1F, 0.0F, -0.7854F, 0.0F));

        ModelPartData left_arm_spikes10 = left_arm_spikes.addChild("left_arm_spikes10", ModelPartBuilder.create(), ModelTransform.of(8.8F, -7.8263F, -9.7631F, -1.5708F, 0.0F, 0.0F));

        ModelPartData left_arm_spikes_r15 = left_arm_spikes10.addChild("left_arm_spikes_r15", ModelPartBuilder.create().uv(49, 10).cuboid(0.6213F, -1.25F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.8F, 9.5F, 0.9F, 0.0F, 0.7854F, 0.0F));

        ModelPartData left_arm_spikes_r16 = left_arm_spikes10.addChild("left_arm_spikes_r16", ModelPartBuilder.create().uv(49, 10).cuboid(-3.6213F, -1.25F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.8F, 9.5F, 0.9F, 0.0F, -0.7854F, 0.0F));

        ModelPartData left_arm_spikes11 = left_arm_spikes.addChild("left_arm_spikes11", ModelPartBuilder.create(), ModelTransform.of(8.8F, -8.2263F, -9.7631F, -1.5708F, 0.0F, 0.0F));

        ModelPartData left_arm_spikes_r17 = left_arm_spikes11.addChild("left_arm_spikes_r17", ModelPartBuilder.create().uv(49, 10).cuboid(0.6213F, -1.25F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.8F, 9.5F, 4.9F, 0.0F, 0.7854F, 0.0F));

        ModelPartData left_arm_spikes_r18 = left_arm_spikes11.addChild("left_arm_spikes_r18", ModelPartBuilder.create().uv(49, 10).cuboid(-3.6213F, -1.25F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.8F, 9.5F, 4.9F, 0.0F, -0.7854F, 0.0F));

        ModelPartData left_arm_spikes12 = left_arm_spikes.addChild("left_arm_spikes12", ModelPartBuilder.create(), ModelTransform.of(6.0F, -12.0263F, -16.0131F, 3.1416F, 0.0F, 0.0F));

        ModelPartData left_arm_spikes_r19 = left_arm_spikes12.addChild("left_arm_spikes_r19", ModelPartBuilder.create().uv(57, 11).cuboid(0.6213F, -0.25F, -2.1213F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -0.75F, 3.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData left_arm_spikes_r20 = left_arm_spikes12.addChild("left_arm_spikes_r20", ModelPartBuilder.create().uv(57, 11).cuboid(-3.6213F, -0.25F, -2.1213F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -0.75F, 3.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData left_arm_spikes13 = left_arm_spikes.addChild("left_arm_spikes13", ModelPartBuilder.create(), ModelTransform.of(6.0F, -12.0263F, -16.0131F, 3.1416F, 0.0F, 0.0F));

        ModelPartData left_arm_spikes_r21 = left_arm_spikes13.addChild("left_arm_spikes_r21", ModelPartBuilder.create().uv(49, 10).cuboid(0.6213F, -0.25F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-3.0F, -0.75F, 1.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData left_arm_spikes_r22 = left_arm_spikes13.addChild("left_arm_spikes_r22", ModelPartBuilder.create().uv(49, 10).cuboid(-3.6213F, -0.25F, -2.1213F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-3.0F, -0.75F, 1.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_leg = modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F))
                .uv(0, 48).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.pivot(-1.9F, 12.0F, 0.0F));

        ModelPartData right_leg_spikes = right_leg.addChild("right_leg_spikes", ModelPartBuilder.create(), ModelTransform.of(-2.1F, 12.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        ModelPartData right_leg_spikes2 = right_leg_spikes.addChild("right_leg_spikes2", ModelPartBuilder.create(), ModelTransform.of(0.9F, -4.4263F, 5.2369F, -1.5708F, 0.0F, 0.0F));

        ModelPartData right_leg_spikes_r1 = right_leg_spikes2.addChild("right_leg_spikes_r1", ModelPartBuilder.create().uv(49, 1).cuboid(-1.5F, -1.75F, -5.6569F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 4.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_leg_spikes_r2 = right_leg_spikes2.addChild("right_leg_spikes_r2", ModelPartBuilder.create().uv(49, 1).cuboid(-7.1569F, -1.75F, 0.0F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 4.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_leg_spikes3 = right_leg_spikes.addChild("right_leg_spikes3", ModelPartBuilder.create(), ModelTransform.of(3.5F, -4.6263F, 5.2369F, -1.5708F, 0.0F, 0.0F));

        ModelPartData right_leg_spikes_r3 = right_leg_spikes3.addChild("right_leg_spikes_r3", ModelPartBuilder.create().uv(49, 1).cuboid(-1.5F, -1.75F, -5.6569F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 8.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_leg_spikes_r4 = right_leg_spikes3.addChild("right_leg_spikes_r4", ModelPartBuilder.create().uv(49, 1).cuboid(-7.1569F, -1.75F, 0.0F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 8.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_leg_spikes4 = right_leg_spikes.addChild("right_leg_spikes4", ModelPartBuilder.create(), ModelTransform.of(6.1F, -7.8263F, 5.2369F, -1.5708F, 0.0F, 0.0F));

        ModelPartData right_leg_spikes_r5 = right_leg_spikes4.addChild("right_leg_spikes_r5", ModelPartBuilder.create().uv(49, 1).cuboid(-1.5F, -1.75F, -5.6569F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.5F, 0.0F, 8.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_leg_spikes_r6 = right_leg_spikes4.addChild("right_leg_spikes_r6", ModelPartBuilder.create().uv(49, 1).cuboid(-7.1569F, -1.75F, 0.0F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.5F, 0.0F, 8.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_leg_spikes6 = right_leg_spikes.addChild("right_leg_spikes6", ModelPartBuilder.create(), ModelTransform.of(1.3F, -3.6263F, 5.2369F, -1.5708F, 0.0F, 0.0F));

        ModelPartData right_leg_spikes_r7 = right_leg_spikes6.addChild("right_leg_spikes_r7", ModelPartBuilder.create().uv(49, 9).cuboid(-1.5707F, -1.775F, -5.7276F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 10.0F, 4.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_leg_spikes_r8 = right_leg_spikes6.addChild("right_leg_spikes_r8", ModelPartBuilder.create().uv(49, 9).cuboid(-7.2276F, -1.775F, 0.0707F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 10.0F, 4.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_leg_spikes5 = right_leg_spikes.addChild("right_leg_spikes5", ModelPartBuilder.create(), ModelTransform.of(1.5F, -2.7263F, 5.2369F, -1.5708F, 0.0F, 0.0F));

        ModelPartData right_leg_spikes_r9 = right_leg_spikes5.addChild("right_leg_spikes_r9", ModelPartBuilder.create().uv(49, 9).cuboid(-1.5707F, -1.775F, -5.7276F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(2.0F, 10.0F, 5.6F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_leg_spikes_r10 = right_leg_spikes5.addChild("right_leg_spikes_r10", ModelPartBuilder.create().uv(49, 9).cuboid(-7.2276F, -1.775F, 0.0707F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(2.0F, 10.0F, 5.6F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_leg_spikes7 = right_leg_spikes.addChild("right_leg_spikes7", ModelPartBuilder.create(), ModelTransform.of(1.5F, -2.7263F, 5.2369F, -1.5708F, 0.0F, 0.0F));

        ModelPartData right_leg_spikes_r11 = right_leg_spikes7.addChild("right_leg_spikes_r11", ModelPartBuilder.create().uv(49, 9).cuboid(-1.5707F, -1.775F, -5.7276F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(2.0F, 10.0F, 2.6F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_leg_spikes_r12 = right_leg_spikes7.addChild("right_leg_spikes_r12", ModelPartBuilder.create().uv(49, 9).cuboid(-7.2276F, -1.775F, 0.0707F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(2.0F, 10.0F, 2.6F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_leg_spikes8 = right_leg_spikes.addChild("right_leg_spikes8", ModelPartBuilder.create(), ModelTransform.of(6.5F, -0.5263F, -1.3631F, -1.5708F, -1.5708F, 0.0F));

        ModelPartData right_leg_spikes_r13 = right_leg_spikes8.addChild("right_leg_spikes_r13", ModelPartBuilder.create().uv(49, 9).cuboid(1.3284F, -5.6F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 4.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_leg_spikes_r14 = right_leg_spikes8.addChild("right_leg_spikes_r14", ModelPartBuilder.create().uv(49, 9).cuboid(-4.3284F, -5.6F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 4.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_leg_spikes9 = right_leg_spikes.addChild("right_leg_spikes9", ModelPartBuilder.create(), ModelTransform.of(6.5F, -0.5263F, -1.3631F, -1.5708F, -1.5708F, 0.0F));

        ModelPartData right_leg_spikes_r15 = right_leg_spikes9.addChild("right_leg_spikes_r15", ModelPartBuilder.create().uv(49, 9).cuboid(1.3284F, -5.6F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_leg_spikes_r16 = right_leg_spikes9.addChild("right_leg_spikes_r16", ModelPartBuilder.create().uv(49, 9).cuboid(-4.3284F, -5.6F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_leg_spikes10 = right_leg_spikes.addChild("right_leg_spikes10", ModelPartBuilder.create(), ModelTransform.of(6.5F, -0.5263F, -1.3631F, -1.5708F, -1.5708F, 0.0F));

        ModelPartData right_leg_spikes_r17 = right_leg_spikes10.addChild("right_leg_spikes_r17", ModelPartBuilder.create().uv(49, 9).cuboid(1.3284F, -5.6F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, 0.0F, 2.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_leg_spikes_r18 = right_leg_spikes10.addChild("right_leg_spikes_r18", ModelPartBuilder.create().uv(49, 9).cuboid(-4.3284F, -5.6F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, 0.0F, 2.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_leg_spikes11 = right_leg_spikes.addChild("right_leg_spikes11", ModelPartBuilder.create(), ModelTransform.of(6.5F, -0.5263F, -1.3631F, -1.5708F, -1.5708F, 0.0F));

        ModelPartData right_leg_spikes_r19 = right_leg_spikes11.addChild("right_leg_spikes_r19", ModelPartBuilder.create().uv(49, 9).cuboid(1.3284F, -5.6F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(1.4F, -0.7F, -3.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_leg_spikes_r20 = right_leg_spikes11.addChild("right_leg_spikes_r20", ModelPartBuilder.create().uv(49, 9).cuboid(-4.3284F, -5.6F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(1.4F, -0.7F, -3.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_leg_spikes12 = right_leg_spikes.addChild("right_leg_spikes12", ModelPartBuilder.create(), ModelTransform.of(6.5F, -0.5263F, -1.3631F, -1.5708F, -1.5708F, 0.0F));

        ModelPartData right_leg_spikes_r21 = right_leg_spikes12.addChild("right_leg_spikes_r21", ModelPartBuilder.create().uv(49, 9).cuboid(1.3284F, -5.6F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.3F, -0.7F, -5.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_leg_spikes_r22 = right_leg_spikes12.addChild("right_leg_spikes_r22", ModelPartBuilder.create().uv(49, 9).cuboid(-4.3284F, -5.6F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.3F, -0.7F, -5.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_leg_spikes13 = right_leg_spikes.addChild("right_leg_spikes13", ModelPartBuilder.create(), ModelTransform.of(6.5F, -0.5263F, -1.3631F, -1.5708F, -1.5708F, 0.0F));

        ModelPartData right_leg_spikes_r23 = right_leg_spikes13.addChild("right_leg_spikes_r23", ModelPartBuilder.create().uv(49, 9).cuboid(1.3284F, -5.6F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(2.7F, -0.7F, -6.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_leg_spikes_r24 = right_leg_spikes13.addChild("right_leg_spikes_r24", ModelPartBuilder.create().uv(49, 9).cuboid(-4.3284F, -5.6F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(2.7F, -0.7F, -6.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_leg_spikes14 = right_leg_spikes.addChild("right_leg_spikes14", ModelPartBuilder.create(), ModelTransform.of(6.1F, -7.8263F, 5.2369F, -1.5708F, 0.0F, 0.0F));

        ModelPartData right_leg_spikes_r25 = right_leg_spikes14.addChild("right_leg_spikes_r25", ModelPartBuilder.create().uv(49, 1).cuboid(-1.5F, -1.75F, -5.6569F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.8F, 0.5F, 4.9F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_leg_spikes_r26 = right_leg_spikes14.addChild("right_leg_spikes_r26", ModelPartBuilder.create().uv(49, 1).cuboid(-7.1569F, -1.75F, 0.0F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.8F, 0.5F, 4.9F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_leg_spikes15 = right_leg_spikes.addChild("right_leg_spikes15", ModelPartBuilder.create(), ModelTransform.of(6.1F, -7.8263F, 5.2369F, -1.5708F, 0.0F, 0.0F));

        ModelPartData right_leg_spikes_r27 = right_leg_spikes15.addChild("right_leg_spikes_r27", ModelPartBuilder.create().uv(49, 1).cuboid(-1.5F, -1.75F, -5.6569F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.8F, 0.5F, 0.9F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_leg_spikes_r28 = right_leg_spikes15.addChild("right_leg_spikes_r28", ModelPartBuilder.create().uv(49, 1).cuboid(-7.1569F, -1.75F, 0.0F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.8F, 0.5F, 0.9F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_leg_spikes16 = right_leg_spikes.addChild("right_leg_spikes16", ModelPartBuilder.create(), ModelTransform.of(6.1F, -7.8263F, 5.2369F, -1.5708F, 0.0F, 0.0F));

        ModelPartData right_leg_spikes_r29 = right_leg_spikes16.addChild("right_leg_spikes_r29", ModelPartBuilder.create().uv(49, 1).cuboid(-1.5F, -1.75F, -5.6569F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-5.8F, 0.5F, 4.9F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_leg_spikes_r30 = right_leg_spikes16.addChild("right_leg_spikes_r30", ModelPartBuilder.create().uv(49, 1).cuboid(-7.1569F, -1.75F, 0.0F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-5.8F, 0.5F, 4.9F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_leg_spikes17 = right_leg_spikes.addChild("right_leg_spikes17", ModelPartBuilder.create(), ModelTransform.of(6.1F, -7.8263F, 5.2369F, -1.5708F, 0.0F, 0.0F));

        ModelPartData right_leg_spikes_r31 = right_leg_spikes17.addChild("right_leg_spikes_r31", ModelPartBuilder.create().uv(49, 1).cuboid(-1.5F, -1.75F, -5.6569F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-5.2F, 0.5F, 2.1F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_leg_spikes_r32 = right_leg_spikes17.addChild("right_leg_spikes_r32", ModelPartBuilder.create().uv(49, 1).cuboid(-7.1569F, -1.75F, 0.0F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-5.2F, 0.5F, 2.1F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_leg_spikes18 = right_leg_spikes.addChild("right_leg_spikes18", ModelPartBuilder.create(), ModelTransform.of(1.5F, -2.7263F, 5.2369F, -1.5708F, 0.0F, 0.0F));

        ModelPartData right_leg_spikes_r33 = right_leg_spikes18.addChild("right_leg_spikes_r33", ModelPartBuilder.create().uv(49, 1).cuboid(-1.5F, -1.75F, -5.6569F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-1.2F, 0.0F, 5.1F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_leg_spikes_r34 = right_leg_spikes18.addChild("right_leg_spikes_r34", ModelPartBuilder.create().uv(49, 1).cuboid(-7.1569F, -1.75F, 0.0F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-1.2F, 0.0F, 5.1F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_leg_spikes19 = right_leg_spikes.addChild("right_leg_spikes19", ModelPartBuilder.create(), ModelTransform.of(6.4F, -7.4263F, 5.2369F, -1.5708F, 0.0F, 0.0F));

        ModelPartData right_leg_spikes_r35 = right_leg_spikes19.addChild("right_leg_spikes_r35", ModelPartBuilder.create().uv(49, 10).cuboid(-1.5F, -1.75F, -5.6569F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-5.2F, 9.5F, 2.1F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_leg_spikes_r36 = right_leg_spikes19.addChild("right_leg_spikes_r36", ModelPartBuilder.create().uv(49, 10).cuboid(-7.1569F, -1.75F, 0.0F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-5.2F, 9.5F, 2.1F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_leg_spikes21 = right_leg_spikes.addChild("right_leg_spikes21", ModelPartBuilder.create(), ModelTransform.of(6.1F, -7.8263F, 5.2369F, -1.5708F, 0.0F, 0.0F));

        ModelPartData right_leg_spikes_r37 = right_leg_spikes21.addChild("right_leg_spikes_r37", ModelPartBuilder.create().uv(49, 10).cuboid(-1.5F, -1.75F, -5.6569F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.8F, 9.5F, 0.9F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_leg_spikes_r38 = right_leg_spikes21.addChild("right_leg_spikes_r38", ModelPartBuilder.create().uv(49, 10).cuboid(-7.1569F, -1.75F, 0.0F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.8F, 9.5F, 0.9F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_leg_spikes20 = right_leg_spikes.addChild("right_leg_spikes20", ModelPartBuilder.create(), ModelTransform.of(6.1F, -7.8263F, 5.2369F, -1.5708F, 0.0F, 0.0F));

        ModelPartData right_leg_spikes_r39 = right_leg_spikes20.addChild("right_leg_spikes_r39", ModelPartBuilder.create().uv(49, 10).cuboid(-1.5F, -1.75F, -5.6569F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.8F, 9.5F, 4.9F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_leg_spikes_r40 = right_leg_spikes20.addChild("right_leg_spikes_r40", ModelPartBuilder.create().uv(49, 10).cuboid(-7.1569F, -1.75F, 0.0F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.8F, 9.5F, 4.9F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_leg_spikes22 = right_leg_spikes.addChild("right_leg_spikes22", ModelPartBuilder.create(), ModelTransform.of(-7.1F, -0.5263F, -1.3631F, -1.5708F, 1.5708F, 0.0F));

        ModelPartData right_leg_spikes_r41 = right_leg_spikes22.addChild("right_leg_spikes_r41", ModelPartBuilder.create().uv(57, 11).cuboid(1.3284F, -4.6F, -2.8284F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.6F, 4.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_leg_spikes_r42 = right_leg_spikes22.addChild("right_leg_spikes_r42", ModelPartBuilder.create().uv(57, 11).cuboid(-4.3284F, -4.6F, -2.8284F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.6F, 4.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_leg_spikes23 = right_leg_spikes.addChild("right_leg_spikes23", ModelPartBuilder.create(), ModelTransform.of(-7.1F, -0.5263F, 0.6369F, -1.5708F, 1.5708F, 0.0F));

        ModelPartData right_leg_spikes_r43 = right_leg_spikes23.addChild("right_leg_spikes_r43", ModelPartBuilder.create().uv(57, 11).cuboid(1.3284F, -4.6F, -2.8284F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.6F, 1.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_leg_spikes_r44 = right_leg_spikes23.addChild("right_leg_spikes_r44", ModelPartBuilder.create().uv(57, 11).cuboid(-4.3284F, -4.6F, -2.8284F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.6F, 1.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_leg_spikes24 = right_leg_spikes.addChild("right_leg_spikes24", ModelPartBuilder.create(), ModelTransform.of(-7.1F, -0.5263F, 0.6369F, -1.5708F, 1.5708F, 0.0F));

        ModelPartData right_leg_spikes_r45 = right_leg_spikes24.addChild("right_leg_spikes_r45", ModelPartBuilder.create().uv(57, 11).cuboid(1.3284F, -4.6F, -2.8284F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(1.7F, 2.125F, -3.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_leg_spikes_r46 = right_leg_spikes24.addChild("right_leg_spikes_r46", ModelPartBuilder.create().uv(57, 11).cuboid(-4.3284F, -4.6F, -2.8284F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(1.7F, 2.125F, -3.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_leg_spikes25 = right_leg_spikes.addChild("right_leg_spikes25", ModelPartBuilder.create(), ModelTransform.of(-7.1F, -0.5263F, 0.6369F, -1.5708F, 1.5708F, 0.0F));

        ModelPartData right_leg_spikes_r47 = right_leg_spikes25.addChild("right_leg_spikes_r47", ModelPartBuilder.create().uv(57, 11).cuboid(1.3284F, -4.6F, -2.8284F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.3F, 2.125F, -4.2F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_leg_spikes_r48 = right_leg_spikes25.addChild("right_leg_spikes_r48", ModelPartBuilder.create().uv(57, 11).cuboid(-4.3284F, -4.6F, -2.8284F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.3F, 2.125F, -4.2F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_leg_spikes26 = right_leg_spikes.addChild("right_leg_spikes26", ModelPartBuilder.create(), ModelTransform.of(-7.1F, -0.5263F, 0.6369F, -1.5708F, 1.5708F, 0.0F));

        ModelPartData right_leg_spikes_r49 = right_leg_spikes26.addChild("right_leg_spikes_r49", ModelPartBuilder.create().uv(57, 11).cuboid(1.3284F, -4.6F, -2.8284F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.8F, 2.125F, -6.7F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_leg_spikes_r50 = right_leg_spikes26.addChild("right_leg_spikes_r50", ModelPartBuilder.create().uv(57, 11).cuboid(-4.3284F, -4.6F, -2.8284F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.8F, 2.125F, -6.7F, 0.0F, -0.7854F, 0.0F));

        ModelPartData left_leg = modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(0, 16).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)).mirrored(false)
                .uv(0, 48).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.5F)).mirrored(false), ModelTransform.pivot(1.9F, 12.0F, 0.0F));

        ModelPartData left_leg_spikes = left_leg.addChild("left_leg_spikes", ModelPartBuilder.create(), ModelTransform.pivot(-4.6F, 12.0F, 15.0F));

        ModelPartData left_leg_spikes2 = left_leg_spikes.addChild("left_leg_spikes2", ModelPartBuilder.create(), ModelTransform.of(3.6F, -3.4263F, -9.7631F, -1.5708F, 0.0F, 0.0F));

        ModelPartData left_leg_spikes_r3 = left_leg_spikes2.addChild("left_leg_spikes_r3", ModelPartBuilder.create().uv(49, 1).cuboid(1.3284F, -1.75F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 4.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData left_leg_spikes_r4 = left_leg_spikes2.addChild("left_leg_spikes_r4", ModelPartBuilder.create().uv(49, 1).cuboid(-4.3284F, -1.75F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 4.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData left_leg_spikes3 = left_leg_spikes.addChild("left_leg_spikes3", ModelPartBuilder.create(), ModelTransform.of(6.2F, -4.6263F, -9.7631F, -1.5708F, 0.0F, 0.0F));

        ModelPartData left_leg_spikes_r5 = left_leg_spikes3.addChild("left_leg_spikes_r5", ModelPartBuilder.create().uv(49, 1).cuboid(1.3284F, -1.75F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 8.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData left_leg_spikes_r6 = left_leg_spikes3.addChild("left_leg_spikes_r6", ModelPartBuilder.create().uv(49, 1).cuboid(-4.3284F, -1.75F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 8.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData left_leg_spikes4 = left_leg_spikes.addChild("left_leg_spikes4", ModelPartBuilder.create(), ModelTransform.of(8.8F, -7.8263F, -9.7631F, -1.5708F, 0.0F, 0.0F));

        ModelPartData left_leg_spikes_r7 = left_leg_spikes4.addChild("left_leg_spikes_r7", ModelPartBuilder.create().uv(49, 1).cuboid(1.3284F, -1.75F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.5F, 0.0F, 8.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData left_leg_spikes_r8 = left_leg_spikes4.addChild("left_leg_spikes_r8", ModelPartBuilder.create().uv(49, 1).cuboid(-4.3284F, -1.75F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.5F, 0.0F, 8.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData left_leg_spikes5 = left_leg_spikes.addChild("left_leg_spikes5", ModelPartBuilder.create(), ModelTransform.of(3.3F, -2.7263F, -9.7631F, -1.5708F, 0.0F, 0.0F));

        ModelPartData left_leg_spikes_r9 = left_leg_spikes5.addChild("left_leg_spikes_r9", ModelPartBuilder.create().uv(49, 9).cuboid(1.2577F, -1.775F, -2.8991F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 10.0F, 4.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData left_leg_spikes_r10 = left_leg_spikes5.addChild("left_leg_spikes_r10", ModelPartBuilder.create().uv(49, 9).cuboid(-4.3991F, -1.775F, -2.7577F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 10.0F, 4.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData left_leg_spikes6 = left_leg_spikes.addChild("left_leg_spikes6", ModelPartBuilder.create(), ModelTransform.of(4.2F, -2.7263F, -9.7631F, -1.5708F, 0.0F, 0.0F));

        ModelPartData left_leg_spikes_r11 = left_leg_spikes6.addChild("left_leg_spikes_r11", ModelPartBuilder.create().uv(49, 9).cuboid(1.2577F, -1.775F, -2.8991F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(2.0F, 10.0F, 5.6F, 0.0F, 0.7854F, 0.0F));

        ModelPartData left_leg_spikes_r12 = left_leg_spikes6.addChild("left_leg_spikes_r12", ModelPartBuilder.create().uv(49, 9).cuboid(-4.3991F, -1.775F, -2.7577F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(2.0F, 10.0F, 5.6F, 0.0F, -0.7854F, 0.0F));

        ModelPartData left_leg_spikes7 = left_leg_spikes.addChild("left_leg_spikes7", ModelPartBuilder.create(), ModelTransform.of(4.2F, -2.7263F, -9.7631F, -1.5708F, 0.0F, 0.0F));

        ModelPartData left_leg_spikes_r13 = left_leg_spikes7.addChild("left_leg_spikes_r13", ModelPartBuilder.create().uv(49, 9).cuboid(1.2577F, -1.775F, -2.8991F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(2.0F, 10.0F, 2.6F, 0.0F, 0.7854F, 0.0F));

        ModelPartData left_leg_spikes_r14 = left_leg_spikes7.addChild("left_leg_spikes_r14", ModelPartBuilder.create().uv(49, 9).cuboid(-4.3991F, -1.775F, -2.7577F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(2.0F, 10.0F, 2.6F, 0.0F, -0.7854F, 0.0F));

        ModelPartData left_leg_spikes8 = left_leg_spikes.addChild("left_leg_spikes8", ModelPartBuilder.create(), ModelTransform.of(9.2F, -0.5263F, -16.3631F, -1.5708F, -1.5708F, 0.0F));

        ModelPartData left_leg_spikes_r15 = left_leg_spikes8.addChild("left_leg_spikes_r15", ModelPartBuilder.create().uv(49, 9).cuboid(1.3284F, -1.6F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 4.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData left_leg_spikes_r16 = left_leg_spikes8.addChild("left_leg_spikes_r16", ModelPartBuilder.create().uv(49, 9).cuboid(-4.3284F, -1.6F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 4.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData left_leg_spikes9 = left_leg_spikes.addChild("left_leg_spikes9", ModelPartBuilder.create(), ModelTransform.of(9.2F, -0.5263F, -16.3631F, -1.5708F, -1.5708F, 0.0F));

        ModelPartData left_leg_spikes_r17 = left_leg_spikes9.addChild("left_leg_spikes_r17", ModelPartBuilder.create().uv(49, 9).cuboid(1.3284F, -1.6F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData left_leg_spikes_r18 = left_leg_spikes9.addChild("left_leg_spikes_r18", ModelPartBuilder.create().uv(49, 9).cuboid(-4.3284F, -1.6F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData left_leg_spikes10 = left_leg_spikes.addChild("left_leg_spikes10", ModelPartBuilder.create(), ModelTransform.of(9.2F, -0.5263F, -16.3631F, -1.5708F, -1.5708F, 0.0F));

        ModelPartData left_leg_spikes_r19 = left_leg_spikes10.addChild("left_leg_spikes_r19", ModelPartBuilder.create().uv(49, 9).cuboid(1.3284F, -1.6F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, 0.0F, 2.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData left_leg_spikes_r20 = left_leg_spikes10.addChild("left_leg_spikes_r20", ModelPartBuilder.create().uv(49, 9).cuboid(-4.3284F, -1.6F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, 0.0F, 2.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData left_leg_spikes11 = left_leg_spikes.addChild("left_leg_spikes11", ModelPartBuilder.create(), ModelTransform.of(9.2F, -0.5263F, -16.3631F, -1.5708F, -1.5708F, 0.0F));

        ModelPartData left_leg_spikes_r21 = left_leg_spikes11.addChild("left_leg_spikes_r21", ModelPartBuilder.create().uv(49, 9).cuboid(1.3284F, -1.6F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(1.4F, -0.7F, -3.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData left_leg_spikes_r22 = left_leg_spikes11.addChild("left_leg_spikes_r22", ModelPartBuilder.create().uv(49, 9).cuboid(-4.3284F, -1.6F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(1.4F, -0.7F, -3.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData left_leg_spikes12 = left_leg_spikes.addChild("left_leg_spikes12", ModelPartBuilder.create(), ModelTransform.of(9.2F, -0.5263F, -16.3631F, -1.5708F, -1.5708F, 0.0F));

        ModelPartData left_leg_spikes_r23 = left_leg_spikes12.addChild("left_leg_spikes_r23", ModelPartBuilder.create().uv(49, 9).cuboid(1.3284F, -1.6F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.3F, -0.7F, -5.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData left_leg_spikes_r24 = left_leg_spikes12.addChild("left_leg_spikes_r24", ModelPartBuilder.create().uv(49, 9).cuboid(-4.3284F, -1.6F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.3F, -0.7F, -5.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData left_leg_spikes13 = left_leg_spikes.addChild("left_leg_spikes13", ModelPartBuilder.create(), ModelTransform.of(9.2F, -0.5263F, -16.3631F, -1.5708F, -1.5708F, 0.0F));

        ModelPartData left_leg_spikes_r25 = left_leg_spikes13.addChild("left_leg_spikes_r25", ModelPartBuilder.create().uv(49, 9).cuboid(1.3284F, -1.6F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(2.7F, -0.7F, -6.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData left_leg_spikes_r26 = left_leg_spikes13.addChild("left_leg_spikes_r26", ModelPartBuilder.create().uv(49, 9).cuboid(-4.3284F, -1.6F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(2.7F, -0.7F, -6.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData left_leg_spikes14 = left_leg_spikes.addChild("left_leg_spikes14", ModelPartBuilder.create(), ModelTransform.of(8.8F, -7.8263F, -9.7631F, -1.5708F, 0.0F, 0.0F));

        ModelPartData left_leg_spikes_r27 = left_leg_spikes14.addChild("left_leg_spikes_r27", ModelPartBuilder.create().uv(49, 1).cuboid(1.3284F, -1.75F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.8F, 0.5F, 4.9F, 0.0F, 0.7854F, 0.0F));

        ModelPartData left_leg_spikes_r28 = left_leg_spikes14.addChild("left_leg_spikes_r28", ModelPartBuilder.create().uv(49, 1).cuboid(-4.3284F, -1.75F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.8F, 0.5F, 4.9F, 0.0F, -0.7854F, 0.0F));

        ModelPartData left_leg_spikes15 = left_leg_spikes.addChild("left_leg_spikes15", ModelPartBuilder.create(), ModelTransform.of(8.8F, -7.8263F, -9.7631F, -1.5708F, 0.0F, 0.0F));

        ModelPartData left_leg_spikes_r29 = left_leg_spikes15.addChild("left_leg_spikes_r29", ModelPartBuilder.create().uv(49, 1).cuboid(1.3284F, -1.75F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.8F, 0.5F, 0.9F, 0.0F, 0.7854F, 0.0F));

        ModelPartData left_leg_spikes_r30 = left_leg_spikes15.addChild("left_leg_spikes_r30", ModelPartBuilder.create().uv(49, 1).cuboid(-4.3284F, -1.75F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.8F, 0.5F, 0.9F, 0.0F, -0.7854F, 0.0F));

        ModelPartData left_leg_spikes16 = left_leg_spikes.addChild("left_leg_spikes16", ModelPartBuilder.create(), ModelTransform.of(8.8F, -7.8263F, -9.7631F, -1.5708F, 0.0F, 0.0F));

        ModelPartData left_leg_spikes_r31 = left_leg_spikes16.addChild("left_leg_spikes_r31", ModelPartBuilder.create().uv(49, 1).cuboid(1.3284F, -1.75F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-5.8F, 0.5F, 4.9F, 0.0F, 0.7854F, 0.0F));

        ModelPartData left_leg_spikes_r32 = left_leg_spikes16.addChild("left_leg_spikes_r32", ModelPartBuilder.create().uv(49, 1).cuboid(-4.3284F, -1.75F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-5.8F, 0.5F, 4.9F, 0.0F, -0.7854F, 0.0F));

        ModelPartData left_leg_spikes17 = left_leg_spikes.addChild("left_leg_spikes17", ModelPartBuilder.create(), ModelTransform.of(8.8F, -7.8263F, -9.7631F, -1.5708F, 0.0F, 0.0F));

        ModelPartData left_leg_spikes_r33 = left_leg_spikes17.addChild("left_leg_spikes_r33", ModelPartBuilder.create().uv(49, 1).cuboid(1.3284F, -1.75F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-5.2F, 0.5F, 2.1F, 0.0F, 0.7854F, 0.0F));

        ModelPartData left_leg_spikes_r34 = left_leg_spikes17.addChild("left_leg_spikes_r34", ModelPartBuilder.create().uv(49, 1).cuboid(-4.3284F, -1.75F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-5.2F, 0.5F, 2.1F, 0.0F, -0.7854F, 0.0F));

        ModelPartData left_leg_spikes18 = left_leg_spikes.addChild("left_leg_spikes18", ModelPartBuilder.create(), ModelTransform.of(4.2F, -2.7263F, -9.7631F, -1.5708F, 0.0F, 0.0F));

        ModelPartData left_leg_spikes_r35 = left_leg_spikes18.addChild("left_leg_spikes_r35", ModelPartBuilder.create().uv(49, 1).cuboid(1.3284F, -1.75F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-1.2F, 0.0F, 5.1F, 0.0F, 0.7854F, 0.0F));

        ModelPartData left_leg_spikes_r36 = left_leg_spikes18.addChild("left_leg_spikes_r36", ModelPartBuilder.create().uv(49, 1).cuboid(-4.3284F, -1.75F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-1.2F, 0.0F, 5.1F, 0.0F, -0.7854F, 0.0F));

        ModelPartData left_leg_spikes19 = left_leg_spikes.addChild("left_leg_spikes19", ModelPartBuilder.create(), ModelTransform.of(8.8F, -7.8263F, -9.7631F, -1.5708F, 0.0F, 0.0F));

        ModelPartData left_leg_spikes_r37 = left_leg_spikes19.addChild("left_leg_spikes_r37", ModelPartBuilder.create().uv(49, 10).cuboid(1.3284F, -1.75F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-5.2F, 9.5F, 2.1F, 0.0F, 0.7854F, 0.0F));

        ModelPartData left_leg_spikes_r38 = left_leg_spikes19.addChild("left_leg_spikes_r38", ModelPartBuilder.create().uv(49, 10).cuboid(-4.3284F, -1.75F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-5.2F, 9.5F, 2.1F, 0.0F, -0.7854F, 0.0F));

        ModelPartData left_leg_spikes20 = left_leg_spikes.addChild("left_leg_spikes20", ModelPartBuilder.create(), ModelTransform.of(8.8F, -7.8263F, -9.7631F, -1.5708F, 0.0F, 0.0F));

        ModelPartData left_leg_spikes_r39 = left_leg_spikes20.addChild("left_leg_spikes_r39", ModelPartBuilder.create().uv(49, 10).cuboid(1.3284F, -1.75F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.8F, 9.5F, 0.9F, 0.0F, 0.7854F, 0.0F));

        ModelPartData left_leg_spikes_r40 = left_leg_spikes20.addChild("left_leg_spikes_r40", ModelPartBuilder.create().uv(49, 10).cuboid(-4.3284F, -1.75F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.8F, 9.5F, 0.9F, 0.0F, -0.7854F, 0.0F));

        ModelPartData left_leg_spikes21 = left_leg_spikes.addChild("left_leg_spikes21", ModelPartBuilder.create(), ModelTransform.of(8.8F, -7.8263F, -9.7631F, -1.5708F, 0.0F, 0.0F));

        ModelPartData left_leg_spikes_r41 = left_leg_spikes21.addChild("left_leg_spikes_r41", ModelPartBuilder.create().uv(49, 10).cuboid(1.3284F, -1.75F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.8F, 9.5F, 4.9F, 0.0F, 0.7854F, 0.0F));

        ModelPartData left_leg_spikes_r42 = left_leg_spikes21.addChild("left_leg_spikes_r42", ModelPartBuilder.create().uv(49, 10).cuboid(-4.3284F, -1.75F, -2.8284F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.8F, 9.5F, 4.9F, 0.0F, -0.7854F, 0.0F));

        ModelPartData left_leg_spikes22 = left_leg_spikes.addChild("left_leg_spikes22", ModelPartBuilder.create(), ModelTransform.of(1.2F, -0.5263F, -16.3631F, -1.5708F, 1.5708F, 0.0F));

        ModelPartData left_leg_spikes_r43 = left_leg_spikes22.addChild("left_leg_spikes_r43", ModelPartBuilder.create().uv(57, 11).cuboid(1.3284F, -0.6F, -2.8284F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 4.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData left_leg_spikes_r44 = left_leg_spikes22.addChild("left_leg_spikes_r44", ModelPartBuilder.create().uv(57, 11).cuboid(-4.3284F, -0.6F, -2.8284F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 4.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData left_leg_spikes23 = left_leg_spikes.addChild("left_leg_spikes23", ModelPartBuilder.create(), ModelTransform.of(1.2F, -0.5263F, -16.3631F, -1.5708F, 1.5708F, 0.0F));

        ModelPartData left_leg_spikes_r45 = left_leg_spikes23.addChild("left_leg_spikes_r45", ModelPartBuilder.create().uv(57, 11).cuboid(1.3284F, -0.6F, -2.8284F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-3.0F, 0.0F, 4.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData left_leg_spikes_r46 = left_leg_spikes23.addChild("left_leg_spikes_r46", ModelPartBuilder.create().uv(57, 11).cuboid(-4.3284F, -0.6F, -2.8284F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-3.0F, 0.0F, 4.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData left_leg_spikes24 = left_leg_spikes.addChild("left_leg_spikes24", ModelPartBuilder.create(), ModelTransform.of(1.2F, -0.5263F, -16.3631F, -1.5708F, 1.5708F, 0.0F));

        ModelPartData left_leg_spikes_r47 = left_leg_spikes24.addChild("left_leg_spikes_r47", ModelPartBuilder.create().uv(57, 11).cuboid(1.3284F, -0.6F, -2.8284F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, 0.0F, 1.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData left_leg_spikes_r48 = left_leg_spikes24.addChild("left_leg_spikes_r48", ModelPartBuilder.create().uv(57, 11).cuboid(-4.3284F, -0.6F, -2.8284F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, 0.0F, 1.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData left_leg_spikes25 = left_leg_spikes.addChild("left_leg_spikes25", ModelPartBuilder.create(), ModelTransform.of(1.2F, -0.3263F, -16.3631F, -1.5708F, 1.5708F, 0.0F));

        ModelPartData left_leg_spikes_r49 = left_leg_spikes25.addChild("left_leg_spikes_r49", ModelPartBuilder.create().uv(57, 11).cuboid(1.3284F, -0.6F, -2.8284F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-3.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData left_leg_spikes_r50 = left_leg_spikes25.addChild("left_leg_spikes_r50", ModelPartBuilder.create().uv(57, 11).cuboid(-4.3284F, -0.6F, -2.8284F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-3.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData left_leg_spikes26 = left_leg_spikes.addChild("left_leg_spikes26", ModelPartBuilder.create(), ModelTransform.of(1.5F, -3.6263F, -17.1631F, -1.5708F, 1.5708F, 0.0F));

        ModelPartData left_leg_spikes_r51 = left_leg_spikes26.addChild("left_leg_spikes_r51", ModelPartBuilder.create().uv(57, 11).cuboid(1.3284F, -0.6F, -2.8284F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-3.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData left_leg_spikes_r52 = left_leg_spikes26.addChild("left_leg_spikes_r52", ModelPartBuilder.create().uv(57, 11).cuboid(-4.3284F, -0.6F, -2.8284F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-3.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData left_leg_spikes27 = left_leg_spikes.addChild("left_leg_spikes27", ModelPartBuilder.create(), ModelTransform.of(1.5F, -3.3263F, -17.8631F, -1.5708F, 1.5708F, 0.0F));

        ModelPartData left_leg_spikes_r53 = left_leg_spikes27.addChild("left_leg_spikes_r53", ModelPartBuilder.create().uv(57, 11).cuboid(1.3284F, -0.6F, -2.8284F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, 0.0F, -3.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData left_leg_spikes_r54 = left_leg_spikes27.addChild("left_leg_spikes_r54", ModelPartBuilder.create().uv(57, 11).cuboid(-4.3284F, -0.6F, -2.8284F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, 0.0F, -3.0F, 0.0F, -0.7854F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }
}

