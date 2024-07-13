package de.takacick.onegirlboyblock.client.item.renderer;

import de.takacick.onegirlboyblock.OneGirlBoyBlock;
import de.takacick.onegirlboyblock.client.item.renderer.feature.TurboBoardBeamFeatureRenderer;
import de.takacick.onegirlboyblock.client.item.model.TurboBoardItemModel;
import de.takacick.utils.item.client.render.ItemModelRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class TurboBoardItemRenderer extends ItemModelRenderer<TurboBoardItemModel> {
    public static final Identifier TEXTURE = Identifier.of(OneGirlBoyBlock.MOD_ID, "textures/entity/turbo_board.png");

    public TurboBoardItemRenderer() {
        super(new TurboBoardItemModel(TurboBoardItemModel.getTexturedModelData().createModel()));
        this.addFeature(new TurboBoardBeamFeatureRenderer(this));
    }

    @Override
    public Identifier getTexture(ItemStack itemStack, @Nullable LivingEntity livingEntity, ModelTransformationMode modelTransformationMode) {
        return TEXTURE;
    }
}
