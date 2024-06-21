package de.takacick.onescaryblock.mixin;

import de.takacick.onescaryblock.registry.ParticleRegistry;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin implements SynchronousResourceReloader,
        AutoCloseable {

    @Shadow
    private @Nullable ClientWorld world;

    @Inject(method = "processWorldEvent", at = @At("HEAD"), cancellable = true)
    public void processWorldEvent(int eventId, BlockPos blockPos, int data, CallbackInfo info) {
        if (eventId == 236741293) {
            Random random = this.world.random;

            if (data == 1) {
                Vec3d pos = blockPos.toCenterPos();
                for (int i = 0; i < 70; ++i) {
                    Vec3d vel = new Vec3d(random.nextGaussian(), random.nextGaussian(), random.nextGaussian()).normalize().multiply(0.1 + world.getRandom().nextDouble() * 0.5);

                    world.addParticle(ParticleRegistry.SOUL, true,
                            pos.getX() + vel.getX(), pos.getY() + vel.getY(), pos.getZ() + vel.getZ(),
                            vel.getX(), vel.getY(), vel.getZ());


                    if (random.nextDouble() <= 0.3) {
                        this.world.playSound(pos.getX() + vel.getX(), pos.getY() + vel.getY(), pos.getZ() + vel.getZ(),
                                SoundEvents.PARTICLE_SOUL_ESCAPE, SoundCategory.HOSTILE, 4.0f,
                                (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                    }
                }
                this.world.playSoundAtBlockCenter(blockPos, SoundEvents.ENTITY_BREEZE_LAND, SoundCategory.HOSTILE, 2f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.1f, false);

                info.cancel();
            }
        }
    }
}
