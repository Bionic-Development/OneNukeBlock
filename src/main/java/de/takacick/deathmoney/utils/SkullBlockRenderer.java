package de.takacick.deathmoney.utils;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.SkullBlockEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class SkullBlockRenderer {

    public static void renderSkull(@Nullable Direction direction, ItemStack stack, float yaw, float animationProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, SkullBlockEntityModel model, RenderLayer renderLayer) {
        matrices.push();
        if (direction == null) {
            matrices.translate(0.5, 0.0, 0.5);
        } else {
            float f = 0.25f;
            matrices.translate(0.5f - (float) direction.getOffsetX() * 0.25f, 0.25, 0.5f - (float) direction.getOffsetZ() * 0.25f);
        }
        matrices.scale(-1.0f, -1.0f, 1.0f);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);
        model.setHeadRotation(animationProgress, yaw, 0.0f);

        NbtCompound nbtCompound = stack.getOrCreateNbt();

        float red = 1f;
        float green = 1f;
        float blue = 1f;
        float alpha = nbtCompound.contains("alpha", NbtElement.FLOAT_TYPE) ? nbtCompound.getFloat("alpha") : 1f;

        if(nbtCompound.getInt("deathmoney") < 0) {
            green = 0.33333f;
            blue = 0.33333f;
        }

        model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, red, green, blue, alpha);
        matrices.pop();
    }
}
