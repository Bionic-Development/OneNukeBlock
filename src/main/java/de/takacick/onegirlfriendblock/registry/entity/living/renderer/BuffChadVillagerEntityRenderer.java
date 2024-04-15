package de.takacick.onegirlfriendblock.registry.entity.living.renderer;

import de.takacick.onegirlfriendblock.OneGirlfriendBlock;
import de.takacick.onegirlfriendblock.registry.entity.living.BuffChadVillagerEntity;
import de.takacick.onegirlfriendblock.registry.entity.living.model.BuffChadVillagerEntityModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class BuffChadVillagerEntityRenderer extends MobEntityRenderer<BuffChadVillagerEntity, BuffChadVillagerEntityModel<BuffChadVillagerEntity>> {
    private static final Identifier TEXTURE = new Identifier(OneGirlfriendBlock.MOD_ID, "textures/entity/buff_chad_villager.png");

    public BuffChadVillagerEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new BuffChadVillagerEntityModel<>(BuffChadVillagerEntityModel.getTexturedModelData().createModel()), 0.65f);
    }

    @Override
    public Identifier getTexture(BuffChadVillagerEntity buffChadVillagerEntity) {
        return TEXTURE;
    }

    @Override
    protected void setupTransforms(BuffChadVillagerEntity buffChadVillagerEntity, MatrixStack matrixStack, float f, float g, float h) {
        super.setupTransforms(buffChadVillagerEntity, matrixStack, f, g, h);
        if ((double) buffChadVillagerEntity.limbAnimator.getSpeed() < 0.01) {
            return;
        }
        float i = 13.0f;
        float j = buffChadVillagerEntity.limbAnimator.getPos(h) + 6.0f;
        float k = (Math.abs(j % 13.0f - 6.5f) - 3.25f) / 3.25f;
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(6.5f * k));
    }
}

