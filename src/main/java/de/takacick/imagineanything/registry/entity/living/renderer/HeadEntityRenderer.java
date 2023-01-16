package de.takacick.imagineanything.registry.entity.living.renderer;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import de.takacick.imagineanything.registry.entity.living.HeadEntity;
import de.takacick.imagineanything.registry.entity.living.model.HeadEntityModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.DynamicSerializableUuid;

import java.util.Map;

public class HeadEntityRenderer extends LivingEntityRenderer<HeadEntity, HeadEntityModel<HeadEntity>> {

    public HeadEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new HeadEntityModel<>(HeadEntityModel.getTexturedModelData().createModel()), 0.2f);
    }

    @Override
    public Identifier getTexture(HeadEntity headEntity) {

        Identifier identifier = DefaultSkinHelper.getTexture();
        if (headEntity.getGameProfile() == null) {
            return identifier;
        }
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = minecraftClient.getSkinProvider().getTextures(headEntity.getGameProfile());
        if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
            return minecraftClient.getSkinProvider().loadSkin(map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
        }
        return DefaultSkinHelper.getTexture(DynamicSerializableUuid.getUuidFromProfile(headEntity.getGameProfile()));
    }
}

