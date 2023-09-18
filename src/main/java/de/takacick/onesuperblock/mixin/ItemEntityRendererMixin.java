package de.takacick.onesuperblock.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import de.takacick.onesuperblock.OneSuperBlock;
import de.takacick.onesuperblock.client.render.SpotlightRenderUtils;
import de.takacick.onesuperblock.client.render.SuperRenderLayers;
import de.takacick.onesuperblock.registry.item.Super;
import de.takacick.superitems.registry.item.SuperItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntityRenderer.class)
public abstract class ItemEntityRendererMixin {
    private static final Identifier BEAM_TEXTURE = new Identifier(OneSuperBlock.MOD_ID, "textures/entity/spotlight_beam.png");

    @Inject(method = "render(Lnet/minecraft/entity/ItemEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/BakedModel;getTransformation()Lnet/minecraft/client/render/model/json/ModelTransformation;", ordinal = 1, shift = At.Shift.BEFORE))
    public void render(ItemEntity itemEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo info) {
        if (itemEntity.getStack().getItem() instanceof SuperItem
                || itemEntity.getStack().getItem() instanceof Super
                || (itemEntity.getStack().getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof Super)) {
            float h = 0.25f;
            float n = 0.1f;
            float o = 0.1f;
            float q = -0.1f;
            float t = -0.1f;
            float w = -1.0f + h;
            float x = (float) 2 * 1 * (0.5f / 0.5f) + w;

            vertexConsumerProvider = MinecraftClient.getInstance().getBufferBuilders().getEffectVertexConsumers();
            RenderSystem.setShaderGameTime(itemEntity.age + itemEntity.getId() * 601, g);
            SpotlightRenderUtils.renderSpotlight(matrixStack, vertexConsumerProvider.getBuffer(SuperRenderLayers.RAINBOW_SPOTLIGHT.apply(BEAM_TEXTURE)),
                    1f, 1f, 1f, 0, 2.5f, 0.0f, n, o, 0.0f, q, 0.0f, 0.0f, t, 0.0f, 1.0f, x, w);
            MinecraftClient.getInstance().getBufferBuilders().getEffectVertexConsumers().drawCurrentLayer();
            RenderSystem.setShaderGameTime(itemEntity.getWorld().getTime(), g);
        }
    }
}

