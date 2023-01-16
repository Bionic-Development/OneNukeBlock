package de.takacick.imagineanything.registry.block.entity.renderer;

import de.takacick.imagineanything.registry.EntityRegistry;
import de.takacick.imagineanything.registry.ItemRegistry;
import de.takacick.imagineanything.registry.block.entity.TrappedBedBlockEntity;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.block.enums.BedPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.LightmapCoordinatesRetriever;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

public class TrappedBedBlockEntityRenderer
        implements BlockEntityRenderer<TrappedBedBlockEntity> {

    private final ItemRenderer itemRenderer;

    public TrappedBedBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
    }


    @Override
    public void render(TrappedBedBlockEntity bedBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        World world2 = bedBlockEntity.getWorld();
        if (world2 != null) {
            BlockState blockState = bedBlockEntity.getCachedState();
            DoubleBlockProperties.PropertySource<TrappedBedBlockEntity> propertySource = DoubleBlockProperties.toPropertySource(EntityRegistry.TRAPPED_BED_BLOCK, BedBlock::getBedPart, BedBlock::getOppositePartDirection, ChestBlock.FACING, blockState, world2, bedBlockEntity.getPos(), (world, pos) -> false);
            int k = ((Int2IntFunction) propertySource.apply(new LightmapCoordinatesRetriever())).get(i);
            if (blockState.get(BedBlock.PART).equals(BedPart.HEAD)) {
                matrixStack.translate(0.5, 0.5, 0.5);
                Direction direction = blockState.get(BedBlock.FACING);
                if (direction.equals(Direction.EAST) || direction.equals(Direction.WEST)) {
                    direction = direction.getOpposite();
                }

                this.renderPart(matrixStack, vertexConsumerProvider, direction, k);
            }
        } else {
            matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(-90.0f));

            this.renderPart(matrixStack, vertexConsumerProvider, Direction.SOUTH, i);
        }
    }

    private void renderPart(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Direction direction, int light) {
        matrices.push();
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(direction.asRotation()));
        this.itemRenderer.renderItem(ItemRegistry.IMAGINED_INFINITY_TRAPPED_BED_ITEM.getDefaultStack(), ModelTransformation.Mode.NONE, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, 0);
        matrices.pop();
    }
}

