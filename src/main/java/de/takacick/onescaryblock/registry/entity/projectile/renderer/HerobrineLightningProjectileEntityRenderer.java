package de.takacick.onescaryblock.registry.entity.projectile.renderer;

import de.takacick.onescaryblock.registry.EntityRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

@Environment(value = EnvType.CLIENT)
public class HerobrineLightningProjectileEntityRenderer<T extends Entity>
        extends EntityRenderer<T> {

    public HerobrineLightningProjectileEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected int getBlockLight(T entity, BlockPos pos) {
        return 15;
    }

    @Override
    public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (entity.age < 2 && this.dispatcher.camera.getFocusedEntity().squaredDistanceTo(entity) < 12.25) {
            return;
        }
        matrices.push();
        matrices.translate(0, 0.125, 0);
        matrices.scale(1f, 1f, 1f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw()) - 90));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch()) + 90));

        matrices.scale(0.02f, 0.02f, 0.02f);
        MinecraftClient.getInstance().getEntityRenderDispatcher().render(new LightningEntity(EntityRegistry.HEROBRINE_LIGHTNING_BOLT, MinecraftClient.getInstance().world), BlockPos.ORIGIN.getX(), BlockPos.ORIGIN.getY(), BlockPos.ORIGIN.getZ(), 0, 0, matrices, vertexConsumers, light);

        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(Entity entity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }
}

