package de.takacick.tinyhouse.client.feature;

import de.takacick.tinyhouse.access.LivingProperties;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Items;

@Environment(value = EnvType.CLIENT)
public class IceFeetFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {

    private final HeldItemRenderer heldItemRenderer;

    public IceFeetFeatureRenderer(FeatureRendererContext<T, M> context, HeldItemRenderer heldItemRenderer) {
        super(context);
        this.heldItemRenderer = heldItemRenderer;
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (this.getContextModel() instanceof BipedEntityModel<?> bipedEntityModel) {
            if(entity instanceof LivingProperties livingProperties && livingProperties.hasFrozenBody()) {
                matrixStack.push();

                matrixStack.push();
                bipedEntityModel.rightLeg.rotate(matrixStack);
                matrixStack.translate(0, 0.6, 0);
                matrixStack.scale(0.4f, 0.4f, 0.4f);
                this.heldItemRenderer.renderItem(entity, Items.ICE.getDefaultStack(), ModelTransformationMode.NONE, false, matrixStack, vertexConsumers, light);
                matrixStack.pop();

                matrixStack.push();
                bipedEntityModel.leftLeg.rotate(matrixStack);
                matrixStack.translate(0, 0.6, 0);
                matrixStack.scale(0.4f, 0.4f, 0.4f);
                this.heldItemRenderer.renderItem(entity, Items.ICE.getDefaultStack(), ModelTransformationMode.NONE, false, matrixStack, vertexConsumers, light);
                matrixStack.pop();
                matrixStack.pop();
            }
        }
    }
}

