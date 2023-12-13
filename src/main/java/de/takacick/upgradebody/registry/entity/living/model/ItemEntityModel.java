package de.takacick.upgradebody.registry.entity.living.model;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;

import java.util.List;

public class ItemEntityModel<T extends Entity> extends EntityModel<T> {

    protected ItemStack itemStack;
    protected float pitch;
    protected float yaw;
    protected float scale;
    protected float y;
    protected float z;
    protected boolean modifyPitch = false;
    protected float offsetPitch = 0f;

    public ItemEntityModel(ItemStack itemStack, float pitch, float yaw, float y, float z, boolean modifyPitch, float scale) {
        super(RenderLayer::getEntityCutout);
        this.itemStack = itemStack;
        this.pitch = pitch;
        this.yaw = yaw;
        this.scale = scale;
        this.y = y;
        this.z = z;
        this.modifyPitch = modifyPitch;
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        if (this.modifyPitch) {
            this.offsetPitch = headPitch;
        } else {
            this.offsetPitch = 0f;
        }
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        matrices.translate(-0.5f, y, z);

        matrices.translate(0.5, 0.5, 0.5);
        matrices.scale(-1f * scale, -1f * scale, scale);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yaw));
        matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(pitch + this.offsetPitch));
        matrices.translate(-0.5, -0.5, -0.5);

        renderBakedItemQuads(matrices, vertices, MinecraftClient.getInstance().getItemRenderer().getModels().getModel(itemStack.getItem())
                .getQuads(null, null, Random.create()), itemStack, light, overlay);
    }

    protected void renderBakedItemQuads(MatrixStack matrices, VertexConsumer vertices, List<BakedQuad> quads, ItemStack stack, int light, int overlay) {
        boolean bl = !stack.isEmpty();
        MatrixStack.Entry entry = matrices.peek();
        for (BakedQuad bakedQuad : quads) {
            int i = -1;
            if (bl && bakedQuad.hasColor()) {
                ItemColorProvider itemColorProvider = ColorProviderRegistry.ITEM.get(stack.getItem());
                i = itemColorProvider != null ? itemColorProvider.getColor(stack, bakedQuad.getColorIndex()) : -1;
            }
            float f = (float) (i >> 16 & 0xFF) / 255.0f;
            float g = (float) (i >> 8 & 0xFF) / 255.0f;
            float h = (float) (i & 0xFF) / 255.0f;
            vertices.quad(entry, bakedQuad, f, g, h, light, overlay);
        }
    }
}
