package de.takacick.onenukeblock.registry.entity.living.renderer.feature;

import de.takacick.onenukeblock.registry.entity.living.CreeperScientistEntity;
import de.takacick.onenukeblock.registry.entity.living.model.CreeperScientistEntityModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.render.entity.feature.EnergySwirlOverlayFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.util.Identifier;

@Environment(value = EnvType.CLIENT)
public class CreeperScientistChargeFeatureRenderer
        extends EnergySwirlOverlayFeatureRenderer<CreeperScientistEntity, CreeperScientistEntityModel<CreeperScientistEntity>> {
    private static final Identifier SKIN = Identifier.ofVanilla("textures/entity/creeper/creeper_armor.png");
    private final CreeperScientistEntityModel<CreeperScientistEntity> model;

    public CreeperScientistChargeFeatureRenderer(FeatureRendererContext<CreeperScientistEntity, CreeperScientistEntityModel<CreeperScientistEntity>> context, EntityModelLoader loader) {
        super(context);
        this.model = new CreeperScientistEntityModel<>(CreeperScientistEntityModel.getTexturedModelData(new Dilation(2.0f)).createModel());
    }

    @Override
    protected float getEnergySwirlX(float partialAge) {
        return partialAge * 0.01f;
    }

    @Override
    protected Identifier getEnergySwirlTexture() {
        return SKIN;
    }

    @Override
    protected EntityModel<CreeperScientistEntity> getEnergySwirlModel() {
        return this.model;
    }
}

