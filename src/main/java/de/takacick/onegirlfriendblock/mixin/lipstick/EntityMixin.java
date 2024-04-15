package de.takacick.onegirlfriendblock.mixin.lipstick;

import de.takacick.onegirlfriendblock.access.LivingProperties;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public abstract World getWorld();

    @Shadow
    private World world;

    @Shadow
    public abstract Vec3d getPos();

    @Shadow
    @Final
    protected Random random;

    @Inject(method = "playStepSound", at = @At("HEAD"))
    private void playStepSound(BlockPos blockPos, BlockState state, CallbackInfo info) {
        if (this.world.isClient) {
            if (this instanceof LivingProperties livingProperties && livingProperties.getLipstickStrength() > 0) {
                Vec3d pos = getPos().add(this.random.nextGaussian() * 0.03, 0, this.random.nextGaussian() * 0.03);
                this.world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_HONEY_BLOCK_SLIDE, SoundCategory.BLOCKS, 0.7f, 1f, false);
            }
        }
    }
}
