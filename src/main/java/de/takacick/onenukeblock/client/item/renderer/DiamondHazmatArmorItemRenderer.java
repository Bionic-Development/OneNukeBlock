package de.takacick.onenukeblock.client.item.renderer;

import de.takacick.onenukeblock.client.entity.feature.DiamondHazmatArmorFeatureRenderer;
import de.takacick.onenukeblock.client.item.model.DiamondHazmatArmorItemModel;
import de.takacick.utils.item.client.render.ItemModelRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class DiamondHazmatArmorItemRenderer extends ItemModelRenderer<DiamondHazmatArmorItemModel> {
    public static final Identifier TEXTURE = DiamondHazmatArmorFeatureRenderer.TEXTURE;

    public DiamondHazmatArmorItemRenderer(DiamondHazmatArmorItemModel heavyCoreArmorItemModel) {
        super(heavyCoreArmorItemModel);
    }

    public static DiamondHazmatArmorItemRenderer createHelmet() {
        return new DiamondHazmatArmorItemRenderer(new DiamondHazmatArmorItemModel(DiamondHazmatArmorItemModel.getHelmetTexturedModelData().createModel()));
    }

    public static DiamondHazmatArmorItemRenderer createChestplate() {
        return new DiamondHazmatArmorItemRenderer(new DiamondHazmatArmorItemModel(DiamondHazmatArmorItemModel.getChestplateTexturedModelData().createModel()));
    }

    public static DiamondHazmatArmorItemRenderer createLeggings() {
        return new DiamondHazmatArmorItemRenderer(new DiamondHazmatArmorItemModel(DiamondHazmatArmorItemModel.getLeggingsTexturedModelData().createModel()));
    }

    public static DiamondHazmatArmorItemRenderer createBoots() {
        return new DiamondHazmatArmorItemRenderer(new DiamondHazmatArmorItemModel(DiamondHazmatArmorItemModel.getBootsTexturedModelData().createModel()));
    }

    @Override
    public Identifier getTexture(ItemStack itemStack, @Nullable LivingEntity livingEntity, ModelTransformationMode modelTransformationMode) {
        return TEXTURE;
    }
}
