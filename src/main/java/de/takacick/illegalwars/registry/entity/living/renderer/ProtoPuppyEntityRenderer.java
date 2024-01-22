package de.takacick.illegalwars.registry.entity.living.renderer;

import de.takacick.illegalwars.IllegalWars;
import de.takacick.illegalwars.registry.entity.living.model.ProtoPuppyEntityModel;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.util.Identifier;

public class ProtoPuppyEntityRenderer extends MobEntityRenderer<WolfEntity, ProtoPuppyEntityModel<WolfEntity>> {
    private static final Identifier WILD_TEXTURE = new Identifier(IllegalWars.MOD_ID, "textures/entity/proto.png");

    public ProtoPuppyEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new ProtoPuppyEntityModel<>(ProtoPuppyEntityModel.getTexturedModelData().createModel()), 0.5f);
    }

    @Override
    protected float getAnimationProgress(WolfEntity wolfEntity, float f) {
        return wolfEntity.getTailAngle();
    }

    @Override
    public void render(WolfEntity wolfEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(wolfEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(WolfEntity wolfEntity) {
        return WILD_TEXTURE;
    }
}

