package de.takacick.onedeathblock.mixin;

import de.takacick.onedeathblock.access.PlayerProperties;
import de.takacick.onedeathblock.client.renderer.HeadSpawnerRenderer;
import de.takacick.onedeathblock.client.renderer.LightningFeatureRenderer;
import de.takacick.onedeathblock.client.renderer.SpikyIronArmorSuitRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    private AbstractClientPlayerEntity onedeathblock$tempPlayer;

    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    public void init(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo info) {
        this.addFeature(new SpikyIronArmorSuitRenderer(this));
        this.addFeature(new HeadSpawnerRenderer(this));
        this.addFeature(new LightningFeatureRenderer<>(ctx, this));
    }

    @Inject(method = "renderArm", at = @At(value = "HEAD"))
    private void renderArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve, CallbackInfo info) {
        this.onedeathblock$tempPlayer = player;

        if (this.onedeathblock$tempPlayer instanceof PlayerProperties playerProperties) {
            int ticks = playerProperties.getTntExplosionTicks();

            if (ticks > 0) {
                float delta = MinecraftClient.getInstance().getTickDelta();
                if ((float) ticks - delta + 1.0f < 10.0f) {
                    float h = 1.0f - ((float) ticks - delta + 1.0f) / 10.0f;
                    h = MathHelper.clamp(h, 0.0f, 1.0f);
                    h *= h;
                    h *= h;
                    float k = 1.0f + h * 0.3f;
                    matrices.scale(k, k, k);
                }
            }
        }
    }

    @ModifyArg(method = "renderArm", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;II)V"), index = 3)
    private int renderArm(int uv) {
        if (this.onedeathblock$tempPlayer instanceof PlayerProperties playerProperties) {
            int ticks = playerProperties.getTntExplosionTicks();

            if (ticks > 0) {
                return ticks / 5 % 2 == 0
                        ? OverlayTexture.packUv(OverlayTexture.getU(1.0f), 10)
                        : OverlayTexture.DEFAULT_UV;
            }
        }

        this.onedeathblock$tempPlayer = null;

        return uv;
    }

}
