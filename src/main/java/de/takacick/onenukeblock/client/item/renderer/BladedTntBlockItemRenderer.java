package de.takacick.onenukeblock.client.item.renderer;

import de.takacick.onenukeblock.OneNukeBlock;
import de.takacick.onenukeblock.client.item.model.BladedTntBlockItemModel;
import de.takacick.utils.item.client.render.ItemModelRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class BladedTntBlockItemRenderer extends ItemModelRenderer<BladedTntBlockItemModel> {

    public static final Identifier TEXTURE = Identifier.of(OneNukeBlock.MOD_ID, "textures/entity/bladed_tnt.png");

    public BladedTntBlockItemRenderer() {
        super(new BladedTntBlockItemModel(BladedTntBlockItemModel.getTexturedModelData().createModel()));
    }

    @Override
    public Identifier getTexture(ItemStack itemStack, @Nullable LivingEntity livingEntity, ModelTransformationMode modelTransformationMode) {
        return TEXTURE;
    }
}
