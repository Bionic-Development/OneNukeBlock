package de.takacick.illegalwars.registry.block.entity.renderer;

import de.takacick.illegalwars.registry.block.entity.CyberWardenSecurityTrialSpawnerBlockEntity;
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

public class CyberWardenSecurityTrialSpawnerBlockEntityRenderer
        implements BlockEntityRenderer<CyberWardenSecurityTrialSpawnerBlockEntity> {
    private final EntityRenderDispatcher entityRenderDispatcher;

    public CyberWardenSecurityTrialSpawnerBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this.entityRenderDispatcher = context.getEntityRenderDispatcher();
    }

    @Override
    public void render(CyberWardenSecurityTrialSpawnerBlockEntity cyberWardenSecurityTrialSpawnerBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        World world = cyberWardenSecurityTrialSpawnerBlockEntity.getWorld();
        if (world == null) {
            return;
        }
        TrialSpawnerLogic trialSpawnerLogic = cyberWardenSecurityTrialSpawnerBlockEntity.getSpawner();
        TrialSpawnerData trialSpawnerData = trialSpawnerLogic.getData();
        Entity entity = trialSpawnerData.setDisplayEntity(trialSpawnerLogic, world, trialSpawnerLogic.getSpawnerState());
        if (entity != null) {
            MobSpawnerBlockEntityRenderer.render(f, matrixStack, vertexConsumerProvider, i, entity, this.entityRenderDispatcher, trialSpawnerData.getLastDisplayEntityRotation(), trialSpawnerData.getDisplayEntityRotation());
        }
    }
}

