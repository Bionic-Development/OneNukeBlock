package de.takacick.immortalmobs.registry.entity.living.renderer;

import de.takacick.immortalmobs.ImmortalMobs;
import de.takacick.immortalmobs.client.CustomLayers;
import de.takacick.immortalmobs.registry.entity.living.ImmortalSkeletonEntity;
import de.takacick.immortalmobs.registry.entity.living.model.ImmortalSkeletonEntityModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.*;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.SkeletonEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import org.jetbrains.annotations.Nullable;

public class ImmortalSkeletonEntityRenderer extends MobEntityRenderer<ImmortalSkeletonEntity, SkeletonEntityModel<ImmortalSkeletonEntity>> {
    private static final Identifier TEXTURE = new Identifier("textures/entity/skeleton/skeleton.png");
    private static final Identifier EYES = new Identifier(ImmortalMobs.MOD_ID, "textures/entity/immortal_skeleton_eyes.png");


    public ImmortalSkeletonEntityRenderer(EntityRendererFactory.Context context) {
        this(context, EntityModelLayers.SKELETON, EntityModelLayers.SKELETON_INNER_ARMOR, EntityModelLayers.SKELETON_OUTER_ARMOR);
    }

    public ImmortalSkeletonEntityRenderer(EntityRendererFactory.Context ctx, EntityModelLayer layer, EntityModelLayer legArmorLayer, EntityModelLayer bodyArmorLayer) {
        super(ctx, new SkeletonEntityModel(ctx.getPart(layer)), 0.5f);
        this.addFeature(new ArmorFeatureRenderer(this, new SkeletonEntityModel(ctx.getPart(legArmorLayer)), new SkeletonEntityModel(ctx.getPart(bodyArmorLayer))));
        this.addFeature(new ImmortalFeatureRenderer(this, ctx, layer));
        this.addFeature(new ImmortalEyesFeatureRenderer<>(this, EYES));
        this.addFeature(new HeadFeatureRenderer(this, ctx.getModelLoader(), 1.0f, 1.0f, 1.0f, ctx.getHeldItemRenderer()));
        this.addFeature(new ElytraFeatureRenderer(this, ctx.getModelLoader()));
        this.addFeature(new HeldItemFeatureRenderer(this, ctx.getHeldItemRenderer()));
    }

    @Override
    public Identifier getTexture(ImmortalSkeletonEntity immortalSkeletonEntity) {
        return TEXTURE;
    }

    @Nullable
    @Override
    protected RenderLayer getRenderLayer(ImmortalSkeletonEntity immortalSkeletonEntity, boolean showBody, boolean translucent, boolean showOutline) {
        return CustomLayers.IMMORTAL_CUTOUT.apply(getTexture(immortalSkeletonEntity));
    }

    @Environment(value = EnvType.CLIENT)
    public static class ImmortalFeatureRenderer
            extends FeatureRenderer<ImmortalSkeletonEntity, SkeletonEntityModel<ImmortalSkeletonEntity>> {

        private final SkeletonEntityModel<ImmortalSkeletonEntity> outerModel;
        private static final Identifier TEXTURE = new Identifier(ImmortalMobs.MOD_ID, "textures/entity/immortal_skeleton.png");

        public ImmortalFeatureRenderer(FeatureRendererContext<ImmortalSkeletonEntity, SkeletonEntityModel<ImmortalSkeletonEntity>> context, EntityRendererFactory.Context ctx, EntityModelLayer layer) {
            super(context);
            this.outerModel = new SkeletonEntityModel(ImmortalSkeletonEntityModel.getOuterTexturedModelData().createModel());
        }

        @Override
        public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, ImmortalSkeletonEntity entity, float limbAngle, float limbDistance, float tickDelta, float age, float headYaw, float headPitch) {
            getContextModel().copyStateTo(outerModel);

            float rotation = MathHelper.lerp(tickDelta, (entity.age - 1) / 10f, entity.age / 10f);

            outerModel.body.getChild("heart").resetTransform();
            outerModel.body.getChild("heart").rotate(new Vec3f(rotation, rotation, rotation));
            outerModel.animateModel(entity, limbAngle, limbDistance, tickDelta);
            outerModel.setAngles(entity, limbAngle, limbDistance, age, headYaw, headPitch);

            float[] hs = SheepEntity.getRgbColor(DyeColor.PURPLE);
            float s = hs[0];
            float t = hs[1];
            float u = hs[2];

            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(CustomLayers.IMMORTAL_CUTOUT.apply(TEXTURE));
            outerModel.render(matrixStack, vertexConsumer, i, LivingEntityRenderer.getOverlay(entity, 0.0f), s, t, u, 1.0f);
            outerModel.render(matrixStack, vertexConsumer, i, LivingEntityRenderer.getOverlay(entity, 0.0f), s, t, u, 1.0f);

            vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEyes(TEXTURE));
            outerModel.render(matrixStack, vertexConsumer, i, LivingEntityRenderer.getOverlay(entity, 0.0f), s, t, u, 1.0f);
            outerModel.render(matrixStack, vertexConsumer, i, LivingEntityRenderer.getOverlay(entity, 0.0f), s, t, u, 1.0f);
        }
    }
}

