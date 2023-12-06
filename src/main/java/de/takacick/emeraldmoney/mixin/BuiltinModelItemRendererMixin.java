package de.takacick.emeraldmoney.mixin;

import de.takacick.emeraldmoney.client.entity.model.VillagerDrillModel;
import de.takacick.emeraldmoney.client.entity.model.VillagerRobeEntityModel;
import de.takacick.emeraldmoney.client.entity.renderer.VillagerRobeFeatureRenderer;
import de.takacick.emeraldmoney.registry.ItemRegistry;
import de.takacick.emeraldmoney.registry.item.VillagerDriller;
import de.takacick.emeraldmoney.registry.item.VillagerRobe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.VillagerType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuiltinModelItemRenderer.class)
public abstract class BuiltinModelItemRendererMixin {

    @Unique
    private VillagerDrillModel emeraldmoney$terraDrill;
    @Unique
    private VillagerRobeEntityModel<LivingEntity> emeraldmoney$villagerRobe;

    @Inject(method = "reload", at = @At("TAIL"))
    public void render(ResourceManager manager, CallbackInfo info) {
        this.emeraldmoney$terraDrill = new VillagerDrillModel();
        this.emeraldmoney$villagerRobe = new VillagerRobeEntityModel<>(VillagerRobeEntityModel.getTexturedModelData(Dilation.NONE).createModel());
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo info) {
        Item item = stack.getItem();
        if (item.equals(ItemRegistry.VILLAGER_DRILLER)) {
            matrices.push();
            if (!mode.equals(ModelTransformationMode.THIRD_PERSON_RIGHT_HAND)) {
                matrices.scale(1.0f, -1.0f, -1.0f);
            }

            this.emeraldmoney$terraDrill.setRotation(VillagerDriller.getRotation(stack, MinecraftClient.getInstance().getTickDelta()));
            VertexConsumer vertexConsumer = ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, this.emeraldmoney$terraDrill.getLayer(VillagerDrillModel.TEXTURE), false, stack.hasGlint());
            this.emeraldmoney$terraDrill.render(matrices, vertexConsumer, light, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
            matrices.pop();
        } else if (item.equals(ItemRegistry.VILLAGER_ROBE)) {
            matrices.push();
            matrices.translate(0.5, 0, 0.5);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0F));
            matrices.scale(2.f, 2f, 2f);
            matrices.translate(0, -1.5, 0);

            VillagerData villagerData = VillagerRobe.getVillagerData(stack);
            VillagerType villagerType = villagerData.getType();
            VillagerProfession villagerProfession = villagerData.getProfession();
            Identifier identifier = VillagerRobeFeatureRenderer.findTexture("type", Registries.VILLAGER_TYPE.getId(villagerType));

            this.emeraldmoney$villagerRobe.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(identifier)), light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);

            if (villagerProfession != VillagerProfession.NONE) {
                identifier = VillagerRobeFeatureRenderer.findTexture("profession", Registries.VILLAGER_PROFESSION.getId(villagerProfession));

                this.emeraldmoney$villagerRobe.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(identifier)), light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
            }

            matrices.pop();
        }
    }
}

