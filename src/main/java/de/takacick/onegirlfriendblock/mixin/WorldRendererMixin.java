package de.takacick.onegirlfriendblock.mixin;

import de.takacick.onegirlfriendblock.registry.ParticleRegistry;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = WorldRenderer.class, priority = 999)
public abstract class WorldRendererMixin {

    @Shadow
    private @Nullable ClientWorld world;

    @Inject(method = "processWorldEvent", at = @At("HEAD"))
    public void processWorldEvent(int eventId, BlockPos blockPos, int data, CallbackInfo info) {
        if (eventId == 19238412) {
            Vec3d pos = Vec3d.ofCenter(blockPos);

            if (data == 0) {
                this.world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_CAT_AMBIENT, SoundCategory.BLOCKS, 1f, 1f + world.getRandom().nextFloat() * 0.2f, true);

                for (int i = 0; i < 10; ++i) {
                    double d = this.world.getRandom().nextGaussian() * 0.4;
                    double e = this.world.getRandom().nextGaussian() * 0.4;
                    double f = this.world.getRandom().nextGaussian() * 0.4;

                    this.world.addParticle(ParticleRegistry.FEATHER,
                            true, pos.getX() + d, pos.getY() + e, pos.getZ() + f, d * 0.4, e * 0.4, f * 0.4);
                }

                for (int i = 0; i < 2; ++i) {
                    double d = this.world.getRandom().nextGaussian() * 0.4;
                    double e = this.world.getRandom().nextGaussian() * 0.4;
                    double f = this.world.getRandom().nextGaussian() * 0.4;

                    this.world.addParticle(ParticleTypes.POOF,
                            true, pos.getX() + d, pos.getY() + e, pos.getZ() + f, 0, 0, 0);
                }
            }
        }
    }
}