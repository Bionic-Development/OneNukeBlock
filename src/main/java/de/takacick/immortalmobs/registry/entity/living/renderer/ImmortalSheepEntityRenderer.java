package de.takacick.immortalmobs.registry.entity.living.renderer;

import de.takacick.immortalmobs.ImmortalMobs;
import de.takacick.immortalmobs.client.CustomLayers;
import de.takacick.immortalmobs.registry.entity.living.ImmortalSheepEntity;
import de.takacick.immortalmobs.registry.entity.living.model.ImmortalSheepEntityModel;
import de.takacick.immortalmobs.registry.entity.living.model.ImmortalSheepWoolEntityModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class ImmortalSheepEntityRenderer extends MobEntityRenderer<ImmortalSheepEntity, ImmortalSheepEntityModel<ImmortalSheepEntity>> {
    private static final Identifier TEXTURE = new Identifier("textures/entity/sheep/sheep.png");
    private static final Identifier EYES = new Identifier(ImmortalMobs.MOD_ID, "textures/entity/immortal_sheep_eyes.png");

    public ImmortalSheepEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new ImmortalSheepEntityModel(context.getPart(EntityModelLayers.SHEEP)), 0.7f);
        this.addFeature(new SheepWoolFeatureRenderer(this, context.getModelLoader()));
        this.addFeature(new ImmortalEyesFeatureRenderer<>(this, EYES));
    }

    @Nullable
    @Override
    protected RenderLayer getRenderLayer(ImmortalSheepEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return CustomLayers.IMMORTAL_CUTOUT.apply(getTexture(entity));
    }

    @Override
    public Identifier getTexture(ImmortalSheepEntity sheepEntity) {
        return TEXTURE;
    }

    @Environment(value = EnvType.CLIENT)
    public static class SheepWoolFeatureRenderer
            extends FeatureRenderer<ImmortalSheepEntity, ImmortalSheepEntityModel<ImmortalSheepEntity>> {
        private static final Identifier SKIN = new Identifier("textures/entity/sheep/sheep_fur.png");
        private final ImmortalSheepWoolEntityModel<ImmortalSheepEntity> model;
        private final ImmortalSheepWoolEntityModel<ImmortalSheepEntity> innerModel;

        public SheepWoolFeatureRenderer(FeatureRendererContext<ImmortalSheepEntity, ImmortalSheepEntityModel<ImmortalSheepEntity>> context, EntityModelLoader loader) {
            super(context);
            this.model = new ImmortalSheepWoolEntityModel(loader.getModelPart(EntityModelLayers.SHEEP_FUR));
            this.innerModel = new ImmortalSheepWoolEntityModel(ImmortalSheepWoolEntityModel.getTexturedModelData().createModel());
        }

        @Override
        public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, ImmortalSheepEntity sheepEntity, float f, float g, float h, float j, float k, float l) {
            float u;
            float t;
            float s;
            if (sheepEntity.isSheared()) {
                return;
            }
            if (sheepEntity.isInvisible()) {
                MinecraftClient minecraftClient = MinecraftClient.getInstance();
                boolean bl = minecraftClient.hasOutline(sheepEntity);
                if (bl) {
                    this.getContextModel().copyStateTo(this.model);
                    this.getContextModel().copyStateTo(this.innerModel);
                    this.model.animateModel(sheepEntity, f, g, h);
                    this.model.setAngles(sheepEntity, f, g, j, k, l);
                    this.innerModel.animateModel(sheepEntity, f, g, h);
                    this.innerModel.setAngles(sheepEntity, f, g, j, k, l);
                    VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getOutline(SKIN));
                    this.model.render(matrixStack, vertexConsumer, i, LivingEntityRenderer.getOverlay(sheepEntity, 0.0f), 0.0f, 0.0f, 0.0f, 1.0f);
                    this.innerModel.render(matrixStack, vertexConsumer, i, LivingEntityRenderer.getOverlay(sheepEntity, 0.0f), 0.0f, 0.0f, 0.0f, 1.0f);
                }
                return;
            }
            if (sheepEntity.hasCustomName() && "jeb_".equals(sheepEntity.getName().getString())) {
                int m = 25;
                int n = sheepEntity.age / 25 + sheepEntity.getId();
                int o = DyeColor.values().length;
                int p = n % o;
                int q = (n + 1) % o;
                float r = ((float) (sheepEntity.age % 25) + h) / 25.0f;
                float[] fs = SheepEntity.getRgbColor(DyeColor.byId(p));
                float[] gs = SheepEntity.getRgbColor(DyeColor.byId(q));
                s = fs[0] * (1.0f - r) + gs[0] * r;
                t = fs[1] * (1.0f - r) + gs[1] * r;
                u = fs[2] * (1.0f - r) + gs[2] * r;
            } else {
                float[] hs = SheepEntity.getRgbColor(sheepEntity.getColor());
                s = hs[0];
                t = hs[1];
                u = hs[2];
            }

            renderSheepFur(this.getContextModel(), this.model, this.innerModel, SKIN, matrixStack, vertexConsumerProvider, i, sheepEntity, f, g, j, k, l, h, s, t, u);
        }

        protected <T extends LivingEntity> void renderSheepFur(EntityModel<T> contextModel, EntityModel<T> model, EntityModel<T> innerModel, Identifier texture, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float age, float headYaw, float headPitch, float tickDelta, float red, float green, float blue) {
            if (!entity.isInvisible()) {

                contextModel.copyStateTo(innerModel);
                innerModel.animateModel(entity, limbAngle, limbDistance, tickDelta);
                innerModel.setAngles(entity, limbAngle, limbDistance, age, headYaw, headPitch);
                VertexConsumer vertexConsumer = vertexConsumers.getBuffer(CustomLayers.IMMORTAL_CUTOUT.apply(texture));
                innerModel.render(matrices, vertexConsumer, light, LivingEntityRenderer.getOverlay(entity, 0.0f), red, green, blue, 1.0f);
                innerModel.render(matrices, vertexConsumer, light, LivingEntityRenderer.getOverlay(entity, 0.0f), red, green, blue, 1.0f);

                contextModel.copyStateTo(model);
                model.animateModel(entity, limbAngle, limbDistance, tickDelta);
                model.setAngles(entity, limbAngle, limbDistance, age, headYaw, headPitch);
                vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEyes(texture));
                model.render(matrices, vertexConsumer, light, LivingEntityRenderer.getOverlay(entity, 0.0f), red, green, blue, 1.0f);
                model.render(matrices, vertexConsumer, light, LivingEntityRenderer.getOverlay(entity, 0.0f), red, green, blue, 1.0f);
            }
        }
    }
}

