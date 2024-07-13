package de.takacick.onegirlboyblock.client.item.renderer;

import de.takacick.onegirlboyblock.OneGirlBoyBlock;
import de.takacick.onegirlboyblock.client.item.model.TiaraItemModel;
import de.takacick.utils.item.client.render.ItemModelRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class TiaraItemRenderer extends ItemModelRenderer<TiaraItemModel> {
    public static final Identifier TEXTURE = Identifier.of(OneGirlBoyBlock.MOD_ID, "textures/entity/tiara.png");

    public TiaraItemRenderer() {
        super(new TiaraItemModel(TiaraItemModel.getTexturedModelData().createModel()));
    }

    @Override
    public Identifier getTexture(ItemStack itemStack, @Nullable LivingEntity livingEntity, ModelTransformationMode modelTransformationMode) {
        return TEXTURE;
    }
}
