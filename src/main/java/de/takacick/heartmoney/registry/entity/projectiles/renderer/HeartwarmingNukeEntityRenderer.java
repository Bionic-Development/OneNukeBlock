package de.takacick.heartmoney.registry.entity.projectiles.renderer;

import de.takacick.heartmoney.registry.ItemRegistry;
import de.takacick.heartmoney.registry.entity.projectiles.HeartwarmingNukeEntity;
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

public class HeartwarmingNukeEntityRenderer extends EntityRenderer<HeartwarmingNukeEntity> {
    private final ItemRenderer itemRenderer;

    public HeartwarmingNukeEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.shadowRadius = 0f;
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(HeartwarmingNukeEntity heartwarmingNukeEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        if (heartwarmingNukeEntity.isFalling()) {
            matrixStack.scale(12, 12, 12);
            matrixStack.translate(0, 0.5, 0);
        } else {
            matrixStack.translate(0, 1.5, 0);
            matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));
        }
        this.itemRenderer.renderItem(ItemRegistry.HEARTWARMING_NUKE_ITEM.getDefaultStack(), ModelTransformation.Mode.NONE, i, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumerProvider, heartwarmingNukeEntity.getId());

        matrixStack.pop();
        super.render(heartwarmingNukeEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(HeartwarmingNukeEntity heartwarmingNukeEntity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }
}

