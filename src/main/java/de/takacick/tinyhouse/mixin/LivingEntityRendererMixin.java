package de.takacick.tinyhouse.mixin;

import de.takacick.tinyhouse.access.EntityProperties;
import de.takacick.tinyhouse.access.LivingProperties;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements FeatureRendererContext<T, M> {

    protected LivingEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(EntityRendererFactory.Context ctx, EntityModel model, float shadowRadius, CallbackInfo ci) {
    }

    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V", shift = At.Shift.AFTER), cancellable = true)
    public void render(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo info) {
        if (livingEntity instanceof EntityProperties livingProperties
                && livingProperties.getBlockMagnetOwner() >= 0) {
            int m = 5;
            Random random = Random.create();

            matrixStack.push();
            List<ModelPart> list = new ArrayList<>();
            HashMap<ModelPart, ModelPart> map = null;
            if (getModel() instanceof AnimalModel<?> animalModel) {
                animalModel.getHeadParts().forEach(list::add);
                animalModel.getBodyParts().forEach(list::add);
            } else if (getModel() instanceof SinglePartEntityModel<?> singlePartEntityModel) {
                singlePartEntityModel.getPart().rotate(matrixStack);
                HashMap<ModelPart, ModelPart> hashMap = new HashMap<>();

                ((ModelPartAccessor) (Object) singlePartEntityModel.getPart()).getChildren().forEach((s, modelPart) -> {
                    ((ModelPartAccessor) (Object) modelPart).getChildren().forEach((c, part) -> {
                        list.add(part);
                        hashMap.put(part, modelPart);
                    });
                    list.add(modelPart);
                });

                map = hashMap;
            }

            if (!list.isEmpty()) {
                for (int n = 0; n < m && list.size() > 0; ++n) {
                    ModelPart modelPart = list.get(random.nextInt(list.size()));
                    if (!modelPart.isEmpty()) {
                        matrixStack.push();
                        ModelPart.Cuboid cuboid = modelPart.getRandomCuboid(random);

                        if (map != null) {
                            ModelPart modelPart1 = map.getOrDefault(modelPart, null);
                            if (modelPart1 != null) {
                                modelPart1.rotate(matrixStack);
                            }
                        }

                        modelPart.rotate(matrixStack);
                        float o = random.nextFloat();
                        float p = random.nextFloat();
                        float q = random.nextFloat();
                        float r = MathHelper.lerp(o, cuboid.minX, cuboid.maxX) / 16.0f;
                        float s = MathHelper.lerp(p, cuboid.minY, cuboid.maxY) / 16.0f;
                        float t = MathHelper.lerp(q, cuboid.minZ, cuboid.maxZ) / 16.0f;
                        matrixStack.translate(r, s, t);
                        this.tinyhouse$renderObject(matrixStack, vertexConsumerProvider, i);
                        matrixStack.pop();
                    }
                }
            }
            matrixStack.pop();
        }
    }

    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;setupTransforms(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/util/math/MatrixStack;FFF)V", shift = At.Shift.BEFORE))
    public void scale(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo info) {
        if (livingEntity instanceof EntityProperties entityProperties) {
            float height = entityProperties.getCrushedHeight(g);
            if(height != 1f) {
                float width = height <= 0 ? 0 : 1 + 1 - height;
                matrixStack.scale(width, height, width);
            }
        }
    }

    @Inject(method = "isShaking", at = @At("HEAD"), cancellable = true)
    private void isShaking(T entity, CallbackInfoReturnable<Boolean> info) {
        if (entity instanceof LivingProperties livingProperties
                && livingProperties.hasFrozenBody()) {
            info.setReturnValue(true);
        } else if (entity instanceof EntityProperties entityProperties
                && entityProperties.getBlockMagnetOwner() >= 0) {
            info.setReturnValue(true);
        }
    }

    private void tinyhouse$renderObject(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        Random random = Random.create();

        matrices.scale(0.00475f, 0.00475f, 0.00475f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotation((float) random.nextGaussian()));
        matrices.multiply(RotationAxis.POSITIVE_X.rotation((float) random.nextGaussian()));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotation((float) random.nextGaussian()));
        this.dispatcher.render(new LightningEntity(EntityType.LIGHTNING_BOLT, MinecraftClient.getInstance().world), BlockPos.ORIGIN.getX(), BlockPos.ORIGIN.getY(), BlockPos.ORIGIN.getZ(), 0, 0, matrices, vertexConsumers, light);
    }
}