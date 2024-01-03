package de.takacick.raidbase.mixin;

import de.takacick.raidbase.access.LivingProperties;
import de.takacick.raidbase.registry.EntityRegistry;
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
import net.minecraft.entity.Entity;
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

    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
    private void render(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo info) {
        if (livingEntity instanceof LivingProperties livingProperties && livingProperties.isGettingBanned()) {
            matrixStack.translate(0, 0.5, 0);

            Entity renderEntity = livingProperties.getRenderEntity();

            if (renderEntity != null) {
                float width = livingEntity.getWidth() / renderEntity.getWidth();
                float height = livingEntity.getHeight() / renderEntity.getHeight();

                matrixStack.scale(width, height, width);
            }

            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(g, livingEntity.prevYaw, livingEntity.getYaw()) - 90.0F));
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerp(g, livingEntity.prevPitch, livingEntity.getPitch()) - 45.0F));
            matrixStack.multiply(RotationAxis.NEGATIVE_Z.rotation(MathHelper.lerp(g, (float) livingEntity.age - 1 + livingEntity.getId(), livingEntity.age + livingEntity.getId()) * 0.45f));
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotation(MathHelper.lerp(g, (float) livingEntity.age - 1 + livingEntity.getId(), livingEntity.age + livingEntity.getId()) * 0.45f));
            matrixStack.multiply(RotationAxis.NEGATIVE_Y.rotation(MathHelper.lerp(g, (float) livingEntity.age - 1 + livingEntity.getId(), livingEntity.age + livingEntity.getId()) * 0.45f));

            matrixStack.translate(0, -0.5, 0);

            Random random = Random.create();
            if (livingProperties.getBanTicks() <= 15) {
                for (int x = 0; x < random.nextBetween(1, 5); x++) {
                    matrixStack.push();
                    matrixStack.translate(random.nextGaussian() * 0.1, livingEntity.getHeight() * random.nextDouble(), random.nextGaussian() * 0.1);
                    matrixStack.scale(0.0075f, 0.0075f, 0.0075f);
                    matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation((float) Random.create().nextGaussian()));
                    matrixStack.multiply(RotationAxis.POSITIVE_X.rotation((float) Random.create().nextGaussian()));
                    matrixStack.multiply(RotationAxis.POSITIVE_Z.rotation((float) Random.create().nextGaussian()));
                    MinecraftClient.getInstance().getEntityRenderDispatcher().render(new LightningEntity(EntityRegistry.BAN_LIGHTNING, MinecraftClient.getInstance().world), BlockPos.ORIGIN.getX(), BlockPos.ORIGIN.getY(), BlockPos.ORIGIN.getZ(), 0, 0, matrixStack, vertexConsumerProvider, i);
                    matrixStack.pop();
                }
            }

            if (renderEntity != null) {
                MinecraftClient.getInstance().getEntityRenderDispatcher().render(renderEntity,
                        0, 0, 0, 0, g, matrixStack, vertexConsumerProvider, i);
                info.cancel();
            }
        }


        if (livingEntity instanceof LivingProperties livingProperties
                && livingProperties.isWaterElectroShocked()) {
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
                        this.raidbase$renderObject(matrixStack, vertexConsumerProvider, i);
                        matrixStack.pop();
                    }
                }
            }
            matrixStack.pop();
        }
    }

    @Inject(method = "isShaking", at = @At("HEAD"), cancellable = true)
    public void isShaking(T livingEntity, CallbackInfoReturnable<Boolean> info) {
        if (livingEntity instanceof LivingProperties livingProperties
                && livingProperties.isWaterElectroShocked()) {
            info.setReturnValue(true);
        }
    }

    private void raidbase$renderObject(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        Random random = Random.create();

        matrices.scale(0.00475f, 0.00475f, 0.00475f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotation((float) random.nextGaussian()));
        matrices.multiply(RotationAxis.POSITIVE_X.rotation((float) random.nextGaussian()));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotation((float) random.nextGaussian()));
        this.dispatcher.render(new LightningEntity(EntityType.LIGHTNING_BOLT, MinecraftClient.getInstance().world), BlockPos.ORIGIN.getX(), BlockPos.ORIGIN.getY(), BlockPos.ORIGIN.getZ(), 0, 0, matrices, vertexConsumers, light);
    }
}