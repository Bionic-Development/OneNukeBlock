package de.takacick.emeraldmoney.client.entity.renderer;

import de.takacick.emeraldmoney.client.entity.model.VillagerRobeEntityModel;
import de.takacick.emeraldmoney.registry.ItemRegistry;
import de.takacick.emeraldmoney.registry.item.VillagerRobe;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.VillagerType;

@Environment(value = EnvType.CLIENT)
public class VillagerRobeFeatureRenderer<T extends LivingEntity, M extends BipedEntityModel<T>> extends FeatureRenderer<T, M> {

    private final VillagerRobeEntityModel<T> villagerRobeEntityModel;

    public VillagerRobeFeatureRenderer(FeatureRendererContext<T, M> context, VillagerRobeEntityModel<T> villagerRobeEntityModel) {
        super(context);
        this.villagerRobeEntityModel = villagerRobeEntityModel;
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
        ItemStack itemStack = livingEntity.getEquippedStack(EquipmentSlot.CHEST);
        if (!itemStack.isOf(ItemRegistry.VILLAGER_ROBE)) {
            return;
        }

        VillagerData villagerData = VillagerRobe.getVillagerData(itemStack);
        VillagerType villagerType = villagerData.getType();
        VillagerProfession villagerProfession = villagerData.getProfession();
        Identifier identifier = findTexture("type", Registries.VILLAGER_TYPE.getId(villagerType));
        this.getContextModel().copyBipedStateTo(this.villagerRobeEntityModel);
        renderModel(this.villagerRobeEntityModel, identifier, matrixStack, vertexConsumerProvider, i, livingEntity, 1.0f, 1.0f, 1.0f);

        if (villagerProfession != VillagerProfession.NONE) {
            identifier = findTexture("profession", Registries.VILLAGER_PROFESSION.getId(villagerProfession));
            renderModel(this.villagerRobeEntityModel, identifier, matrixStack, vertexConsumerProvider, i, livingEntity, 1.0f, 1.0f, 1.0f);
        }
    }

    public static Identifier findTexture(String keyType, Identifier keyId) {
        return keyId.withPath(path -> "textures/entity/villager/" + keyType + "/" + path + ".png");
    }
}

