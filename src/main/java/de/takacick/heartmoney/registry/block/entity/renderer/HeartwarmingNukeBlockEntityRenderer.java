package de.takacick.heartmoney.registry.block.entity.renderer;

import de.takacick.heartmoney.registry.ItemRegistry;
import de.takacick.heartmoney.registry.block.entity.HeartwarmingNukeBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.random.Random;

public class HeartwarmingNukeBlockEntityRenderer implements BlockEntityRenderer<HeartwarmingNukeBlockEntity> {

    private final ItemRenderer itemRenderer;

    public HeartwarmingNukeBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(HeartwarmingNukeBlockEntity heartwarmingNukeBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        matrixStack.translate(0.5, 1.5, 0.5);
        matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));

        BakedModel bakedModel = this.itemRenderer.getModel(ItemRegistry.HEARTWARMING_NUKE_ITEM.getDefaultStack(), heartwarmingNukeBlockEntity.getWorld(), null, Random.createLocal().nextInt());
        itemRenderer.renderItem(ItemRegistry.HEARTWARMING_NUKE_ITEM.getDefaultStack(), ModelTransformation.Mode.NONE, false, matrixStack, vertexConsumerProvider, i, j, bakedModel);
    }

    @Override
    public int getRenderDistance() {
        return 256;
    }
}

