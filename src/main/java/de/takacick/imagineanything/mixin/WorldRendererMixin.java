package de.takacick.imagineanything.mixin;

import de.takacick.imagineanything.registry.particles.goop.GoopDropParticleEffect;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Shadow
    private @Nullable ClientWorld world;

    @Inject(method = "processWorldEvent", at = @At("HEAD"), cancellable = true)
    public void processWorldEvent(int eventId, BlockPos pos, int data, CallbackInfo info) {

        if (eventId == 132391834) {
            BlockState blockState = world.getBlockState(pos);

            for (int x = 0; x < 13; x++) {
                double d = pos.getX() + 0.5f + 0.6 * world.getRandom().nextGaussian();
                double e = pos.getY() + 0.5f + 0.6 * world.getRandom().nextGaussian();
                double f = pos.getZ() + 0.5f + 0.6 * world.getRandom().nextGaussian();
                world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, blockState), d, e, f,
                        MathHelper.nextBetween(world.getRandom(), -1.0F, 1.0F) * 0.083333336F,
                        0.05000000074505806D, MathHelper.nextBetween(world.getRandom(), -1.0F, 1.0F) * 0.083333336F);
            }
            this.world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, blockState.getSoundGroup().getHitSound(), SoundCategory.BLOCKS, 1.0f, 1.0f, false);
            info.cancel();
        } else if (eventId == 1853928903) {
            for (int i = 0; i < world.getRandom().nextInt(15) + 15; i++)
                world.addParticle(new GoopDropParticleEffect(new Vec3f(Vec3d.unpackRgb(0x413024)), (float) (world.getRandom().nextDouble() * 3)),
                        pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, world.getRandom().nextGaussian() * 0.4, world.getRandom().nextDouble() * 0.25, world.getRandom().nextGaussian() * 0.4);
            world.addParticle(new GoopDropParticleEffect(new Vec3f(Vec3d.unpackRgb(0x413024)), (float) (world.getRandom().nextDouble() * 3)),
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, -1, 0);
        }
    }
}
