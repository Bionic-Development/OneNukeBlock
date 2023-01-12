package de.takacick.immortalmobs.registry.block.entity.renderer;

import de.takacick.immortalmobs.ImmortalMobs;
import de.takacick.immortalmobs.client.CustomLayers;
import de.takacick.immortalmobs.registry.block.entity.ImmortalWoolBlockEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class ImmortalWoolBlockEntityRenderer implements BlockEntityRenderer<ImmortalWoolBlockEntity> {

    private static final Identifier TEXTURE = new Identifier(ImmortalMobs.MOD_ID, "textures/entity/immortal_wool.png");
    private final ModelPart root;
    private final ModelPart rootInner;

    public ImmortalWoolBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        root = getTexturedModelData().createModel();
        rootInner = getInnerTexturedModelData().createModel();
    }

    @Override
    public void render(ImmortalWoolBlockEntity immortalWoolBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        matrixStack.translate(0.5, -0.5, 0.5);
        rootInner.render(matrixStack, vertexConsumerProvider.getBuffer(CustomLayers.IMMORTAL_CUTOUT.apply(TEXTURE)), i,
                OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
        root.render(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getEyes(TEXTURE)), i,
                OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("block", ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    public static TexturedModelData getInnerTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("block", ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, new Dilation(-0.1F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }
}

