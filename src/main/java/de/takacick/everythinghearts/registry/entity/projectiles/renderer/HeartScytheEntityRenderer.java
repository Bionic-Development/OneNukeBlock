package de.takacick.everythinghearts.registry.entity.projectiles.renderer;

import de.takacick.everythinghearts.registry.ItemRegistry;
import de.takacick.everythinghearts.registry.entity.projectiles.HeartScytheEntity;
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
public class HeartScytheEntityRenderer extends EntityRenderer<HeartScytheEntity> {

    private final ItemRenderer itemRenderer;

    public HeartScytheEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    public void render(HeartScytheEntity heartScytheEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.translate(0, 0.5, 0);

        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.lerp(g, heartScytheEntity.prevYaw, heartScytheEntity.getYaw()) + 90));
        matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.lerp(g, heartScytheEntity.prevPitch, heartScytheEntity.getPitch()) + 45));
        matrixStack.multiply(Vec3f.POSITIVE_Z.getRadialQuaternion(MathHelper.lerp(g, heartScytheEntity.age - 1 + heartScytheEntity.getId(), heartScytheEntity.age + heartScytheEntity.getId()) / 2));

        this.itemRenderer.renderItem(ItemRegistry.HEART_SCYTHE.getDefaultStack(), ModelTransformation.Mode.NONE, i, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumerProvider, heartScytheEntity.getId());
        matrixStack.pop();

        super.render(heartScytheEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Identifier getTexture(HeartScytheEntity heartScytheEntity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }
}
