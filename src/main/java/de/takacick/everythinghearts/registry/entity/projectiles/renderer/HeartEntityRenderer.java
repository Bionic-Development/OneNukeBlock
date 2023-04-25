package de.takacick.everythinghearts.registry.entity.projectiles.renderer;

import de.takacick.everythinghearts.registry.ItemRegistry;
import de.takacick.everythinghearts.registry.entity.projectiles.HeartEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class HeartEntityRenderer extends EntityRenderer<HeartEntity> {

    private final ItemRenderer itemRenderer;

    public HeartEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    public void render(HeartEntity heartEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.translate(0, 0.5, 0);

        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.lerp(g, heartEntity.prevYaw, heartEntity.getYaw()) - 90.0F));
        matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.lerp(g, heartEntity.prevPitch, heartEntity.getPitch()) - 45.0F));
        matrixStack.multiply(Vec3f.NEGATIVE_Z.getRadialQuaternion(MathHelper.lerp(g, heartEntity.age - 1 + heartEntity.getId(), heartEntity.age + heartEntity.getId()) / 2));

        this.itemRenderer.renderItem(ItemRegistry.HEART.getDefaultStack(), ModelTransformation.Mode.NONE, i, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumerProvider, heartEntity.getId());
        matrixStack.pop();

        super.render(heartEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Identifier getTexture(HeartEntity heartEntity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }
}
