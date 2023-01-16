package de.takacick.imagineanything.registry.entity.projectiles.renderer;

import de.takacick.imagineanything.registry.ItemRegistry;
import de.takacick.imagineanything.registry.entity.projectiles.GiantAxeMeteorEntity;
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
public class GiantAxeMeteorEntityRenderer extends EntityRenderer<GiantAxeMeteorEntity> {

    private final ItemRenderer itemRenderer;

    public GiantAxeMeteorEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    public void render(GiantAxeMeteorEntity giantAxeMeteorEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.translate(0, 0.5, 0);
        matrixStack.scale(3, 3, 3);

        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.lerp(g, giantAxeMeteorEntity.prevYaw, giantAxeMeteorEntity.getYaw()) - 90.0F));
        matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.lerp(g, giantAxeMeteorEntity.prevPitch, giantAxeMeteorEntity.getPitch()) - 45.0F));

        this.itemRenderer.renderItem(ItemRegistry.GIANT_AXE.getDefaultStack(), ModelTransformation.Mode.NONE, i, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumerProvider, giantAxeMeteorEntity.getId());
        matrixStack.pop();

        super.render(giantAxeMeteorEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Identifier getTexture(GiantAxeMeteorEntity giantAxeMeteorEntity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }
}
