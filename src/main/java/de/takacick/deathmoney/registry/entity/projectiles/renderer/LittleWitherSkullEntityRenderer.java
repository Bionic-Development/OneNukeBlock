package de.takacick.deathmoney.registry.entity.projectiles.renderer;

import de.takacick.deathmoney.registry.entity.projectiles.LittleWitherSkullEntity;
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

public class LittleWitherSkullEntityRenderer
        extends EntityRenderer<LittleWitherSkullEntity> {
    private static final Identifier INVULNERABLE_TEXTURE = new Identifier("textures/entity/wither/wither_invulnerable.png");
    private static final Identifier TEXTURE = new Identifier("textures/entity/wither/wither.png");
    private final SkullEntityModel model;

    public LittleWitherSkullEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new SkullEntityModel(context.getPart(EntityModelLayers.WITHER_SKULL));
    }

    @Override
    protected int getBlockLight(LittleWitherSkullEntity littleWitherSkullEntity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public void render(LittleWitherSkullEntity littleWitherSkullEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.scale(-1.0f, -1.0f, 1.0f);
        float h = MathHelper.lerpAngle(littleWitherSkullEntity.prevYaw, littleWitherSkullEntity.getYaw(), g);
        float j = MathHelper.lerp(g, littleWitherSkullEntity.prevPitch, littleWitherSkullEntity.getPitch());
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(this.model.getLayer(this.getTexture(littleWitherSkullEntity)));
        this.model.setHeadRotation(0.0f, h, j);
        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
        matrixStack.pop();
        super.render(littleWitherSkullEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(LittleWitherSkullEntity littleWitherSkullEntity) {
        return littleWitherSkullEntity.isCharged() ? INVULNERABLE_TEXTURE : TEXTURE;
    }
}

