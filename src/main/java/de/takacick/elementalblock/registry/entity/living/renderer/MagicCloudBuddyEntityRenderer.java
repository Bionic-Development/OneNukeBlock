package de.takacick.elementalblock.registry.entity.living.renderer;

import de.takacick.elementalblock.registry.ItemRegistry;
import de.takacick.elementalblock.registry.entity.living.MagicCloudBuddyEntity;
import de.takacick.elementalblock.registry.entity.living.model.ItemEntityModel;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class MagicCloudBuddyEntityRenderer extends MobEntityRenderer<MagicCloudBuddyEntity, ItemEntityModel<MagicCloudBuddyEntity>> {

    public MagicCloudBuddyEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new ItemEntityModel<>(ItemRegistry.MAGIC_CLOUD_BUDDY.getDefaultStack(), 0f, 0f, 0f, -0.25f, false, 2.5f), 0f);
    }

    @Override
    protected int getBlockLight(MagicCloudBuddyEntity entity, BlockPos pos) {
        return 15;
    }

    @Override
    public void render(MagicCloudBuddyEntity mobEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Identifier getTexture(MagicCloudBuddyEntity magicCloudBuddyEntity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }
}
