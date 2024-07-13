package de.takacick.onegirlboyblock.client.item.renderer;

import de.takacick.onegirlboyblock.client.item.ItemParticleHelper;
import de.takacick.onegirlboyblock.client.item.ItemParticleManager;
import de.takacick.onegirlboyblock.registry.ParticleRegistry;
import de.takacick.utils.item.client.render.ItemRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

public class StrawberryShortcakeItemRenderer extends ItemRenderer {

    public StrawberryShortcakeItemRenderer() {

    }

    @Override
    public void render(ItemStack itemStack, @Nullable LivingEntity livingEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, ModelTransformationMode modelTransformationMode, int light, int uv) {
        long time = getWorldTime(itemStack, livingEntity, modelTransformationMode);

        MinecraftClient client = MinecraftClient.getInstance();
        ItemParticleHelper helper = ItemParticleManager.tick(itemStack, time);

        if (helper.isTicking()) {
            Random random = client.world.getRandom();

            for (int i = 0; i < 2; i++) {
                Vec3d vector = new Vec3d(random.nextGaussian(), random.nextGaussian(), random.nextGaussian()).normalize().multiply(0.5f);

                Vec3d offset = new Vec3d(0f, 0.5f, 0f).add(vector);
                Particle particle = client.particleManager.createParticle(ParticleRegistry.SUGAR, offset.getX(), offset.getY(), offset.getZ(), vector.getX(), vector.getY(), vector.getZ());
                helper.addParticle(particle);
            }
        }

        matrixStack.push();
        matrixStack.translate(0.5, 0, 0.5);
        MatrixStack.Entry entry = matrixStack.peek();
        helper.renderParticles(
                client.gameRenderer.getLightmapTextureManager(),
                client.gameRenderer.getCamera(),
                entry,
                modelTransformationMode,
                tickDelta
        );
        matrixStack.pop();
    }

    @Override
    public Identifier getTexture(ItemStack itemStack, @Nullable LivingEntity livingEntity, ModelTransformationMode modelTransformationMode) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }
}
