package de.takacick.immortalmobs.registry.entity.projectiles.renderer;

import de.takacick.immortalmobs.registry.entity.projectiles.ImmortalFireworkEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;

public class ImmortalFireworkEntityRenderer
        extends EntityRenderer<ImmortalFireworkEntity> {
    private final ItemRenderer itemRenderer;

    public ImmortalFireworkEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(ImmortalFireworkEntity immortalFireworkEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.multiply(this.dispatcher.getRotation());
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0f));
        if (immortalFireworkEntity.wasShotAtAngle()) {
            matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0f));
            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0f));
            matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90.0f));
        }
        this.itemRenderer.renderItem(immortalFireworkEntity.getStack(), ModelTransformation.Mode.GROUND, i, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumerProvider, immortalFireworkEntity.getId());
        matrixStack.pop();
        super.render(immortalFireworkEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(ImmortalFireworkEntity immortalFireworkEntity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }
}

