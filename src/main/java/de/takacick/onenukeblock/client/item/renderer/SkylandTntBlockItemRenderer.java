package de.takacick.onenukeblock.client.item.renderer;

import de.takacick.onenukeblock.OneNukeBlock;
import de.takacick.onenukeblock.client.item.model.OneNukeBlockItemModel;
import de.takacick.onenukeblock.client.item.model.SkylandTntBlockItemModel;
import de.takacick.onenukeblock.registry.block.entity.renderer.NukeOneBlockBlockEntityRenderer;
import de.takacick.utils.item.client.render.ItemModelRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class SkylandTntBlockItemRenderer extends ItemModelRenderer<SkylandTntBlockItemModel> {

    public static final Identifier TEXTURE = Identifier.of(OneNukeBlock.MOD_ID, "textures/entity/skyland_tnt.png");

    public SkylandTntBlockItemRenderer() {
        super(new SkylandTntBlockItemModel(SkylandTntBlockItemModel.getTexturedModelData().createModel()));
    }

    @Override
    public Identifier getTexture(ItemStack itemStack, @Nullable LivingEntity livingEntity, ModelTransformationMode modelTransformationMode) {
        return TEXTURE;
    }
}
