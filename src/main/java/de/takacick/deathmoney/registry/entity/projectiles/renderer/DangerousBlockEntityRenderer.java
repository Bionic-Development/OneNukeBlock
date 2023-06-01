package de.takacick.deathmoney.registry.entity.projectiles.renderer;

import de.takacick.deathmoney.registry.entity.projectiles.DangerousBlockEntity;
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
public class DangerousBlockEntityRenderer extends EntityRenderer<DangerousBlockEntity> {

    private final ItemRenderer itemRenderer;

    public DangerousBlockEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    public void render(DangerousBlockEntity dangerousBlockEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();

        matrixStack.translate(0, 0.5, 0);
        matrixStack.scale(0.5f, 0.5f, 0.5f);
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.lerp(g, dangerousBlockEntity.prevYaw, dangerousBlockEntity.getYaw()) - 90.0F));
        matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.lerp(g, dangerousBlockEntity.prevPitch, dangerousBlockEntity.getPitch()) - 45.0F));
        matrixStack.multiply(Vec3f.NEGATIVE_Z.getRadialQuaternion(MathHelper.lerp(g, dangerousBlockEntity.age - 1 + dangerousBlockEntity.getId(), dangerousBlockEntity.age + dangerousBlockEntity.getId()) / 2));

        this.itemRenderer.renderItem(dangerousBlockEntity.getItemStack(), ModelTransformation.Mode.NONE, i, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumerProvider, dangerousBlockEntity.getId());
        matrixStack.pop();

        super.render(dangerousBlockEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Identifier getTexture(DangerousBlockEntity dangerousBlockEntity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }
}
