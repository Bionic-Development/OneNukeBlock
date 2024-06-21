package de.takacick.onescaryblock.mixin.bloodsuit;

import com.llamalad7.mixinextras.sugar.Local;
import de.takacick.onescaryblock.access.BloodBorderProperties;
import de.takacick.onescaryblock.client.entity.feature.BloodBorderSuitFeatureRenderer;
import de.takacick.onescaryblock.registry.item.BloodBorderSuit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/PlayerEntityRenderer;addFeature(Lnet/minecraft/client/render/entity/feature/FeatureRenderer;)Z", ordinal = 1, shift = At.Shift.AFTER))
    public void init(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo info) {
        this.addFeature(new BloodBorderSuitFeatureRenderer<>(this));
    }

    @Inject(method = "renderArm", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;II)V", ordinal = 1, shift = At.Shift.AFTER))
    private void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve, CallbackInfo info, @Local Identifier identifier) {
        if (player.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof BloodBorderSuit) {

            float delta = MinecraftClient.getInstance().isPaused() ? 0f : MinecraftClient.getInstance().getTickDelta();
            float progress = 0f;
            if (player instanceof BloodBorderProperties bloodBorderProperties) {
                progress = bloodBorderProperties.getBloodBorderSuitHelper().getProgress(delta);
            }

            BloodBorderSuitFeatureRenderer.renderModel(matrices, sleeve, vertexConsumers, player.age + delta, light, progress);
        }
    }
}