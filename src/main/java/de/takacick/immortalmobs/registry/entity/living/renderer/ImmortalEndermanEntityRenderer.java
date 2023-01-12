package de.takacick.immortalmobs.registry.entity.living.renderer;

import de.takacick.immortalmobs.ImmortalMobs;
import de.takacick.immortalmobs.client.CustomLayers;
import de.takacick.immortalmobs.registry.entity.living.ImmortalEndermanEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EndermanEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class ImmortalEndermanEntityRenderer
        extends MobEntityRenderer<ImmortalEndermanEntity, EndermanEntityModel<ImmortalEndermanEntity>> {
    private static final Identifier TEXTURE = new Identifier("textures/entity/enderman/enderman.png");
    private static final Identifier EYES = new Identifier(ImmortalMobs.MOD_ID, "textures/entity/immortal_enderman_eyes.png");

    public ImmortalEndermanEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new EndermanEntityModel<>(context.getPart(EntityModelLayers.ENDERMAN)), 0.5f);
        this.addFeature(new ImmortalEyesFeatureRenderer<>(this, EYES));
    }

    @Override
    public Identifier getTexture(ImmortalEndermanEntity immortalEndermanEntity) {
        return TEXTURE;
    }

    @Nullable
    @Override
    protected RenderLayer getRenderLayer(ImmortalEndermanEntity immortalEndermanEntity, boolean showBody, boolean translucent, boolean showOutline) {
        return CustomLayers.IMMORTAL_CUTOUT.apply(getTexture(immortalEndermanEntity));
    }
}

