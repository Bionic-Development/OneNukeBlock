package de.takacick.elementalblock.client.feature;

import de.takacick.elementalblock.OneElementalBlock;
import de.takacick.elementalblock.client.renderer.LavaBionicEntityRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;

@Environment(value = EnvType.CLIENT)
public class AnimatedModelFeatureRenderer
        extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    public static final SpriteIdentifier FLAME = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier(OneElementalBlock.MOD_ID, "entity/lava_bionic_flame"));

    private final LavaBionicEntityRenderer playerEntityRenderer;

    public AnimatedModelFeatureRenderer(LavaBionicEntityRenderer context) {
        super(context);
        this.playerEntityRenderer = context;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (entity.isInvisible()) {
            return;
        }

        matrices.push();
        VertexConsumer vertexConsumer = getVertexConsumer(vertexConsumers, RenderLayer.getEntityTranslucent(FLAME.getAtlasId()));
        int uv = LivingEntityRenderer.getOverlay(entity, playerEntityRenderer.getAnimationCounter(entity, tickDelta));
        this.getContextModel().render(matrices, vertexConsumer, light, uv, 1f, 1f, 1f, 1f);

        matrices.pop();
    }

    public static VertexConsumer getVertexConsumer(VertexConsumerProvider vertexConsumers, RenderLayer renderLayer) {
        return FLAME.getSprite().getTextureSpecificVertexConsumer(vertexConsumers.getBuffer(renderLayer));
    }

}

