package de.takacick.onescaryblock.client.item.model;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

public class Item303Model extends ItemModel {

    private final ModelPart root;

    public Item303Model() {
        super(RenderLayer::getEntityTranslucentCull);

        this.root = getTexturedModelData().createModel();
    }

    @Override
    public void setAngles(LivingEntity livingEntity, ItemStack itemStack, long time, float tickDelta) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
    }

    @Override
    public boolean shouldIgnoreItemTransforms(ItemStack itemStack) {
        return true;
    }

    @Override
    public void renderFeatures(@Nullable LivingEntity livingEntity, ModelTransformationMode modelTransformationMode, ItemStack itemStack, MatrixStack matrices, long time, float tickDelta, VertexConsumerProvider vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        matrices.push();
        matrices.translate(0.5, 0.5, 0.5);
        Random random = Random.create();

        Registries.ITEM.getRandom(random).ifPresent(itemReference -> {
            Item item = itemReference.value();

            MinecraftClient.getInstance().getItemRenderer().renderItem(item.getDefaultStack(),
                    ModelTransformationMode.values()[random.nextInt(ModelTransformationMode.values().length)],
                    light, overlay, matrices, vertices, null, 0);
        });

        matrices.pop();
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    @Override
    public Identifier getTexture(ItemStack itemStack) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        return TexturedModelData.of(modelData, 64, 64);
    }
}
