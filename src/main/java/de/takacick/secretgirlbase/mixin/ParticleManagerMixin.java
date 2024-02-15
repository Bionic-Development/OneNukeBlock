package de.takacick.secretgirlbase.mixin;

import de.takacick.secretgirlbase.registry.block.MagicFlowerDoorPart;
import net.minecraft.block.BlockState;
import net.minecraft.client.particle.BlockDustParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleManager.class)
public abstract class ParticleManagerMixin {

    @Shadow
    @Final
    private Random random;

    @Shadow
    protected ClientWorld world;

    @Shadow
    public abstract void addParticle(Particle particle);

    @Inject(method = "addBlockBreakingParticles", at = @At("HEAD"), cancellable = true)
    private void setBlockBreakingInfo(BlockPos pos, Direction direction, CallbackInfo info) {
        BlockState blockState = this.world.getBlockState(pos);
        if (!(blockState.getBlock() instanceof MagicFlowerDoorPart)) {
            return;
        }
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();

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
        this.addParticle(new BlockDustParticle(this.world, d, e, g, 0.0, 0.0, 0.0, blockState, pos).move(0.2f).scale(0.6f));
        info.cancel();
    }
}
