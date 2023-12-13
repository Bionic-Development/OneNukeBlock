package de.takacick.upgradebody.mixin;

import de.takacick.upgradebody.client.model.parts.CyberChainsawsModel;
import de.takacick.upgradebody.client.model.parts.EnergyBellyCannonModel;
import de.takacick.upgradebody.client.model.parts.KillerDrillerModel;
import de.takacick.upgradebody.client.model.parts.TankTracksModel;
import de.takacick.upgradebody.registry.ItemRegistry;
import de.takacick.upgradebody.registry.bodypart.BodyPartManager;
import de.takacick.upgradebody.registry.bodypart.BodyParts;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuiltinModelItemRenderer.class)
public abstract class BuiltinModelItemRendererMixin {

    @Unique
    private TankTracksModel upgradebody$tankTracksModel;
    @Unique
    private EnergyBellyCannonModel upgradebody$energyBellyCannonModel;
    @Unique
    private KillerDrillerModel upgradebody$killerDrillerModel;
    @Unique
    private CyberChainsawsModel upgradebody$cyberChainsawsModel;

    @Inject(method = "reload", at = @At("TAIL"))
    public void render(ResourceManager manager, CallbackInfo info) {
        this.upgradebody$tankTracksModel = new TankTracksModel(BodyParts.TANK_TRACKS, new BodyPartManager());
        this.upgradebody$energyBellyCannonModel = new EnergyBellyCannonModel(BodyParts.ENERGY_BELLY_CANNON, new BodyPartManager());
        this.upgradebody$killerDrillerModel = new KillerDrillerModel(BodyParts.KILLER_DRILLER, new BodyPartManager());
        this.upgradebody$cyberChainsawsModel = new CyberChainsawsModel(BodyParts.CYBER_CHAINSAWS, new BodyPartManager());
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo info) {
        Item item = stack.getItem();
        if (item.equals(ItemRegistry.TANK_TRACKS)) {
            matrices.push();
            matrices.scale(1.0f, -1.0f, -1.0f);
            matrices.translate(0.5, -1.501, -0.5);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));

            VertexConsumer vertexConsumer = ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, RenderLayer.getEntityCutoutNoCull(TankTracksModel.TEXTURE), false, stack.hasGlint());
            this.upgradebody$tankTracksModel.render(matrices, vertexConsumer, light, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
            matrices.pop();
        } else if (item.equals(ItemRegistry.ENERGY_BELLY_CANNON)) {
            matrices.push();
            matrices.scale(1.0f, -1.0f, -1.0f);
            matrices.translate(0.5, -1.501, -0.5);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));

            VertexConsumer vertexConsumer = ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, RenderLayer.getEntityCutoutNoCull(EnergyBellyCannonModel.TEXTURE), false, stack.hasGlint());
            this.upgradebody$energyBellyCannonModel.render(matrices, vertexConsumer, light, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
            matrices.pop();
        } else if (item.equals(ItemRegistry.KILLER_DRILLER)) {
            matrices.push();
            matrices.scale(1.0f, -1.0f, -1.0f);
            matrices.translate(0.5, -1.501, -0.5);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));

            VertexConsumer vertexConsumer = ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, RenderLayer.getEntityCutoutNoCull(KillerDrillerModel.TEXTURE), false, stack.hasGlint());
            this.upgradebody$killerDrillerModel.render(matrices, vertexConsumer, light, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
            matrices.pop();
        } else if (item.equals(ItemRegistry.CYBER_CHAINSAWS)) {
            matrices.push();
            matrices.scale(1.0f, -1.0f, -1.0f);
            matrices.translate(0.5, -1.501, -0.5);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));

            VertexConsumer vertexConsumer = ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, RenderLayer.getEntityCutoutNoCull(CyberChainsawsModel.TEXTURE), false, stack.hasGlint());
            this.upgradebody$cyberChainsawsModel.render(matrices, vertexConsumer, light, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
            matrices.pop();
        }
    }
}

