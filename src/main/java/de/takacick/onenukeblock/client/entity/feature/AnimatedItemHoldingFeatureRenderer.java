package de.takacick.onenukeblock.client.entity.feature;

import de.takacick.onenukeblock.access.PlayerModelProperties;
import de.takacick.onenukeblock.client.item.renderer.BangMaceItemRenderer;
import de.takacick.onenukeblock.registry.ItemRegistry;
import de.takacick.onenukeblock.registry.item.HandheldItem;
import de.takacick.onenukeblock.utils.ArmHelper;
import de.takacick.utils.item.client.render.ItemRenderer;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;

public class AnimatedItemHoldingFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    @Unique
    private final HashMap<Item, ItemRenderer> itemModels = new HashMap<>();

    public AnimatedItemHoldingFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext) {
        super(featureRendererContext);

        this.itemModels.put(ItemRegistry.BANG_MACE, new BangMaceItemRenderer(true));
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, AbstractClientPlayerEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        for (Hand hand : Hand.values()) {
            ItemStack itemStack = entity.getStackInHand(hand);
            Arm arm = ArmHelper.getArm(entity, hand);
            if (!(itemStack.getItem() instanceof HandheldItem handheldItem && handheldItem.shouldRender(entity, itemStack, arm))) {
                continue;
            }

            ItemRenderer itemRenderer = itemModels.get(itemStack.getItem());

            if (itemRenderer == null) {
                continue;
            }

            matrixStack.push();
            if (this.getContextModel() instanceof PlayerModelProperties playerModelProperties) {
                playerModelProperties.getAnimationBodyModel().getPart().getChild("bone").rotate(matrixStack);
                matrixStack.translate(0, -1.5f, 0);
            }

            boolean leftHand = ArmHelper.getArmStack(entity, Arm.LEFT).equals(itemStack);

            matrixStack.push();
            ModelPart mainHand = leftHand ? this.getContextModel().leftArm : this.getContextModel().rightArm;
            mainHand.rotate(matrixStack);

            if (getContextModel() instanceof PlayerModelProperties playerModelProperties) {
                playerModelProperties.getAnimationBodyModel().rotateItem(matrixStack, !leftHand);
            }

            matrixStack.push();
            matrixStack.pop();

            itemRenderer.render(itemStack, entity, tickDelta, matrixStack, vertexConsumerProvider, leftHand ? ModelTransformationMode.FIRST_PERSON_LEFT_HAND : ModelTransformationMode.FIRST_PERSON_RIGHT_HAND, light, OverlayTexture.DEFAULT_UV);

            matrixStack.pop();

            matrixStack.pop();
        }
    }
}

