package de.takacick.heartmoney.mixin;

import de.takacick.heartmoney.client.CustomLayers;
import de.takacick.heartmoney.registry.ItemRegistry;
import de.takacick.heartmoney.registry.entity.custom.renderer.HeartJetPackRenderer;
import de.takacick.heartmoney.registry.entity.custom.renderer.LoveBarrierSuitRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    private AbstractClientPlayerEntity tempPlayer;
    private BipedEntityModel<AbstractClientPlayerEntity> heartmoney$loveBarrierSuit;

    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    public void init(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo info) {
        this.heartmoney$loveBarrierSuit = new BipedEntityModel<>(ctx.getPart(slim ? EntityModelLayers.PLAYER_SLIM_INNER_ARMOR : EntityModelLayers.PLAYER_INNER_ARMOR));
        this.addFeature(new LoveBarrierSuitRenderer(this, heartmoney$loveBarrierSuit));
        this.addFeature(new HeartJetPackRenderer<>(this, ctx.getHeldItemRenderer()));
    }

    @Inject(method = "renderArm", at = @At("HEAD"))
    private void saveTempPlayer(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve, CallbackInfo info) {
        this.tempPlayer = player;
    }

    @ModifyArg(method = "renderArm", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumerProvider;getBuffer(Lnet/minecraft/client/render/RenderLayer;)Lnet/minecraft/client/render/VertexConsumer;"))
    private RenderLayer renderMaidArm(RenderLayer renderLayer) {
        if (this.tempPlayer != null && this.tempPlayer.getEquippedStack(EquipmentSlot.CHEST).isOf(ItemRegistry.MAID_SUIT_ARMOR)) {
            RenderLayer layer = CustomLayers.MAID_CUTOUT.apply(getTexture(tempPlayer), CustomLayers.getOverlayTexture(getModel() instanceof PlayerEntityModelAccessor playerEntityModelAccessor && playerEntityModelAccessor.getThinArms()));
            tempPlayer = null;
            return layer;
        }
        return renderLayer;
    }

    @Inject(method = "renderRightArm", at = @At(value = "TAIL"))
    private void renderRightArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, CallbackInfo info) {
        if (!player.getEquippedStack(EquipmentSlot.CHEST).isOf(ItemRegistry.LOVE_BARRIER_SUIT)) {
            return;
        }

        float tickDelta = MinecraftClient.getInstance().getTickDelta();

        float f = (float) player.age + tickDelta;
        heartmoney$loveBarrierSuit.rightArm.copyTransform(this.model.rightArm);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEnergySwirl(LoveBarrierSuitRenderer.LOVE_BARRIER_SUIT, (f * 0.01f) % 1.0f, f * 0.01f % 1.0f));
        heartmoney$loveBarrierSuit.rightArm.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
    }

    @Inject(method = "renderLeftArm", at = @At(value = "TAIL"))
    private void renderLeftArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, CallbackInfo info) {
        if (!player.getEquippedStack(EquipmentSlot.CHEST).isOf(ItemRegistry.LOVE_BARRIER_SUIT)) {
            return;
        }

        float tickDelta = MinecraftClient.getInstance().getTickDelta();

        float f = (float) player.age + tickDelta;
        heartmoney$loveBarrierSuit.leftArm.copyTransform(this.model.leftArm);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEnergySwirl(LoveBarrierSuitRenderer.LOVE_BARRIER_SUIT, (f * 0.01f) % 1.0f, f * 0.01f % 1.0f));
        heartmoney$loveBarrierSuit.leftArm.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
    }
}