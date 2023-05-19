package de.takacick.heartmoney.mixin;

import de.takacick.heartmoney.registry.ParticleRegistry;
import de.takacick.heartmoney.registry.particles.ColoredParticleEffect;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Shadow
    private @Nullable ClientWorld world;

    @Shadow
    @Final
    private BufferBuilderStorage bufferBuilders;

    @Inject(method = "processWorldEvent", at = @At("HEAD"), cancellable = true)
    public void processWorldEvent(int eventId, BlockPos pos, int data, CallbackInfo info) {
        if (eventId == 456789129) {
            if (data == 0) {
                double g = pos.getX() + 0.5;
                double h = pos.getY() + 0.5;
                double j = pos.getZ() + 0.5;

                for (int i = 0; i < 8; ++i) {
                    world.addParticle(ParticleRegistry.HEART, true, g, h, j, world.getRandom().nextGaussian() * 0.15, world.getRandom().nextGaussian() * 0.15, world.getRandom().nextGaussian() * 0.15);
                }

                for (int i = 0; i < 5; ++i) {
                    world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_GLOW_SPARK, new Vec3f(Vec3d.unpackRgb(0xFF1313))), true, g, h, j, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3);
                }

                world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLOCK_AMETHYST_CLUSTER_HIT, SoundCategory.BLOCKS, 0.7f, 1f, true);
                info.cancel();
            } else if (data == 1) {
                double g = pos.getX() + 0.5;
                double h = pos.getY() + 0.5;
                double j = pos.getZ() + 0.5;

                for (int i = 0; i < 55; ++i) {
                    world.addParticle(ParticleRegistry.HEART, true, g, h, j, world.getRandom().nextGaussian() * 0.35, world.getRandom().nextGaussian() * 0.35, world.getRandom().nextGaussian() * 0.35);
                }

                for (int i = 0; i < 20; ++i) {
                    world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_GLOW_SPARK, new Vec3f(Vec3d.unpackRgb(0xFF1313))), true, g, h, j, world.getRandom().nextGaussian() * 0.35, world.getRandom().nextGaussian() * 0.35, world.getRandom().nextGaussian() * 0.35);
                }

                world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLOCK_AMETHYST_CLUSTER_HIT, SoundCategory.BLOCKS, 0.7f, 1f, true);
                world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.ENTITY_WARDEN_HEARTBEAT, SoundCategory.BLOCKS, 0.7f, 6f, true);
                world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.7f, 6f, true);
                info.cancel();
            } else if (data == 2) {
                double g = pos.getX() + 0.5;
                double h = pos.getY();
                double j = pos.getZ() + 0.5;
                for (int i = 0; i < 14; i++) {
                    world.addParticle(ParticleRegistry.HEART_FIRE,
                            g, h, j,
                            world.getRandom().nextGaussian() * 0.25, world.getRandom().nextDouble() * 0.10, world.getRandom().nextGaussian() * 0.25);
                }
            }
        }
    }
}
