package de.takacick.upgradebody.registry.entity.living.renderer;

import de.takacick.upgradebody.registry.ItemRegistry;
import de.takacick.upgradebody.registry.entity.living.SentientDesertPyramidEntity;
import de.takacick.upgradebody.registry.entity.living.model.ItemEntityModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;

public class SentientDesertPyramidEntityRenderer extends MobEntityRenderer<SentientDesertPyramidEntity, ItemEntityModel<SentientDesertPyramidEntity>> {

    public SentientDesertPyramidEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new ItemEntityModel<>(ItemRegistry.SENTIENT_DESERT_TEMPLE.getDefaultStack(), 0f, 180f, -4.5f, -0.5f, true, 32f), 0f);
    }

    public Identifier getTexture(SentientDesertPyramidEntity sentientDesertPyramidEntity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }
}
