package de.takacick.onesuperblock.registry.entity.projectiles.renderer;

import de.takacick.onesuperblock.registry.entity.projectiles.SuperWitherSkullEntity;
import de.takacick.superitems.client.CustomLayers;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.SkullEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class SuperWitherSkullEntityRenderer
        extends EntityRenderer<SuperWitherSkullEntity> {
    private static final Identifier TEXTURE = new Identifier("textures/entity/wither/wither.png");
    private final SkullEntityModel model;

    public SuperWitherSkullEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new SkullEntityModel(context.getPart(EntityModelLayers.WITHER_SKULL));
    }

    @Override
    protected int getBlockLight(SuperWitherSkullEntity witherSkullEntity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public void render(SuperWitherSkullEntity witherSkullEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.scale(-1.0f, -1.0f, 1.0f);
        float h = MathHelper.lerpAngle(witherSkullEntity.prevYaw, witherSkullEntity.getYaw(), g);
        float j = MathHelper.lerp(g, witherSkullEntity.prevPitch, witherSkullEntity.getPitch());
        this.model.setHeadRotation(0.0f, h, j);
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(this.model.getLayer(this.getTexture(witherSkullEntity)));
        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
        vertexConsumer = vertexConsumerProvider.getBuffer(CustomLayers.RAINBOW_OVERLAY.apply(this.getTexture(witherSkullEntity)));
        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
        matrixStack.pop();
        super.render(witherSkullEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(SuperWitherSkullEntity witherSkullEntity) {
        return TEXTURE;
    }
}

