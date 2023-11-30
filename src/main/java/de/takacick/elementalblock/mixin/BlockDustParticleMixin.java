package de.takacick.elementalblock.mixin;

import de.takacick.elementalblock.registry.ItemRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.particle.BlockDustParticle;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockDustParticle.class)
public abstract class BlockDustParticleMixin extends SpriteBillboardParticle {

    protected BlockDustParticleMixin(ClientWorld clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f);
    }

    @Inject(method = "<init>(Lnet/minecraft/client/world/ClientWorld;DDDDDDLnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)V", at = @At("TAIL"))
    public void matchesType(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, BlockState state, BlockPos blockPos, CallbackInfo ci) {
        if (state.isOf(ItemRegistry.WATER_BLOCK)) {
            if (world.getBiome(blockPos).hasKeyAndValue()) {
                int i = world.getBiome(blockPos).value().getWaterColor();
                this.red *= (float) (i >> 16 & 0xFF) / 255.0f;
                this.green *= (float) (i >> 8 & 0xFF) / 255.0f;
                this.blue *= (float) (i & 0xFF) / 255.0f;
            }
        }
    }
}