package de.takacick.onegirlboyblock.registry.particles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;

public record TetrisParticleEffect(ParticleType<TetrisParticleEffect> particleType,
                                   int tetrisVariant) implements ParticleEffect {

    public static MapCodec<TetrisParticleEffect> createCodec(ParticleType<TetrisParticleEffect> type) {
        return Codec.INT.xmap(state -> new TetrisParticleEffect(type, state), effect -> effect.tetrisVariant).fieldOf("tetrisVariant");
    }

    public static PacketCodec<? super RegistryByteBuf, TetrisParticleEffect> createPacketCodec(ParticleType<TetrisParticleEffect> type) {
        return PacketCodecs.INTEGER.xmap(owner -> new TetrisParticleEffect(type, owner), effect -> effect.tetrisVariant);
    }

    public ParticleType<TetrisParticleEffect> getType() {
        return particleType();
    }
}
