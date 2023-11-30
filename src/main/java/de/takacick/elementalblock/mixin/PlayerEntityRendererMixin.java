package de.takacick.elementalblock.mixin;

import de.takacick.elementalblock.client.renderer.WaterArmorVesselRenderer;
import de.takacick.elementalblock.registry.ItemRegistry;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.ArmorEntityModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    @Unique
    private BipedEntityModel<AbstractClientPlayerEntity> elementalblock$waterArmor;

    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    public void init(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo info) {
        this.elementalblock$waterArmor = new BipedEntityModel<>(ArmorEntityModel.getModelData(new Dilation(0.43f)).getRoot().createPart(64, 32));
        this.addFeature(new WaterArmorVesselRenderer(this, this.elementalblock$waterArmor));
    }

    @Inject(method = "renderArm", at = @At(value = "TAIL"))
    private void renderRightArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve, CallbackInfo ci) {
        if (player.getEquippedStack(EquipmentSlot.CHEST).isOf(ItemRegistry.WATER_ARMOR_VESSEL)) {
            ModelPart waterArm = arm.equals(this.getModel().rightArm) ? this.elementalblock$waterArmor.rightArm : this.elementalblock$waterArmor.leftArm;
            waterArm.copyTransform(arm);

            VertexConsumer vertexConsumer = WaterArmorVesselRenderer.SKIN.getSprite().getTextureSpecificVertexConsumer(vertexConsumers.getBuffer(
                    RenderLayer.getEntityTranslucent(WaterArmorVesselRenderer.SKIN.getAtlasId())));
            Vector3f vec3f = Vec3d.unpackRgb(4159204).multiply(1.1f).toVector3f();
            waterArm.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, vec3f.x(), vec3f.y(), vec3f.z(), 0.7f);
        }
    }
}
