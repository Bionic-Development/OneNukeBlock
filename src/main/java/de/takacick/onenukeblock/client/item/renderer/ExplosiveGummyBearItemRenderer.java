package de.takacick.onenukeblock.client.item.renderer;

import de.takacick.onenukeblock.OneNukeBlock;
import de.takacick.onenukeblock.client.item.ItemParticleHelper;
import de.takacick.onenukeblock.client.item.ItemParticleManager;
import de.takacick.onenukeblock.client.item.model.ExplosiveGummyBearItemModel;
import de.takacick.onenukeblock.registry.ParticleRegistry;
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

public class ExplosiveGummyBearItemRenderer extends ItemModelRenderer<ExplosiveGummyBearItemModel> {

    public static final Identifier TEXTURE = Identifier.of(OneNukeBlock.MOD_ID, "textures/entity/explosive_gummy_bear.png");

    public ExplosiveGummyBearItemRenderer() {
        super(new ExplosiveGummyBearItemModel(ExplosiveGummyBearItemModel.getTexturedModelData().createModel()));
    }

    @Override
    public void render(ItemStack itemStack, @Nullable LivingEntity livingEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, ModelTransformationMode modelTransformationMode, int light, int overlay) {
        super.render(itemStack, livingEntity, tickDelta, matrices, vertexConsumers, modelTransformationMode, light, overlay);

        long time = getWorldTime(itemStack, livingEntity, modelTransformationMode);

        MinecraftClient client = MinecraftClient.getInstance();
        ItemParticleHelper helper = ItemParticleManager.tick(itemStack, time);

        if (helper.isTicking()) {
            Random random = client.world.getRandom();

            if (random.nextDouble() <= 0.7) {
                for (int i = 0; i < 1; i++) {
                    Vec3d vector = new Vec3d(random.nextGaussian(), random.nextDouble(), random.nextGaussian()).normalize();

                    Vec3d offset = vector.multiply(4f / 16f, 10f / 16f, 4f / 16f);

                    Particle particle = client.particleManager.createParticle(ParticleRegistry.SMOKE, offset.getX(), offset.getY(), offset.getZ(), vector.getX() * 0.001, vector.getY() * 0.01, vector.getZ() * 0.001);
                    helper.addParticle(particle);
                }
            }
        }

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
