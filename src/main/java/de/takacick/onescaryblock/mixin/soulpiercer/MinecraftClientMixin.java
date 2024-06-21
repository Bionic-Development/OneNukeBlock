package de.takacick.onescaryblock.mixin.soulpiercer;

import com.llamalad7.mixinextras.sugar.Local;
import de.takacick.onescaryblock.registry.ParticleRegistry;
import de.takacick.onescaryblock.registry.item.SoulPiercer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
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

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow
    @Nullable
    public ClientWorld world;

    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    @Inject(method = "handleBlockBreaking", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleManager;addBlockBreakingParticles(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)V", shift = At.Shift.AFTER))
    public void handleBlockBreaking(boolean breaking, CallbackInfo info, @Local(ordinal = 0) BlockPos blockPos) {
        if (this.player.getMainHandStack().getItem() instanceof SoulPiercer) {
            Vec3d pos = blockPos.toCenterPos();
            Random random = this.player.getRandom();
            for (int i = 0; i < 1 + random.nextBetween(1, 2); ++i) {
                Vec3d vel = new Vec3d(random.nextGaussian(), random.nextGaussian(), random.nextGaussian()).normalize().multiply(0.1 + world.getRandom().nextDouble() * 0.5);

                world.addParticle(ParticleRegistry.SOUL, true, pos.getX() + vel.getX(), pos.getY() + vel.getY(), pos.getZ() + vel.getZ(),
                        vel.getX() * 0.05, vel.getY() * 0.05, vel.getZ() * 0.05);

                if (random.nextDouble() <= 0.6) {
                    this.world.playSound(pos.getX() + vel.getX(), pos.getY() + vel.getY(), pos.getZ() + vel.getZ(),
                            SoundEvents.PARTICLE_SOUL_ESCAPE, SoundCategory.HOSTILE, 4.0f,
                            (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                }
            }
        }
    }
}
