package de.takacick.onegirlboyblock.client.item.renderer;

import de.takacick.onegirlboyblock.OneGirlBoyBlock;
import de.takacick.onegirlboyblock.client.item.model.InfernoHairDryerItemModel;
import de.takacick.utils.item.client.render.ItemModelRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.Nullable;

public class InfernoHairDryerItemRenderer extends ItemModelRenderer<InfernoHairDryerItemModel> {
    public static final Identifier TEXTURE = Identifier.of(OneGirlBoyBlock.MOD_ID, "textures/entity/inferno_hair_dryer.png");

    private final boolean handHeld;

    public InfernoHairDryerItemRenderer() {
        this(false);
    }

    public InfernoHairDryerItemRenderer(boolean handHeld) {
        super(new InfernoHairDryerItemModel((handHeld ? InfernoHairDryerItemModel.getThirdPersonTexturedModelData() : InfernoHairDryerItemModel.getTexturedModelData()).createModel()));
        this.handHeld = handHeld;
    }

    public void render(ItemStack itemStack, @Nullable LivingEntity livingEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, ModelTransformationMode modelTransformationMode, int light, int overlay) {
        if (!this.handHeld && (livingEntity != null && livingEntity.isUsingItem() && livingEntity.getActiveItem().equals(itemStack))) {
            if (!(modelTransformationMode.equals(ModelTransformationMode.GUI) || modelTransformationMode.equals(ModelTransformationMode.NONE)
                    || modelTransformationMode.equals(ModelTransformationMode.HEAD)
                    || modelTransformationMode.equals(ModelTransformationMode.GROUND)
                    || modelTransformationMode.equals(ModelTransformationMode.FIXED))) {
                return;
            }
        }

        matrices.push();

        if (!this.handHeld) {
            matrices.translate(0.5, 0.0, 0.5);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0F));
            matrices.translate(0.0F, -1.501F, 0.0F);
        }

        long time = this.getWorldTime(itemStack, livingEntity, modelTransformationMode);
        Identifier texture = this.getTexture(itemStack, livingEntity, modelTransformationMode);
        RenderLayer renderLayer = this.getItemModel().getLayer(texture);
        this.getItemModel().setAngles(itemStack, livingEntity, modelTransformationMode, time, tickDelta);
        this.getItemModel().render(matrices, vertexConsumers.getBuffer(renderLayer), light, overlay, -1);
        this.features.forEach((featureRenderer) -> {
            featureRenderer.render(itemStack, livingEntity, tickDelta, time, matrices, vertexConsumers, modelTransformationMode, light, overlay);
        });
        matrices.pop();
    }

    @Override
    public Identifier getTexture(ItemStack itemStack, @Nullable LivingEntity livingEntity, ModelTransformationMode modelTransformationMode) {
        return TEXTURE;
    }
}
