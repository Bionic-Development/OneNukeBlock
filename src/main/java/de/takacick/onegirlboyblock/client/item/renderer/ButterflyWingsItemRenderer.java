package de.takacick.onegirlboyblock.client.item.renderer;

import de.takacick.onegirlboyblock.OneGirlBoyBlock;
import de.takacick.onegirlboyblock.client.item.ItemParticleHelper;
import de.takacick.onegirlboyblock.client.item.ItemParticleManager;
import de.takacick.onegirlboyblock.client.item.model.ButterflyWingsItemModel;
import de.takacick.onegirlboyblock.registry.ParticleRegistry;
import de.takacick.utils.item.client.render.ItemModelRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

public class ButterflyWingsItemRenderer extends ItemModelRenderer<ButterflyWingsItemModel> {
    public static final Identifier TEXTURE = Identifier.of(OneGirlBoyBlock.MOD_ID, "textures/entity/butterfly_wings.png");

    public ButterflyWingsItemRenderer() {
        super(new ButterflyWingsItemModel(ButterflyWingsItemModel.getTexturedModelData().createModel()));
    }

    @Override
    public void render(ItemStack itemStack, @Nullable LivingEntity livingEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, ModelTransformationMode modelTransformationMode, int light, int overlay) {
        long time = getWorldTime(itemStack, livingEntity, modelTransformationMode);

        MinecraftClient client = MinecraftClient.getInstance();
        ItemParticleHelper helper = ItemParticleManager.tick(itemStack, time);

        if (helper.isTicking()) {
            Random random = client.world.getRandom();

            for (int i = 0; i < 1; i++) {
                Vec3d vector = new Vec3d(random.nextGaussian(), random.nextGaussian(), random.nextGaussian()).normalize();

                Vec3d offset = new Vec3d(0f, 0.5f, 0f).add(vector.multiply(0.5f, 0.5f, 1.5f / 16f));

                Particle particle = client.particleManager.createParticle(ParticleRegistry.BUTTERFLY_WINGS_GLITTER, offset.getX(), offset.getY(), offset.getZ() + 0.1, vector.getX() * 0.05, vector.getY() * 0.05, vector.getZ() * 0.05);
                helper.addParticle(particle);
            }
        }

        super.render(itemStack, livingEntity, tickDelta, matrices, vertexConsumers, modelTransformationMode, light, overlay);

        matrices.push();
        matrices.translate(0.5, 0, 0.5);
        MatrixStack.Entry entry = matrices.peek();
        helper.renderParticles(
                client.gameRenderer.getLightmapTextureManager(),
                client.gameRenderer.getCamera(),
                entry,
                modelTransformationMode,
                tickDelta
        );
        matrices.pop();
    }

    @Override
    public Identifier getTexture(ItemStack itemStack, @Nullable LivingEntity livingEntity, ModelTransformationMode modelTransformationMode) {
        return TEXTURE;
    }
}
