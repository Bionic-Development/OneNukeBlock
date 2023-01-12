package de.takacick.immortalmobs.registry.entity.custom.renderer;

import de.takacick.immortalmobs.registry.entity.custom.BlackHoleEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

public class BlackHoleEntityRenderer extends EntityRenderer<BlackHoleEntity> {

    private final ModelPart root;

    public BlackHoleEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        root = getTexturedModelData().createModel();
    }

    @Override
    public void render(BlackHoleEntity blackHoleEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.translate(0, -1, 0);
        float size = Math.min(blackHoleEntity.age, 80) / 100f + 1;
        matrixStack.scale(size, size, size);
        float rotation = MathHelper.lerp(g, (blackHoleEntity.age - 1) / 10f, blackHoleEntity.age / 10f);
        root.getChild("block").resetTransform();
        root.getChild("block").rotate(new Vec3f(rotation, rotation, rotation));
        root.getChild("block").render(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getEndPortal()), i,
                OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
        matrixStack.pop();
        super.render(blackHoleEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(BlackHoleEntity blackHoleEntity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("block", ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 16.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }
}

