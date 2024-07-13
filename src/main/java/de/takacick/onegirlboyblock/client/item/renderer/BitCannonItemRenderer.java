package de.takacick.onegirlboyblock.client.item.renderer;

import de.takacick.onegirlboyblock.OneGirlBoyBlock;
import de.takacick.onegirlboyblock.client.item.model.BitCannonItemModel;
import de.takacick.onegirlboyblock.client.shader.OneGirlBoyBlockLayers;
import de.takacick.onegirlboyblock.client.utils.SpotlightRenderUtils;
import de.takacick.onegirlboyblock.utils.data.ItemDataComponents;
import de.takacick.onegirlboyblock.utils.data.item.BitCannonItemHelper;
import de.takacick.utils.item.client.render.ItemModelRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class BitCannonItemRenderer extends ItemModelRenderer<BitCannonItemModel> {
    public static final Identifier TEXTURE = Identifier.of(OneGirlBoyBlock.MOD_ID, "textures/entity/8_bit_cannon.png");
    public static final Identifier TEXTURE_GLOW = Identifier.of(OneGirlBoyBlock.MOD_ID, "textures/entity/8_bit_cannon_glow.png");

    private final boolean handHeld;

    public BitCannonItemRenderer() {
        this(false);
    }

    public BitCannonItemRenderer(boolean handHeld) {
        super(new BitCannonItemModel((handHeld ? BitCannonItemModel.getThirdPersonTexturedModelData() : BitCannonItemModel.getTexturedModelData()).createModel()));
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

        BitCannonItemHelper itemAnimationHelper = itemStack.get(ItemDataComponents.BIT_CANNON_HELPER);
        if (itemAnimationHelper != null) {
            float alpha = itemAnimationHelper.getProgress(tickDelta);
            if (alpha > 0f) {
                float width = 0.089f;
                float v = -1.0f + width;
                float u = 2f * 1f * time + v;

                matrices.push();
                this.getItemModel().rotateCannon(matrices);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(45f));
                Vector3f vector3f = Vec3d.unpackRgb(itemAnimationHelper.getVariant().getColor()).toVector3f();
                SpotlightRenderUtils.renderSpotlight(matrices, vertexConsumers.getBuffer(RenderLayer.getBeaconBeam(SpotlightRenderUtils.SPOTLIGHT_TEXTURE, true)),
                        vector3f.x(), vector3f.y(), vector3f.z(), alpha, 0, modelTransformationMode.isFirstPerson() ? 0.5f : 0.25f, 0.0f, width, width, 0.0f, -width, 0.0f, 0.0f, -width, 0.0f, 1.0f, u, v);
                matrices.pop();
                int alphaInt = (int) (alpha * 255);
                int color = (alphaInt << 24) | itemAnimationHelper.getVariant().getColor() ;

                this.getItemModel().render(matrices, vertexConsumers.getBuffer(OneGirlBoyBlockLayers.getBitCannonGlow(TEXTURE_GLOW)), light, overlay, color);
            }
        }

        matrices.pop();
    }

    @Override
    public Identifier getTexture(ItemStack itemStack, @Nullable LivingEntity livingEntity, ModelTransformationMode modelTransformationMode) {
        return TEXTURE;
    }
}
