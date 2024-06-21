package de.takacick.onescaryblock.registry.block.entity.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import de.takacick.onescaryblock.OneScaryBlock;
import de.takacick.onescaryblock.client.item.model.ScaryOneBlockItemModel;
import de.takacick.onescaryblock.client.utils.CircleRenderer;
import de.takacick.onescaryblock.registry.EntityRegistry;
import de.takacick.onescaryblock.registry.ItemRegistry;
import de.takacick.onescaryblock.registry.block.entity.ScaryOneBlockBlockEntity;
import de.takacick.onescaryblock.registry.block.entity.model.ScaryBlockRitualEntityModel;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;

import java.util.List;

public class ScaryOneBlockBlockEntityRenderer implements BlockEntityRenderer<ScaryOneBlockBlockEntity> {

    private static final Identifier TEXTURE = new Identifier(OneScaryBlock.MOD_ID, "textures/entity/scary_block_ritual_circle.png");
    private static final List<EntityType<? extends LivingEntity>> entityTypes = List.of(EntityRegistry.ENTITY_303, EntityRegistry.HEROBRINE, EntityRegistry.BLOOD_MAN);

    private final ScaryOneBlockItemModel scaryLuckyBlockItemModel;
    private final ScaryBlockRitualEntityModel scaryBlockRitual;
    private final EntityRenderDispatcher entityRenderDispatcher;

    public ScaryOneBlockBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.scaryLuckyBlockItemModel = new ScaryOneBlockItemModel(ScaryOneBlockItemModel.getTexturedModelData().createModel());
        this.scaryBlockRitual = new ScaryBlockRitualEntityModel(ScaryBlockRitualEntityModel.getTexturedModelData().createModel());
        this.entityRenderDispatcher = ctx.getEntityRenderDispatcher();
    }

    @Override
    public void render(ScaryOneBlockBlockEntity scaryLuckyBlockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        ItemStack itemStack = ItemRegistry.SCARY_ONE_BLOCK_ITEM.getDefaultStack();

        this.scaryLuckyBlockItemModel.setAngles(
                null,
                itemStack,
                scaryLuckyBlockEntity.getAge(),
                tickDelta
        );

        if (scaryLuckyBlockEntity.isRitualRunning()) {
            this.scaryLuckyBlockItemModel.animateDying(
                    null,
                    itemStack,
                    scaryLuckyBlockEntity.getAge(),
                    tickDelta
            );
        }

        matrixStack.push();
        matrixStack.scale(-1f, -1f, 1f);
        matrixStack.translate(-0.5, -1.501f, 0.5);

        RenderLayer renderLayer = this.scaryLuckyBlockItemModel.getLayer(this.scaryLuckyBlockItemModel.getTexture(itemStack));

        this.scaryLuckyBlockItemModel.render(matrixStack,
                vertexConsumerProvider.getBuffer(renderLayer),
                i, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
        matrixStack.pop();

        matrixStack.push();
        if (scaryLuckyBlockEntity.isRitualRunning()) {
            Tessellator tessellator = RenderSystem.renderThreadTesselator();
            VertexConsumerProvider.Immediate consumerProvider = VertexConsumerProvider.immediate(tessellator.getBuffer());

            float progress = scaryLuckyBlockEntity.getRitualProgress(tickDelta);
            float time = scaryLuckyBlockEntity.getRitualTicks(tickDelta);

            matrixStack.translate(0.5f, 0.001f, 0.5f);
            matrixStack.push();
            matrixStack.scale(-1f, -1f, 1f);
            matrixStack.scale(15f, 1f, 15f);
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(time * 7));
            this.scaryBlockRitual.render(matrixStack, consumerProvider.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE)),
                    15728880, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
            matrixStack.pop();

            consumerProvider.draw();
            tessellator.getBuffer().clear();

            World world = scaryLuckyBlockEntity.getWorld();

            int index = 0;
            float degrees = 360f / entityTypes.size();

            float rotation = -time * (5 + 25 * progress);

            for (EntityType<? extends LivingEntity> entityType : entityTypes) {
                float angle = rotation + degrees * index;
                matrixStack.push();
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(angle));
                matrixStack.translate(-6.5, 0, 0);
                matrixStack.scale(1.5f, 1.5f, 1.5f);
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90));
                LivingEntity livingEntity = entityType.create(world);
                livingEntity.setYaw(-180f);
                livingEntity.prevYaw = -180f;
                this.entityRenderDispatcher.render(livingEntity, 0, 0, 0, 0f, tickDelta, matrixStack, vertexConsumerProvider, i);
                matrixStack.pop();

                index++;
            }

            matrixStack.push();
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(time * 5f));
            CircleRenderer.renderCylinder(matrixStack, 2.8f, 0.3f, 0xB20000, 1f, 360);
            CircleRenderer.renderCylinder(matrixStack, 4.2f, 0.3f, 0xB20000, 1f, 360);
            CircleRenderer.renderCylinder(matrixStack, 4.85f, 0.3f, 0xB20000, 1f, 360);
            CircleRenderer.renderCylinder(matrixStack, 7.3f, 1.6f, 0xB20000, 1f, 360);
            matrixStack.pop();
        }
        matrixStack.pop();

    }
}

