package de.takacick.tinyhouse.registry.entity.living.renderer;

import com.google.common.collect.Maps;
import de.takacick.tinyhouse.TinyHouse;
import de.takacick.tinyhouse.registry.entity.living.RatEntity;
import de.takacick.tinyhouse.registry.entity.living.model.RatEntityModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.Locale;
import java.util.Map;

public class RatEntityRenderer extends MobEntityRenderer<RatEntity, RatEntityModel<RatEntity>> {
    private static final Identifier TEXTURE = new Identifier(TinyHouse.MOD_ID, "textures/entity/rat/rat_brown.png");

    private static final Map<RatEntity.Variant, Identifier> TEXTURES = Util.make(Maps.newHashMap(), variants -> {
        for (RatEntity.Variant variant : RatEntity.Variant.values()) {
            variants.put(variant, new Identifier(TinyHouse.MOD_ID, String.format(Locale.ROOT, "textures/entity/rat/rat_%s.png", variant.getName())));
        }
    });

    public RatEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new RatEntityModel<>(RatEntityModel.getTexturedModelData().createModel()), 0.3f);
    }

    @Override
    protected float getLyingAngle(RatEntity ratEntity) {
        return 180.0f;
    }

    @Override
    public Identifier getTexture(RatEntity ratEntity) {
        return TEXTURES.getOrDefault(ratEntity.getVariant(), TEXTURE);
    }
}

