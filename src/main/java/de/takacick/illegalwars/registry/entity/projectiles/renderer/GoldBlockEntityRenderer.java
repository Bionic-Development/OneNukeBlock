package de.takacick.illegalwars.registry.entity.projectiles.renderer;

import de.takacick.illegalwars.registry.entity.projectiles.GoldBlockEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class GoldBlockEntityRenderer
        extends EntityRenderer<GoldBlockEntity> {

    private final ItemRenderer itemRenderer;

    public GoldBlockEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(GoldBlockEntity goldBlockEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.scale(0.45f, 0.45f, 0.45f);
        matrixStack.translate(0, 0.5, 0);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerpAngleDegrees(g, goldBlockEntity.prevYaw, goldBlockEntity.getYaw()) + 180));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(MathHelper.lerp(g, goldBlockEntity.prevPitch, goldBlockEntity.getPitch()) - (goldBlockEntity.age + g) * 30));
        this.itemRenderer.renderItem(Items.GOLD_BLOCK.getDefaultStack(), ModelTransformationMode.NONE, i, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumerProvider, goldBlockEntity.getWorld(), goldBlockEntity.getId());
        matrixStack.pop();
        super.render(goldBlockEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(GoldBlockEntity goldBlockEntity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }
}

