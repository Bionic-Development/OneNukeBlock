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

public class GlitterBladeItemRenderer extends ItemRenderer {

    public GlitterBladeItemRenderer() {

    }

    @Override
    public void render(ItemStack itemStack, @Nullable LivingEntity livingEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, ModelTransformationMode modelTransformationMode, int light, int uv) {
        long time = getWorldTime(itemStack, livingEntity, modelTransformationMode);

        MinecraftClient client = MinecraftClient.getInstance();
        ItemParticleHelper helper = ItemParticleManager.tick(itemStack, time);

        if (helper.isTicking()) {
            Random random = client.world.getRandom();

            for (int i = 0; i < 1; i++) {
                Vec3d vector = new Vec3d(random.nextGaussian(), random.nextDouble(), random.nextGaussian()).normalize();

                Vec3d offset = new Vec3d(0f, 10f / 16f, 0f).add(vector.multiply(1.5f / 16f, 1f, 1.5f / 16f));

                Particle particle = client.particleManager.createParticle(ParticleRegistry.GLITTER_BLADE, offset.getX(), offset.getY(), offset.getZ(), vector.getX() * 0.05, vector.getY() * 0.05, vector.getZ() * 0.05);
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
