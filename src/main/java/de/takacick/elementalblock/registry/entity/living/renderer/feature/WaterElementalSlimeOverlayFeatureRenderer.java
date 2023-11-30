/*
 * Decompiled with CFR 0.2.1 (FabricMC 53fa44c9).
 */
package de.takacick.elementalblock.registry.entity.living.renderer.feature;

import de.takacick.elementalblock.registry.entity.living.model.WaterElementalSlimeEntityModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

@Environment(value = EnvType.CLIENT)
public class WaterElementalSlimeOverlayFeatureRenderer<T extends LivingEntity>
        extends FeatureRenderer<T, WaterElementalSlimeEntityModel<T>> {
    private final EntityModel<T> model;

    public WaterElementalSlimeOverlayFeatureRenderer(FeatureRendererContext<T, WaterElementalSlimeEntityModel<T>> context) {
        super(context);
        this.model = new WaterElementalSlimeEntityModel<>(WaterElementalSlimeEntityModel.getOuterTexturedModelData().createModel());
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        boolean bl = minecraftClient.hasOutline(livingEntity) && livingEntity.isInvisible();
        if (livingEntity.isInvisible() && !bl) {
            return;
        }
        VertexConsumer vertexConsumer = bl ? vertexConsumerProvider.getBuffer(RenderLayer.getOutline(this.getTexture(livingEntity))) : vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucent(this.getTexture(livingEntity)));
        this.getContextModel().copyStateTo(this.model);
        this.model.animateModel(livingEntity, f, g, h);
        this.model.setAngles(livingEntity, f, g, j, k, l);
        this.model.render(matrixStack, vertexConsumer, i, LivingEntityRenderer.getOverlay(livingEntity, 0.0f), 1.0f, 1.0f, 1.0f, 1.0f);
    }
}

