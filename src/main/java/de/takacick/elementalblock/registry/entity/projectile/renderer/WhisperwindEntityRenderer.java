package de.takacick.elementalblock.registry.entity.projectile.renderer;

import de.takacick.elementalblock.OneElementalBlock;
import de.takacick.elementalblock.registry.entity.projectile.WhisperwindEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class WhisperwindEntityRenderer extends EntityRenderer<WhisperwindEntity> {

    private static final Identifier TEXTURE = new Identifier(OneElementalBlock.MOD_ID, "textures/entity/whisperwind.png");
    private final ModelPart whisperwind;

    public WhisperwindEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.whisperwind = getTexturedModelData().createModel().getChild("sweep");
    }

    public void render(WhisperwindEntity whisperwindEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(g, whisperwindEntity.prevYaw, whisperwindEntity.getYaw())));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-MathHelper.lerp(g, whisperwindEntity.prevPitch, whisperwindEntity.getPitch())));

        matrixStack.scale(1.4f, 1.4f, 1.4f);
        matrixStack.translate(0.0, -1.501f, 0.0);

        this.whisperwind.render(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucentCull(getTexture(whisperwindEntity))), i, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 0.65f);
        matrixStack.pop();

        super.render(whisperwindEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Identifier getTexture(WhisperwindEntity whisperwindEntity) {
        return TEXTURE;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("sweep", ModelPartBuilder.create().uv(-6, 5).mirrored().cuboid(0.0F, 0.0F, -11.0F, 16.0F, 0.0F, 6.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-8.0F, 24.0F, 3.0F));
        return TexturedModelData.of(modelData, 32, 16);
    }

}
