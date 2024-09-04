package de.takacick.onenukeblock.mixin.nuclearwater;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import de.takacick.onenukeblock.utils.data.AttachmentTypes;
import de.takacick.onenukeblock.utils.data.attachments.NuclearMutations;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>>
        extends EntityRenderer<T>
        implements FeatureRendererContext<T, M> {

    @Shadow
    public abstract M getModel();

    protected LivingEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @WrapOperation(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(value = "INVOKE", target = "net/minecraft/client/render/entity/model/EntityModel.render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;III)V"))
    private void render(EntityModel<T> instance, MatrixStack matrixStack, VertexConsumer vertexConsumer, int light, int overlay, int color, Operation<Void> original, @Local(argsOnly = true) LivingEntity livingEntity, @Local(argsOnly = true, ordinal = 1) float tickDelta) {
        original.call(instance, matrixStack, vertexConsumer, light, overlay, color);

        NuclearMutations mutations = livingEntity.getAttached(AttachmentTypes.NUCLEAR_MUTATIONS);
        if (mutations != null) {

            if (this.getModel() instanceof AnimalModelAccessor animalModel) {
                List<ModelPart> modelPartList = new ArrayList<>();
                animalModel.invokeGetHeadParts().forEach(modelPartList::add);
                animalModel.invokeGetBodyParts().forEach(modelPartList::add);

                if (this.getModel() instanceof PlayerEntityModel<?> model) {
                    modelPartList.remove(model.jacket);
                    modelPartList.remove(model.leftSleeve);
                    modelPartList.remove(model.hat);
                    modelPartList.remove(model.leftPants);
                    modelPartList.remove(model.rightSleeve);
                    modelPartList.remove(model.rightPants);
                }

                mutations.getMutations().forEach(mutation -> {
                    Random random = Random.create(mutation.getSeed());

                    matrixStack.push();
                    ModelPart modelPart = modelPartList.get(random.nextInt(modelPartList.size()));
                    ModelPart targetPart = modelPartList.get(random.nextInt(modelPartList.size()));

                    ModelPart.Cuboid cuboid = targetPart.getRandomCuboid(random);
                    targetPart.rotate(matrixStack);
                    float directionX = random.nextFloat();
                    float directionY = random.nextFloat();
                    float directionZ = random.nextFloat();
                    float r = MathHelper.lerp(directionX, cuboid.minX, cuboid.maxX) / 16.0f;
                    float s = MathHelper.lerp(directionY, cuboid.minY, cuboid.maxY) / 16.0f;
                    float t = MathHelper.lerp(directionZ, cuboid.minZ, cuboid.maxZ) / 16.0f;
                    matrixStack.translate(r, s, t);
                    directionX = -1.0f * (directionX * 2.0f - 1.0f);
                    directionY = -1.0f * (directionY * 2.0f - 1.0f);
                    directionZ = -1.0f * (directionZ * 2.0f - 1.0f);

                    float f = MathHelper.sqrt(directionX * directionX + directionZ * directionZ);
                    float yaw = (float) (Math.atan2(directionX, directionZ) * 57.2957763671875);
                    float pitch = (float) (Math.atan2(directionY, f) * 57.2957763671875);
                    matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yaw));
                    matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(pitch));

                    ModelTransform modelTransform = modelPart.getTransform();

                    modelPart.resetTransform();
                    modelPart.pivotX = 0;
                    modelPart.pivotY = 0;
                    modelPart.pivotZ = 0;

                    float timeScale = Math.min(mutation.getAge(tickDelta) / 60f, 1f);
                    float scale = ((random.nextFloat() * 0.6f + 0.4f) * timeScale) * 0.7f;

                    modelPart.xScale = scale;
                    modelPart.yScale = scale;
                    modelPart.zScale = scale;

                    float modelYaw = random.nextFloat();
                    float modelPitch = random.nextFloat();
                    float roll = random.nextFloat();

                    if (random.nextDouble() <= 0.4) {
                        float delta = (float) Math.sin((livingEntity.age + tickDelta) * 0.25f);
                        if (random.nextDouble() <= 0.4) {
                            modelYaw = MathHelper.lerp(delta, modelYaw, modelYaw + random.nextFloat() * 0.2f);
                        }
                        if (random.nextDouble() <= 0.4) {
                            modelPitch = MathHelper.lerp(delta, modelPitch, modelPitch + random.nextFloat() * 0.2f);
                        }
                        if (random.nextDouble() <= 0.4) {
                            roll = MathHelper.lerp(delta, roll, roll + random.nextFloat() * 0.2f);
                        }
                    }
                    modelPart.setAngles(modelYaw * 360f * ((float) Math.PI / 180f), modelPitch * 360f * ((float) Math.PI / 180f), roll * 360f * ((float) Math.PI / 180f));
                    modelPart.render(matrixStack, vertexConsumer, light, overlay, color);

                    modelPart.setTransform(modelTransform);
                    matrixStack.pop();
                });
            }
        }
    }
}