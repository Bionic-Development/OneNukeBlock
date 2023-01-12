package de.takacick.immortalmobs.registry.entity.living.renderer;

import de.takacick.immortalmobs.ImmortalMobs;
import de.takacick.immortalmobs.client.CustomLayers;
import de.takacick.immortalmobs.registry.entity.living.ImmortalPigEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;
import org.jetbrains.annotations.Nullable;

public class ImmortalPigEntityRenderer
        extends MobEntityRenderer<ImmortalPigEntity, PigEntityModel<ImmortalPigEntity>> {
    private static final Identifier TEXTURE = new Identifier("textures/entity/pig/pig.png");
    private static final Identifier EYES = new Identifier(ImmortalMobs.MOD_ID, "textures/entity/immortal_pig_eyes.png");

    public ImmortalPigEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new PigEntityModel<>(context.getPart(EntityModelLayers.PIG)), 0.7f);
        this.addFeature(new ImmortalEyesFeatureRenderer<>(this, EYES));
        this.addFeature(new ElytraFeatureRenderer<>(this, context.getModelLoader()));
    }

    @Override
    protected void setupTransforms(ImmortalPigEntity immortalPigEntity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta) {
        super.setupTransforms(immortalPigEntity, matrices, animationProgress, bodyYaw, tickDelta);
        if (immortalPigEntity.isImmortalFlying()) {
            matrices.translate(-0.0, 0.5, -0.5);
            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90.0F));
        } else if (immortalPigEntity.isImmortalFalling()) {
            matrices.translate(-0.0, 0.5, 0.5);
            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-90.0F));
        }
    }

    @Override
    public Identifier getTexture(ImmortalPigEntity immortalPigEntity) {
        return TEXTURE;
    }

    @Nullable
    @Override
    protected RenderLayer getRenderLayer(ImmortalPigEntity immortalPigEntity, boolean showBody, boolean translucent, boolean showOutline) {
        return CustomLayers.IMMORTAL_CUTOUT.apply(getTexture(immortalPigEntity));
    }

    @Environment(value = EnvType.CLIENT)
    public static class ElytraFeatureRenderer<T extends ImmortalPigEntity, M extends EntityModel<T>>
            extends FeatureRenderer<T, M> {
        private static final Identifier SKIN = new Identifier(ImmortalMobs.MOD_ID, "textures/entity/immortal_elytra.png");
        private final ElytraEntityModel<T> elytra;
        private final ElytraEntityModel<T> outerElytra;

        public ElytraFeatureRenderer(FeatureRendererContext<T, M> context, EntityModelLoader loader) {
            super(context);
            this.elytra = new ElytraEntityModel(loader.getModelPart(EntityModelLayers.ELYTRA));
            this.outerElytra = new ElytraEntityModel(getTexturedModelData().createModel());
        }

        @Override
        public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
            if (!livingEntity.hasImmortalElytra()) {
                return;
            }
            matrixStack.push();
            matrixStack.translate(0.0, 0.75, -0.25);
            matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(80.0F));
            this.getContextModel().copyStateTo(this.elytra);
            this.elytra.setAngles(livingEntity, f, g, j, k, l);
            this.getContextModel().copyStateTo(this.outerElytra);
            this.outerElytra.setAngles(livingEntity, f, g, j, k, l);
            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(CustomLayers.IMMORTAL_CUTOUT.apply(SKIN));
            this.elytra.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
            vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEyes(SKIN));
            this.outerElytra.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 0.4029412f, 0.14705883f, 0.5411765f, 1.0f);
            this.outerElytra.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 0.4029412f, 0.14705883f, 0.5411765f, 1.0f);
            matrixStack.pop();
        }

        public static TexturedModelData getTexturedModelData() {
            ModelData modelData = new ModelData();
            ModelPartData modelPartData = modelData.getRoot();
            Dilation dilation = new Dilation(1.1f);
            modelPartData.addChild(EntityModelPartNames.LEFT_WING, ModelPartBuilder.create().uv(22, 0).cuboid(-10.0f, 0.0f, 0.0f, 10.0f, 20.0f, 2.0f, dilation), ModelTransform.of(5.0f, 0.0f, 0.0f, 0.2617994f, 0.0f, -0.2617994f));
            modelPartData.addChild(EntityModelPartNames.RIGHT_WING, ModelPartBuilder.create().uv(22, 0).mirrored().cuboid(0.0f, 0.0f, 0.0f, 10.0f, 20.0f, 2.0f, dilation), ModelTransform.of(-5.0f, 0.0f, 0.0f, 0.2617994f, 0.0f, 0.2617994f));
            return TexturedModelData.of(modelData, 64, 32);
        }
    }


}

