package de.takacick.onesuperblock.mixin;

import de.takacick.onesuperblock.registry.item.Super;
import de.takacick.onesuperblock.registry.particles.RainbowBlockParticle;
import net.minecraft.client.particle.BlockDustParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.BlockStateParticleEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(BlockDustParticle.Factory.class)
public abstract class BlockDustParticleMixin {

    @Inject(method = "createParticle(Lnet/minecraft/particle/BlockStateParticleEffect;Lnet/minecraft/client/world/ClientWorld;DDDDDD)Lnet/minecraft/client/particle/Particle;", at = @At("HEAD"), cancellable = true)
    private void createParticle(BlockStateParticleEffect blockStateParticleEffect, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, CallbackInfoReturnable<Particle> info) {
        if (blockStateParticleEffect.getBlockState().getBlock() instanceof Super) {
            info.setReturnValue(new RainbowBlockParticle(clientWorld, 0, false, d, e, f, g, h, i, blockStateParticleEffect.getBlockState()));
        }
    }
}
