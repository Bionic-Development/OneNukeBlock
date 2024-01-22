package de.takacick.illegalwars.registry.entity.living.renderer;

import de.takacick.illegalwars.IllegalWars;
import de.takacick.illegalwars.registry.entity.living.SharkEntity;
import de.takacick.illegalwars.registry.entity.living.model.SharkEntityModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class SharkEntityRenderer
extends MobEntityRenderer<SharkEntity, SharkEntityModel<SharkEntity>> {
    private static final Identifier TEXTURE = new Identifier(IllegalWars.MOD_ID, "textures/entity/shark.png");

    public SharkEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new SharkEntityModel<>(SharkEntityModel.getTexturedModelData().createModel()), 0.7f);
    }

    @Override
    public Identifier getTexture(SharkEntity sharkEntity) {
        return TEXTURE;
    }
}

