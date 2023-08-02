package de.takacick.onedeathblock.client.renderer;

import de.takacick.onedeathblock.OneDeathBlock;
import de.takacick.onedeathblock.registry.EntityRegistry;
import de.takacick.onedeathblock.registry.ItemRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import org.jetbrains.annotations.Nullable;

public class HeadSpawnerRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    private static final Identifier TEXTURE = new Identifier(OneDeathBlock.MOD_ID, "textures/entity/head_spawner.png");
    private static final ModelPart root = getTexturedModelData().createModel();

    private final EntityRenderDispatcher dispatcher;
    private final ModelPart spawner;
    private final ModelPart helmet;
    private final ModelPart mob;

    public HeadSpawnerRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext) {
        super(featureRendererContext);
        this.dispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();

        ModelPart root = getTexturedModelData().createModel();
        this.spawner = root.getChild("spawner");
        this.helmet = root.getChild("helmet");
        this.mob = spawner.getChild("mob");
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity playerEntity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (!playerEntity.getEquippedStack(EquipmentSlot.HEAD).isOf(ItemRegistry.HEAD_SPAWNER)) {
            return;
        }

        this.helmet.copyTransform(getContextModel().head);
        this.helmet.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(TEXTURE)), light, OverlayTexture.DEFAULT_UV);

        this.spawner.copyTransform(getContextModel().head);
        this.spawner.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(TEXTURE)), light, OverlayTexture.DEFAULT_UV);

        this.spawner.rotate(matrices);
        this.mob.rotate(matrices);

        Entity entity = EntityRegistry.SKULLAGER.create(playerEntity.getWorld());
        if (entity != null) {
            float g = 0.53125f;
            float h = Math.max(entity.getWidth(), entity.getHeight());
            if ((double) h > 1.0) {
                g /= h;
            }

            matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));
            matrices.translate(0.0, 0.4f, 0.0);
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((float) MathHelper.lerp((double) tickDelta, playerEntity.age, playerEntity.age + 1) * 64.0f));
            matrices.translate(0.0, -0.2f, 0.0);
            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-30.0f));
            matrices.scale(g, g, g);
            this.dispatcher.render(entity, 0.0, 0.0, 0.0, 0.0f, tickDelta, matrices, vertexConsumers, light);
        }
    }

    public static void renderAsItem(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, @Nullable AbstractClientPlayerEntity playerEntity, float tickDelta) {
        ModelPart spawner = root.getChild("spawner");
        ModelPart helmet = root.getChild("helmet");
        ModelPart mob = spawner.getChild("mob");

        helmet.resetTransform();
        spawner.resetTransform();
        helmet.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(TEXTURE)), light, OverlayTexture.DEFAULT_UV);

        spawner.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(TEXTURE)), light, OverlayTexture.DEFAULT_UV);

        spawner.rotate(matrices);
        mob.rotate(matrices);

        float delta = playerEntity != null ? (float) MathHelper.lerp((double) tickDelta, playerEntity.age, playerEntity.age + 1) * 64.0f : 0f;

        Entity entity = EntityRegistry.SKULLAGER.create(playerEntity.getWorld());
        if (entity != null) {
            float g = 0.53125f;
            float h = Math.max(entity.getWidth(), entity.getHeight());
            if ((double) h > 1.0) {
                g /= h;
            }

            matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));
            matrices.translate(0.0, 0.4f, 0.0);
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(delta));
            matrices.translate(0.0, -0.2f, 0.0);
            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-30.0f));
            matrices.scale(g, g, g);
            MinecraftClient.getInstance().getEntityRenderDispatcher().render(entity, 0.0, 0.0, 0.0, 0.0f, tickDelta, matrices, vertexConsumers, light);
        }
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData spawner = modelPartData.addChild("spawner", ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -24.576F, -8.0F, 16.0F, 16.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -0.425F, 0.0F));

        ModelPartData mob = spawner.addChild("mob", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -10.0F, 0.0F));

        ModelPartData helmet = modelPartData.addChild("helmet", ModelPartBuilder.create().uv(0, 48).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(1.0F))
                .uv(32, 48).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(1.5F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }
}

