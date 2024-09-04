package de.takacick.onenukeblock.registry.particles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;

public record EntityParticleEffect(ParticleType<EntityParticleEffect> particleType,
                                   int owner) implements ParticleEffect {

    public static MapCodec<EntityParticleEffect> createCodec(ParticleType<EntityParticleEffect> type) {
        return Codec.INT.xmap(state -> new EntityParticleEffect(type, state), effect -> effect.owner).fieldOf("entity");
    }

    public static PacketCodec<? super RegistryByteBuf, EntityParticleEffect> createPacketCodec(ParticleType<EntityParticleEffect> type) {
        return PacketCodecs.INTEGER.xmap(owner -> new EntityParticleEffect(type, owner), effect -> effect.owner);
    }

    public ParticleType<EntityParticleEffect> getType() {
        return particleType();
    }
}
