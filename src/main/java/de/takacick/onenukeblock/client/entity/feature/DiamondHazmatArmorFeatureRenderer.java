package de.takacick.onenukeblock.client.entity.feature;

import de.takacick.onenukeblock.OneNukeBlock;
import de.takacick.onenukeblock.client.entity.model.DiamondHazmatArmorModel;
import de.takacick.onenukeblock.registry.item.DiamondHazmatArmorItem;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class DiamondHazmatArmorFeatureRenderer<T extends LivingEntity, M extends BipedEntityModel<T>> extends FeatureRenderer<T, M> {

    public static final Identifier TEXTURE = Identifier.of(OneNukeBlock.MOD_ID, "textures/entity/diamond_hazmat_armor.png");
    private final DiamondHazmatArmorModel<T> diamondHazmatArmorModel;

    public DiamondHazmatArmorFeatureRenderer(FeatureRendererContext<T, M> featureRendererContext) {
        this(featureRendererContext, new DiamondHazmatArmorModel<>(DiamondHazmatArmorModel.getTexturedModelData().createModel()));
    }

    public DiamondHazmatArmorFeatureRenderer(FeatureRendererContext<T, M> featureRendererContext, DiamondHazmatArmorModel<T> heavyCoreArmorModel) {
        super(featureRendererContext);
        this.diamondHazmatArmorModel = heavyCoreArmorModel;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        matrices.push();
        this.diamondHazmatArmorModel.copyFromBipedState(this.getContextModel());
        this.diamondHazmatArmorModel.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);

        this.renderArmor(matrices, vertexConsumers, entity, EquipmentSlot.CHEST, light);
        this.renderArmor(matrices, vertexConsumers, entity, EquipmentSlot.LEGS, light);
        this.renderArmor(matrices, vertexConsumers, entity, EquipmentSlot.FEET, light);
        this.renderArmor(matrices, vertexConsumers, entity, EquipmentSlot.HEAD, light);
        matrices.pop();
    }

    protected void setVisible(EquipmentSlot slot) {
        this.diamondHazmatArmorModel.setVisible(false);
        switch (slot) {
            case HEAD: {
                this.diamondHazmatArmorModel.head.visible = true;
                break;
            }
            case CHEST: {
                this.diamondHazmatArmorModel.body.visible = true;
                this.diamondHazmatArmorModel.rightArm.visible = true;
                this.diamondHazmatArmorModel.leftArm.visible = true;
                break;
            }
            case LEGS: {
                this.diamondHazmatArmorModel.rightBelt.visible = true;
                this.diamondHazmatArmorModel.leftBelt.visible = true;
                break;
            }
            case FEET: {
                this.diamondHazmatArmorModel.rightLeg.visible = true;
                this.diamondHazmatArmorModel.leftLeg.visible = true;
            }
        }
    }

    private void renderArmor(MatrixStack matrices, VertexConsumerProvider vertexConsumers, T entity, EquipmentSlot armorSlot, int light) {
        ItemStack itemStack = entity.getEquippedStack(armorSlot);
        Item item = itemStack.getItem();
        if (!(item instanceof DiamondHazmatArmorItem)) {
            return;
        }

        setVisible(armorSlot);

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(this.diamondHazmatArmorModel.getLayer(TEXTURE));
        this.diamondHazmatArmorModel.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);

        if (itemStack.hasGlint()) {
            this.diamondHazmatArmorModel.render(matrices, vertexConsumers.getBuffer(RenderLayer.getArmorEntityGlint()), light, OverlayTexture.DEFAULT_UV);
        }
    }

    public DiamondHazmatArmorModel<T> getDiamondHazmatArmorModel() {
        return this.diamondHazmatArmorModel;
    }
}

