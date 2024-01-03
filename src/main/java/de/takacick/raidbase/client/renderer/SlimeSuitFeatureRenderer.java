package de.takacick.raidbase.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import de.takacick.raidbase.access.PlayerProperties;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

@Environment(value = EnvType.CLIENT)
public class SlimeSuitFeatureRenderer<T extends LivingEntity, M extends BipedEntityModel<T>>
        extends FeatureRenderer<T, M> {

    public static final VertexConsumerProvider.Immediate SLIME_SUIT = VertexConsumerProvider.immediate(new BufferBuilder(256));

    private static final Identifier TEXTURE = new Identifier("textures/entity/slime/slime.png");
    private final ModelPart slimeSuit;

    public SlimeSuitFeatureRenderer(FeatureRendererContext<T, M> context, EntityModelLoader loader) {
        super(context);
        this.slimeSuit = loader.getModelPart(EntityModelLayers.SLIME_OUTER);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T abstractClientPlayerEntity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (!(abstractClientPlayerEntity instanceof PlayerProperties playerProperties && playerProperties.hasSlimeSuit())) {
            return;
        }

        if (abstractClientPlayerEntity.isInvisible()) {
            return;
        }

        matrices.push();
        matrices.scale(5, 5, 5);
        matrices.translate(0, -1.15, 0);

        VertexConsumerProvider.Immediate vertexConsumerProvider = SLIME_SUIT;
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(this.getContextModel().getLayer(TEXTURE));
        this.slimeSuit.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
        matrices.pop();
    }
}