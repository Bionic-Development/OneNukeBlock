package de.takacick.heartmoney.registry.entity.projectiles.renderer;

import de.takacick.heartmoney.registry.ItemRegistry;
import de.takacick.heartmoney.registry.entity.projectiles.LifeStealScytheEntity;
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
public class LifeStealScytheEntityRenderer extends EntityRenderer<LifeStealScytheEntity> {

    private final ItemRenderer itemRenderer;

    public LifeStealScytheEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    public void render(LifeStealScytheEntity lifeStealScytheEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.translate(0, 0.5, 0);

        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.lerp(g, lifeStealScytheEntity.prevYaw, lifeStealScytheEntity.getYaw()) + 90));
        matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.lerp(g, lifeStealScytheEntity.prevPitch, lifeStealScytheEntity.getPitch()) + 45));
        matrixStack.multiply(Vec3f.POSITIVE_Z.getRadialQuaternion(MathHelper.lerp(g, lifeStealScytheEntity.age - 1 + lifeStealScytheEntity.getId(), lifeStealScytheEntity.age + lifeStealScytheEntity.getId()) / 2));

        this.itemRenderer.renderItem(ItemRegistry.LIFE_STEAL_SCYTHE.getDefaultStack(), ModelTransformation.Mode.NONE, i, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumerProvider, lifeStealScytheEntity.getId());
        matrixStack.pop();

        super.render(lifeStealScytheEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Identifier getTexture(LifeStealScytheEntity lifeStealScytheEntity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }
}
