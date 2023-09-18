package de.takacick.onesuperblock.mixin;

import de.takacick.onesuperblock.registry.block.SuperOreBlock;
import de.takacick.onesuperblock.registry.item.Super;
import de.takacick.onesuperblock.registry.particles.RainbowBlockParticle;
import net.minecraft.block.BlockState;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ParticleManager.class)
public abstract class ParticleManagerMixin {

    @Shadow
    protected ClientWorld world;

    @Shadow
    public abstract void addParticle(Particle particle);

    @Shadow
    @Final
    private Random random;

    @Inject(method = "addBlockBreakParticles", at = @At("HEAD"), cancellable = true)
    private void addBlockBreakParticles(BlockPos pos, BlockState state, CallbackInfo info) {
        if (state.getBlock() instanceof Super) {
            VoxelShape voxelShape = state.getOutlineShape(this.world, pos);
            voxelShape.forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> {
                double d = Math.min(1.0, maxX - minX);
                double e = Math.min(1.0, maxY - minY);
                double f = Math.min(1.0, maxZ - minZ);
                int i = Math.max(2, MathHelper.ceil(d / 0.25));
                int j = Math.max(2, MathHelper.ceil(e / 0.25));
                int k = Math.max(2, MathHelper.ceil(f / 0.25));
                for (int l = 0; l < i; ++l) {
                    for (int m = 0; m < j; ++m) {
                        for (int n = 0; n < k; ++n) {
                            double g = ((double) l + 0.5) / (double) i;
                            double h = ((double) m + 0.5) / (double) j;
                            double o = ((double) n + 0.5) / (double) k;
                            double p = g * d + minX;
                            double q = h * e + minY;
                            double r = o * f + minZ;
                            this.addParticle(new RainbowBlockParticle(this.world, 0, false, (double) pos.getX() + p, (double) pos.getY() + q, (double) pos.getZ() + r, g - 0.5, h - 0.5, o - 0.5, state, pos));
                        }
                    }
                }
            });
            info.cancel();
        }
    }

    @Inject(method = "addBlockBreakingParticles", at = @At("HEAD"), cancellable = true)
    private void addBlockBreakingParticles(BlockPos pos, Direction direction, CallbackInfo info) {
        BlockState blockState = this.world.getBlockState(pos);
        if (blockState.getBlock() instanceof Super) {
            int i = pos.getX();
            int j = pos.getY();
            int k = pos.getZ();
            float f = 0.1f;
            Box box = blockState.getOutlineShape(this.world, pos).getBoundingBox();
            double d = (double) i + this.random.nextDouble() * (box.maxX - box.minX - (double) 0.2f) + (double) 0.1f + box.minX;
            double e = (double) j + this.random.nextDouble() * (box.maxY - box.minY - (double) 0.2f) + (double) 0.1f + box.minY;
            double g = (double) k + this.random.nextDouble() * (box.maxZ - box.minZ - (double) 0.2f) + (double) 0.1f + box.minZ;
            if (direction == Direction.DOWN) {
                e = (double) j + box.minY - (double) 0.1f;
            }
            if (direction == Direction.UP) {
                e = (double) j + box.maxY + (double) 0.1f;
            }
            if (direction == Direction.NORTH) {
                g = (double) k + box.minZ - (double) 0.1f;
            }
            if (direction == Direction.SOUTH) {
                g = (double) k + box.maxZ + (double) 0.1f;
            }
            if (direction == Direction.WEST) {
                d = (double) i + box.minX - (double) 0.1f;
            }
            if (direction == Direction.EAST) {
                d = (double) i + box.maxX + (double) 0.1f;
            }
            this.addParticle(new RainbowBlockParticle(this.world, 0, false, d, e, g, 0.0, 0.0, 0.0, blockState, pos).move(0.2f).scale(0.6f));
            info.cancel();
        }
    }
}
