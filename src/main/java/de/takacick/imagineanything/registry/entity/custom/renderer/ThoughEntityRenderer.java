package de.takacick.imagineanything.registry.entity.custom.renderer;

import de.takacick.imagineanything.registry.ItemRegistry;
import de.takacick.imagineanything.registry.entity.custom.ThoughtEntity;
import de.takacick.imagineanything.registry.entity.living.HeadEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class ThoughEntityRenderer extends EntityRenderer<ThoughtEntity> {

    private final ItemRenderer itemRenderer;

    public ThoughEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    public void render(ThoughtEntity thoughEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        if (thoughEntity.thinkingTime <= 0) {
            return;
        }

        Entity owner = thoughEntity.getOwnerEntity();
        if (owner instanceof HeadEntity headEntity) {
            matrixStack.push();

            thoughEntity.tickRiding();

            Quaternion quaternion = new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);
            quaternion.hamiltonProduct(Vec3f.POSITIVE_Y.getDegreesQuaternion(-this.dispatcher.camera.getYaw()));

            matrixStack.multiply(quaternion);
            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0f));
            matrixStack.translate(0.3, 0.5, 0);

            this.itemRenderer.renderItem(thoughEntity, ItemRegistry.THINK.getDefaultStack(),
                    ModelTransformation.Mode.NONE, false,
                    matrixStack, vertexConsumerProvider, thoughEntity.getEntityWorld(),
                    i, OverlayTexture.DEFAULT_UV, 0);

            if (ThoughtEntity.getThinkProgress(thoughEntity.thinkingTime) >= 0.8) {
                matrixStack.translate(0, 0.1, 0.05f);
                matrixStack.scale(0.3f, 0.3f, 0.3f);

                this.itemRenderer.renderItem(headEntity.getThoughtStack(),
                        ModelTransformation.Mode.NONE, i,
                        OverlayTexture.DEFAULT_UV,
                        matrixStack, vertexConsumerProvider,
                        thoughEntity.getId());
            }
            matrixStack.pop();
        }
    }

    public Identifier getTexture(ThoughtEntity thoughEntity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }
}
