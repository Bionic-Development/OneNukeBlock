package de.takacick.onenukeblock.client.item.renderer;

import de.takacick.onenukeblock.OneNukeBlock;
import de.takacick.onenukeblock.client.item.model.KaboomMinerItemModel;
import de.takacick.utils.item.client.render.ItemModelRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.Nullable;

public class KaboomMinerItemRenderer extends ItemModelRenderer<KaboomMinerItemModel> {

    public static final Identifier TEXTURE = Identifier.of(OneNukeBlock.MOD_ID, "textures/entity/kaboom_miner.png");

    public KaboomMinerItemRenderer() {
        super(new KaboomMinerItemModel(KaboomMinerItemModel.getTexturedModelData().createModel()));
    }

    @Override
    public void render(ItemStack itemStack, @Nullable LivingEntity livingEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, ModelTransformationMode modelTransformationMode, int light, int overlay) {
        matrices.push();
        matrices.translate(0.5, 0.0, 0.5);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0F));
        matrices.translate(0.0F, -1.501F, 0.0F);
        long time = this.getWorldTime(itemStack, livingEntity, modelTransformationMode);
        Identifier texture = this.getTexture(itemStack, livingEntity, modelTransformationMode);
        RenderLayer renderLayer = this.getItemModel().getLayer(texture);
        this.getItemModel().setAngles(itemStack, livingEntity, modelTransformationMode, time, tickDelta);

        this.getItemModel().setTntVisible(false);
        this.getItemModel().render(matrices, vertexConsumers.getBuffer(renderLayer), light, overlay, -1);
        this.getItemModel().setTntVisible(true);

        matrices.push();

        float progress = time % 100;
        boolean flash = (int) progress / 5 % 2 == 0 && progress >= 35 && progress <= 65;
        int uv = flash ? OverlayTexture.packUv(OverlayTexture.getU(1.0f), 10) : overlay;

        this.getItemModel().renderTnt(matrices, vertexConsumers.getBuffer(renderLayer), light, uv, -1);
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
