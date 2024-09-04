package de.takacick.onenukeblock.client.item.renderer;

import de.takacick.onenukeblock.OneNukeBlock;
import de.takacick.onenukeblock.client.item.model.BangMaceItemModel;
import de.takacick.onenukeblock.utils.data.ItemDataComponents;
import de.takacick.utils.item.client.render.ItemModelRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.Nullable;

public class BangMaceItemRenderer extends ItemModelRenderer<BangMaceItemModel> {
    public static final Identifier TEXTURE = Identifier.of(OneNukeBlock.MOD_ID, "textures/entity/bang_mace.png");

    private final boolean handHeld;

    public BangMaceItemRenderer() {
        this(false);
    }

    public BangMaceItemRenderer(boolean handHeld) {
        super(new BangMaceItemModel((handHeld ? BangMaceItemModel.getThirdPersonTexturedModelData() : BangMaceItemModel.getTexturedModelData()).createModel()));
        this.handHeld = handHeld;
    }

    @Override
    public void render(ItemStack itemStack, @Nullable LivingEntity livingEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, ModelTransformationMode modelTransformationMode, int light, int overlay) {
        if (!this.handHeld && (livingEntity != null && livingEntity.isUsingItem() && livingEntity.getActiveItem().equals(itemStack))) {
            if (modelTransformationMode.equals(ModelTransformationMode.NONE) || modelTransformationMode.equals(ModelTransformationMode.THIRD_PERSON_RIGHT_HAND)
                    || modelTransformationMode.equals(ModelTransformationMode.THIRD_PERSON_LEFT_HAND)) {
                return;
            }
        }

        matrices.push();
        if (!this.handHeld) {
            matrices.translate(0.5, 0.0, 0.5);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0F));
            matrices.translate(0.0F, -1.501F, 0.0F);
        } else {
            matrices.scale(0.85f, 0.85f, 0.85f);
        }
        long time = this.getWorldTime(itemStack, livingEntity, modelTransformationMode);
        this.getItemModel().setAngles(itemStack, livingEntity, modelTransformationMode, time, tickDelta);

        Identifier texture = this.getTexture(itemStack, livingEntity, modelTransformationMode);
        RenderLayer renderLayer = this.getItemModel().getLayer(texture);

        this.getItemModel().setTntVisible(false);
        this.getItemModel().render(matrices, vertexConsumers.getBuffer(renderLayer), light, overlay, -1);
        this.getItemModel().setTntVisible(true);

        matrices.push();
        var animation = itemStack.get(ItemDataComponents.BANG_MACE);
        boolean flash;
        int uv = overlay;
        if (animation != null && animation.isFused()) {
            float progress = animation.getTick(tickDelta);
            if (progress + 1.0f < 10.0f) {
                float h = 1.0f - (progress + 1.0f) / 10.0f;
                h = MathHelper.clamp(h, 0.0f, 1.0f);
                h *= h;
                h *= h;
                float k = 1.0f + h * 0.3f;
                this.getItemModel().setTntScale(k, k, k);
            }

            flash = (int) progress / 5 % 2 == 0;
            uv = flash ? OverlayTexture.packUv(OverlayTexture.getU(1.0f), 10) : overlay;
        }

        this.getItemModel().renderTnt(matrices, vertexConsumers.getBuffer(renderLayer), light, uv, -1);
        this.getItemModel().setTntScale(1f, 1f, 1f);

        matrices.pop();

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
