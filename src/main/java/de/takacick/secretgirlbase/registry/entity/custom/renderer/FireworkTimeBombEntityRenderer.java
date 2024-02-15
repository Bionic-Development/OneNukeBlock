package de.takacick.secretgirlbase.registry.entity.custom.renderer;

import de.takacick.secretgirlbase.SecretGirlBase;
import de.takacick.secretgirlbase.registry.entity.custom.FireworkTimeBombEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class FireworkTimeBombEntityRenderer extends EntityRenderer<FireworkTimeBombEntity> {

    private static final Identifier TEXTURE = new Identifier(SecretGirlBase.MOD_ID, "textures/entity/firework_time_bomb.png");
    private final ModelPart model;
    private final ModelPart rope;

    public FireworkTimeBombEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = getTexturedModelData().createModel();
        this.rope = this.model.getChild("bone").getChild("rope");
    }

    public void render(FireworkTimeBombEntity fireworkTimeBombEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        if (fireworkTimeBombEntity.isExploding()) {
            return;
        }

        matrixStack.push();
        matrixStack.scale(-1, -1, 1);
        matrixStack.translate(0, -1.501, 0);

        this.rope.visible = fireworkTimeBombEntity.hasPassengers();

        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(getTexture(fireworkTimeBombEntity)));
        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV);
        matrixStack.pop();

        super.render(fireworkTimeBombEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Identifier getTexture(FireworkTimeBombEntity paintEntity) {
        return TEXTURE;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create().uv(0, 23).cuboid(-7.0F, -32.0F, -7.0F, 14.0F, 32.0F, 14.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 17.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-11.0F, -32.0F, -11.0F, 22.0F, 0.0F, 22.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData bone_r1 = bone.addChild("bone_r1", ModelPartBuilder.create().uv(57, 1).cuboid(0.0F, -22.0F, -11.0F, 0.0F, 22.0F, 22.0F, new Dilation(0.0F)), ModelTransform.of(-11.0F, -32.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

        ModelPartData bone_r2 = bone.addChild("bone_r2", ModelPartBuilder.create().uv(57, 1).cuboid(0.0F, -22.0F, -11.0F, 0.0F, 22.0F, 22.0F, new Dilation(0.0F)), ModelTransform.of(11.0F, -32.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

        ModelPartData bone_r3 = bone.addChild("bone_r3", ModelPartBuilder.create().uv(57, 23).cuboid(-11.0F, -22.0F, 0.0F, 22.0F, 22.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -32.0F, 11.0F, 0.5236F, 0.0F, 0.0F));

        ModelPartData bone_r4 = bone.addChild("bone_r4", ModelPartBuilder.create().uv(57, 23).cuboid(-11.0F, -22.0F, 0.0F, 22.0F, 22.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -32.0F, -11.0F, -0.5236F, 0.0F, 0.0F));

        ModelPartData rope = bone.addChild("rope", ModelPartBuilder.create().uv(9, 0).cuboid(7.0F, -12.0F, 7.0F, 1.0F, 10.0F, 1.0F, new Dilation(0.0F))
                .uv(41, 55).cuboid(-8.0F, -19.0F, -8.0F, 16.0F, 7.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 112, 112);
    }
}
