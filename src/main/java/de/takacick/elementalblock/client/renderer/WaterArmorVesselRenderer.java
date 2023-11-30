package de.takacick.elementalblock.client.renderer;

import de.takacick.elementalblock.registry.ItemRegistry;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public class WaterArmorVesselRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public static final SpriteIdentifier SKIN = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("block/water_flow"));
    private final BipedEntityModel<AbstractClientPlayerEntity> leggingsModel;

    public WaterArmorVesselRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext,
                                    BipedEntityModel<AbstractClientPlayerEntity> leggingsModel) {
        super(featureRendererContext);
        this.leggingsModel = leggingsModel;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (!entity.getEquippedStack(EquipmentSlot.CHEST).isOf(ItemRegistry.WATER_ARMOR_VESSEL)) {
            return;
        }

        BipedEntityModel<AbstractClientPlayerEntity> entityModel = leggingsModel;
        entityModel.animateModel(entity, limbAngle, limbDistance, tickDelta);
        this.getContextModel().copyBipedStateTo(entityModel);
        VertexConsumer vertexConsumer = SKIN.getSprite().getTextureSpecificVertexConsumer(vertexConsumers.getBuffer(
                RenderLayer.getEntityTranslucent(SKIN.getAtlasId())));

        Vector3f vec3f = Vec3d.unpackRgb(4159204).multiply(1.1f).toVector3f();

        entityModel.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, vec3f.x(), vec3f.y(), vec3f.z(), 0.7f);
    }
}

