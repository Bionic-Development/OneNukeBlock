package de.takacick.illegalwars.registry.block.entity.renderer;

import de.takacick.illegalwars.registry.block.entity.KingRatTrialSpawnerBlockEntity;
import net.minecraft.block.spawner.TrialSpawnerData;
import net.minecraft.block.spawner.TrialSpawnerLogic;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.MobSpawnerBlockEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class KingRatTrialSpawnerBlockEntityRenderer
        implements BlockEntityRenderer<KingRatTrialSpawnerBlockEntity> {
    private final EntityRenderDispatcher entityRenderDispatcher;

    public KingRatTrialSpawnerBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this.entityRenderDispatcher = context.getEntityRenderDispatcher();
    }

    @Override
    public void render(KingRatTrialSpawnerBlockEntity kingRatTrialSpawnerBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        World world = kingRatTrialSpawnerBlockEntity.getWorld();
        if (world == null) {
            return;
        }
        TrialSpawnerLogic trialSpawnerLogic = kingRatTrialSpawnerBlockEntity.getSpawner();
        TrialSpawnerData trialSpawnerData = trialSpawnerLogic.getData();
        Entity entity = trialSpawnerData.setDisplayEntity(trialSpawnerLogic, world, trialSpawnerLogic.getSpawnerState());
        if (entity != null) {
            MobSpawnerBlockEntityRenderer.render(f, matrixStack, vertexConsumerProvider, i, entity, this.entityRenderDispatcher, trialSpawnerData.getLastDisplayEntityRotation(), trialSpawnerData.getDisplayEntityRotation());
        }
    }
}

