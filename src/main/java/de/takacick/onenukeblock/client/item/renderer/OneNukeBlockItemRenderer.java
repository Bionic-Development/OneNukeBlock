package de.takacick.onenukeblock.client.item.renderer;

import de.takacick.onenukeblock.OneNukeBlock;
import de.takacick.onenukeblock.client.item.model.OneNukeBlockItemModel;
import de.takacick.utils.item.client.render.ItemModelRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class OneNukeBlockItemRenderer extends ItemModelRenderer<OneNukeBlockItemModel> {

    public static final Identifier TEXTURE = Identifier.of(OneNukeBlock.MOD_ID, "textures/entity/one_nuke_block.png");

    public OneNukeBlockItemRenderer() {
        super(new OneNukeBlockItemModel(OneNukeBlockItemModel.getTexturedModelData().createModel()));
    }

    @Override
    public Identifier getTexture(ItemStack itemStack, @Nullable LivingEntity livingEntity, ModelTransformationMode modelTransformationMode) {
        return TEXTURE;
    }
}
