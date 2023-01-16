package de.takacick.imagineanything.registry.entity.living.model;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.random.Random;

import java.util.List;

public class ItemEntityModel<T extends MobEntity> extends EntityModel<T> {

    private ItemStack itemStack;
    private float pitch;
    private float yaw;
    private float scale;

    private float swingPitch = 0;

    public ItemEntityModel(ItemStack itemStack, float pitch) {
        this.itemStack = itemStack;
        this.pitch = pitch;
        this.yaw = 90f;
        this.scale = 1.7f;
    }

    public ItemEntityModel(ItemStack itemStack, float pitch, float yaw) {
        this.itemStack = itemStack;
        this.pitch = pitch;
        this.yaw = yaw;
        this.scale = 1.7f;
    }

    public ItemEntityModel(ItemStack itemStack, float pitch, float yaw, float scale) {
        this.itemStack = itemStack;
        this.pitch = pitch;
        this.yaw = yaw;
        this.scale = scale;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        if (itemStack.isOf(Items.DIAMOND_SWORD)) {
            matrices.translate(0, 0.2, 0);
        } else if (itemStack.isOf(Items.SHIELD)) {
            matrices.translate(0, 0.15, 0);
        } else {
            matrices.translate(0, 0.3, 0);
        }

        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(yaw));
        matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(pitch - swingPitch));

        matrices.scale(-1f * scale, -1f * scale, scale);


        matrices.translate(-0.5, -0.5, -0.5);
        renderBakedItemQuads(matrices, vertices, MinecraftClient.getInstance().getItemRenderer().getModels().getModel(itemStack.getItem())
                .getQuads(null, null, Random.create()), itemStack, light, overlay);
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        float f = this.handSwingProgress;
        this.swingPitch = MathHelper.sin(MathHelper.sqrt(f) * ((float) Math.PI * 2)) * 75f;
    }

    private void renderBakedItemQuads(MatrixStack matrices, VertexConsumer vertices, List<BakedQuad> quads, ItemStack stack, int light, int overlay) {
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
