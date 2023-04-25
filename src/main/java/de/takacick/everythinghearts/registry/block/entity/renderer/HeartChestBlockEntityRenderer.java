package de.takacick.everythinghearts.registry.block.entity.renderer;

import de.takacick.everythinghearts.EverythingHearts;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.block.*;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.LightmapCoordinatesRetriever;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

public class HeartChestBlockEntityRenderer<T extends ChestBlockEntity>
        implements BlockEntityRenderer<T> {

    private static final Identifier HEART_CHEST = new Identifier(EverythingHearts.MOD_ID, "textures/entity/heart_chest.png");

    private static final String BASE = "base";
    private static final String LID = "lid";
    private static final String LATCH = "lock";
    private final ModelPart singleChestLid;
    private final ModelPart singleChestBase;
    private final ModelPart singleChestLatch;

    public HeartChestBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        ModelPart modelPart = getTexturedModelData().createModel();
        this.singleChestBase = modelPart.getChild(BASE);
        this.singleChestLid = modelPart.getChild(LID);
        this.singleChestLatch = modelPart.getChild(LATCH);
    }

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        World world = entity.getWorld();
        boolean bl = world != null;
        BlockState blockState = bl ? entity.getCachedState() : Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, Direction.SOUTH);
        ChestType chestType = blockState.contains(ChestBlock.CHEST_TYPE) ? blockState.get(ChestBlock.CHEST_TYPE) : ChestType.SINGLE;
        Block block = blockState.getBlock();
        if (!(block instanceof AbstractChestBlock abstractChestBlock)) {
            return;
        }
        boolean bl2 = chestType != ChestType.SINGLE;
        matrices.push();
        float f = blockState.get(ChestBlock.FACING).asRotation();
        matrices.translate(0.5, -0.5, 0.5);
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-f));

        DoubleBlockProperties.PropertySource<ChestBlockEntity> propertySource = bl ? abstractChestBlock.getBlockEntitySource(blockState, world, entity.getPos(), true) : DoubleBlockProperties.PropertyRetriever::getFallback;
        float g = propertySource.apply(ChestBlock.getAnimationProgressRetriever(entity)).get(tickDelta);
        g = 1.0f - g;
        g = 1.0f - g * g * g;
        int i = ((Int2IntFunction) propertySource.apply(new LightmapCoordinatesRetriever())).applyAsInt(light);

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(HEART_CHEST));

        this.render(matrices, vertexConsumer, this.singleChestLid, this.singleChestLatch, this.singleChestBase, g, i, overlay);

        matrices.pop();
    }

    private void render(MatrixStack matrices, VertexConsumer vertices, ModelPart lid, ModelPart latch, ModelPart base, float openFactor, int light, int overlay) {
        latch.pitch = lid.pitch = -(openFactor * 1.5707964f);
        lid.render(matrices, vertices, light, overlay);
        latch.render(matrices, vertices, light, overlay);
        base.render(matrices, vertices, light, overlay);
    }

    private static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData base = modelPartData.addChild("base", ModelPartBuilder.create().uv(0, 19).cuboid(-7.0F, -16.0F, -7.0F, 14.0F, 10.0F, 14.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData lid = modelPartData.addChild("lid", ModelPartBuilder.create().uv(0, 0).cuboid(-7.0F, 0.0F, 0.0F, 14.0F, 5.0F, 14.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 17.0F, -7.0F));

        ModelPartData lock = modelPartData.addChild("lock", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 17.0F, -7.0F));

        ModelPartData heart = lock.addChild("heart", ModelPartBuilder.create().uv(0, 0).cuboid(1.5F, -2.9412F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(0.5F, -1.9412F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-0.5F, -1.9412F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-1.5F, -1.9412F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-2.5F, -1.9412F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(0.5F, -0.9412F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(1.5F, -0.9412F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-0.5F, -0.9412F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-1.5F, -0.9412F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-2.5F, -0.9412F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(0.5F, 0.0588F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(1.5F, 0.0588F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 6).cuboid(1.5F, 1.0588F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-0.5F, 0.0588F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(0.5F, 1.0588F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-1.5F, 1.0588F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-0.5F, 1.0588F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-0.5F, 2.0588F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 6).cuboid(0.5F, 2.0588F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 6).cuboid(-0.5F, 3.0588F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-1.5F, 0.0588F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-2.5F, 0.0588F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 6).cuboid(-1.5F, 2.0588F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 6).cuboid(-3.5F, 0.0588F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 6).cuboid(-2.5F, 1.0588F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 3).cuboid(1.5F, -1.9412F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(2.5F, -1.9412F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(2.5F, -0.9412F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 6).cuboid(2.5F, 0.0588F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-2.5F, -2.9412F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-3.5F, -1.9412F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-3.5F, -0.9412F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-1.5F, -2.9412F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(0.5F, -2.9412F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.4412F, 14.5F, 3.1416F, 0.0F, 0.0F));

        ModelPartData chest = modelPartData.addChild("chest", ModelPartBuilder.create(), ModelTransform.of(0.0F, 16.0F, 0.0F, 0.0F, 0.0F, -3.1416F));
        return TexturedModelData.of(modelData, 64, 64);
    }
}

